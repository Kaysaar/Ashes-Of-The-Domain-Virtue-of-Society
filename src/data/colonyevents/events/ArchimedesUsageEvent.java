package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import data.colonyevents.models.AoTDColonyEvent;

public class ArchimedesUsageEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        return super.canOccur(marketAPI)&&marketAPI.hasIndustry("archimedes");
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator, Archimedes supercomputer located on "+currentlyAffectedMarket.getName()+" have served it's purpose.",INFORMATIVE,10f);
        tooltip.addPara("There are suggestions about dismantling it, or use it to boost our research project's with it's immense calculating power.",INFORMATIVE,10f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if(optionId.equals("arch_op1")){
            tooltip.addPara("Lose Archimedes QSC structure on "+currentlyAffectedMarket.getName()+".",NEGATIVE,10f);
            tooltip.addPara("Gain 500.000 Credits of refund.",POSITIVE,10f);
        }
        else{
            tooltip.addPara("Repurpose Archimedes to help our research endeavours, giving 50% bonus to research speed.",POSITIVE,10f);
            tooltip.addPara("Gain ability to built Archimedes SQC",POSITIVE,10f);
        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("arch_op1")){
            currentlyAffectedMarket.removeIndustry("archimedes",null,false);
           Global.getSector().getPlayerFleet().getCargo().getCredits().add(500000);
        }
        else{
            Global.getSector().getMemory().set("$aotd_archimedes_event",true);
        }
        super.executeDecision(currentDecision);
    }
}
