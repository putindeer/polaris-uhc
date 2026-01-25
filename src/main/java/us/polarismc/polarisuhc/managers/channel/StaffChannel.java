package us.polarismc.polarisuhc.managers.channel;

public record StaffChannel() implements ChannelKey {
    public ChannelType type() {
        return ChannelType.STAFF;
    }
    public boolean sendable() {
        return true;
    }
}
