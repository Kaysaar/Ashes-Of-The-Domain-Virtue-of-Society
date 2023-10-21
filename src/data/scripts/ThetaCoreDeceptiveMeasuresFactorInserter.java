package data.scripts;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.intel.events.HostileActivityEventIntel;

public class ThetaCoreDeceptiveMeasuresFactorInserter implements EveryFrameScript {
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        if (!Global.getSector().getMemory().contains("$aotd_inserted_theta")) {
            HostileActivityEventIntel eventIntel = (HostileActivityEventIntel) Global.getSector().getIntelManager().getFirstIntel(HostileActivityEventIntel.class);
            if (eventIntel != null) eventIntel.addFactor(new ThetaCoreDeceptiveMeasuresFactor());
            Global.getSector().getMemory().set("$aotd_inserted_theta", true);
            Global.getSector().removeScriptsOfClass(this.getClass());
        }

    }
}
