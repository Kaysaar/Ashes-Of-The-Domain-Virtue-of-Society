package data.colonyevents.ui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.models.AoTDColonyEvent;
import data.colonyevents.manager.AoTDColonyEventManager;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AoTDColonyEventOutcomeUI implements CustomUIPanelPlugin {
    public static final float WIDTH = 900;
    public static final float HEIGHT = 700;
    PositionAPI pos;
    float oppacity = 0.0f;
    Color bgColor = new Color(6, 35, 40, 42);
    Color uiColor = Global.getSector().getPlayerFaction().getDarkUIColor();
    public static final Logger log = Global.getLogger(AoTDColonyEventOutcomeUI.class);
    public static final float ENTRY_HEIGHT = 40;
    public static final float ENTRY_WIDTH = WIDTH - 7f;
    public static final float CONTENT_HEIGHT = 80;
    protected InteractionDialogAPI dialog;
    protected CustomVisualDialogDelegate.DialogCallbacks callbacks;
    protected CustomPanelAPI panel;
    protected CustomPanelAPI mainPanel;
    public AoTDColonyEvent eventToSolve = AoTDColonyEventManager.getInstance().getOnGoingEvent();
    public String selected = null;
    public List<ButtonAPI> buttons = new ArrayList<>();

    TooltipMakerAPI optionToolTipSaved = null;
    TooltipMakerAPI optionDescrpSaved = null;
    TooltipMakerAPI eventDescriptionSaved = null;
    boolean madeDecision = false;
    float scroller = 0f;
    float scrollerOfEventText = 0f;
    float scrolerOfOutcome = 0f;
    private TooltipMakerAPI descriptionTooltip;
    private TooltipMakerAPI headerTooltip;
    private CustomPanelAPI anchorForImage;
    private CustomPanelAPI panelWithDescription;


    @Override
    public void positionChanged(PositionAPI position) {
        this.pos = position;
    }

    @Override
    public void renderBelow(float alphaMult) {
        if (bgColor == null) return;

        float x = pos.getX();
        float y = pos.getY();
        float w = pos.getWidth();
        float h = pos.getHeight();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(bgColor.getRed() / 255f, bgColor.getGreen() / 255f, bgColor.getBlue() / 255f,
                bgColor.getAlpha() / 255f * alphaMult * 0.01f);
        GL11.glRectf(x, y, x + w, y + h);
        GL11.glColor4f(1, 1, 1, 1);
        drawRectangleFilledForTooltip(headerTooltip,alphaMult);
        GL11.glPopMatrix();
    }

    public void drawRectangleFilledForTooltip(TooltipMakerAPI tooltipMakerAPI,float alphaMult) {
        if (uiColor == null) return;

        float x = tooltipMakerAPI.getPosition().getX();
        float y = tooltipMakerAPI.getPosition().getY();
        float w = tooltipMakerAPI.getPosition().getWidth();
        float h = tooltipMakerAPI.getPosition().getHeight();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(uiColor.getRed() / 255f, uiColor.getGreen() / 255f, uiColor.getBlue() / 255f,
                uiColor.getAlpha() / 255f * alphaMult* 23f);
        GL11.glRectf(x, y, x + w, y + h);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
    void drawPanelBorder(CustomPanelAPI p) {
        GL11.glBegin(GL11.GL_LINE_LOOP);
        float x = p.getPosition().getX() - 5;
        float y = p.getPosition().getY();
        float w = p.getPosition().getWidth() + 10;
        float h = p.getPosition().getHeight();
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();
    }


    @Override
    public void render(float alphaMult) {
        GL11.glPushMatrix();
        //GL11.glScalef(1/uiscale, 1/uiscale, 1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Color color = Misc.getDarkPlayerColor();
        Color colorResearched = Misc.getBrightPlayerColor();
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alphaMult);
        if(anchorForImage!=null){
            drawPanelBorder(anchorForImage);
        }
        if(panelWithDescription !=null){
            drawPanelBorder(panelWithDescription);
        }
    }

    @Override
    public void advance(float amount) {
        oppacity += amount;


    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        //this works for keyboard events, but does not seem to capture other UI events like button presses, thus why we use advance()
//        for (InputEventAPI event : events) {
//            if (event.isConsumed()) continue;
//            //is ESC is pressed, close the custom UI panel and the blank IDP we used to create it
//            if (event.isKeyDownEvent() && event.getEventValue() == Keyboard.KEY_ESCAPE ) {
//                event.consume();
//                callbacks.dismissDialog();
//                dialog.dismiss();
//
//                return;
//
//            }
//        }
    }

    @Override
    public void buttonPressed(Object buttonId) {

    }

    public void init(CustomPanelAPI panel, CustomVisualDialogDelegate.DialogCallbacks callbacks, InteractionDialogAPI dialog) {
        this.callbacks = callbacks;
        this.panel = panel;
        this.dialog = dialog;

        reset();


    }

    public void setEvent() {

    }

    public void reset() {
        if (mainPanel != null) {
            panel.removeComponent(mainPanel);
            buttons.clear();
        }
        mainPanel = this.panel.createCustomPanel(WIDTH, HEIGHT, null);
        createUIForDecision(panel);
        dialog.setOpacity(0.95f);

    }

    public void createUIForDecision(CustomPanelAPI panel) {

        panelWithDescription = mainPanel.createCustomPanel(WIDTH-515, 275,null);

        descriptionTooltip = panelWithDescription.createUIElement(WIDTH-515, 275, true);
        headerTooltip = mainPanel.createUIElement(WIDTH, HEIGHT * 0.08f, true);

        headerTooltip.setParaFont(Fonts.ORBITRON_24AABOLD);
        LabelAPI labelAPI = headerTooltip.addPara("Event : " + eventToSolve.getSpec().getName(),Misc.getBrightPlayerColor(),10f);

        buttons.clear();
        descriptionTooltip.setParaFont(Fonts.ORBITRON_12);

        eventToSolve.generateDescriptionOfEvent(descriptionTooltip);
        Color color = Global.getSector().getPlayerFaction().getBaseUIColor();
        Color dark = Global.getSector().getPlayerFaction().getDarkUIColor();
        float opad = 10f;
        float spad = 2f;
        Color baseColor = Misc.getButtonTextColor();
        Color bgColour = Misc.getDarkPlayerColor();
        Color brightColor = Misc.getBrightPlayerColor();
        ;
        TooltipMakerAPI panelTooltip = mainPanel.createUIElement(WIDTH, HEIGHT * 0.30f, true);
        anchorForImage = mainPanel.createCustomPanel(490,275,null);
        TooltipMakerAPI anch = anchorForImage.createUIElement(500,275,false);
        anch.addImage(Global.getSettings().getSpriteName("aotd_colony_event",eventToSolve.getSpec().getImage()),10f);
        anchorForImage.addUIElement(anch).inTL(-10,-10);

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
            anchor.setParaFont(Fonts.ORBITRON_12);
            anchor.addPara(optionEntry.getValue(), Color.ORANGE, 10f);
            optionButtonPanel.addUIElement(anchor).inTL(2, 2);
            panelTooltip.addCustom(optionButtonPanel, 0f);
            panelTooltip.addSpacer(15);
            buttons.add(areaCheckbox);
        }


        optionToolTipSaved = panelTooltip;
        eventDescriptionSaved = descriptionTooltip;
        panelWithDescription.addUIElement(descriptionTooltip).inTL(0,0);
        this.mainPanel.addUIElement(headerTooltip).inTL(0, 0);
        this.mainPanel.addComponent(panelWithDescription).inTL(504, HEIGHT * 0.07f);
        this.mainPanel.addUIElement(panelTooltip).inTL(0, HEIGHT * 0.52f);
        this.mainPanel.addComponent(anchorForImage).inTL(5,HEIGHT*0.07f);
        if (selected != null) {
            TooltipMakerAPI panelWithOutcomes = this.mainPanel.createUIElement(WIDTH, HEIGHT * 0.14f, true);
            eventToSolve.showOptionOutcomes(panelWithOutcomes, selected);
            this.mainPanel.addUIElement(panelWithOutcomes).inTL(5, HEIGHT * 0.82f);
            optionDescrpSaved = panelWithOutcomes;
        }
        float width = labelAPI.computeTextWidth("Event : " + eventToSolve.getSpec().getName());

        labelAPI.getPosition().setLocation(0,0).inTL(headerTooltip.getPosition().getCenterX()-(width/2),5);
        panel.addComponent(mainPanel).inTL(13F, 13F);
        panelTooltip.getExternalScroller().setYOffset(scroller);
        descriptionTooltip.getExternalScroller().setYOffset(scrollerOfEventText);
        if (optionDescrpSaved != null) {
            optionDescrpSaved.getExternalScroller().setYOffset(scrolerOfOutcome);
        }
    }

    public void reportButtonPressed(Object id) {
        String prev = selected;
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
        if(prev!=null&&prev.equals(selected)){
            eventToSolve.executeDecision(selected);
            eventToSolve.isWaitingForDecision = false;
            eventToSolve.cooldownBeforeEventCanOccur = eventToSolve.getSpec().getBaseCooldown();
            AoTDColonyEventManager.getInstance().updateEvent(eventToSolve);
            AoTDColonyEventManager.getInstance().onGoingEvent = null;
            if(AoTDColonyEventManager.getInstance().lastEvent>=45){
                AoTDColonyEventManager.getInstance().lastEvent/=2;
            }
            callbacks.dismissDialog();
            dialog.dismiss();
        }
        reset();

    }

    public static class ButtonReportingCustomPanel extends BaseCustomUIPanelPlugin {
        public AoTDColonyEventOutcomeUI delegate;

        public ButtonReportingCustomPanel(AoTDColonyEventOutcomeUI delegate) {
            this.delegate = delegate;
        }

        @Override
        public void buttonPressed(Object buttonId) {
            super.buttonPressed(buttonId);
            delegate.reportButtonPressed(buttonId);
        }
    }
}
