package us.polarismc.polarisuhc.managers.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
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

    public GameFlowManager(@NotNull Main plugin) {
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

    public void preStart(Player host) {
        preStart.preStart(host);
    }

    public void scatter(Player host) {
        scatter.scatter(host);
    }

    public void start(Player host) {
        start.start(host);
    }

    public void finalHeal() {
        finalHeal.giveFinalheal();
    }

    public void startPvP() {
        pvpStart.startPvP();
    }

    public void startMeetup() {
        meetupStart.startMeetup();
    }

    public void resetPrestartAttributes(Player player) {
        Arrays.stream(preStart.getPrestartAttributes()).map(player::getAttribute).filter(Objects::nonNull).forEach(attribute -> attribute.setBaseValue(attribute.getDefaultValue()));
    }
}