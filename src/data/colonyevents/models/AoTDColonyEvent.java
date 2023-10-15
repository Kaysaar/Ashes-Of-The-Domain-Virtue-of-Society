package data.colonyevents.models;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.manager.AoTDColonyEventManager;

public class AoTDColonyEvent  {
    protected AoTDColonyEventSpec spec;
    public int currentDurationOfEvent = 0;
    public boolean haveFiredAtLeastOnce = false;
    public float cooldownBeforeEventCanOccur = 0;
    public boolean isOnGoing = false;
    public MarketAPI currentlyAffectedMarket=null;
    public boolean isWaitingForDecision = false;
    public AoTDColonyEvent nextEventAfter = null;

    public float daysToMakeDecision = AoTDColonyEventManager.maxTimeToMakeDecision;
    public String prevDecisionId = null;

    public AoTDColonyEventSpec getSpec() {
        return this.spec;
    }

    public void setSpec(AoTDColonyEventSpec spec) {
        this.spec = spec;
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
        currentlyAffectedMarket.addCondition("fired_event");
        this.haveFiredAtLeastOnce = true;
    }
    public boolean canShowOptionOutcomesBeforeDeciding(){
        return true;
    }
    public void showOptionOutcomes(TooltipMakerAPI tooltip,String optionId){

    }
}
