package com.fs.starfarer.api.impl.campaign.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.Set;

public class ProjectCheronIntel extends BaseIntelPlugin{
    protected PlanetAPI target;
    public ProjectCheronIntel(PlanetAPI planetAPI){
        this.target = planetAPI;
        Misc.makeImportant(target,"Cheron");
    }
    @Override
    public boolean isImportant() {
        return true;
    }
    @Override
    public boolean shouldRemoveIntel() {
        return Global.getSector().getMemory().is("$aotd_cheron_done",true);
        //return false;
    }
    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
        Color h = Misc.getHighlightColor();
        Color g = Misc.getGrayColor();
        Color tc = Misc.getTextColor();
        float pad = 3f;
        float opad = 10f;

        info.addPara("Data presented on Tri-pad presents location of   " + target.getName() + " located in " + target.getStarSystem().getName() + " Star system", 10f);
        addBulletPoints(info, ListInfoMode.IN_DESC);
    }
    public String getSortString() {
        return "Project Cheron";
    }
    public String getName() {
        if (isEnded() || isEnding()) {
            return "Project Cheron - Finished";
        }
        return "Project Cheron";
    }
    public String getIcon() {
        return Global.getSettings().getSpriteName("intel", "red_planet");
    }
    @Override
    public void createLargeDescription(CustomPanelAPI panel, float width, float height) {
        super.createLargeDescription(panel, width, height);
    }
    @Override
    public FactionAPI getFactionForUIColors() {
        return super.getFactionForUIColors();
    }

    public String getSmallDescriptionTitle() {
        return getName();
    }

    @Override
    public SectorEntityToken getMapLocation(SectorMapAPI map) {
        return target;

    }
    @Override
    public String getCommMessageSound() {
        return "ui_discovered_entity";
    }
    @Override
    public void createIntelInfo(TooltipMakerAPI info, ListInfoMode mode) {
        Color c = getTitleColor(mode);
        info.addPara(getName(), c, 0f);
        addBulletPoints(info, mode);
    }

    @Override
    protected void notifyEnded() {
        super.notifyEnded();
        Global.getSector().removeScript(this);


    }
    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = super.getIntelTags(map);
        tags.add(Tags.INTEL_MISSIONS);
        tags.add(Tags.INTEL_ACCEPTED);
        tags.add(Tags.INTEL_EXPLORATION);
        return tags;
    }
    protected void addBulletPoints(TooltipMakerAPI info, ListInfoMode mode) {

        Color h = Misc.getHighlightColor();
        Color g = Misc.getGrayColor();
        float pad = 3f;
        float opad = 10f;

        float initPad = pad;
        if (mode == ListInfoMode.IN_DESC) initPad = opad;

        Color tc = getBulletColorForMode(mode);

        bullet(info);
        boolean isUpdate = getListInfoParam() != null;

        initPad = 0f;

        unindent(info);
    }
}
