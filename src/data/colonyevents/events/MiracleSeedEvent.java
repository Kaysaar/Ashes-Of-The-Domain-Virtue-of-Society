package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.PopulationAndInfrastructure;
import com.fs.starfarer.api.impl.campaign.econ.impl.Spaceport;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.ui.P;
import data.colonyevents.conditions.MiracleSeedApplier;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import data.kaysaar.aotd.vok.scripts.research.AoTDMainResearchManager;
import data.kaysaar.aotd.vok.scripts.research.scientist.models.ScientistAPI;

import java.awt.*;

public class MiracleSeedEvent extends AoTDColonyEvent {
    public int getCredits(int amount) {
        if (Global.getSettings().getModManager().isModEnabled("aotd_vok")) {
            ScientistAPI scientist = AoTDMainResearchManager.getInstance().getManagerForPlayer().currentHeadOfCouncil;
            if (scientist != null) {
                if (scientist.getScientistPerson().getId().contains("sophia")) {
                    return amount / 2;
                }
            }
        }
        return amount;
    }

    @Override
    public boolean canOccur(MarketAPI marketAPI) {

        boolean hasTag = false;
        for (Industry industry : marketAPI.getIndustries()) {
            if (industry.getSpec().hasTag("farming")) {
                hasTag = true;
                break;
            }
        }
        if (getSpec().getEventId().equals("miracle_seed_start")) {
            return super.canOccur(marketAPI) && marketAPI.hasIndustry(Industries.TECHMINING)
                    && marketAPI.hasCondition(Conditions.FARMLAND_POOR)
                    && hasTag && marketAPI.getSize() >= 4
                    && !marketAPI.hasCondition(Conditions.INIMICAL_BIOSPHERE);
        } else {
            return false;
        }


    }

    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        String id = spec.getEventId();
        if (id.equals("miracle_seed_start")) {
            tooltip.addPara("Farmers on " + currentlyAffectedMarket.getName() + " report that their crops are likely to fail this year, but at the same time, local techminers report strange seeds found in a sealed cache in what appeared to be a Domain era biotech laboratory. Any data found was corrupted beyond repair, but the fragments recovered appeared to indicate that was research was done on ways to improve food production based on the planet's unique ecology. When one of the seeds was planted by a techminer, to his surprise, it grew into a plant his height overnight, and within a week, bore sweet, succulent fruit that tests confirm is edible and nutritious. ", INFORMATIVE, 10f);
            tooltip.addPara("Thus the techminers began to call them %s", 10f, INFORMATIVE, Color.ORANGE, "\"Miracle Seeds\"");
        }
        if (id.equals("miracle_seed_2a")) {
            tooltip.addPara("Months have passed on " + currentlyAffectedMarket.getName() +
                    ", and the new plant has proved surprisingly hardly and prolific, thriving under conditions that " +
                    "prove too harsh for traditional crops, allowing for previously unsuitable land to be tapped for farming. " +
                    "If anything, they appear to be too prolific, with reports of the plant seeming appearing in places overnight " +
                    "where they were not planted. Their fruits have also been accepted by the colonists as a new staple in their diet thanks " +
                    "to their pleasant flavor. However, a worrying report from the medical team suggests that a previously unknown respiratory " +
                    "illness has been circulating around the colony, with the old and vulnerable most affected. While the two seem unrelated, " +
                    "you cannot help the nagging suspicion that something may have gone wrong.", INFORMATIVE, 10f);
            tooltip.addSectionHeading("Effects from previous decisions", Alignment.MID, 10f);
            tooltip.addPara("Poor Farmland -> Adequate Farmland", POSITIVE, 5f);
            tooltip.addPara("Growth of colony population is severely halted", NEGATIVE, 5f);
        }
        if (id.equals("miracle_seed_3aa")) {
            tooltip.addPara("Disaster has struck " + currentlyAffectedMarket.getName() +
                    ". Doctors have realized, too late, that the respiratory illness was caused by the spores released from the plants grown from the strange seeds found in the ruins. " +
                    "Though harmless in small quantities, the uncontrolled proliferation of the plant has caused the concentration of spores to rise to deadly levels for unprotected humans. " +
                    "Now, much like the fabled Pandora's box, the plant has spread too far, and most colonists have been unknowingly exposed over the months to fatal doses. " +
                    "Reports have surfaced of the plant growing out of graves, after one colonist discovered it sprouting from her deceased husband's resting place. " +
                    "Orders have been issued to burn the bodies. If there is any consolation, it is that the plant's agricultural benefits remain permanent.", INFORMATIVE, 10f);
            tooltip.addSectionHeading("Effects from previous decisions", Alignment.MID, 10f);
            tooltip.addPara("Adequate Farmland  -> Rich Farmland", POSITIVE, 5f);
            tooltip.addPara("Add Inimical Biosphere to planet conditions", NEGATIVE, 5f);
            tooltip.addPara("Lower colony size by 1 ", NEGATIVE, 5f);
            tooltip.addPara("-3 stability, recovers by 1 every 3 months", NEGATIVE, 5f);
        }
        if (id.equals("miracle_seed_3ab")) {
            tooltip.addPara("The cause of the respiratory illness on " + currentlyAffectedMarket.getName() +
                    " has been uncovered alongside a disturbing truth. The very plant responsible for saving the colony's harvests " +
                    "has also been releasing spores that, over time and in high concentrations, are lethal to unprotected humans. " +
                    "Unfortunately, the plant has now spread far beyond control, and " + currentlyAffectedMarket.getName() +
                    "'s biosphere has been irreversibly altered. Thankfully, your swift actions have kept fatalities to a minimum, " +
                    "and the plant's benefits to agriculture appear to be permanent.", INFORMATIVE, 10f);

            tooltip.addSectionHeading("Effects from previous decisions", Alignment.MID, 10f);
            tooltip.addPara("Adequate Farmland  -> Rich Farmland", POSITIVE, 5f);
            tooltip.addPara("Add Inimical Biosphere to planet conditions", NEGATIVE, 5f);
        }
        if (id.equals("miracle_seed_2b")) {
            tooltip.addPara("The scientists have completed their analysis of the seeds found on " + currentlyAffectedMarket.getName() +
                    ". While much of their report is incomprehensible scientific jargon, their summary paints a clear picture. " +
                    "The seeds, though highly beneficial for food production, are a product of genetic engineering. The mutant plant they produce " +
                    "also releases spores that are deadly to humans if inhaled in large enough quantities. Given the plant's rapid reproduction, " +
                    "the biosphere on " + currentlyAffectedMarket.getName() + " is permanently changed. The scientists suggest further research " +
                    "might allow for alterations to the seeds to remove the harmful side effects.", INFORMATIVE, 10f);

        }
        if (id.equals("miracle_seed_3b")) {
            tooltip.addPara("Thanks to your funding, the scientists have successfully altered the seeds found on " + currentlyAffectedMarket.getName() +
                    ". They assure you that the modified plant no longer produces spores, thus preventing any further risks of uncontrolled proliferation. " +
                    "However, this alteration also limits the plant's ability to grow in regions where farming is otherwise economically unfeasible. " +
                    "With the potential risks mitigated, perhaps now is the time to reap the full rewards of your decision.", INFORMATIVE, 10f);
        }

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if (optionId.equals("miracle_seed_start1")) {
            tooltip.addPara("We will use those to boost our food production", INFORMATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_start2")) {
            tooltip.addPara("-2 food production for 1 year", NEGATIVE, 10f);
            tooltip.addPara("Lose " + Misc.getDGSCredits(getCredits(50000)), NEGATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_start3")) {
            tooltip.addPara("-2 food production for 1 year", NEGATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_2a1")) {
            tooltip.addPara("We will continue to use those seeds as we used to", INFORMATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_2a2")) {
            tooltip.addPara("Disrupt all industries for 90 days", NEGATIVE, 10f);
            tooltip.addPara("-3 Stability, recovers by 1 each 90 days", NEGATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_2b1")) {
            tooltip.addPara("Lose " + Misc.getDGSCredits(50000 * (currentlyAffectedMarket.getSize() - 2)), NEGATIVE, 10f);
            tooltip.addPara("Remove food production penalty", POSITIVE, 10f);
            tooltip.addPara("Poor Farmland -> Adequate Farmland after 90 days", POSITIVE, 5f);
            tooltip.addPara("Adequate Farmland  -> Rich Farmland after 180 days", POSITIVE, 5f);
            tooltip.addPara("Add Inimical Biosphere to planet conditions", NEGATIVE, 5f);
        }
        if (optionId.equals("miracle_seed_2b2")) {
            tooltip.addPara("Lose " + Misc.getDGSCredits(getCredits(200000)), NEGATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_2b3")) {
            tooltip.addPara("Maybe it's better for us all to destroy them.", INFORMATIVE, 10f);
        }
        if (optionId.equals("miracle_seed_3b1")) {
            tooltip.addPara("Remove food production penalty", POSITIVE, 10f);
            tooltip.addPara("Poor Farmland -> Adequate Farmland after 90 days", POSITIVE, 5f);
        }
        if (optionId.equals("miracle_seed_3b2")) {
            tooltip.addPara("Maybe it's better for us all to destroy them.", INFORMATIVE, 10f);

        }

    }

    @Override
    public boolean isOptionValid(String optionId) {
        float credits = Global.getSector().getPlayerFleet().getCargo().getCredits().get();
        if (optionId.equals("miracle_seed_start2")) {
            return credits >= getCredits(50000);
        }
        if (optionId.equals("miracle_seed_2b1")) {
            return credits >= (50000 * (currentlyAffectedMarket.getSize() - 2));
        }
        if (optionId.equals("miracle_seed_2b2")) {
            return credits >= getCredits(200000);
        }
        return true;
    }

    @Override
    public void executeDecision(String currentDecision) {

        if (currentDecision.equals("miracle_seed_start1")) {
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("miracle_seed_2a", currentlyAffectedMarket.getId(), 90);
        }
        if (currentDecision.equals("miracle_seed_start2")) {
            Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(getCredits(50000));
            currentlyAffectedMarket.addCondition("miracle_seed_food_penalty");
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("miracle_seed_2b", currentlyAffectedMarket.getId(), 90);
        }
        if (currentDecision.equals("miracle_seed_start3")) {
            currentlyAffectedMarket.addCondition("miracle_seed_food_penalty");
        }
        if (currentDecision.equals("miracle_seed_2a1")) {
            currentlyAffectedMarket.removeCondition(Conditions.FARMLAND_POOR);
            currentlyAffectedMarket.addCondition(Conditions.FARMLAND_ADEQUATE);
            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                industry.getSupply(Commodities.FOOD).getQuantity().getFlatMods().clear();
            }
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("miracle_seed_3aa", currentlyAffectedMarket.getId(), 90);
        }
        if (currentDecision.equals("miracle_seed_2a2")) {

            currentlyAffectedMarket.getStability().addTemporaryModFlat(30, "aotd_mir_pen_1", "Miracle Seeds", -1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(60, "aotd_mir_pen_1", "Miracle Seeds", -1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "aotd_mir_pen_1", "Miracle Seeds", -1);
            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                if (industry instanceof PopulationAndInfrastructure || industry instanceof Spaceport) continue;
                industry.setDisrupted(90);
            }
            currentlyAffectedMarket.removeCondition(Conditions.FARMLAND_POOR);
            currentlyAffectedMarket.addCondition(Conditions.FARMLAND_ADEQUATE);
            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                industry.getSupply(Commodities.FOOD).getQuantity().getFlatMods().clear();
            }
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("miracle_seed_3ab", currentlyAffectedMarket.getId(), 90);

        }
        if (currentDecision.equals("miracle_seed_3aa1")) {
            currentlyAffectedMarket.removeCondition(Conditions.FARMLAND_ADEQUATE);
            currentlyAffectedMarket.addCondition(Conditions.FARMLAND_BOUNTIFUL);
            currentlyAffectedMarket.addCondition(Conditions.INIMICAL_BIOSPHERE);
            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                industry.getSupply(Commodities.FOOD).getQuantity().getFlatMods().clear();
            }
            currentlyAffectedMarket.setSize(currentlyAffectedMarket.getSize() - 1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(30, "aotd_mir_pen_1", "Miracle Seeds", -1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(60, "aotd_mir_pen_1", "Miracle Seeds", -1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "aotd_mir_pen_1", "Miracle Seeds", -1);

        }
        if (currentDecision.equals("miracle_seed_3ab1")) {
            currentlyAffectedMarket.removeCondition(Conditions.FARMLAND_ADEQUATE);
            currentlyAffectedMarket.addCondition(Conditions.FARMLAND_BOUNTIFUL);
            currentlyAffectedMarket.addCondition(Conditions.INIMICAL_BIOSPHERE);
        }
        if (currentDecision.equals("miracle_seed_2b1")) {
            Global.getSector().getPlayerFleet().getCargo().getCredits().subtract((50000 * (currentlyAffectedMarket.getSize() - 2)));
            currentlyAffectedMarket.removeCondition("miracle_seed_food_penalty");
            String token = currentlyAffectedMarket.addCondition("miracle_seed_food_apply");
            currentlyAffectedMarket.addCondition(Conditions.INIMICAL_BIOSPHERE);
            MiracleSeedApplier applier = (MiracleSeedApplier) currentlyAffectedMarket.getSpecificCondition(token).getPlugin();
            applier.setShouldAddRichFarmland(true);

        }
        if (currentDecision.equals("miracle_seed_2b2")) {
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("miracle_seed_3b", currentlyAffectedMarket.getId(), 90);
            Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(getCredits(200000));
        }
        if (currentDecision.equals("miracle_seed_2b3")) {
            // Order the seeds to be destroyed.
        }
        if (currentDecision.equals("miracle_seed_3b1")) {
            String token = currentlyAffectedMarket.addCondition("miracle_seed_food_apply");
            MiracleSeedApplier applier = (MiracleSeedApplier) currentlyAffectedMarket.getCondition(token).getPlugin();
            applier.setShouldAddRichFarmland(false);
        }
        if (currentDecision.equals("miracle_seed_3b2")) {
            // Change your mind, order the seeds to be destroyed.
        }
        super.executeDecision(currentDecision);
    }

}
