package be.pyrrh4.holoxp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import be.pyrrh4.holoxp.listeners.EntityDeath;
import be.pyrrh4.holoxp.listeners.MythicMobDeath;

public class HoloXP extends JavaPlugin {

	// ------------------------------------------------------------
	// Instance and constructor
	// ------------------------------------------------------------

	private static HoloXP instance;

	public HoloXP() {
		instance = this;
	}

	public static HoloXP inst() {
		return instance;
	}

	// ------------------------------------------------------------
	// Fields and methods
	// ------------------------------------------------------------

	private List<Hologram> holograms = new ArrayList<Hologram>();
	private List<String> hologramLines = new ArrayList<String>();
	private long hologramDisappearTicks;

	public List<Hologram> getHolograms() {
		return holograms;
	}

	public void addHologram(Player player, Location mobLocation, String mobName, String killerName, int xp) {
		// get location
		Location base = player.getLocation();
		double sign = Math.signum(3D);
		double dist = Math.abs(3D);
		double horizontalRad = Math.toRadians(-base.getYaw());
		double x = base.getX() + sign * (dist * Math.sin(horizontalRad));
		double y = mobLocation.getY() + 1.5D;
		double z = base.getZ() + sign * (dist * Math.cos(horizontalRad));
		Location location = new Location(base.getWorld(), x, y, z);
		// create hologram
		final Hologram hologram = HologramsAPI.createHologram(this, location);
		for (String line : hologramLines) {
			hologram.appendTextLine(line.replace("{mob}", mobName).replace("{player}", killerName).replace("{xp}", "" + xp));
		}
		holograms.add(hologram);
		// unregister later
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!hologram.isDeleted()) {
					hologram.delete();
				}
			}
		}.runTaskLater(this, hologramDisappearTicks);
	}

	// ------------------------------------------------------------
	// Enable
	// ------------------------------------------------------------

	@Override
	public void onEnable() {
		// config
		saveDefaultConfig();
		this.hologramLines = getConfig().getStringList("texts.hologram");
		for (int i = 0; i < hologramLines.size(); i++) {
			hologramLines.set(i, ChatColor.translateAlternateColorCodes('&', hologramLines.get(i)));
		}
		this.hologramDisappearTicks = getConfig().getLong("hologram_disappear_ticks");

		// listeners
		Bukkit.getPluginManager().registerEvents(new EntityDeath(), this);
		if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
			Bukkit.getPluginManager().registerEvents(new MythicMobDeath(), this);
		}
	}

	// ------------------------------------------------------------
	// Disable
	// ------------------------------------------------------------

	@Override
	public void onDisable() {
		// holograms
		for (Hologram hologram : holograms) {
			hologram.delete();
		}
		holograms.clear();

		// listeners
		HandlerList.unregisterAll(this);
	}

}
