package com.fs.starfarer.api.impl.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ListenerUtil;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.intel.ProjectCheronIntel;
import com.fs.starfarer.api.impl.campaign.procgen.SalvageEntityGenDataSpec;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.SalvageEntity;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial;
import com.fs.starfarer.api.util.Misc;

import java.util.*;

public class ProjectCharonGenLoot extends BaseCommandPlugin{
    protected CampaignFleetAPI playerFleet;
    protected SectorEntityToken entity;
    protected PlanetAPI planet;
    protected FactionAPI playerFaction;
    protected FactionAPI entityFaction;
    protected TextPanelAPI text;
    protected OptionPanelAPI options;
    protected CargoAPI playerCargo;
    protected MemoryAPI memory;
    protected MarketAPI market;
    protected InteractionDialogAPI dialog;
    protected Map<String, MemoryAPI> memoryMap;
    protected FactionAPI faction;
    public ProjectCharonGenLoot() {
    }

    public ProjectCharonGenLoot(SectorEntityToken entity) {
        init(entity);
    }
    protected void init(SectorEntityToken entity) {
        memory = entity.getMemoryWithoutUpdate();
        memory = entity.getMemoryWithoutUpdate();
        this.entity = entity;
        planet = (PlanetAPI) entity;
        playerFleet = Global.getSector().getPlayerFleet();
        playerCargo = playerFleet.getCargo();

        playerFaction = Global.getSector().getPlayerFaction();
        entityFaction = entity.getFaction();

        faction = entity.getFaction();

        market = entity.getMarket();


    }
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {

        this.dialog = dialog;
        this.memoryMap = memoryMap;

        String command = params.get(0).getString(memoryMap);
        if (command == null) return false;

        entity = dialog.getInteractionTarget();
        init(entity);

        memory = getEntityMemory(memoryMap);

        text = dialog.getTextPanel();
        options = dialog.getOptionPanel();

        if (command.equals("genLoot")) {
            genLoot();
        }

        return true;
    }

    protected void genLoot() {
        OptionPanelAPI options = dialog.getOptionPanel();
        TextPanelAPI text = dialog.getTextPanel();

        MemoryAPI memory = planet.getMemoryWithoutUpdate();
        long seed = memory.getLong(MemFlags.SALVAGE_SEED);
        Random random = Misc.getRandom(seed, 100);

        SalvageEntityGenDataSpec.DropData d = new SalvageEntityGenDataSpec.DropData();
        d.chances = 4;
        d.group = "blueprints";
        planet.addDropRandom(d);

        d = new SalvageEntityGenDataSpec.DropData();
        d.chances = 2;
        d.group = "rare_tech";
        planet.addDropRandom(d);
        CargoAPI salvage = SalvageEntity.generateSalvage(random, 1f, 1f, 1f, 1f, planet.getDropValue(), planet.getDropRandom());
        CargoAPI extra = BaseSalvageSpecial.getCombinedExtraSalvage(memoryMap);
        salvage.addAll(extra);
        BaseSalvageSpecial.clearExtraSalvage(memoryMap);
        if (!extra.isEmpty()) {
            ListenerUtil.reportExtraSalvageShown(planet);
        }
        salvage.addCommodity("theta_core",1);
        salvage.addCommodity("alpha_core",3);
        salvage.addCommodity("beta_core",5);
        salvage.addCommodity("research_databank",2);
        salvage.addSpecial(new SpecialItemData("aotd_vok_databank_decayed","cheron"),1);
        salvage.sort();
        entity.getMemory().unset("$aotd_quest_theta");
        dialog.getVisualPanel().showLoot("Salvaged", salvage, false, true, true, new CoreInteractionListener() {
            public void coreUIDismissed() {
                dialog.dismiss();
                dialog.hideTextPanel();
                dialog.hideVisualPanel();

            }
        });
        options.clearOptions();
        Global.getSector().getMemory().set("$aotd_cheron",true);
        dialog.setPromptText("");
        IntelInfoPlugin intel = Global.getSector().getIntelManager().getFirstIntel(ProjectCheronIntel.class);
        if( intel!=null){
            Global.getSector().getIntelManager().removeIntel(intel);
            Misc.makeUnimportant(planet,"Cheron");
        }
    }
}
