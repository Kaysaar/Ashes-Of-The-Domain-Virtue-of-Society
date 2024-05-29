package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.comm.CommMessageAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.impl.campaign.intel.MessageIntel;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class EternalSlumberEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return marketAPI.hasIndustry(Industries.TECHMINING);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("One of scavenger teams located on "+currentlyAffectedMarket.getName()+" has located an entrance to what seems to be an " +
                "ancient vault. After closer inspection it was determined to be very old Cryosanctum, it's construction dating all the way back to Pre-Collapse era." +
                "\nWhat should we do with this discovery?",10f);

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
       if(optionId.equals("es_op1")){
           tooltip.addPara("We shall seal the entrance to this vault, leaving people here in eternal, peaceful slumber.", Misc.getTooltipTitleAndLightHighlightColor(),10f);
       }
        if(optionId.equals("es_op2")){
            tooltip.addPara("Recieve Cryosanctum structure on "+currentlyAffectedMarket.getName(), Misc.getPositiveHighlightColor(),10f);
            tooltip.addPara("-3 Stability on "+currentlyAffectedMarket.getName()+" for 365 days", Misc.getNegativeHighlightColor(),10f);
        }
        if(optionId.equals("es_op3")){
            tooltip.addPara("Increase population of "+currentlyAffectedMarket.getName()+" by 1.",Misc.getPositiveHighlightColor(),10f);
            tooltip.addPara("Increase food consumption by 3 units for 365 days",Misc.getNegativeHighlightColor(),10f);
            tooltip.addPara("Decrease colony's income by 20% for 365 days",Misc.getNegativeHighlightColor(),10f);
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("es_op2")){
            currentlyAffectedMarket.addCondition("cryosanctum_event_organs");
            currentlyAffectedMarket.addIndustry(Industries.CRYOSANCTUM);
        }
        if(currentDecision.equals("es_op3")){
            int size = currentlyAffectedMarket.getSize();
            currentlyAffectedMarket.addCondition("cryosanctum_event_growth");
            currentlyAffectedMarket.removeCondition("population_" + currentlyAffectedMarket.getSize());
            currentlyAffectedMarket.setSize(size + 1);
            currentlyAffectedMarket.addCondition("population_" + currentlyAffectedMarket.getSize());
            currentlyAffectedMarket.getPopulation().setWeight(0);
            MessageIntel intel = new MessageIntel("Colony Growth - " + this.currentlyAffectedMarket.getName(), Misc.getBasePlayerColor());
            intel.addLine("    - Size increased to %s", Misc.getTextColor(), new String[]{"" + Math.round((float) this.currentlyAffectedMarket.getSize())}, new Color[]{Misc.getHighlightColor()});
            intel.setIcon(Global.getSector().getPlayerFaction().getCrest());
            intel.setSound(BaseIntelPlugin.getSoundMajorPosting());
            Global.getSector().getCampaignUI().addMessage(intel, CommMessageAPI.MessageClickAction.COLONY_INFO, this.currentlyAffectedMarket);
        }
        super.executeDecision(currentDecision);
    }

}
