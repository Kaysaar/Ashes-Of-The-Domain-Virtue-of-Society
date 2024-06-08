package data.colonyevents.models;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.campaign.econ.MarketCondition;

public abstract class BaseEventCondition extends BaseMarketConditionPlugin {
    public float timeSincePlaced = 0f;
    public boolean isToBeRemoved = false;

    @Override
    public void apply(String id) {
        super.apply(id);
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
    }

    @Override
    public void advance(float amount) {
        if(market.getMemory().contains("$"+getModId()+"event_timer")){
            timeSincePlaced = market.getMemory().getFloat("$"+getModId()+"event_timer");
        }
        timeSincePlaced+= Global.getSector().getClock().convertToDays(amount);
        market.getMemory().set("$"+getModId()+"event_timer",timeSincePlaced);

    }

    public void removeWhenPassCertainTime(float amountOfDaysPassed){
        if((int)timeSincePlaced>=(int)amountOfDaysPassed){
            isToBeRemoved = true;
            unapply(null);
            market.removeCondition(condition.getId());
            market.getMemory().unset("$"+getModId()+"event_timer");
            return;
        }
    }

    @Override
    public boolean showIcon() {
        return false;
    }
}
