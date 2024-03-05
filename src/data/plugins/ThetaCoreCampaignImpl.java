package data.plugins;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;
import data.campaign.econ.plugins.ThetaCoreAdmin;

public class ThetaCoreCampaignImpl extends BaseCampaignPlugin {
    @Override
    public PluginPick<AICoreAdminPlugin> pickAICoreAdminPlugin(String commodityId) {
        if(commodityId.equals("theta_core")){
            return new PluginPick<AICoreAdminPlugin>(new ThetaCoreAdmin(),PickPriority.MOD_SET);
        }

        return null;
    }

    @Override
    public String getId() {
        return "AoTDVosCampaignPluginImpl";
    }
}
