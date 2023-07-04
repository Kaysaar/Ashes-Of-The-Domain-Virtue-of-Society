package data.scripts.industry;

import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.*;
import data.Ids.AodCryosleeperConditions;
import org.magiclib.util.MagicSettings;

public class KaysaarCryoHarvester extends BaseIndustry {

    public int BONUS = MagicSettings.getInteger("Cryo_but_better", "ORGAN_HARVESTING_RATE");

    @Override
    public void apply() {

        super.apply(true);
        int size = market.getSize();
        demand(Commodities.MARINES, 3 + size - 2);
        demand(Commodities.ORGANICS, 4 + size - 2);

        if (!market.isIllegal(Commodities.ORGANS)) {
            supply(Commodities.ORGANS, size + BONUS);
        } else {
            supply(Commodities.ORGANS, 0);
        }
        if (!isFunctional()) {
            supply.clear();
            unapply();
        }
    }

    @Override
    public void unapply() {
        super.unapply();

    }

    @Override
    protected boolean canImproveToIncreaseProduction() {
        return true;
    }

    @Override
    public boolean isAvailableToBuild() {
        return market.hasCondition(AodCryosleeperConditions.ORGAN_HARVESTING_OPERATIONS);
    }

    @Override
    public boolean showWhenUnavailable() {
        return false;
    }

}
