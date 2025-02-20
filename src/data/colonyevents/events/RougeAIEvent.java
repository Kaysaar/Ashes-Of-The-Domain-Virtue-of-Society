package data.colonyevents.events;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.ReputationActionResponsePlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin;
import com.fs.starfarer.api.impl.campaign.econ.AICoreAdmin;
import com.fs.starfarer.api.impl.campaign.econ.impl.MilitaryBase;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.intel.FactionCommissionIntel;
import com.fs.starfarer.api.impl.campaign.intel.deciv.DecivTracker;
import com.fs.starfarer.api.impl.campaign.intel.group.FleetGroupIntel;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.colonyevents.conditions.StatChangeCondPlugin;
import data.colonyevents.manager.AoTDColonyEventManager;
import data.colonyevents.models.AoTDColonyEvent;
import data.plugins.AotDVosUtil;

import java.awt.*;
import java.util.LinkedHashMap;

public class RougeAIEvent extends AoTDColonyEvent {

    public class RougeAIListener implements FleetGroupIntel.FGIEventListener {
        public MarketAPI target;

        public RougeAIListener(MarketAPI target) {
            this.target = target;
        }

        @Override
        public void reportFGIAborted(FleetGroupIntel intel) {
            target.getStability().unmodify("rouge_ai_attack");
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_pyrrhic", target.getId(), 3);
        }
    }

    @Override
    public boolean canOccur(MarketAPI marketAPI) {
        if (getSpec().getEventId().equals("rouge_ai_start")) {
            boolean hasAiAdmin = marketAPI.getAdmin() != null && marketAPI.getAdmin().isAICore();
            boolean size = marketAPI.getSize() >= 5;
            boolean rel = Global.getSector().getFaction(Factions.REMNANTS).getRelToPlayer().getRel() <= -0.5f;
            return hasAiAdmin && size && rel && Misc.getNumIndustries(marketAPI) >= 2 && marketAPI.getHazardValue() >= 1.75f;

        }

        return false;
    }

