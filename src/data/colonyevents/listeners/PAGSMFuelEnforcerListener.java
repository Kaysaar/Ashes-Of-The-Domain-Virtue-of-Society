package data.colonyevents.listeners;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.util.Misc;

public class PAGSMFuelEnforcerListener implements EconomyTickListener {
    @Override
    public void reportEconomyTick(int iterIndex) {
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {
            if(!playerMarket.hasCondition("PAGSM_Enforcer")){
                playerMarket.addCondition("PAGSM_Enforcer");
            }
        }
    }

    @Override
    public void reportEconomyMonthEnd() {

    }
}
