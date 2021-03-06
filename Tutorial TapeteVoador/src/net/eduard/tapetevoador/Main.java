
package net.eduard.tapetevoador;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.server.EduardPlugin;
import net.eduard.tapetevoador.command.TapeteCommand;
import net.eduard.tapetevoador.event.TapeteEvents;

public class Main extends EduardPlugin {

	@Override
	public void onEnable() {

		config.add("Material", "DIAMOND_BLOCK");
		config.add("Gamemode", "CREATIVE");
		config.add("Enable", "&6Voce ativou o tapete voador!");
		config.add("Disable", "&6Voce desativou o tapete voador!");
		config.saveConfig();
		new TapeteEvents().register(this);
		new TapeteCommand().register();
		asyncTimer(new Runnable() {
			
			@Override
			public void run() {
				effect();
			}
		}, 3, 3);
	}
	private static void effect() {
		for (Player p : players) {
			if (p.isSneaking()) {
				for (Block block : getTapeteBlocks(p.getLocation())) {
					if (block.getType() == getMaterial()) {
						block.setType(Material.AIR);
					}
				}
			}
		}
	}
	public static List<Player> players = new ArrayList<>();

	public static Config config;

	public static List<Block> getTapeteBlocks(Location location) {
		List<Block> list = new ArrayList<>();
		for (Location loc : Mine.getBox(location.subtract(0, 1, 0), 0, 0, 2)) {
			list.add(loc.getBlock());
		}
		return list;
	}



	public static Material getMaterial() {
		return Material.valueOf(Main.config.getString("Material"));
	}

	public static GameMode getGamemode() {
		return GameMode.valueOf(Main.config.getString("Gamemode"));
	}

	public static void reset() {

		for (Player p : players) {
			reset(p);
		}
	}

	public static void reset(Player p) {

		for (Block block : getTapeteBlocks(p.getLocation())) {
			if (block.getType() == getMaterial()) {
				block.setType(Material.AIR);
			}
		}
	}

}
