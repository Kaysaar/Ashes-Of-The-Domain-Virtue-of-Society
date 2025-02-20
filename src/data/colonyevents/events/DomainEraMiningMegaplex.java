package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.impl.campaign.econ.ResourceDepositsCondition;
import com.fs.starfarer.api.impl.campaign.econ.impl.PopulationAndInfrastructure;
import com.fs.starfarer.api.impl.campaign.econ.impl.Spaceport;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.intel.MegastructureLocationIntel;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.conditions.DelayedMarketCondPlugin;
import data.colonyevents.conditions.IncreaseUpkeepIndApplier;
import data.colonyevents.conditions.MiracleSeedApplier;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import data.kaysaar.aotd.vok.Ids.AoTDTechIds;
import data.kaysaar.aotd.vok.campaign.econ.industry.MiningMegaplex;
import data.kaysaar.aotd.vok.misc.AoTDMisc;
import data.kaysaar.aotd.vok.scripts.research.AoTDMainResearchManager;
import data.plugins.AotDVosUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static data.plugins.AotDVosUtil.addUpkeepCond;
import static data.plugins.AotDVosUtil.getDelayedMarketCondPlugin;

public class DomainEraMiningMegaplex extends AoTDColonyEvent {
    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        if (!Global.getSettings().getModManager().isModEnabled("aotd_vok")) return false;
        if (this.getSpec().getEventId().equals("demm_start")) {
            return super.canOccur(marketAPI) && marketAPI.hasIndustry(Industries.MINING) && marketAPI.hasCondition(Conditions.ORE_SPARSE) && marketAPI.getSize() >= 4&& !AotDVosUtil.doesHaveCondFromCommodityCategory(Commodities.RARE_ORE,marketAPI);
        }
        if (this.getSpec().getEventId().equals("demm_end")) {
            boolean hasIndustry = marketAPI.hasIndustry(Industries.MINING);
            if (hasIndustry) {
                Industry ind = marketAPI.getIndustry(Industries.MINING);
                if (marketAPI.getMemory().is("$aotd_demm_event_second", true)) {
                    return Commodities.ALPHA_CORE.equals(ind.getAICoreId()) && super.canOccur(marketAPI) && marketAPI.getStability().getMods().get("aotd_demm_second3") == null;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canOccurGuaranteed(MarketAPI marketAPI) {
        if (this.getSpec().getEventId().equals("demm_first")) {
            return marketAPI.hasIndustry(Industries.MINING);
        }
        if (this.getSpec().getEventId().equals("demm_second")) {
            return marketAPI.hasIndustry(Industries.MINING);
        }
        return super.canOccurGuaranteed(marketAPI);

    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        super.generateDescriptionOfEvent(tooltip);
        if (this.getSpec().getEventId().equals("demm_start")) {
            tooltip.addPara(
                    "On the world of %s, a tremor shakes the foundations of your burgeoning colony. It's not an uncommon occurrence in this geologically active region, but this quake reveals something unexpected. Veteran miners, hardened by months of toiling in the sparse ore veins, report the discovery of an ancient mineshaft, hidden for ages beneath the surface. The entrance, cracked open by the seismic shift, is a gaping maw in the rock. A powerful draft, a relentless air-pull, emanates from the forgotten shaft, sucking at the miners' suits and stirring up clouds of fine regolith. It has been doing so for days, a constant, unnerving whisper from the depths. This alone speaks volumes; for such a vacuum to persist suggests either a colossal, sealed cavern system or a connection to some unknown, subterranean process. Flickering lamplight, cast deep into the opening, catches on glints of something unusual – not the dull gleam of common ore, but the unmistakable, iridescent shimmer of transplutonics, a far rarer and more valuable resource. The discovery has sent ripples of excitement and unease through the mining community. The promise of wealth is undeniable, but the sheer age and mystery of the shaft, coupled with the unnatural airpull, instill a primal apprehension. Is it a forgotten relic of a long-dead civilization? A natural phenomenon of incredible scale? Or something far more sinister?",
                    10f,
                    Color.ORANGE,
                    currentlyAffectedMarket.getName()
            );

        }
        if (this.getSpec().getEventId().equals("demm_first")) {
            tooltip.addPara("Exploration of the ancient mineshaft has revealed a large, roughly-hewn chamber beyond the initial caverns. Massive, riveted iron girders support the ceiling, and the walls are lined with crumbling brickwork. The air is thick with coal dust and the scent of old machinery. You've discovered a forgotten outpost, a forward operating base for a much larger mining operation. Within, miners find dormant, but largely intact, steampunk-inspired machinery: steam-powered drills, ore crushers, and a rudimentary smelting furnace. Cogwheels the size of hab-modules are frozen, and networks of pressure-sealed pipes snake across the walls. A thick layer of grime and rust covers everything, but the underlying engineering speaks of robust, if crude, industrial power. A massive, inactive conveyor system, wide enough to transport enormous loads of ore, disappears into the darkness, hinting at deeper levels. Crucially, alongside the mining equipment, the outpost contains a substantial store of safety equipment: reinforced supports, emergency ventilation systems, and robust personal protective gear – all far exceeding current colony standards.", 5f);
        }
        if (this.getSpec().getEventId().equals("demm_second")) {
            tooltip.addPara("With the outpost fully operational, the restored conveyor system has begun transporting significant quantities of both common and transplutonic ore. However, the system's reactivation has triggered a secondary effect. A section of the outpost's rear wall, previously thought to be solid rock, has partially collapsed, revealing a massive archway – far larger than the original mineshaft entrance. This is clearly the true entrance to the deeper mining complex, designed for the passage of truly colossal machinery. The archway is in a precarious state. Rubble and debris partially block the passage, and the surrounding rock is riddled with cracks and fissures. The air thrumming from within is intense, carrying with it the sounds of grinding stone and the distant, echoing clang of metal on metal. It's evident that the deeper complex is active, at least to some degree, but in a state of disrepair. Initial scans reveal incredibly rich veins of both common ore and transplutonics just beyond the archway, but accessing them will be a major undertaking. The instability of the entrance poses a significant risk to your miners, and the machinery within, while potentially immensely productive, appears to be in desperate need of repair. Parts are needed that the colony doesn't have.", 10f);
            tooltip.addSectionHeading("Effects from previous decisions", Alignment.MID, 10f);
            tooltip.addPara("Spares Ore Deposits  -> Moderate Ore Deposits ", POSITIVE, 5f);
            tooltip.addPara("Spares Transplutonic Ore Deposits  -> Moderate Transplutonic Ore Deposits", POSITIVE, 5f);
        }
        if (this.getSpec().getEventId().equals("demm_end")) {
            tooltip.addPara("Following the arduous and costly restoration of the true entrance, the full scale of the ancient mining operation has been revealed. It is not merely a large mine; it is a %s , a subterranean industrial hub of breathtaking scale and complexity. Countless rail lines, designed for heavy-duty ore transport, radiate outwards from a central nexus, disappearing into the darkness. Gigantic, multi-stage drill heads, larger than any seen in the sector, are mounted on massive, tracked platforms, connected to the rail network. The central hub itself is a cavern of colossal proportions. Processing plants, built on a scale to dwarf even the largest orbital stations, stand silent and dark. Gargantuan conveyor belts, capable of moving mountains of material, are frozen mid-operation. Networks of pipes, conduits, and power cables – a chaotic yet strangely organized tapestry of industrial infrastructure – crisscross the vast space. A sense of dormant power permeates the megaplex; it feels like a sleeping giant, awaiting the spark to reawaken. The scale of engineering is almost incomprehensible, clearly the work of a civilization with a mastery of resource extraction far beyond current Domain-era technology. The key to reactivating this behemoth lies in integrating a sufficiently powerful control system. The advanced systems, though steampunk in design, are incredibly intricate and responsive. Reports indicate the installed Alpha-level AI core represents the perfect candidate", 5f, Color.ORANGE, "Mining Megaplex");
            tooltip.addSectionHeading("Effects from previous decisions", Alignment.MID, 10f);
            tooltip.addPara("Moderate Ore Deposits  -> Abundant Ore Deposits ", POSITIVE, 5f);
            tooltip.addPara("Moderate Transplutonic Ore Deposits  -> Abundant Transplutonic Ore Deposits", POSITIVE, 5f);
        }
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if (optionId.equals("demm_start1")) {
            tooltip.addPara("Order the mineshaft to be fully opened and explored", INFORMATIVE, 10f);
            tooltip.addPara("Add spares transplutonic ore deposits",POSITIVE,5f);
            tooltip.addPara("Increase upkeep of mining operations by "+Misc.getDGSCredits(20000f)+" for 2 months!",NEGATIVE,5f);
        }
        if (optionId.equals("demm_start2")) {
            tooltip.addPara("Heed the warnings whispered on the wind. Order the mineshaft to be sealed once more, burying the mystery and the potential danger. ",INFORMATIVE,10f);
            tooltip.addPara("-1 stability for three months!", NEGATIVE, 5f);
        }

        if (optionId.equals("demm_first1")) {
            tooltip.addPara("Restore the outpost to full operation. Repair the machinery, reinforce the structure, and clear the conveyor system.", INFORMATIVE, 10f);
            tooltip.addPara("Increase upkeep of mining operations by "+Misc.getDGSCredits(40000f)+" for 3 months!",NEGATIVE,5f);
        }
        if (optionId.equals("demm_first2")) {
            tooltip.addPara("Salvage the outpost's valuable safety equipment and usable components and integrate them into your existing mining operations.",INFORMATIVE,10f);
            tooltip.addPara("Add moderate ore deposits",POSITIVE,5f);
            tooltip.addPara("Permanent +2 towards Stability.",POSITIVE,5f);
        }
        if (optionId.equals("demm_second1")) {
            tooltip.addPara("Prioritize stabilizing the entrance and repairing the machinery dedicated to common ore extraction.", INFORMATIVE, 10f);
            tooltip.addPara("Increase upkeep of mining operations by "+Misc.getDGSCredits(20000f)+" for 2 months!",NEGATIVE,5f);
            tooltip.addPara("-2 stability for two months!", NEGATIVE, 5f);
        }
        if (optionId.equals("demm_second2")) {
            tooltip.addPara("Concentrate efforts on securing the entrance and repairing the machinery specialized for transplutonic ore extraction ",INFORMATIVE,10f);
            tooltip.addPara("Increase upkeep of mining operations by "+Misc.getDGSCredits(20000f)+" for 2 months!",NEGATIVE,5f);
            tooltip.addPara("-2 stability for two months!", NEGATIVE, 5f);

        }
        if (optionId.equals("demm_second3")) {
            tooltip.addPara("Heed the warnings whispered on the wind. Order the mineshaft to be sealed once more, burying the mystery and the potential danger. ",INFORMATIVE,10f);
            tooltip.addPara("Increase upkeep of mining operations by "+Misc.getDGSCredits(60000f)+" for 4 months!",NEGATIVE,5f);
            tooltip.addPara("-4 stability for four months!", NEGATIVE, 5f);


        }
        if (optionId.equals("demm_final")) {
            tooltip.addPara("Integrate the Alpha Core into the Megaplex control systems and initiate full activation. ",INFORMATIVE,10f);
            tooltip.addPara("Remove any mining industry from market",INFORMATIVE,5f);
            tooltip.addPara("Add Mining Megaplex with already integrated Alpha Core and Mantle bore", POSITIVE, 5f);
            tooltip.addPara("Reveal Location of Pluto Mining Station!", POSITIVE, 5f);
        }
    }

    @Override
    public void applyBeforeDecision() {
        if (this.getSpec().getEventId().equals("demm_second")) {
            Industry ind = currentlyAffectedMarket.getIndustry(Industries.MINING);
            if (ind != null) {
                if (currentlyAffectedMarket.hasCondition(Conditions.ORE_SPARSE)) {
                    currentlyAffectedMarket.removeCondition(Conditions.ORE_SPARSE);
                    String token = currentlyAffectedMarket.addCondition(Conditions.ORE_MODERATE);
                    currentlyAffectedMarket.getSpecificCondition(token).setSurveyed(true);
                }
                if (currentlyAffectedMarket.hasCondition(Conditions.RARE_ORE_SPARSE)) {
                    currentlyAffectedMarket.removeCondition(Conditions.RARE_ORE_SPARSE);
                    String token =   currentlyAffectedMarket.addCondition(Conditions.RARE_ORE_MODERATE);
                    currentlyAffectedMarket.getSpecificCondition(token).setSurveyed(true);
                }
                AotDVosUtil.clearSupply(ind, Commodities.ORE, Commodities.RARE_ORE);
            }


        }
        if (this.getSpec().getEventId().equals("demm_end")) {
            Industry ind = currentlyAffectedMarket.getIndustry(Industries.MINING);
            if (ind != null) {
                if (currentlyAffectedMarket.hasCondition(Conditions.ORE_MODERATE)) {
                    currentlyAffectedMarket.removeCondition(Conditions.ORE_MODERATE);
                    String token =  currentlyAffectedMarket.addCondition(Conditions.ORE_ABUNDANT);
                    currentlyAffectedMarket.getSpecificCondition(token).setSurveyed(true);
                }
                if (currentlyAffectedMarket.hasCondition(Conditions.RARE_ORE_MODERATE)) {
                    currentlyAffectedMarket.removeCondition(Conditions.RARE_ORE_MODERATE);
                    String token =  currentlyAffectedMarket.addCondition(Conditions.RARE_ORE_ABUNDANT);
                    currentlyAffectedMarket.getSpecificCondition(token).setSurveyed(true);
                }
                AotDVosUtil.clearSupply(ind, Commodities.ORE, Commodities.RARE_ORE);
            }


        }
    }

    @Override
    public void executeDecision(String currentDecision) {
        if (currentDecision.equals("demm_start1")) {
            if (!currentlyAffectedMarket.hasCondition(Conditions.RARE_ORE_SPARSE)) {
//                Industry ind = currentlyAffectedMarket.getIndustry(Industries.MINING);
//                ind.getSupply(ResourceDepositsCondition.COMMODITY.get(Conditions.RARE_ORE_SPARSE)).getQuantity().unmodify();
                String token =currentlyAffectedMarket.addCondition(Conditions.RARE_ORE_SPARSE);
                currentlyAffectedMarket.getSpecificCondition(token).setSurveyed(true);
            }
            HashMap<String, Float> map = new HashMap<>();
            map.put(Industries.MINING, 20000f);
            addUpkeepCond(map, 60,currentlyAffectedMarket);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("demm_first", currentlyAffectedMarket.getId(), 60);


        }
        if (currentDecision.equals("demm_start2")) {
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "aotd_dmm_event_failed", "Unrealized dreams", -2);


        }
        if (currentDecision.equals("demm_first1")) {
            //Remember to implement upkeep increse
            HashMap<String, Float> map = new HashMap<>();
            map.put(Industries.MINING, 30000f);
            addUpkeepCond(map, 90,currentlyAffectedMarket);

            AoTDColonyEventManager.getInstance().addGuaranteedEvent("demm_second", currentlyAffectedMarket.getId(), 90);

        }
        if (currentDecision.equals("demm_first2")) {
            if (currentlyAffectedMarket.hasCondition(Conditions.ORE_SPARSE)) {
                currentlyAffectedMarket.removeCondition(Conditions.ORE_SPARSE);
                Industry ind = currentlyAffectedMarket.getIndustry(Industries.MINING);
                if (ind != null) {
                    ind.getSupply(ResourceDepositsCondition.COMMODITY.get(Conditions.ORE_SPARSE)).getQuantity().unmodify();
                }
                currentlyAffectedMarket.addCondition(Conditions.ORE_MODERATE);
                currentlyAffectedMarket.getStability().modifyFlatAlways("aotd_demm_event_first2", 2, "Miner's safety");
            }
        }
        if (currentDecision.equals("demm_second1")) {
            HashMap<String, Float> map = new HashMap<>();
            map.put(Industries.MINING, 20000f);
            addUpkeepCond(map, 60,currentlyAffectedMarket);
            DelayedMarketCondPlugin applier = getDelayedMarketCondPlugin(currentlyAffectedMarket);
            ArrayList<String> remove, add, commodities;
            remove = new ArrayList<>();
            add = new ArrayList<>();
            commodities = new ArrayList<>();
            remove.add(Conditions.ORE_MODERATE);
            add.add(Conditions.ORE_ABUNDANT);
            commodities.add(Commodities.ORE);
            applier.init(60, remove, add, commodities);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(60, "aotd_demm_second", "Restoration", -2);
        }

        if (currentDecision.equals("demm_second2")) {
            HashMap<String, Float> map = new HashMap<>();
            map.put(Industries.MINING, 20000f);
            addUpkeepCond(map, 60,currentlyAffectedMarket);
            DelayedMarketCondPlugin applier = getDelayedMarketCondPlugin(currentlyAffectedMarket);
            ArrayList<String> remove, add, commodities;
            remove = new ArrayList<>();
            add = new ArrayList<>();
            commodities = new ArrayList<>();
            remove.add(Conditions.RARE_ORE_MODERATE);
            add.add(Conditions.RARE_ORE_ABUNDANT);
            commodities.add(Commodities.RARE_ORE);
            applier.init(60, remove, add, commodities);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(60, "aotd_demm_second", "Restoration", -2);


        }
        if (currentDecision.equals("demm_second3")) {
            HashMap<String, Float> map = new HashMap<>();
            map.put(Industries.MINING, 60000f);
            addUpkeepCond(map, 120,currentlyAffectedMarket);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(120, "aotd_demm_second3", "Grand Restoration", -4);
            currentlyAffectedMarket.getMemory().set("$aotd_demm_event_second", true);
        }
        if (currentDecision.equals("demm_final")) {
            ArrayList<String>ids = new ArrayList<>();
            for (Industry industry : currentlyAffectedMarket.getIndustries()) {
                if(industry.getSpec().hasTag("mining")){
                    ids.add(industry.getSpec().getId());
                }
            }
            for (String id : ids) {
                currentlyAffectedMarket.removeIndustry(id, MarketAPI.MarketInteractionMode.REMOTE,false);
            }
            currentlyAffectedMarket.addIndustry("mining_megaplex");
            MiningMegaplex megaplex = (MiningMegaplex) currentlyAffectedMarket.getIndustry("mining_megaplex");
            megaplex.setAICoreId(Commodities.ALPHA_CORE);
            megaplex.setFromEvent(true);
            megaplex.setSpecialItem(new SpecialItemData(Items.MANTLE_BORE, null));
            StarSystemAPI token =    AoTDMisc.getStarSystemWithMegastructure("aotd_pluto_station");
            Global.getSector().getIntelManager().addIntel(new MegastructureLocationIntel(token));

        }

        super.executeDecision(currentDecision);
    }


}
