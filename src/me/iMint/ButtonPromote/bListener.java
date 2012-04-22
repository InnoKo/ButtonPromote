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
		HashMap<Player, String> selecting = ButtonPromote.selecting;
		HashMap<Player, Location> warping = ButtonPromote.warping;
		HashMap<Block, String> buttons = ButtonPromote.buttons;
		HashMap<Block, Location> warps = ButtonPromote.warps;
		List<Player> removing = ButtonPromote.removing;
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		// Returns
		if (!(action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK))) return;
		if (!(block.getType().equals(Material.STONE_BUTTON))) return;
		
		// Set Promotions
		if (selecting.containsKey(player)) {
			buttons.put(block, selecting.get(player));
			player.sendMessage(ChatColor.GREEN + "This button will now promote users to " + selecting.get(player));
			selecting.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Remove Promotions
		if (removing.contains(player)) {
			buttons.remove(block);
			player.sendMessage(ChatColor.GREEN + "This button will no longer promote users.");
			removing.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Remove Warps
		if (warping.containsKey(player)) {
			warps.put(block, warping.get(player));
			player.sendMessage(ChatColor.GREEN + "This button will now warp players to the set location!");
			warping.remove(player);
			ButtonPromote.save();
			return;
		}
		
		// Promotion
		if (buttons.containsKey(block)) {
			if (!permissions.has(player, "use")) return;
			String group = buttons.get(block);
			permissions.playerAddGroup(player, group);
			player.sendMessage(ChatColor.GREEN + "You are now a member of " + group + "!");
		}
		
		// Warping
		if (warps.containsKey(block)) {
			if (permissions.has(player, "use")) player.teleport(warps.get(block));
		}
	}
}
