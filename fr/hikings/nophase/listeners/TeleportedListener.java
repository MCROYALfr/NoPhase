package fr.hikings.nophase.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.hikings.nophase.Main;

public final class TeleportedListener implements Listener {
	private final Main main;

	public TeleportedListener(final Main main) {
		this.main = main;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		main.getOrCreateUser(event.getPlayer().getUniqueId()).setTeleported(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		main.getOrCreateUser(event.getPlayer().getUniqueId()).setTeleported(true);
	}
}
