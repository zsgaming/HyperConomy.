package regalowl.hyperconomy.bukkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.display.SignType;
import regalowl.hyperconomy.serializable.SerializableBookMeta;
import regalowl.hyperconomy.serializable.SerializableColor;
import regalowl.hyperconomy.serializable.SerializableEnchantment;
import regalowl.hyperconomy.serializable.SerializableEnchantmentStorageMeta;
import regalowl.hyperconomy.serializable.SerializableFireworkEffect;
import regalowl.hyperconomy.serializable.SerializableFireworkEffectMeta;
import regalowl.hyperconomy.serializable.SerializableFireworkMeta;
import regalowl.hyperconomy.serializable.SerializableInventory;
import regalowl.hyperconomy.serializable.SerializableInventoryType;
import regalowl.hyperconomy.serializable.SerializableItemMeta;
import regalowl.hyperconomy.serializable.SerializableItemStack;
import regalowl.hyperconomy.serializable.SerializableLeatherArmorMeta;
import regalowl.hyperconomy.serializable.SerializableMapMeta;
import regalowl.hyperconomy.serializable.SerializablePotionEffect;
import regalowl.hyperconomy.serializable.SerializablePotionMeta;
import regalowl.hyperconomy.serializable.SerializableSkullMeta;
import regalowl.hyperconomy.util.HBlock;
import regalowl.hyperconomy.util.HItem;
import regalowl.hyperconomy.util.SimpleLocation;

public class BukkitCommon {

