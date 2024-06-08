package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.listeners.AoTDCrestOfPhoenixListener;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class CrestOfPhoenixEvent extends AoTDColonyEvent {

    //TODO - Event for Reforming Domain : Needs to have size bigger than 50% of sector and hegemony must have 0 markets left

    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        int playerSize = 0;
        int sectorSize = 0;
        int hegemonySize = 0;
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {
            playerSize += playerMarket.getSize();
        }
        for (MarketAPI api : Global.getSector().getEconomy().getMarketsCopy()) {
            if (api.getFactionId().equals(Global.getSector().getPlayerFaction().getId())) {
                continue;
            }
            if (api.getFactionId().equals(Factions.HEGEMONY)) {
                hegemonySize += api.getSize();
            }
            sectorSize += api.getSize();
        }

        return super.canOccur(marketAPI) && ((playerSize >= (sectorSize / 2) && (hegemonySize <= 5)));

    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator, this has been a long and perilous journey. You alone have united the scattered sector under your banner", 10f);
        tooltip.addPara("Most of the Persean Sector is now under control of " + currentlyAffectedMarket.getFaction().getDisplayName() + ".", 10f);
        tooltip.addPara("Many important voices within our faction say its time; Time to proclaim a new colossus, a torch for the entire sector.", 10f);
        tooltip.addPara("The Rebirth of Domain of Man.", Color.CYAN, 10f);
        tooltip.addPara("Decision is all yours to make administrator.", 10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if (optionId.equals("nc_op1")) {
            tooltip.addPara("We wil change name to \"Domain of Man\".",Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Receive permanent +5 Stability modifier to all of your worlds",Misc.getPositiveHighlightColor(),10f);
            tooltip.addPara("And From the Ashes of The Domain, a new Colossus shall rise. ",Color.ORANGE,10f);

        }
        if (optionId.equals("nc_op2")) {
            tooltip.addPara("Receive permanent +5 Stability modifier to all your worlds",Misc.getPositiveHighlightColor(),10f);
            tooltip.addPara("Its time to build our own legacy across the stars.",Color.ORANGE,10f);
        }

    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("nc_op1")){
            Global.getSector().getPlayerFaction().setDisplayNameOverride("Domain of Man");
            Global.getSector().getPlayerFaction().setFactionCrestOverride("graphics/factions/crest_domain.png");
            Global.getSector().getPlayerFaction().setFactionLogoOverride("graphics/factions/domain_explorarium.png");
        }
        Global.getSector().getListenerManager().addListener(new AoTDCrestOfPhoenixListener());
        super.executeDecision(currentDecision);
    }
}
