package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import data.colonyevents.models.BaseEventCondition;

import java.util.LinkedHashMap;

public class SupplyManipulatorPlugin extends BaseEventCondition {
    LinkedHashMap<String, Integer> supplyMap = new LinkedHashMap<>();
    boolean allSupply = false;
    int globalIncrease =0;
    float daysTill = 10;

    public void init(float daysTill, LinkedHashMap<String, Integer> supplyMap, boolean allSupply, int globalIncrease) {
        this.daysTill = daysTill;
        this.supplyMap.putAll(supplyMap);
        this.allSupply = allSupply;
        this.globalIncrease = globalIncrease;
    }

    @Override
    public void apply(String id) {
        super.apply(id);

        for (Industry industry : market.getIndustries()) {
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                if(allSupply){
                    mutableCommodityQuantity.getQuantity().modifyFlat("aotd_increase_event_supply"+"_"+getModId(), globalIncrease, "Recent events");

                }
                else {
                    if (supplyMap.get(mutableCommodityQuantity.getCommodityId()) != null) {
                        mutableCommodityQuantity.getQuantity().modifyFlat("aotd_increase_event_supply"+"_"+getModId(), supplyMap.get(mutableCommodityQuantity.getCommodityId()), "Recent events");
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
                mutableCommodityQuantity.getQuantity().unmodifyFlat("aotd_increase_event_supply"+"_"+getModId());
            }

        }
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(daysTill);
    }

    @Override
    public void clearCondition() {
        supplyMap.clear();
        super.clearCondition();
    }
}
