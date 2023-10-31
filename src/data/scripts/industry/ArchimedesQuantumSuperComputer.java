package data.scripts.industry;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.ProjectCheronIntel;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ArchimedesQuantumSuperComputer extends BaseIndustry {
    public float decryptionProgress = 0.0f;
    public float decryptionThreshold = 120f;
    public boolean decrypted = false;
    public static String ELECTRONICS = "electronics";
    public static String WATER = "water";
    public boolean metDemand = false;
    @Override
    public void apply() {
        demand(ELECTRONICS,4);
        demand(WATER,4);
        if(getMaxDeficit(ELECTRONICS,WATER).two<=0){
            metDemand = true;
        }
        else{
            metDemand = false;
        }
        if(decryptionProgress>=decryptionThreshold&&!decrypted&&!Global.getSector().getMemory().contains("$aotd_archimedes_event")){
            List<StarSystemAPI> starSystemAPIList = Global.getSector().getStarSystems();
            Collections.shuffle(starSystemAPIList,new Random());
            for (StarSystemAPI starSystemAPI : starSystemAPIList) {
                boolean breaken= false;
                for (PlanetAPI planet : starSystemAPI.getPlanets()) {
                    if (planet.isStar()) continue;
                    if (!planet.getMarket().isPlanetConditionMarketOnly()) continue;
                    if (planet.hasTag(Tags.NOT_RANDOM_MISSION_TARGET)) continue;
                    if (planet.hasTag(Tags.MISSION_ITEM)) continue;
                    if (planet.isGasGiant()) continue;

                    if(!planet.getMarket().getSurveyLevel().equals(MarketAPI.SurveyLevel.NONE))continue;
                    if(planet.getMemoryWithoutUpdate().contains("$aotd_quest_veil"))continue;
                    planet.getMemoryWithoutUpdate().set(MemFlags.SALVAGE_SPEC_ID_OVERRIDE, "aotd_quest_theta");
                    planet.getMemory().set("$aotd_quest_theta",true);
                    Global.getSector().getIntelManager().addIntel(new ProjectCheronIntel(planet));
                    breaken=true;
                    break;

                }
                if(breaken)break;
            }
            decrypted=true;
        }

    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        if(metDemand&&isFunctional()){
            decryptionProgress+= Global.getSector().getClock().convertToDays(amount);
        }
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {
        super.addPostDemandSection(tooltip, hasDemand, mode);
        if(mode.equals(IndustryTooltipMode.NORMAL)){
            if(!Global.getSector().getMemory().contains("$aotd_archimedes_event")){
                if(decryptionThreshold-decryptionProgress>1){
                    tooltip.addPara((int)(decryptionThreshold-decryptionProgress)+" days left to decipher location of Project Charon main facility", Color.ORANGE,10f);
                }
                else{
                    tooltip.addPara((int)(decryptionThreshold-decryptionProgress)+" day left to decipher location of Project Charon main facility", Color.ORANGE,10f);
                }
            }
            else{
                tooltip.addPara("Provides 50% research speed bonus", Misc.getPositiveHighlightColor(),10f);
            }

        }



    }

    @Override
    public boolean isAvailableToBuild() {
        boolean haveAlreadyArchimedes = false;
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(true)) {
            if(playerMarket.getId()!=this.market.getId()){
                if(playerMarket.hasIndustry("archimedes")){
                    haveAlreadyArchimedes = true;
                    break;
                }
            }
        }
        return Global.getSector().getMemory().contains("$aotd_archimedes_event")&&!haveAlreadyArchimedes;
    }

    @Override
    public boolean showWhenUnavailable() {
        boolean haveAlreadyArchimedes = false;
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(true)) {
            if(playerMarket.getId()!=this.market.getId()){
                if(playerMarket.hasIndustry("archimedes")){
                    haveAlreadyArchimedes = true;
                    break;
                }
            }
        }
        return Global.getSector().getMemory().contains("$aotd_archimedes_event")&&!haveAlreadyArchimedes;
    }

    @Override
    public boolean showShutDown() {
        return Global.getSector().getMemory().contains("$aotd_archimedes_event");
    }

    @Override
    public boolean canInstallAICores() {
        return false;
    }
}
