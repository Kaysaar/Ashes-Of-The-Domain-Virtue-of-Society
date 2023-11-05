package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.models.AoTDColonyEvent;

public class HarvestOfYearEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean hasTag = false;
        for (Industry industry : marketAPI.getIndustries()) {
            if(industry.getSpec().hasTag("farming")){
                hasTag= true;
            }

        }
      return hasTag&&super.canOccur(marketAPI);
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
        tooltip.addPara("Administrator, our reports from "+currentlyAffectedMarket.getName()+ " indicate, that recent harvest of food has never been that great.Amount of food that was collected is nearly enough to feed planet for few years." +
                "Some people suggest using that surplus, to make feast for entire planet, which would boost everyone's morale, others suggest selling that for huge profits. What are you going to do administrator?", INFORMATIVE,10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if(optionId.equals("harvest_of_year_op1")){
            tooltip.addPara("+1 To production to all industries for 365 days",POSITIVE,10f);
            tooltip.addPara("+1 To stability for 365 days",POSITIVE,10f);
        }
        else{
            tooltip.addPara("Add 200000 Credits to account",POSITIVE,10f);
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("harvest_of_year_op2")){
            Global.getSector().getPlayerFleet().getCargo().getCredits().add(200000);
        }
        else{
            currentlyAffectedMarket.addCondition("harvest_of_year");
        }
        super.executeDecision(currentDecision);
    }
}
