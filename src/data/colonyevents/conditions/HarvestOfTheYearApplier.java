package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.AICoreAdminPluginImpl;
import com.fs.starfarer.api.impl.campaign.econ.AICoreAdmin;
import com.fs.starfarer.api.impl.campaign.econ.impl.TechMining;
import com.fs.starfarer.api.impl.campaign.skills.Hypercognition;
import data.colonyevents.models.BaseEventCondition;

public class HarvestOfTheYearApplier extends BaseEventCondition {
    public float days = 365;

    @Override
    public void apply(String id) {
        super.apply(id);
        for (Industry industry : market.getIndustries()) {
            if(industry.getSpec().hasTag("farming"))continue;
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                mutableCommodityQuantity.getQuantity().modifyFlat("harvest",1,"Harvest of Year");
            }
        }
        market.getStability().modifyFlat("harvest",1,"Harvest of Year");
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        for (Industry industry : market.getIndustries()) {
            if(industry.getSpec().hasTag("farming"))continue;
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                mutableCommodityQuantity.getQuantity().unmodifyFlat("harvest");
            }
        }
        market.getStability().unmodifyFlat("harvest");
        removeWhenPassCertainTime(days);
    }
}
