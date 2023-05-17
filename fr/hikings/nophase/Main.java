package fr.hikings.nophase;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import fr.hikings.nophase.entities.User;
import fr.hikings.nophase.listeners.TeleportedListener;
import fr.hikings.nophase.tasks.NoPhaseTask;

public final class Main extends JavaPlugin {
	private final HashMap<UUID, User> users = new HashMap<>();

	public User getOrCreateUser(final UUID playerId) {
		User user = users.get(playerId);
		if (user == null) {
			user = new User();
			users.put(playerId, user);
		}
		return user;
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new TeleportedListener(this), this);
		getServer().getScheduler().runTaskTimer(this, new NoPhaseTask(this), 1L, 1L);
	}
}
