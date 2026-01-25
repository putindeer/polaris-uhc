package us.polarismc.polarisuhc.managers.channel;

import us.polarismc.polarisuhc.managers.team.UHCTeam;

public record TeamChannel(UHCTeam team) implements ChannelKey {
    public TeamChannel {
        if (team == null) throw new IllegalArgumentException("team");
    }

    public ChannelType type() {
        return ChannelType.TEAM;
    }

    public boolean sendable() {
        return true;
    }
}
