package data.colonyevents.models;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

public class AoTDGuarantedEvent {
    public String eventId;
    public String idOfMarket;
    public float timeToFireEvent = 0.0f;
    public AoTDGuarantedEvent(String eventId,String idOfMarket, float timeToFire){
        this.eventId = eventId;
        this.idOfMarket = idOfMarket;
        this.timeToFireEvent = timeToFire;
    }
}
