package me.furt.buttonpromote;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ButtonListener implements Listener {
	private ButtonPromote plugin;

	public ButtonListener(ButtonPromote instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onButtonPress(PlayerInteractEvent event) {
		Block b = event.getClickedBlock();
		if (b == null)
			return;
		Action a = event.getAction();
		switch (b.getType()) {
		case STONE_PLATE:
			if (a.equals(Action.PHYSICAL))
				break;
			else
				return;

		case WOOD_PLATE:
			if (a.equals(Action.PHYSICAL))
				break;
			else
				return;
			
		case GOLD_PLATE:
			if (a.equals(Action.PHYSICAL))
				break;
			else
				return;
			
		case IRON_PLATE:
			if (a.equals(Action.PHYSICAL))
				break;
			else
				return;

		case STONE_BUTTON:
			switch (a) {
			case LEFT_CLICK_BLOCK:
				break;
			case RIGHT_CLICK_BLOCK:
				break;
			default:
				return;
			}

			break;

		case WOOD_BUTTON:
			switch (a) {
			case LEFT_CLICK_BLOCK:
				break;
			case RIGHT_CLICK_BLOCK:
				break;
			default:
				return;
			}

			break;

		default:
			return;
		}

		final Player p = event.getPlayer();
		World w = p.getWorld();
		final ButtonApi ba = new ButtonApi(plugin, w.getName(), b.getX(),
				b.getY(), b.getZ());

		if (ButtonPromote.selecting.containsKey(p)) {
			// Set Group
			if (ButtonPromote.selecting.get(p).equalsIgnoreCase("promote")) {
				ba.setGroup(ButtonPromote.promoting.get(p));
				event.getPlayer().sendMessage(
						ChatColor.GREEN
								+ "This button will now promote users to "
								+ ButtonPromote.selecting.get(p));
				plugin.cancelSelections(p);

				// Remove Features
			} else if (ButtonPromote.selecting.get(p)
					.equalsIgnoreCase("remove")) {
				if (ba.clearButton(p))
					p.sendMessage(ChatColor.GREEN
							+ "This button no longer has ButtonPromote features.");
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
				plugin.cancelSelections(p);

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
				plugin.cancelSelections(p);

				// Set one time usage
			} else if (ButtonPromote.selecting.get(p).equalsIgnoreCase("usage")) {
				boolean bool = ButtonPromote.usage.get(p);
				ba.setOneTimeUse(bool);
				if (bool) {
					p.sendMessage(ChatColor.GREEN
							+ "This button now has one time use enabled!");
					plugin.cancelSelections(p);
				} else {
					p.sendMessage(ChatColor.GREEN
							+ "This button now has one time use disabled!");
					plugin.cancelSelections(p);
				}
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
				if (ba.hasOneTimeUse()) {
					// Make sure user has not used the button already
					if (ba.hasUsed(p.getName(), ba.getID())) {
						p.sendMessage(ChatColor.GREEN
								+ "You have already used this button");
						return;
					}
				}

				// Check to see if there is a economy plugin first
				if (ButtonPromote.econ) {
					// Take Currency
					if (ba.hasCurrency()) {
						String action = ba.getCurrencyAction();
						if (action.equalsIgnoreCase("take")) {
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

				// Take Item
				if (ba.hasItem()) {
					String action = ba.getItemAction();
					if (action.equalsIgnoreCase("take")) {
						ItemStack item = ba.getItem();
						Material check = ba.getItem().getType();
						if (p.getInventory().contains(check)) {
							int slot = p.getInventory().first(check);
							ItemStack stack = p.getInventory().getItem(slot);
							if (stack.getAmount() >= item.getAmount()) {
								int newAmount = stack.getAmount()
										- item.getAmount();
								stack.setAmount(newAmount);
								p.getInventory().setItem(slot, stack);
							} else {
								p.sendMessage(ChatColor.RED
										+ "You do not have the required item amount.");
								return;
							}
						} else {
							p.sendMessage(ChatColor.RED
									+ "You do not have the required item.");
							return;
						}
					}
				}

				// Check to see if there is a economy plugin first
				if (ButtonPromote.econ) {
					// Give currency
					if (ba.hasCurrency()) {
						String action = ba.getCurrencyAction();
						if (action.equalsIgnoreCase("give")) {
							EconomyResponse cash = ButtonPromote.economy
									.depositPlayer(p.getName(),
											ba.getCurrency());
							if (cash.transactionSuccess())
								p.sendMessage(ChatColor.GREEN
										+ ""
										+ ba.getCurrency()
										+ " "
										+ ButtonPromote.economy
												.currencyNamePlural()
										+ " has been added to your account!");
						}
					}
				}

				// Give Item
				if (ba.hasItem()) {
					String action = ba.getItemAction();
					ItemStack item = ba.getItem();
					if (action.equalsIgnoreCase("give")) {
						p.getInventory().addItem(item);
					}
				}

				// Get Promotions
				if (ba.hasGroup()) {
					String g = ba.getGroup();
					if (!ButtonPromote.permissions.playerInGroup(p, g)) {
						if (plugin.getConfig().getBoolean("keepOldGroups")) {
							ButtonPromote.permissions.playerAddGroup(p, g);
						} else {
							String[] groups = ButtonPromote.permissions
									.getPlayerGroups(p);
							if (groups.length != 0) {
								for (String old : groups) {
									ButtonPromote.permissions
											.playerRemoveGroup(p, old);
								}
							}
							ButtonPromote.permissions.playerAddGroup(p, g);
						}
						p.sendMessage(ChatColor.GREEN
								+ "You are now a member of " + g + "!");
					} else {
						p.sendMessage(ChatColor.GREEN
								+ "You have already been promoted.");
					}
				}

				// Get Commands
				if (ba.hasCommand()) {
					String[] cmd = ba.getCommand().split("-");
					String s = null;
					for (int i = 0; i < cmd.length; i++) {
						s = plugin.argument(cmd[i],
								new String[] { "%p", "%w" },
								new String[] { p.getName(),
										p.getWorld().getName() });
						if (plugin.getConfig().getBoolean("consoleCommands")) {
							plugin.getServer().dispatchCommand(
									plugin.getServer().getConsoleSender(), s);
						} else {
							p.performCommand(s);
						}
					}
				}

				// Get Messages
				if (ba.hasMessage()) {
					String[] msg = ba.getMessage().split("-");
					for (int i = 0; i < msg.length; i++)
						p.sendMessage(ChatColor.translateAlternateColorCodes(
								'&', msg[i]));
				}

				// Get Warps
				if (ba.hasWarp()) {
					plugin.getServer().getScheduler()
							.scheduleSyncDelayedTask(plugin, new Runnable() {
								public void run() {
									Player[] players = plugin.getServer()
											.getOnlinePlayers();
									for (Player player : players) {
										if (player.equals(p))
											player.teleport(ba.getWarp());
									}
								}
							}, plugin.getConfig().getInt("warpTimer") * 20L);
				}

				// If button is one time use add player to user table
				if (ba.hasOneTimeUse())
					ba.setUsed(p.getName(), ba.getID());
			}
		}
	}

	/*
	@EventHandler
	public void onButtonBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		Player p = event.getPlayer();

		switch (b.getType()) {
		case STONE_PLATE:
			break;

		case WOOD_PLATE:
			break;
			
		case GOLD_PLATE:
			break;
			
		case IRON_PLATE:
			break;

		case STONE_BUTTON:
			break;

		case WOOD_BUTTON:
			break;

		default:
			return;
		}

		ButtonApi ba = new ButtonApi(plugin, b.getWorld().getName(), b.getX(),
				b.getY(), b.getZ());
		if (ba != null) {
			if (ButtonPromote.permissions.has(p, "ButtonPromote.remove")) {
				ButtonPromote.buttonRemoval.put(p.getName(), p.getLocation());
				p.sendMessage("This button has features tied to it, to remove type /bp confirm or type /bp cancel and replace the button to keep.");
			}
		}
	}
	*/
}
