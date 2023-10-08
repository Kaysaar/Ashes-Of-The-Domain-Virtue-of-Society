package data.colonyevents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AoTDColonyEventSpec {
    protected String eventId;
    protected String name;


    protected HashMap<String,String> options;
    protected  boolean isOneTimeEvent;
    protected boolean isPermanent;
    protected int duration;
    protected String plugin;
    protected String modId;
    protected int baseCooldown;



    protected String image;
    public String getImage() {
        return image;
    }
    public AoTDColonyEventSpec( String EventId,String Name, HashMap<String,String> Options,  boolean IsOneTimeEvent,boolean IsPermament, int Duration, String Plugin, String ModId, int BaseCooldown,String Image){
        this.eventId=EventId;

        this.name = Name;
        this.options = Options;
        this.isOneTimeEvent = IsOneTimeEvent;
        this.isPermanent = IsPermament;
        this.duration = Duration;
        this.plugin = Plugin;
        this.modId = ModId;
        this.baseCooldown = BaseCooldown;
        this.image = Image;
    }
    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }



    public HashMap<String, String> getOptions() {
        return options;
    }

    public boolean isOneTimeEvent() {
        return isOneTimeEvent;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public int getDuration() {
        return duration;
    }

    public String getPlugin() {
        return plugin;
    }

    public String getModId() {
        return modId;
    }

    public float getBaseCooldown() {
        return baseCooldown;
    }

    public static AoTDColonyEventSpec initSpecFromJson(JSONObject object) throws JSONException {
         String eventId = object.getString("eventId");
         String name= object.getString("name");
         HashMap<String,String> options = retrieveOptionsFromCsv(object.getString("options"));
         boolean isOneTimeEvent= object.getBoolean("isOneTime");
         boolean isPermanent= object.getBoolean("isPermament");
         int duration = object.getInt("duration");
         String plugin =object.getString("plugin");
        int baseCooldown = object.getInt("cooldown");
         String modId = object.getString("modId");
         String image =  object.getString("imageId");

        return new AoTDColonyEventSpec(eventId,name,options,isOneTimeEvent,isPermanent,duration,plugin,modId,baseCooldown,image);
    }
    static HashMap<String,String>retrieveOptionsFromCsv(String options){
        HashMap<String,String> toReturn = new HashMap<>();
        for (String s : options.split("\n")) {
            String[]splitted = s.split(":");
            toReturn.put(splitted[0],splitted[1]);
        }
        return toReturn;
    }
}
