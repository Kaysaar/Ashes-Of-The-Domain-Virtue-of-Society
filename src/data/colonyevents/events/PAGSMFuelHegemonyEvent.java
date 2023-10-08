package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class PAGSMFuelHegemonyEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        int share = marketAPI.getCommodityData(Commodities.FUEL).getCommodityMarketData().getMarketSharePercent(marketAPI.getFaction());
        return false;

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        super.showOptionOutcomes(tooltip, optionId);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator , critical situation, we have been contacted by",10f);
        tooltip.addPara("The Magnanimous, Excellent, Glorious and Auspicious Supreme Chief Overlord President Executive Gas Station Manager Phillip Andrada's Fuel Company",new Color(239, 12, 227, 247),10f);
        tooltip.addPara("The message reads: We have noticed, that your fuel production have risen quickly in recent years." +
                "We demand you to lower immediately production of fuel , and excess of it to be delivered to Sindrian Fuel Company." +
                "In return our glorious company shall protect, and commission you as one of main suppliers, that will serve the glory of",10f);
        tooltip.addPara("The Magnanimous, Excellent, Glorious and Auspicious Supreme Chief Overlord President Executive Gas Station Manager Phillip Andrada",new Color(239, 12, 227, 247),10f);
        tooltip.addPara("Should you refuse our generous offer, we shall do so called \"Hostile Takeover\"",10f);
    }

}
