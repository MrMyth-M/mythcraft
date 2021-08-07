package at.mythcraft.chestlock;

public class LockedChest implements KeyInfoApplicable {
    private final KeyInfo keyInfo;

    public LockedChest(KeyInfo key) {
        this.keyInfo = key;
    }

    @Override
    public KeyInfo getKeyInfo() {
        return this.keyInfo;
    }

    @Override
    public String toString() {
        return "LockedChest{" +
                "keyInfo=" + keyInfo +
                '}';
    }
}
