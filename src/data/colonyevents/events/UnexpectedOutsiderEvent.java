package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.Ids.AoDIndustries;
import data.Ids.AodResearcherSkills;
import data.colonyevents.models.AoTDColonyEvent;
import data.plugins.AoDUtilis;

public class UnexpectedOutsiderEvent extends AoTDColonyEvent {
    // Silly AoTD - ACOT collab - this can only fire once

    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean hasDimProc = false;
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {
            if(playerMarket.getIndustry(AoDIndustries.RESEARCH_CENTER)!=null){
                SpecialItemData itemData = playerMarket.getIndustry(AoDIndustries.RESEARCH_CENTER).getSpecialItem();
                if(itemData!=null&&itemData.getId().equals("omega_processor")){
                    hasDimProc = true;
                }
            }
        }
        return super.canOccur(marketAPI)&& Global.getSettings().getModManager().isModEnabled("aod_core")&&AoDUtilis.getResearchAPI().getCurrentResearcher().hasTag(AodResearcherSkills.RESOURCEFUL)&&hasDimProc;
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara()
    }
}
