package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;

public class SafetyConcernsEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean hasTag = false;
        for (Industry industry : marketAPI.getIndustries()) {
            if(industry.getSpec().hasTag("heavyindustry")||industry.getSpec().hasTag("lightindustry")){
                hasTag= true;
            }
        }

        return super.canOccur(marketAPI)&&hasTag&&marketAPI.getHazardValue()>=1.0f&&marketAPI.getSize()>4;
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator, situation of our workers on "+currentlyAffectedMarket.getName()+" has gotten significantly worse. There have been loud demands to " +
                "increase safety conditions in our factories, which in return would reduce productivity of our facilities. We could always ignore those demands, but " +
                "this is ought to cause unrest among the population.",10f);
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("qa_op1")){
            currentlyAffectedMarket.addCondition("safety_achieved");
        }
        else{
            currentlyAffectedMarket.addCondition("safety_declined");
        }
        super.executeDecision(currentDecision);

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if(optionId.equals("qa_op1")){
            tooltip.addPara("Increase upkeep costs of all heavy and light industries by 10% for 180 days.", Misc.getNegativeHighlightColor(),10f);
        }
        else{
            tooltip.addPara("Decrease Stability by 1 for 180 days.", Misc.getNegativeHighlightColor(),10f);
        }
    }
}
