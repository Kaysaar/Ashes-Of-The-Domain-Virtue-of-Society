package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.models.AoTDColonyEvent;

public class AncientCacheEvent extends AoTDColonyEvent {

    //TODO - Return here once created new industries for Vaults of Knowledge
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean can = Global.getSettings().getModManager().isModEnabled("aod_core");
        return super.canOccur(marketAPI)&&can&&marketAPI.hasCondition("pre_collapse_facility");
    }

}
