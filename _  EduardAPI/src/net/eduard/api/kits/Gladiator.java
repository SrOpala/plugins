package net.eduard.api.kits;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.eduard.api.gui.Click;
import net.eduard.api.gui.ClickEffect;
import net.eduard.api.gui.Kit;
import net.eduard.api.setup.GameAPI;
import net.eduard.api.setup.LocationEffect;
import net.eduard.api.setup.WorldAPI;

public class Gladiator extends Kit {
	
	public static HashMap<Player, Location> locations = new HashMap<>();
	public static HashMap<Player, Player> targets = new HashMap<>();
	public static HashMap<Player, List<Location>> arenas = new HashMap<>();
	public int high = 100;
	public int size = 10;
	public Material mat = Material.GLASS;
	public int data = 0;
	public int effectSeconds = 5;
	public Gladiator() {
		setIcon(Material.IRON_FENCE, "�fChame seus inimigos para um Duelo 1v1");
		add(Material.IRON_FENCE);
		setMessage("�6Voce esta invuneravel por 5 segundos!");
		setPrice(60 * 1000);
		setClick(new Click(Material.IRON_FENCE, new ClickEffect() {

			@Override
			public void effect(PlayerInteractEvent e) {

			}

			@Override
			public void effect(PlayerInteractEntityEvent e) {
				Player p = e.getPlayer();
				if (e.getRightClicked() instanceof Player) {
					Player target = (Player) e.getRightClicked();
					if (onCooldown(p)){
						return;
					}
					if (arenas.containsKey(target) | arenas.containsKey(p)) {
						p.sendMessage("�6Voce ja esta em Batalha!");
						return;
					}
					Location loc = p.getLocation().add(0, high, 0);
					List<Location> arena = createArena(p);
					locations.put(p, p.getLocation());
					locations.put(target, target.getLocation());
					targets.put(p, target);
					targets.put(target, p);
					arenas.put(p, arena);
					arenas.put(target, arena);
					GameAPI.makeInvunerable(p, effectSeconds);
					GameAPI.makeInvunerable(target, effectSeconds);
					p.teleport(loc.clone().add(size - 2, 1, 2 - size)
							.setDirection(p.getLocation().getDirection()));
					target.teleport(loc.clone().add(2 - size, 1, size - 2)
							.setDirection(target.getLocation().getDirection()));
					sendMessage(p);
					sendMessage(target);

				}
			}

		}));
		
	}
	public List<Location> createArena(Player p){
		List<Location> locs = WorldAPI.getBox(p.getLocation().add(0, 100, 0), size, size, size, new LocationEffect() {
			
			@SuppressWarnings("deprecation")
			@Override
			public boolean effect(Location location) {
				location.getBlock().setType(mat);
				location.getBlock().setData((byte) data);
				return true;
			}
		});
		WorldAPI.getBox(p.getLocation().add(0, 100, 0), size-1, size-1, size-1, new LocationEffect() {
			
			@Override
			public boolean effect(Location location) {
				location.getBlock().setType(Material.AIR);
				return false;
			}
		});
		return locs;
	}

	@EventHandler
	public void event(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (arenas.containsKey(p)) {
			if (e.getBlock().getType() == mat) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void event(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (arenas.containsKey(p)) {
			win(targets.get(p), p);
		}
	}

	@EventHandler
	public void event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (arenas.containsKey(p)) {
			win(targets.get(p), p);
		}
	}
	@EventHandler
	public void event(BlockDamageEvent e){
		
	}


	public void win(Player winner, Player loser) {
		winner.sendMessage("�6Voce venceu a batalha!");

		List<Location> arena = arenas.get(winner);
		for (Location location : arena) {
			location.getBlock().setType(Material.AIR);
		}
		loser.sendMessage("�6Voce perdeu a batalha!");
		winner.teleport(locations.get(winner)
				.setDirection(winner.getLocation().getDirection()));
		loser.teleport(locations.get(loser)
				.setDirection(loser.getLocation().getDirection()));
		targets.remove(winner);
		targets.remove(loser);
		arenas.remove(winner);
		arenas.remove(loser);
		locations.remove(winner);
		locations.remove(loser);
	}
}