	protected static final BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};
	protected static final BlockFace[] allFaces = {BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.DOWN, BlockFace.UP};
	
	private BukkitCommon() {}
	
	
	
	protected static Location getLocation(SimpleLocation l) {
		return new Location(Bukkit.getWorld(l.getWorld()), l.getX(), l.getY(), l.getZ());
	}

	protected static SimpleLocation getLocation(Location l) {
		return new SimpleLocation(l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
	}
	
	protected static Block getBlock(SimpleLocation location) {
		Location l = getLocation(location);
		return l.getBlock();
	}
	
	protected static HBlock getBlock(Block b) {
		SimpleLocation l = getLocation(b.getLocation());
		return new HBlock(l);
	}
	
	protected static Sign getSign(SimpleLocation l) {
		BlockState bs = getBlock(l).getState();
		if (bs instanceof Sign) {
			return (Sign)bs;
		}
		return null;
	}
	

	protected static boolean isTransactionSign(SimpleLocation l) {
		Block b = getBlock(l);
		if (b != null && b.getType().equals(Material.SIGN_POST) || b != null && b.getType().equals(Material.WALL_SIGN)) {
			Sign s = (Sign) b.getState();
			String line3 = ChatColor.stripColor(s.getLine(2)).trim();
			if (line3.equalsIgnoreCase("[sell:buy]") || line3.equalsIgnoreCase("[sell]") || line3.equalsIgnoreCase("[buy]")) {
				return true;
			}
		}
		return false;
	}

	protected static boolean isInfoSign(SimpleLocation l) {
		Block b = getBlock(l);
		if (b != null && b.getType().equals(Material.SIGN_POST) || b != null && b.getType().equals(Material.WALL_SIGN)) {
			Sign s = (Sign) b.getState();
			String type = ChatColor.stripColor(s.getLine(2)).trim().replace(":", "").replace(" ", "");
			if (SignType.isSignType(type)) return true;
		}
		return false;
	}

	protected static boolean isChestShopSign(SimpleLocation l) {
		Block b = getBlock(l);
		if (b == null) return false;
		if (b.getType().equals(Material.WALL_SIGN)) {
			Sign s = (Sign) b.getState();
			String line2 = s.getLine(1).trim();
			if (line2.equalsIgnoreCase(ChatColor.AQUA + "[Trade]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Buy]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Sell]")) {
				BlockState chestblock = Bukkit.getWorld(s.getBlock().getWorld().getName()).getBlockAt(s.getX(), s.getY() - 1, s.getZ()).getState();
				if (chestblock instanceof Chest) {
					s.update();
					return true;
				}
			}
		} else {
			for (BlockFace cface : faces) {
				Block relative = b.getRelative(cface);
				if (relative.getType().equals(Material.WALL_SIGN)) {
					Sign s = (Sign) relative.getState();
					String line2 = s.getLine(1).trim();
					if (line2.equalsIgnoreCase(ChatColor.AQUA + "[Trade]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Buy]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Sell]")) {
						org.bukkit.material.Sign sign = (org.bukkit.material.Sign) relative.getState().getData();
						BlockFace attachedface = sign.getFacing();
						if (relative.getRelative(attachedface.getOppositeFace()).equals(b)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected static Sign getChestShopSign(SimpleLocation l) {
		Block b = getBlock(l);
		if (b == null) {
			return null;
		}
		if (b.getState() instanceof Chest) {
			Chest chest = (Chest) b.getState();
			String world = chest.getBlock().getWorld().getName();
			BlockState signblock = Bukkit.getWorld(world).getBlockAt(chest.getX(), chest.getY() + 1, chest.getZ()).getState();
			if (signblock instanceof Sign) {
				Sign s = (Sign) signblock;
				String line2 = ChatColor.stripColor(s.getLine(1)).trim();
				if (line2.equalsIgnoreCase("[Trade]") || line2.equalsIgnoreCase("[Buy]") || line2.equalsIgnoreCase("[Sell]")) {
					return s;
				}
			}
		} else if (b.getType().equals(Material.WALL_SIGN)) {
			Sign s = (Sign) b.getState();
			String line2 = s.getLine(1).trim();
			if (line2.equalsIgnoreCase(ChatColor.AQUA + "[Trade]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Buy]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Sell]")) {
				BlockState chestblock = Bukkit.getWorld(s.getBlock().getWorld().getName()).getBlockAt(s.getX(), s.getY() - 1, s.getZ()).getState();
				if (chestblock instanceof Chest) {
					s.update();
					return s;
				}
			}
		} else {
			for (BlockFace cface : faces) {
				Block relative = b.getRelative(cface);
				if (relative.getType().equals(Material.WALL_SIGN)) {
					Sign s = (Sign) relative.getState();
					String line2 = s.getLine(1).trim();
					if (line2.equalsIgnoreCase(ChatColor.AQUA + "[Trade]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Buy]") || line2.equalsIgnoreCase(ChatColor.AQUA + "[Sell]")) {
						org.bukkit.material.Sign sign = (org.bukkit.material.Sign) relative.getState().getData();
						BlockFace attachedface = sign.getFacing();
						if (relative.getRelative(attachedface.getOppositeFace()).equals(b)) {
							return s;
						}
					}
				}
			}
		}
		return null;
	}

	protected static boolean isChestShop(SimpleLocation l, boolean includeSign) {
		Block b = getBlock(l);
		if (b == null) {
			return false;
		}
		if (b.getState() instanceof Chest) {
			Chest chest = (Chest) b.getState();
			String world = chest.getBlock().getWorld().getName();
			BlockState signblock = Bukkit.getWorld(world).getBlockAt(chest.getX(), chest.getY() + 1, chest.getZ()).getState();
			if (signblock instanceof Sign) {
				Sign s = (Sign) signblock;
				String line2 = ChatColor.stripColor(s.getLine(1)).trim();
				if (line2.equalsIgnoreCase("[Trade]") || line2.equalsIgnoreCase("[Buy]") || line2.equalsIgnoreCase("[Sell]")) {
					return true;
				}
			}
		} else {
			if (includeSign && isChestShopSign(l)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	protected static HyperPlayer getPlayer(Player p) {
		return HyperConomy.hc.getHyperPlayerManager().getHyperPlayer(p.getName());
	}
	
	@SuppressWarnings("deprecation")
	protected static Player getPlayer(HyperPlayer hp) {
		return Bukkit.getPlayer(hp.getName());
	}
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
	protected static SerializableInventory getInventory(HyperPlayer hp) {
		ArrayList<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
		Player p = Bukkit.getPlayer(hp.getName());
		Inventory i = p.getInventory();
		int size = i.getSize();
		int heldSlot = p.getInventory().getHeldItemSlot();
		for (int c = 0; c < size; c++) {
	        items.add(getSerializableItemStack(i.getItem(c)));
		}
		SerializableInventory si = new SerializableInventory(items, SerializableInventoryType.PLAYER);
		si.setOwner(hp.getName());
		si.setHeldSlot(heldSlot);
		return si;
	}

	protected static SerializableInventory getInventory(Inventory i) {
		ArrayList<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
		SerializableInventoryType type = null;
		if (i.getType() == InventoryType.PLAYER) {
			type = SerializableInventoryType.PLAYER;
		} else if (i.getType() == InventoryType.CHEST) {
			type = SerializableInventoryType.CHEST;
		}
		int size = i.getSize();
		for (int c = 0; c < size; c++) {
	        items.add(getSerializableItemStack(i.getItem(c)));
		}
		SerializableInventory si = new SerializableInventory(items, type);
		if (i.getType() == InventoryType.PLAYER) {
			if (i.getHolder() instanceof Player) {
				Player p = (Player)i.getHolder();
				si.setOwner(p.getName());
				si.setHeldSlot(p.getInventory().getHeldItemSlot());
			}
		} else if (i.getType() == InventoryType.CHEST) {
			if (i.getHolder() instanceof Chest) {
				Chest c = (Chest)i.getHolder();
				si.setLocation(getLocation(c.getLocation()));
			}
		}
		return si;
	}
	

	protected static SerializableInventory getChestInventory(SimpleLocation l) {
		Location loc = new Location(Bukkit.getWorld(l.getWorld()), l.getX(), l.getY(), l.getZ());
		if (loc.getBlock().getState() instanceof Chest) {
			Chest chest = (Chest)loc.getBlock().getState();
			Inventory i = chest.getInventory();
			ArrayList<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
			int size = i.getSize();
			for (int c = 0; c < size; c++) {
		        items.add(getSerializableItemStack(i.getItem(c)));
			}
			SerializableInventory si = new SerializableInventory(items, SerializableInventoryType.CHEST);
			si.setLocation(l);
			return si;
		}
		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	protected static void setInventory(SerializableInventory inventory) {
		if (inventory.getInventoryType() == SerializableInventoryType.PLAYER) {
			HyperPlayer hp = inventory.getHyperPlayer();
			Player p = Bukkit.getPlayer(hp.getName());
			p.getInventory().setHeldItemSlot(inventory.getHeldSlot());
			ArrayList<SerializableItemStack> currentInventory = getInventory(hp).getItems();
			ArrayList<SerializableItemStack> newInventory = inventory.getItems();
			if (currentInventory.size() != newInventory.size()) return;
			Inventory inv = p.getInventory();
			for (int i = 0; i < newInventory.size(); i++) {
				if (newInventory.get(i).equals(currentInventory.get(i))) continue;
				ItemStack is = getItemStack(newInventory.get(i));
				if (is == null) {
					inv.clear(i);
				} else {
					inv.setItem(i, is);
				}
			}
			p.updateInventory();
		} else if (inventory.getInventoryType() == SerializableInventoryType.CHEST) {
			SimpleLocation l = inventory.getLocation();
			Location loc = new Location(Bukkit.getWorld(l.getWorld()), l.getX(), l.getY(), l.getZ());
			if (loc.getBlock() instanceof Chest) {
				Chest chest = (Chest)loc.getBlock();
				ArrayList<SerializableItemStack> currentInventory = getChestInventory(l).getItems();
				ArrayList<SerializableItemStack> newInventory = inventory.getItems();
				Inventory chestInv = chest.getInventory();
				for (int i = 0; i < newInventory.size(); i++) {
					if (newInventory.get(i).equals(currentInventory.get(i))) continue;
					ItemStack is = getItemStack(newInventory.get(i));
					if (is == null) {
						chestInv.clear(i);
					} else {
						chestInv.setItem(i, is);
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	protected static SerializableItemStack getSerializableItemStack(ItemStack s) {
		if (s == null) return new SerializableItemStack();
		boolean isBlank = (s.getType() == Material.AIR) ? true:false;
        String material = s.getType().toString();
        short durability = s.getDurability();
        byte data = s.getData().getData(); 
        int amount = s.getAmount();
        int maxStackSize = s.getType().getMaxStackSize();
        int maxDurability = s.getType().getMaxDurability();
        SerializableItemStack sis = null;
        if (s.hasItemMeta()) {
        	ItemMeta im = s.getItemMeta();
            String displayName = im.getDisplayName();
            List<String> lore = im.getLore();
            List<SerializableEnchantment> enchantments = new ArrayList<SerializableEnchantment>();
            Map<Enchantment, Integer> enchants = im.getEnchants();
    		Iterator<Enchantment> it = enchants.keySet().iterator();
    		while (it.hasNext()) {
    			Enchantment e = it.next();
    			int lvl = enchants.get(e);
    			enchantments.add(new SerializableEnchantment(e.getName(), lvl));
    		}
    		SerializableItemMeta itemMeta = null;
        	if (im instanceof EnchantmentStorageMeta) {
        		EnchantmentStorageMeta sItemMeta = (EnchantmentStorageMeta)im;
        		List<SerializableEnchantment> storedEnchantments = new ArrayList<SerializableEnchantment>();
    			Map<Enchantment, Integer> stored = sItemMeta.getStoredEnchants();
    			Iterator<Enchantment> iter = stored.keySet().iterator();
    			while (iter.hasNext()) {
    				Enchantment e = iter.next();
    				int lvl = enchants.get(e);
    				storedEnchantments.add(new SerializableEnchantment(e.getName(), lvl));
    			}
        		itemMeta = new SerializableEnchantmentStorageMeta(displayName, lore, enchantments, storedEnchantments);
        	} else if (im instanceof BookMeta) {
        		BookMeta sItemMeta = (BookMeta)im;
        		itemMeta = new SerializableBookMeta(displayName, lore, enchantments, sItemMeta.getAuthor(), sItemMeta.getPages(), sItemMeta.getTitle());
        	} else if (im instanceof FireworkEffectMeta) {
        		FireworkEffectMeta sItemMeta = (FireworkEffectMeta)im;
        		FireworkEffect fe = sItemMeta.getEffect();
        		ArrayList<SerializableColor> colors = new ArrayList<SerializableColor>();
        		for (Color color:fe.getColors()) {
        			colors.add(new SerializableColor(color.getRed(), color.getGreen(), color.getBlue()));
        		}
        		ArrayList<SerializableColor> fadeColors = new ArrayList<SerializableColor>();
        		for (Color color:fe.getFadeColors()) {
        			fadeColors.add(new SerializableColor(color.getRed(), color.getGreen(), color.getBlue()));
        		}
        		SerializableFireworkEffect sfe = new SerializableFireworkEffect(colors, fadeColors, fe.getType().toString(), fe.hasFlicker(), fe.hasTrail());
        		itemMeta = new SerializableFireworkEffectMeta(displayName, lore, enchantments, sfe);
        	} else if (im instanceof FireworkMeta) {
        		FireworkMeta sItemMeta = (FireworkMeta)im;
        		ArrayList<SerializableFireworkEffect> fireworkEffects = new ArrayList<SerializableFireworkEffect>();
    			for (FireworkEffect fe:sItemMeta.getEffects()) {
	        		ArrayList<SerializableColor> colors = new ArrayList<SerializableColor>();
	        		for (Color color:fe.getColors()) {
	        			colors.add(new SerializableColor(color.getRed(), color.getGreen(), color.getBlue()));
	        		}
	        		ArrayList<SerializableColor> fadeColors = new ArrayList<SerializableColor>();
	        		for (Color color:fe.getFadeColors()) {
	        			fadeColors.add(new SerializableColor(color.getRed(), color.getGreen(), color.getBlue()));
	        		}
	        		fireworkEffects.add(new SerializableFireworkEffect(colors, fadeColors, fe.getType().toString(), fe.hasFlicker(), fe.hasTrail()));
    			}
        		itemMeta = new SerializableFireworkMeta(displayName, lore, enchantments, fireworkEffects, sItemMeta.getPower());
        	} else if (im instanceof LeatherArmorMeta) {
        		LeatherArmorMeta sItemMeta = (LeatherArmorMeta)im;
        		Color color = sItemMeta.getColor();
        		itemMeta = new SerializableLeatherArmorMeta(displayName, lore, enchantments, new SerializableColor(color.getRed(), color.getGreen(), color.getBlue()));
        	} else if (im instanceof PotionMeta) {
        		PotionMeta sItemMeta = (PotionMeta)im;
        		ArrayList<SerializablePotionEffect> potionEffects = new ArrayList<SerializablePotionEffect>();
        		for (PotionEffect pe:sItemMeta.getCustomEffects()) {
        			potionEffects.add(new SerializablePotionEffect(pe.getType().toString(), pe.getAmplifier(), pe.getDuration(), pe.isAmbient()));
        		}
        		itemMeta = new SerializablePotionMeta(displayName, lore, enchantments, potionEffects);
        	} else if (im instanceof SkullMeta) {
        		SkullMeta sItemMeta = (SkullMeta)im;
        		itemMeta = new SerializableSkullMeta(displayName, lore, enchantments, sItemMeta.getOwner());
        	} else if (im instanceof MapMeta) {
        		MapMeta sItemMeta = (MapMeta)im;
        		itemMeta = new SerializableMapMeta(displayName, lore, enchantments, sItemMeta.isScaling());
        	} else {
        		itemMeta = new SerializableItemMeta(displayName, lore, enchantments);
        	}
        	sis = new SerializableItemStack(itemMeta, material, durability, data, amount, maxStackSize, maxDurability);
        }
        sis = new SerializableItemStack(null, material, durability, data, amount, maxStackSize, maxDurability);
        if (isBlank) sis.setBlank();
        return sis;
	}
	
	@SuppressWarnings("deprecation")
	protected static ItemStack getItemStack(SerializableItemStack sis) {
		if (sis == null || sis.isBlank()) return null;
        ItemStack item = new ItemStack(Material.matchMaterial(sis.getMaterial()));
        item.setAmount(1);
        item.setDurability(sis.getDurability());
        item.getData().setData(sis.getData());
        if (sis.getItemMeta() != null) {
        	SerializableItemMeta sim = sis.getItemMeta();
        	ItemMeta itemMeta = item.getItemMeta();
        	itemMeta.setDisplayName(sim.getDisplayName());
        	itemMeta.setLore(sim.getLore());
    		for (SerializableEnchantment se:sim.getEnchantments()) {
    			itemMeta.addEnchant(Enchantment.getByName(se.getEnchantmentName()), se.getLvl(), true);
    		}
        	if (sim instanceof SerializableEnchantmentStorageMeta) {
        		SerializableEnchantmentStorageMeta sItemMeta = (SerializableEnchantmentStorageMeta)sim;
        		EnchantmentStorageMeta esm = (EnchantmentStorageMeta)itemMeta;
        		for (SerializableEnchantment se:sItemMeta.getEnchantments()) {
        			esm.addStoredEnchant(Enchantment.getByName(se.getEnchantmentName()), se.getLvl(), true);
        		}
        	} else if (sim instanceof SerializableBookMeta) {
        		SerializableBookMeta sItemMeta = (SerializableBookMeta)sim;
        		BookMeta bm = (BookMeta)itemMeta;
        		bm.setPages(sItemMeta.getPages());
        		bm.setAuthor(sItemMeta.getAuthor());
        		bm.setTitle(sItemMeta.getTitle());
        	} else if (sim instanceof SerializableFireworkEffectMeta) {
        		SerializableFireworkEffectMeta sItemMeta = (SerializableFireworkEffectMeta)sim;
        		FireworkEffectMeta fem = (FireworkEffectMeta)itemMeta;
        		SerializableFireworkEffect sfe = sItemMeta.getEffect();
    			Builder fb = FireworkEffect.builder();
    			for (SerializableColor c:sfe.getColors()) {
    				fb.withColor(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
    			}
    			for (SerializableColor c:sfe.getFadeColors()) {
    				fb.withFade(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
    			}
    			fb.with(FireworkEffect.Type.valueOf(sfe.getType()));
    			fb.flicker(sfe.hasFlicker());
    			fb.trail(sfe.hasTrail());
    			fem.setEffect(fb.build());
        	} else if (sim instanceof SerializableFireworkMeta) {
        		SerializableFireworkMeta sItemMeta = (SerializableFireworkMeta)sim;
        		FireworkMeta fm = (FireworkMeta)itemMeta;
        		for (SerializableFireworkEffect sfe:sItemMeta.getEffects()) {
        			Builder fb = FireworkEffect.builder();
        			for (SerializableColor c:sfe.getColors()) {
        				fb.withColor(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
        			}
        			for (SerializableColor c:sfe.getFadeColors()) {
        				fb.withFade(Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
        			}
        			fb.with(FireworkEffect.Type.valueOf(sfe.getType()));
        			fb.flicker(sfe.hasFlicker());
        			fb.trail(sfe.hasTrail());
        			fm.addEffect(fb.build());
        		}
        		fm.setPower(sItemMeta.getPower());
        	} else if (sim instanceof SerializableLeatherArmorMeta) {
        		SerializableLeatherArmorMeta sItemMeta = (SerializableLeatherArmorMeta)sim;
        		LeatherArmorMeta lam = (LeatherArmorMeta)itemMeta;
        		SerializableColor sc = sItemMeta.getColor();
        		lam.setColor(Color.fromRGB(sc.getRed(), sc.getGreen(), sc.getBlue()));
        	} else if (sim instanceof SerializablePotionMeta) {
        		SerializablePotionMeta sItemMeta = (SerializablePotionMeta)sim;
        		PotionMeta pm = (PotionMeta)itemMeta;
        		for (SerializablePotionEffect spe:sItemMeta.getPotionEffects()) {
        			PotionEffect pe = new PotionEffect(PotionEffectType.getByName(spe.getType()), spe.getDuration(), spe.getAmplifier(), spe.isAmbient());
        			pm.addCustomEffect(pe, true);
        		}
        	} else if (sim instanceof SerializableSkullMeta) {
        		SerializableSkullMeta sItemMeta = (SerializableSkullMeta)sim;
        		SkullMeta sm = (SkullMeta)itemMeta;
        		sm.setOwner(sItemMeta.getOwner());
        	} else if (sim instanceof SerializableMapMeta) {
        		SerializableMapMeta sItemMeta = (SerializableMapMeta)sim;
        		MapMeta mm = (MapMeta)itemMeta;
        		mm.setScaling(sItemMeta.isScaling());
        	}
        	item.setItemMeta(itemMeta);
        }
        return item;
	}
	
	
	
	protected static String applyColor(String message) {
		message = message.replace("&0", ChatColor.BLACK + "");
		message = message.replace("&1", ChatColor.DARK_BLUE + "");
		message = message.replace("&2", ChatColor.DARK_GREEN + "");
		message = message.replace("&3", ChatColor.DARK_AQUA + "");
		message = message.replace("&4", ChatColor.DARK_RED + "");
		message = message.replace("&5", ChatColor.DARK_PURPLE + "");
		message = message.replace("&6", ChatColor.GOLD + "");
		message = message.replace("&7", ChatColor.GRAY + "");
		message = message.replace("&8", ChatColor.DARK_GRAY + "");
		message = message.replace("&9", ChatColor.BLUE + "");
		message = message.replace("&a", ChatColor.GREEN + "");
		message = message.replace("&b", ChatColor.AQUA + "");
		message = message.replace("&c", ChatColor.RED + "");
		message = message.replace("&d", ChatColor.LIGHT_PURPLE + "");
		message = message.replace("&e", ChatColor.YELLOW + "");
		message = message.replace("&f", ChatColor.WHITE + "");
		message = message.replace("&k", ChatColor.MAGIC + "");
		message = message.replace("&l", ChatColor.BOLD + "");
		message = message.replace("&m", ChatColor.STRIKETHROUGH + "");
		message = message.replace("&n", ChatColor.UNDERLINE + "");
		message = message.replace("&o", ChatColor.ITALIC + "");
		message = message.replace("&r", ChatColor.RESET + "");
		return message;
	}
	
	
	
	protected static Item getItem(HItem i) {
		Location l = getLocation(i.getLocation());
		for (Entity e:l.getWorld().getEntities()) {
			if (e instanceof Item) {
				Item item = (Item)e;
				if (item.getEntityId() == i.getId()) {
					return item;
				}
			}
		}
		return null;
	}
	
	protected static HItem getItem(Item i) {
		SimpleLocation l = getLocation(i.getLocation());
		SerializableItemStack stack = getSerializableItemStack(i.getItemStack());
		return new HItem(l, i.getEntityId(), stack);
	}
	
	protected static boolean chunkContainsLocation(SimpleLocation l, Chunk c) {
		Location loc = getLocation(l);
		if (loc.getChunk().equals(c)) return true;
		return false;
	}
	
	
}
