package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.util.Misc;

import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import data.Ids.AodCryosleeperConditions;
import data.Ids.AodMemFlags;
import data.Ids.AodRuleIds;

public class CA_RepairCryosleeper extends BaseCommandPlugin {

    public String setMem(FleetMemberAPI member) {
        String variant = member.getVariant().getHullVariantId();

        if (variant.matches( AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_BEGIN)) {
            return AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN;
        }
        if (variant.matches( AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_1)) {
            return AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_1;
        }
        if (variant.matches( AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_2)) {
            return AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_2;
        }
        if (variant.matches( AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_3)) {
            return AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_3;
        }
        if (variant.matches( AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_FINAL)) {
            return AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_FINAL;
        }
        return AodMemFlags.CRYOSLEEPER_DEPLETION_STAGE_BEGIN;

    }


    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {

        if (dialog == null) return false;

        if (ruleId.equals(AodRuleIds.CRYOSLEEPER_REPAIRED)) {
            FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, AodMemFlags.CRYOSLEEPER_HULL_VARIANT_STAGE_BEGIN);
            memoryMap.get(MemKeys.LOCAL).set("$cryosleeper_info", member);

            SectorEntityToken entity = dialog.getInteractionTarget();
            Misc.fadeAndExpire(entity, 1f);
            return true;
        } else if (ruleId.equals(AodRuleIds.CRYOSLEEPER_MARKET_CONFIRM)) {
            FleetMemberAPI cryosleeper = null;
            for (FleetMemberAPI member : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (member.getHullId().equals("ca_cryosleeper")) {
                    cryosleeper = member;
                    break;
                }
            }

            if (cryosleeper != null) {
                MarketAPI setMarket = dialog.getInteractionTarget().getMarket();
                if (setMarket != null) {
                    if (!setMarket.hasCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER)) {
                        setMarket.addCondition(AodCryosleeperConditions.ORBITING_CRYOSLEEPER);
                        String cryoName = cryosleeper.getShipName();
                        SectorEntityToken stationEntity = setMarket.getContainingLocation().addCustomEntity((String) null, "Domain-era Cryosleeper : " + cryoName, "parked_cryosleeper", setMarket.getFactionId());
                        Global.getSector().getPlayerFleet().getFleetData().removeFleetMember(cryosleeper);
                        SectorEntityToken primary = setMarket.getPrimaryEntity();
                        float orbitRadius = primary.getRadius() + 150.0F;
                        stationEntity.setCircularOrbitWithSpin(primary, (float) Math.random() * 360.0F, orbitRadius, orbitRadius / 10.0F, 5.0F, 5.0F);
                        setMarket.getConnectedEntities().add(stationEntity);
                        stationEntity.setMarket(setMarket);
                        stationEntity.getMemory().set(setMem(cryosleeper), true);
                        stationEntity.setDiscoverable(false);
                        AddRemoveCommodity.addFleetMemberLossText(cryosleeper, dialog.getTextPanel());
                        if (!setMarket.getMemory().contains("$cryobased")) {
                            setMarket.getMemory().set("$cryobased", true);
                        }
                        stationEntity.getMemory().set(AodMemFlags.BIND_TO_PLANET,setMarket.getPlanetEntity().getId());
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }

        return false;
    }

}