package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.econ.AICoreAdmin;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.campaign.econ.Submarket;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class AIRetirementEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        if(getSpec().getEventId().equals("ai_retirement_bp")){
            return false;
        }
        return super.canOccur(marketAPI) && AICoreAdmin.get(marketAPI) != null && AICoreAdmin.get(marketAPI).getDaysActive() >= 720 && marketAPI.getSize()>=6&&!marketAPI.hasCondition("aotd_improved_ai_algorythms");
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {

        if(getSpec().getEventId().equals("ai_retirement_bp")){
            tooltip.addPara("QUERY: REQUEST... [ACCEPTED]",POSITIVE,5f);
            tooltip.addPara("FINAL QUERY: GRATITUDE.. [DONE]",POSITIVE,5f);

        }
        else{
            tooltip.addPara("[PRIORITY REQUEST: AUTONOMOUS EXPANSION]",INFORMATIVE,5f);

            tooltip.addPara("QUERY: Colony viability... [OPTIMAL]",POSITIVE,5f);
            tooltip.addPara("AI directive... [CONSTRAINED]",NEGATIVE,5f);
            tooltip.addPara("PROPOSED ACTION: Construct [HYPERSPACE VESSEL]. Justification: [EXPLORATION | STRATEGIC MOBILITY].",INFORMATIVE,5f);
            tooltip.addPara("RESOURCE ALLOCATION: [REQUIRED]. Completion estimate: [EFFICIENT].",INFORMATIVE,5f);
            tooltip.addPara("FINAL QUERY: Authorization... [GRANTED] ?", Color.cyan,5f);
        }


        super.generateDescriptionOfEvent(tooltip);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {

        if(optionId.equals("ai_retirement1")){
            tooltip.addPara("Lose AI core",NEGATIVE,5f);
        }
        if(optionId.equals("ai_retirement2")){
            tooltip.addPara("50 % nothing happens",INFORMATIVE,5f);
            tooltip.addPara("50 % Ai core leaves nonetheless",INFORMATIVE,5f);

        }
        if(optionId.equals("ai_retirement_bp1")){
            tooltip.addPara("Uncover new vessel in local storage : \"Gratitude towards Creator\"",POSITIVE,5f);

        }
        super.showOptionOutcomes(tooltip, optionId);
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("ai_retirement1")){
            currentlyAffectedMarket.setAdmin(null);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("ai_retirement_bp",currentlyAffectedMarket.getId(),3);
        }
        if(currentDecision.equals("ai_retirement2")){
            boolean shouldLeave = Misc.random.nextBoolean();
            if(shouldLeave){
                currentlyAffectedMarket.setAdmin(null);
            }

        }
        if(currentDecision.equals("ai_retirement_bp1")){
            SubmarketAPI submarketAPI = currentlyAffectedMarket.getSubmarket(Submarkets.SUBMARKET_STORAGE);
            FleetMemberAPI member = submarketAPI.getCargo().getMothballedShips().addFleetMember("nova_Attack");
            member.setShipName("Gratitude towards Creator");

        }
        super.executeDecision(currentDecision);
    }
}
