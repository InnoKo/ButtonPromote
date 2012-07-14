package me.iMint.ButtonPromote;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ButtonApi {

	private int x;
	private int y;
	private int z;
	private ButtonPromote plugin;
	private String world;

	public ButtonApi(ButtonPromote instance, String world, int x, int y, int z) {
		this.plugin = instance;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

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
			bt.setOneTimeUse(false);
			bt.setCost(0);
			bt.setMessage("none");
			bt.setCommand("none");
			bt.setGroupName("none");
			bt.setWarpPitch(0);
			bt.setWarpWorld("none");
			bt.setWarpX(0);
			bt.setWarpY(0);
			bt.setWarpYaw(0);
			bt.setWarpX(0);
			bt.setItem(0);
			bt.setItemDurability(0);
			bt.setItemAmount(0);
			bt.setItemAction("none");
			plugin.getDatabase().save(bt);
		}
	}

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

	public void setUsed(String player, int button) {
		ButtonUserTable but = new ButtonUserTable();
		but.setName(player);
		but.setButtonID(button);
		plugin.getDatabase().save(but);
	}

	public boolean getOneTimeUse() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.isOneTimeUse();
	}

	public void setOneTimeUse(boolean b) {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setOneTimeUse(b);
		plugin.getDatabase().save(bt);
	}

	public String getMessage() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getMessage();

		return null;
	}

	public void setMessage(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setMessage(s);
		plugin.getDatabase().save(bt);
	}

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

	public String getGroup() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getGroupName();

		return null;
	}

	public void setGroup(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setGroupName(s);
		plugin.getDatabase().save(bt);
	}

	public String getCommand() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getCommand();

		return null;
	}

	public void setCommand(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setCommand(s);
		plugin.getDatabase().save(bt);
	}

	public String getPermission() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getPermission();

	}

	public void setPermission(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setPermission(s);
		plugin.getDatabase().save(bt);
	}

	public ItemStack getItem() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		ItemStack i = new ItemStack(bt.getItem());
		i.setDurability((short) bt.getItemDurability());
		i.setAmount(bt.getItemAmount());
		return i;
	}

	public void setItem(ItemStack i) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setItem(i.getTypeId());
		bt.setItemDurability(i.getDurability());
		bt.setItemAmount(i.getAmount());
		plugin.getDatabase().save(bt);
	}

	public String getItemAction() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getItemAction();
	}

	public void setItemAction(String s) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setItemAction(s);
		plugin.getDatabase().save(bt);
	}

	public int getCost() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		return bt.getCost();

	}

	public void setCost(int i) {
		this.checkButton();
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		bt.setCost(i);
		plugin.getDatabase().save(bt);
	}

	public boolean hasCost() {
		try {
			int cost = this.getCost();
			if (cost != 0)
				return true;
		} catch (Exception exc) {
			return false;
		}
		return false;

	}

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

	public void clearButton() {
		try {
			ButtonTable bt = plugin.getDatabase().find(ButtonTable.class)
					.where().ieq("world", world).eq("x", x).eq("y", y)
					.eq("z", z).findUnique();
			plugin.getDatabase().delete(bt);
		} catch (Exception exc) {
			plugin.getServer().broadcastMessage("Could not delete button.");
		}
	}
}
