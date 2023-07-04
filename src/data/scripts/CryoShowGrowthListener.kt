package data.scripts

import com.fs.starfarer.api.EveryFrameScript
import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.impl.campaign.ids.Industries
import com.fs.starfarer.api.ui.*
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.campaign.CampaignState
import com.fs.starfarer.campaign.ui.marketinfo.IndustryListPanel
import com.fs.state.AppDriver
import data.Ids.AoDIndustries
import data.plugins.AodCryosleeperPLugin
import data.scripts.industry.KaysaarCryorevival
import org.lazywizard.lazylib.MathUtils
import java.awt.AWTException
import java.awt.Robot
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

//This code has been reverse Enginnied from Lukas04 from Grand Colonies mod
class CryoShowGrowthListener : EveryFrameScript {

    var frames = 0
    var newIndustryPanel: CustomPanelAPI? = null


    companion object {
        var reset = false
        var rsetAll = false;

    }

    init {
        reset = false

    }

    override fun isDone(): Boolean {
        return false
    }

    override fun runWhilePaused(): Boolean {
        return true
    }



    override fun advance(amount: Float) {
        frames++
        frames = MathUtils.clamp(frames, 0, 15)

        //Returns if not paused, so that this doesnt run while the player isnt in any UI screen.
        if (!Global.getSector().isPaused)
        {
            return
        }

        //Wait a few frames to ensure there is no attempt at getting panels before the campaign ui loaded
        if (frames < 5) return



        var state = AppDriver.getInstance().currentState

        //Makes sure that the current state is the campaign state.
        if (state !is CampaignState) return

        //Gets the Campaigns Main UI Panel that all UI is attached to.
        // var core = state.core
        var core = invokeMethod("getCore", state)

        var managementPanel: UIPanelAPI? = null
        var industryPanel: UIPanelAPI? = null

        //The Following block gets both relevant panels by doing a deep dive through the core panel.
        //To find the required classes i used VisualVM, which made it a lot easier to find out where specificly the required panels can be accessed from.

        //Gets the main dialog panel.
        //var dialogCore = state.encounterDialog?.coreUI?.currentTab?.childrenCopy?.find { hasMethodOfName("getOutpostPanelParams", it) }

        var dialog = invokeMethod("getEncounterDialog", state)
        if (dialog != null)
        {
            var dCore = invokeMethod("getCoreUI", dialog)
            if (dCore != null)
            {
                var tab = invokeMethod("getCurrentTab", dCore!!)

                if (tab is UIPanelAPI)
                {
                    var dialogCore = tab.getChildrenCopy().find { hasMethodOfName("getOutpostPanelParams", it) }

                    if (dialogCore is UIPanelAPI)
                    {
                        //Gets the subpanel that holds the management panel
                        var dialogSubcore = dialogCore.getChildrenCopy().find { hasMethodOfName("showOverview", it) }

                        //Attempts to get the management panel
                        if (dialogSubcore is UIPanelAPI)
                        {
                            managementPanel = dialogSubcore.getChildrenCopy().find { hasMethodOfName("recreateWithEconUpdate", it) } as UIPanelAPI?
                            if (managementPanel == null)
                            {

                                return
                            }
                            if (managementPanel.getChildrenCopy().find { it == newIndustryPanel } == null)
                            {
                                industryPanel = managementPanel.getChildrenCopy().find { it is IndustryListPanel } as IndustryListPanel?
                            }
                            else
                            {

                            }
                        }
                    }
                }
            }
        }


        //There is a copy of the panel that can be found from the command window, to get this one we can do the same as above, but instead going from core directly, instead of the
        //secondary core attached to a dialog.
        if (core != null)
        {
            var tab = invokeMethod("getCurrentTab", core)
            if (tab is UIPanelAPI)
            {
                var intelCore = tab.getChildrenCopy()?.find { hasMethodOfName("getOutpostPanelParams", it) }
                if (intelCore is UIPanelAPI)
                {
                    //Gets the subpanel that holds the management panel
                    var intelSubcore = intelCore.getChildrenCopy().find { hasMethodOfName("showOverview", it) }

                    //Attempts to get the management panel
                    if (intelSubcore is UIPanelAPI)
                    {
                        managementPanel = intelSubcore.getChildrenCopy().find { hasMethodOfName("recreateWithEconUpdate", it) } as UIPanelAPI?
                        if (managementPanel == null)
                        {

                            return
                        }
                        if (managementPanel.getChildrenCopy().find { it == newIndustryPanel } == null)
                        {
                            industryPanel = managementPanel.getChildrenCopy().find { it is IndustryListPanel } as IndustryListPanel?
                        }
                    }
                }
            }
        }

        if (industryPanel != null && managementPanel != null) {
            //Gets the previous scroller if there was one before, this makes sure that you dont have to scroll back down each time the panel updates.f
            //Create the replacement panel.
            //Gets the current market
            industryPanel as IndustryListPanel
            var market = getPrivateVariable("market", industryPanel) as MarketAPI ?: return
            var prev =  Misc.MAX_COLONY_SIZE;
            if (market.hasCondition("cryo_in_system")&&market.hasIndustry(AoDIndustries.CRYOREVIVAL)) {
               var ind = market.getIndustry(AoDIndustries.CRYOREVIVAL) as KaysaarCryorevival;
                if(ind.canGrow){
                    Misc.MAX_COLONY_SIZE = AodCryosleeperPLugin.POP_SIZE
                }
            } else {
                Misc.MAX_COLONY_SIZE = AodCryosleeperPLugin.configSize
            }
            if(prev!=Misc.MAX_COLONY_SIZE){
                industryPanel.recreateOverviewNoEconStep()
            }

        }




    }


