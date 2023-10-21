package data.colonyevents.ui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomDialogDelegate;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.characters.AdminData;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.conditions.MikoshiUsageApplier;
import data.scripts.industry.MikoshiFacility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MikoshiUI implements CustomDialogDelegate {
    public static final float WIDTH = 400;
    public static final float HEIGHT = 230;
    public static final float ENTRY_HEIGHT = 145; //MUST be even
    public static final float ENTRY_WIDTH = WIDTH - 5f; //MUST be even
    public static final float CONTENT_HEIGHT = 130;
    public AdminData selected = null;
    public List<ButtonAPI> buttons = new ArrayList<>();
    public MikoshiFacility mikoshi;
    public MikoshiUI(MikoshiFacility mikoshiFacility){
        mikoshi=mikoshiFacility;
    }
    @Override
    public void createCustomDialog(CustomPanelAPI panel, CustomDialogCallback callback) {
        TooltipMakerAPI panelTooltip = panel.createUIElement(WIDTH, HEIGHT, true);
        panelTooltip.addSectionHeading("Select Admin to go through Soulkiller", Alignment.MID, 0f);
        float opad = 10f;
        float ySpacer = 20f;
        int index =0;
        for (AdminData admin : Global.getSector().getCharacterData().getAdmins()) {
            final PersonAPI personAPI = admin.getPerson();
            Color baseColor = Misc.getButtonTextColor();
            Color bgColour = Misc.getDarkPlayerColor();
            Color brightColor = Misc.getBrightPlayerColor();
            String spriteName = admin.getPerson().getPortraitSprite();
            SpriteAPI sprite = Global.getSettings().getSprite(spriteName);
            float aspectRatio = sprite.getWidth() / sprite.getHeight();
            float adjustedWidth = CONTENT_HEIGHT * aspectRatio;
            CustomPanelAPI researcherInfo = panel.createCustomPanel(ENTRY_WIDTH, ENTRY_HEIGHT + 2f, new ButtonReportingCustomPanel(this));
            TooltipMakerAPI anchor =researcherInfo.createUIElement(ENTRY_WIDTH - adjustedWidth - (3 * opad), CONTENT_HEIGHT, false);

            ButtonAPI areaCheckbox = anchor.addAreaCheckbox("",admin.getPerson().getId(), baseColor, bgColour, brightColor, //new Color(255,255,255,0)
                    ENTRY_WIDTH,
                    CONTENT_HEIGHT + 20,
                    0f,
                    true);
            researcherInfo.addUIElement(anchor).inTL(-10,5+ySpacer*index);
            anchor = researcherInfo.createUIElement(128,128,false);
            anchor.addImage(spriteName, 128, 128, 0f);
            researcherInfo.addUIElement(anchor).inTL(0,13+ySpacer*index);
            anchor = researcherInfo.createUIElement(ENTRY_WIDTH - adjustedWidth - (6 * opad), CONTENT_HEIGHT, false);
            anchor.setParaFont(Fonts.ORBITRON_16);
            anchor.addPara("Name: "+admin.getPerson().getName().getFirst()+ " " +admin.getPerson().getName().getLast(), Color.ORANGE,10f);
            buttons.add(areaCheckbox);
            researcherInfo.addUIElement(anchor).inTL(135,5+ySpacer*index);
            panelTooltip.addCustom(researcherInfo,0f);
            index++;

        }
        panel.addUIElement(panelTooltip).inTL(1,1);
    }

    @Override
    public boolean hasCancelButton() {
        return false;
    }

    @Override
    public String getConfirmText() {
        return null;
    }

    @Override
    public String getCancelText() {
        return null;
    }

    @Override
    public void customDialogConfirm() {
        if(selected!=null){
            int i =0;
            for (AdminData admin : Global.getSector().getCharacterData().getAdmins()) {
                if(admin.getPerson().getId().equals(selected.getPerson().getId())){
                    if(admin.getMarket()!=null){
                        admin.getMarket().setAdmin(null);
                    }
                    admin.setMarket(null);
                    Global.getSector().getCharacterData().getAdmins().remove(i);
                    break;
                }
                i++;
            }
           mikoshi.conversionStarted=true;
            for (MarketAPI playerMarket : Misc.getPlayerMarkets(true)) {
                int mult=2;
                for (MarketConditionAPI condition : playerMarket.getConditions()) {
                    if(condition.getSpec().getId().equals("mikoshi_consequence")){
                        mult*=2;
                    }
                }
                String token =  playerMarket.addCondition("mikoshi_consequence");
                MikoshiUsageApplier applier = (MikoshiUsageApplier) playerMarket.getSpecificCondition(token).getPlugin();
                applier.GlobalPenalty = mult;
            }
        }
    }

    @Override
    public void customDialogCancel() {

    }

    @Override
    public CustomUIPanelPlugin getCustomPanelPlugin() {
     return null;
    }
    public void reportButtonPressed(Object id) {
        if (id instanceof String) {
            for (AdminData adminData :Global.getSector().getCharacterData().getAdmins()) {
                if(adminData.getPerson().getId().equals(id)){
                    selected = adminData;
                    break;
                }
            }

        }
        for (ButtonAPI button : buttons) {
            if (button.isChecked() && button.getCustomData() != id) button.setChecked(false);
        }
    }
    public static class ButtonReportingCustomPanel extends BaseCustomUIPanelPlugin {
        public MikoshiUI delegate;

        public ButtonReportingCustomPanel(MikoshiUI delegate) {
            this.delegate = delegate;
        }

        @Override
        public void buttonPressed(Object buttonId) {
            super.buttonPressed(buttonId);
            delegate.reportButtonPressed(buttonId);
        }
    }
}
