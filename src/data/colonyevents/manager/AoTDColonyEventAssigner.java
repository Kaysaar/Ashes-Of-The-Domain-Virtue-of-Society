package data.colonyevents.manager;

import com.fs.starfarer.api.EveryFrameScript;

public class AoTDColonyEventAssigner implements EveryFrameScript {

    public AoTDColonyEventManager manager  = AoTDColonyEventManager.getInstance();

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
        manager.advance(amount);
    }
}
