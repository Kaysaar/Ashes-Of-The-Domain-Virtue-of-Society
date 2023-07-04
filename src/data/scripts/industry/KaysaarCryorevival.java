package data.scripts.industry;


import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.comm.CommMessageAPI;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.impl.campaign.intel.MessageIntel;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import data.Ids.AodCryosleeperConditions;
import data.Ids.AodMemFlags;
import data.plugins.AodCryosleeperPLugin;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.util.MagicSettings;

import java.awt.*;
import java.util.List;

public class KaysaarCryorevival extends BaseIndustry implements MarketImmigrationModifier {

    public static float ALPHA_CORE_BONUS = 1f;
    public static float IMPROVE_BONUS = 1f;
    public static int POP_SIZE = MagicSettings.getInteger("Cryo_but_better", "MAX_POP_GROWTH");
    public static float POP_GROWTH_RATE = MagicSettings.getFloat("Cryo_but_better", "POP_GROWTH_RATE");
    public static boolean moreChargesSet = MagicSettings.getBoolean("Cryo_but_better", "MORE_CHARGES_MODE");
    public static float MAX_BONUS_WHEN_UNMET_DEMAND = 0.5f;
    public boolean canGrow = true;

    public void apply() {

        super.apply(true);

        if (isFunctional() && market.hasCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER)) {
            CryosleeperGrowthPopValidation();
        }
        demand(Commodities.ORGANICS, 10);


    }


    @Override
    public void unapply() {
        super.unapply();
    }

    protected boolean hasPostDemandSection(boolean hasDemand, IndustryTooltipMode mode) {

        return mode != IndustryTooltipMode.NORMAL || isFunctional();
    }


    @Override
    public void startBuilding() {
        super.startBuilding();
        if (!market.hasCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER)) {
            market.addCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER);
        }
    }

    @Override
    protected void addPostDemandSection(TooltipMakerAPI tooltip, boolean hasDemand, IndustryTooltipMode mode) {

        if (mode != IndustryTooltipMode.NORMAL || isFunctional()) {
            Color h = Misc.getHighlightColor();
            float opad = 10f;

            float bonus = getImmigrationBonus();
            float max = getMaxImmigrationBonus();
            float demandMult = getDemandMetPopulationMult();
            if (mode != IndustryTooltipMode.NORMAL) {
                tooltip.addPara("If any demand is unmet, " +
                                "the maximum growth bonus is reduced by %s.",
                        opad, h,
                        "" + (int) Math.round(MAX_BONUS_WHEN_UNMET_DEMAND * 100f) + "%");
            } else {
                tooltip.addPara("%s growth bonus multiplier based on met demand. If any demand is unmet, " +
                                "the maximum bonus is reduced by %s.",
                        opad, h,
                        "" + (int) Math.round(demandMult * 100f) + "%",
                        "" + (int) Math.round(MAX_BONUS_WHEN_UNMET_DEMAND * 100f) + "%");
            }

            tooltip.addPara("Population growth: %s (max for colony size: %s)", opad, h, "+" + Math.round(bonus), "+" + Math.round(max));
        }

    }


    @Override
    public boolean isAvailableToBuild() {
        return market.hasCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER);
    }

    @Override
    public boolean showWhenUnavailable() {
        return false;
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        if (isFunctional()&& canGrow) {
            incoming.add(Factions.SLEEPER, getImmigrationBonus() * 2f);
            incoming.getWeight().modifyFlat(getModId(), getImmigrationBonus(), getNameForModifier());

            if (Commodities.ALPHA_CORE.equals(getAICoreId())) {
                incoming.getWeight().modifyFlat(getModId(1), (int) (getImmigrationBonus() * ALPHA_CORE_BONUS),
                        "Alpha core (" + getNameForModifier() + ")");
            }
            if (isImproved()) {
                incoming.getWeight().modifyFlat(getModId(2), (int) (getImmigrationBonus() * IMPROVE_BONUS),
                        getImprovementsDescForModifiers() + " (" + getNameForModifier() + ")");
            }
        }
    }

    @Override
    protected void applyAlphaCoreModifiers() {
    }

    @Override
    protected void applyNoAICoreModifiers() {
    }

    @Override
    protected void applyAlphaCoreSupplyAndDemandModifiers() {
        demandReduction.modifyFlat(getModId(0), DEMAND_REDUCTION, "Alpha core");
    }

    protected void addAlphaCoreDescription(TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();
        String pre = "Alpha-level AI core currently assigned. ";
        if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            pre = "Alpha-level AI core. ";
        }
        float a = getImmigrationBonus() * ALPHA_CORE_BONUS;
        String str = "+" + (int) Math.round(a);

        if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            CommoditySpecAPI coreSpec = Global.getSettings().getCommoditySpec(aiCoreId);
            TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48);
            text.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by %s unit. " +
                            "%s population growth.", 0f, highlight,
                    "" + (int) ((1f - UPKEEP_MULT) * 100f) + "%", "" + DEMAND_REDUCTION,
                    str);
            tooltip.addImageWithText(opad);
            return;
        }

        tooltip.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by %s unit. " +
                        "%s population growth.", opad, highlight,
                "" + (int) ((1f - UPKEEP_MULT) * 100f) + "%", "" + DEMAND_REDUCTION,
                str);

    }

    @Override
    public boolean canImprove() {
        return true;
    }

    public void addImproveDesc(TooltipMakerAPI info, ImprovementDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();

        float a = getImmigrationBonus() * IMPROVE_BONUS;
        String str = "" + (int) Math.round(a);

        if (mode == ImprovementDescriptionMode.INDUSTRY_TOOLTIP) {
            info.addPara("Population growth increased by %s.", 0f, highlight, str);
        } else {
            info.addPara("Increases population growth by %s.", 0f, highlight, str);
        }

        info.addSpacer(opad);
        super.addImproveDesc(info, mode);
    }


    protected float getDemandMetPopulationMult() {
        Pair<String, Integer> deficit = getMaxDeficit(Commodities.ORGANICS);
        float demand = getDemand(Commodities.ORGANICS).getQuantity().getModifiedValue();
        float def = deficit.two;
        if (def > demand) def = demand;

        float mult = 1f;
        if (def > 0 && demand > 0) {
            mult = (demand - def) / demand;
            mult *= MAX_BONUS_WHEN_UNMET_DEMAND;
        }
        return mult;
    }

    protected float getImmigrationBonus() {
        return getMaxImmigrationBonus() * getDemandMetPopulationMult() * POP_GROWTH_RATE;
    }

    public static Pair<SectorEntityToken, Float> getNearestCryosleeper(Vector2f locInHyper, boolean usable) {
        SectorEntityToken nearest = null;
        float minDist = Float.MAX_VALUE;

        for (SectorEntityToken entity : Global.getSector().getCustomEntitiesWithTag(Tags.CRYOSLEEPER)) {
            if (!usable || entity.getMemoryWithoutUpdate().contains("$usable")) {
                float dist = Misc.getDistanceLY(locInHyper, entity.getLocationInHyperspace());
                if (dist < minDist) {
                    minDist = dist;
                    nearest = entity;
                }
            }
        }

        if (nearest == null) return null;

        return new Pair<SectorEntityToken, Float>(nearest, minDist);
    }

    protected float getMaxImmigrationBonus() {
        return getSizeMult() * 10f;
    }

    public void CryosleeperGrowthPopValidation() {
        float progress = Misc.getMarketSizeProgress(this.market);
        int size = this.market.getSize();
        List<CustomCampaignEntityAPI> entityList = market.getContainingLocation().getCustomEntitiesWithTag("parked_cryosleeper");
        if (entityList != null && !entityList.isEmpty()) {
            SectorEntityToken cryoOrbiting = null;
            for (CustomCampaignEntityAPI orbitingCryos : entityList) {
                if((orbitingCryos.getMemory().get(AodMemFlags.BIND_TO_PLANET)).toString().equals(this.market.getPlanetEntity().getId())){
                    cryoOrbiting = orbitingCryos;
                    break;
                }

            }

            if(cryoOrbiting==null){
                return;
            }
            if(market.getSize()>=AodCryosleeperPLugin.configSize){
                canGrow = false;
                if(!cryoOrbiting.getMemory().contains(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL)||!cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL, true)){
                    canGrow = true;
                }
            }

            if (progress >= 1 && size >= 6 && this.market.hasCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER) && !cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL, true)) {
                if (size < POP_SIZE) {
                    market.removeCondition("population_" + market.getSize());
                    market.setSize(size + 1);
                    market.addCondition("population_" + market.getSize());
                    market.getPopulation().setWeight(0);
                    if(moreChargesSet){
                        if (cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN, true)) {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN, false);
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_1, true);
                        } else if (cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_1, true)) {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_1, false);
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, true);
                        } else if (cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, true)) {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, false);
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_3,true);
                        } else if (cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_3, true)) {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_3, false);
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL, true);
                        } else {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL, true);
                        }
                    }
                    else{
                        if (cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN, true)) {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN, false);
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, true);
                        } else if (cryoOrbiting.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, true)) {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, false);
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL,true);
                        } else {
                            cryoOrbiting.getMemory().set(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL, true);
                        }
                    }

                    if (this.market.isPlayerOwned()) {
                        MessageIntel intel = new MessageIntel("Colony Growth - " + this.market.getName(), Misc.getBasePlayerColor());
                        intel.addLine("    - Size increased to %s", Misc.getTextColor(), new String[]{"" + Math.round((float) this.market.getSize())}, new Color[]{Misc.getHighlightColor()});
                        intel.setIcon(Global.getSector().getPlayerFaction().getCrest());
                        intel.setSound(BaseIntelPlugin.getSoundMajorPosting());
                        Global.getSector().getCampaignUI().addMessage(intel, CommMessageAPI.MessageClickAction.COLONY_INFO, this.market);

                    }
                }
            }
        }


    }
}

