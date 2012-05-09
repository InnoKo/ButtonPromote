package me.iMint.ButtonPromote;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ButtonPromote extends JavaPlugin {

	public Logger log = Logger.getLogger("Minecraft");
	public static Permission permissions = null;
	public static HashMap<Player, String> selecting = new HashMap<Player, String>();
	public static HashMap<Player, String> commanding = new HashMap<Player, String>();
	public static HashMap<Player, String> messaging = new HashMap<Player, String>();
	public static HashMap<Player, String> promoting = new HashMap<Player, String>();
	public static HashMap<Player, Location> warping = new HashMap<Player, Location>();

	@Override
	public void onDisable() {
		log.info("ButtonPromote by iMint is now disabled.");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		setupDatabase();
		setupPermissions();
		getCommand("bp").setExecutor(new ButtonCommand(this));
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ButtonListener(this), this);
		log.info("ButtonPromote v" + pdf.getVersion()
				+ " by iMint is now enabled!");
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

	// Show command usage to a player
	public void sendUsage(CommandSender s) {
		s.sendMessage(ChatColor.GRAY
				+ "|---------------ButtonPromote Commands----------------|");
		s.sendMessage("/bp setpromotion <group name> " + ChatColor.GOLD
				+ "- Set a button to promote users to the specified group.");
		s.sendMessage("/bp setmessage <message> " + ChatColor.GOLD
				+ "- Set a button to send users the specified message.");
		s.sendMessage("/bp setwarp " + ChatColor.GOLD
				+ "- Set a button to warp users to your current location.");
		s.sendMessage("/bp remove " + ChatColor.GOLD
				+ "- Remove group promotion from a button.");
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
		} catch (PersistenceException ex) {
			this.log.log(Level.INFO, "Installing database.");
			installDDL();
		}
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(ButtonTable.class);
		return list;
	}
}
