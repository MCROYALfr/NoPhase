package fr.hikings.nophase.utils;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Material;

public final class MaterialUtil {
	private static final HashSet<Integer> FULL_BLOCKS = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 7, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 35, 41, 42, 43, 45, 46, 47, 48, 49, 52, 56, 57, 58, 61, 62, 73, 74, 79, 80, 82, 84, 86, 87, 89, 91, 95, 97, 98, 99, 100, 103, 110, 112, 121, 125, 129, 133, 137, 152, 153, 155, 158, 159, 161, 162, 166, 168, 169, 170, 172, 173, 174, 179, 181));

	public static boolean isFullBlock(final Material material) {
		return MaterialUtil.FULL_BLOCKS.contains(material.getId());
	}
}
