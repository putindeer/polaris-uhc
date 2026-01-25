package us.polarismc.polarisuhc.managers.channel;

public record SpecChannel() implements ChannelKey {
    public ChannelType type() {
        return ChannelType.SPEC;
    }
    public boolean sendable() {
        return true;
    }
}

