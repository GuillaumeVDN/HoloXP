package be.pyrrh4.holoxp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import be.pyrrh4.holoxp.HoloXP;

public class EntityDeath implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void event(EntityDeathEvent event) {
		if (event.getEntity().getKiller() != null) {
			HoloXP.inst().addHologram(event.getEntity().getKiller(), event.getEntity().getLocation(),
					event.getEntity().isCustomNameVisible() && event.getEntity().getCustomName() != null ? event.getEntity().getCustomName() :
						event.getEntity().getType().getName(), event.getEntity().getKiller().getName(), event.getDroppedExp());
		}
	}

}
