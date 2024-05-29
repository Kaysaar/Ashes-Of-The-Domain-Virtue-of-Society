package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.conditions.HarshTreatStrikeEventApplier;
import data.colonyevents.conditions.ResolvedStrikesEventApplier;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import data.colonyevents.models.StrikeDemand;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MassiveStrikesEvent extends AoTDColonyEvent {
    //It's guaranteed event : we don't want it to be picked up randomly : Validation is done in listener
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return false;
    }

    StrikeDemand currentDemandNotHarsh = null;
    StrikeDemand currentDemandHarsh = null;


    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        super.generateDescriptionOfEvent(tooltip);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if (optionId.equals("ms_warning")) {
            tooltip.addPara("We should increase the stability of " + currentlyAffectedMarket.getName() + " as fast as possible to level greater than 5!", NEGATIVE, 10f);
        }
        if (optionId.equals("ms_strike")) {
            tooltip.addPara("We should increase the stability of " + currentlyAffectedMarket.getName() + " as fast as possible to level greater than 5!", NEGATIVE, 10f);
        }
        if (optionId.contains("ms_demand")) {
            switch (optionId) {
                case "ms_demand_op1":
                    tooltip.addPara("We will do everything in our power to help our people.", INFORMATIVE, 10f);
                    generateDemand(false);
                    if (currentDemandNotHarsh.amountOfCashToPay != 0) {
                        tooltip.addPara("We need to pay " + currentDemandNotHarsh.amountOfCashToPay + " credits to stabilize situation", NEGATIVE, 10f);
                    }
                    if (!currentDemandNotHarsh.industriesToEffect.isEmpty()) {
                        for (String s : currentDemandNotHarsh.industriesToEffect) {
                            tooltip.addPara("We need to close " + currentlyAffectedMarket.getIndustry(s).getCurrentName() + " for 60 days", NEGATIVE, 10f);
                        }
                    }
                    break;
                case "ms_demand_op2":
                    tooltip.addPara("We understand what the people are going through, but we must endure.", INFORMATIVE, 10f);
                    break;
                case "ms_demand_op3":
                    tooltip.addPara("Silence them and make them get back to work. Ungrateful pigs.", INFORMATIVE, 10f);
                    generateDemand(true);
                    tooltip.addPara("Reduce stability by " + currentDemandHarsh.stabilityPenalty+" for 90 days", NEGATIVE, 10f);
                    tooltip.addPara("Add " + currentDemandHarsh.defencePenalty + " multiplier to ground defence strength for 90 days", NEGATIVE, 10f);
                    break;
                case "ms_demand_op_final":
                    int got = (int) currentlyAffectedMarket.getMemory().get("$aotd_strike_counter");
                    int total = (int) currentlyAffectedMarket.getMemory().get("$aotd_strike_counter_max") * 2;
                    if (got < total * 0.6) {
                        tooltip.addPara("You have failed your people and their trust in you is gone!", NEGATIVE,10f);
                    } else if (got >= total * 0.6 && got < total * 0.8) {
                        tooltip.addPara("You tried your best, you did some good but it was not enough!", INFORMATIVE,10f);
                    } else {
                        tooltip.addPara("You have done everything what you could to satisfy your people, well done!", POSITIVE,10f);
                    }
                    break;
            }
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if (currentDecision.contains("ms_demand")) {
            switch (currentDecision) {
                case "ms_demand_op1":
                    changeEventScore(2);
                    executeDemand(false);
                    break;
                case "ms_demand_op2":
                    changeEventScore(1);
                    break;
                case "ms_demand_op3":
                    changeEventScore(0);
                    executeDemand(true);
                    break;
                case "ms_demand_op_final":
                    decideFinalStage();
                    super.executeDecision(currentDecision);
                    break;
            }
            if (currentDecision.equals("ms_strike")) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("strike_demand", currentlyAffectedMarket.getId(), 5);
                super.executeDecision(currentDecision);
            }

        }
    }

    public void changeEventScore(int amount) {
        if (currentlyAffectedMarket.getMemory().contains("$aotd_strike_counter")) {
            int currentScore = (int) currentlyAffectedMarket.getMemory().get("$aotd_strike_counter");
            currentScore += amount;
            currentlyAffectedMarket.getMemory().set("$aotd_strike_counter", currentScore);
            int currentAmountOfCharges = (int) currentlyAffectedMarket.getMemory().get("$aotd_strike_counter_left") - 1;
            if (currentAmountOfCharges <= 0) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("strike_conclusion", currentlyAffectedMarket.getId(), 10);
            } else {
                currentlyAffectedMarket.getMemory().set("$aotd_strike_counter_left", currentAmountOfCharges - 1);
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("strike_demand", currentlyAffectedMarket.getId(), 20 + new Random().nextInt(20));
            }
        } else {
            currentlyAffectedMarket.getMemory().set("$aotd_strike_counter", amount);
            int rand = new Random().nextInt(2) + 2;
            currentlyAffectedMarket.getMemory().set("$aotd_strike_counter_left", rand);
            currentlyAffectedMarket.getMemory().set("$aotd_strike_counter_max", rand + 1);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("strike_demand", currentlyAffectedMarket.getId(), 20 + new Random().nextInt(20));
        }
    }

    public void decideFinalStage() {
        int got = (int) currentlyAffectedMarket.getMemory().get("$aotd_strike_counter");
        int total = (int) currentlyAffectedMarket.getMemory().get("$aotd_strike_counter_max") * 2;
        currentlyAffectedMarket.getMemory().unset("$aotd_strike_counter");
        String token = currentlyAffectedMarket.addCondition("resolved_strikes");
        ResolvedStrikesEventApplier condition = (ResolvedStrikesEventApplier) currentlyAffectedMarket.getCondition(token).getPlugin();
        if (got < total * 0.6) {
            condition.stage3 = true;
        } else if (got >= total * 0.6 && got < total * 0.8) {
            condition.stage2 = true;
        } else {
            condition.stage1 = true;
        }


    }

    public void generateDemand(boolean isHarsh) {
        if (!isHarsh && currentDemandNotHarsh == null) {
            currentDemandNotHarsh = new StrikeDemand();
            Random random = new Random();
            int amountOfIndustries = random.nextInt(2);
            List<Industry> currIndustries = currentlyAffectedMarket.getIndustries();
            Collections.shuffle(currIndustries);
            for (Industry currIndustry : currIndustries) {
                if (currIndustry.getId().equals(Industries.POPULATION)) continue;
                if (!currIndustry.isFunctional()) continue;
                if (currIndustry.getSpec().hasTag("industry")) {
                    currentDemandNotHarsh.industriesToEffect.add(currIndustry.getId());
                    amountOfIndustries--;
                }
                if (amountOfIndustries <= 0) break;
            }
            currentDemandNotHarsh.amountOfCashToPay = (float) (1 + random.nextInt(7) * 20000);

        } else if (currentDemandHarsh == null) {
            currentDemandHarsh = new StrikeDemand();
            Random random = new Random();
            currentDemandHarsh.defencePenalty = (5 + random.nextInt(3)) * 0.1f;
            currentDemandHarsh.stabilityPenalty = random.nextInt(2) + 1;
        }
    }

    public void executeDemand(boolean isHarsh) {
        if (!isHarsh) {
            for (String s : currentDemandNotHarsh.industriesToEffect) {
                currentlyAffectedMarket.getIndustry(s).setDisrupted(60);
            }
            Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(currentDemandNotHarsh.amountOfCashToPay);
        } else if (currentDemandHarsh == null) {
            String token = currentlyAffectedMarket.addCondition("strike_harsh");
            HarshTreatStrikeEventApplier condition=  (HarshTreatStrikeEventApplier)currentlyAffectedMarket.getCondition(token).getPlugin();

            //TODO params for debufs
        }
        currentDemandHarsh = null;
        currentDemandNotHarsh = null;
    }
}
