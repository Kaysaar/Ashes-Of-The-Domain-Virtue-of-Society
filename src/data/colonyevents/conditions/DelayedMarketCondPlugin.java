package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import data.colonyevents.models.BaseEventCondition;
import data.plugins.AotDVosUtil;

import java.util.ArrayList;

public class DelayedMarketCondPlugin extends BaseEventCondition {
    float daysToRemove = 10;
    ArrayList<String> condToRemove = new ArrayList<>();
    ArrayList<String> commoditiesToReapply = new ArrayList<>();
    ArrayList<String> condToAdd = new ArrayList<>();

    public void init(float daysToRemove, ArrayList<String> condToRemove,  ArrayList<String> condToAdd,ArrayList<String> commoditiesToReapply) {
        this.daysToRemove = daysToRemove;
        this.condToRemove = condToRemove;
        this.commoditiesToReapply = commoditiesToReapply;
        this.condToAdd = condToAdd;
    }

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
        removeWhenPassCertainTime(daysToRemove);
    }

    @Override
    public void removeWhenPassCertainTime(float amountOfDaysPassed) {
        super.removeWhenPassCertainTime(amountOfDaysPassed);
    }

    @Override
    public void clearCondition() {
        for (String s : condToRemove) {
            market.removeCondition(s);
        }
        for (String s : condToAdd) {
            String token=  market.addCondition(s);
            market.getSpecificCondition(token).setSurveyed(true);
        }
        for (Industry industry : market.getIndustries()) {
            AotDVosUtil.clearSupply(industry, commoditiesToReapply.toArray(new String[0]));
        }
        condToAdd.clear();
        commoditiesToReapply.clear();
        condToRemove.clear();
        super.clearCondition();
    }
}
