package uk.co.crazyfools.instagib;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;




public class EntityEvents implements Listener {
	
	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
		if(e.getEntityType() == EntityType.ARROW) {
			if(e.getEntity().getShooter() instanceof Player) {
				Player p = (Player) e.getEntity().getShooter();
				if(p.getInventory().getItemInMainHand() != null) {
					if(p.getInventory().getItemInMainHand().getType() == Material.CROSSBOW) {
						if(isInstagibCrossbow(p.getInventory().getItemInMainHand()) && hasShockArrow(p) && p.hasPermission("CFUK.Instagib")) {
									
						e.setCancelled(true);
						
						Location beamEnd = null;
						
						if(p.getTargetEntity(64) != null) {
							beamEnd = p.getTargetEntity(64).getLocation();
							if(p.getTargetEntity(64) instanceof LivingEntity) {
							LivingEntity le = (LivingEntity) p.getTargetEntity(64);
							if(!isBlacklistedMob(le.getType())) {
								le.setKiller(p);
								if(le instanceof Player) {
									// Check if le is wearing helmet of redemption
									Player targetP = (Player) le;
									if(targetP.getInventory().getHelmet() != null) {
										// Is wearing a helmet
										ItemStack helmet = targetP.getInventory().getHelmet();
										if(isRedemptionHelmet(helmet)) {
											redemptionTeleport(targetP);
											p.sendMessage(Component.text(">" + targetP + " was saved by their undying helmet!").color(TextColor.color(0, 255, 0)));
										}
										
									} else {
										le.setHealth(0);
										p.getServer().broadcast(Component.text(p.getName() + " fried " + targetP.getName() + " with a super shock crossbow."));
									}
								} else {
								le.setHealth(0);
								}
								bloodSplatter(beamEnd);
							}
							p.getWorld().createExplosion(p.getTargetEntity(64).getLocation(), 2, false, false);
							}
						} else if(p.getTargetBlock(64) != null) {
							p.getWorld().createExplosion(p.getTargetBlock(64).getLocation(), 1, true, true);
							beamEnd = p.getTargetBlock(64).getLocation();
							beamSplatter(beamEnd);
						}
						
						flash(e.getEntity().getLocation());
						drawLine(e.getEntity().getLocation(), beamEnd, 0.25);

					}
				}
				}
			}
		}
	}
	
	private void redemptionTeleport(Player targetP) {
		// TODO Auto-generated method stub
		if(targetP.getBedSpawnLocation() != null) {
			targetP.teleportAsync(targetP.getBedSpawnLocation());
		} else {
			targetP.teleportAsync(targetP.getWorld().getSpawnLocation());
		}
		
	}

	private boolean isRedemptionHelmet(ItemStack helmet) {
		// TODO Auto-generated method stub
		if(helmet.getType() == Material.GOLDEN_HELMET) {
			ItemMeta im = helmet.getItemMeta();
			if(im.getPersistentDataContainer().has(new NamespacedKey(Index.plugin, "redemption-helmet"), PersistentDataType.INTEGER)) {
				Damageable gHelmet = (Damageable) helmet.getItemMeta();
				gHelmet.setDamage(gHelmet.getDamage() + 1);
				return true;
			}
		}
		return false;
	}

	private boolean hasShockArrow(Player p) {
		CrossbowMeta cim = (CrossbowMeta) p.getInventory().getItemInMainHand().getItemMeta();
		for(ItemStack i : cim.getChargedProjectiles()) {
			if(i.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Index.plugin, "super-shock-arrow"), PersistentDataType.INTEGER)) {
				return true;
			}
		}
		return false;
	}
	

	private boolean isBlacklistedMob(EntityType t) {
		if(t == EntityType.ENDER_DRAGON || t == EntityType.WITHER) {
			return true;
		} 
		return false;
	}
	
	public static boolean isInstagibCrossbow(@NotNull ItemStack itemInMainHand) {
		NamespacedKey key = new NamespacedKey(Index.plugin, "instagib-bow");
		if(itemInMainHand.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
			return true;
		} else {
			return false;
		}

	}

	public void bloodSplatter(Location point) {
		point.getWorld().spawnParticle(Particle.REDSTONE, point.getX(), point.getY(), point.getZ(), 500, 1, 1, 1, new DustOptions(Color.RED, 1));
		Random rnd = new Random();
		int drops = rnd.nextInt(3);
		if(drops > 0) {
		point.getWorld().dropItemNaturally(point, Giblets(drops));
		}
		drops = rnd.nextInt(3);
		if(drops > 0) {
		point.getWorld().dropItemNaturally(point, Bones(drops));
		}
	}
	
	public void beamSplatter(Location point) {
		point.getWorld().spawnParticle(Particle.REDSTONE, point.getX(), point.getY(), point.getZ(), 100, 1, 1, 1, new DustOptions(Color.ORANGE, 1));
	}
	
	public ItemStack Giblets(Integer drops) {
		ItemStack i = new ItemStack(Material.RED_DYE, drops);
		return i;
	}
	
	public ItemStack Bones(Integer drops) {
		ItemStack i = new ItemStack(Material.BONE, drops);
		return i;
	}
	
	public void flash(Location point1) {
		World world = point1.getWorld();
		world.spawnParticle(Particle.FLASH, point1, 1);
	}
	
	public void drawLine(Location point1, Location point2, double space) {
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double length = 0;
	    for (; length < distance; p1.add(vector)) {
	        world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 3, new DustOptions(Color.ORANGE, 1));
	        length += space;
	    }
	}
}
