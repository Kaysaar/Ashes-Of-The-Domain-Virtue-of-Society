package data.scripts.campaign.econ.conditions;
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.econ.BaseHazardCondition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.Ids.AodMemFlags;

import java.util.List;

public class cryo_in_system extends BaseHazardCondition {
    public static float STABILITY_BONUS = 1;
    @Override
    public void apply(String id) {

        market.getStability().modifyFlat(id,STABILITY_BONUS,"Local Cryosleeper");

    }


    @Override
    public void unapply(String id) {
        market.getStability().unmodify(id);
    }
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);
         List<CustomCampaignEntityAPI> entityList  = market.getContainingLocation().getCustomEntitiesWithTag("parked_cryosleeper");

         if(entityList!=null&&!entityList.isEmpty()){
             SectorEntityToken entity = null;
             for (CustomCampaignEntityAPI orbitingCryos : entityList) {
                 if((orbitingCryos.getMemory().get(AodMemFlags.BIND_TO_PLANET)).toString().equals(this.market.getPlanetEntity().getId())){
                     entity = orbitingCryos;
                     break;
                 }
             }
             if(entity.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN,true)){
                 tooltip.addPara(
                         "%s stability.\nNone of Pods have been opened so far",
                         10f,
                         Misc.getHighlightColor(),
                         "+" + (int)STABILITY_BONUS
                 );
             }
             if(entity.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_1,true)){
                 tooltip.addPara(
                         "%s stability.\nSome pods has been opened and small percenteage have beeen awakened, but most are still slumbering",
                         10f,
                         Misc.getHighlightColor(),
                         "+" + (int)STABILITY_BONUS
                 );
             }
             if(entity.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2,true)){
                 tooltip.addPara(
                         "%s stability.\nHalf of pods have been already opened, but yet another half still remains to be awakened",
                         10f,
                         Misc.getHighlightColor(),
                         "+" + (int)STABILITY_BONUS
                 );
             }
             if(entity.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_3,true)){
                 tooltip.addPara(
                         "%s stability.\nCryosleeper has been mostly emptied, but still there are pods that haven't been opened yet",
                         10f,
                         Misc.getHighlightColor(),
                         "+" + (int)STABILITY_BONUS
                 );
             }
             if(entity.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL,true)){
                 tooltip.addPara(
                         "%s stability.\nCryosleeper has been fully emptied from people. All have been awaken and for now it only serves as a statue in honor of old times",
                         10f,
                         Misc.getHighlightColor(),
                         "+" + (int)STABILITY_BONUS
                 );
             }

         }


}
}
