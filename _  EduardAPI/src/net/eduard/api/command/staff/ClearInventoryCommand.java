
package net.eduard.api.command.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.manager.CMD;
import net.eduard.api.manager.ItemAPI;

public class ClearInventoryCommand extends CMD {
	public String message = "�6Seu inventario foi limpo!";
	public String messageTarget = "�6Voce limpou o inventario do �e$player";
	public ClearInventoryCommand() {
		super("clearinventory");
		
	}
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				ItemAPI.clearInventory(p);
				ConfigSection.chat(p,message);
			}else 
			return false;
		} else {
			String name = args[0];
			if (API.existsPlayer(sender, name)) {
				Player target = API.getPlayer(name);
				ConfigSection.chat(sender, messageTarget.replace("$player",
						target.getDisplayName()));
				ConfigSection.chat(target,message);
				ItemAPI.clearInventory(target);
			}
		}

		return true;
	}
}