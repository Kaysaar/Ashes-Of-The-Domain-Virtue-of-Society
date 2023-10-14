package data.colonyevents.scripts;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEventListener;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.fleets.SourceBasedFleetManager;
import com.fs.starfarer.api.impl.campaign.ids.Abilities;
import com.fs.starfarer.api.impl.campaign.ids.FleetTypes;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import org.lazywizard.lazylib.MathUtils;

import java.util.Random;

public class UAFBakeryDefenderFleetManager extends SourceBasedFleetManager {


    public UAFBakeryDefenderFleetManager(SectorEntityToken source, float thresholdLY, int minFleets, int maxFleets, float respawnDelay) {
        super(source, thresholdLY, minFleets, maxFleets, respawnDelay);
    }

    @Override
    public void advance(float amount) {

        super.advance(amount);
    }

    @Override
    protected CampaignFleetAPI spawnFleet() {
        if(source==null)return null;
        if (source.getMarket() == null) return null;
        if (source.getMarket().getSize() == 1) return null;
        if (source.getMarket().getFaction() == null) return null;
        String type = FleetTypes.PATROL_SMALL;
        float basePoints = MathUtils.getRandomNumberInRange(50f, 100f);
        float modifier = source.getMarket().getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).computeEffective(0f);
        Random random = new Random();
        float points = basePoints * modifier;
        if (points > 90) type = FleetTypes.PATROL_MEDIUM;
        if (points > 160) type = FleetTypes.PATROL_LARGE;
        FleetParamsV3 params =  new FleetParamsV3(
                source.getMarket(),
                source.getLocationInHyperspace(),
                "uaf",
                1f,
                type,
                points,
                0f,  // freighterPts
                0f,  // tankerPts
                0f,  // transportPts
                0f,  // linerPts
                0f,  // utilityPts
                0f
        );
        params.random = random;
        CampaignFleetAPI fleet = FleetFactoryV3.createFleet(params);
        if(fleet==null)return null;
        source.getMarket().getContainingLocation().addEntity(fleet);
        initDefenderProperties(random,fleet,false);
        fleet.setLocation(source.getLocation().x,source.getLocation().y);
        fleet.setFacing(random.nextFloat()*360f);
        fleet.addAssignment(FleetAssignment.DEFEND_LOCATION,source,1000000f,"Protecting the confectionary secret recipe.");

        fleet.setFaction(source.getMarket().getFaction().getId());
        fleet.setName("Pastry Copyright Protectors");
        fleet.setNoFactionInName(true);
        long delayTimestamp = Global.getSector().getClock().getTimestamp();
        long launchDelayDays = MathUtils.getRandomNumberInRange(2, 4);

        return fleet;
    }
    public void initDefenderProperties(Random rand, CampaignFleetAPI fleet, boolean isDormant){
        fleet.removeAbility(Abilities.EMERGENCY_BURN);
        fleet.removeAbility(Abilities.SENSOR_BURST);
        fleet.removeAbility(Abilities.GO_DARK);
        fleet.setTransponderOn(true);
        fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_PATROL_FLEET, true);
        fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_ALLOW_LONG_PURSUIT, true);
        fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_HOLD_VS_STRONGER, true);
        fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_HOLD_VS_STRONGER, true);

    }


}
