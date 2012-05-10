package me.iMint.ButtonPromote;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ButtonListener implements Listener {
	private ButtonPromote plugin;

	public ButtonListener(ButtonPromote instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onButtonPress(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_BLOCK
				&& event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (!event.getClickedBlock().getType().equals(Material.STONE_BUTTON))
			return;

		Player p = event.getPlayer();
		World w = p.getWorld();
		Block b = event.getClickedBlock();
		ButtonApi ba = new ButtonApi(plugin, w.getName(), b.getX(), b.getY(),
				b.getZ());

		if (ButtonPromote.selecting.containsKey(p)) {
			// Set Promotions
			if (ButtonPromote.selecting.get(p).equalsIgnoreCase("promote")) {
				ba.setGroup(ButtonPromote.promoting.get(p));
				event.getPlayer().sendMessage(
						ChatColor.GREEN
								+ "This button will now promote users to "
								+ ButtonPromote.selecting.get(p));
				plugin.cancelSelections(p);
			// Remove Promotions
			} else if (ButtonPromote.selecting.get(p)
					.equalsIgnoreCase("remove")) {
				ba.clearButton();
				p.sendMessage(ChatColor.GREEN
						+ "This button will no longer promote users.");
				plugin.cancelSelections(p);
			// Set Warps
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase("warp")) {
				ba.setWarp(ButtonPromote.warping.get(p));
				p.sendMessage(ChatColor.GREEN
						+ "This button will now warp players to the set location!");
				plugin.cancelSelections(p);
			// Set Message
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase(
					"message")) {
				ba.setMessage(ButtonPromote.messaging.get(p));
				p.sendMessage(ChatColor.GREEN
						+ "This button will now send players that message!");
				plugin.cancelSelections(p);
			// Set Command
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase(
					"command")) {
				ba.setCommand(ButtonPromote.commanding.get(p));
				p.sendMessage(ChatColor.GREEN
						+ "This button will now execute the command "
						+ ButtonPromote.commanding.get(p));
				plugin.cancelSelections(p);
			} else {
				p.sendMessage("Unknown command value reseting selection.");
				plugin.cancelSelections(p);
			}
		} else {
			if (ButtonPromote.permissions.has(p, "ButtonPromote.use")) {
				// Get Warps
				if (ba.getWarp() != null) {
					p.teleport(ba.getWarp());
				}

				// Get Promotions
				if (ba.getGroup() != null) {
					if (!ba.getGroup().equalsIgnoreCase("none")) {
						String g = ba.getGroup();
						if(!ButtonPromote.permissions.playerInGroup(p, g)) {
							ButtonPromote.permissions.playerAddGroup(p, g);
							p.sendMessage(ChatColor.GREEN
									+ "You are now a member of " + g + "!");
						} else {
							p.sendMessage(ChatColor.GREEN + "You have already been promoted.");
						}
					}
				}

				// Get Messages
				if (ba.getMessage() != null) {
					if (!ba.getMessage().equalsIgnoreCase("none"))
						p.sendMessage(ChatColor.translateAlternateColorCodes(
								'&', ba.getMessage()));
				}

				// Get Commands
				if (ba.getCommand() != null) {
					String command = ba.getCommand();
					if (!command.equalsIgnoreCase("none"))
						p.performCommand(command);
				}
			}
		}
	}
}
