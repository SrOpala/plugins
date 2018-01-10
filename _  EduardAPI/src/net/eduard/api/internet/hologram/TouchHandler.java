package net.eduard.api.internet.hologram;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

public interface TouchHandler {

	public void onTouch(@Nonnull Hologram hologram, @Nonnull Player player, @Nonnull TouchAction action);

}
