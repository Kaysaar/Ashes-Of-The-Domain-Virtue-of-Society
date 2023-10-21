package data.colonyevents.conditions;

import data.colonyevents.models.BaseEventCondition;

public class CrestOfPhoenixApplier extends BaseEventCondition {
    @Override
    public void apply(String id) {
        super.apply(id);
        this.market.getStability().modifyFlat("phoenix_reborned",+5,"New Colossus");
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        this.market.getStability().unmodifyFlat("phoenix_reborned");
    }
}
