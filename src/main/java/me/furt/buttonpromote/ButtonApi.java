package me.furt.buttonpromote;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonApi {

	private int x;
	private int y;
	private int z;
	private ButtonPromote plugin;
	private String world;

	/**
	 * 
	 * @param instance
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public ButtonApi(ButtonPromote instance, String world, int x, int y, int z) {
		this.plugin = instance;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Checks to see if the button data exists if not it creates it.
	 */
	public void checkButton() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt == null) {
			bt = new ButtonTable();
			bt.setX(x);
			bt.setY(y);
			bt.setZ(z);
			bt.setWorld(world);
			bt.setPermission("none");
			if (plugin.getConfig().getBoolean("globalOneTimeUse")) {
				bt.setOneTimeUse(true);
			} else {
				bt.setOneTimeUse(false);
			}
			bt.setCurrency(0);
			bt.setCurrencyAction("none");
			bt.setMessage("none");
			bt.setCommand("none");
			bt.setGroupName("none");
			bt.setWarpPitch(0);
			bt.setWarpWorld("none");
			bt.setWarpX(0);
			bt.setWarpY(0);
			bt.setWarpYaw(0);
			bt.setWarpX(0);
			bt.setItem("none");
			bt.setItemDurability(0);
			bt.setItemAmount(0);
			bt.setItemAction("none");
			plugin.getDatabase().save(bt);
		}
	}

	/**
	 * Return button ID for one time use checks
	 * 
	 * @return Button data ID
	 */
	public int getID() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getId();
	}

	/**
	 * Checks to see if a player has already used a button with one time use
	 * feature
	 * 
	 * @param player
	 * @param button
	 * @return
	 */
	public boolean hasUsed(String player, int button) {
		try {
			ButtonUserTable but = plugin.getDatabase()
					.find(ButtonUserTable.class).where().ieq("name", player)
					.eq("buttonID", button).findUnique();
			if (but != null)
				return true;

			return false;
		} catch (Exception exc) {
			return false;
		}

	}

	/**
	 * Adds a player to db after using a button with the one time use feature
	 * 
	 * @param player
	 * @param button
	 */
	public void setUsed(String player, int button) {
		ButtonUserTable but = new ButtonUserTable();
		but.setName(player);
		but.setButtonID(button);
		plugin.getDatabase().save(but);
	}

	/**
	 * Checks to see if button has one time use enabled
	 * 
	 * @return boolean
	 */
	public boolean getOneTimeUse() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.isOneTimeUse();
	}

	/**
	 * Sets a button to enable or disable one time use
	 * 
	 * @param b
	 */
	public void setOneTimeUse(boolean b) {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setOneTimeUse(b);
		plugin.getDatabase().save(bt);
	}

	/**
	 * Gets message from said button
	 * 
	 * @return String
	 */
	public String getMessage() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getMessage();

		return null;
	}

	/**
	 * 
	 * @param s
	 */
	public void setMessage(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setMessage(s);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public Location getWarp() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return new Location(plugin.getServer().getWorld(bt.getWarpWorld()),
					bt.getWarpX(), bt.getWarpY(), bt.getWarpZ(),
					bt.getWarpYaw(), bt.getWarpPitch());

		return null;
	}

	/**
	 * 
	 * @param loc
	 */
	public void setWarp(Location loc) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setWarpX(loc.getX());
		bt.setWarpY(loc.getY());
		bt.setWarpZ(loc.getZ());
		bt.setWarpYaw(loc.getYaw());
		bt.setWarpPitch(loc.getPitch());
		bt.setWarpWorld(loc.getWorld().getName());
		plugin.getDatabase().save(bt);

	}

	/**
	 * 
	 * @return
	 */
	public String getGroup() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getGroupName();

		return null;
	}

	/**
	 * 
	 * @param s
	 */
	public void setGroup(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setGroupName(s);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public String getCommand() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getCommand();

		return null;
	}

	/**
	 * 
	 * @param s
	 */
	public void setCommand(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setCommand(s);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public String getPermission() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getPermission();

	}

	/**
	 * 
	 * @param s
	 */
	public void setPermission(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setPermission(s);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public ItemStack getItem() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		ItemStack i = new ItemStack(Material.getMaterial(bt.getItem()));
		i.setDurability((short) bt.getItemDurability());
		i.setAmount(bt.getItemAmount());
		return i;
	}

	/**
	 * 
	 * @param i
	 */
	public void setItem(ItemStack i) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setItem(i.getType().name());
		bt.setItemDurability(i.getDurability());
		bt.setItemAmount(i.getAmount());
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public String getItemAction() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getItemAction();
	}

	/**
	 * 
	 * @param s
	 */
	public void setItemAction(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setItemAction(s);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public int getCurrency() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getCurrency();

	}

	/**
	 * 
	 * @param i
	 */
	public void setCurrency(int i) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setCurrency(i);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public String getCurrencyAction() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getCurrencyAction();
	}

	/**
	 * 
	 * @param s
	 */
	public void setCurrencyAction(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setCurrencyAction(s);
		plugin.getDatabase().save(bt);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasCurrency() {
		try {
			int cost = this.getCurrency();
			if (cost != 0)
				return true;
		} catch (Exception exc) {
			return false;
		}
		return false;

	}

	/**
	 * 
	 * @return
	 */
	public boolean hasPermission() {
		try {
			String perm = this.getPermission();
			if (!perm.equalsIgnoreCase("none")) {
				return true;
			}
		} catch (Exception exc) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasMessage() {
		try {
			String msg = this.getMessage();
			if (!msg.equalsIgnoreCase("none")) {
				return true;
			}
		} catch (Exception exc) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasWarp() {
		try {
			Location loc = this.getWarp();
			if (!loc.getWorld().getName().equalsIgnoreCase("none")) {
				return true;
			}
		} catch (Exception exc) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasGroup() {
		try {
			String grp = this.getGroup();
			if (!grp.equalsIgnoreCase("none"))
				return true;
		} catch (Exception exc) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasCommand() {
		try {
			String cmd = this.getCommand();
			if (!cmd.equalsIgnoreCase("none"))
				return true;
		} catch (Exception exc) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasItem() {
		try {
			ItemStack iName = this.getItem();
			if (iName.getAmount() != 0) {
				return true;
			}
		} catch (Exception exc) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasOneTimeUse() {
		try {
			boolean b = this.getOneTimeUse();
			return b;

		} catch (Exception exc) {
			return false;
		}
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean clearButton(Player p) {
		try {
			ButtonTable bt = plugin.getDatabase().find(ButtonTable.class)
					.where().ieq("world", world).eq("x", x).eq("y", y)
					.eq("z", z).findUnique();
			plugin.getDatabase().delete(bt);
			return true;
		} catch (Exception exc) {
			p.sendMessage(ChatColor.RED + "Could not delete button.");
			return false;
		}
	}
}
