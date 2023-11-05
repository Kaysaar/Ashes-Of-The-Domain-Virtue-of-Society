package data.scripts.industry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class TriTachyonBranchHQ extends BaseIndustry {
    public float daysSinceOpening = 0.0f;
    @Override
    public void apply() {
        super.apply(true);
        float income = this.market.getGrossIncome();
        this.getUpkeep().modifyFlat("tri_tachyon_mendling",income*0.15f,"Tri Tachyon Cut");
        for (Industry industry : market.getIndustries()) {
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                industry.getSupply(mutableCommodityQuantity.getCommodityId()).getQuantity().modifyFlat("tri_tachyon_mendling",1,"Tri Tachyon Investment");
            }

        }
        this.market.getStability().modifyFlat("tri_tachyon_mendling",1,"Tri Tachyon Influence");
    }

    @Override
    public void unapply() {
        super.unapply();
        this.getUpkeep().unmodifyFlat("tri_tachyon_mendling");
        for (Industry industry : market.getIndustries()) {
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                industry.getSupply(mutableCommodityQuantity.getCommodityId()).getQuantity().unmodifyFlat("tri_tachyon_mendling");
            }

        }
        this.market.getStability().unmodifyFlat("tri_tachyon_mendling");
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        super.addPostDemandSection(tooltip, hasDemand, mode);
        tooltip.addSectionHeading("Tri Tachyon HQ Effects", Alignment.MID,10f);
        tooltip.addPara("Boost production of all industries by 1",10f);
        tooltip.addPara("Boost Stability by 1",10f);
        tooltip.addPara("Takes 15% of colony's income", Misc.getNegativeHighlightColor(),10f);
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        daysSinceOpening+= Global.getSector().getClock().convertToDays(amount);
    }

    @Override
    public boolean isAvailableToBuild() {
        return false;
    }

    @Override
    public boolean canShutDown() {
        return daysSinceOpening>=720;
    }

    @Override
    public boolean showWhenUnavailable() {
        return false;
    }

    @Override
    public String getCanNotShutDownReason() {
        return "You can't shutdown this HQ branch, due to contract, that you have signed with Tri Tachyon investor for next "+(720-(int)daysSinceOpening)+" days.";
    }
}
