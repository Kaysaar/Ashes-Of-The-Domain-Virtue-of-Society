package data.colonyevents.listeners;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.util.Misc;

public class UAFMarketConditionEnforcer implements EconomyTickListener {
    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {
            if(!playerMarket.hasCondition("aotd_own_bakery")){
                playerMarket.addCondition("aotd_own_bakery");
            }
        }
    }

    @Override
    public void reportEconomyMonthEnd() {

    }
}
