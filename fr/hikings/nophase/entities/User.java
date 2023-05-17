package fr.hikings.nophase.entities;

import org.bukkit.Location;

public final class User {
	private boolean teleported;
	private Location validLocation;

	public boolean isTeleported() {
		return teleported;
	}

	public void setTeleported(final boolean teleported) {
		this.teleported = teleported;
	}

	public Location getValidLocation() {
		return validLocation;
	}

	public void setValidLocation(final Location validLocation) {
		this.validLocation = validLocation;
	}
}
