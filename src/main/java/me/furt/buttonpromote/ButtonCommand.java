package me.furt.buttonpromote;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ButtonCommand implements CommandExecutor {

	private ButtonPromote plugin;

	public ButtonCommand(ButtonPromote instance) {
		this.plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command cannot be used in console.");
			return true;
		}
		Player p = (Player) sender;
		if (args.length < 1) {
			plugin.sendUsage(sender);
			return true;
		}

		// Set Promotions
		if (args[0].equalsIgnoreCase("setpromotion")) {
			if (args.length < 2) {
				plugin.sendUsage(sender);
				return true;
			}
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set buttons!");
				return true;
			}
			if (args[1].isEmpty()) {
				sender.sendMessage(ChatColor.RED
						+ "You need to specify a group to promote to!");
				return true;
			}
			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "promote");
			ButtonPromote.promoting.put(p, args[1]);
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to add this promotion to it! To cancel selection, type "
					+ ChatColor.WHITE + "/bp cancel");
			return true;
		}

		// Set Messages
		if (args[0].equalsIgnoreCase("setmessage")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set button messages!");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED
						+ "You need to specify a message!");
				return true;
			}
			plugin.cancelSelections(p);
			StringBuilder msg = new StringBuilder();
			for (String loop : args) {
				if (!loop.equalsIgnoreCase("setmessage"))
					msg.append(loop + " ");
			}
			ButtonPromote.selecting.put(p, "message");
			ButtonPromote.messaging.put(p, msg.toString());
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to add this message to it! To cancel selection, type  "
					+ ChatColor.WHITE + "/bp cancel");
			return true;
		}

		// Remove Features
		if (args[0].equalsIgnoreCase("remove")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.remove")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to remove buttons!");
				return true;
			}
			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "remove");
			sender.sendMessage(ChatColor.AQUA
					+ "Right-click a button to remove it's ButtonPromote features!");
			return true;
		}

		// Set Warps
		if (args[0].equalsIgnoreCase("setwarp")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set button warps!");
				return true;
			}
			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "warp");
			ButtonPromote.warping.put(p, p.getLocation());
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to add a warp to this location! To cancel selection, type "
					+ ChatColor.WHITE + "/bp cancel");
			return true;
		}

		// Set Commands
		if (args[0].equalsIgnoreCase("setcommand")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set button warps!");
				return true;
			}
			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "command");
			StringBuilder command = new StringBuilder();
			for (String loop : args) {
				if (!loop.equalsIgnoreCase("setcommand"))
					command.append(loop + " ");
			}
			ButtonPromote.commanding.put(p, command.toString());
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to add this command to it! To cancel selection, type "
					+ ChatColor.WHITE + "/bp cancel");
		}

		// Set Permissions
		if (args[0].equalsIgnoreCase("setpermission")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set button permissions!");
				return true;
			}

			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "permission");
			ButtonPromote.permGiving.put(p, args[1]);
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to add a custom permission to it! To cancel selection, type "
					+ ChatColor.WHITE + "/bp cancel");
		}

		// Set Item
		if (args.length == 4 && args[0].equalsIgnoreCase("setitem")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set button items!");
				return true;
			}

			plugin.cancelSelections(p);
			Material m = Material.matchMaterial(args[2]);
			if (m != null) {
				ButtonPromote.selecting.put(p, "item");
				ButtonPromote.itemGiving.put(p, args[1] + ":" + m.name() + ":"
						+ args[3]);
				sender.sendMessage(ChatColor.AQUA
						+ "Click a button to give/take items from players! To cancel selection, type "
						+ ChatColor.WHITE + "/bp cancel");
			} else {
				sender.sendMessage(ChatColor.RED
						+ "Item name was not found please try again.");
			}
		}

		// Set Use
		if (args.length == 2 && args[0].equalsIgnoreCase("setusage")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set one time use!");
				return true;
			}

			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "usage");
			ButtonPromote.usage.put(p, Boolean.parseBoolean(args[1]));
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to give/remove the one time use feature! To cancel selection, type "
					+ ChatColor.WHITE + "/bp cancel");
		}

		// Set Economy
		if (args.length == 3 && args[0].equalsIgnoreCase("setcurrency")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.create")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to set economy costs!");
				return true;
			}

			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "economy");
			ButtonPromote.currency.put(p, args[1] + ":" + args[2]);
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to give/take currency from players! To cancel selection, type "
					+ ChatColor.WHITE + "/bp cancel");
		}
		
		/*
		// Confirm removal
		if(args[0].equalsIgnoreCase("confirm")) {
			if(ButtonPromote.buttonRemoval.containsKey(p.getName())) {
				Location loc = ButtonPromote.buttonRemoval.get(p.getName());
				ButtonApi ba = new ButtonApi(plugin, loc.getWorld().getName(), (int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
				if(ba != null) {
					ba.clearButton(p);
					ButtonPromote.buttonRemoval.remove(p.getName());
				}
			}
		} */

		// Cancel Selections
		if (args[0].equalsIgnoreCase("cancel")) {
			plugin.cancelSelections(p);
			p.sendMessage(ChatColor.GRAY + "Canceled all selections.");
			return true;
		}
		return true;
	}

}
