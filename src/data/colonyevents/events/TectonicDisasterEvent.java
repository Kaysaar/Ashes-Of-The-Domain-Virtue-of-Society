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
        int random = new Random().nextInt(4);
        return super.canOccur(marketAPI)&&haveMantleBore&&marketAPI.hasCondition(Conditions.EXTREME_TECTONIC_ACTIVITY)&&random==1&&!marketAPI.hasCondition("tectonic_destabilization");
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator, our recent mining operations on "+currentlyAffectedMarket.getName()+" have caused massive damage to already unstable tectonic plates."+
                "Our team of analysts have deducted the cause of that incident: overuse of Mantle bore."+
                "If we keep using it more and more the risk of massive earthquakes will increase.",INFORMATIVE,10f);
        tooltip.addPara("We heavily suggest to fully stop using Mantle Bore.",INFORMATIVE,10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        tooltip.addPara("From now on if Mantle Bore is kept used, every month there is 20% chance of Tectonic Disaster to occur",NEGATIVE,10f);
        tooltip.addPara("Tectonic Disaster can have such effects : ",INFORMATIVE,10f);
        tooltip.addPara("5% Chance for -1 market size. WARNING! If market is size 3 when this occurs it destroys colony!",NEGATIVE,10f);
        tooltip.addPara("60% Chance for mining getting disabled for 365 days.",NEGATIVE,10f);
        tooltip.addPara("35% Chance for total destruction of mines and removing mining industries from market, including destruction of special items and AI cores.",NEGATIVE,10f);
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(!currentlyAffectedMarket.hasCondition("tectonic_destabilization")){
            currentlyAffectedMarket.addCondition("tectonic_destabilization");
        }
        super.executeDecision(currentDecision);
    }
}

