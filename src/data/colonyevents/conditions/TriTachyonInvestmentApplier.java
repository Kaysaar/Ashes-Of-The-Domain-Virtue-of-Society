package data.colonyevents.conditions;

import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.BaseEventCondition;

import java.awt.*;

public class TriTachyonInvestmentApplier extends BaseEventCondition {
    public float maxDaysOnMarket = 720f;

    @Override
    public void apply(String id) {
        super.apply(id);
        if(!isToBeRemoved)
        market.getIndustry(Industries.POPULATION).getUpkeep().modifyFlat("tri_tachyon_mendling", 20000, "Tri Tachyon's Cut");

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getIndustry(Industries.POPULATION).getUpkeep().unmodifyFlat("tri_tachyon_mendling");
        removeWhenPassCertainTime(maxDaysOnMarket);
    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);
        tooltip.addPara("Due to their investment in this colony currently Tri Tachyon takes around %s of credits per month.",10f, Color.ORANGE,""+ Misc.getDGSCredits(20000));
    }


    @Override
    public void advance(float amount) {
        super.advance(amount);
    }
    @Override
    public boolean showIcon() {
        return true;
    }
}
