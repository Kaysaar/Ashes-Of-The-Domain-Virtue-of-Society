package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;


public class UnexpectedOutsiderEvent extends AoTDColonyEvent {
    // Silly AoTD - ACOT collab - this can only fire once

    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return false;

    }

}
