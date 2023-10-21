package data.colonyevents.listeners;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.util.Misc;

public class AoTDCrestOfPhoenixListener implements EconomyTickListener {
    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {
            if(!playerMarket.hasCondition("crest_of_phoenix")){
                playerMarket.addCondition("crest_of_phoenix");
            }
        }
    }

    @Override
    public void reportEconomyMonthEnd() {

    }
}
