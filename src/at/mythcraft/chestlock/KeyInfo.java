package at.mythcraft.chestlock;

import org.bukkit.Location;

public class KeyInfo implements Cloneable {
    private final int keyId;
    private final String ownerUUID;
    private CustomLocation customLocation;
    private boolean isDuplicate;

    public KeyInfo(int keyId, String uuid) {
        this.keyId = keyId;
        this.ownerUUID = uuid;
        this.customLocation = null;
        this.isDuplicate = false;
    }

    public boolean matches(KeyInfo other) {
        return this.keyId == other.keyId && this.ownerUUID.equals(other.ownerUUID) && this.customLocation.matches(other.customLocation);
    }

    public int getKeyId() {
        return this.keyId;
    }

    public CustomLocation getLocation() {
        return customLocation;
    }

    public void setLocation(Location location) {
        this.customLocation = new CustomLocation(location.getX(), location.getY(), location.getZ());
    }

    public CustomLocation getCustomLocation() {
        return this.customLocation;
    }

    public void setCustomLocation(CustomLocation customLocation) {
        this.customLocation = customLocation;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean duplicate) {
        isDuplicate = duplicate;
    }

    @Override
    public String toString() {
        return "KeyInfo{" +
                "keyId=" + keyId +
                ", ownerUUID='" + ownerUUID + '\'' +
                ", customLocation=" + customLocation +
                ", isDuplicate=" + isDuplicate +
                '}';
    }

    public KeyInfo clone() {
        KeyInfo newInfo = new KeyInfo(keyId, ownerUUID);
        newInfo.setCustomLocation(customLocation);
        newInfo.setDuplicate(true);
        return newInfo;
    }
}
