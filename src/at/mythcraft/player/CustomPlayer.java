package at.mythcraft.player;

import org.bukkit.entity.Player;

public class CustomPlayer {
    private final Player player;
    private final PlayerInfo playerInfo;

    public CustomPlayer(Player player, PlayerInfo playerInfo) {
        this.player = player;
        this.playerInfo = playerInfo;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }
}
