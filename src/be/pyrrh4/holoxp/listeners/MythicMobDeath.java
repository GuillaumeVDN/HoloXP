package be.pyrrh4.holoxp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import be.pyrrh4.holoxp.HoloXP;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;

public class MythicMobDeath implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void event(MythicMobDeathEvent event) {
		if (event.getKiller() instanceof Player) {
			HoloXP.inst().addHologram((Player) event.getKiller(), event.getEntity().getLocation(),
					event.getMobType().getDisplayName(), event.getKiller().getName(), event.getExp());
		}
	}

}
