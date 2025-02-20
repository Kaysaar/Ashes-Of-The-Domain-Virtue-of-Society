package data.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.impl.campaign.econ.ResourceDepositsCondition;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.intel.events.BaseEventIntel;
import com.fs.starfarer.api.impl.campaign.intel.events.RemnantHostileActivityFactor;
import com.fs.starfarer.api.impl.campaign.intel.group.FGRaidAction;
import com.fs.starfarer.api.impl.campaign.intel.group.FleetGroupIntel;
import com.fs.starfarer.api.impl.campaign.intel.group.GenericRaidFGI;
import com.fs.starfarer.api.impl.campaign.missions.FleetCreatorMission;
import com.fs.starfarer.api.impl.campaign.missions.hub.HubMissionWithTriggers;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD;
import data.colonyevents.conditions.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class AotDVosUtil {
    public static void clearSupply(Industry ind, String... ids) {
        for (String id : ids) {
            ind.getSupply(id).getQuantity().unmodify();
        }

    }

    public static boolean doesHaveCondFromCommodityCategory(String commodityId, MarketAPI market) {
        for (Map.Entry<String, String> entry : ResourceDepositsCondition.COMMODITY.entrySet()) {
            if (entry.getValue().equals(commodityId)) {
                if (market.hasCondition(entry.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static DelayedMarketCondPlugin getDelayedMarketCondPlugin(MarketAPI currentlyAffectedMarket) {
        String token = currentlyAffectedMarket.addCondition("aotd_delayed_conditions_applier");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        DelayedMarketCondPlugin applier = (DelayedMarketCondPlugin) cond.getPlugin();
        return applier;
    }

    public static void addUpkeepCond(HashMap<String, Float> map, float durr, MarketAPI currentlyAffectedMarket) {
        String token = currentlyAffectedMarket.addCondition("aotd_increase_cost");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        IncreaseUpkeepIndApplier applier = (IncreaseUpkeepIndApplier) cond.getPlugin();
        applier.initializePlugin(durr, map);
    }

    public static void addUpkeepCondForEntireMarket(float durr, MarketAPI currentlyAffectedMarket, float modifier) {
        String token = currentlyAffectedMarket.addCondition("aotd_increase_cost");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        IncreaseUpkeepIndApplier applier = (IncreaseUpkeepIndApplier) cond.getPlugin();
        applier.initializePlugin(durr, modifier);
    }

    public static void addDemandModifier(LinkedHashMap<String, Integer> map, float durr, MarketAPI currentlyAffectedMarket, int globalIncrease) {
        String token = currentlyAffectedMarket.addCondition("aotd_demand_manipulator");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        DemandManipulatorPlugin applier = (DemandManipulatorPlugin) cond.getPlugin();
        applier.init(durr, map, globalIncrease != 0, globalIncrease);
    }

    public static void addSupplyChange(LinkedHashMap<String, Integer> map, float durr, MarketAPI currentlyAffectedMarket, int globalIncrease) {
        String token = currentlyAffectedMarket.addCondition("aotd_supply_manipulator");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        SupplyManipulatorPlugin applier = (SupplyManipulatorPlugin) cond.getPlugin();
        applier.init(durr, map, globalIncrease != 0, globalIncrease);
    }

    public static void addAccessibilityChange(float change, float durr, MarketAPI currentlyAffectedMarket) {
        String token = currentlyAffectedMarket.addCondition("aotd_accesibilty_manipulator");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        AcessibilityChangeCondPlugin applier = (AcessibilityChangeCondPlugin) cond.getPlugin();
        applier.init(durr, change);
    }

    public static StatChangeCondPlugin getStatChangeCond(MarketAPI currentlyAffectedMarket) {
        String token = currentlyAffectedMarket.addCondition("aotd_stat_manipulator");
        MarketConditionAPI cond = currentlyAffectedMarket.getSpecificCondition(token);
        StatChangeCondPlugin applier = (StatChangeCondPlugin) cond.getPlugin();
        return applier;
    }

    public static StarSystemAPI getStarSystemWithNexus() {
        for (StarSystemAPI starSystem : Global.getSector().getStarSystems()) {
            if (RemnantHostileActivityFactor.getRemnantNexus(starSystem) != null) {
                return starSystem;
            }
        }
        return null;
    }

    public static boolean startAttack(MarketAPI target, StarSystemAPI system, Random random, FleetGroupIntel.FGIEventListener listener) {
        //System.out.println("RANDOM: " + random.nextLong());
        StarSystemAPI nexusStar = getStarSystemWithNexus();
        if(nexusStar==null)return false;
        CampaignFleetAPI nexus = RemnantHostileActivityFactor.getRemnantNexus(nexusStar);
        GenericRaidFGI.GenericRaidParams params = new GenericRaidFGI.GenericRaidParams(new Random(random.nextLong()), true);

        params.makeFleetsHostile = true;
        params.remnant = true;

        params.factionId = nexus.getFaction().getId();

        MarketAPI fake = Global.getFactory().createMarket(nexus.getId(), nexus.getName(), 3);
        fake.setPrimaryEntity(nexus);
        fake.setFactionId(params.factionId);
        fake.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat(
                "nexus_" + nexus.getId(), 1f);

        //nexus.setMarket(fake); // can actually trade etc then, no good

        params.source = fake;

        params.prepDays = 0f;
        params.payloadDays = 27f + 7f * random.nextFloat();

        params.raidParams.where = system;
        params.raidParams.type = FGRaidAction.FGRaidType.SEQUENTIAL;
        params.raidParams.tryToCaptureObjectives = false;
        params.raidParams.allowedTargets.add(target);
        params.raidParams.allowNonHostileTargets = true;
        params.raidParams.setBombardment(MarketCMD.BombardType.SATURATION);
        params.forcesNoun = "remnant forces";

        params.style = FleetCreatorMission.FleetStyle.STANDARD;
        params.repImpact = HubMissionWithTriggers.ComplicationRepImpact.FULL;


        // standard Askonia fleet size multiplier with no shortages/issues is a bit over 230%
        float fleetSizeMult = 2f;
        boolean damaged = nexus.getMemoryWithoutUpdate().getBoolean("$damagedStation");
        if (damaged) {
            fleetSizeMult = 0.5f;
        }

        float totalDifficulty = fleetSizeMult * 50f;

        totalDifficulty -= 10;
        params.fleetSizes.add(230);



        GenericRaidFGI raid = new GenericRaidFGI(params);
        raid.setListener(listener);
        Global.getSector().getIntelManager().addIntel(raid);

        return true;
    }
}
