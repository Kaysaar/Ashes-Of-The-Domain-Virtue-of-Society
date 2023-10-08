package data.colonyevents.listeners;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.listeners.BaseIndustryOptionProvider;
import com.fs.starfarer.api.campaign.listeners.DialogCreatorUI;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.ui.AoTDColonyEventOutomeDP;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AoTDColonyEventUIListener extends BaseIndustryOptionProvider {
   public static Object EVENT_AOTD = new Object();
   public AoTDColonyEventManager manager = AoTDColonyEventManager.getInstance();
    public List<IndustryOptionData> getIndustryOptions(Industry ind) {
        ArrayList<IndustryOptionData> list = new ArrayList<>();
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
    }

}
