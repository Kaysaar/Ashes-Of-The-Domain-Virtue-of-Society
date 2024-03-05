package data.colonyevents.conditions;

import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.BaseEventCondition;

import java.awt.*;

public class CryoSanctumEventApplier extends BaseEventCondition {
    public float maxDaysOnMarket = 720f;

    @Override
    public void apply(String id) {
        super.apply(id);
        if(isToBeRemoved)return;
        if(getModId().equals("cryosanctum_event_organs")){
            market.getStability().modifyFlat("organs",-3,"Recent Event");
        }
        if(getModId().equals("cryosanctum_event_growth")){
            float income = market.getGrossIncome();
            market.getIndustry(Industries.POPULATION).getUpkeep().modifyFlat("organs",income/5,"Sudden Growth");
        }

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getStability().unmodifyFlat("organs");
        market.getIndustry(Industries.POPULATION).getUpkeep().unmodifyFlat("organs");

    }
    @Override
    public void advance(float amount) {
        super.advance(amount);
        market.getStability().unmodifyFlat("organs");
        market.getIndustry(Industries.POPULATION).getUpkeep().unmodifyFlat("organs");
        removeWhenPassCertainTime(maxDaysOnMarket);
    }


}
