package uk.co.crazyfools.instagib;


import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class PlayerEvents implements Listener {
	
	@EventHandler
	public void onPlayerReadyArrowEvent(PlayerReadyArrowEvent e) {
		if(EntityEvents.isInstagibCrossbow(e.getBow())) {
			if(!isShockArrowLoaded(e.getArrow())) {
				// A non shock arrow in a shock crossbow
				e.getPlayer().sendMessage(Component.text("[A52-HUD] Incompatible ammo loaded.").color(TextColor.color(255, 0, 0)));
			}
		} else {
			if(isShockArrowLoaded(e.getArrow())) {
				e.getPlayer().sendMessage(Component.text("> Huh? This weapon does not appear to work with the shock arrow.").color(TextColor.color(255, 0, 0)));
			}
		}
	}
	
	private boolean isShockArrowLoaded(ItemStack i) {
		//for(ItemStack i : list) {
			if(i.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Index.plugin, "super-shock-arrow"), PersistentDataType.INTEGER)) {
				return true;	
			}
		//}
		return false;
	}

}
