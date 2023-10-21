package data.campaign.econ.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.util.Misc;

public class ThetaCoreAdmin implements AICoreAdminPlugin {
    @Override
    public PersonAPI createPerson(String aiCoreId, String factionId, long seed) {
        PersonAPI personAPI = Misc.getAICoreAdminPlugin(Commodities.ALPHA_CORE).createPerson(Commodities.ALPHA_CORE,factionId,seed);
        personAPI.setAICoreId("theta_core");
        personAPI.getName().setFirst("Theta Core");
        personAPI.getStats().setLevel(7);

        personAPI.getStats().setSkillLevel("fourth_dimensional_sight",1);
        personAPI.setPortraitSprite(Global.getSettings().getSpriteName("portraits","theta_core"));
        return personAPI;
    }
}
