package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import org.lazywizard.lazylib.MathUtils;

import java.util.HashMap;

public class CrimeSyndicateEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        if(this.getSpec().getEventId().equals("crime_syndicate_beginning")){
            return super.canOccur(marketAPI)&&!haveCrimeSyndicateCondition(marketAPI)&&!marketAPI.getMemory().contains("$aotd_had_crime_event")&&marketAPI.getPrevStability()<=2;
        }
        return false;
    }
    public boolean haveCrimeSyndicateCondition(MarketAPI marketAPI){
        return marketAPI.hasCondition("crime_syndicate_beginning")&& marketAPI.hasCondition("crime_syndicate_stabilize")&&marketAPI.hasCondition("crime_syndicate_resolve_deal")&&marketAPI.hasCondition("crime_syndicate_resolve_violence");
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        super.generateDescriptionOfEvent(tooltip);
        if(this.spec.getEventId().equals("crime_syndicate_beginning")){
            tooltip.addPara("Administrator, recent unrest on "+currentlyAffectedMarket.getName()+ "led to creation of a massive crime syndicate, whose influence spreads across the entire world.", Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Crime rate has gone rampant and our police forces can't contain this massive threat.",Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Currently this world is in full disarray.",Misc.getNegativeHighlightColor(),10f);
        }
        if(this.spec.getEventId().equals("crime_syndicate_resolve")&&currentlyAffectedMarket.getStabilityValue()<7){
            tooltip.addPara("Administrator, recent unrest on "+currentlyAffectedMarket.getName()+ "led to creation of a massive crime syndicate, whose influence spreads across the entire world.", Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Few months have passed and we have failed to establish order. In this time the crime syndicate kept on amassing power.",Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Because of this one of crime lords came here, with proposal for us.",Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("We can allow them to continue their operations unopposed, and in return they promise to bring back order to streets.",Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Alternatively, one of our generals proposed to gather a elite strike force to fully eliminate the crime syndicate, but it will temporaily cause much more unrest as planetwide eradication of the syndicate's hideouts ensues.",Misc.getNegativeHighlightColor(),10f);

        }
        if(this.spec.getEventId().equals("crime_syndicate_resolve")&&currentlyAffectedMarket.getStabilityValue()>=7){
            tooltip.addPara("Administrator, recent unrest on "+currentlyAffectedMarket.getName()+ "led to creation of a massive crime syndicate, whose influence spreads across the entire world.", Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Few months have passed and we have successfully reestablished order on this planet. It may once again prosper under justice.",Misc.getPositiveHighlightColor(),10f);

        }
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        super.showOptionOutcomes(tooltip, optionId);
        switch (optionId){
            case "crime_beginning":
                tooltip.addPara("-3 Stability and -40% accessibility until situation improves.",Misc.getNegativeHighlightColor(),10f);
                tooltip.addPara("We have between 180 to 250 days to deal with rampant crime.",Misc.getNegativeHighlightColor(),10f);
                break;
            case "crime_syndicate_deal":
                tooltip.addPara("-1 Stability and -20% accessibility ",Misc.getNegativeHighlightColor(),10f);
                tooltip.addPara("1.5x multiplier towards defences",Misc.getPositiveHighlightColor(),10f);
                tooltip.addPara("Gain 10.000 to 40.000 credits per month from the black market trade.",Misc.getPositiveHighlightColor(),10f);
                tooltip.addPara("Remove Rampant Crime condition.",Misc.getPositiveHighlightColor(),10f);

                break;
            case "crime_syndicate_rampage":
                tooltip.addPara("-5 Stability and -20% accessibility for 600 days.",Misc.getNegativeHighlightColor(),10f);
                tooltip.addPara("Remove Rampant Crime condition.",Misc.getPositiveHighlightColor(),10f);

                break;
            case "crime_syndicate_resolved":
                tooltip.addPara("Permanent +1 stability on market and 5% accessibility bonus.",Misc.getPositiveHighlightColor(),10f);
                tooltip.addPara("Remove Rampant Crime condition.",Misc.getPositiveHighlightColor(),10f);

                break;

        }
    }

    @Override
    public void overrideOptions() {
        if(this.spec.getEventId().equals("crime_syndicate_resolve")){
            if(this.currentlyAffectedMarket.getStabilityValue()>=7){
                this.loadedOptions.clear();
                this.loadedOptions.put("crime_syndicate_resolved","We have persisted through this crisis.");
            }
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("crime_beginning")){
            currentlyAffectedMarket.addCondition("crime_syndicate_beginning");
            currentlyAffectedMarket.getMemory().set("$aotd_had_crime_event",true);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("crime_syndicate_resolve",currentlyAffectedMarket.getId(), MathUtils.getRandomNumberInRange(180,250));

        }
        if(currentDecision.equals("crime_syndicate_deal")){
            currentlyAffectedMarket.removeCondition("crime_syndicate_beginning");
            currentlyAffectedMarket.addCondition("crime_syndicate_resolve_deal");

        }
        if(currentDecision.equals("crime_syndicate_rampage")){
            currentlyAffectedMarket.removeCondition("crime_syndicate_beginning");
            currentlyAffectedMarket.addCondition("crime_syndicate_resolve_violence");

        }
        if(currentDecision.equals("crime_syndicate_resolved")){
            currentlyAffectedMarket.removeCondition("crime_syndicate_beginning");
            currentlyAffectedMarket.addCondition("crime_syndicate_stabilize");

        }
        super.executeDecision(currentDecision);
    }
}
