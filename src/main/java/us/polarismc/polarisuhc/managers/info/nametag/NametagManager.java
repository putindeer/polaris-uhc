package us.polarismc.polarisuhc.managers.info.nametag;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

public class NametagManager implements Listener {
    private final Main plugin;

    public NametagManager(Main plugin) {
        this.plugin = plugin;
        checkTeam();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ensureDisplay(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        TextDisplay display = uhcPlayer.getNametag();
        if (display != null && display.isValid()) {
            display.remove();
        }
        uhcPlayer.setNametag(null);
    }

    public void ensureDisplay(Player player) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);

        if (shouldHideVanilla(uhcPlayer)) {
            ensureHiddenVanilla(player);
        }

        TextDisplay display = uhcPlayer.getNametag();
        if (display == null || !display.isValid()) {
            display = spawn(player);
            uhcPlayer.setNametag(display);
        }

        applyState(display, player.isSneaking());
        display.text(uhcPlayer.getDisplayNameComponent());
        applyVisibilityForOwner(player, display);
    }

    public void updateText(UHCPlayer uhcPlayer) {
        TextDisplay display = uhcPlayer.getNametag();
        if (display == null || !display.isValid()) return;

        Component name = uhcPlayer.getDisplayNameComponent();
        display.text(name);
    }

    public void reapplyAll() {
        for (UHCPlayer uhcPlayer : plugin.player.getOnlinePlayers()) {
            TextDisplay display = uhcPlayer.getNametag();
            if (display == null || !display.isValid()) continue;
            display.text(uhcPlayer.getDisplayNameComponent());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(event.getEntity());
        TextDisplay display = uhcPlayer.getNametag();
        display.remove();
        uhcPlayer.setNametag(null);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(event.getPlayer());
        ensureDisplay(event.getPlayer());
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        TextDisplay display = plugin.player.getUHCPlayer(event.getPlayer()).getNametag();
        if (display == null || !display.isValid()) return;
        applyState(display, event.isSneaking());
    }

    @EventHandler
    public void onOwnerGamemodeChange(PlayerGameModeChangeEvent event) {
        Player owner = event.getPlayer();
        UHCPlayer uhcOwner = plugin.player.getUHCPlayer(owner);

        TextDisplay display = uhcOwner.getNametag();
        if (display == null || !display.isValid()) return;

        boolean ownerWillBeSpec = event.getNewGameMode() == GameMode.SPECTATOR;
        applyVisibilityForOwner(owner, display, ownerWillBeSpec);
    }

    @EventHandler
    public void onViewerGamemodeChange(PlayerGameModeChangeEvent event) {
        Player viewer = event.getPlayer();
        boolean viewerWillBeSpec = event.getNewGameMode() == GameMode.SPECTATOR;

        for (UHCPlayer ownerUhc : plugin.player.getOnlinePlayers()) {
            Player owner = ownerUhc.getPlayer();
            if (owner == null) continue;
            if (owner.equals(viewer)) continue;

            TextDisplay display = ownerUhc.getNametag();
            if (display == null || !display.isValid()) continue;

            boolean ownerIsSpec = owner.getGameMode() == GameMode.SPECTATOR;

            if (ownerIsSpec && !viewerWillBeSpec) {
                viewer.hideEntity(plugin, display);
            } else {
                viewer.showEntity(plugin, display);
            }
        }
    }

    private void applyVisibilityForOwner(Player owner, TextDisplay display) {
        applyVisibilityForOwner(owner, display, owner.getGameMode() == GameMode.SPECTATOR);
    }

    private void applyVisibilityForOwner(Player owner, TextDisplay display, boolean ownerIsSpec) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer.equals(owner)) continue;

            boolean viewerIsSpec = viewer.getGameMode() == GameMode.SPECTATOR;

            if (ownerIsSpec && !viewerIsSpec) {
                viewer.hideEntity(plugin, display);
            } else {
                viewer.showEntity(plugin, display);
            }
        }
    }

    private TextDisplay spawn(Player player) {
        TextDisplay display = player.getWorld().spawn(player.getLocation(), TextDisplay.class, NametagPreset.NORMAL::apply);
        player.addPassenger(display);
        return display;
    }

    private void applyState(TextDisplay display, boolean sneaking) {
        NametagPreset preset = sneaking ? NametagPreset.SNEAKING : NametagPreset.NORMAL;
        preset.apply(display);
    }

    private boolean shouldHideVanilla(UHCPlayer uhcPlayer) {
        return uhcPlayer.getTeam() == null;
    }

    private void ensureHiddenVanilla(Player player) {
        Team team = checkTeam();
        if (team.hasEntry(player.getName())) return;
        team.addEntry(player.getName());
    }

    private Team checkTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String hideTeam = "nametag_hide";
        Team team = scoreboard.getTeam(hideTeam);
        if (team == null) {
            team = scoreboard.registerNewTeam(hideTeam);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        return team;
    }
}

