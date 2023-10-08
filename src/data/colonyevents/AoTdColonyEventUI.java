package data.colonyevents;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomDialogDelegate;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.listeners.DialogCreatorUI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.AoTDColonyEventUiEnforcer;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AoTdColonyEventUI implements CustomDialogDelegate {
    public static final float WIDTH = 600;
    public static final float HEIGHT = 300;
    public static final Logger log = Global.getLogger(AoTdColonyEventUI.class);
    public static final float ENTRY_HEIGHT = 40; //MUST be even
    public static final float ENTRY_WIDTH = WIDTH - 2f; //MUST be even
    public static final float CONTENT_HEIGHT = 80;

    public AoTDColonyEvent eventToSolve = AoTDColonyEventManager.getInstance().getOnGoingEvent();
    public String selected = null;
    public List<ButtonAPI> buttons = new ArrayList<>();
    CustomPanelAPI panel = null;
    CustomDialogCallback callback = null;
    CustomPanelAPI insertedPanel = null;
    TooltipMakerAPI optionToolTipSaved = null;
    TooltipMakerAPI optionDescrpSaved = null;
    TooltipMakerAPI eventDescriptionSaved = null;
    float scroller = 0f;
    float scrollerOfEventText = 0f;
    float scrolerOfOutcome = 0f;
    DialogCreatorUI ui= null;

    public AoTdColonyEventUI(DialogCreatorUI UI) {
       this.ui = UI;
    }

    @Override
    public void createCustomDialog(CustomPanelAPI panel, CustomDialogCallback callback) {
        this.insertedPanel = panel.createCustomPanel(WIDTH, HEIGHT, null);

        TooltipMakerAPI optionToolTip = insertedPanel.createUIElement(WIDTH, HEIGHT * 0.17f, true);
        TooltipMakerAPI headerTooltip = insertedPanel.createUIElement(WIDTH, HEIGHT * 0.08f, true);
        headerTooltip.addSectionHeading("Event : " + eventToSolve.getSpec().getName(), Alignment.MID, 0f);
        buttons.clear();
        eventToSolve.generateDescriptionOfEvent(optionToolTip);
        Color color = Global.getSector().getPlayerFaction().getBaseUIColor();
        Color dark = Global.getSector().getPlayerFaction().getDarkUIColor();
        float opad = 10f;
        float spad = 2f;
        Color baseColor = Misc.getButtonTextColor();
        Color bgColour = Misc.getDarkPlayerColor();
        Color brightColor = Misc.getBrightPlayerColor();
        ;
        TooltipMakerAPI panelTooltip = insertedPanel.createUIElement(WIDTH, HEIGHT * 0.60f, true);
        for (Map.Entry<String, String> optionEntry : eventToSolve.getSpec().getOptions().entrySet()) {
            CustomPanelAPI optionButtonPanel = panel.createCustomPanel(ENTRY_WIDTH, ENTRY_HEIGHT, new ButtonReportingCustomPanel(this));
            TooltipMakerAPI anchor = optionButtonPanel.createUIElement(ENTRY_WIDTH, ENTRY_HEIGHT, false);
            ButtonAPI areaCheckbox = anchor.addAreaCheckbox("", optionEntry.getKey(), baseColor, bgColour, brightColor, //new Color(255,255,255,0)
                    ENTRY_WIDTH,
                    ENTRY_HEIGHT,
                    0f,
                    true);
            areaCheckbox.setChecked(selected == optionEntry.getKey());
            optionButtonPanel.addUIElement(anchor).inTL(-opad, 0f);

            anchor = optionButtonPanel.createUIElement(ENTRY_WIDTH, ENTRY_HEIGHT, true);
            anchor.addPara(optionEntry.getValue(), Color.ORANGE, 10f);
            optionButtonPanel.addUIElement(anchor).inTL(2, 2);
            panelTooltip.addCustom(optionButtonPanel, 0f);
            panelTooltip.addSpacer(15);
            buttons.add(areaCheckbox);
        }


        optionToolTipSaved = panelTooltip;
        eventDescriptionSaved = optionToolTip;
        this.insertedPanel.addUIElement(headerTooltip).inTL(0, 0);
        this.insertedPanel.addUIElement(optionToolTip).inTL(0, HEIGHT * 0.06f);
        this.insertedPanel.addUIElement(panelTooltip).inTL(0, HEIGHT * 0.26f);
        if (eventToSolve.canShowOptionOutcomesBeforeDeciding() && selected != null) {
            TooltipMakerAPI panelWithOutcomes = this.insertedPanel.createUIElement(WIDTH, HEIGHT * 0.14f, true);
            eventToSolve.showOptionOutcomes(panelWithOutcomes, selected);
            this.insertedPanel.addUIElement(panelWithOutcomes).inTL(0, HEIGHT * 0.88f);
            optionDescrpSaved = panelWithOutcomes;
        }

        panel.addComponent(insertedPanel).inTL(0.0F, 0.0F);
        this.panel = panel;
        this.callback = callback;
        panelTooltip.getExternalScroller().setYOffset(scroller);
        optionToolTip.getExternalScroller().setYOffset(scrollerOfEventText);
        if (optionDescrpSaved != null) {
            optionDescrpSaved.getExternalScroller().setYOffset(scrolerOfOutcome);
        }

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

        if (selected == null) {

            return;
        }
        eventToSolve.executeDecision(selected);
        eventToSolve.isWaitingForDecision = false;
        eventToSolve.cooldownBeforeEventCanOccur = 1080f;
        AoTDColonyEventManager.getInstance().updateEvent(eventToSolve);
        AoTDColonyEventManager.getInstance().onGoingEvent = null;
        Global.getSector().addScript(new AoTDColonyEventUiEnforcer(eventToSolve,ui));

    }

    @Override
    public void customDialogCancel() {

    }

    @Override
    public CustomUIPanelPlugin getCustomPanelPlugin() {
        return null;
    }

    public void reportButtonPressed(Object id) {
        selected = (String) id;
        log.info("EVENT ID IN UI: " + selected);
        for (ButtonAPI button : buttons) {
            if (button.isChecked() && button.getCustomData() != id) button.setChecked(false);
        }
        scroller = optionToolTipSaved.getExternalScroller().getYOffset();
        scrollerOfEventText = eventDescriptionSaved.getExternalScroller().getYOffset();
        if (optionDescrpSaved != null) {
            scrolerOfOutcome = optionDescrpSaved.getExternalScroller().getYOffset();
        }
        panel.removeComponent(insertedPanel);
        createCustomDialog(panel, callback);
    }

    public static class ButtonReportingCustomPanel extends BaseCustomUIPanelPlugin {
        public AoTdColonyEventUI delegate;

        public ButtonReportingCustomPanel(AoTdColonyEventUI delegate) {
            this.delegate = delegate;
        }

        @Override
        public void buttonPressed(Object buttonId) {
            super.buttonPressed(buttonId);
            delegate.reportButtonPressed(buttonId);
        }
    }
}
