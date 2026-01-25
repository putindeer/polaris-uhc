package us.polarismc.polarisuhc.managers.channel;

public record GlobalChannel() implements ChannelKey {
    public ChannelType type() {
        return ChannelType.GLOBAL;
    }
    public boolean sendable() {
        return true;
    }
}
