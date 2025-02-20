package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin2;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.ui.P;
import org.lwjgl.opengl.GL11;

import java.util.LinkedHashMap;

public class SpecialItemOnIndustryTracker extends BaseMarketConditionPlugin2 {
    public class IndustryItemTrackerData{
        String itemId;
        float timeSincePlaced;

        public String getItemId() {
            return itemId;
        }

        public float getTimeSincePlaced() {
            return timeSincePlaced;
        }

        public void setItemId(String itemId) {
            if(itemId!=null){
                if(itemId.equals(this.itemId)){
                    return;
                }
            }
            this.itemId = itemId;
            timeSincePlaced = 0f;
        }

        public void setTimeSincePlaced(float timeSincePlaced) {
            this.timeSincePlaced = timeSincePlaced;
        }
        public IndustryItemTrackerData(String itemId) {
            this.itemId = itemId;
            timeSincePlaced =0f;
        }
        public boolean isItemInstalled(String itemId){
            return itemId.equals(this.itemId);
        }

        public void advance(float amount){
            if(itemId!=null){
                timeSincePlaced += Global.getSector().getClock().convertToDays(amount);

            }
        }
    }
    LinkedHashMap<String,IndustryItemTrackerData>data = new LinkedHashMap<>();
    IntervalUtil util = new IntervalUtil(2.5f,2.5f);
    public float getDaysSinceItemInstalled(String itemId,String industryId){
        if(data.containsKey(industryId)){
            IndustryItemTrackerData datten = data.get(industryId);
            if(datten.isItemInstalled(itemId)){
                return datten.getTimeSincePlaced();
            }
            return 0f;
        }
        return 0f;
    }
    @Override
    public void advance(float amount) {
        super.advance(amount);
        util.advance(amount);
        if(util.intervalElapsed()){
            for (Industry industry : market.getIndustries()) {
                if(!data.containsKey(industry.getId())){
                    data.put(industry.getSpec().getId(),new IndustryItemTrackerData(null));
                }
                if(industry.getSpecialItem()!=null){
                    data.get(industry.getSpec().getId()).setItemId(industry.getSpecialItem().getId());
                }

            }
            for (IndustryItemTrackerData value : data.values()) {
                value.advance(amount);
            }
        }


    }

    public static SpecialItemOnIndustryTracker getTrackerOnMarket(MarketAPI market){
        if(!market.hasCondition("aotd_item_tracker")){
            applyTrackerOnMarket(market);
        }
        MarketConditionAPI cond = market.getCondition("aotd_item_tracker");
        return (SpecialItemOnIndustryTracker) cond.getPlugin();

    }
    public static void applyTrackerOnMarket(MarketAPI market){
        if(!market.hasCondition("aotd_item_tracker")&&market.isInEconomy()){
            market.addCondition("aotd_item_tracker");
        }
    }

}
