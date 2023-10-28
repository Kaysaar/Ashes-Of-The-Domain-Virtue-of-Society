package data.scripts.industry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;

public class UAFBakeryKnockOff extends BaseIndustry {
    public boolean isFromContract = false;

    public static final String Bakery_Product_1 = "uaf_bakery_choco_lava";
    public static final String Bakery_Product_2 = "uaf_bakery_product_2";
    public static final String Bakery_Product_3 = "uaf_bakery_product_3";
    @Override
    public String getCurrentName() {
        if(isFromContract){
            return "Flambé Delights";
        }
        return super.getCurrentName();
    }

    @Override
    public void apply() {
        super.apply(true);
        demand(Commodities.FOOD, 5);
        demand(Commodities.DOMESTIC_GOODS, 3);
        demand(Commodities.HEAVY_MACHINERY, 5);
        int maxDeficit = market.getSize()-3;
        if(isFromContract){
            supply(Bakery_Product_1, 9);
            supply(Bakery_Product_2, 4);
            supply(Bakery_Product_3, 4);
        }
        else{
            supply(Bakery_Product_1, 6);
            supply(Bakery_Product_2, 3);
            supply(Bakery_Product_3, 1);
        }

        Pair<String,Integer>deficit=getMaxDeficit(Commodities.FOOD,Commodities.DOMESTIC_GOODS,Commodities.HEAVY_MACHINERY);
        if (deficit.two > maxDeficit) deficit.two = maxDeficit;
        if(isFromContract){
            if(deficit.two!=0){
                this.getIncome().modifyFlat("aurelio",50000/deficit.two,"Aurelio Flambé Baking");
            }
            else{
                this.getIncome().modifyFlat("aurelio",50000,"Aurelio Flambé Baking");
            }
        }
        else{
            modifyIncome(this,this.market,"aotd_bakery_");
        }

    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        if(!isFromContract){
            tooltip.addPara("Slightly boost income of planet", Color.ORANGE,10f);
        }
    }

    @Override
    protected String getDescriptionOverride() {
        return "Aurelio Flambé's bakery, \"Flambé Delights™,\" serves up a delightful knockoff on the interdimensional confectionary trend from New Auroria. Step into a world where imagination knows no bounds. With a touch of enchantment and a pinch of [redacted], Flambé Delights transports you to a magical realm of pastries, bread, and sweets that are out of this world!";
    }

    @Override
    public boolean isAvailableToBuild() {
        return Global.getSector().getMemory().contains("$aotdxuaf_bakery");
    }

    @Override
    public boolean showWhenUnavailable() {
        return Global.getSector().getMemory().contains("$aotdxuaf_bakery");
    }

    @Override
    public void unapply() {
        super.unapply();
        String modId = "aotd_bakery_";
        this.getIncome().unmodifyFlat("aurelio");
        market.getIncomeMult().unmodifyFlat(modId+"popmod");
//        market.getIncomeMult().unmodifyFlat(modId+"stabilitymod");

//        market.getUpkeepMult().unmodifyFlat(modId+"base");
        market.getUpkeepMult().unmodifyFlat(modId+"popmod");
    }
    public  void modifyIncome(Industry industry, MarketAPI market, String modId) {
        int popsize = market.getSize();
        float marketstability = market.getPrevStability();
        int basepop = 5;
        float basestability = 5;
        float popmodifier = (popsize - basepop)*0.1f;
        float stabilitymodifier = (marketstability - basestability)*0.1f;
        market.getIncomeMult().modifyMultAlways(modId+"popmod", 1f+popmodifier+stabilitymodifier, "Local Bakery: Population");
        market.getUpkeepMult().modifyMultAlways(modId+"popmod", 1f+popmodifier-stabilitymodifier, "Local Bakery Population Upkeep");

    }

    @Override
    public boolean canShutDown() {
        return !isFromContract;
    }

    @Override
    public String getCanNotShutDownReason() {
        return "You have signed contract! You can't shut down this industry.";
    }

}
