package me.iMint.ButtonPromote;

import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class bListener implements Listener {
	
	@EventHandler
	public void onButtonPress(PlayerInteractEvent event) {
		// Variables
		Permission permissions = ButtonPromote.permissions;
		HashMap<Player, String> promoting = ButtonPromote.promoting;
		HashMap<Player, String> messaging = ButtonPromote.messaging;
		HashMap<Player, Location> warping = ButtonPromote.warping;
		HashMap<Block, String> promotions = ButtonPromote.promotions;
		HashMap<Block, String> messages = ButtonPromote.messages;
		HashMap<Block, Location> warps = ButtonPromote.warps;
		List<Player> removing = ButtonPromote.removing;
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		// Returns
		if (!(action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK))) return;
		if (!(block.getType().equals(Material.STONE_BUTTON))) return;
		
		// Set Promotions
		if (promoting.containsKey(player)) {
			promotions.put(block, promoting.get(player));
			player.sendMessage(ChatColor.GREEN + "This button will now promote users to " + promoting.get(player));
			promoting.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Set Messages
		if (messaging.containsKey(player)) {
			messages.put(block, messaging.get(player));
			player.sendMessage(ChatColor.GREEN + "This button will now send players that message!");
			messaging.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Set Warps
		if (warping.containsKey(player)) {
			warps.put(block, warping.get(player));
			player.sendMessage(ChatColor.GREEN + "This button will now warp players to the set location!");
			warping.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Remove Promotions
		if (removing.contains(player)) {
			promotions.remove(block);
			player.sendMessage(ChatColor.GREEN + "This button will no longer promote users.");
			removing.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Receive Promotion
		if (promotions.containsKey(block)) {
			if (!permissions.has(player, "use")) return;
			String group = promotions.get(block);
			permissions.playerAddGroup(player, group);
			// Commented out because admins can instead use /setmessage to do this more freely.
			// Un - commented out because it looks nice :P
			player.sendMessage(ChatColor.GREEN + "You are now a member of " + group + "!");
		}
		
		// Receive Message
		if (messages.containsKey(block)) {
			if (!permissions.has(player, "use")) return;
			String message = messages.get(block);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
		
		// Receive Warp
		if (warps.containsKey(block)) {
			if (permissions.has(player, "use")) player.teleport(warps.get(block));
		}
	}
}
