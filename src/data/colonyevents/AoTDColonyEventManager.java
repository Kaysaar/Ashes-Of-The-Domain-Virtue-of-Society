package data.colonyevents;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ModSpecAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.IndustryOptionProvider;
import com.fs.starfarer.api.campaign.listeners.TestIndustryOptionProvider;
import com.fs.starfarer.api.impl.campaign.econ.impl.PopulationAndInfrastructure;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.ui.P;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class AoTDColonyEventManager {


    private static final Logger logger = Global.getLogger(AoTDColonyEventManager.class);
    public static final String specsFilename = "data/campaign/aotd_colony_events.csv";
    public static final String eventRepo = "$aotd_event_repo";
    public static final String ManagerMemo = "$aotd_manager_repo";
    public static final String currentMarketOfEventMemKey = "$currentMarketOfEvent";
    public static final String onGoingEventMemKey = "$ongoingEvent";
    public static final int intervalMin = 5;
    public static final int intervalMax = 10;
    public static final float maxTimeToMakeDecision = 7;
    public static final String lastEventMemKey = "$lastEvent";
    public float lastEvent = 0;

    @NotNull
    private Map<String, AoTDColonyEventSpec> eventsSpec = new HashMap<>();
    @NotNull
    private List<AoTDColonyEvent> events = new ArrayList<>();
    public AoTDColonyEvent onGoingEvent = null;
    public MarketAPI currentMarketWithEvent = null;

    @NotNull
    public static AoTDColonyEventManager getInstance() {
        return (AoTDColonyEventManager) Global.getSector().getPersistentData().get(ManagerMemo);

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
        logger.info("Accomplished reading fully CSV");
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
                if (newEventSpec.modId == id) {
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
            newEventSpecById.put(newEventSpec.eventId, newEventSpec);
        }
        return newEventSpecById;

    }

    public void onGameLoad(boolean isGameSaveloaded) {
        eventsSpec.clear();
        eventsSpec.putAll(getSpecsFromFiles());
        reloadEvents();
        for (AoTDColonyEvent colonyEvent : events) {
            logger.info(colonyEvent.spec.getEventId() + "Spec and loaded class " + colonyEvent.getClass().getName());
        }
    }


    public List<AoTDColonyEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AoTDColonyEvent> updated) {
        events.clear();
        events.addAll(updated);
        if(onGoingEvent!=null){
            for (AoTDColonyEvent colonyEvent : events) {
                if(colonyEvent.getSpec().getEventId().equals(onGoingEvent.getSpec().getEventId())){
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
        float prevval = lastEvent;
        lastEvent += days;
        for (AoTDColonyEvent event : events) {
            if (event.isOnGoing || event.isWaitingForDecision) continue;
            if (event.cooldownBeforeEventCanOccur > 0) {
                event.cooldownBeforeEventCanOccur -= Global.getSector().getClock().convertToDays(ammount);
            }
        }

        if (onGoingEvent != null && onGoingEvent.isWaitingForDecision) {
            onGoingEvent.daysToMakeDecision -= Global.getSector().getClock().convertToDays(ammount);
            if (onGoingEvent.daysToMakeDecision <= 0) {
                logger.info("Event Ended VOS ");
                onGoingEvent.daysToMakeDecision = 7;
                onGoingEvent.isWaitingForDecision = false;
                onGoingEvent.cooldownBeforeEventCanOccur = onGoingEvent.getSpec().getBaseCooldown();
                updateEvent(onGoingEvent);
                onGoingEvent = null;
            }

        }
        if (onGoingEvent != null && onGoingEvent.isWaitingForDecision) {
            onGoingEvent.apply();
        }

        if (lastEvent >= intervalMin && onGoingEvent == null) {
            logger.info("Passed first interval");
            int numb = getRandomNumber(0, intervalMax - intervalMin);
            if (lastEvent >= intervalMax) {
                logger.info("Passed second interval");
                currentMarketWithEvent = pickMarket();
                if (currentMarketWithEvent != null) {
                    logger.info("Picked Market for Event");
                }
                if(currentMarketWithEvent!=null){
                    AoTDColonyEvent event = pickEvent(currentMarketWithEvent);
                    if (event != null) {
                        onGoingEvent = event;
                        onGoingEvent.daysToMakeDecision = 7;
                        lastEvent = 0;
                        logger.info("We have assigned event "+onGoingEvent.getSpec().getName());
                    }
                }



            }
        }


    }

    public void updateEvent(AoTDColonyEvent eventUpdated) {
        for (AoTDColonyEvent event : events) {
            if(event.getSpec().getEventId().equals(onGoingEvent.getSpec().getEventId())){
                event = eventUpdated;
            }
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public List<IndustryOptionProvider.IndustryOptionData> getResolveEventOption(Industry industry, IndustryOptionProvider provider) {
        if (onGoingEvent == null && industry.getMarket().getId() != currentMarketWithEvent.getId()) return null;

        if (industry instanceof PopulationAndInfrastructure) {

            IndustryOptionProvider.IndustryOptionData data = new IndustryOptionProvider.IndustryOptionData("Resolve Event", onGoingEvent, industry, provider);
            data.color = Color.CYAN;
            ArrayList<IndustryOptionProvider.IndustryOptionData> list = new ArrayList<>();
            list.add(data);
            return list;
        }
        return null;
    }

    public AoTDColonyEvent pickEvent(MarketAPI marketAPI) {
        logger.info("Start Picking Event");
        List<AoTDColonyEvent> eventsList = events;
        Collections.shuffle(eventsList);
        AoTDColonyEvent eventChoosen = null;
        for (AoTDColonyEvent event : eventsList) {
            if (event.isOnGoing || event.cooldownBeforeEventCanOccur > 0 || event.isWaitingForDecision || !event.canOccur(marketAPI))
                continue;
            event.isWaitingForDecision = true;
            event.currentlyAffectedMarket = marketAPI;
            logger.info("Event Picked");
           eventChoosen = event;
           break;
        }
        if(eventChoosen!=null){
            for (AoTDColonyEvent event : events) {
                if(event.getSpec().getEventId().equals(eventChoosen.getSpec().getEventId())){
                    event = eventChoosen;
                    return event;
                }
            }
        }

        return eventChoosen;
    }

    public MarketAPI pickMarket() {
        List<MarketAPI> markets = Misc.getPlayerMarkets(true);
        Collections.shuffle(markets);
        return markets.get(0);
    }

    public void reloadEvents()  {
        Map<String, AoTDColonyEvent> eventsGeneratedFromSpecs = generateEventsFromSpec(eventsSpec);
        List<AoTDColonyEvent> updatedEvents = new ArrayList<>();
            for (AoTDColonyEvent colonyEvent : eventsGeneratedFromSpecs.values()) {
                boolean wasAlreadyInRepo = false;
                logger.info(colonyEvent.getClass().getName() + "Event VOS");
                AoTDColonyEvent event = null;
                for (AoTDColonyEvent eventToChoose : events) {
                    if(eventToChoose.getSpec().getEventId().equals(colonyEvent.getSpec().getEventId())){
                        event = eventToChoose;
                    }
                }
                if(event==null){
                    updatedEvents.add(colonyEvent);
                }
                else{
                Class<?>updatedClass = colonyEvent.getClass();
                Class<?>oldClass = event.getClass();
                    for (Field declaredField : updatedClass.getDeclaredFields()) {
                        for (Field field : oldClass.getDeclaredFields()) {
                           if( field.getName().equals(declaredField.getName())){
                               field.setAccessible(true);
                               try {
                                   declaredField.set(declaredField,field.get(oldClass));
                               } catch (IllegalAccessException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                        }
                    }
                    updatedEvents.add(colonyEvent);

                }

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
                colonyEvent.spec = value;
                newEventsById.put(value.getEventId(), colonyEvent);
                logger.info("Loaded VOS Event " + colonyEvent.spec.getEventId() + " from " + value.getModId() + " with script " + value.getPlugin() + ".");
            } catch (Exception e) {
                throw new RuntimeException("Class not found");
            }
        }
        return newEventsById;
    }

}
