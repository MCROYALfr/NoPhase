package fr.hikings.nophase.entities;

public final class Tile {

	public Tile(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	private final int x;

	private int y;

	private final int z;

	private boolean passable = true;

	private double g = Double.MAX_VALUE;

	private double h;

	private Tile parent;

	public double getF() {
		return h + g;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(final boolean passable) {
		this.passable = passable;
	}

	public double getG() {
		return g;
	}

	public void setG(final double g) {
		this.g = g;
	}

	public void setH(final double h) {
		this.h = h;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(final Tile parent) {
		this.parent = parent;
	}

}
