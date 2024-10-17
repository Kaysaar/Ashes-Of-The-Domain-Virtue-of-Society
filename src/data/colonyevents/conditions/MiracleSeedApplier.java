package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.Farming;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import data.colonyevents.models.BaseEventCondition;

public class MiracleSeedApplier extends BaseEventCondition {
    boolean shouldAddRichFarmland = false;

    public void setShouldAddRichFarmland(boolean shouldAddRichFarmland) {
        this.shouldAddRichFarmland = shouldAddRichFarmland;
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        if(timeSincePlaced>=90&&!market.hasCondition(Conditions.FARMLAND_ADEQUATE)){
            market.removeCondition(Conditions.FARMLAND_POOR);
            for (Industry industry : market.getIndustries()) {
                industry.getSupply(Commodities.FOOD).getQuantity().unmodify();
            }
            MarketConditionAPI id = market.getSpecificCondition(market.addCondition(Conditions.FARMLAND_ADEQUATE));
            id.setSurveyed(true);
        }

        if(timeSincePlaced>=180&&!market.hasCondition(Conditions.FARMLAND_RICH)&&shouldAddRichFarmland){
            market.removeCondition(Conditions.FARMLAND_ADEQUATE);
            market.removeCondition(Conditions.WATER_SURFACE);
            for (Industry industry : market.getIndustries()) {
                industry.getSupply(Commodities.FOOD).getQuantity().unmodify();
            }
            MarketConditionAPI id = market.getSpecificCondition(market.addCondition(Conditions.FARMLAND_RICH));
            id.setSurveyed(true);






        }

        removeWhenPassCertainTime(180);
    }
}
