package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.util.Misc;

import java.util.List;
import java.util.Map;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import data.Ids.AodMemFlags;
import data.Ids.AodRuleIds;

public class kaysaarRelocateRepairedCryosleeper extends BaseCommandPlugin {


    public int checkMem(SectorEntityToken parckedCryosleeper) {
        if (parckedCryosleeper.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN, true)) {
            return 0;
        }
        if (parckedCryosleeper.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_1, true)) {
            return 1;
        }
        if (parckedCryosleeper.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2, true)) {
            return 2;
        }
        if (parckedCryosleeper.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_3, true)) {
            return 3;
        }
        if (parckedCryosleeper.getMemory().is(AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL, true)) {
            return 4;
        }
        return -1;
    }

    public String setVariant(int checkMemResult) {
        if (checkMemResult == 0) {
            return AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_BEGIN;
        }
        if (checkMemResult == 1) {
            return AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_1;
        }
        if (checkMemResult == 2) {
            return AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_2;
        }
        if (checkMemResult == 3) {
            return AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_3;
        }
        if (checkMemResult == 4) {
            return AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_FINAL;
        }
        return AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_BEGIN;
    }

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        if (dialog == null) return false;
        if (ruleId.equals(AodRuleIds.CRYOSLEEPER_CONFIRM_DECISION)) {
            SectorEntityToken entity = dialog.getInteractionTarget();
            FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, setVariant(checkMem(entity)));
            memoryMap.get(MemKeys.LOCAL).set("$cryosleeper_info", member);
            MarketAPI currMarket = entity.getMarket();
            currMarket.removeCondition("cryo_in_system");
            Misc.fadeAndExpire(entity, 1f);
            return true;
        }
        return false;
    }
}
