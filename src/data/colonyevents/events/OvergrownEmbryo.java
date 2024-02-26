package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import data.kaysaar.aotd.vok.Ids.AoTDTechIds;
import data.kaysaar.aotd.vok.scripts.research.AoTDMainResearchManager;
import org.lazywizard.lazylib.MathUtils;

public class OvergrownEmbryo extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        boolean metREq = false;
        if(marketAPI.hasIndustry(Industries.LIGHTINDUSTRY))    {
            if(marketAPI.getIndustry(Industries.LIGHTINDUSTRY).isImproved()&&marketAPI.getIndustry(Industries.LIGHTINDUSTRY).getSpecialItem().getId().equals(Items.BIOFACTORY_EMBRYO)){
                metREq = true;
            }
        }
        return super.canOccur(marketAPI) && metREq;
    }


    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        super.generateDescriptionOfEvent(tooltip);
        if (this.spec.getEventId().equals("biofactory_detection")) {
            tooltip.addPara("A Biofactory Embryo installed at " + currentlyAffectedMarket.getName() + " has started spreading an unknown -- by the words of the Engineers, Guards, anyone who works there, really -- Red, Fungi-like growth.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("It's not exactly clear what this \"Life Spread\" is at first glance, any and all Domain-grade tech operates on laws and principles unknown to the sector at large, and, considering most records are long gone, too, you probably won't find anything about it.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("Perhaps, an intentional behaviour, perhaps, a not-so-funny one-off joke by a long-dead domain Bio-expert, or worse, a critical gen-coded failsafe failure.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("Any and all lifeforms in proximity of the Facility show weak but alarming signs of being drugged, as if, weirdly, by combat stims widely used by marine corps across the Sector.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("It however, will probably be worse in no time.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);

            tooltip.addPara("These news, somehow, broke out into public already, in turn falling into Tri-Tachyon's hands. Your Second-In-Command reports that their private party of bio-engineers from Eochu Bres are offering quite modest help, for money, of course.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("Your crew loudly voices doubts and concerns that anyone in the Persean knows what the hell that thing in a jar is doing, and that those, as your bridge CO names them, \"egg-heads\", are merely scamming you.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("One thing is clear, we must do something about this Fungi urgently, whatever it's doing, will not take too long to spread beyond control...", Misc.getTooltipTitleAndLightHighlightColor(), 10f);


        }

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        super.showOptionOutcomes(tooltip, optionId);
        switch (optionId) {
            case "biofactory_tri_tachyon":
                tooltip.addPara("Loose %s credits", 10f, Misc.getNegativeHighlightColor(), Misc.getDGSCredits(100000));
                tooltip.addPara("Biofactory will be taken for %s months to Tri Tachyon, for investigation.", 10f, Misc.getNegativeHighlightColor(), "2-3");
                break;
            case "biofactory_integration":
                tooltip.addPara("Whatever it's doing, it's probably intentional. The Domain wasn't built by idiots, after all!", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
                break;
            case "biofactory_our_depo":
                tooltip.addPara("Issue new special project: Rouge Biofactory", Misc.getPositiveHighlightColor(), 10f);
                break;

        }
    }

    @Override
    public void overrideOptions() {
//        if(this.spec.getEventId().equals("biofactory_detection")){
//            if(Global.getSettings().getModManager().isModEnabled("aotd_vok")){
//                if(AoTDMainResearchManager.getInstance().getManagerForPlayer().howManyFacilitiesFactionControlls()>=1){
//                    this.loadedOptions.put("biofactory_our_depo","We will investigate on our own this phenomenon");
//                }
//            }
//        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if (currentDecision.equals("biofactory_tri_tachyon")) {
            currentlyAffectedMarket.addCondition("crime_syndicate_beginning");
            currentlyAffectedMarket.getMemory().set("$aotd_had_crime_event", true);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("crime_syndicate_resolve", currentlyAffectedMarket.getId(), MathUtils.getRandomNumberInRange(180, 250));

        }
        if (currentDecision.equals("biofactory_integration")) {
            currentlyAffectedMarket.removeCondition("crime_syndicate_beginning");
            currentlyAffectedMarket.addCondition("crime_syndicate_resolve_deal");

        }
    }
}
