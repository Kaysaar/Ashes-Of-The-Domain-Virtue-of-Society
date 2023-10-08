package data;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomDialogDelegate;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.listeners.DialogCreatorUI;
import data.colonyevents.AoTDColonyEvent;
import data.colonyevents.AoTDColonyEventOutcomeUI;
import data.colonyevents.AoTDColonyEventOutomeDP;
import data.colonyevents.AoTdColonyEventUI;
import org.lazywizard.lazylib.MathUtils;

public class AoTDColonyEventUiEnforcer implements EveryFrameScript {
    DialogCreatorUI dialogCreatorUI;
    float frames =0;
    public AoTDColonyEventUiEnforcer( AoTDColonyEvent event,DialogCreatorUI creatorUI){
        eventToSolve = event;
        dialogCreatorUI = creatorUI;
    }
    boolean accomplished = false;
    AoTDColonyEvent eventToSolve;
    @Override
    public boolean isDone() {
        return accomplished;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        frames++;
        for (PlanetAPI planet : Global.getSector().getStarSystem("SystemWithPlanetsToRename").getPlanets()) {
            if(planet.getId().equals("wantedId")){
                planet.setName("Renamed Planet");
            }

        }
        MathUtils.clamp(frames, 0, 15);
        if (frames < 6) return;
        if (!Global.getSector().isPaused())
        {
            return;
        }
        AoTDColonyEventOutomeDP dp = new AoTDColonyEventOutomeDP();
        dp.setEvent(eventToSolve);
        boolean did = true;
        if(!accomplished){
         dialogCreatorUI.showDialog(AoTdColonyEventUI.WIDTH,AoTdColonyEventUI.HEIGHT,new AoTdColonyEventUI(null));
        }
        accomplished = did;
        Global.getSector().removeScriptsOfClass(this.getClass());


    }
}
