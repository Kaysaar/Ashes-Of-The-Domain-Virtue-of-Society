package data.colonyevents.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin2;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

import java.awt.*;

public class ImprovedAICoreAlgorythms extends BaseMarketConditionPlugin2 {

    @Override
    public void apply(String id) {
        super.apply(id);
        if(market.getAdmin()!=null&&market.getAdmin().isAICore()){
            market.getAccessibilityMod().modifyFlat("aotd_ai_bonus",0.1f,"Improved AI Algorithms");
            market.getStats().getDynamic().getStat(Stats.COMBAT_FLEET_SIZE_MULT).modifyMult("aotd_ai_bonus",1.3f,"Improved AI Algorithms");
            market.getStability().modifyFlat("aotd_ai_bonus",1f,"Improved AI Algorithms");
        }
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getAccessibilityMod().unmodifyFlat("aotd_ai_bonus");
        market.getStats().getDynamic().getStat(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyMult("aotd_ai_bonus");
        market.getStability().unmodifyFlat("aotd_ai_bonus");
    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);
        tooltip.addSectionHeading("Bonuses from AI Administrator", Alignment.MID,5f);
        tooltip.addPara("Increase accessibility by %s",5f, Color.ORANGE,"10%");
        tooltip.addPara("Increase fleet size by %s",5f, Color.ORANGE,"30%");
        tooltip.addPara("Increase stability by %s",5f,Color.ORANGE,"1");

    }

    @Override
    public boolean showIcon() {
        return market.getAdmin()!=null&&market.getAdmin().isAICore();
    }
}
