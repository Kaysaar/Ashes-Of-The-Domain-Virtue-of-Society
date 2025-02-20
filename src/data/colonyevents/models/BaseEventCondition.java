package data.colonyevents.models;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.Farming;
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
        timeSincePlaced+= Global.getSector().getClock().convertToDays(amount);


    }

    public void removeWhenPassCertainTime(float amountOfDaysPassed){
        if((int)timeSincePlaced>=(int)amountOfDaysPassed){
            clearCondition();
            return;
        }
    }

    public void clearCondition() {
        isToBeRemoved = true;
        unapply(null);
        market.removeCondition(condition.getId());
    }

    @Override
    public boolean showIcon() {
        return false;
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}
