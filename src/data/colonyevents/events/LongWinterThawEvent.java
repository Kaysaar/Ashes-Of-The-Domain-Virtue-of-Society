package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.conditions.SpecialItemOnIndustryTracker;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class LongWinterThawEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return super.canOccur(marketAPI)&& SpecialItemOnIndustryTracker.getTrackerOnMarket(marketAPI).getDaysSinceItemInstalled(Items.ORBITAL_FUSION_LAMP, Industries.POPULATION)>=365&&marketAPI.hasCondition(Conditions.VERY_COLD);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("For more than year, the Orbital Fusion Lamp has bathed %s in artificial sunlight, pushing back the oppressive grip of extreme cold.  Life has become sustainable, industries have flourished despite the harsh environment, and the colony has thrived under the lamp's unwavering warmth.  However, this prolonged period of operation has revealed more than just the lamp's life-sustaining capabilities.",5f, Color.ORANGE,currentlyAffectedMarket.getName());
        tooltip.addPara("Engineers report that the extended exposure to the Fusion Lamp's energy output has begun to subtly alter the planet's atmospheric conditions and thermal dynamics.  Long-term sensor readings indicate a gradual but consistent warming trend across the planet.  The persistent energy input from the Orbital Fusion Lamp, initially intended as a temporary countermeasure, is now showing signs of initiating a more permanent shift in the planetary climate.  You are presented with a choice: capitalize on this unexpected environmental change, or refine the lamp's operation for greater efficiency",5f);
        super.generateDescriptionOfEvent(tooltip);

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        super.showOptionOutcomes(tooltip, optionId);


        if(optionId.equals("lgtt1")) {
            tooltip.addPara("Direct engineering efforts to amplify and solidify the observed warming trend. ",INFORMATIVE,10f);
            tooltip.addPara("Lose Orbital Fusion Lamp",NEGATIVE,5f);
            tooltip.addPara("Change Extremely Cold market condition to Cold",POSITIVE,5f);

        }
        if(optionId.equals("lgtt2")) {
            tooltip.addPara("Task engineering teams with analyzing the long-term operational data of the Orbital Fusion Lamp to identify and implement efficiency improvements.",INFORMATIVE,10f);
            tooltip.addPara("Reduce volatiles consumption of Orbital Fusion lamp by 2 units",NEGATIVE,5f);

        }

    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("lgtt1")) {
            currentlyAffectedMarket.removeCondition(Conditions.VERY_COLD);
            currentlyAffectedMarket.addCondition(Conditions.COLD);
            currentlyAffectedMarket.getIndustry(Industries.POPULATION).setSpecialItem(null);

        }
        if(currentDecision.equals("lgtt2")) {
            currentlyAffectedMarket.removeCondition(Conditions.VERY_COLD);
            currentlyAffectedMarket.addCondition(Conditions.COLD);
            currentlyAffectedMarket.getIndustry(Industries.POPULATION).getDemand(Commodities.VOLATILES).getQuantity().modifyFlat("aotd_improvment",-2,"Improved Orbital Fusion Lamp");

        }
        super.executeDecision(currentDecision);
    }
}
