package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import data.colonyevents.models.BaseEventCondition;

import java.util.LinkedHashMap;

public class DemandManipulatorPlugin extends BaseEventCondition {
    LinkedHashMap<String, Integer> demandMapIncrease = new LinkedHashMap<>();
    boolean allDemandIncreaes = false;
    int globalIncrease =0;
    float daysTill = 10;

    public void init(float daysTill, LinkedHashMap<String, Integer> supplyMapIncrease, boolean allDemandIncreaes, int globalIncrease) {
        this.daysTill = daysTill;
        this.demandMapIncrease.putAll(supplyMapIncrease);
        this.allDemandIncreaes = allDemandIncreaes;
        this.globalIncrease = globalIncrease;
    }

    @Override
    public void apply(String id) {
        super.apply(id);

        for (Industry industry : market.getIndustries()) {
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllDemand()) {
                if(allDemandIncreaes){
                    mutableCommodityQuantity.getQuantity().modifyFlat("aotd_increase_event_demand"+"_"+getModId(), globalIncrease, "Recent events");

                }
                else {
                    if (demandMapIncrease.get(mutableCommodityQuantity.getCommodityId()) != null) {
                        mutableCommodityQuantity.getQuantity().modifyFlat("aotd_increase_event_demand"+"_"+getModId(), demandMapIncrease.get(mutableCommodityQuantity.getCommodityId()), "Recent events");
                    }
                }

            }

        }
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        for (Industry industry : market.getIndustries()) {
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllDemand()) {
                mutableCommodityQuantity.getQuantity().unmodifyFlat("aotd_increase_event_demand"+"_"+getModId());
            }

        }
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(daysTill);
    }

    @Override
    public void removeWhenPassCertainTime(float amountOfDaysPassed) {
        super.removeWhenPassCertainTime(amountOfDaysPassed);
    }

    @Override
    public void clearCondition() {
        demandMapIncrease.clear();
        super.clearCondition();
    }
}
