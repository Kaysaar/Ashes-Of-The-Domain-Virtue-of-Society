package data.scripts.industry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;

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
    public String getCanNotShutDownReason() {
        return "You can't shutdown this HQ branch, due to contract, that you have signed with Tri Tachyon investor for next "+(720-(int)daysSinceOpening)+" days.";
    }
}
