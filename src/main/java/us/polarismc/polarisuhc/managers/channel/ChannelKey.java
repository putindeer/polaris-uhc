package us.polarismc.polarisuhc.managers.channel;

public sealed interface ChannelKey
        permits GlobalChannel, StaffChannel, SpecChannel, TeamChannel {
    ChannelType type();
    boolean sendable();
}
