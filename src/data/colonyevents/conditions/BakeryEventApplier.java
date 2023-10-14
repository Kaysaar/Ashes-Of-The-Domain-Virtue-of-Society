package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import data.colonyevents.models.BaseEventCondition;
import data.colonyevents.scripts.UAFBakeryDefenderFleetManager;

public class BakeryEventApplier extends BaseEventCondition {
    @Override
    public void apply(String id) {
        super.apply(id);
        if(Global.getSector().getPlayerFaction().knowsIndustry("uaf_bakery_branch")){
            Global.getSector().getMemory().set("$uaf_bakery_permit",true);
        }
        if(getModId().contains("aotd_bakery_assistance")){
            market.getAccessibilityMod().modifyFlat("uaf_bakery_fleet",0.1f,"Pastry Fleet Protection");
            try {
                if(market.getPrimaryEntity()!=null){
                    SectorEntityToken entity = market.getPrimaryEntity();
                    if(!entity.hasScriptOfClass(UAFBakeryDefenderFleetManager.class)){
                        entity.addScript(new UAFBakeryDefenderFleetManager(entity,1f,1,1,3));
                    }
                }
            }
            catch (Throwable throwable){

            }
        }
        else{
            if(market.hasIndustry("uaf_bakery_fake")){
                market.getStability().modifyFlat("uaf_fake_bakery_good",1, market.getFaction().getDisplayName()+" Pastries");
            }
            else{
                market.getStability().modifyFlat("uaf_fake_bakery_bad",-1, market.getFaction().getDisplayName()+"lack of pastries");

            }
        }

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getAccessibilityMod().unmodifyFlat("uaf_bakery_fleet");
        market.getStability().unmodifyFlat("uaf_fake_bakery_good");
        market.getStability().unmodifyFlat("uaf_fake_bakery_bad");
    }

}
