package data.colonyevents.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import data.colonyevents.models.BaseEventCondition;
import data.colonyevents.scripts.DerelictEnforcerFleetManager;
import data.colonyevents.scripts.UAFBakeryDefenderFleetManager;

public class AncientFactoryApplier extends BaseEventCondition {
    @Override
    public void apply(String id) {
        super.apply(id);
            try {
                if(market.getPrimaryEntity()!=null){
                    SectorEntityToken entity = market.getPrimaryEntity();
                    if(!entity.hasScriptOfClass(DerelictEnforcerFleetManager.class)){
                        entity.addScript(new DerelictEnforcerFleetManager(entity,1f,2,2,3));
                    }
                }
            }
            catch (Throwable throwable){

            }
        market.getUpkeepMult().modifyMult("aotd_ancient_def",1.4f,"Ancient Defences");

    }

    @Override
    public void unapply(String id) {
        super.unapply(id);
        market.getUpkeepMult().unmodifyMult("aotd_ancient_def");
    }

}
