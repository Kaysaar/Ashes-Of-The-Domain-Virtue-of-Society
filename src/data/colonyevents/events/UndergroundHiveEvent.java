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
        tooltip.addPara("Geological survey that our colony has conduced has revealed that a network of caves, previously assumed to be natural in origin, is in fact breeding grounds for a previously undiscovered insect species.", INFORMATIVE, 10f);
        tooltip.addPara("The dispatched team broke into a large cavern containing few thousand eggs, each measuring approximately 1.5m. " +
                "However, before they could secure samples several creatures that seemed to be the warrior caste of the hive began attacking them. Each of these monstrosities was the size of a small " +
                "combat mech. Luckily for us the team was able to evacuate without any " +
                "human losses and with one of the eggs, but now the question of what to do with the hive presents itself.", INFORMATIVE, 10f);
        tooltip.addPara("One of the surveyors is still amazed by the creatures and their robustness, and suggests that we try and find a way to use them to improve our defences, in case of an ground invasion as their sheer size " +
                "endurance and terrifying appearance could be devastating on enemy forces when unleashed upon them.", INFORMATIVE, 10f);
        tooltip.addPara("The secretary has suggested purging the warrior caste and try to extend the territory of the insects to produce enough eggs in order to export them as luxury " +
                "food, after testing revealed that they are edible and in fact quite delicious, despite being a little bit chewy.", INFORMATIVE, 10f);
        tooltip.addPara("Finally, one of our warrant officers suggested nuking the site from orbit to remove all traces of these species.", INFORMATIVE, 10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId) {
            case "hive_op1":
                tooltip.addPara("Decrease stability by 1.", NEGATIVE, 15f);
                tooltip.addPara("Increase ground defence multiplier by 1.25.", POSITIVE, 15f);
                tooltip.addPara("With special pheromones we can control them... Most of the time.", INFORMATIVE, 15f);
                break;
            case "hive_op2":
                tooltip.addPara("We'll make the mother of all omelettes with these!", POSITIVE, 15f);
                tooltip.addPara("Increase production of luxury goods by 2 units.", POSITIVE, 15f);
                tooltip.addPara("Increase demand for marines for industries producing luxury goods by 4 units.",NEGATIVE,10f);
                break;
            case "hive_op3":
                tooltip.addPara("I will not let those things roam freely on my planet, destroy them in nuclear hellfire:\n" +
                        "It's the only way to be sure.", INFORMATIVE, 15f);
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
