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
        tooltip.addPara("Administrator, our reports from "+currentlyAffectedMarket.getName()+ " indicate that the most recent harvest of food is one of the greatest they ever had! The amount of food that was collected is nearly enough to keep the planet fed planet for few next years." +
                "Some of our advisors suggest using that surplus to host a feast for the entire planet, which would boost people's morale, while others suggest selling that for huge profits. What are you going to do administrator?", INFORMATIVE,10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if(optionId.equals("harvest_of_year_op1")){
            tooltip.addPara("All industries produce 1 more units for 365 days.",POSITIVE,10f);
            tooltip.addPara("+1 To stability for 365 days.",POSITIVE,10f);
        }
        else{
            tooltip.addPara("Gain 200.000 Credits.",POSITIVE,10f);
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
