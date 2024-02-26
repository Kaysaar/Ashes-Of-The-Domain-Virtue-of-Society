package data.colonyevents.models;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.manager.AoTDColonyEventManager;

import java.awt.*;
import java.util.HashMap;

public class AoTDColonyEvent  {
    public static Color INFORMATIVE = Misc.getTooltipTitleAndLightHighlightColor();
    public static Color POSITIVE = Misc.getPositiveHighlightColor();
    public static Color NEGATIVE = Misc.getNegativeHighlightColor();
    protected AoTDColonyEventSpec spec;
    public int currentDurationOfEvent = 0;
    public boolean haveFiredAtLeastOnce = false;
    public float cooldownBeforeEventCanOccur = 0;
    public boolean isOnGoing = false;
    public MarketAPI currentlyAffectedMarket=null;
    public boolean isWaitingForDecision = false;
    public AoTDColonyEvent nextEventAfter = null;
    public HashMap<String,String> loadedOptions;
    public boolean hasCompletedEntireEventChain = false;

    public float daysToMakeDecision = AoTDColonyEventManager.maxTimeToMakeDecision;
    public String prevDecisionId = null;

    public AoTDColonyEventSpec getSpec() {
        return this.spec;
    }

    public void setSpec(AoTDColonyEventSpec spec) {
        this.spec = spec;
    }
    public void setLoadedOptions(){
        this.loadedOptions = this.spec.options;
    }
    public HashMap<String,String> getLoadedOptions(){
        return this.loadedOptions;
    }
    public void apply() {

    }
    public void unapply(){

    }
    public void advance(float amount){
        apply();
        unapply();
    }
    public boolean canOccur(MarketAPI marketAPI){
        return !marketAPI.hasCondition("fired_event");
    }
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip){

    }
    public void executeDecision(String currentDecision){
    }
     public void fullyExecuteDecision(String currentDecision){
        executeDecision(currentDecision);
         currentlyAffectedMarket.addCondition("fired_event");
         prevDecisionId = currentDecision;
         this.haveFiredAtLeastOnce = true;
         currentlyAffectedMarket=null;
     }

    public void overrideOptions(){

    }
    public boolean isOptionValid(String optionId){
        return optionId!=null;
    }
    public boolean canShowOptionOutcomesBeforeDeciding(){
        return true;
    }
    public void showOptionOutcomes(TooltipMakerAPI tooltip,String optionId){

    }
}