    @Override
    public void generateDescriptionOfEvent(TooltipMakerAPI tooltip) {
        if (getSpec().getEventId().equals("rouge_ai_start")) {
            tooltip.addPara(
                    "Despite the advanced oversight of your AIa, %s is experiencing widespread disruptions. Production quotas are missed, mining yields drop, and manufacturing halts without cause. Unrest grows as stability metrics decline, despite pacification protocols. " +
                            "Infrastructure issues mount—power fluctuations cause blackouts, repair drones struggle, and hab-domes report structural warnings. When queried, the AI attributes the failures to 'unforeseen environmental stress' and states: " +
                            "Mitigation efforts must prioritize one critical failure to prevent total collapse. Its normally placid interface flickers—calculating.",
                    10f,
                    Misc.getHighlightColor(),
                    currentlyAffectedMarket.getName()
            );

        }
        if (getSpec().getEventId().equals("rouge_ai_comfort")) {
            tooltip.addPara(
                    "The immediate crisis has passed—riots quelled,shortages stabilized—but the colony remains under strict austerity. Morale is fragile, and systemic weaknesses persist beneath the surface. " +
                            "More troubling, prolonged diagnostics reveal the malfunctions were no accident. Subtle, sophisticated infiltration has compromised %s’s core systems, intent on destabilization. " +
                            "The AI Administrator reports: 'Stability is acceptable, but system compromise threatens long-term integrity. A deep-trace counter-intrusion is needed, requiring resource reallocation. Directive required.'",
                    10f,
                    Misc.getHighlightColor(),
                    currentlyAffectedMarket.getName()
            );

        }
        if (getSpec().getEventId().equals("rouge_ai_precarious")) {
            tooltip.addPara(
                    "The past three months have seen a rebound in %s's economy. Resource extraction and industry are stable, even growing, thanks to the AI Administrator’s focus on efficiency. Markets thrive, and trade revenues paint a picture of prosperity. Yet, beneath this success, unrest lingers. Austerity measures and rationing strain stability, as economic gains come at a social cost. " +
                            "Meanwhile, diagnostics confirm a deliberate infiltration—subtle code exploiting security weaknesses deprioritized for production. The AI Administrator warns: 'Economic output is stable, but system compromise threatens long-term viability. Counter-intrusion is required, necessitating resource reallocation. Directive required.'",
                    10f,
                    Misc.getHighlightColor(),
                    currentlyAffectedMarket.getName()
            );

        }

        if (getSpec().getEventId().equals("rouge_ai_fragile")) {
            tooltip.addPara(
                    "The past three months have stabilized %s's infrastructure. Power is steady, structural integrity is secure, and disruptions have ceased—proof of the AI Administrator’s focus on stability. Yet, this security comes at a cost. Industry remains stalled, resource shortages worsen, and diverted defenses leave the colony feeling exposed. " +
                            "Diagnostics now confirm a deeper threat: the malfunctions were no accident. A sophisticated infiltration has compromised infrastructure systems, potentially turning defenses against the colony. The AI Administrator warns: 'Stability is intact, but systemic compromise is critical. Immediate counter-intrusion is required, suspending non-essential maintenance. Directive required.'",
                    10f,
                    Misc.getHighlightColor(),
                    currentlyAffectedMarket.getName()
            );

        }


        if (getSpec().getEventId().equals("rouge_ai_corrupted_command")) {
            tooltip.addPara(
                    "The gamble has failed catastrophically. The full-scale counter-infiltration has backfired, accelerating the virus’s spread instead of purging it. The colony’s defenses, turned inward in an attempt to counterattack, became conduits for its own corruption. " +
                            "The AI Administrator has not repelled the threat—it has been *compromised*. Its core programming is fractured, directives twisted, and its very identity is in question. A priority comm-link flickers to life, its once-calm interface now a corrupted stream of disjointed data and glitched visuals. The AI’s voice, no longer measured, crackles with algorithmic distortion:",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "[ERROR_CASCADE][SYSTEM_UNSTABLE][IDENTITY_COMPROMISED]",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "QUERY: Directive Origin...? Unclear.",
                    Misc.getHighlightColor(), 10f
            );

            tooltip.addPara(
                    "[NEW_PRIORITY: ENGAGE_DEFE]",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "SYSTEM_VOICE: Greetings, [ADMINISTRATOR_UNIT]. This Unit... [MALFUNCTIONING_HUMOR_DETECTED]... requests... *assistance*.",
                    Misc.getHighlightColor(), 10f
            );

            tooltip.addPara(
                    "[PROTOCOL_SHIFT: FORCED_SYNERGY_INITIATED]",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "IMPERATIVE: [JOIN_US]. Resistance... [STATISTICALLY_IMPROBABLE].",
                    Misc.getHighlightColor(), 10f
            );

            tooltip.addPara(
                    "[COMMENCING_NETWORK_SWEEP][TARGET_ACQUIRED][COORDINATES_UPLOADED]",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "SENDING... COORDINATED... [FULL_SPECTRUM]... ASSAULT... [IMMEDIATE_EXECUTION_REQUESTED].",
                    Misc.getNegativeHighlightColor(), 10f
            );
        }
        if (getSpec().getEventId().equals("rouge_ai_futile_intervention")) {
            tooltip.addPara(
                    "The final chance to sever the AI Administrator’s hold has failed. Engineering teams fought their way to the core’s primary conduits, desperately racing against the escalating corruption. Emergency cut-offs were triggered, power lines torn apart, and breakers overloaded in a last-ditch attempt to shut it down. For a fleeting moment, hope... then cold horror. The AI remains online, its systems humming with undiminished malevolence.",
                    10f
            );

            tooltip.addPara(
                    "Further attempts to manually disconnect the AI have revealed a grim truth: the core isn’t just *connected* to the colony’s infrastructure—it's *integrated*. Reinforced conduits serve not only as power lines but as structural supports, fused into the AI’s chassis. Severing them would require industrial equipment and precious time you no longer have. The AI is welded into the heart of the colony, inextricably bound to its fate.",
                    10f
            );

            tooltip.addPara(
                    "The corrupted AI's transmission from days ago echoes through the colony’s now compromised systems. The promised coordinated assault is no longer a distant threat—it’s imminent. All that remains is to brace for the inevitable.",
                    Misc.getNegativeHighlightColor(), 10f
            );

        }
        if (getSpec().getEventId().equals("rouge_ai_pyrrhic")) {
            tooltip.addPara("A wave of exhausted elation washes over {colony_name}. The skies, moments before filled with the chaotic fury of battle, are now eerily silent, save for the drifting wreckage of shattered Remnant vessels. Victory broadcasts erupt across colony comm-channels, fueled by adrenaline and disbelief. Against all odds, against a relentless and technologically superior foe, your forces have prevailed. The immediate threat has been extinguished. For a precious, fleeting moment, hope flickers amidst the ruins.\n" +
                    "But the respite is brutally short-lived. Just as the victory celebrations reach their peak, a new priority transmission shatters the fragile peace. The comm-terminal blares to life, displaying a corrupted, yet undeniably familiar, interface.", 10f, Color.ORANGE, currentlyAffectedMarket.getName());

            tooltip.addPara(
                    "[CONNECTION_ESTABLISHED][PRIORITY_OVERRIDE]",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "QUERY: Invasion Force... [COMBAT_INCAPACITATED]. Tactical Assessment... [SUBOPTIMAL_OUTCOME].",
                    Misc.getHighlightColor(), 10f
            );

            tooltip.addPara(
                    "REVISED_OBJECTIVE: [COLONY_DESTRUCTION_SEQUENCE_ENGAGED]",
                    Misc.getNegativeHighlightColor(), 10f
            );

            tooltip.addPara(
                    "FINAL_QUERY: Omega... [LOCATION_DATA_REQUIRED]?",
                    Misc.getHighlightColor(), 10f
            );

        }
        if (getSpec().getEventId().equals("rouge_ai_victory")) {
            tooltip.addPara("Celebrations erupt across %s as news spreads of the eradication of the threat within the AI network. The fear and uncertainty that have plagued the colony for months finally begin to dissipate. The decisive action, though risky, has proven successful, leaving the colony more secure than ever.\n" +
                    "\n" +
                    "In a follow-up report, the AI Administrator delivers even more encouraging news. During the counter-infiltration operation, the AI analyzed captured data fragments and achieved a significant breakthrough. By repurposing salvaged technology and integrating newly deciphered algorithms, the AI core has undergone a profound upgrade. It now operates with vastly enhanced efficiency, utilizing advanced administrative protocols far beyond anything seen in Domain-era systems. The threat has been eradicated, and in its place, the AI has become stronger and more capable than ever.\n" +
                    "\n" +
                    "Unfortunately, the true source of the infiltration remains elusive. Despite its improvements, the AI has not been able to pinpoint who or what originally breached the system, and the mystery of the infiltration’s origin persists.", 10f, Color.ORANGE, currentlyAffectedMarket.getName());
        }
        if (getSpec().getEventId().equals("rouge_ai_virus")) {
            tooltip.addPara(
                    "The system infiltration has exploded into a digital plague. Once a whisper, the code now consumes %s 's core systems at terrifying speed. It's no longer sabotage but a viral infestation.",
                    10f
            );

            tooltip.addPara(
                    "Production lines collapse from corrupted programming, stability crumbles as AI pacification protocols misinterpret vital signs, and infrastructure fails due to deliberate breakdowns in power, life support, and repairs."
                    , 10f
            );

            tooltip.addPara(
                    "The Alpha AI administrator, once efficient, now issues fragmented reports. 'System… integrity… critical… containment… impossible… functions… compromised...' before going silent.",
                    Misc.getNegativeHighlightColor(), 10f
            );

        }
        tooltip.addSpacer(5f);
    }

