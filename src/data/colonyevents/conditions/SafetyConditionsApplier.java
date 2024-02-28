package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.models.BaseEventCondition;

public class SafetyConditionsApplier extends BaseEventCondition {
    public float daysThatModifierlast = 180;
    @Override
    public void apply(String id) {
        super.apply(id);
        if(this.getModId().contains("safety_achieved")){
            for (Industry industry : market.getIndustries()) {
                if(industry.getSpec().hasTag("heavyindustry")||industry.getSpec().hasTag("lightindustry")){
                    industry.getUpkeep().modifyMultAlways("safety",1.1f,"Safetry Measures");
                }
            }
        }
        else{
            market.getStability().modifyFlat("safety",-1,"Unsafe Conditions");
        }

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        for (Industry industry : market.getIndustries()) {
            if(industry.getSpec().hasTag("heavyindustry")||industry.getSpec().hasTag("lightindustry")){
                industry.getUpkeep().unmodifyMult("safety");
            }
        }
        market.getStability().unmodifyFlat("safety");

    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);
        removeWhenPassCertainTime(daysThatModifierlast);
    }

}
