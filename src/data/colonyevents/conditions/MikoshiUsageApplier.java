package data.colonyevents.conditions;

import data.colonyevents.models.BaseEventCondition;

public class MikoshiUsageApplier extends BaseEventCondition {
    public float threshold = 180;
    public  int GlobalPenalty = 1;
    public boolean removed = false;
    @Override
    public void apply(String id) {
        super.apply(id);
        if(!removed){
            market.getStability().modifyFlat("theta_mendling_"+getModId(),-(GlobalPenalty),"Mikoshi Usage");
        }

    }
    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getStability().unmodifyFlat("theta_mendling_"+getModId());
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(threshold);
    }


}
