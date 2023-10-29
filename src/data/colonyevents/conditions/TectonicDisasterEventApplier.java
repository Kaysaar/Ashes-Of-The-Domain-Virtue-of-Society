package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.campaign.listeners.ColonyDecivListener;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.intel.deciv.DecivTracker;
import data.colonyevents.models.BaseEventCondition;

import java.util.ArrayList;
import java.util.Random;

public class TectonicDisasterEventApplier extends BaseEventCondition implements EconomyTickListener {
    boolean validForCheck = false;
    @Override
    public void apply(String id) {
        super.apply(id);
        for (Industry industry : market.getIndustries()) {
            if(!industry.isFunctional())continue;
            if(industry.getSpecialItem()!=null&&industry.getSpecialItem().getId().equals(Items.MANTLE_BORE)){
                validForCheck = true;
            }
        }
        Global.getSector().getListenerManager().addListener(this);

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        Global.getSector().getListenerManager().removeListener(this);
    }



    @Override
    public void advance(float amount) {
        super.advance(amount);
    }

    @Override
    public void reportEconomyTick(int iterIndex) {

    }

    @Override
    public void reportEconomyMonthEnd() {
        if(validForCheck){
            validForCheck = false;
            int first_interval = getRandomNumber(1,100);
            if(first_interval<=20){
                int rand = getRandomNumber(1,100);
                if(rand>0&&rand<=5){
                    if(market.getSize()<=3){
                        DecivTracker.decivilize(this.market,true,true);
                    }
                    else{
                        market.setSize(market.getSize()-1);
                        market.getPopulation().setWeight(0f);
                    }
                }
                if(rand>5&&rand<=35){
                    ArrayList<String>industryIds = new ArrayList<>();
                    for (Industry industry : market.getIndustries()) {
                        if(industry.getSpec().hasTag("mining")){
                            industryIds.add(industry.getId());
                        }
                    }
                    for (String industryId : industryIds) {
                        market.removeIndustry(industryId,null,false);
                    }
                }
                if(rand>35&&rand<=100){
                    for (Industry industry : market.getIndustries()) {
                        if(industry.getSpec().hasTag("mining")){
                            industry.setDisrupted(365,false);
                        }
                    }

                }
            }


        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
