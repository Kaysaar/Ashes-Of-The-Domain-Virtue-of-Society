package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Pair;
import data.colonyevents.models.AoTDColonyEvent;

public class UndergroundHiveEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return super.canOccur(marketAPI) && marketAPI.hasCondition(Conditions.INIMICAL_BIOSPHERE) && marketAPI.getSize() >= 4;
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("New geological survey on part of the colony have revealed that a network of cave previously suspected of being of natural origin are in fact breeding ground for a new insect species.", INFORMATIVE, 10f);
        tooltip.addPara("The dispatched team broke into a wide cavern containing a few thousand eggs measuring each approximatively 1.5m. " +
                "Before they could secure samples however several warrior-type creature the size of a small " +
                "combat mech started to attack them. Luckily for us the team was able to evacuate without any " +
                "human losses and with one of the eggs but now the question remain of what to do with the hive.", INFORMATIVE, 10f);
        tooltip.addPara("One of the surveyor still amazed by the creature and their robustness suggest that we try to find a way to use them to improve our defence in case of an invasion as their shear size, " +
                "robustness and scariness could be devastating when unleash upon an unsuspected enemy.", INFORMATIVE, 10f);
        tooltip.addPara("The secretary of the administrator have suggested purging the warrior-types and try to expend the territory of the creature to produce enough eggs to be exported as luxury " +
                "food after testing revealed that they are quite edible and actually rather good despite being a little bit chewy.", INFORMATIVE, 10f);
        tooltip.addPara("Finally one of our warrant officer suggested nuking the site from orbit to remove all traces of this species.", INFORMATIVE, 10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId) {
            case "hive_op1":
                tooltip.addPara("Decrease stability by 1.", NEGATIVE, 15f);
                tooltip.addPara("Increase defence multiplier by 1.25", POSITIVE, 15f);
                tooltip.addPara("With special pheromones we can control them... most of the time", INFORMATIVE, 15f);
                break;
            case "hive_op2":
                tooltip.addPara("We'll make the mother of all omelettes with those !", POSITIVE, 15f);
                tooltip.addPara("Increase production of luxury goods by 2", POSITIVE, 15f);
                tooltip.addPara("Increase demand for marines to industries producing luxury goods by 4",NEGATIVE,10f);
                break;
            case "hive_op3":
                tooltip.addPara("I will not let those things roam freely on my planet, destroy them:\n" +
                        "It's the only way to be sure", INFORMATIVE, 15f);
                break;
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        switch (currentDecision) {
            case "hive_op1":
                currentlyAffectedMarket.addCondition("hive_defence");
                break;
            case "hive_op2":
                currentlyAffectedMarket.addCondition("hive_food");
                break;
        }
        super.executeDecision(currentDecision);
    }

}
