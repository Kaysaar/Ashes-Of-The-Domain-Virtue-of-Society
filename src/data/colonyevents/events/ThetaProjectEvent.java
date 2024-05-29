package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.ProjectCheronIntel;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import data.colonyevents.models.AoTDGuarantedEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ThetaProjectEvent extends AoTDColonyEvent {
    public String previiousExpedition = "";
    boolean visitedMagazine = false;
    boolean visitedDirectorRoom = false;
    boolean visitedMainServerRoom = false;
    boolean assignedSystem = false;
    boolean canBuildThetaEncryptionDecoder = false;
    boolean finishedExplorationSection = false;
    boolean gotCores = false;

    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return false;
//        if (this.spec.getEventId().equals("theta_finding_facility")) {
//            return marketAPI.hasCondition("pre_collapse_facility") && marketAPI.getSize() >= 4 && marketAPI.hasIndustry("researchfacility") && Global.getSector().getPlayerFleet().getCargo().getCredits().get() >= 500000;
//        }
//        return false;
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        super.generateDescriptionOfEvent(tooltip);
        if (this.spec.getEventId().equals("theta_finding_facility")) {
            tooltip.addPara("Administrator, we have uncovered abandoned research facility on" + currentlyAffectedMarket.getName() + ".", INFORMATIVE, 10f);
            tooltip.addPara("We should investigate it immediately!", INFORMATIVE, 10f);
        }
        if (this.spec.getEventId().equals("theta_expl_fac")) {
            tooltip.addPara("Administrator", INFORMATIVE, 10f);
            tooltip.addPara("Exciting news from " + currentlyAffectedMarket.getName() + ", we have unveiled a hidden treasure trove - a clandestine research facility overseen by the Tri Tachyon Corporation and Ko Combine.", INFORMATIVE, 10f);
            tooltip.addPara("The enigmatic past of this facility was skillfully shrouded from prying eyes, guarding its secrets from those who might want to uncover it's mysteries.", INFORMATIVE, 10f);
            tooltip.addPara("Regrettably, our expedition faced challenges in extracting any sort of valuable data from the damaged terminals. However, within the cryptic fragments of the information, two words persistently came up, getting attention of our expedition team:", INFORMATIVE, 10f);
            tooltip.addPara("Project Charon", Color.MAGENTA, 10f);
            tooltip.addPara("The alluring nature of this discovery is undeniable, we remain committed to unraveling the enigma surrounding this intriguing project.", INFORMATIVE, 10f);
        }
        if (this.spec.getEventId().equals("theta_expl_fac_continue")) {
            switch (previiousExpedition) {
                case "bs_fac_op1":
                    if (!visitedDirectorRoom) {

                        tooltip.addPara("Our investigation of main server room has yielded more information about Project Charon.", POSITIVE, 10f);
                        tooltip.addPara("Most of the data has been heavily encrypted, and our scientists were not able to deduce anything useful from it. ", NEGATIVE, 10f);
                        tooltip.addPara("From what we have investigated so far, Project Charon was a initiative, ordered by higher ups of both corporations. ", INFORMATIVE, 10f);
                        tooltip.addPara("It was covered up to such extend, that no one in entire Domain knew about this project." +
                                "They decided have that the Persean Sector will be perfect place for such projects, due to low presence of Domain's authorities.", INFORMATIVE, 10f);
                        tooltip.addPara("Immediately after our scientists left the server room, it has collapsed, now there is no option of going back to it.", NEGATIVE, 10f);
                        tooltip.addPara("But the data from it has been transferred to our own servers and databanks, so we will be able to try to decode on out own.", INFORMATIVE, 10f);
                        tooltip.addPara("Moreover, before we left server room we have discovered blueprints to build what seems to be a quantum supercomputer, designed by Ko Combine.", INFORMATIVE, 10f);
                        tooltip.addPara("Gain a new structure blueprint: Archimedes QSC", POSITIVE, 10f);
                        currentlyAffectedMarket.addIndustry("archimedes");
                        currentlyAffectedMarket.getIndustry("archimedes").startBuilding();


                    } else {
                        tooltip.addPara("Our investigation of main server room has yielded more information about Project Charon.", POSITIVE, 10f);
                        tooltip.addPara("Thanks to finding the encryption key in director's office we have been able to decode all of the data present there. ", POSITIVE, 10f);
                        tooltip.addPara("From what we have investigated so far, Project Charon was a initiative, ordered by higher ups of both corporations. ", INFORMATIVE, 10f);
                        tooltip.addPara("It was covered up to such extend, that no one in entire Domain knew about this project." +
                                "They decided have that the Persean Sector will be perfect place for such projects, due to low presence of Domain's authorities.", INFORMATIVE, 10f);
                        tooltip.addPara("Project Charon was centered around constructing a new type of a core, the Theta core." +
                                "But these cores weren't supposed to be an inhabited by an AI, the goal of Project Charon was not about making a new type of AI cores. ", INFORMATIVE, 10f);
                        tooltip.addPara("It's true intention was transferring consciousness of human being into Theta core, resulting in AI core with human personality, behaviors and perhaps even consciousness." +
                                "Curiously, as it turns out consciousness is only getting copied over to the core instead of being transferred, and the strain put on brain of the human subject during the copying process is guaranteed to kill them. Yet the Theta cores have been proven to be fully functional." +
                                "Unfortunately, further experiments were abandoned, due to lack of further funding after this failure. ", INFORMATIVE, 10f);
                        tooltip.addPara("Along with this data, we also found a location of a testing site, where some theta cores might be located.", INFORMATIVE, 10f);
                        tooltip.addPara("Gain location of planet, where Theta Cores might be located", POSITIVE, 10f);
                        List<StarSystemAPI> starSystemAPIList = Global.getSector().getStarSystems();
                        Collections.shuffle(starSystemAPIList,new Random());
                        if(!assignedSystem){
                            for (StarSystemAPI starSystemAPI : starSystemAPIList) {
                                boolean breaken= false;
                                for (PlanetAPI planet : starSystemAPI.getPlanets()) {
                                    if (planet.isStar()) continue;
                                    if (!planet.getMarket().isPlanetConditionMarketOnly()) continue;
                                    if (planet.hasTag(Tags.NOT_RANDOM_MISSION_TARGET)) continue;
                                    if (planet.hasTag(Tags.MISSION_ITEM)) continue;
                                    if (planet.isGasGiant()) continue;

                                    if(!planet.getMarket().getSurveyLevel().equals(MarketAPI.SurveyLevel.NONE))continue;
                                    if(planet.getMemoryWithoutUpdate().contains("$aotd_quest_veil"))continue;
                                    planet.getMemoryWithoutUpdate().set(MemFlags.SALVAGE_SPEC_ID_OVERRIDE, "aotd_quest_theta");
                                    planet.getMemory().set("$aotd_quest_theta",true);
                                    Global.getSector().getIntelManager().addIntel(new ProjectCheronIntel(planet));
                                    breaken=true;
                                    assignedSystem=true;
                                    break;

                                }
                                if(breaken)break;
                            }
                        }



                    }
                    break;


                case "bs_fac_op2":
                    if (!visitedMainServerRoom) {
                        tooltip.addPara("We have found an encryption key in this room, possibly for the encrypted data that is located in main server room.", POSITIVE, 10f);
                        tooltip.addPara("Besides that, we haven't found anything useful there.", INFORMATIVE, 10f);
                    } else {
                        tooltip.addPara("Unfortunately we haven't found anything valuable there.", NEGATIVE, 10f);
                    }

                    break;
                case "bs_fac_op3":
                    tooltip.addPara("Our investigation of the storage room has found a storage filled with Alpha Class AI cores, that have been transported to the local storage.", POSITIVE, 10f);
                    if(!gotCores){
                        currentlyAffectedMarket.getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo().addCommodity("alpha_core",5);
                        gotCores=true;
                    }
                    break;
            }
            if (hasNotExplored()) {
                tooltip.addPara("For the past month, dedicated team of our scientists has been immersed in exploration of enigmatic remnants\n" +
                        "of the research facility.", INFORMATIVE, 10f);
                tooltip.addPara("While we have meticulously mapped most of the facility, the true treasures within remain unknown.\n" +
                        "The time has come for us to make a decision - to determine which part of this complex we shall venture into first.", INFORMATIVE, 10f);
            } else {
                AoTDColonyEvent event = AoTDColonyEventManager.getInstance().getEventById("theta_finding_facility");
                if (event.prevDecisionId.equals("bs_op1")&&!finishedExplorationSection) {
                    tooltip.addPara("Unfortunately due to damage that facility's structural integrity suffered from activity of our expedition team, we have to choose only one of the two remaining " +
                            "areas of the facility to explore.", NEGATIVE, 10f);
                }
            }

        }
        super.generateDescriptionOfEvent(tooltip);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId) {
            case "bs_op1":
                tooltip.addPara("Lose 100.000 Credits", NEGATIVE, 10f);
                tooltip.addPara("Our team will be equipped only with the tools deemed necessary for this expedition.", INFORMATIVE, 10f);
                break;
            case "bs_op2":
                tooltip.addPara("Lose 500.000 Credits", NEGATIVE, 10f);
                tooltip.addPara("Our expedition will be equipped with state-of-the-art tools for the purpose of exploring this facility.", INFORMATIVE, 10f);
                break;
            case "bs_op3":
                tooltip.addPara("We won't fund this expedition at all, it's a waste of resources.", INFORMATIVE, 10f);
                break;
            case "bs_fac_op":
                tooltip.addPara("We shall continue the exploration, with hope of getting more information about Project Charon.", INFORMATIVE, 10f);
                break;
            case "bs_fac_op1":
                tooltip.addPara("We will explore heart of this research facility, in hopes of finding valuable data.", INFORMATIVE, 10f);
                break;
            case "bs_fac_op2":
                tooltip.addPara("We will explore the office of the supposed director of this facility.", INFORMATIVE, 10f);
                break;
            case "bs_fac_op3":
                tooltip.addPara("We will explore and salvage the massive storage area.", INFORMATIVE, 10f);
                break;
            case "bs_fac_op_finish":
                tooltip.addPara("There is nothing more left for us to explore.", INFORMATIVE, 10f);
                break;

        }


    }

    @Override
    public void executeDecision(String currentDecision) {
        switch (currentDecision) {
            case "bs_op1":
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("theta_expl_fac", currentlyAffectedMarket.getId(), 14);
                Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(100000);
                break;
            case "bs_op2":
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("theta_expl_fac", currentlyAffectedMarket.getId(), 21);
                Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(500000);
                break;
            case "bs_fac_op":
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("theta_expl_fac_continue", currentlyAffectedMarket.getId(), 10);
                break;
            case "bs_fac_op1":
                visitedMainServerRoom = true;
                previiousExpedition = "bs_fac_op1";
                exploreFurther(12);
                break;
            case "bs_fac_op2":
                visitedDirectorRoom = true;
                previiousExpedition = "bs_fac_op2";
                exploreFurther(4);
                break;
            case "bs_fac_op3":
                visitedMagazine = true;
                previiousExpedition = "bs_fac_op3";
                exploreFurther(15);
                break;
            case "bs_fac_op_finish":
                AoTDColonyEventManager.getInstance().lastEvent = -20;
                break;

        }

        super.executeDecision(currentDecision);
    }

    void exploreFurther(float amountOfDays) {
        AoTDColonyEvent event = AoTDColonyEventManager.getInstance().getEventById("theta_finding_facility");
        int initalAmountOfChances = 2;

        if (event.prevDecisionId.equals("bs_op2")) {
            initalAmountOfChances++;
        }

        if (visitedMagazine) initalAmountOfChances--;
        if (visitedDirectorRoom) initalAmountOfChances--;
        if (visitedMainServerRoom) initalAmountOfChances--;
        AoTDColonyEventManager.getInstance().addGuaranteedEvent("theta_expl_fac_continue",currentlyAffectedMarket.getId(),amountOfDays);
        if (initalAmountOfChances <= 0) {
            this.finishedExplorationSection = true;
        }

    }

    @Override
    public void overrideOptions() {
        if (finishedExplorationSection) {
            loadedOptions.clear();
            loadedOptions.put("bs_fac_op_finish", "Acknowledged");
        }
        else{
            if(visitedMainServerRoom){
                loadedOptions.remove("bs_fac_op1");
            }
            if(visitedDirectorRoom){
                loadedOptions.remove("bs_fac_op2");
            }
            if(visitedMagazine){
                loadedOptions.remove("bs_fac_op3");
            }
        }
    }


    boolean hasNotExplored() {
        return !visitedMagazine && !visitedDirectorRoom && !visitedMainServerRoom;
    }
}
