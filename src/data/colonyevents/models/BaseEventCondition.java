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
        super.advance(amount);
        timeSincePlaced+= Global.getSector().getClock().convertToDays(amount);
    }

    public void removeWhenPassCertainTime(float amountOfDaysPassed){
        if(timeSincePlaced>=amountOfDaysPassed){
            market.removeCondition(this.getModId());
            isToBeRemoved=true;
        }
    }

    @Override
    public boolean showIcon() {
        return false;
    }
}
