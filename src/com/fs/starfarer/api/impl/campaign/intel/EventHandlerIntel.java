package com.fs.starfarer.api.impl.campaign.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CoreUITabId;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.IntelUIAPI;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.ui.AoTDColonyEventOutomeDP;
import data.kaysaar.aotd.vok.scripts.CoreUITracker;

import java.awt.*;

public class EventHandlerIntel extends BaseIntelPlugin{
    public MarketAPI data;
    public static Object BUTTON_EVENT = new Object();
    public EventHandlerIntel(MarketAPI marketAPI){
        this.data=marketAPI;
    }
    @Override
    public SectorEntityToken getMapLocation(SectorMapAPI map) {
        // The location on the map of the intel
       return data.getPrimaryEntity();
    }


    @Override
    public void createIntelInfo(TooltipMakerAPI info, ListInfoMode mode) {
        Color title = getTitleColor(mode);

        // Title of the intel
        info.addPara(getName(), title,0f);
        info.addPara("A major event has occurred in "+data.getName(),5f);

    }

    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
        info.addPara("An event has occurred in "+data.getName(),5f);
        if(AoTDColonyEventManager.getInstance().getOnGoingEvent()!=null){
            info.addPara("Your assistance is required",5f);
            info.addPara("You have "+ Misc.getStringForDays(Math.round(AoTDColonyEventManager.getInstance().getOnGoingEvent().daysToMakeDecision))+" to make decision",5f);
        }
        else{
            info.addPara("Event has been resolved",5f);
        }
        ButtonAPI button  = addGenericButton(info,width,"Resolve event", BUTTON_EVENT);
        button.setEnabled(AoTDColonyEventManager.getInstance().getOnGoingEvent()!=null);
        if(!button.isEnabled()){
            this.setImportant(false);
            if(!this.isEnding()&&!this.isEnded()){
                this.endAfterDelay(1);
            }

        }
        else{
            this.setImportant(true);
        }
    }



    @Override
    public String getIcon() {
        return Global.getSettings().getSpriteName("intel", "respite40");
    }

    @Override
    protected String getName() {
        return "Major Event  : "+ data.getName();
    }

    @Override
    public boolean doesButtonHaveConfirmDialog(Object buttonId) {
        return super.doesButtonHaveConfirmDialog(buttonId);
    }

    @Override
    public void buttonPressConfirmed(Object buttonId, IntelUIAPI ui) {
        if(buttonId== BUTTON_EVENT){
            ui.showDialog(null,new AoTDColonyEventOutomeDP(ui));
        }


    }

}
