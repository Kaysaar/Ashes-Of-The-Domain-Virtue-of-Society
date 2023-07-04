package data.plugins;


import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.ListenerManagerAPI;
import data.Ids.AodCryosleeperConditions;
import data.Ids.AoDIndustries;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.AbilityPlugin;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.CryoShowGrowthListener;
import data.scripts.CryoAdditionalListener;
import data.scripts.industry.KaysaarCryorevival;
import org.magiclib.util.MagicSettings;

//import data.ui.ExampleIDP;


public class AodCryosleeperPLugin extends BaseModPlugin {
    public  static int POP_SIZE = org.magiclib.util.MagicSettings.getInteger("Cryo_but_better", "MAX_POP_GROWTH");
    public  int maxTriTachyonElectronics = 2;
    public static String aodTech = "$Aodtecha";
    public  boolean randomIndustrySpawn =MagicSettings.getBoolean("Cryo_but_better","RANDOM_SECTOR");
    public static int  configSize=Misc.MAX_COLONY_SIZE;

    private void setListenersIfNeeded() {
        ListenerManagerAPI l = Global.getSector().getListenerManager();

        if (!l.hasListenerOfClass(CryoAdditionalListener.class))
            l.addListener(new CryoAdditionalListener(), true);
        if (!l.hasListenerOfClass(CryoShowGrowthListener.class))
            l.addListener(new CryoShowGrowthListener(), true);
    }
    public void onGameLoad(boolean newGame) {
        setListenersIfNeeded();
        CampaignEventListener customlistener = new CampaignEventListener() {
            @Override
            public void reportPlayerOpenedMarket(MarketAPI market) {
                if (market.hasCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER) && market.hasIndustry(AoDIndustries.CRYOREVIVAL) && market.getSize() < POP_SIZE && !market.getIndustry(AoDIndustries.CRYOREVIVAL).isBuilding()) {
                    KaysaarCryorevival ind = (KaysaarCryorevival) market.getIndustry(AoDIndustries.CRYOREVIVAL);
                    if(ind.canGrow){
                        Misc.MAX_COLONY_SIZE = POP_SIZE;
                    }

                }


            }


            @Override
            public void reportPlayerClosedMarket(MarketAPI market) {
                Misc.MAX_COLONY_SIZE = configSize;

            }

            @Override
            public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {

            }

            @Override
            public void reportEncounterLootGenerated(FleetEncounterContextPlugin plugin, CargoAPI loot) {

            }

            @Override
            public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {

            }

            @Override
            public void reportBattleOccurred(CampaignFleetAPI primaryWinner, BattleAPI battle) {

            }

            @Override
            public void reportBattleFinished(CampaignFleetAPI primaryWinner, BattleAPI battle) {

            }

            @Override
            public void reportPlayerEngagement(EngagementResultAPI result) {

            }

            @Override
            public void reportFleetDespawned(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {

            }

            @Override
            public void reportFleetSpawned(CampaignFleetAPI fleet) {

            }

            @Override
            public void reportFleetReachedEntity(CampaignFleetAPI fleet, SectorEntityToken entity) {

            }

            @Override
            public void reportFleetJumped(CampaignFleetAPI fleet, SectorEntityToken from, JumpPointAPI.JumpDestination to) {

            }

            @Override
            public void reportShownInteractionDialog(InteractionDialogAPI dialog) {

            }

            @Override
            public void reportPlayerReputationChange(String faction, float delta) {

            }

            @Override
            public void reportPlayerReputationChange(PersonAPI person, float delta) {

            }

            @Override
            public void reportPlayerActivatedAbility(AbilityPlugin ability, Object param) {

            }

            @Override
            public void reportPlayerDeactivatedAbility(AbilityPlugin ability, Object param) {

            }

            @Override
            public void reportPlayerDumpedCargo(CargoAPI cargo) {

            }


            @Override
            public void reportPlayerDidNotTakeCargo(CargoAPI cargo) {

            }

            @Override
            public void reportEconomyTick(int iterIndex) {

            }

            @Override
            public void reportEconomyMonthEnd(){
                //for testing purpouse

            }
        };
        if (!Global.getSector().hasScript(CryoShowGrowthListener.class)) {
            Global.getSector().addScript(new CryoShowGrowthListener()); //Check for dittos

        }

        if (!Global.getSector().hasScript(CryoAdditionalListener.class)) {
            Global.getSector().addScript(new CryoAdditionalListener()); //Check for dittos

        }
        Global.getSector().addListener(customlistener);

    }
 }
