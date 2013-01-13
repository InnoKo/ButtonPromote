package me.furt.buttonpromote;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.PersistenceException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class ButtonPromote extends JavaPlugin {

	public static Permission permissions = null;
	public static Economy economy = null;
	public static boolean econ = false;
	public static HashMap<Player, String> selecting = new HashMap<Player, String>();
	public static HashMap<Player, String> commanding = new HashMap<Player, String>();
	public static HashMap<Player, String> messaging = new HashMap<Player, String>();
	public static HashMap<Player, String> promoting = new HashMap<Player, String>();
	public static HashMap<Player, Location> warping = new HashMap<Player, Location>();
	public static HashMap<Player, String> itemGiving = new HashMap<Player, String>();
	public static HashMap<Player, String> permGiving = new HashMap<Player, String>();
	public static HashMap<Player, String> currency = new HashMap<Player, String>();
	public static HashMap<Player, Boolean> usage = new HashMap<Player, Boolean>();
	public static HashMap<String, Location> buttonRemoval = new HashMap<String, Location>();

	@Override
	public void onDisable() {
		selecting.clear();
		promoting.clear();
		warping.clear();
		messaging.clear();
		commanding.clear();
		itemGiving.clear();
		permGiving.clear();
		currency.clear();
		usage.clear();
		this.getLogger().log(Level.INFO, "Disabled");
	}

	@Override
	public void onEnable() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		
		this.getConfig().addDefault("keepOldGroups", false);
		this.getConfig().addDefault("globalOneTimeUse", false);
		this.getConfig().addDefault("consoleCommands", false);
		this.getConfig().addDefault("warpTimer", 3);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		PluginDescriptionFile pdf = this.getDescription();
		setupDatabase();
		setupPermissions();
		setupEconomy();
		getCommand("buttonpromote").setExecutor(new ButtonCommand(this));
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ButtonListener(this), this);
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.addCustomData(new Metrics.Plotter("Total Buttons Used") {

		        @Override
		        public int getValue() {
		            return getTotalButtons();
		        }

		    });
		    metrics.start();
		} catch (IOException e) {
		   this.getLogger().log(Level.WARNING, "PluginMetrics could not start.");
		}
		this.getLogger().log(Level.INFO,
				"v" + pdf.getVersion() + " is now enabled!");
	}

	// Set up Vault permissions
	public boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permissions = permissionProvider.getProvider();
		}
		return (permissions != null);
	}

	// Set up Vault economy
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		if (economy != null) {
			econ = true;
		}

		return (economy != null);
	}

	// Show command usage to a player
	public void sendUsage(CommandSender s) {
		s.sendMessage(ChatColor.GRAY
				+ "|---------------ButtonPromote Commands----------------|");
		s.sendMessage("/bp setpromotion <group name> " + ChatColor.GOLD
				+ "- Set a button to promote users to the specified group.");
		s.sendMessage("/bp setmessage <message> " + ChatColor.GOLD
				+ "- Set a button to send users the specified message.");
		s.sendMessage("/bp setcommand <command> " + ChatColor.GOLD
				+ "- Set a button perform a command by the player.");
		s.sendMessage("/bp setwarp " + ChatColor.GOLD
				+ "- Set a button to warp users to your current location.");
		s.sendMessage("/bp setitem <give/take> <item-name> <amount> "
				+ ChatColor.GOLD + "- Set a button to give or take item/s.");
		s.sendMessage("/bp setcurrency <give/take> <amount> " + ChatColor.GOLD
				+ "- Set a button to give or take currency.");
		s.sendMessage("/bp setusage <true/false> " + ChatColor.GOLD
				+ "- Set a button to only be used once.");
		s.sendMessage("/bp remove " + ChatColor.GOLD
				+ "- Removes all features from a button.");
		s.sendMessage("/bp cancel " + ChatColor.GOLD
				+ "- Cancels any current selections.");
		s.sendMessage("/bp reload " + ChatColor.GOLD + "- Reloads data files.");
		s.sendMessage(ChatColor.GRAY
				+ "|----------------------------------------------------|");
	}

	// Cancel all button selections
	public void cancelSelections(Player p) {
		selecting.remove(p);
		promoting.remove(p);
		warping.remove(p);
		messaging.remove(p);
		commanding.remove(p);
		itemGiving.remove(p);
		permGiving.remove(p);
		currency.remove(p);
		usage.remove(p);
	}

	private void setupDatabase() {
		try {
			File ebeans = new File("ebean.properties");
			if (!ebeans.exists()) {
				try {
					ebeans.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			getDatabase().find(ButtonTable.class).findRowCount();
			getDatabase().find(ButtonUserTable.class).findRowCount();
		} catch (PersistenceException ex) {
			this.getLogger().log(Level.INFO, "Installing database.");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(ButtonTable.class);
		list.add(ButtonUserTable.class);
		return list;
	}
	
	public String argument(String original, String[] arguments, String[] points) {
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i].contains(",")) {
				for (String arg : arguments[i].split(",")) {
					original = original.replace(arg, points[i]);
				}
			} else {
				original = original.replace(arguments[i], points[i]);
			}
		}

		return original;
	}
	
	public int getTotalButtons() {
		return getDatabase().find(ButtonTable.class).findRowCount();
	}
}
