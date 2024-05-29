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

        return false;
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
            tooltip.addPara("Administrator, during our recent mining expeditions we have discovered a hidden vault." +
                    "Our scientists indicate, that this vault might be from the Pre Collapse era. Some people suggest leaving this vault as it is, but our scientist believe, that this vault might have cointain some valuable things.", 10f);
        }
        if (eventId.equals("accidental_finding_vault")) {
            tooltip.addPara("Administrator, our team has began to unseal the vault that we have recently discovered. However those doors turn out to be surprisingly strong. There have been suggestions of using antimatter charges to blast those doors open. One scientist from our expedition also suggested that it could be possible to " +
                    "hack the vault doors. However there also are some voices of concern about unsealing this vault, if something was sealed maybe there was a good reason for it?", 10f);
        }
        if (eventId.equals("accidental_finding_vault_hacking")) {
            tooltip.addPara("Administrator, the hacking was successful, the facility appears to be dormant, what should we do next?", 10f);
        }
        if (eventId.equals("accidental_finding_vault_trigger_defences")) {
            tooltip.addPara("Unfortunately our actions have activated vault's Automated Defence Systems and now our security forces are engaged in fights across the entire mine.", 10f);
        }

        if (eventId.equals("accidental_finding_vault_outcome")) {
            if(AoTDColonyEventManager.getInstance().getEventById(AoTDColonyEventManager.getInstance().previousGuaranteedEventId).prevDecisionId.equals("accidental_finding_hacking_op1")){
                tooltip.addPara("Administrator, our expedition was a success, we haven't suffered any loses and we were able to retrieve some artifacts from the vault.", 10f);
            }
            else{
                tooltip.addPara("Administrator, our expedition was a success, but unfortunately we have suffered heavy losses. At least we managed to retrieve some artifacts from vault.", 10f);
            }

        }
        if(eventId.equals("accidental_finding_vault_antimatter")){
            tooltip.addPara("Administrator, explosion caused by antimatter charges caused massive structural damage to the mine. We must evacuate the expedition team NOW!", Color.ORANGE, 10f);

        }
        if(eventId.equals("accidental_finding_vault_antimatter_outcome")){
            tooltip.addPara("Administrator, our expedition team couldn't escape before the mine has collapsed, moreover this explosion collapsed one of major settlements in our colony, disrupting all of our mining operations and killing thousands of people.", Color.ORANGE, 10f);

        }

    }

    @Override
    public void executeDecision(String currentDecision) {
        String eventId = this.getSpec().getEventId();
        if (eventId.equals("accidental_finding")) {
            prevDecisionId = currentDecision;
            if (currentDecision.equals("accidental_finding_op1")) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault",currentlyAffectedMarket.getId(),7);
            }
            else{
                AoTDColonyEventManager.getInstance().lastEvent = 0;
                super.executeDecision(currentDecision);
            }
        }
        if (eventId.equals("accidental_finding_vault")) {
            prevDecisionId = currentDecision;
            if (currentDecision.equals("vault_op1")) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault_hacking",currentlyAffectedMarket.getId(),4);
            } else if (currentDecision.equals("vault_op2")) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault_antimatter",currentlyAffectedMarket.getId(),12);
            }
        }
        if (eventId.equals("accidental_finding_vault_hacking")) {
            prevDecisionId = currentDecision;
            AoTDColonyEventManager.getInstance().previousGuaranteedEventId = this.getSpec().getEventId();
            if (currentDecision.equals("accidental_finding_hacking_op1")) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault_outcome",currentlyAffectedMarket.getId(),8);
            } else {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault_trigger_defences",currentlyAffectedMarket.getId(),10);
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
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault_outcome",currentlyAffectedMarket.getId(),14);

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
            AoTDColonyEventManager.getInstance().lastEvent = -40;
        }
        if(eventId.equals("accidental_finding_vault_antimatter")){
            prevDecisionId = currentDecision;
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("accidental_finding_vault_antimatter_outcome",currentlyAffectedMarket.getId(),14);
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
            AoTDColonyEventManager.getInstance().lastEvent = 0;
            super.executeDecision(currentDecision);
        }



    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId) {
            case "accidental_finding_op1":
                tooltip.addPara("Our team shall start working on unsealing these doors to uncover mysteries of " + currentlyAffectedMarket.getName() + " vault.", Misc.getHighlightedOptionColor(), 15f);
                break;
            case "accidental_finding_op2":
                tooltip.addPara("We will collapse this part of the mine in order to fully seal entrance to vault, so it may never be discovered again.", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "vault_op1":
                tooltip.addPara("Our team will start cracking the code that protects the vault's doors.", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "vault_op2":
                tooltip.addPara("Our expedition team will carefully plant antimatter charges to destroy the door.", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "vault_op3":
                tooltip.addPara("We will seal the path to this vault forever!", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "accidental_finding_hacking_op1":
                tooltip.addPara("Our team will only use a small amount of power that will be enough to power up only the most basic systems of the facility.", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "accidental_finding_hacking_op2":
                tooltip.addPara("We will fully power the entire facility to access it in its entirety", Misc.getHighlightedOptionColor(), 10f);
                break;
            case "accidental_finding_fighting_op1":
                tooltip.addPara("Lose 300.000 Credits\nDisrupt mining operations for 120 days", Misc.getNegativeHighlightColor(), 10f);
                break;
            case "accidental_finding_conclusion_op1":
                prevEvent = AoTDColonyEventManager.getInstance().getEventById(AoTDColonyEventManager.getInstance().previousGuaranteedEventId);
                if(prevEvent.prevDecisionId.equals("accidental_finding_hacking_op1")){
                    tooltip.addPara("Acquired 3 artifacts, with one of them being a Nanoforge of Pristine quality.\nThe items have been transported to "+currentlyAffectedMarket.getName()+"'s local storage", Misc.getPositiveHighlightColor(), 10f);

                }
                else{
                    tooltip.addPara("Acquired 4 artifacts.\nThe items have been transported to "+currentlyAffectedMarket.getName()+"'s local storage", Misc.getPositiveHighlightColor(), 10f);

                }
                break;
            case "accidental_finding_antimatter_op1":
                tooltip.addPara("WE MUST EVACUATE NOW!!!", Misc.getNegativeHighlightColor(), 10f);


                break;
            case "accidental_finding_antimatter_outcome_op1":
                tooltip.addPara("Reduce colony size of "+currentlyAffectedMarket.getName()+ " by one if size is greater than 3\nDistrupt all mining operations for 180 days", Misc.getNegativeHighlightColor(), 10f);

                break;
        }

    }
}
