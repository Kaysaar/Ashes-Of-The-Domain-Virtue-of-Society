package data.colonyevents.listeners;

import com.fs.starfarer.api.campaign.CustomDialogDelegate;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.listeners.BaseIndustryOptionProvider;
import com.fs.starfarer.api.campaign.listeners.DialogCreatorUI;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.ui.AoTDColonyEventOutomeDP;
import data.colonyevents.ui.MikoshiUI;
import data.scripts.industry.MikoshiFacility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AoTDColonyEventUIListener extends BaseIndustryOptionProvider {
   public static Object EVENT_AOTD = new Object();
    public static Object MIKOSHI = new Object();
   public AoTDColonyEventManager manager = AoTDColonyEventManager.getInstance();
    public List<IndustryOptionData> getIndustryOptions(Industry ind) {
        ArrayList<IndustryOptionData> list = new ArrayList<>();
        if (ind.getId().equals("cheron")) {
            IndustryOptionData data = new IndustryOptionData("Soulkiller", MIKOSHI, ind, this);
            data.color = Color.CYAN;
            MikoshiFacility mikoshi = (MikoshiFacility)ind;
            if(mikoshi.conversionStarted||!mikoshi.hasMetDemand){
                data.enabled=false;
            }
            list.add(data);
            return list;
        }
        if (manager.onGoingEvent == null || ind.getMarket().getId() != manager.currentMarketWithEvent.getId()) return null;
        if (ind.getId().equals(Industries.POPULATION)&&ind.getMarket().getId().equals(manager.currentMarketWithEvent.getId())) {
            IndustryOptionData data = new IndustryOptionData("Resolve Event", EVENT_AOTD, ind, this);
            data.color = Color.CYAN;
            list.add(data);
            return list;
        }

        return null;
    }
    @Override
    public void optionSelected(IndustryOptionData opt, DialogCreatorUI ui) {

        if( opt.id == EVENT_AOTD){
          ui.showDialog(null,new AoTDColonyEventOutomeDP());
        }
        if(opt.id==MIKOSHI){
            CustomDialogDelegate delegate = new MikoshiUI((MikoshiFacility) opt.ind.getMarket().getIndustry("cheron"));
            ui.showDialog(MikoshiUI.WIDTH, MikoshiUI.HEIGHT, delegate);
        }
    }

    @Override
    public void createTooltip(IndustryOptionData opt, TooltipMakerAPI tooltip, float width) {
        super.createTooltip(opt, tooltip, width);
        if(opt.id==MIKOSHI){
            tooltip.addPara("Soulkiller is advanced procedure, that copies consciousness of specific person into advanced AI core. Due to being very demanding task for person's brain it gots fried once transfer is completed, therefore killing this person.", Misc.getTooltipTitleAndLightHighlightColor(),10f);
            tooltip.addPara("Warning! This procedure is irreversible once started!",Misc.getNegativeHighlightColor(),10f);
        }
    }
}
