package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.Misc;
import data.Ids.AodCryosleeperConditions;
import data.Ids.AodMemFlags;
import data.Ids.AodRuleIds;

import java.util.List;
import java.util.Map;

public class kaysaarHarvestingOp extends BaseCommandPlugin {
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        if (dialog == null) return false;
        if (ruleId.equals(AodRuleIds.CRYOHARVESTER_CONFIRM)) {
            FleetMemberAPI cryosleeper = null;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (member.getHullId().equals("ca_cryosleeper")) {
                    cryosleeper = member;
                    break;
                }
            }
            MarketAPI setMarket = dialog.getInteractionTarget().getMarket();
            if (setMarket.hasCondition(AodCryosleeperConditions.ORGAN_HARVESTING_OPERATIONS)) {
                return false;
            }
            if (cryosleeper == null) {
                return false;
            }
            if (cryosleeper.getVariant().getHullVariantId().equals(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL)) {
                return false;
            }

            AddRemoveCommodity.addFleetMemberLossText(cryosleeper, dialog.getTextPanel());
            Global.getSector().getPlayerFleet().getFleetData().removeFleetMember(cryosleeper);
            setMarket.addCondition(AodCryosleeperConditions.ORGAN_HARVESTING_OPERATIONS);
            return true;
        }
        return false;
    }
}
