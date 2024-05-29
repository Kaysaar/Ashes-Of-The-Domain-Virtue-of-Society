package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.models.AoTDColonyEvent;

public class FoodExperimentEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        if(marketAPI.getPlanetEntity()==null)return false;
        return super.canOccur(marketAPI)&&marketAPI.getPlanetEntity().getTypeId().contains("desert")&&marketAPI.hasCondition(Conditions.FARMLAND_POOR);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("On the Desert world of" +currentlyAffectedMarket.getName()+ " your team of scientists found old labs built before The Collapse.",INFORMATIVE,10f);
        tooltip.addPara("It seems to have been some sort of agriculture research center focusing on development of hydroponics before it was abandoned.",INFORMATIVE,10f);
        tooltip.addPara("Our team has found some of the plant seeds that researchers used to work on back when the lab was still active.",INFORMATIVE,10f);
        tooltip.addPara("We need to choose one of the three projects, as the structure can collapse at any time.",INFORMATIVE,10f);
    }

    @Override
    public void executeDecision(String currentDecision) {
        switch (currentDecision){
            case "fe_op1":
               currentlyAffectedMarket.addCondition("food_experiment_potato");
                break;
            case "fe_op2":
                currentlyAffectedMarket.addCondition("food_experiment_flowers");
                break;
            case "fe_op3":
                currentlyAffectedMarket.addCondition("food_experiment_tree");
                break;
        }
        super.executeDecision(currentDecision);
    }

    @Override
    public void overrideOptions() {
        super.overrideOptions();
    }


    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId){
            case "fe_op1":
                tooltip.addPara("The market's agriculture industries have their production permamently increased by 1 unit.",POSITIVE,10f);
                tooltip.addPara("Market income is permamently increased by 10%.",POSITIVE,10f);
                break;
            case "fe_op2":
                tooltip.addPara("Market accessibility is increased by 20%",POSITIVE,10f);
                tooltip.addPara("Market income is permamently increased by 10%.",POSITIVE,10f);
                break;
            case "fe_op3":
                tooltip.addPara("Market's hazard rating is decreased by 35%.",POSITIVE,10f);
                break;
        }
    }
}
