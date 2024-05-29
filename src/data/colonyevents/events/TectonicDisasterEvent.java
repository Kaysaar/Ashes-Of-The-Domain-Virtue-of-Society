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
        tooltip.addPara("Administrator, our recent mining operations on "+currentlyAffectedMarket.getName()+" have caused massive damage to the already unstable tectonic plates."+
                "Our team of analysts has deducted the cause of this incident: overuse of Mantle bore."+
                "If we continue using it the risk of massive earthquakes will increase.",INFORMATIVE,10f);
        tooltip.addPara("It's strongly suggested to fully stop using the Mantle Bore.",INFORMATIVE,10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        tooltip.addPara("From now on if Mantle Bore is used, every month there is 20% chance that a Tectonic Disaster will occur.",NEGATIVE,10f);
        tooltip.addPara("Tectonic Disaster can have following effects : ",INFORMATIVE,10f);
        tooltip.addPara("5% Chance for -1 market size. URGENT! If market is size 3 when this variant of Tectonic Disaster occurs it will destroy the colony!",NEGATIVE,10f);
        tooltip.addPara("60% Chance for mining disruption for 365 days.",NEGATIVE,10f);
        tooltip.addPara("35% Chance for total destruction of the mines and removing mining industry from market, destroying special items and AI cores that were in use in process.",NEGATIVE,10f);
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(!currentlyAffectedMarket.hasCondition("tectonic_destabilization")){
            currentlyAffectedMarket.addCondition("tectonic_destabilization");
        }
        super.executeDecision(currentDecision);
    }
}

