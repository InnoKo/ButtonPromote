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
	public PluginDescriptionFile pdf = this.getDescription();
	public static Permission permissions = null;
	public static boolean usePerms = false;
	public static HashMap<Player, String> selecting = new HashMap<Player, String>();
	public static List<Player> removing = new ArrayList<Player>();
	public static HashMap<Player, Location> warping = new HashMap<Player, Location>();
	public static HashMap<Block, String> buttons = new HashMap<Block, String>();
	public static HashMap<Block, Location> warps = new HashMap<Block, Location>();
	static File pData;
	static File wData;
	static String path = "plugins/ButtonPromote";
	
	@Override
	public void onDisable() {
		save();
		log.info("ButtonPromote v" + pdf.getVersion() + " by iMint is now disabled.");
	}
	
	@Override
	public void onEnable() {
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
			for(Entry<Block, String> entry : buttons.entrySet()) {
				Block block = entry.getKey();
				String group = entry.getValue();
				String world = block.getWorld().getName();
				String print = block.getX() + "," + block.getY() + "," + block.getZ() + ":" + group + ":" + world;
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
				buttons.put(button, group);
				
				String[] _warp = temp[3].split(",");
				Location warp = new Location(world, Integer.parseInt(_warp[0]), Integer.parseInt(_warp[1]), Integer.parseInt(_warp[2]));
				warps.put(button, warp);
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
		// Set Warps
		if (a[0].equalsIgnoreCase("setpromotion")) {
			if (a.length < 2) {
				sendUsage(player);
				return true;
			}
			if (!permissions.has(player, "buttonpromote.create")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to set buttons!");
				return true;
			}
			if (a[1].isEmpty()) {
				player.sendMessage(ChatColor.RED + "You need to specify a group to promote to!");
				return true;
			}
			cancelSelections(player);
			selecting.put(player, a[1]);
			player.sendMessage(ChatColor.AQUA + "Click a button to add this promotion to it! To cancel selection, type " + ChatColor.WHITE + "/bp cancel");
			return true;
		}
		// Remove Promotions
		if (a[0].equalsIgnoreCase("remove")) {
			if (!permissions.has(player, "buttonpromote.remove")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to remove buttons!");
				return true;
			}
			cancelSelections(player);
			removing.add(player);
			player.sendMessage(ChatColor.AQUA + "Right-click a button to remove it's promotion!");
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
		// Cancel Selections
		if (a[0].equalsIgnoreCase("cancel")) {
			cancelSelections(player);
			return true;
		}
		sendUsage(player);
		return true;
	}
	
	// Show command usage to a player
	public void sendUsage(Player s) {
		s.sendMessage(ChatColor.GRAY + "|---------------ButtonPromote Commands----------------|");
		s.sendMessage("/bp setpromotion <group name> " + ChatColor.GOLD + "- Set a button to promote users to the specified group.");
		s.sendMessage("/bp remove " + ChatColor.GOLD + "- Remove group promotion from a button.");
		s.sendMessage("/bp setwarp " + ChatColor.GOLD + "- Set a button to warp users to your current location.");
		s.sendMessage("/bp cancel " + ChatColor.GOLD + "- Cancels all current selections.");
		s.sendMessage("/bp help " + ChatColor.GOLD + "- Shows help dialog.");
		s.sendMessage(ChatColor.GRAY + "|----------------------------------------------------|");
	}
	
	// Cancel all button selections
	public static void cancelSelections(Player p) {
		ButtonPromote.selecting.remove(p);
		ButtonPromote.removing.remove(p);
		ButtonPromote.warping.remove(p);
		p.sendMessage(ChatColor.GRAY + "Canceled all selections.");
	}
	
}
