package data.colonyevents.manager;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.comm.CommMessageAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.impl.campaign.intel.MessageIntel;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;
import data.colonyevents.models.AoTDColonyEventSpec;
import data.colonyevents.models.AoTDGuarantedEvent;
import data.colonyevents.ui.AoTDColonyEventOutomeDP;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class AoTDColonyEventManager {


    private static final Logger logger = Global.getLogger(AoTDColonyEventManager.class);
    public static final String specsFilename = "data/campaign/aotd_colony_events.csv";
    public static final String eventRepo = "$aotd_event_repo";
    public static final String ManagerMemo = "$aotd_manager_repo";
    public static final String currentMarketOfEventMemKey = "$currentMarketOfEvent";
    public static final String onGoingEventMemKey = "$ongoingEvent";
    public static final int intervalMin = 30;
    public static final int intervalMax = 45;
    public static final float maxTimeToMakeDecision = 7;
    public static final String lastEventMemKey = "$lastEvent";
    public float lastEvent = 0;
    @NotNull
    private Map<String, AoTDColonyEventSpec> eventsSpec = new HashMap<>();
    @NotNull
    private List<AoTDColonyEvent> events = new ArrayList<>();
    public AoTDColonyEvent onGoingEvent = null;
    public MarketAPI currentMarketWithEvent = null;

    public List<AoTDGuarantedEvent> guarantedEvents = new ArrayList<>();
    public String previousGuaranteedEventId = null;


    @NotNull
    public static AoTDColonyEventManager getInstance() {
        if (Global.getSector().getPersistentData().get(ManagerMemo) == null) {
            setInstance();
        }
        return (AoTDColonyEventManager) Global.getSector().getPersistentData().get(ManagerMemo);

    }

    public AoTDColonyEvent getEventById(String id) {
        for (AoTDColonyEvent event : events) {
            if (event.getSpec().getEventId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public static void setInstance() {
        AoTDColonyEventManager manager = new AoTDColonyEventManager();
        manager.eventsSpec.putAll(getSpecsFromFiles());
        manager.reloadEvents();
        manager.saveData();
    }

    public static @NotNull Map<String, AoTDColonyEventSpec> getSpecsFromFiles() {
        List<AoTDColonyEventSpec> newEventSpecs = new ArrayList<>();
        JSONArray eventCsvFromMod;
        logger.info("VOS: Starting reading from csv");
        try {
            eventCsvFromMod = Global.getSettings().getMergedSpreadsheetDataForMod("eventId", specsFilename, "aod_vos");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        logger.info("Accomplished fully reading CSV");
        for (int i = 0; i < eventCsvFromMod.length(); i++) {
            boolean skip = false;
            JSONObject item = null;
            String id;
            try {
                item = eventCsvFromMod.getJSONObject(i);
                id = item.getString("eventId");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            logger.info("VOS:" + id + " event has been read");
            if (id == null || id.isEmpty()) continue;
            for (AoTDColonyEventSpec newEventSpec : newEventSpecs) {
                if (newEventSpec.getModId() == id) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                try {
                    newEventSpecs.add(AoTDColonyEventSpec.initSpecFromJson(item));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Map<String, AoTDColonyEventSpec> newEventSpecById = new HashMap<>();
        for (AoTDColonyEventSpec newEventSpec : newEventSpecs) {
            newEventSpecById.put(newEventSpec.getEventId(), newEventSpec);
        }
        return newEventSpecById;

    }

    public void onGameLoad(boolean isGameSaveloaded) {
        this.eventsSpec.clear();
        this.eventsSpec.putAll(getSpecsFromFiles());
        reloadEvents();
        for (AoTDColonyEvent colonyEvent : events) {
            logger.info(colonyEvent.getSpec().getEventId() + "Spec and loaded class " + colonyEvent.getClass().getName());
        }
    }


    public List<AoTDColonyEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AoTDColonyEvent> updated) {
        events.clear();
        events.addAll(updated);
        if (onGoingEvent != null) {
            for (AoTDColonyEvent colonyEvent : events) {
                if (colonyEvent.getSpec().getEventId().equals(onGoingEvent.getSpec().getEventId())) {
                    colonyEvent.isWaitingForDecision = onGoingEvent.isWaitingForDecision;
                    colonyEvent.daysToMakeDecision = onGoingEvent.daysToMakeDecision;
                    colonyEvent.currentlyAffectedMarket = onGoingEvent.currentlyAffectedMarket;
                    onGoingEvent = colonyEvent;
                }
            }
        }


    }

    public void saveData() {
        Global.getSector().getPersistentData().put(ManagerMemo, this);
    }

    public AoTDColonyEvent getOnGoingEvent() {
        return onGoingEvent;
    }

    public void advance(float ammount) {
        float days = Global.getSector().getClock().convertToDays(ammount);

        lastEvent += days;
        if(guarantedEvents==null){
            guarantedEvents = new ArrayList<>();
        }
        for (AoTDGuarantedEvent guarantedEvent:guarantedEvents){
            if (guarantedEvent.timeToFireEvent > 0) {
                guarantedEvent.timeToFireEvent -= days;
            }
        }
        for (AoTDColonyEvent event : events) {
            if (event.isOnGoing || event.isWaitingForDecision) continue;
            if (event.cooldownBeforeEventCanOccur > 0) {
                event.cooldownBeforeEventCanOccur -= days;
            }
        }

        if (onGoingEvent != null && onGoingEvent.isWaitingForDecision) {
            onGoingEvent.daysToMakeDecision -= Global.getSector().getClock().convertToDays(ammount);
            if (onGoingEvent.daysToMakeDecision <= 0) {
                Global.getSector().getCampaignUI().showInteractionDialog(new AoTDColonyEventOutomeDP(), null);
                logger.info("Event Ended VOS ");
            }

        }

        if (checkForValidGuaranteedEvent() && onGoingEvent == null) {
            logger.info("Passed first interval of guaranteed event");
            AoTDGuarantedEvent guarantedEvent = popFirstGuaranteedEvent();
            AoTDColonyEvent event = pickGuaranteedEvent(guarantedEvent);

            if (event != null&& checkForMarketExistence(event)) {
                onGoingEvent = event;
                onGoingEvent.daysToMakeDecision = 7;
                lastEvent = intervalMin - 7;
                logger.info("We have assigned event " + onGoingEvent.getSpec().getName());
                Global.getSector().getCampaignUI().showInteractionDialog(new AoTDColonyEventOutomeDP(), null);
                return;
            }
        }

        if (lastEvent >= intervalMin && onGoingEvent == null ) {
            logger.info("Passed first interval");
            int numb = getRandomNumber(0, intervalMax - intervalMin);
            if ((numb >= (intervalMax - intervalMin) / 2) || lastEvent >= intervalMax) {
                logger.info("Passed second interval");
                currentMarketWithEvent = pickMarket();
                if (currentMarketWithEvent != null) {
                    logger.info("Picked Market for Event");
                }
                if (currentMarketWithEvent != null) {
                    AoTDColonyEvent event = pickEvent(currentMarketWithEvent);
                    if (event != null) {
                        onGoingEvent = event;
                        onGoingEvent.daysToMakeDecision = 7;
                        lastEvent = 0;
                        logger.info("We have assigned event " + onGoingEvent.getSpec().getName());
                        MessageIntel intel = new MessageIntel("Administrator, there has been major event occuring on  " + onGoingEvent.currentlyAffectedMarket.getName(), Misc.getBasePlayerColor());
                        intel.setIcon(Global.getSector().getPlayerFaction().getCrest());
                        intel.setSound(BaseIntelPlugin.getSoundMajorPosting());
                        intel.setImportant(true);
                        Global.getSector().getCampaignUI().addMessage(intel, CommMessageAPI.MessageClickAction.NOTHING);
                    }
                }
                lastEvent = 0;

            }
        }


    }
    public boolean checkForMarketExistence(AoTDColonyEvent event){
        if(event.currentlyAffectedMarket==null)return false;
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(true)) {
            if(playerMarket.getId().equals(event.currentlyAffectedMarket.getId())){
                return true;
            }
        }
        return false;
    }
    public void updateEvent(AoTDColonyEvent eventUpdated) {
        for (AoTDColonyEvent event : events) {
            if (event.getSpec().getEventId().equals(onGoingEvent.getSpec().getEventId())) {
                events.remove(event);
                events.add(eventUpdated);
                break;
            }
        }
//        for (AoTDColonyEvent event : events) {
//            if (event.getSpec().getEventId().equals(onGoingEvent.getSpec().getEventId())) {
//                event = eventUpdated;
//
//                break;
//            }
//        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    public AoTDColonyEvent pickEvent(MarketAPI marketAPI) {
        logger.info("Start Picking Event");
        List<AoTDColonyEvent> eventsList = new ArrayList<>(events);
        Random random = Misc.getRandom(getRandomNumber(1, 50000), 100);
        Collections.shuffle(eventsList, random);
        AoTDColonyEvent eventChoosen = null;
        for (AoTDColonyEvent event : eventsList) {
            if (!validateEvent(marketAPI, event))
                continue;
            event.isWaitingForDecision = true;
            event.currentlyAffectedMarket = marketAPI;
            event.haveFiredAtLeastOnce = true;
            logger.info("Event Picked");
            eventChoosen = event;
            break;
        }
        if (eventChoosen != null) {
            for (AoTDColonyEvent event : events) {
                if (event.getSpec().getEventId().equals(eventChoosen.getSpec().getEventId())) {
                    event = eventChoosen;
                    return event;
                }
            }
        }

        return eventChoosen;
    }

    private static boolean validateEvent(MarketAPI marketAPI, AoTDColonyEvent event) {
        boolean canFire = !event.isOnGoing && event.cooldownBeforeEventCanOccur <= 0 && !event.isWaitingForDecision && event.canOccur(marketAPI);
        if (event.getSpec().isOneTimeEvent() && canFire) {
            boolean unique = !event.haveFiredAtLeastOnce;
            return unique;
        }
        return canFire;


    }

    public MarketAPI pickMarket() {
        List<MarketAPI> markets = Misc.getPlayerMarkets(true);
        Collections.shuffle(markets);
        if (markets.isEmpty()) return null;
        for (MarketAPI market : markets) {
            if(market.getMemory().contains("$nex_playerOutpost")){
                continue;
            }
            return market;

        }
        return null;
    }

    public void reloadEvents() {
        Map<String, AoTDColonyEvent> eventsGeneratedFromSpecs = generateEventsFromSpec(eventsSpec);
        List<AoTDColonyEvent> updatedEvents = new ArrayList<>();
        List<AoTDColonyEvent> eventsBeforeUpdate = new ArrayList<>(events);
        events.clear();
        for (AoTDColonyEvent colonyEvent : eventsGeneratedFromSpecs.values()) {
            boolean wasAlreadyInRepo = false;
            logger.info(colonyEvent.getClass().getName() + "Event VOS");
            AoTDColonyEvent event = null;
            for (AoTDColonyEvent eventToChoose : eventsBeforeUpdate) {
                if (eventToChoose.getSpec().getEventId().equals(colonyEvent.getSpec().getEventId())) {
                    colonyEvent.haveFiredAtLeastOnce = eventToChoose.haveFiredAtLeastOnce;
                    colonyEvent.prevDecisionId = eventToChoose.prevDecisionId;
                    colonyEvent.isOnGoing = eventToChoose.isOnGoing;
                }
            }
            updatedEvents.add(colonyEvent);


        }

        setEvents(updatedEvents);


    }

    public static @NotNull Map<String, AoTDColonyEvent> generateEventsFromSpec(@NotNull Map<String, AoTDColonyEventSpec> specs) {
        Map<String, AoTDColonyEvent> newEventsById = new HashMap<>();
        for (AoTDColonyEventSpec value : specs.values()) {
            try {
                final Class<?> eventPlugin = Global.getSettings().getScriptClassLoader().loadClass(value.getPlugin());
                if (!AoTDColonyEvent.class.isAssignableFrom(eventPlugin)) {
                    throw new RuntimeException(String.format("%s does not extend %s", eventPlugin.getCanonicalName(), AoTDColonyEvent.class.getCanonicalName()));
                }
                AoTDColonyEvent colonyEvent = (AoTDColonyEvent) eventPlugin.newInstance();
                colonyEvent.setSpec(value);
                colonyEvent.setLoadedOptions();
                newEventsById.put(value.getEventId(), colonyEvent);
                logger.info("Loaded VOS Event " + colonyEvent.getSpec().getEventId() + " from " + value.getModId() + " with script " + value.getPlugin() + ".");
            } catch (Exception e) {
                throw new RuntimeException("Class not found");
            }
        }
        return newEventsById;
    }
    public AoTDGuarantedEvent popFirstGuaranteedEvent(){
        int index = 0;
        for (AoTDGuarantedEvent guarantedEvent : guarantedEvents) {
            if(guarantedEvent.timeToFireEvent<=0){
                return guarantedEvents.remove(index);
            }
            index++;
        }
        return null;
    }
    public boolean checkForValidGuaranteedEvent(){
        for (AoTDGuarantedEvent guarantedEvent : guarantedEvents) {
            if(guarantedEvent.timeToFireEvent<=0){
                return true;
            }
        }
        return false;
    }
    public void addGuaranteedEvent(String id , String marketId, float timeToFire){
        guarantedEvents.add(new AoTDGuarantedEvent(id,marketId,timeToFire));
    }
    public AoTDColonyEvent pickGuaranteedEvent(AoTDGuarantedEvent guarantedEvent) {
        logger.info("Start Guaranteed Event");
        List<AoTDColonyEvent> eventsList = new ArrayList<>(events);
        Random random = Misc.getRandom(getRandomNumber(1, 50000), 100);
        Collections.shuffle(eventsList, random);
        AoTDColonyEvent eventChoosen = null;
        for (AoTDColonyEvent event : eventsList) {
            if(!event.getSpec().getEventId().equals(guarantedEvent.eventId))continue;
            event.isWaitingForDecision = true;
            event.currentlyAffectedMarket = findMarketOfFaction(Factions.PLAYER,guarantedEvent.idOfMarket);
            event.haveFiredAtLeastOnce = true;
            logger.info("Event Picked");
            eventChoosen = event;
            break;
        }
        if (eventChoosen != null) {
            for (AoTDColonyEvent event : events) {
                if (event.getSpec().getEventId().equals(eventChoosen.getSpec().getEventId())) {
                    event = eventChoosen;
                    return event;
                }
            }
        }

        return eventChoosen;
    }
    public MarketAPI findMarketOfFaction(String factionId, String marketId){
        if(factionId.equals(Factions.PLAYER)){
            for (MarketAPI s :Misc.getPlayerMarkets(true)) {
                if(s.getId().equals(marketId)){
                    return s;
                }
            }
        }
        else{
            for (MarketAPI marketAPI : Global.getSector().getEconomy().getMarketsCopy()) {
                if(marketAPI.getId().equals(marketId)&&marketAPI.getFactionId().equals(factionId)){
                    return marketAPI;
                }
            }
        }
        return null;
    }
}
