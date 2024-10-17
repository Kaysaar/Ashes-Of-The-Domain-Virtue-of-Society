package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;
import data.kaysaar.aotd.vok.campaign.econ.industry.ResearchFacility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AncientFactoryEvent extends AoTDColonyEvent {
    public boolean canOccur(MarketAPI marketAPI) {
        return  super.canOccur(marketAPI)&&marketAPI.hasCondition("pre_collapse_facility")&&marketAPI.hasIndustry("researchfacility");
    }

    @Override
    public void apply() {
        super.apply();
    }

    @Override
    public void unapply() {
        super.unapply();
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        tooltip.addPara("Administrator, our exploration teams on "+currentlyAffectedMarket.getName()+" have uncovered a sprawling, ancient factory complex from the Pre-Collapse era! The facility appears to be a treasure trove of valuable data and cutting-edge technology long lost to time. Additionally, the site contains a significant stockpile of rare, valuable items—perhaps remnants of a long-forgotten production line.",INFORMATIVE,5f);
        tooltip.addPara("However, it seems the factory's defense systems, though dormant for now, are complex and intricate, raising concerns among our experts. Some recommend securing the data first, which could unlock untold technological advancements. Others suggest extracting the valuable items while the defenses are still inactive. How shall we proceed, Administrator?",INFORMATIVE,10f);

    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if(optionId.equals("ancient_factory1")){
            tooltip.addPara("Increase amount of databanks monthly from 1 to 3 permanently on all sites with Pre Collapse Facility",POSITIVE,10f);}
        if(optionId.equals("ancient_factory2")){
            tooltip.addPara("Spawns two huge defense drone fleets that will protect colony",POSITIVE,10f);
            tooltip.addPara("Increase market upkeep by 40%",NEGATIVE,10f);
        }
        if(optionId.equals("ancient_factory3")){
            tooltip.addPara("50% to get two colony items",POSITIVE,10f);
            tooltip.addPara("50% to get nothing",NEGATIVE,10f);
        }
    }
//TODO implement effects
    @Override
    public void executeDecision(String currentDecision) {
        if(currentDecision.equals("ancient_factory1")) {
            if(Global.getSettings().getModManager().isModEnabled("aotd_vok")){
                ResearchFacility.amountDatabanksMonthly = 3;
            }
        }
        if(currentDecision.equals("ancient_factory2")){
            currentlyAffectedMarket.addCondition("aotd_derelict_factory_fleet");
        }
        if(currentDecision.equals("ancient_factory3")){
            Random rand = Misc.random;
           boolean succeded =  rand.nextBoolean();
           if(succeded){
               ArrayList<String> list = new ArrayList<>(SandsOfTimeEvent.ITEMS);
               Collections.shuffle(list);
               CargoAPI cargoAPI = currentlyAffectedMarket.getSubmarket(Submarkets.SUBMARKET_STORAGE).getCargo();
               cargoAPI.addSpecial(new SpecialItemData(Items.PRISTINE_NANOFORGE,null),1);
               for(int i=0;i<2;i++){
                   cargoAPI.addSpecial(new SpecialItemData(list.get(i),null),1);
               }
           }

        }
        super.executeDecision(currentDecision);
    }
}
