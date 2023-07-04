package data.scripts;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Misc;
import data.plugins.AodCryosleeperPLugin;

public class CryoAdditionalListener implements EveryFrameScript {
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        if(!Global.getSector().isPaused()&&  Misc.MAX_COLONY_SIZE != AodCryosleeperPLugin.configSize){
            Misc.MAX_COLONY_SIZE =  AodCryosleeperPLugin.configSize;
            CryoShowGrowthListener.Companion.setRsetAll(false);
            Global.getSector().getEconomy().tripleStep();
        }

    }
}
