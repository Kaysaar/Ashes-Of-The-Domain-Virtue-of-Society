package data.colonyevents.events;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class SynchroTronSynergyEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return super.canOccur(marketAPI)&& marketAPI.hasIndustry("blast_processing") && marketAPI.getIndustry("blast_processing").getSpecialItem() != null && marketAPI.getIndustry("blast_processing").getSpecialItem().getId().equals(Items.SYNCHROTRON);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
       tooltip.addPara("Routine diagnostics within the %s Antimatter Production facility have flagged an unexpected anomaly. Energy readings are fluctuating beyond predicted parameters, and particle flow within the containment fields is exhibiting unusual, rhythmic patterns. Initial concerns of a critical system malfunction are quickly dismissed as technicians realize something far more remarkable is occurring.  The recently installed Synchrotron Core, intended for advanced materials research, is generating an unforeseen resonance effect within the Antimatter Production process.  This interaction, entirely accidental and previously only theorized in fringe physics, is unlocking surprising efficiencies and unexpected potentials within the facility.  A fortunate accident, it seems, has presented a unique opportunity.",5f, Color.ORANGE,currentlyAffectedMarket.getName()+"'s");
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if(optionId.equals("synchrotron_improve1")){
            tooltip.addPara("Direct engineering teams to focus on calibrating and optimizing the resonance effect to directly enhance antimatter fuel production",INFORMATIVE,10f);
            tooltip.addPara("Increase production of fuel by 1",POSITIVE,5f);
        }
        if(optionId.equals("synchrotron_improve2")){
            tooltip.addPara("Instruct technicians to prioritize understanding and stabilizing the resonance to reduce the operational costs of antimatter production",INFORMATIVE,10f);
            tooltip.addPara("Reduce upkeep by 70%",POSITIVE,5f);
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("synchrotron_improve1")){
            currentlyAffectedMarket.getIndustry("blast_processing").getSupply(Commodities.FUEL).getQuantity().modifyFlat("aotd_improvment",1,"Improved Infrastructure");
        }
        if(currentDecision.equals("synchrotron_improve2")){
            currentlyAffectedMarket.getIndustry("blast_processing").getUpkeep().modifyMult("aotd_improvment",0.5f,"Improved Infrastructure");

        }
        super.executeDecision(currentDecision);
    }
}
