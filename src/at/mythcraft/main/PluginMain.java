package at.mythcraft.main;

import at.mythcraft.commands.CommandKit;
import at.mythcraft.commands.PlayerCheckCommandKit;
import at.mythcraft.enchantments.MyEnchants;
import at.mythcraft.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {

    public static String latestPatchMessage = String.format(ChatColor.DARK_RED + "--------------------------------------------\nBUGFIXES\n--------------------------------------------\n" + ChatColor.GOLD + "- Wenn Spieler schlafen, spawnen dennoch Phantoms - behoben\n" +
            ChatColor.DARK_RED + "--------------------------------------------\nNEUE COMMANDS\n--------------------------------------------\n" + ChatColor.GOLD + "- /overworld - Listet alle Spieler in der Overworld\n- /nether - Listet alle Spieler im Nether\n" +
            "- /end - Listet alle Spieler im End\n" +
            "- /patch - Zeigt die aktuellen Plugin-Patchnotes an");

    private Model model = new Model();

    @Override
    public void onEnable() {
        System.out.println("MythCraft wurde geladen.");

        /** Add plugin listeners **/
        getServer().getPluginManager().registerEvents(new AnvilListener(model), this);
        getServer().getPluginManager().registerEvents(new BedListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(model), this);
        getServer().getPluginManager().registerEvents(new EnchantListener(model), this);
        getServer().getPluginManager().registerEvents(new EntityListener(model), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(model), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new HeatWalkerListener(), this);
        getServer().getPluginManager().registerEvents(new SlimeyListener(), this);

        /** Command Kits **/
        CommandKit commandKit = new CommandKit(model);
        PlayerCheckCommandKit playerCheck = new PlayerCheckCommandKit(model);

        /** Basic commands without lookup algorithms **/
        getCommand("pvp").setExecutor(commandKit);
        getCommand("nick").setExecutor(commandKit);
        getCommand("broadcast").setExecutor(commandKit);
        getCommand("patch").setExecutor(commandKit);
        getCommand("passive").setExecutor(commandKit);
        getCommand("debug").setExecutor(commandKit);
        getCommand("destroykey").setExecutor(commandKit);

        /** Commands that require a search for a player**/
        getCommand("role").setExecutor(playerCheck);
        getCommand("nether").setExecutor(playerCheck);
        getCommand("end").setExecutor(playerCheck);
        getCommand("overworld").setExecutor(playerCheck);
        getCommand("invsee").setExecutor(playerCheck);
        getCommand("enderchest").setExecutor(playerCheck);

        MyEnchants customEnchants = new MyEnchants(model);
        customEnchants.register();
    }

    @Override
    public void onDisable() {
        System.out.println("MythCraft wird deaktiviert...");
    }

}
