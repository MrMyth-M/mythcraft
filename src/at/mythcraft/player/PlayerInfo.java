package at.mythcraft.player;

import org.bukkit.ChatColor;

public class PlayerInfo {

    private final String UID;
    private ChatColor chatColor = ChatColor.GOLD;
    private Role role = Role.MEMBER;
    private String nickname;
    private int keyCount = 0;

    public PlayerInfo(String uid, String nickname) {
        this.UID = uid;
        this.nickname = nickname;
    }

    public String getUID() {
        return UID;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public Role getRole() {
        return role;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public int incrementKeyCount() {
        return ++keyCount;
    }

    public void setRole(Role role) {
        this.role = role;
        switch (role) {
            case VIP:
                this.chatColor = ChatColor.LIGHT_PURPLE;
                break;
            case ADMIN:
                this.chatColor = ChatColor.DARK_RED;
                break;
            case DONATOR:
                this.chatColor = ChatColor.GREEN;
                break;
            default:
                this.chatColor = ChatColor.GOLD;
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "UID='" + UID + '\'' +
                ", chatColor=" + chatColor +
                ", role=" + role +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
