package me.iMint.ButtonPromote;

/**
 * ButtonPromote
 * Version 1.5
 * CB 1.2.5-R1.0
 * by iMint
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ButtonPromote extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");
	public static Permission permissions = null;
	public static boolean usePerms = false;
	public static HashMap<Player, String> promoting = new HashMap<Player, String>();
	public static HashMap<Player, String> messaging = new HashMap<Player, String>();
	public static HashMap<Player, Location> warping = new HashMap<Player, Location>();
	public static HashMap<Block, String> promotions = new HashMap<Block, String>();
	public static HashMap<Block, String> messages = new HashMap<Block, String>();
	public static HashMap<Block, Location> warps = new HashMap<Block, Location>();
	public static List<Player> removing = new ArrayList<Player>();
	static File pData;
	static File mData;
	static File wData;
	static String path = "plugins/ButtonPromote";
	
	@Override
	public void onDisable() {
		save();
		log.info("ButtonPromote by iMint is now disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = this.getDescription();
		usePerms = setupPermissions();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new bListener(), this);
		load();
		log.info("ButtonPromote v" + pdf.getVersion() + " by iMint is now enabled!");
	}
	
	public static void save() {
		// Save promotions
		try {
			FileOutputStream buttonOS = new FileOutputStream(pData);
			PrintStream buttonOut = new PrintStream(buttonOS);
			for(Entry<Block, String> entry : promotions.entrySet()) {
				Block block = entry.getKey();
				String group = entry.getValue();
				String world = block.getWorld().getName();
				String print = block.getX() + "," + block.getY() + "," + block.getZ() + ":" + group + ":" + world;
				buttonOut.println(print);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Save messages
		try {
			FileOutputStream buttonOS = new FileOutputStream(mData);
			PrintStream buttonOut = new PrintStream(buttonOS);
			for(Entry<Block, String> entry : messages.entrySet()) {
				Block block = entry.getKey();
				String message = entry.getValue();
				String world = block.getWorld().getName();
				String print = world + ":" + block.getX() + "," + block.getY() + "," + block.getZ() + ":" + message;
				buttonOut.println(print);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Save warps
		try {
			FileOutputStream buttonOS = new FileOutputStream(wData);
			PrintStream buttonOut = new PrintStream(buttonOS);
			for(Entry<Block, Location> entry : warps.entrySet()) {
				Block block = entry.getKey();
				Location warp = entry.getValue();
				String world = block.getWorld().getName();
				String world2 = warp.getWorld().getName();
				String print = world + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ":" + world2 + warp.getX() + warp.getY() + warp.getZ();
				buttonOut.println(print);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		// Load promotions
		try {
			pData = new File(path + "/promotions.dat");
			if (!pData.exists()) {
				(new File(path)).mkdir();
				pData.createNewFile();
			}
			FileInputStream in = new FileInputStream(pData);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(in)));
			String s;
			while ((s = reader.readLine()) != null) {
				String temp[] = s.split(":");
				String[] _button = temp[0].split(",");
				String _group = temp[1];
				String _world = temp[2];
				World world = getServer().getWorld(_world);
				Block button = world.getBlockAt(Integer.parseInt(_button[0]), Integer.parseInt(_button[1]), Integer.parseInt(_button[2]));
				String group = _group;
				promotions.put(button, group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Load messages
		try {
			mData = new File(path + "/messages.dat");
			if (!mData.exists()) {
				(new File(path)).mkdir();
				mData.createNewFile();
			}
			FileInputStream in = new FileInputStream(mData);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(in)));
			String s;
			while ((s = reader.readLine()) != null) {
				String temp[] = s.split(":");
				String[] _button = temp[1].split(",");
				String _world = temp[0];
				World world = getServer().getWorld(_world);
				Block button = world.getBlockAt(Integer.parseInt(_button[0]), Integer.parseInt(_button[1]), Integer.parseInt(_button[2]));
				String message = temp[2];
				messages.put(button, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Load warps
		try {
			wData = new File(path + "/warps.dat");
			if (!wData.exists()) {
				(new File(path)).mkdir();
				wData.createNewFile();
			}
			FileInputStream in = new FileInputStream(wData);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(in)));
			String s;
			while ((s = reader.readLine()) != null) {
				String[] temp = s.split(":");
				String[] _button = temp[0].split(",");
				String[] _warp = temp[1].split(",");
				String _world = _button[0];
				String _world2 = _warp[0];
				World world = getServer().getWorld(_world);
				World world2 = getServer().getWorld(_world2);
				Block button = world.getBlockAt(Integer.parseInt(_button[1]), Integer.parseInt(_button[2]), Integer.parseInt(_button[3]));
				Location warp = new Location(world2, Integer.parseInt(_warp[0]), Integer.parseInt(_warp[1]), Integer.parseInt(_warp[2]));
				warps.put(button, warp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Set up Vault permissions
	public boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permissions = permissionProvider.getProvider();
		}
		return (permissions != null);
	}
	
	@Override
	// Commands
	public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
		if (!(s instanceof Player)) return false;
		Player player = (Player) s;
		if (!l.equalsIgnoreCase("bp")) return false;
		if (a.length < 1) {
			sendUsage(player);
			return true;
		}
		// Set Promotions
		if (a[0].equalsIgnoreCase("setpromotion")) {
			if (!permissions.has(player, "buttonpromote.create")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to set button promotions!");
				return true;
			}
			if (a.length < 2) {
				player.sendMessage(ChatColor.RED + "You need to specify a group to promote to!");
				return true;
			}
			cancelSelections(player);
			promoting.put(player, a[1]);
			player.sendMessage(ChatColor.AQUA + "Click a button to add this promotion to it! To cancel selection, type  " + ChatColor.WHITE + "/bp cancel");
			return true;
		}
		// Set Warps
		if (a[0].equalsIgnoreCase("setwarp")) {
			if (!permissions.has(player, "buttonpromote.create")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to set button warps!");
				return true;
			}
			cancelSelections(player);
			warping.put(player, player.getLocation());
			player.sendMessage(ChatColor.AQUA + "Click a button to add a warp to this location! To cancel selection, type " + ChatColor.WHITE + "/bp cancel");
			return true;
		}
		// Set Messages
		if (a[0].equalsIgnoreCase("setmessage")) {
			if (!permissions.has(player, "buttonpromote.create")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to set button messages!");
				return true;
			}
			if (a.length < 2) {
				player.sendMessage(ChatColor.RED + "You need to specify a message!");
				return true;
			}
			cancelSelections(player);
			String message = a[1];
			for(int i = 2; i < a.length; i++) {
				message += " " + a[i];
			}
			messaging.put(player, message);
			player.sendMessage(ChatColor.AQUA + "Click a button to add this message to it! To cancel selection, type " + ChatColor.WHITE + "/bp cancel");
			return true;
		}
		// Remove Promotions
		if (a[0].equalsIgnoreCase("remove")) {
			if (!permissions.has(player, "buttonpromote.remove")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to remove promotions!");
				return true;
			}
			cancelSelections(player);
			removing.add(player);
			player.sendMessage(ChatColor.AQUA + "Right-click a button to remove it's promotion!");
			return true;
		}
		// Reload Files
		if (a[0].equalsIgnoreCase("reload")) {
			if (!permissions.has(player, "buttonpromote.reload")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to reload!");
				return true;
			}
			load();
			player.sendMessage(ChatColor.GREEN + "ButtonPromote successfully reloaded!");
			return true;
		}
		// Cancel Selections
		if (a[0].equalsIgnoreCase("cancel")) {
			cancelSelections(player);
			player.sendMessage(ChatColor.GRAY + "Canceled all selections.");
			return true;
		}
		sendUsage(player);
		return true;
	}
	
	// Show command usage to a player
	public void sendUsage(Player s) {
		s.sendMessage(ChatColor.GRAY + "|---------------ButtonPromote Commands----------------|");
		s.sendMessage("/bp setpromotion <group name> " + ChatColor.GOLD + "- Set a button to promote users to the specified group.");
		s.sendMessage("/bp setmessage <message> " + ChatColor.GOLD + "- Set a button to send users the specified message.");
		s.sendMessage("/bp setwarp " + ChatColor.GOLD + "- Set a button to warp users to your current location.");
		s.sendMessage("/bp remove " + ChatColor.GOLD + "- Remove group promotion from a button.");
		s.sendMessage("/bp cancel " + ChatColor.GOLD + "- Cancels any current selections.");
		s.sendMessage("/bp reload " + ChatColor.GOLD + "- Reloads data files.");
		s.sendMessage(ChatColor.GRAY + "|----------------------------------------------------|");
	}
	
	// Cancel all button selections
	public static void cancelSelections(Player p) {
		ButtonPromote.promoting.remove(p);
		ButtonPromote.removing.remove(p);
		ButtonPromote.warping.remove(p);
	}
	
}
