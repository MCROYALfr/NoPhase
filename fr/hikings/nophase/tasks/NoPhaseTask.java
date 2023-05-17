package fr.hikings.nophase.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.hikings.nophase.Main;
import fr.hikings.nophase.entities.Tile;
import fr.hikings.nophase.entities.User;
import fr.hikings.nophase.utils.MaterialUtil;
import fr.hikings.nophase.utils.PathUtil;

public final class NoPhaseTask implements Runnable {
	private final Main plugin;

	public NoPhaseTask(final Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			final User user = plugin.getOrCreateUser(player.getUniqueId());

			if (checkState(player, user)) {
				continue;
			}

			final Location previous = user.getValidLocation();
			final Location current = player.getLocation();

			if (previous.distanceSquared(current) > 256) {
				player.teleport(previous);
				continue;
			}

			if (checkMovement(previous, current, player.isInsideVehicle())) {
				user.setValidLocation(current);
				continue;
			}

			player.teleport(previous);
			user.setTeleported(false);
		}
	}

	private boolean checkState(final Player player, final User user) {
		if (player.isFlying()) {
			user.setValidLocation(null);
			return true;
		}

		if (user.isTeleported()) {
			user.setValidLocation(null);
			user.setTeleported(false);
			return true;
		}

		final Location prev = user.getValidLocation();
		final Location cur = player.getLocation();

		if (prev == null) {
			user.setValidLocation(cur);
			return true;
		}

		if (prev.getWorld() != cur.getWorld()) {
			user.setValidLocation(null);
			return true;
		}

		if (cur.getBlockX() == prev.getBlockX() && cur.getBlockY() == prev.getBlockY() && cur.getBlockZ() == prev.getBlockZ()) {
			return true;
		}

		return false;
	}

	private boolean checkMovement(final Location previous, final Location current, final boolean vehicle) {
		final int moveMinX = Math.min(previous.getBlockX(), current.getBlockX());
		final int moveMaxX = Math.max(previous.getBlockX(), current.getBlockX());
		int moveMinY = Math.min(256, Math.min(previous.getBlockY(), current.getBlockY()));
		final int moveMaxY = Math.min(256, Math.max(previous.getBlockY(), current.getBlockY()) + 1);
		final int moveMinZ = Math.min(previous.getBlockZ(), current.getBlockZ());
		final int moveMaxZ = Math.max(previous.getBlockZ(), current.getBlockZ());

		if (vehicle && moveMinY != 256) {
			moveMinY++;
		}

		boolean passable = true;
		final Tile[][][] movement = new Tile[moveMaxX - moveMinX + 1][moveMaxY - moveMinY + 1][moveMaxZ - moveMinZ + 1];
		for (int x = moveMinX; x <= moveMaxX; x++) {
			for (int z = moveMinZ; z <= moveMaxZ; z++) {
				for (int y = moveMinY; y <= moveMaxY; y++) {
					final int diffX = moveMaxX - x;
					final int diffY = moveMaxY - y;
					final int diffZ = moveMaxZ - z;
					movement[diffX][diffY][diffZ] = new Tile(diffX, diffY, diffZ);

					final Block block = previous.getWorld().getBlockAt(x, y, z);
					if (!MaterialUtil.isFullBlock(block.getType())) {
						continue;
					}

					if (isPassable(block, previous, current)) {
						passable = false;
						movement[diffX][diffY][diffZ].setPassable(false);
					}
				}
			}
		}

		return passable || PathUtil.hasPath(movement, movement[moveMaxX - previous.getBlockX()][Math.max(0, moveMaxY - previous.getBlockY() - 1)][moveMaxZ - previous.getBlockZ()],
				movement[moveMaxX - current.getBlockX()][Math.max(0, moveMaxY - current.getBlockY() - 1)][moveMaxZ - current.getBlockZ()]);
	}

	private boolean isPassable(final Block block, final Location previous, final Location current) {
		final double moveMaxX = Math.max(previous.getX(), current.getX());
		final double moveMinX = Math.min(previous.getX(), current.getX());
		final double moveMaxY = Math.max(previous.getY(), current.getY()) + 1.8;
		final double moveMinY = Math.min(previous.getY(), current.getY());
		final double moveMaxZ = Math.max(previous.getZ(), current.getZ());
		final double moveMinZ = Math.min(previous.getZ(), current.getZ());

		final double blockMaxX = block.getLocation().getBlockX() + 1;
		final double blockMinX = block.getLocation().getBlockX();
		final double blockMaxY = block.getLocation().getBlockY() + 2;
		final double blockMinY = block.getLocation().getBlockY();
		final double blockMaxZ = block.getLocation().getBlockZ() + 1;
		final double blockMinZ = block.getLocation().getBlockZ();

		final boolean x = previous.getX() < current.getX();
		final boolean y = previous.getY() < current.getY();
		final boolean z = previous.getZ() < current.getZ();

		return moveMinX != moveMaxX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ
				&& (x && moveMinX <= blockMinX && moveMaxX >= blockMinX || !x && moveMinX <= blockMaxX && moveMaxX >= blockMaxX) ||

				moveMinY != moveMaxY && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ
				&& (y && moveMinY <= blockMinY && moveMaxY >= blockMinY || !y && moveMinY <= blockMaxY && moveMaxY >= blockMaxY)
				||

				moveMinZ != moveMaxZ && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinY <= blockMaxY && moveMaxY >= blockMinY
				&& (z && moveMinZ <= blockMinZ && moveMaxZ >= blockMinZ || !z && moveMinZ <= blockMaxZ && moveMaxZ >= blockMaxZ);
	}

}
