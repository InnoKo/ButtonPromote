package me.iMint.ButtonPromote;

import org.bukkit.Location;

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
	
	public String getMessage() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null)
			return bt.getMessage();

		return null;
	}
	
	public void setMessage(String s) {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null) {
			bt.setMessage(s);
			plugin.getDatabase().save(bt);
		} else {
			bt = new ButtonTable();
			bt.setMessage(s);
			bt.setCommand("none");
			bt.setGroupName("none");
			bt.setWarpPitch(0);
			bt.setWarpWorld("none");
			bt.setWarpX(0);
			bt.setWarpY(0);
			bt.setWarpYaw(0);
			bt.setWarpX(0);
			bt.setX(x);
			bt.setY(y);
			bt.setZ(z);
			bt.setWorld(world);
			plugin.getDatabase().save(bt);
		}
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
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null) {
			bt.setWarpX(loc.getX());
			bt.setWarpY(loc.getY());
			bt.setWarpZ(loc.getZ());
			bt.setWarpYaw(loc.getYaw());
			bt.setWarpPitch(loc.getPitch());
			bt.setWarpWorld(loc.getWorld().getName());
		} else {
			bt = new ButtonTable();
			bt.setWarpX(loc.getX());
			bt.setWarpY(loc.getY());
			bt.setWarpZ(loc.getZ());
			bt.setWarpYaw(loc.getYaw());
			bt.setWarpPitch(loc.getPitch());
			bt.setWarpWorld(loc.getWorld().getName());
			bt.setMessage("none");
			bt.setCommand("none");
			bt.setGroupName("none");
			bt.setX(x);
			bt.setY(y);
			bt.setZ(z);
			bt.setWorld(world);
		}
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
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null) {
			bt.setGroupName(s);
			plugin.getDatabase().save(bt);
		} else {
			bt = new ButtonTable();
			bt.setGroupName(s);
			bt.setMessage("none");
			bt.setCommand("none");
			bt.setWarpPitch(0);
			bt.setWarpWorld("none");
			bt.setWarpX(0);
			bt.setWarpY(0);
			bt.setWarpYaw(0);
			bt.setWarpX(0);
			bt.setX(x);
			bt.setY(y);
			bt.setZ(z);
			bt.setWorld(world);
			plugin.getDatabase().save(bt);
		}
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
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if (bt != null) {
			bt.setCommand(s);
			plugin.getDatabase().save(bt);
		} else {
			bt = new ButtonTable();
			bt.setCommand(s);
			bt.setMessage("none");
			bt.setGroupName("none");
			bt.setWarpPitch(0);
			bt.setWarpWorld("none");
			bt.setWarpX(0);
			bt.setWarpY(0);
			bt.setWarpYaw(0);
			bt.setWarpX(0);
			bt.setX(x);
			bt.setY(y);
			bt.setZ(z);
			bt.setWorld(world);
			plugin.getDatabase().save(bt);
		}
	}

	public void clearButton() {
		ButtonTable bt = plugin.getDatabase().find(ButtonTable.class).where()
				.ieq("world", world).eq("x", x).eq("y", y).eq("z", z)
				.findUnique();
		if(bt != null)
			plugin.getDatabase().delete(bt);
	}
}
