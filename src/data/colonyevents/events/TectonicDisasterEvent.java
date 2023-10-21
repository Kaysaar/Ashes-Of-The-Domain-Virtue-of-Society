package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.ui.P;
import data.colonyevents.listeners.AoTDCrestOfPhoenixListener;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;
import java.util.Random;

public class TectonicDisasterEvent extends AoTDColonyEvent {

    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean haveMantleBore = false;
        for (Industry industry : marketAPI.getIndustries()) {
            if(industry.getSpec().hasTag("mining")){
                SpecialItemData specialItemData =  industry.getSpecialItem();
                if(specialItemData!=null&&specialItemData.getId().equals(Items.MANTLE_BORE)){
                    haveMantleBore = true;
                }
            }
        }
        int random = new Random().nextInt(3);
        return super.canOccur(marketAPI)&&haveMantleBore&&!marketAPI.hasCondition("tectonic_disaster")&&random>1;
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {

    }

    @Override
    public void executeDecision(String currentDecision) {
        super.executeDecision(currentDecision);
    }
}

