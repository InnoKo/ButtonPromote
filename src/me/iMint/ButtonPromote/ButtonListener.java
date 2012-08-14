package me.iMint.ButtonPromote;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
				if (ba.clearButton(p))
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
				// Set Currency
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase(
					"economy")) {
				String data = ButtonPromote.currency.get(p);
				String[] split = data.split(":");
				ba.setCurrency(Integer.parseInt(split[1]));
				ba.setCurrencyAction(split[0]);
				p.sendMessage(ChatColor.GREEN + "This button will now "
						+ split[0] + " currency!");
				// Set Item
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase("item")) {
				String data = ButtonPromote.itemGiving.get(p);
				String[] split = data.split(":");
				ItemStack stack = new ItemStack(
						Material.matchMaterial(split[1]),
						Integer.parseInt(split[2]));
				ba.setItem(stack);
				ba.setItemAction(split[0]);
				p.sendMessage(ChatColor.GREEN + "This button will now "
						+ split[0] + " items!");
				// Set one time usage
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase("usage")) {
				boolean bool = ButtonPromote.usage.get(p);
				ba.setOneTimeUse(bool);
				if (bool)
					p.sendMessage(ChatColor.GREEN
							+ "This button now has one time use enabled!");
				else
					p.sendMessage(ChatColor.GREEN
							+ "This button now has one time use disabled!");
			} else {
				p.sendMessage("Unknown command value reseting selection.");
				plugin.cancelSelections(p);
			}
		} else {
			if (ButtonPromote.permissions.has(p, "ButtonPromote.use")) {
				// Get custom permissions
				if (ba.hasPermission()) {
					if (ButtonPromote.permissions.has(p, ba.getPermission())) {
						p.sendMessage(ChatColor.RED
								+ "You do not have permission to use this sign.");
						return;
					}
				}
				// Check for one time use
				if (ba.getOneTimeUse()) {
					// Make sure user has not used the button already
					if (ba.hasUsed(p.getName(), ba.getID())) {
						p.sendMessage(ChatColor.GREEN
								+ "You have already used this button");
						return;
					}
				}
				
				// Check to see if there is a economy plugin first
				if (ButtonPromote.econ) {
					// Get currency
					if (ba.hasCurrency()) {
						String action = ba.getCurrencyAction();
						if (action.equalsIgnoreCase("give")) {
							EconomyResponse cash = ButtonPromote.economy.depositPlayer(p.getName(), ba.getCurrency());
							if (cash.transactionSuccess())
								p.sendMessage(ChatColor.GREEN + "" + ba.getCurrency() + " " + ButtonPromote.economy
										.currencyNamePlural() + " has been added to your account!");
						} else if (action.equalsIgnoreCase("take")) {
							EconomyResponse cash = ButtonPromote.economy
									.withdrawPlayer(p.getName(),
											ba.getCurrency());
							if (!cash.transactionSuccess()) {
								p.sendMessage(ChatColor.RED
										+ "You do not have enough "
										+ ButtonPromote.economy
												.currencyNameSingular()
										+ " to use this button");
								return;
							}
						}
					}
				}

				// Get Warps
				if (ba.hasWarp()) {
					p.teleport(ba.getWarp());
				}

				// Get Promotions
				if (ba.hasGroup()) {
					String g = ba.getGroup();
					if (!ButtonPromote.permissions.playerInGroup(p, g)) {
						String[] groups = ButtonPromote.permissions
								.getPlayerGroups(p);
						if (groups.length != 0) {
							for (String old : groups) {
								ButtonPromote.permissions.playerRemoveGroup(p,
										old);
							}
						}
						ButtonPromote.permissions.playerAddGroup(p, g);
						p.sendMessage(ChatColor.GREEN
								+ "You are now a member of " + g + "!");
					} else {
						p.sendMessage(ChatColor.GREEN
								+ "You have already been promoted.");
					}
				}

				// Get Commands
				if (ba.hasCommand()) {
					p.performCommand(ba.getCommand());
				}

				// Get Item
				if (ba.hasItem()) {
					String action = ba.getItemAction();
					ItemStack item = ba.getItem();
					if (action.equalsIgnoreCase("give")) {
						p.getInventory().addItem(item);
					} else if (action.equalsIgnoreCase("take")) {
						p.getInventory().remove(item);
					}
				}
				
				// Get Messages
				if (ba.hasMessage()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							ba.getMessage()));
				}
				
				// If button is one time use add player to user table
				if (ba.getOneTimeUse())
					ba.setUsed(p.getName(), ba.getID());
			}
		}
	}
}
