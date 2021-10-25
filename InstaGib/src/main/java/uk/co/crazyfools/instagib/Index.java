package uk.co.crazyfools.instagib;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class Index extends JavaPlugin {

	static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		getServer().getConsoleSender().sendMessage("[CFUK] Starting up InstaGib " + plugin.getDescription().getVersion());
		getServer().getPluginManager().registerEvents(new EntityEvents(), plugin);
		getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
		addCraftingCrossbow();
		addCraftingShockArrow();
		addRedemptionHelnet();
	}
	
	public void onDisable() {
	}
	
	public ItemStack instagibCrossbow() {
		NamespacedKey key = new NamespacedKey(plugin, "instagib-bow");
		ItemStack i = new ItemStack(Material.CROSSBOW, 1);
		
		ItemMeta im = i.getItemMeta();
		TextComponent itemName = Component.text("Super Shock Crossbow");
		im.displayName(itemName);
		//im.lore().add(Component.text("Area 52 Experimental Weapon"));
		//im.lore().add(Component.text("Energises shock arrows to vaporize living targets"));
		im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 10);
		
		CrossbowMeta cim = (CrossbowMeta) im;
		cim.addChargedProjectile(new ItemStack(Material.ARROW, 1));
		
		i.setItemMeta(im);
		return i;
	}
	
	public ItemStack superShockArrow() {
		NamespacedKey key = new NamespacedKey(plugin, "super-shock-arrow");
		ItemStack i = new ItemStack(Material.ARROW, 1);
		ItemMeta im = i.getItemMeta();
		TextComponent itemName = Component.text("Shock Arrow");
		im.displayName(itemName);
		//im.lore().add(Component.text("A single highly energised and expensive arrow"));
		im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 10);
		i.setItemMeta(im);
		return i;
	}
	
	public ItemStack redemptionHelmet() {
		NamespacedKey key = new NamespacedKey(plugin, "redemption-helmet");
		ItemStack i = new ItemStack(Material.GOLDEN_HELMET, 1);
		ItemMeta im = i.getItemMeta();
		TextComponent itemName = Component.text("Redemption Helmet");
		im.displayName(itemName);
		im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 10);
		i.setItemMeta(im);
		return i;
	}
	
	public void addRedemptionHelnet() {
		NamespacedKey key = new NamespacedKey(plugin, "redemption-recipe");
		ShapedRecipe recipeHelmet = new ShapedRecipe(key, redemptionHelmet());
		recipeHelmet.shape("GTG","GAG","AAA");
		recipeHelmet.setIngredient('G', Material.GOLD_INGOT);
		recipeHelmet.setIngredient('T', Material.TOTEM_OF_UNDYING);
		recipeHelmet.setIngredient('A', Material.AIR);
		getServer().addRecipe(recipeHelmet);
	}
	
	public void addCraftingCrossbow() {
		NamespacedKey key = new NamespacedKey(plugin, "instagib-bow-recipe");
		ShapedRecipe recipeCrossbow = new ShapedRecipe(key, instagibCrossbow());
		recipeCrossbow.shape("NXN","SRS","ADA");
		recipeCrossbow.setIngredient('N', Material.NETHERITE_INGOT);
		recipeCrossbow.setIngredient('X', Material.NETHER_STAR);
		recipeCrossbow.setIngredient('S', Material.STRING);
		recipeCrossbow.setIngredient('R', Material.CONDUIT);
		recipeCrossbow.setIngredient('D', Material.DRAGON_BREATH);
		recipeCrossbow.setIngredient('A', Material.AIR);
		
		getServer().addRecipe(recipeCrossbow);
	}
	
	public void addCraftingShockArrow() {
		NamespacedKey key = new NamespacedKey(plugin, "shock-arrow-recipe");
		ShapedRecipe recipeArrow = new ShapedRecipe(key, superShockArrow());
		recipeArrow.shape("ADA", "RBR", "AFA");
		recipeArrow.setIngredient('A', Material.AIR);
		recipeArrow.setIngredient('D', Material.DIAMOND);
		recipeArrow.setIngredient('R', Material.REDSTONE);
		recipeArrow.setIngredient('F', Material.GLOWSTONE_DUST);
		recipeArrow.setIngredient('B', Material.BLAZE_ROD);

		getServer().addRecipe(recipeArrow);
	}
}
