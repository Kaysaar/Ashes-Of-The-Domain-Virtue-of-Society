package data.colonyevents.listeners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import data.colonyevents.conditions.SpecialItemOnIndustryTracker;

public class AoTDTrackerInit implements EconomyTickListener {
    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI marketAPI : Global.getSector().getEconomy().getMarketsCopy()) {
            SpecialItemOnIndustryTracker.applyTrackerOnMarket(marketAPI);
        }
    }

    @Override
    public void reportEconomyMonthEnd() {

    }
}
