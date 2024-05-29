package data.colonyevents.conditions;

import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.util.Pair;
import data.colonyevents.models.BaseEventCondition;

public class UndergroundHiveApplier extends BaseEventCondition {
    String hiveEffect = "hive_effect";
    @Override
    public void apply(String id) {
        super.apply(id);
        if(isToBeRemoved)return;
        if(getModId().contains("hive_defence")){
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD)
                    .modifyMult("hive_effect", 1.25f, "Hive Creatures");
        }
        if(getModId().contains("hive_food")){
            for (Industry industry : market.getIndustries()) {
                for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                    if(mutableCommodityQuantity.getCommodityId().equals(Commodities.LUXURY_GOODS)){
                        industry.getDemand(Commodities.MARINES).getQuantity().modifyFlat(hiveEffect,4,"Protection of workers");
                        if(getMaxDeficit(industry,Commodities.MARINES).two==0){
                            mutableCommodityQuantity.getQuantity().modifyFlat(hiveEffect,2,"Hive Eggs");
                        }
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD)
                .unmodifyFlat("hive_effect");
        for (Industry industry : market.getIndustries()) {
            for (MutableCommodityQuantity mutableCommodityQuantity : industry.getAllSupply()) {
                if(mutableCommodityQuantity.getCommodityId().equals(Commodities.LUXURY_GOODS)){
                    industry.getDemand(Commodities.MARINES).getQuantity().unmodifyFlat(hiveEffect);
                    mutableCommodityQuantity.getQuantity().unmodifyFlat(hiveEffect);
                }
            }
        }
    }



    @Override
    public void advance(float amount) {
        super.advance(amount);
    }
    public Pair<String, Integer> getMaxDeficit(Industry ind, String ... commodityIds) {
        Pair<String, Integer> result = new Pair<String, Integer>();
        result.two = 0;
        for (String id : commodityIds) {
            int demand = (int) ind.getDemand(id).getQuantity().getModifiedValue();
            CommodityOnMarketAPI com = ind.getMarket().getCommodityData(id);
            int available = com.getAvailable();

            int deficit = Math.max(demand - available, 0);
            if (deficit > result.two) {
                result.one = id;
                result.two = deficit;
            }
        }
        return result;
    }
}
