package data.skills;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.intel.events.HostileActivityEventIntel;
import com.fs.starfarer.api.impl.campaign.intel.events.HostileActivityManager;
import com.fs.starfarer.api.impl.campaign.skills.Hypercognition;

public class FourthDimensionalSight {
    public static float ACCESS = 0.1f;
    public static float FLEET_SIZE = 20f;
    public static int DEFEND_BONUS = 50;
    public static float STABILITY_BONUS = 1;
    //WIP - Introudce more benefits  to Theta Core
    public static class Level1 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
        }

        public void unapply(MarketAPI market, String id) {
            market.getAccessibilityMod().unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "Enables building Dolos Network Center on this market.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

}
