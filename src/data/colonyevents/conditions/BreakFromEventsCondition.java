package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.BaseHazardCondition;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.procgen.ConditionGenDataSpec;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.BaseEventCondition;

import java.util.Map;

public class BreakFromEventsCondition extends BaseEventCondition {
    public static float timeInBetween = 180f;
    public void apply(String id) {

    }

    public void unapply(String id) {

    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(timeInBetween);
    }

    @Override
    public boolean showIcon() {
        return false;
    }
}
