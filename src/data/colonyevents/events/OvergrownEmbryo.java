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
            tooltip.addPara("A Biofactory Embryo installed at " + currentlyAffectedMarket.getName() + " has started spreading an unknown - according to testimonies of the Engineers, Guards and pretty much anyone who works at this facility - Red, Fungi-like growth.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("It's not exactly clear what this \"Life Spread\" is about at the first glance, after all the Domain era tech operates on laws and principles mostly forgotten to the sector at large, and considering that the most of the records are long gone you probably won't find anything to shed some light on it.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("Perhaps it's intentional behaviour, perhaps it's just a not-so-funny one-off joke by a long-dead domain Bio-engineer, or worse, a gene-coded failsafe failure.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("On top of that, all lifeforms in proximity of the Facility have trace amounts of drugs in their blood, composition of which is surprisingly similar to combat stims widely used by the marines all across the Sector.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("However, there is no doubt that it will get worse if we won't do anything about it.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);

            tooltip.addPara("Unfortunately these news already have broken out into the public, in turn falling into hands of Tri-Tachyon. Your Second-In-Command reports that their private party of bio-engineers from Eochu Bres is offering help with our unusual problem, for money of course.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("Your crew loudly voices doubts and concerns that anyone in the Persean would know what the hell is the embryo doing, and those as your bridge CO calls them, \"eggheads\", are only trying to scam you.", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
            tooltip.addPara("One thing is clear, we must do something about this Fungi right now, at the rate it' s spreading it will not take long for it to spread beyond control...", Misc.getTooltipTitleAndLightHighlightColor(), 10f);


        }

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        super.showOptionOutcomes(tooltip, optionId);
        switch (optionId) {
            case "biofactory_tri_tachyon":
                tooltip.addPara("Lose %s credits", 10f, Misc.getNegativeHighlightColor(), Misc.getDGSCredits(100000));
                tooltip.addPara("Biofactory will be taken for %s months to Tri Tachyon research facilities, for investigation.", 10f, Misc.getNegativeHighlightColor(), "2-3");
                break;
            case "biofactory_integration":
                tooltip.addPara("Whatever it's doing, it's probably intentional. The Domain wasn't built by idiots, after all!", Misc.getTooltipTitleAndLightHighlightColor(), 10f);
                break;
            case "biofactory_our_depo":
                tooltip.addPara("Issue a new special project: Rogue Biofactory", Misc.getPositiveHighlightColor(), 10f);
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