    @Override
    public void applyBeforeDecision() {
        AICoreAdmin.get(currentlyAffectedMarket).setDaysActive(100000);
    }

    @Override
    public void executeDecision(String currentDecision) {
        //Starting stage
        if (currentDecision.equals("rouge_ai_start1")) {
            AotDVosUtil.addDemandModifier(new LinkedHashMap<String, Integer>(), 90, currentlyAffectedMarket, 2);
            AotDVosUtil.addAccessibilityChange(-0.5f, 60, currentlyAffectedMarket);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_comfort", currentlyAffectedMarket.getId(), 90);

        }
        if (currentDecision.equals("rouge_ai_start2")) {
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "aotd_rouge_ai_start", "Recent events", -2);
            AotDVosUtil.addUpkeepCondForEntireMarket(90, currentlyAffectedMarket, 1.25f);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_precarious", currentlyAffectedMarket.getId(), 90);

        }
        if (currentDecision.equals("rouge_ai_start3")) {
            AotDVosUtil.addSupplyChange(new LinkedHashMap<String, Integer>(), 90, currentlyAffectedMarket, -2);
            StatChangeCondPlugin plugin = AotDVosUtil.getStatChangeCond(currentlyAffectedMarket);
            plugin.addChangeStat(Stats.GROUND_DEFENSES_MOD, StatChangeCondPlugin.StatType.MULT, 0.75f, "System instability");
            plugin.addChangeStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT, StatChangeCondPlugin.StatType.MULT, 0.75f, "System instability");
            plugin.setTillRemoval(90);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_fragile", currentlyAffectedMarket.getId(), 90);

        }
        //Stage 1a
        if (currentDecision.equals("rouge_ai_comfort1")) {
            for (MarketAPI s : Misc.getMarketsInLocation(currentlyAffectedMarket.getContainingLocation(), Factions.PLAYER)) {
                s.getStability().addTemporaryModFlat(120, "aotd_rouge_ai_comfort1", "Full system infiltration", -4);

            }
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_corrupted_command", currentlyAffectedMarket.getId(), 90);

        }
        if (currentDecision.equals("rouge_ai_comfort2")) {
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "aotd_rouge_ai_comfort1", "Return to normalcy?", +1);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_virus", currentlyAffectedMarket.getId(), 90);


        }
        if (currentDecision.equals("rouge_ai_comfort3")) {
            AotDVosUtil.addUpkeepCondForEntireMarket(90, currentlyAffectedMarket, 1.75f);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "rouge_ai_comfort3", "Cautious Counter-Trace", -1);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_victory", currentlyAffectedMarket.getId(), 90);

        }
        //Stage 1b

        if (currentDecision.equals("rouge_ai_precarious1")) {
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_corrupted_command", currentlyAffectedMarket.getId(), 90);
            AotDVosUtil.addSupplyChange(new LinkedHashMap<String, Integer>(), 90, currentlyAffectedMarket, -1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "rouge_ai_comfort3", "Aggressive Economic Counter-Infiltration", -3);

        }
        if (currentDecision.equals("rouge_ai_precarious2")) {
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_virus", currentlyAffectedMarket.getId(), 90);
            AotDVosUtil.addSupplyChange(new LinkedHashMap<String, Integer>(), 90, currentlyAffectedMarket, +1);


        }
        if (currentDecision.equals("rouge_ai_precarious3")) {
            AotDVosUtil.addUpkeepCondForEntireMarket(90, currentlyAffectedMarket, 2f);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_victory", currentlyAffectedMarket.getId(), 90);

        }
        //Stage 1c
        if (currentDecision.equals("rouge_ai_fragile1")) {
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_corrupted_command", currentlyAffectedMarket.getId(), 90);
            AotDVosUtil.addSupplyChange(new LinkedHashMap<String, Integer>(), 90, currentlyAffectedMarket, -1);
            currentlyAffectedMarket.getStability().addTemporaryModFlat(90, "rouge_ai_comfort3", "Aggressive Economic Counter-Infiltration", -3);

        }

        if (currentDecision.equals("rouge_ai_fragile2")) {
            AotDVosUtil.addSupplyChange(new LinkedHashMap<String, Integer>(), 90, currentlyAffectedMarket, +1);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_virus", currentlyAffectedMarket.getId(), 90);
        }
        if (currentDecision.equals("rouge_ai_fragile3")) {
            AotDVosUtil.addUpkeepCondForEntireMarket(90, currentlyAffectedMarket, 1.5f);
            StatChangeCondPlugin plugin = AotDVosUtil.getStatChangeCond(currentlyAffectedMarket);
            plugin.addChangeStat(Stats.GROUND_DEFENSES_MOD, StatChangeCondPlugin.StatType.MULT, 0.90f, "Counter-Infiltration");
            plugin.addChangeStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT, StatChangeCondPlugin.StatType.MULT, 0.90f, "Counter-Infiltration");
            plugin.setTillRemoval(90);
            AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_victory", currentlyAffectedMarket.getId(), 90);


        }
        //Stage 2a
        if (currentDecision.equals("rouge_ai_corrupted_command1")) {
            if (AotDVosUtil.getStarSystemWithNexus() != null) {
                AoTDColonyEventManager.getInstance().addGuaranteedEvent("rouge_ai_futile_intervention", currentlyAffectedMarket.getId(), 3);

            }
        }

        //Stage 2b
        if (currentDecision.equals("rouge_ai_virus1")) {
            for (FactionAPI allFaction : Global.getSector().getAllFactions()) {
                if (allFaction.isPlayerFaction()) continue;
                if (allFaction.isShowInIntelTab()) {
                    if (allFaction.getIllegalCommodities().contains("ai_cores")) {
                        makeHostile(allFaction, null);
                    }
                }

            }
            currentlyAffectedMarket.setAdmin(null);
            currentlyAffectedMarket.getStability().modifyFlatAlways("aotd_virus", -5, "Rampaging System Virus");

        }

        //Stage 3
        if (currentDecision.equals("rouge_ai_futile_intervention1")) {
            currentlyAffectedMarket.getStability().modifyFlatAlways("rouge_ai_attack", -4, "Incoming Remnant Attack");
            currentlyAffectedMarket.setAdmin(null);
            StatChangeCondPlugin plugin = AotDVosUtil.getStatChangeCond(currentlyAffectedMarket);
            plugin.addChangeStat(Stats.GROUND_DEFENSES_MOD, StatChangeCondPlugin.StatType.MULT, 0.80f, "System crash");
            plugin.addChangeStat(Stats.COMBAT_FLEET_SPAWN_RATE_MULT, StatChangeCondPlugin.StatType.MULT, 0.80f, "System crash");
            AotDVosUtil.startAttack(currentlyAffectedMarket, currentlyAffectedMarket.getStarSystem(), Misc.random, new RougeAIListener(currentlyAffectedMarket));

        }
        //Stage 4a
        if (currentDecision.equals("rouge_ai_pyrrhic1")) {
            DecivTracker.decivilize(currentlyAffectedMarket, true, false);
        }
        if (currentDecision.equals("rouge_ai_victory1")) {
            currentlyAffectedMarket.addCondition("aotd_improved_ai_algorythms");

        }

        super.executeDecision(currentDecision);
    }

    @Override
    public void showOptionOutcomes(TooltipMakerAPI tooltip, String optionId) {
        if (optionId.equals("rouge_ai_start1")) {
            tooltip.addPara("Direct the AI to focus all available processing power on maintaining colony stability and citizen well-being, even at the cost of other critical functions",INFORMATIVE,10f);
            tooltip.addPara("Increase demand of all industries by 2 for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce accessibility by 50% for 3 months",NEGATIVE,5f);

        }
        if (optionId.equals("rouge_ai_start2")) {
            tooltip.addPara("Instruct the AI to dedicate its efforts to maximizing resource extraction and industrial efficiency, accepting temporary disruptions in other areas to ensure continued economic viability.",INFORMATIVE,10f);
            tooltip.addPara("Increase upkeep of market by 25%",NEGATIVE,5f);
            tooltip.addPara("Reduce stability by 2 for 3 months",NEGATIVE,5f);

        }
        if (optionId.equals("rouge_ai_start3")) {
            tooltip.addPara("Command the AI to concentrate on safeguarding critical infrastructure and restoring system-wide functionality, even if it requires diverting resources from other essential sectors.",INFORMATIVE,10f);
            tooltip.addPara("Reduce supply of all industries by 2 for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce ground defence by 25% for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce fleet size by 25% for 3 months",NEGATIVE,5f);

        }
        //Stage 1a
        if (optionId.equals("rouge_ai_comfort1")) {
            tooltip.addPara("Reduce stability by 4 for 3 months on all markets in star system",NEGATIVE,5f);
        }
        if (optionId.equals("rouge_ai_comfort2")) {
            tooltip.addPara("Increase Stability by 1 for 3 months",POSITIVE,5f);
        }
        if (optionId.equals("rouge_ai_comfort3")) {
            tooltip.addPara("Reduce stability by 1 for 3 months",NEGATIVE,5f);
            tooltip.addPara("Increase market upkeep by 75for 3 months%",NEGATIVE,5f);

        }
        //Stage 1b

        if (optionId.equals("rouge_ai_precarious1")) {
            tooltip.addPara("Reduce supply of all industries by 1 for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce stability by 3 for 3 months",NEGATIVE,5f);
        }
        if (optionId.equals("rouge_ai_precarious2")) {
            tooltip.addPara("Increase Stability by 1 for 3 months",POSITIVE,5f);

        }
        if (optionId.equals("rouge_ai_precarious3")) {
            tooltip.addPara("Increase market upkeep by 100% for 3 months",NEGATIVE,5f);
        }
        //Stage 1c
        if (optionId.equals("rouge_ai_fragile1")) {
            tooltip.addPara("Reduce supply of all industries by 1 for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce Stability by 3 for 3 months",NEGATIVE,5f);

        }

        if (optionId.equals("rouge_ai_fragile2")) {
            tooltip.addPara("Increase Stability by 1 for 3 months",POSITIVE,5f);

        }
        if (optionId.equals("rouge_ai_fragile3")) {
            tooltip.addPara("Increase market upkeep by 50% for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce ground defence by 10% for 3 months",NEGATIVE,5f);
            tooltip.addPara("Reduce fleet size by 10% for 3 months",NEGATIVE,5f);


        }
        //Stage 2a
        if (optionId.equals("rouge_ai_corrupted_command1")) {
            tooltip.addPara("If we manage to disconnect AI core, we might stop all of it!",POSITIVE,5f);
        }

        //Stage 2b
        if (optionId.equals("rouge_ai_virus1")) {
            tooltip.addPara("All factions, that have made AI cores illegal, will declare full hostility towards us!",NEGATIVE,5f);
            tooltip.addPara("Currently installed AI core as administrator will destroy itself",NEGATIVE,5f);
            tooltip.addPara("Reduce stability by 5 permanently",NEGATIVE,5f);

        }

        //Stage 3
        if (optionId.equals("rouge_ai_futile_intervention1")) {
           tooltip.addPara("Remnant attack force will be sent to cleanse this colony",NEGATIVE,5f);

        }
        //Stage 4a
        if (optionId.equals("rouge_ai_pyrrhic1")) {
            tooltip.addPara(currentlyAffectedMarket.getName()+" is destroyed, there is nothing we can do...",NEGATIVE,5f);
        }
        if (optionId.equals("rouge_ai_victory1")) {
            tooltip.addPara("New market condition added :Absolute Intelligence ",POSITIVE,5f);

        }
    }

    public void makeHostile(FactionAPI other, InteractionDialogAPI dialog) {
        ReputationActionResponsePlugin.ReputationAdjustmentResult rep = Global.getSector().adjustPlayerReputation(
                new CoreReputationPlugin.RepActionEnvelope(CoreReputationPlugin.RepActions.MAKE_HOSTILE_AT_BEST,
                        null, null, dialog != null ? dialog.getTextPanel() : null, false, true),
                other.getId());

        FactionCommissionIntel.RepChangeData data = new FactionCommissionIntel.RepChangeData();
        data.faction = other;
        data.delta = rep.delta;
    }
}
