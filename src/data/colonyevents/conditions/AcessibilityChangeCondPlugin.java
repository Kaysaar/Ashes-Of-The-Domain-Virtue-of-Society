package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import data.colonyevents.models.BaseEventCondition;

import java.util.LinkedHashMap;

public class AcessibilityChangeCondPlugin extends BaseEventCondition {
    float change = 0f;
    float daysTill = 10;

    public void init(float daysTill,float changeOfAccesibilty) {
        this.daysTill = daysTill;
        this.change = changeOfAccesibilty;

    }

    @Override
    public void apply(String id) {
        super.apply(id);
        market.getAccessibilityMod().modifyFlat("aotd_accesibility_mod"+"_"+getModId(),change,"Recent events");
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getAccessibilityMod().unmodifyFlat("aotd_accesibility_mod"+"_"+getModId());

    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(daysTill);
    }
}
