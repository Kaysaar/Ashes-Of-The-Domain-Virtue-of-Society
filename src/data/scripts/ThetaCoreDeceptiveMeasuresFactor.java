package data.scripts;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.intel.events.BaseEventFactor;
import com.fs.starfarer.api.impl.campaign.intel.events.BaseEventIntel;
import com.fs.starfarer.api.impl.campaign.intel.events.BaseFactorTooltip;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.scripts.industry.DolosNetwork;

import java.awt.*;

public class ThetaCoreDeceptiveMeasuresFactor extends BaseEventFactor {
    public static int DOLOS_NETWORK_POINTS = 5;

    public ThetaCoreDeceptiveMeasuresFactor() {
    }

    @Override
    public TooltipMakerAPI.TooltipCreator getMainRowTooltip() {
        return new BaseFactorTooltip() {
            public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
                Color h = Misc.getHighlightColor();
                float opad = 10f;

                tooltip.addPara("Network maintained by Theta Cores",Color.CYAN, 0f);
            }
        };
    }

    public boolean isValidToHaveBonus() {
        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {
            if (playerMarket.hasIndustry("dolos_network")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldShow(BaseEventIntel intel) {
        return isValidToHaveBonus();
    }

    @Override
    public void addExtraRows(TooltipMakerAPI info, BaseEventIntel intel) {
        Color tc = Misc.getTextColor();

        if (isValidToHaveBonus()) {
            int p = getDolosNetworkScore();
            if (p != 0) {
                info.addRowWithGlow(Alignment.LMID, tc, "    Dolos Network",
                        Alignment.RMID, intel.getProgressColor(getDolosNetworkScore()), "" + getDolosNetworkScore());
                TooltipMakerAPI.TooltipCreator t = new BaseFactorTooltip() {
                    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
                        float opad = 10f;
                        tooltip.addPara("Based on the amount of functional Dolos Network Structures under your control.", 0f);

                        tooltip.addPara("One Functional Dolos Network Center reduces progress by %s points.", opad, Misc.getHighlightColor(),
                                "" + DOLOS_NETWORK_POINTS);

                    }
                };
                info.addTooltipToAddedRow(t, TooltipMakerAPI.TooltipLocation.RIGHT, false);
            }
        }



//		int def = getIncreasedDefensesScore(intel);
//		if (def != 0) {
//			info.addRowWithGlow(Alignment.LMID, tc, "    Increased defenses",
//				    Alignment.RMID, intel.getProgressColor(def), "" + def);
//
//			t = new BaseFactorTooltip() {
//				public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
//					tooltip.addPara("Event progress reduced further by %s the other defense factors.", 0f,
//							Misc.getHighlightColor(), "" + (int)INCREASED_DEFENSES_MULT + Strings.X);
//				}
//			};
//			info.addTooltipToAddedRow(t, TooltipLocation.RIGHT, false);
//		}
    }

    //	public int getProgress(BaseEventIntel intel) {
//		return getProgress(intel, true);
//	}
    public int getProgress(BaseEventIntel intel) {
        return getDolosNetworkScore();
    }

//	public int getIncreasedDefensesScore(BaseEventIntel intel) {
//		if (intel.isStageOrOneOffEventReached(Stage.INCREASED_DEFENSES)) {
//			return (int) Math.round(getProgress(intel, false) * INCREASED_DEFENSES_MULT);
//		}
//		return 0;
//	}

    public int getDolosNetworkScore() {
        int network = 0;
        for (MarketAPI market : Misc.getPlayerMarkets(false)) {
            if (market.hasIndustry("dolos_network")) {
                DolosNetwork net = (DolosNetwork) market.getIndustry("dolos_network");
                if (net.hasAiCore && !net.hasDeficit) {
                    network += 5;
                }
            }
        }
        return -1*network;
    }

    @Override
    public String getProgressStr(BaseEventIntel intel) {
        return "";
    }

    public String getDesc(BaseEventIntel intel) {
        return "Dolos Network";
    }

    @Override
    public Color getDescColor(BaseEventIntel intel) {
        if (getProgress(intel) == 0) {
            return Misc.getGrayColor();
        }
        return super.getDescColor(intel);
    }


}