    //Used to be able to find specific files without having to reference their obfuscated class name.
    private fun hasMethodOfName(name: String, instance: Any): Boolean {
        val methodClass = Class.forName("java.lang.reflect.Method", false, Class::class.java.classLoader)
        val getNameMethod =
            MethodHandles.lookup().findVirtual(methodClass, "getName", MethodType.methodType(String::class.java))

        val instancesOfMethods: Array<out Any> = instance.javaClass.getDeclaredMethods()
        return instancesOfMethods.any { getNameMethod.invoke(it) == name }
    }

    //Required to execute obfuscated methods without referencing their obfuscated class name.
    private fun invokeMethod(methodName: String, instance: Any, vararg arguments: Any?): Any? {
        val methodClass = Class.forName("java.lang.reflect.Method", false, Class::class.java.classLoader)
        val getNameMethod =
            MethodHandles.lookup().findVirtual(methodClass, "getName", MethodType.methodType(String::class.java))
        val invokeMethod = MethodHandles.lookup().findVirtual(
            methodClass,
            "invoke",
            MethodType.methodType(Any::class.java, Any::class.java, Array<Any>::class.java)
        )

        var foundMethod: Any? = null
        for (method in instance::class.java.methods as Array<Any>) {
            if (getNameMethod.invoke(method) == methodName) {
                foundMethod = method
            }
        }

        return invokeMethod.invoke(foundMethod, instance, arguments)
    }

    fun setPrivateVariable(fieldName: String, instanceToModify: Any, newValue: Any?) {
        val fieldClass = Class.forName("java.lang.reflect.Field", false, Class::class.java.classLoader)
        val setMethod = MethodHandles.lookup()
            .findVirtual(fieldClass, "set", MethodType.methodType(Void.TYPE, Any::class.java, Any::class.java))
        val getNameMethod =
            MethodHandles.lookup().findVirtual(fieldClass, "getName", MethodType.methodType(String::class.java))
        val setAcessMethod = MethodHandles.lookup().findVirtual(
            fieldClass,
            "setAccessible",
            MethodType.methodType(Void.TYPE, Boolean::class.javaPrimitiveType)
        )

        val instancesOfFields: Array<out Any> = instanceToModify.javaClass.getDeclaredFields()
        for (obj in instancesOfFields) {
            setAcessMethod.invoke(obj, true)
            val name = getNameMethod.invoke(obj)
            if (name.toString() == fieldName) {
                setMethod.invoke(obj, instanceToModify, newValue)
            }
        }
    }

    //Required to get certain variables.
    private fun getPrivateVariable(fieldName: String, instanceToGetFrom: Any): Any? {
        val fieldClass = Class.forName("java.lang.reflect.Field", false, Class::class.java.classLoader)
        val getMethod = MethodHandles.lookup()
            .findVirtual(fieldClass, "get", MethodType.methodType(Any::class.java, Any::class.java))
        val getNameMethod =
            MethodHandles.lookup().findVirtual(fieldClass, "getName", MethodType.methodType(String::class.java))
        val setAcessMethod = MethodHandles.lookup().findVirtual(
            fieldClass,
            "setAccessible",
            MethodType.methodType(Void.TYPE, Boolean::class.javaPrimitiveType)
        )

        val instancesOfFields: Array<out Any> = instanceToGetFrom.javaClass.getDeclaredFields()
        for (obj in instancesOfFields) {
            setAcessMethod.invoke(obj, true)
            val name = getNameMethod.invoke(obj)
            if (name.toString() == fieldName) {
                return getMethod.invoke(obj, instanceToGetFrom)
            }
        }
        return null
    }

    //Extends the UI API by adding the required method to get the child objects of a panel, only when used within this class.
    private fun UIPanelAPI.getChildrenCopy(): List<UIComponentAPI> {
        return invokeMethod("getChildrenCopy", this) as List<UIComponentAPI>
    }

}



