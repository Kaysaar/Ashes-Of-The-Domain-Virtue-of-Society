package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.listeners.UAFMarketConditionEnforcer;
import data.colonyevents.models.AoTDColonyEvent;
import data.scripts.industry.UAFBakeryKnockOff;

import java.awt.*;

public class UAFCakeEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return Global.getSector().getMemory().is("$uaf_aotd_event", true);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator, we have been contacted by one of famous culinary figure: %s.", 10f, Misc.getTooltipTitleAndLightHighlightColor(),
                new Color(73, 172, 255), "" + "Aurelio Flambé");
        tooltip.addPara("Intrigued by the taste of your mysterious freshly baked pastries, never seen before on the market." +
                "The delegation have sampled \"your\" delectable creations, their eyes lighting up with the promise of money." +
                "You quickly explain to them that they are in fact not yours but belong to a small faction in the bottom left part of the sector" +
                "hey give you a contract anyways", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
    }

    @Override
    public void executeDecision(String currentDecision) {
        if (currentDecision.equals("bd_op1")) {
            currentlyAffectedMarket.addIndustry("uaf_bakery_fake");
            UAFBakeryKnockOff ind = (UAFBakeryKnockOff) currentlyAffectedMarket.getIndustry("uaf_bakery_fake");
            ind.isFromContract = true;
        }
        if (currentDecision.equals("bd_op2")) {
            currentlyAffectedMarket.addCondition("aotd_bakery_assistance");
            currentlyAffectedMarket.getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addSpecial(new SpecialItemData("industry_bp", "uaf_bakery_branch"), 1);
        }
        if (currentDecision.equals("bd_op3")) {
            Global.getSector().getMemory().set("$aotdxuaf_bakery", true);
            Global.getSector().getListenerManager().addListener(new UAFMarketConditionEnforcer());
        }
        super.executeDecision(currentDecision);

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if (optionId.equals("bd_op1")) {
            tooltip.addPara("Gain new Bakery industry with high boost to income", Misc.getPositiveHighlightColor(), 10f);
            tooltip.addPara("Warning! This industry can't be removed", Misc.getNegativeHighlightColor(), 10f);

        }
        if (optionId.equals("bd_op2")) {
            tooltip.addPara("Gain 10% accessibility bonus.", Misc.getPositiveHighlightColor(), 10f);
            tooltip.addPara(currentlyAffectedMarket.getName() + " will recieve special UAF fleet to defend it", Misc.getPositiveHighlightColor(), 10f);
            tooltip.addPara("Receive blueprint for confectionary industry to " + currentlyAffectedMarket.getName() + " local storage", Misc.getPositiveHighlightColor(), 10f);

        }
        if (optionId.equals("bd_op3")) {
            tooltip.addPara("Faction gets unique trait for rest of the game:", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("+1 Stability on world's with Bakery", Misc.getPositiveHighlightColor(), 10f);
            tooltip.addPara("-1 Stability on world's without Bakery", Misc.getNegativeHighlightColor(), 10f);
            tooltip.addPara("Gain ability to built Bakery industry, but without huge income bonus and with lowered production", Misc.getPositiveHighlightColor(), 10f);
        }
    }
}
