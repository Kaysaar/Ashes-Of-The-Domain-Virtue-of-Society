package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;
import data.colonyevents.manager.AoTDColonyEventManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SandsOfTimeEvent extends AoTDColonyEvent {
    public static ArrayList<String> ITEMS = new ArrayList();
    static {
        ITEMS.add("corrupted_nanoforge");
        ITEMS.add("synchrotron");
        ITEMS.add("orbital_fusion_lamp");
        ITEMS.add("coronal_portal");
        ITEMS.add("mantle_bore");
        ITEMS.add("catalytic_core");
        ITEMS.add("soil_nanites");
        ITEMS.add("biofactory_embryo");
        ITEMS.add("fullerene_spool");
        ITEMS.add("plasma_dynamo");
        ITEMS.add("cryoarithmetic_engine");
        ITEMS.add("drone_replicator");
        ITEMS.add("dealmaker_holosuite");
    }
    public AoTDColonyEvent prevEvent = null;
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        String eventId = this.getSpec().getEventId();
        if (eventId.equals("accidental_finding")) {
            boolean hasTag = false;
            for (Industry industry : marketAPI.getIndustries()) {
                if (industry.getSpec().hasTag("mining")) {
                    hasTag = true;
                }
            }

            return marketAPI.hasCondition(Conditions.RUINS_VAST) && hasTag;
        }
        if (eventId.equals("accidental_finding_vault")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        if (eventId.equals("accidental_finding_vault_hacking")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        if (eventId.equals("accidental_finding_vault_trigger_defences")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        if (eventId.equals("accidental_finding_vault_outcome")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        if (eventId.equals("accidental_finding_vault_conclusion")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        if (eventId.equals("accidental_finding_vault_antimatter")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        if (eventId.equals("accidental_finding_vault_antimatter_outcome")) {
            String id = AoTDColonyEventManager.getInstance().guaranteedNextEventId;
            return id != null && id.equals(this.getSpec().getEventId());
        }
        return super.canOccur(marketAPI);
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
        String eventId = this.getSpec().getEventId();
        if (eventId.equals("accidental_finding")) {
            tooltip.addPara("Administrator, during recent mining expeditions we have discovered hidden vault." +
                    "Our scientists indicate, that this vault may be from Pre Collapse era. Some people suggest leaving this vault as it is, but our scientist believe, that this vault might have cointain some valuable things.", 10f);
        }
        if (eventId.equals("accidental_finding_vault")) {
            tooltip.addPara("Administrator, our team has began to unseal doors to vault, that we have recently discovered.However those doors seems to be impressively strong.There has been suggestions of using antimatter charges to blast those doors. One scientist from expedition also suggested,that it's possible to " +
                    "hack vault doors.However there are voices of concern about unsealing this vault, especially that if something was sealed, then should we open it ?", 10f);
        }
        if (eventId.equals("accidental_finding_vault_hacking")) {
            tooltip.addPara("Administrator hacking was success facility is dormant what should we do?", 10f);
        }
        if (eventId.equals("accidental_finding_vault_trigger_defences")) {
            tooltip.addPara("Unfortunately our actions have awakened Automated Defence System and fighting spread through entire mining. ", 10f);
        }

        if (eventId.equals("accidental_finding_vault_outcome")) {
            if(AoTDColonyEventManager.getInstance().getEventById(AoTDColonyEventManager.getInstance().previousGuaranteedEventId).prevDecisionId.equals("accidental_finding_hacking_op1")){
                tooltip.addPara("Administrator our expedition was a success, we have not suffered any loses and we were able to retrieve certain artifacts", 10f);
            }
            else{
                tooltip.addPara("Administrator our expedition was a success, unfortunately we have suffered heavy loses. At least we have retrieved a lot of artifacts from vault.", 10f);
            }

        }
        if(eventId.equals("accidental_finding_vault_antimatter")){
            tooltip.addPara("Administrator, explosion caused by antimatter charges caused huge tectonic instability. We must evacuate team NOW!", Color.ORANGE, 10f);

        }
        if(eventId.equals("accidental_finding_vault_antimatter_outcome")){
            tooltip.addPara("Administrator, our team has not made in time to escape, moreover this explosion collapsed one of major settlements in our colony , disrupting all mining operations , and killings thousands of thousands of people", Color.ORANGE, 10f);

        }

    }

    @Override
    public void executeDecision(String currentDecision) {
        String eventId = this.getSpec().getEventId();
        if (eventId.equals("accidental_finding")) {
            prevDecisionId = currentDecision;
            if (currentDecision.equals("accidental_finding_op1")) {
                AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault";
                AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent = 7;
                AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;
            }
        }
        if (eventId.equals("accidental_finding_vault")) {
            prevDecisionId = currentDecision;
            if (currentDecision.equals("vault_op1")) {
                AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault_hacking";
                AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent = 4;
                AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;
            } else if (currentDecision.equals("vault_op2")) {
                AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault_antimatter";
                AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent=12;
                AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;
            }
        }
        if (eventId.equals("accidental_finding_vault_hacking")) {
            prevDecisionId = currentDecision;
            AoTDColonyEventManager.getInstance().previousGuaranteedEventId = this.getSpec().getEventId();
            if (currentDecision.equals("accidental_finding_hacking_op1")) {
                AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault_outcome";
                AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent = 8;
                AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;
            } else {
                AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault_trigger_defences";
                AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent = 10;
                AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;
            }
        }
        if(eventId.equals("accidental_finding_vault_trigger_defences")){
            prevDecisionId = currentDecision;
            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                if(industry.getSpec().hasTag("mining")){
                    industry.setDisrupted(180,false);
                }
            }
            Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(300000);
            AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault_outcome";
            AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent = 14;
            AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;

        }
        if(eventId.equals("accidental_finding_vault_outcome")){
        prevEvent = AoTDColonyEventManager.getInstance().getEventById(AoTDColonyEventManager.getInstance().previousGuaranteedEventId);
            ArrayList<String> list = new ArrayList<>(ITEMS);
            Collections.shuffle(list);
            if(prevEvent.prevDecisionId.equals("accidental_finding_hacking_op1")){
                CargoAPI cargoAPI = currentlyAffectedMarket.getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo();
                cargoAPI.addSpecial(new SpecialItemData(Items.PRISTINE_NANOFORGE,null),1);
                for(int i=0;i<2;i++){
                    cargoAPI.addSpecial(new SpecialItemData(list.get(i),null),1);
                }
            }
            else{
                CargoAPI cargoAPI = currentlyAffectedMarket.getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo();
                for(int i=0;i<4;i++){
                    cargoAPI.addSpecial(new SpecialItemData(list.get(i),null),1);
                }

            }
            AoTDColonyEventManager.getInstance().guaranteedNextEventId = null;
            AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = null;
            AoTDColonyEventManager.getInstance().lastEvent = -40;
        }
        if(eventId.equals("accidental_finding_vault_antimatter")){
            prevDecisionId = currentDecision;
            AoTDColonyEventManager.getInstance().guaranteedNextEventId = "accidental_finding_vault_antimatter_outcome";
            AoTDColonyEventManager.getInstance().breakFromAnotherStageOfEvent = 14;
            AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = currentlyAffectedMarket;
        }
        if(eventId.equals("accidental_finding_vault_antimatter_outcome")){
            prevDecisionId = currentDecision;
            int size =  currentlyAffectedMarket.getSize();
            if(size>=4){
                currentlyAffectedMarket.setSize(size-1);
                currentlyAffectedMarket.removeCondition("popluation_"+size);
                currentlyAffectedMarket.addCondition("population_"+(size-1));
                currentlyAffectedMarket.getPopulation().setWeight(0);
            }

            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                if(industry.getSpec().hasTag("mining")){
                    industry.setDisrupted(180,false);
                }
            }
            currentlyAffectedMarket.reapplyConditions();
            AoTDColonyEventManager.getInstance().guaranteedNextEventId = null;
            AoTDColonyEventManager.getInstance().guaranteedNextEventMarket = null;
            AoTDColonyEventManager.getInstance().lastEvent = 0;
            super.executeDecision(currentDecision);
        }



    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId) {
            case "accidental_finding_op1":
                tooltip.addPara("Our team shall start working on unsealing those gates, to uncover mysteries of " + currentlyAffectedMarket.getName() + " vault", Misc.getHighlightedOptionColor(), 15f);
                break;
            case "accidental_finding_op2":
                tooltip.addPara("We will place explosive charges to fully seal entrance to vault, never to be discovered again", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "vault_op1":
                tooltip.addPara("Team will start cracking cyber defences, to open vault", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "vault_op2":
                tooltip.addPara("Expedition team will carefully planet antimatter charges to destroy doors", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "vault_op3":
                tooltip.addPara("We will seal path to this vault forever!", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "accidental_finding_hacking_op1":
                tooltip.addPara("Our team will only use small portion of power to power up only basic systems of facility", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "accidental_finding_hacking_op2":
                tooltip.addPara("We will fully power enter facility to access it's entirety", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "accidental_finding_fighting_op1":
                tooltip.addPara("Lose 300.000 Credits\nDisrupt mining for 120 days", Misc.getNegativeHighlightColor(), 10f);
                break;
            case "accidental_finding_conclusion_op1":
                prevEvent = AoTDColonyEventManager.getInstance().getEventById(AoTDColonyEventManager.getInstance().previousGuaranteedEventId);
                if(prevEvent.prevDecisionId.equals("accidental_finding_hacking_op1")){
                    tooltip.addPara("Gained 3 artefacts one of them being Nanoforge with Pristine quality.\nItems have been transported to "+currentlyAffectedMarket.getName()+" local storage", Misc.getPositiveHighlightColor(), 10f);

                }
                else{
                    tooltip.addPara("Gained 4 artefacts.\nItems have been transported to "+currentlyAffectedMarket.getName()+" local storage", Misc.getPositiveHighlightColor(), 10f);

                }
                break;
            case "accidental_finding_antimatter_op1":
                tooltip.addPara("WE MUST EVACUATE NOW!!!", Misc.getNegativeHighlightColor(), 10f);


                break;
            case "accidental_finding_antimatter_outcome_op1":
                tooltip.addPara("Reduce size of "+currentlyAffectedMarket.getName()+ " by one if size is greater than 3\nDistrupt all mining operations for 180 days", Misc.getNegativeHighlightColor(), 10f);

                break;
        }

    }
}
