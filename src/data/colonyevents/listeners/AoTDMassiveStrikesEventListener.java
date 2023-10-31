package data.colonyevents.listeners;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.manager.AoTDColonyEventManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AoTDMassiveStrikesEventListener implements EconomyTickListener {
    public HashMap<String,Integer> marketsUnrest = new HashMap<>();
    @Override
    public void reportEconomyTick(int iterIndex) {

    }

    @Override
    public void reportEconomyMonthEnd() {
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(true)) {
            if(playerMarket.getStability().getModifiedInt()<=5&&!playerMarket.hasCondition("massive_strikes")&&!playerMarket.hasCondition("resolved_strikes")){
                if(marketsUnrest.get(playerMarket.getId())==null){
                    marketsUnrest.put(playerMarket.getId(),1);
                }
                else{
                   int current =  marketsUnrest.get(playerMarket.getId());
                   marketsUnrest.put(playerMarket.getId(),current+1);
                }
            }
            else{
                marketsUnrest.remove(playerMarket.getId());
            }
        }
        for (Map.Entry<String, Integer> entry : marketsUnrest.entrySet()) {
            if(entry.getValue()>=3&&entry.getValue()<6){
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("strike_warning",entry.getKey(),3);
            }
            if(entry.getValue()>=6){
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("strike_imminent",entry.getKey(),1);
            }
        }

    }

}
