package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class CometSightedEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return false;
    }

    @Override
    public void apply() {
        super.apply();

    }

    @Override
    public void unapply() {
        super.unapply();
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("People are always superstitious, and the apperance of a comet in the sky has caused panic among our people. " +
                "They are convinced that this is sign that the end of times is near or that something bad is going to happen in the near future.", Color.ORANGE,10f);

   }

    @Override
    public boolean canShowOptionOutcomesBeforeDeciding() {
        return true;
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        tooltip.addPara("Something VERY BAD will happen", Misc.getNegativeHighlightColor(),10f);
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("comet_sighted_op13")){
            currentlyAffectedMarket.addCondition(Conditions.TECTONIC_ACTIVITY);
            prevDecisionId = currentDecision;
        }
        super.executeDecision(currentDecision);
    }
}
