package data.colonyevents.conditions;

import data.colonyevents.models.BaseEventCondition;

import java.util.HashMap;
import java.util.Map;

public class IncreaseUpkeepIndApplier extends BaseEventCondition {
    public float amountOfDaysNeeded = 10;
    public HashMap<String, Float> upkeepIncreaseMap = new HashMap<>();
    boolean marketMode = false;
    float marketIncrease;

    public void initializePlugin(float effectDays, HashMap<String, Float> upkeepMap) {
        this.amountOfDaysNeeded = effectDays;
        upkeepIncreaseMap.putAll(upkeepMap);
    }

    public void initializePlugin(float effectDays, float marketIncrease) {
        this.amountOfDaysNeeded = effectDays;
        this.marketIncrease = marketIncrease;
        marketMode = true;
    }

    public void initializePlugin(float effectDays,HashMap<String,Float> upkeepIncreaseMap, float marketIncrease) {
        this.amountOfDaysNeeded = effectDays;
        this.marketIncrease = marketIncrease;
        this.upkeepIncreaseMap.putAll(upkeepIncreaseMap);
        marketMode = true;
    }
    @Override
    public void apply(String id) {
        super.apply(id);
        for (Map.Entry<String, Float> entry : upkeepIncreaseMap.entrySet()) {
            if (market.hasIndustry(entry.getKey())) {
                market.getIndustry(entry.getKey()).getUpkeep().modifyFlat("aotd_upkeep_additional"+"_"+getModId(), entry.getValue(), "Increased upkeep");
            }
        }

        if (marketMode) {
            market.getUpkeepMult().modifyMult("aotd_upkeep_additional"+"_"+getModId(), marketIncrease, "Increased upkeep");
        }

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        for (String s : upkeepIncreaseMap.keySet()) {
            if (market.hasIndustry(s)) {
                market.getIndustry(s).getUpkeep().unmodifyFlat("aotd_upkeep_additional"+"_"+getModId());
            }
        }
        market.getUpkeepMult().unmodifyMult("aotd_upkeep_additional"+"_"+getModId());
    }

    @Override
    public boolean showIcon() {
        return false;
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(amountOfDaysNeeded);
    }

    @Override
    public void removeWhenPassCertainTime(float amountOfDaysPassed) {
        if ((int) timeSincePlaced >= (int) amountOfDaysPassed) {
            isToBeRemoved = true;
            upkeepIncreaseMap.clear();
            unapply(null);
            market.removeCondition(condition.getId());
            return;
        }
    }
}
