
package net.eduard.eduardapi.command.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.manager.CMD;

public class EconomyResetCommand extends CMD {

	public EconomyResetCommand() {
		super("reload");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length ==0) {
			
		}
		
		return true;
	}

}
