package fr.hikings.nophase.utils;

import java.util.ArrayList;

import fr.hikings.nophase.entities.Tile;

public final class PathUtil {
	private static int[][] ADJACENT = { { -1, 0, 0 }, { 0, -1, 0 }, { 0, 0, -1 }, { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
	private final Tile[][][] area;
	private final Tile start;
	private final Tile end;
	private final ArrayList<Tile> open = new ArrayList<>();
	private final ArrayList<Tile> closed = new ArrayList<>();

	public PathUtil(final Tile[][][] area, final Tile start, final Tile end) {
		this.area = area;
		this.start = start;
		this.end = end;
	}

	public static boolean hasPath(final Tile[][][] area, final Tile start, final Tile end) {
		return new PathUtil(area, start, end).process();
	}

	private boolean process() {
		for (int x = 0; x < area.length; x++) {
			for (int y = 0; y < area[x].length; y++) {
				for (int z = 0; z < area[x][y].length; z++) {
					area[x][y][z].setH(Math.abs(end.getX() - x) + Math.abs(end.getY() - y) + Math.abs(end.getZ() - z));
				}
			}
		}
		Tile current;
		open.add(start);
		start.setG(0);
		while (!closed.contains(end)) {
			current = getNextTile();
			if (current == null) {
				return false;
			}
			processAdjacentTiles(current);
		}
		return true;
	}

	private Tile getNextTile() {
		double f = Double.MAX_VALUE;
		Tile next = null;
		for (final Tile tile : open) {
			if (tile.getF() < f || f == Double.MAX_VALUE) {
				f = tile.getF();
				next = tile;
			}
		}
		if (next == null) {
			return null;
		}
		open.remove(next);
		closed.add(next);
		return next;
	}

	private void processAdjacentTiles(final Tile base) {
		for (final int[] modifier : PathUtil.ADJACENT) {
			final int x = base.getX() + modifier[0];
			final int y = base.getY() + modifier[1];
			final int z = base.getZ() + modifier[2];

			if (x < 0 || y < 0 || z < 0 || area.length <= x || area[x].length <= y + 1 || area[x][y].length <= z) {
				continue;
			}
			final Tile current = area[x][y][z];

			if (!current.isPassable() || !area[x][y + 1][z].isPassable()) {
				continue;
			}
			if (current.getG() > base.getG() + 1) {
				current.setG(base.getG() + 1);
				current.setParent(base);
				open.add(current);
			}
		}
	}
}
