package me.iMint.ButtonPromote;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ButtonCommand implements CommandExecutor {

	private ButtonPromote plugin;

	public ButtonCommand(ButtonPromote instance) {
		this.plugin = instance;
	}

	@Override
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
			String message = args[1];
			for (int i = 2; i < args.length; i++) {
				message += " " + args[i];
			}
			ButtonPromote.selecting.put(p, "message");
			ButtonPromote.messaging.put(p, message);
			sender.sendMessage(ChatColor.AQUA
					+ "Click a button to add this message to it! To cancel selection, type  "
					+ ChatColor.WHITE + "/bp cancel");
			return true;
		}

		// Remove Promotions
		if (args[0].equalsIgnoreCase("remove")) {
			if (!ButtonPromote.permissions.has(p, "buttonpromote.remove")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to remove buttons!");
				return true;
			}
			plugin.cancelSelections(p);
			ButtonPromote.selecting.put(p, "remove");
			sender.sendMessage(ChatColor.AQUA
					+ "Right-click a button to remove it's promotion!");
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

		// Cancel Selections
		if (args[0].equalsIgnoreCase("cancel")) {
			plugin.cancelSelections(p);
			p.sendMessage(ChatColor.GRAY + "Canceled all selections.");
			return true;
		}
		return true;
	}

}
