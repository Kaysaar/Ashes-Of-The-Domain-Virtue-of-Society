package data.scripts.industry;

import com.fs.starfarer.api.characters.AdminData;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

import java.awt.*;

public class DolosNetwork extends BaseIndustry {
   public  boolean hasAiCore = false;
   public boolean hasDeficit = false;

    @Override
    public void apply() {
        super.apply(true);
        if(market.getAdmin().getStats().getSkillLevel("fourth_dimensional_sight")>=1){
            hasAiCore =true;
        }
        else{
            hasAiCore = false;
        }

    }

    @Override
    public boolean isAvailableToBuild() {
        return market.getAdmin().getStats().getSkillLevel("fourth_dimensional_sight")>=1;
    }

    @Override
    public boolean showWhenUnavailable() {
        return market.getAdmin().getStats().getSkillLevel("fourth_dimensional_sight")>=1;
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        super.addPostDemandSection(tooltip, hasDemand, mode);
        tooltip.addPara("This structure when Theta AI core is administrator of colony, reduces Colony threat meter by 5 points, for every working DNC structure.",10f);
        if(mode.equals(IndustryTooltipMode.NORMAL)){
            tooltip.addPara("This instance of network can only work, when Theta AI core is administrator of this colony!", Color.ORANGE,10f);
        }
    }

    @Override
    public boolean canInstallAICores() {
        return false;
    }
}
