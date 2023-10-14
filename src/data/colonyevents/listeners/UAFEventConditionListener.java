package data.colonyevents.listeners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.PlayerMarketTransaction;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener;
import data.scripts.industry.UAFBakeryKnockOff;

public class UAFEventConditionListener implements ColonyInteractionListener {
    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        if(!market.getFactionId().equals("uaf")){
            CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
           float choco =  cargo.getCommodityQuantity(UAFBakeryKnockOff.Bakery_Product_1);
            float second =  cargo.getCommodityQuantity(UAFBakeryKnockOff.Bakery_Product_2);
            float third =  cargo.getCommodityQuantity(UAFBakeryKnockOff.Bakery_Product_1);
            if(choco!=0||second!=0||third!=0){
                Global.getSector().getMemory().set("$uaf_aotd_event",true);
                Global.getSector().getListenerManager().removeListenerOfClass(this.getClass());
            }
        }
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {

    }

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {

    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {

    }
}
