package data.plugins;


import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.listeners.ListenerManagerAPI;
import com.fs.starfarer.api.impl.campaign.intel.events.HostileActivityEventIntel;
import data.colonyevents.listeners.AoTDTrackerInit;
import data.colonyevents.listeners.UAFEventConditionListener;
import data.colonyevents.manager.AoTDColonyEventAssigner;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.listeners.AoTDColonyEventUIListener;
import data.scripts.ThetaCoreDeceptiveMeasuresFactor;
import data.scripts.ThetaCoreDeceptiveMeasuresFactorInserter;
import org.apache.log4j.Logger;

//import data.colonyevents.ui.ExampleIDP;


public class AoTDVoSModPlugin extends BaseModPlugin {

    private static Logger log = Global.getLogger(AoTDVoSModPlugin.class);

    private void setListenersIfNeeded() {
        ListenerManagerAPI l = Global.getSector().getListenerManager();
        if (!l.hasListenerOfClass(UAFEventConditionListener.class)) {
            if (!Global.getSector().getMemory().contains("$uaf_aotd_event")) {
                l.addListener(new UAFEventConditionListener());
            }
        }
        if (!l.hasListenerOfClass(AoTDColonyEventUIListener.class)) {
            l.addListener(new AoTDColonyEventUIListener());

        }
        l.addListener(new AoTDTrackerInit(),true);

    }

    @Override
    public void onNewGameAfterProcGen() {

        AoTDColonyEventManager.setInstance();
    }

    public void afterGameSave() {
        super.afterGameSave();
        AoTDColonyEventManager manager = AoTDColonyEventManager.getInstance();
        manager.saveData();
    }

    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        AoTDColonyEventManager.getInstance().onGameLoad(!newGame);

        if (!Global.getSector().hasScript(AoTDColonyEventAssigner.class)) {
            Global.getSector().addScript(new AoTDColonyEventAssigner());
            Global.getSector().removeTransientScriptsOfClass(AoTDColonyEventAssigner.class);
        }
        setListenersIfNeeded();
        Global.getSector().registerPlugin(new ThetaCoreCampaignImpl());
        if(!Global.getSector().getMemory().contains("$aotd_inserted_theta")){
            if(!Global.getSector().hasScript(ThetaCoreDeceptiveMeasuresFactorInserter.class)){
                Global.getSector().addScript(new ThetaCoreDeceptiveMeasuresFactorInserter());
            }
        }


    }


}
