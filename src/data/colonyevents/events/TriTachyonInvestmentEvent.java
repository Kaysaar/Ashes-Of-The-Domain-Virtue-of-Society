package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;

import java.awt.*;

public class TriTachyonInvestmentEvent extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean occur = marketAPI.getNetIncome()>=20000;
        return occur&& marketAPI.isFreePort()&&!marketAPI.hasIndustry("tt_hq");
    }

    @Override
    public void executeDecision(String currentDecision) {
        prevDecisionId = currentDecision;
        if(currentDecision.equals("hh_op1")){
            currentlyAffectedMarket.addIndustry("tt_hq");
            Global.getSector().getFaction(Factions.TRITACHYON).adjustRelationship(Global.getSector().getPlayerFaction().getId(),0.2f);
        }
        if(currentDecision.equals("hh_op2")){
            currentlyAffectedMarket.addCondition("tt_smaller_investment");
            Global.getSector().getFaction(Factions.TRITACHYON).adjustRelationship(Global.getSector().getPlayerFaction().getId(),0.1f);
            Global.getSector().getPlayerFleet().getCargo().getCredits().add(350000);
        }
        if(currentDecision.equals("hh_op3")){
            Global.getSector().getFaction(Factions.TRITACHYON).adjustRelationship(Global.getSector().getPlayerFaction().getId(),-0.2f);

        }

    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("As the usual monthly reports come in over Hypercomms from "+currentlyAffectedMarket.getName()+" , a very embellished message arrived on your communiques channel, one that is often used for diplomatic communications from the various factions that inhabit the Persean sector: One coming from a retiring executive of the Tri-Tachyon board of directors no less!\n" +
                "\n" +
                "After having a comms specialist and several security experts analyze the message down to the last pico-byte used to formulate the message on your quarter's monitor, it has been confirmed to be legitimate, safe and surprisingly amicable.\n" +
                "\n" +
                "The message itself once you read it six hours after receive it is a simple offer:\n" +
                "\n" +
                "Allow the executive to move onto "+currentlyAffectedMarket.getName()+" and build their own estate in a remote region of the planet, ideally not too far from either a financial center or the colonial capital. In return they promise to use their retirement hedge fund to stimulate the economy of the world via philanthropic charities and investments in the planetary industry... although your experience in interstellar diplomacy tells you that there is likely some power struggle occurring within the megacorporation itself amongst the directors, the matter the fact the message was not bugged or infected in any discernable way leads you to believe that competing interests in the board are likely quietly supporting the retiring board members request, even if its just to remove a potential rival without incurring extra costs on elimination and cover ups. \n" +
                "\n" +
                "This is likely a rare opportunity that has presented itself to "+currentlyAffectedMarket.getFaction().getDisplayName()+", you as the leader should carefully consider the options. ",20f);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        switch (optionId){
            case "hh_op1":
                tooltip.addPara("The Tri-Tachyon Board member will settle on the planet, " +
                        "improving the stability of the world and boosting industrial output of industries by one unit.",
                        Misc.getHighlightedOptionColor(), 15f);
                tooltip.addPara("Lose 15% of colony's income for at least 720 days.", Misc.getNegativeHighlightColor(), 15f);
                tooltip.addPara("Increase opinion with Tri Tachyon by 30 points", Misc.getPositiveHighlightColor(), 15f);
                break;
            case "hh_op2":
                tooltip.addPara("Get 350.000 Credits immediately, in exchange for 20.000 Credits from colony's income.",Misc.getTooltipTitleAndLightHighlightColor(), 15f);
                tooltip.addPara("Increase opinion with Tri Tachyon by 10 points.",Misc.getPositiveHighlightColor(), 15f);
                break;
            case "hh_op3":
                tooltip.addPara("Reduce opinion with Tri Tachyon by 20 points", Misc.getNegativeHighlightColor(), 15f);
                break;

        }
    }
}
