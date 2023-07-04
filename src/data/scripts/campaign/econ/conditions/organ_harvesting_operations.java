package data.scripts.campaign.econ.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseHazardCondition;
import com.fs.starfarer.api.impl.campaign.econ.impl.LightIndustry;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class organ_harvesting_operations extends BaseHazardCondition {
    public static float STABILITY_PENALTY = -2;
    @Override
    public void apply(String id) {
        market.getStability().modifyFlat(id,STABILITY_PENALTY,"Underworld organ harvesting opperations");
    }


    @Override

    public void unapply(String id) {
        market.getStability().unmodify(id);
    }
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);
        tooltip.addPara(
                "%s stability",
                10f,
                Misc.getHighlightColor(),
                "" + (int) STABILITY_PENALTY
        );
    }
}
