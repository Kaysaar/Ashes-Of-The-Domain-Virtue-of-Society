package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.listeners.EconomyTickListener;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import data.colonyevents.models.BaseEventCondition;
import org.lazywizard.lazylib.MathUtils;

public class CrimeSyndicateApplier extends BaseEventCondition implements EconomyTickListener {
    public float maxDaysOnMarket = 600f;
    @Override
    public void apply(String id) {
        super.apply(id);
        if(condition.getId().equals("crime_syndicate_beginning")){
            market.getStability().modifyFlat("crime_beginning",-3,"Rampant Crime");
            market.getAccessibilityMod().modifyFlat("crime_beginning",-0.4f,"Rampant Crime");
        }
        if(condition.getId().equals("crime_syndicate_resolve_deal")){
            market.getStability().modifyFlat("crime_deal",-1,"Deal with crime lord");
            market.getStats().getDynamic().getStat(Stats.GROUND_DEFENSES_MOD).modifyFlat("crime_deal",2,"Deal with crime lord");
            market.getAccessibilityMod().modifyFlat("crime_deal",-0.2f,"Deal with crime lord");
            Global.getSector().getListenerManager().addListener(this);
        }
        if(condition.getId().equals("crime_syndicate_resolve_violence")){
            market.getStability().modifyFlat("crime_violence",-5,"Massive arrests");
        }
        if(condition.getId().equals("crime_syndicate_stabilize")){
            market.getStability().modifyFlat("crime_vanished",+1,"Justice prevails");
            market.getAccessibilityMod().modifyFlat("crime_vanished",0.05f,"Justice prevails");
        }
    }

    @Override
    public void unapply(String id) {
        if(condition.getId().equals("crime_syndicate_beginning")){
            market.getStability().unmodifyFlat("crime_beginning");
            market.getAccessibilityMod().unmodifyFlat("crime_beginning");
        }
        if(condition.getId().equals("crime_syndicate_resolve_deal")){
            market.getStability().unmodifyFlat("crime_deal");
            market.getStats().getDynamic().getStat(Stats.GROUND_DEFENSES_MOD).unmodifyFlat("crime_deal");
            market.getAccessibilityMod().unmodifyFlat("crime_deal");
        }
        if(condition.getId().equals("crime_syndicate_resolve_violence")){
            market.getStability().unmodifyFlat("crime_violence");
            market.getAccessibilityMod().unmodifyFlat("crime_vanished");

        }
        Global.getSector().getListenerManager().removeListener(this);

        super.unapply(id);
    }
    @Override
    public void advance(float amount) {
        super.advance(amount);
        if(condition.getId().equals("crime_syndicate_resolve_violence")){
            removeWhenPassCertainTime(maxDaysOnMarket);

        }
    }

    @Override
    public void reportEconomyTick(int iterIndex) {

    }

    @Override
    public void reportEconomyMonthEnd() {
        Global.getSector().getPlayerFleet().getCargo().getCredits().add(MathUtils.getRandomNumberInRange(10000,40000));
    }
}
