package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import data.colonyevents.models.BaseEventCondition;

public class MiracleSeedFoodPenalty extends BaseEventCondition {



    @Override
    public void apply(String id) {
        super.apply(id);
        for (Industry industry : market.getIndustries()) {
            industry.getSupply(Commodities.FOOD).getQuantity().modifyFlat("aotd_miracle_penalty_food",-2,"Miracle Seed");
        }

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        for (Industry industry : market.getIndustries()) {
            industry.getSupply(Commodities.FOOD).getQuantity().unmodifyFlat("aotd_miracle_penalty_food");
        }

    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(365);
    }
}
