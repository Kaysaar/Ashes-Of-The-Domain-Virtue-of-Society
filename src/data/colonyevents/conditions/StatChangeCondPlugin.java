package data.colonyevents.conditions;

import com.fs.starfarer.api.combat.MutableStat;
import data.colonyevents.models.BaseEventCondition;

import java.util.ArrayList;

public class StatChangeCondPlugin extends BaseEventCondition {
    public enum StatType{
        FLAT,
        MULT,
        PERCENT
    }
    public class StatChangeData{
        public String id;
        public StatType type;
        public float value;
        public String description;
        public StatChangeData(String id, StatType type, float value, String description){
            this.id = id;
            this.type = type;
            this.value = value;
            this.description = description;
        }
    }
    ArrayList<StatChangeData>data = new ArrayList<StatChangeData>();

    public void addChangeStat(String id, StatType type, float value, String description){
        StatChangeData data = new StatChangeData(id, type, value, description);
        this.data.add(data);
    }
    public  float tillRemoval = 10;
    public void setTillRemoval(float tillRemoval){
        this.tillRemoval = tillRemoval;
    }
    @Override
    public void apply(String id) {
        super.apply(id);
        for (StatChangeData datum : data) {
            MutableStat stat = market.getStats().getDynamic().getStat(datum.id);
            if(datum.type == StatType.FLAT){
                stat.modifyFlat("aotd_stat_changer_"+getModId(), datum.value, datum.description);
            }
            if(datum.type == StatType.MULT){
                stat.modifyMult("aotd_stat_changer_"+getModId(), datum.value, datum.description);

            }
            if(datum.type == StatType.PERCENT){
                stat.modifyPercent("aotd_stat_changer_"+getModId(), datum.value, datum.description);

            }

        }
    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        for (StatChangeData datum : data) {
            MutableStat stat = market.getStats().getDynamic().getStat(datum.id);
            stat.unmodifyFlat("aotd_stat_changer_"+getModId());
        }
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        removeWhenPassCertainTime(tillRemoval);
    }

    @Override
    public void clearCondition() {
        data.clear();
        super.clearCondition();
    }
}
