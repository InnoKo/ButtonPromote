package me.iMint.ButtonPromote;

import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ButtonTimer extends TimerTask {
	private final ButtonPromote plugin;
	private Player player;
	private Location location;

	public ButtonTimer(ButtonPromote instance, Player p, Location loc) {
		this.plugin = instance;
		this.player = p;
		this.location = loc;
	}

	@Override
	public void run() {
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (Player p : players) {
		if(p.equals(player))
			player.teleport(location);
		}
	}
	
}
