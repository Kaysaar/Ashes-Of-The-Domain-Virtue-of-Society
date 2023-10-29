package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import data.colonyevents.models.BaseEventCondition;

public class FoodExperimentEventApplier extends BaseEventCondition {
    String foodExperiment = "food_exper";
    @Override
    public void apply(String id) {
        super.apply(id);
        if(getModId().contains("food_experiment_flowers")){
            market.getAccessibilityMod().modifyFlat(foodExperiment,0.20f,"Amusing Flowers");
        }
        if(getModId().contains("food_experiment_tree")){
            float income = market.getGrossIncome();
            market.getHazard().modifyFlat(foodExperiment,-0.35f,"Wonderful Trees");
        }
        if(getModId().contains("food_experiment_potato")){
            market.getIncomeMult().modifyPercent(foodExperiment,10,"Experimental Food");
            for (Industry industry : market.getIndustries()) {
                if(industry.getSpec().hasTag("farming")){
                    for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                        mutableCommodityQuantity.getQuantity().modifyFlat(foodExperiment,1,"Experimental Food");
                    }
                }
            }
        }
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getAccessibilityMod().unmodifyFlat(foodExperiment);
        market.getHazard().unmodifyFlat(foodExperiment);
        market.getIncomeMult().unmodifyPercent(foodExperiment);
        for (Industry industry : market.getIndustries()) {
            if(industry.getSpec().hasTag("farming")){
                for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                    mutableCommodityQuantity.getQuantity().unmodifyFlat(foodExperiment);
                }
            }
        }
    }



    @Override
    public void advance(float amount) {
        super.advance(amount);
    }
}
