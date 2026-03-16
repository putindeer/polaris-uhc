package us.polarismc.polarisuhc.managers.game;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.game.services.*;
import us.polarismc.polarisuhc.managers.game.services.util.AdvancementService;
import us.polarismc.polarisuhc.managers.game.services.util.LateScatterService;
import us.polarismc.polarisuhc.managers.game.services.util.LocationFinderService;

import java.util.*;

public class GameFlowManager implements Listener {
    private final Main plugin;
    private final PreStartService preStart;
    private final ScatterService scatter;
    private final StartService start;
    private final FinalHealService finalHeal;
    private final PvPStartService pvpStart;
    private final MeetupStartService meetupStart;
    public final LateScatterService lateScatter;
    public final AdvancementService advancementService;
    public final LocationFinderService locationService;
    private StartStep currentStep = StartStep.WAITING;
    private boolean isManualStart = false;

    public GameFlowManager(Main plugin) {
        this.plugin = plugin;
        this.preStart = new PreStartService(plugin);
        this.scatter = new ScatterService(plugin);
        this.finalHeal = new FinalHealService(plugin);
        this.start = new StartService(plugin);
        this.pvpStart = new PvPStartService(plugin);
        this.meetupStart = new MeetupStartService(plugin);
        this.lateScatter = new LateScatterService(plugin);
        this.advancementService = new AdvancementService(plugin);
        this.locationService = new LocationFinderService(plugin);
    }

    public void quickStart(Player host) {
        if (!isManualStart && currentStep != StartStep.WAITING) {
            plugin.utils.message(host, "Quickstart is already started. If you want to cancel it, you can use <aqua>/manualstart</aqua>.");
            return;
        }
        String isManual = isManualStart ? "re-" : "";
        if (isManualStart) {
            isManualStart = false;
        }
        plugin.utils.message(host,
                "Quickstart has been " + isManual + "started. You can use <aqua>/manualstart</aqua> to stop it.");
        nextStep(host);
    }

    public void manualStart(Player host) {
        if (!isManualStart) {
            isManualStart = true;
            if (currentStep != StartStep.WAITING) {
                plugin.utils.message(host,
                        "Quickstart has been stopped. You can start it again with <aqua>/quickstart</aqua> or continue the start process with <aqua>/manualstart</aqua>.");
                return;
            }
        }
        nextStep(host);
    }

    public void finalizeStep(Player host) {
        switch (currentStep) {
            case PRESTART -> currentStep = StartStep.PRESTART_DONE;
            case SCATTER -> currentStep = StartStep.SCATTER_DONE;
            case START -> {
                currentStep = StartStep.START_DONE;
                return;
            }
        }
        if (isManualStart) {
            plugin.utils.message(host, "The current step has finalized. You may start the next one with <aqua>/manualstart</aqua>.");
        } else {
            nextStep(host);
        }
    }

    private void nextStep(Player host) {
        switch (currentStep) {
            case PRESTART, SCATTER, START -> plugin.utils.message(host, "<red>You can't use this command if the previous start state hasn't finished yet.");
            case WAITING -> {
                currentStep = StartStep.PRESTART;
                preStart(host);
            }
            case PRESTART_DONE -> {
                currentStep = StartStep.SCATTER;
                scatter(host);
            }
            case SCATTER_DONE -> {
                currentStep = StartStep.START;
                start(host);
            }
            case START_DONE -> plugin.utils.message(host, "<red>You can't start the UHC if it's already started!");
        }
    }

    public void preStart(Player host) {
        preStart.preStart(host);
    }

    public void scatter(Player host) {
        scatter.scatter(host);
    }

    public void start(Player host) {
        start.start(host);
    }

    public void giveFinalHeal() {
        finalHeal.giveFinalHeal();
    }

    public void startPvP() {
        pvpStart.startPvP();
    }

    public void startMeetup() {
        meetupStart.startMeetup();
    }

    public void resetPrestartAttributes(Player player) {
        Attributable defaults = EntityType.PLAYER.getDefaultAttributes();
        Arrays.stream(preStart.getPrestartAttributes()).forEach(attribute -> {
            AttributeInstance instance = player.getAttribute(attribute);
            AttributeInstance defaultInstance = defaults.getAttribute(attribute);
            if (instance == null || defaultInstance == null) return;
            instance.setBaseValue(defaultInstance.getBaseValue());
        });
    }
}