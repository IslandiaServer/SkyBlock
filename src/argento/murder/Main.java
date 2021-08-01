package argento.murder;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.BlockIterator;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import argento.murder.json.JSON;
import argento.murder.json.JSON_1_16_R1;
import argento.murder.json.JSON_1_16_R2;
import argento.murder.json.JSON_1_16_R3;
import argento.murder.json.JSON_1_17_R1;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutChat;

public class Main extends JavaPlugin implements Listener {
    public HashMap<String, List<ItemStack>> items = new HashMap<String, List<ItemStack>>();
    public HashMap<Player, Integer> food_level = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> block_limit = new HashMap<Player, Integer>();
    public HashMap<Location, Integer> island_diff = new HashMap<Location, Integer>();
    public HashMap<String, Integer> snowball_limits = new HashMap<String, Integer>();
    public HashMap<String, Inventory> player_guide = new HashMap<String, Inventory>();
    public HashMap<String, Inventory> player_kit = new HashMap<String, Inventory>();
    public HashMap<String, Inventory> player_members = new HashMap<String, Inventory>();
    public HashMap<String, Inventory> player_addmember = new HashMap<String, Inventory>();
    public HashMap<String, Inventory> player_properties = new HashMap<String, Inventory>();
    public HashMap<Location, Boss> location_boss = new HashMap<Location, Boss>();
    public HashMap<Location, Boolean> is_alive = new HashMap<Location, Boolean>();
    public HashMap<Integer, String> localka = new HashMap<Integer, String>();
    public HashSet<Player> infromed = new HashSet<Player>();
    public List<Loc> delete = new ArrayList<Loc>();
    public HashSet<Location> chests = new HashSet<Location>();
	HashMap<String, Long> end = new HashMap<String, Long>();
    public HashSet<Location> centers = new HashSet<Location>();
    public HashSet<Integer> isLoaded = new HashSet<Integer>();
    public HashSet<String> spammed = new HashSet<String>();
    public HashSet<String> isJoined = new HashSet<String>();
    public ArrayList<Island> top = new ArrayList<Island>();
	public HashMap<ItemStack, Integer> quests = new HashMap<ItemStack, Integer>();
	HashMap<Player, Inventory> player_quests = new HashMap<Player, Inventory>();
	HashSet<ItemStack> items_today = new HashSet<ItemStack>();
	HashSet<String> done = new HashSet<String>();
	HashMap<List<String>, Integer> limit = new HashMap<List<String>, Integer>();
	HashSet<String> lever = new HashSet<String>();
	List<Boss> list_bosses = new ArrayList<Boss>();
	List<Location> disabled_locations = new ArrayList<Location>();
	HashMap<String, Boolean> checkPvP = new HashMap<String, Boolean>();
	HashMap<String, Boolean> checkPortal = new HashMap<String, Boolean>();
	HashMap<String, Boolean> inPortal = new HashMap<String, Boolean>();
	HashMap<String, String> islands_all = new HashMap<String, String>();
    int type = 1;
	File file = null;
	FileConfiguration config = null;
	File file2 = null;
	FileConfiguration config2 = null;
	File file3 = null;
	FileConfiguration lang = null;
	int border = 1000;
	Inventory top45, shop, bosses;
	boolean canPlace = true;
	boolean canMech = false;
	boolean bungee = false;
	String hub_server = "lobby";
	boolean TAB = true;
    private Economy econ;
    boolean LOAD = false;
    boolean giveMoney = true;
    int mult1 = 5;
    HashSet<String> isKit = new HashSet<String>();
    private JSON json = null;
    
    public void setupJSON() {
    	if(Bukkit.getVersion().contains("1.16.2")) {
    		json = new JSON_1_16_R2();
    	}
    	else if(Bukkit.getVersion().contains("1.16.3") || Bukkit.getVersion().contains("1.16.4") || Bukkit.getVersion().contains("1.16.5")) {
    		json = new JSON_1_16_R3();
    	}
    	else if(Bukkit.getVersion().contains("1.17")) {
    		json = new JSON_1_17_R1();
    	}
    	else if(Bukkit.getVersion().contains("1.16")) {
        	json = new JSON_1_16_R1();
        }
    }
    
    class Island {
    	private String player;
    	private int lvl;
    	private double strength;
    	private double prot;
    	public Island(String player, int lvl, double s, double p) {
    		this.player = player;
    		this.lvl = lvl;
    		this.strength = s;
    		this.prot = p;
    	}
    }
    
    class Boss {
    	private String name;
    	private EntityType type;
    	private Location loc;
    	private int hp;
    	private int min_lvl;
    	private List<String> abilities;
    	private double damage;
    	public Boss(String name, EntityType type, Location loc, int hp, int min_lvl, List<String> abilities, double damage) {
    		this.name = name;
    		this.type = type;
    		this.loc = loc;
    		this.hp = hp;
    		this.min_lvl = min_lvl;
    		this.abilities = abilities;
    		this.damage = damage;
    	}
    }
    
    @EventHandler
    public void portal(PlayerMoveEvent e) {
    	Player p = e.getPlayer();
		String p_name = p.getName();
    	if(p.getLocation().getBlock().getType() == Material.NETHER_PORTAL) {
    		if(inPortal.isEmpty() || !inPortal.containsKey(p_name) || !inPortal.get(p_name)) {
    			if(inPortal.containsKey(p_name)) {
    				inPortal.replace(p_name, true);
    			}
    			else inPortal.put(p_name, true);
	    		Location lcc = p.getLocation();
	    		Location loc = getPortal(lcc);
	    		String key = String.valueOf(loc.getBlockX()) + "," + String.valueOf(loc.getBlockY()) + "," +String.valueOf(loc.getBlockZ());
	    		if(!config2.contains("portals."+key)) {
	    			if(!spammed.contains(p_name)) {
	    				spammed.add(p_name);
	    				send(p, lang.getString("node_not_found1"));
	    				send(p, lang.getString("node_not_found2"));
	    				send(p, lang.getString("node_not_found3"));
	    				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	    					public void run() {
	    						if(spammed.contains(p_name)) spammed.remove(p_name);
	    					}
	    				}, 20*3);
	    			}
	    		}
	    		else {
	    			Location to = config2.getLocation("portals."+key);
	    			boolean f = false;
	    			int radius = 3;
	    			for(int x = to.getBlockX()-radius; x <= to.getBlockX()+radius && !f; ++x) {
	    				for(int y = to.getBlockY()-radius; y <= to.getBlockY()+radius && !f; ++y) {
	    					for(int z = to.getBlockZ()-radius; z <= to.getBlockZ()+radius && !f; ++z) {
	    	    				Location lc = new Location(loc.getWorld(), x, y, z);
	    	    				if(lc.getBlock().getType() == Material.NETHER_PORTAL) {
	    	    					f = true;
	    	    					Location l = getPortal(lc);
	    	    					Location ln = new Location(l.getWorld(), l.getBlockX(), 255, l.getBlockZ());
	    	    					if(ln.getBlock().getType() != Material.BARRIER || isPortal(ln)) {
		    	    					boolean c = true;
		    	    					if(disabled_locations.contains(l) && is_alive.containsKey(l) && is_alive.get(l)) {
		    								Boss b = location_boss.get(l);
		    								if(getLevel(p) < b.min_lvl) {
		    									c = false;
		    								}
		    	    					}
		    	    					if(c) {
			    	    					if(getProt(p) < 1.2) {
			        	    					if(p.getFoodLevel() > 4) p.setFoodLevel(p.getFoodLevel() - 4);
			    	    						if(p.getHealth() > 4) p.setHealth(p.getHealth() - 4);
			        	    					send(p, lang.getString("strong_hurt_from_teleportation"));
			    	    					}
			    	    					else if(getProt(p) < 1.5) {
			    	    						if(p.getFoodLevel() > 4) p.setFoodLevel(p.getFoodLevel() - 2);
			    	    						if(p.getHealth() > 4) p.setHealth(p.getHealth() - 2);
			        	    					send(p, lang.getString("medium_hurt_from_teleportation"));
			    	    					}
			    	    					else if(getProt(p) < 2.0) {
			    	    						if(p.getFoodLevel() > 4) p.setFoodLevel(p.getFoodLevel() - 1);
			    	    						if(p.getHealth() > 4) p.setHealth(p.getHealth() - 1);
			        	    					send(p, lang.getString("weak_hurt_from_teleportation"));
			    	    					}
			    	    					if(disabled_locations.contains(l) && is_alive.containsKey(l) && is_alive.get(l)) {
			    	    						send(p, lang.getString("you_come_to_boss"));
			    	    						boolean can = true;
			    	    						for(Player pl : Bukkit.getOnlinePlayers()) {
			    	    							if(getDistance(pl.getLocation(), l) <= 200) {
			    	    								can = false;
			    	    								break;
			    	    							}
			    	    						}
			    	    						List<Entity> enList = p.getWorld().getEntities();
			    	    						for(Entity en : enList) {
			    	    							if(!can) break;
			    	    							if(!en.getScoreboardTags().isEmpty() && getDistance(en.getLocation(), l) <= 200) {
			    	    								for(String tag : en.getScoreboardTags()) {
			    	    									if(tag.contains("location: ")) {
			    	    										can = false;
			    	    										break;
			    	    									}
			    	    								}
			    	    							}
			    	    						}
			    	    						if(can) {
				    								Boss b = location_boss.get(l);
				    	    						send(p, lang.getString("get_ready_to_boss"));
				    	    						
				    	    						Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				    	    							public void run() {
				    	    	    						Location spawn = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ()+3);
				    	    	    						if(spawn.getBlock().getType() != Material.AIR) spawn.getBlock().setType(Material.AIR);
				    	    	    						if(spawn.add(0, 1, 0).getBlock().getType() != Material.AIR) spawn.getBlock().setType(Material.AIR);
				    	    								LivingEntity entity = (LivingEntity) spawn.getWorld().spawnEntity(spawn, b.type);
				    	    								entity.setCustomNameVisible(true);
				    	    								entity.setCustomName((String.valueOf((int)b.hp)) + " §a§l❤");
				    	    								entity.setMaxHealth(b.hp);
				    	    								entity.setHealth(b.hp);
				    	    								int drop = 1;
				    	    								if(b.min_lvl <= 7) drop = 5;
				    	    								else if(b.min_lvl <= 10) drop = 6;
				    	    								else if(b.min_lvl <= 14) drop = 7;
				    	    								else if(b.min_lvl <= 18) drop = 8;
				    	    								else if(b.min_lvl <= 23) drop = 9;
				    	    								else drop = 10;
				    	    								entity.addScoreboardTag("location: "+String.valueOf(l.getBlockX()) + " " + String.valueOf(l.getBlockY()) + " " + String.valueOf(l.getBlockZ()));
				    	    								entity.addScoreboardTag("drop: "+String.valueOf(drop));
				    	    								entity.addScoreboardTag("damage: "+String.valueOf(b.damage));
				    	    								if(b.abilities != null && !b.abilities.isEmpty()) {
				    	    									for(String ab : b.abilities) {
				    	    										entity.addScoreboardTag("ability: "+ab);
				    	    									}
				    	    								}
				    	    								
				    	    							}
				    	    						}, 10*20);
			    	    						}
			    	    						
			    	    					}
			    	    					else send(p, lang.getString("success_tp"));
			    	    					
			    	    					l.add(0.5, 0, 0);
			    	    					p.teleport(l);
			    	    					break;
		    	    					}
		    	    					else {
		    	    						send(p, lang.getString("have_no_level_to_boss"));
		    	    					}
	    	    					}
	    	    					else {
	    	    						send(p, lang.getString("portal_was_disabled"));
	    	    					}
	    	    				}
	    	    			}
	        			}
	    			}
	    			if(!f) {
	    				send(p, lang.getString("portal_not_found"));
	    			}
	    		}
	    	}
    	}
    	else {
    		if(inPortal.containsKey(p_name)) {
				inPortal.replace(p_name, false);
			}
			else inPortal.put(p_name, false);
    	}
    }
    
    Location getPortal(Location loc) {
    	Location lc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());
    	while(lc.getBlock().getType() == Material.NETHER_PORTAL) {
    		lc = new Location(lc.getWorld(), lc.getBlockX(), lc.getBlockY()-1, lc.getBlockZ());
    	}
    	lc.add(-1, 1, 0);
    	
    	while(lc.getBlock().getType() == Material.NETHER_PORTAL) {
    		lc = new Location(loc.getWorld(), lc.getBlockX()-1, lc.getBlockY(), lc.getBlockZ());
    	}
    	lc.add(1, 0, 0);
    	return lc;
    }
    
    public class Loc {
    	private int x, y, z;
    	public Loc(int xx, int yy, int zz) {
    		this.x = xx;
    		this.y = yy;
    		this.z = zz;
    	}
    }
    
    @EventHandler
    public void craft(CraftItemEvent e) {
    	Player p = (Player) e.getWhoClicked();
    	if(!canMech) {
	    	if(e.getCurrentItem().getType() == Material.PISTON || e.getCurrentItem().getType() == Material.STICKY_PISTON) {
	    		send(p, lang.getString("craft_disabled"));
	    		e.setCancelled(true);
	    	}
	    	else if(e.getCurrentItem().getType() == Material.OBSERVER || e.getCurrentItem().getType() == Material.REPEATER || e.getCurrentItem().getType() == Material.COMPARATOR) {
	    		send(p, lang.getString("craft_disabled"));
				e.setCancelled(true);
			}
    	}
    }
    
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public void saveDefaultConfig2() {
		if(file2 == null) {
			file2 = new File(getDataFolder(), "islands.yml");
		}
		if(!file2.exists()) {
			saveResource("islands.yml", false);
		}
	}
	
	public void saveConfig2() {
		if(config2 == null || file2 == null) return;
		try {
			config2.save(file2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig2() {
		if(file2 == null) {
			file2 = new File(getDataFolder(), "islands.yml");
		}
		config2 = YamlConfiguration.loadConfiguration(file2);
		Reader defConfigStream = new InputStreamReader(getResource("islands.yml"));
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config2.setDefaults(defConfig);
        }
	}
	
	public void saveDefaultConfig3() {
		if(file3 == null) {
			file3 = new File(getDataFolder(), "lang.yml");
		}
		if(!file3.exists()) {
			saveResource("lang.yml", false);
		}
	}
	
	public void saveConfig3() {
		if(lang == null || file3 == null) return;
		try {
			lang.save(file3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig3() {
		if(file3 == null) {
			file3 = new File(getDataFolder(), "lang.yml");
		}
		lang = YamlConfiguration.loadConfiguration(file2);
		Reader defConfigStream = new InputStreamReader(getResource("lang.yml"));
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            lang.setDefaults(defConfig);
        }
	}
	
	public FileConfiguration getConfig2() {
		if(config2 == null) {
			reloadConfig2();
		}
		return config2;
	}
	
	public FileConfiguration getLang() {
		if(lang == null) {
			reloadConfig3();
		}
		return lang;
	}
	
	
	public void saveDefaultConfig() {
		if(file == null) {
			file = new File(getDataFolder(), "config.yml");
		}
		if(!file.exists()) {
			saveResource("config.yml", false);
		}
	}
	
	public void saveConfig() {
		if(config == null || file == null) return;
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadConfig() {
		if(file == null) {
			file = new File(getDataFolder(), "config.yml");
		}
		config = YamlConfiguration.loadConfiguration(file);
		Reader defConfigStream = new InputStreamReader(getResource("config.yml"));
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
	}
	
	public FileConfiguration getConfig() {
		if(config == null) {
			reloadConfig();
		}
		return config;
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity en = e.getEntity();
		if(en instanceof Villager && !(damager instanceof Player)) {
			if(!en.getScoreboardTags().isEmpty()) {
				if(!en.getCustomName().contains("❤")) {
					e.setCancelled(true);
				}
			}
		}
		if(en instanceof Player && damager instanceof Player) {
			
			Location lc1 = new Location(damager.getWorld(), damager.getLocation().getBlockX(), 255, damager.getLocation().getBlockZ());
			Location lc2 = new Location(en.getWorld(), en.getLocation().getBlockX(), 255, en.getLocation().getBlockZ());
			if(lc1.getBlock().getType() == Material.BARRIER || lc2.getBlock().getType() == Material.BARRIER) {
				if(!isPvP(damager.getLocation()) || !isPvP(en.getLocation())) {
					e.setCancelled(true);
				}
			}
		}
		else if(e.getEntity() instanceof Player && e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
			if(e.getDamager() instanceof Arrow) {
				Arrow a = (Arrow) e.getDamager();
				if(a.getShooter() instanceof Player) {
					Player damager2 = (Player) a.getShooter();
					Location lc1 = new Location(damager2.getWorld(), damager2.getLocation().getBlockX(), 255, damager2.getLocation().getBlockZ());
					Location lc2 = new Location(en.getWorld(), en.getLocation().getBlockX(), 255, en.getLocation().getBlockZ());
					if(lc1.getBlock().getType() == Material.BARRIER || lc2.getBlock().getType() == Material.BARRIER) {
						if(!isPvP(damager2.getLocation()) || !isPvP(en.getLocation())) {
							e.setCancelled(true);
						}
					}
				}
			}
			if(e.getDamager() instanceof Trident) {
				Trident a = (Trident) e.getDamager();
				if(a.getShooter() instanceof Player) {
					Player damager2 = (Player) a.getShooter();
					Location lc1 = new Location(damager2.getWorld(), damager2.getLocation().getBlockX(), 255, damager2.getLocation().getBlockZ());
					Location lc2 = new Location(en.getWorld(), en.getLocation().getBlockX(), 255, en.getLocation().getBlockZ());
					if(lc1.getBlock().getType() == Material.BARRIER || lc2.getBlock().getType() == Material.BARRIER) {
						if(!isPvP(damager2.getLocation()) || !isPvP(en.getLocation())) {
							e.setCancelled(true);
						}
					}
				}
			}
    	}
		else if(damager instanceof Player) {
			if(!canChange((Player) damager, en.getLocation())) {
				send((Player)damager, lang.getString("you_cant_kill_entities_not_from_your_island"));
				e.setCancelled(true);
			}
		}
		else if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
			if(e.getDamager() instanceof Arrow) {
				Arrow a = (Arrow) e.getDamager();
				if(a.getShooter() instanceof Player) {
					Player p = (Player) a.getShooter();
					if(!canChange(p, en.getLocation())) {
						send(p, lang.getString("you_cant_kill_entities_not_from_your_island"));
						e.setCancelled(true);
					}
				}
			}
			if(e.getDamager() instanceof Trident) {
				Trident a = (Trident) e.getDamager();
				if(a.getShooter() instanceof Player) {
					Player p = (Player) a.getShooter();
					if(!canChange(p, en.getLocation())) {
						send(p, lang.getString("you_cant_kill_entities_not_from_your_island"));
						e.setCancelled(true);
					}
				}
			}
		}
		if(!e.isCancelled()) {
			if(!en.getScoreboardTags().isEmpty()) {
				for(String tag : en.getScoreboardTags()) {
					if(tag.contains("ability: ")) {
						try {
							String[] spl = tag.split(" ");
							String ab = "";
							for(int i = 1; i < spl.length; ++i) {
								ab+=spl[i];
							}
							if(ab.contains(lang.getString("abilities.1"))) {
								if(damager instanceof Arrow) e.setCancelled(true);
							}
							else if(ab.contains(lang.getString("abilities.2"))) {
								double ran = new Random().nextDouble();
								if(ran <= 0.1) {
									LivingEntity len = (LivingEntity) en;
									if(len.getHealth() + 50 <= len.getMaxHealth()) {
										len.setHealth(len.getHealth()+50);
									}
								}
							}
							else if(ab.contains(lang.getString("abilities.3"))) {
								if(damager instanceof Player) {
									Player p = (Player) damager;
									double ran = new Random().nextDouble();
									if(ran <= 0.1) {
										p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 1));
									}
									ran = new Random().nextDouble();
									if(ran <= 0.05) {
										p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*5, 1));
									}
									ran = new Random().nextDouble();
									if(ran <= 0.05) {
										p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*5, 1));
									}
								}
							}
							else if(ab.contains(lang.getString("abilities.4"))) {
								if(damager instanceof Player) {
									Player p = (Player) damager;
									double ran = new Random().nextDouble();
									if(ran <= 0.1) {
										if(p.getFoodLevel() <= 5) {
											p.setFoodLevel(0);
										}
										else {
											p.setFoodLevel(p.getFoodLevel()-5);
										}
									}
								}
							}
							else if(ab.contains(lang.getString("abilities.5"))) {
								if(damager instanceof Player) {
									Player p = (Player) damager;
									double ran = new Random().nextDouble();
									if(ran <= 0.1) {
										p.damage(e.getDamage());
										e.setCancelled(true);
									}
								}
							}
							else if(ab.contains(lang.getString("abilities.6"))) {
								if(damager instanceof Player) {
									Player p = (Player) damager;
									double ran = new Random().nextDouble();
									if(ran <= 0.08) {
										en.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), en.getLocation().getYaw(), en.getLocation().getPitch()));
									}
								}
							}
							else if(ab.contains(lang.getString("abilities.7"))) {
								if(damager instanceof Player) {
									Player p = (Player) damager;
									double ran = new Random().nextDouble();
									if(ran <= 0.15) {
										ItemStack it = p.getItemInHand().clone();
										p.getItemInHand().setAmount(0);
										p.getWorld().dropItemNaturally(en.getLocation(), it);
									}
								}
							}
						}
						catch (Exception ee) {};
					}
				}
			}
			if(!e.isCancelled()) {
				double k1 = 1, k2 = 1;
				if(!damager.getScoreboardTags().isEmpty()) {
					for(String tag : damager.getScoreboardTags()) {
						if(tag.contains("damage:")) {
							k1 = Double.valueOf(tag.split(": ")[1]);
						}
					}
				}
				if(en instanceof Player) {
					Player p = (Player) en;
					k2 = getProt(p);
				}
				if(damager instanceof Player) {
					Player p = (Player) damager;
					k1 = getStrength(p);
				}
				double damage = e.getDamage()*k1/k2;
				e.setDamage(damage);
				if(en.getCustomName() != null) {
					if(en.getCustomName().contains("❤")) {
						int hp = (int) ((LivingEntity) en).getHealth();
						int set = hp-(int) damage;
						if(set >= 0) en.setCustomName(String.valueOf(set) + " §a§l❤");
						else {
							en.setCustomName(String.valueOf(0) + " §a§l❤");
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void damage2(EntityDamageEvent e) {
		Entity en = e.getEntity();
		if(en.getLastDamageCause() == null || en.getLastDamageCause().getCause() != DamageCause.ENTITY_ATTACK) {
			double k2 = 1;
			if(en instanceof Player) {
				Player p = (Player) en;
				k2 = getProt(p);
			}
			double damage = e.getDamage()/k2;
			e.setDamage(damage);
			if(en.getCustomName() != null) {
				if(en.getCustomName().contains("❤")) {
					int hp = (int) ((LivingEntity) en).getHealth();
					int set = hp-(int) damage;
					if(set >= 0) en.setCustomName(String.valueOf(set) + " §a§l❤");
					else {
						en.setCustomName(String.valueOf(0) + " §a§l❤");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void move(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(e.getTo().getBlockY() <= -100) p.setHealth(0.0);
		if(e.getTo().getBlock().getType() == Material.NETHER_PORTAL) {
			if(end.containsKey(p.getName())) {
				long time = (long) System.currentTimeMillis()/1000;
				long pred = end.get(p.getName());
				if(time - pred > 15) {
					send(p, lang.getString("stuck_in_nether_portal"));
					send(p, lang.getString("suicide_help"));
					e.getTo().getBlock().setType(Material.AIR);
					end.remove(p.getName());
				}
			}
			else {
				long time = (long) System.currentTimeMillis()/1000;
				end.put(p.getName(), time);
			}
		}
		else if(end.containsKey(p.getName())) {
			end.remove(p.getName());
		}
	}
	
	public void generateIslandFromConfig(Location center, int radius, double p, int addons, int k, int distance, HashSet<Location> used, String island) {
		HashMap<Integer, List<Pair>> layers = new HashMap<Integer, List<Pair>>();
		ConfigurationSection cs = config.getConfigurationSection("islands_types."+island+".layers");
		//LOG(island);
		for(String key : cs.getKeys(false)) {
			List<Pair> pairs = new ArrayList<Pair>();
			List<ItemStack> its = (List<ItemStack>) config.get("islands_types."+island+".layers."+key+".materials");
			List<Double> probs = (List<Double>) config.get("islands_types."+island+".layers."+key+".probs");
			
			for(int i = 0; i < its.size(); ++i) {
				Material mat = its.get(i).getType();
				double prob = probs.get(i);
				Pair pair = new Pair(mat, prob);
				pairs.add(pair);
			}
			layers.put(Integer.valueOf(key), pairs);
		}
		
		ArrayList<String> other_structures = new ArrayList<String>();
		ArrayList<String> only_main_island_structs = new ArrayList<String>();
		
		for(String struct : config.getStringList("islands_types."+island+".other_structures")) {
			other_structures.add(struct);
		}
		
		for(String struct : config.getStringList("islands_types."+island+".only_main_island_structures")) {
			only_main_island_structs.add(struct);
		}
		
		List<CustomEntity> entities = new ArrayList<CustomEntity>();
		
		cs = config.getConfigurationSection("islands_types."+island+".entities");
		
		for(String key : cs.getKeys(false)) {
			EntityType type = EntityType.fromName(config.getString("islands_types."+island+".entities."+key+".type"));
			double hp = config.getDouble("islands_types."+island+".entities."+key+".hp");
			double damage = config.getDouble("islands_types."+island+".entities."+key+".damage");
			double prob = config.getDouble("islands_types."+island+".entities."+key+".prob");
			ArrayList<String> abilities = (ArrayList<String>) config.get("islands_types."+island+".entities."+key+".abilities");
			
			CustomEntity ce = new CustomEntity(type, hp, damage, prob, abilities);
			entities.add(ce);
		}
		
		double s = config.getDouble("islands_types."+island+".s");
		
		generateIsland(center, radius, p, layers, entities, addons, k, distance, used, s, only_main_island_structs, other_structures, island);
	}
	
	public void generateIsland(Location center, int radius, double p, HashMap<Integer, List<Pair>> layers, List<CustomEntity> entities, int addons, int k, int distance, HashSet<Location> used, double s, ArrayList<String> only_main_island_structs, ArrayList<String> other_structures, String TYPE) {
		int x = center.getBlockX();
		int y = center.getBlockY();
		int z = center.getBlockZ();
		
		int min_y = 999999;
		
		double max_dis = 0;
		
		for(int xx = x - radius; xx <= x + radius; ++xx) {
			for(int zz = z - radius; zz <= z + radius; ++zz) {
				for(int layer: layers.keySet()) {
					int yy = y-layer+1;
					double sum = 0;
					
					if(yy < min_y) {
						min_y = yy;
					}
					
					double max_sum = 0;
					for(Pair pair : layers.get(layer)) {
						max_sum += pair.probability;
					}
					
					double ran = new Random().nextDouble()*max_sum;
					
					Location lc2 = new Location(center.getWorld(), xx, yy, zz);
					double dis = getDistance3d(center, lc2);
					if(dis > max_dis) {
						max_dis = dis;
					}
					
					for(Pair pair : layers.get(layer)) {
						sum += pair.probability;
						if(sum >= ran) {
							Material mat = pair.mat;
							Location lc = new Location(center.getWorld(), xx, yy, zz);
							lc.getBlock().setType(mat);
							if(addons != 0) used.add(lc);
							break;
						}
					}
				}
			}
		}
		//sqrt(2)*(r-1)/max_dis >= p
		for(int xx = x - radius; xx <= x + radius; ++xx) {
			for(int zz = z - radius; zz <= z + radius; ++zz) {
				for(int layer: layers.keySet()) {
					int yy = y-layer+1;
					Location lc = new Location(center.getWorld(), xx, yy, zz);
					double dis = getDistance3d(center, lc);
					double prob = dis/max_dis;
					boolean is = false;
					if(prob >= p) {
						if(addons != 0) {
							lc.getBlock().setType(Material.AIR);
							used.remove(lc);
							is = true;
						}
						else {
							if(!used.contains(lc)) {
								lc.getBlock().setType(Material.AIR);
								is = true;
							}
						}
					}
					if(!TYPE.equals("player")) {
						delete.add(new Loc(xx, yy, zz));
					}
				}
			}
		}
		int pred_y = y;
		for(int i = 0; i < addons; ++i) {
			int xx = (x-radius) + new Random().nextInt(2*radius);
			int zz = (z-radius) + new Random().nextInt(2*radius);
			if(distance-1 > 0) {
				int yy = (pred_y-distance) + new Random().nextInt(distance-1);
				pred_y = yy;
				
				Location center2 = new Location(center.getWorld(), xx, yy, zz);
				int nr = (int) radius/k + new Random().nextInt(1+(int)radius/(k));
				if(nr >= 3) {
					generateIsland(center2, nr, (double) Math.sqrt(2)*(nr*1.0)/max_dis, layers, entities, 0, k, distance, used, s, new ArrayList<String>(), other_structures, TYPE);
				}
			}
		}
		
		//entity spawn
		for(CustomEntity en : entities) {
			int attemps = 0;
			
			double ran = new Random().nextDouble();
			
			if(ran <= en.prob) {
				
				while(attemps <= 5) {
					int xx = (x-radius) + new Random().nextInt(2*radius);
					int zz = (z-radius) + new Random().nextInt(2*radius);
					
					Location lc = new Location(center.getWorld(), xx+0.5, y, zz+0.5);
					Location lc2 = new Location(center.getWorld(), xx+0.5, y+1, zz+0.5);
					Location lc3 = new Location(center.getWorld(), xx+0.5, y+2, zz+0.5);
					if(lc.getBlock().getType() != Material.AIR && lc2.getBlock().getType() == Material.AIR && lc3.getBlock().getType() == Material.AIR) {
						LivingEntity entity = (LivingEntity) center.getWorld().spawnEntity(lc2, en.type);
						entity.setCustomNameVisible(true);
						entity.setCustomName((String.valueOf((int)en.hp)) + " §a§l❤");
						entity.setMaxHealth(en.hp);
						entity.setHealth(en.hp);
						entity.addScoreboardTag("damage: "+String.valueOf(en.damage));
						if(en.abilities != null && !en.abilities.isEmpty()) {
							for(String ab : en.abilities) {
								entity.addScoreboardTag("ability: "+String.valueOf(ab));
								if(ab.equals("ANTIFIRE") && entity.getEquipment().getHelmet().getType() == Material.AIR) {
									entity.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
								}
							}
						}
						break;
					}
					attemps++;
				}
			}
		}
		
		//generate structures
		ArrayList<String> structures = new ArrayList<String>();
		if(!only_main_island_structs.isEmpty()) {
			structures = new ArrayList<String>(only_main_island_structs);
		}
		else {
			structures = new ArrayList<String>(other_structures);
		}
		for(String structure : structures) {
			String struct = structure.split(", ")[0];
			double prob = Double.valueOf(structure.split(", ")[1]);
			double ran = new Random().nextDouble();
			if(ran <= prob) {
				switch(struct) {
					case "PORTAL":
						genPortal(randomLocation3(center), TYPE);
						break;
					case "COBBLESTONE_GENERATOR":
						genGenerator(randomLocation2(center, radius-5), TYPE);
						break;
					case "CHEST":
						genChest(randomLocation(center, radius-2), TYPE);
						break;
					case "TREE-1":
						genTree(randomLocation(center, radius-1), Material.OAK_LOG, Material.OAK_LEAVES, TYPE);
						break;
					case "TREE-2":
						genTree(randomLocation(center, radius-1), Material.ACACIA_LOG, Material.ACACIA_LEAVES, TYPE);
						break;
					case "TREE-3":
						genTree(randomLocation(center, radius-1), Material.SPRUCE_LOG, Material.SPRUCE_LEAVES, TYPE);
						break;
					case "TREE-4":
						genTree(randomLocation(center, radius-1), Material.BIRCH_LOG, Material.BIRCH_LEAVES, TYPE);
						break;
					case "TREE-5":
						genTree(randomLocation(center, radius-1), Material.JUNGLE_LOG, Material.JUNGLE_LEAVES, TYPE);
						break;
					case "TREE-6":
						genTree(randomLocation(center, radius-1), Material.DARK_OAK_LOG, Material.DARK_OAK_LEAVES, TYPE);
						break;
					case "TREE-7":
						genTree(randomLocation(center, radius-1), Material.CRIMSON_STEM, Material.NETHER_WART_BLOCK, TYPE);
						break;
					case "TREE-8":
						genTree(randomLocation(center, radius-1), Material.WARPED_STEM, Material.WARPED_WART_BLOCK, TYPE);
						break;
					case "BLOCK":
						String block_type = structure.split(", ")[2];
						genBlock(randomLocation(center, radius-1), Material.getMaterial(block_type), TYPE);
				}
			}
		}
	}
	
	public boolean hasEmptySlot(Player p) {
		ItemStack[] cont = p.getInventory().getContents();
		for(int i = 0; i < 36; ++i) {
			if(cont[i] == null) return true;
		}
		return false;
	}
	
	public boolean hasItem(Inventory inv, Material mat, int amount) {
		int sum = 0;
		for(ItemStack it : inv.getContents()) {
			if(it != null) {
				if(it.getType() == mat) {
					sum += it.getAmount();
					if(sum >= amount) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void removeItem(Inventory inv, Material mat, int amount) {
		int sum = amount;
		int raw = 0;
		for(ItemStack it : inv.getContents()) {
			if(it != null) {
				if(it.getType() == mat) {
					if(sum == 0) break;
					if(it.getAmount() <= sum) {
						inv.setItem(raw, null);
						sum -= it.getAmount();
					}
					else {
						it.setAmount(it.getAmount()-sum);
						sum = 0;
					}
				}
			}
			raw++;
		}
	}
	
	String toColorDiff(int diff) {
		String dif = String.valueOf(diff);
		if(diff <= 3) return "§a§l"+diff;
		if(diff <= 5) return "§e§l"+diff;
		if(diff <= 7) return "§6§l"+diff;
		if(diff <= 9) return "§c§l"+diff;
		return "§4"+diff;
	}
	
	public void generateRandomIsland(int diff, String num, Location center) {
		String dif = String.valueOf(diff);
		island_diff.put(center, diff);
		//generateIslandFromConfig(center, 8+new Random().nextInt(2), 0.4+new Random().nextDouble()*0.1,
		//3+new Random().nextInt(3), 3+new Random().nextInt(2), 2+ new Random().nextInt(2),
		//new HashSet<Location>(), "player");
		//Location center, int radius, double p, int addons, int k, int distance, HashSet<Location> used, String island
		//(Location center, int radius, double p, HashMap<Integer, List<Pair>> layers, List<CustomEntity> entities, int addons, int k, int distance, HashSet<Location> used, double s, ArrayList<String> only_main_island_structs, ArrayList<String> other_structures, String TYPE)
		generateIslandFromConfig(center, 4+new Random().nextInt(5), 0.35+new Random().nextDouble()*0.1,
				0+new Random().nextInt(5), 2+new Random().nextInt(4), 2+ new Random().nextInt(3),
				new HashSet<Location>(), dif+"-"+num);
		for(Player p : Bukkit.getOnlinePlayers()) {
			int dis = (int) getDistance(p.getLocation(), center);
			if(dis <= 300) {
				send(p, lang.getString("island_found")+toColorDiff(diff));
				json.json(p, center, lang);
			}
		}
	}
	
	@EventHandler
	public void chunkLoad(ChunkLoadEvent e) {
		Chunk c = e.getChunk();
		if(LOAD && c.getX()*16 >= 0 && c.getZ()*16-12 >= 0 && delete.size() <= 1500000) {
			if(!isLoaded.contains(c.getX())) {
				double ran = new Random().nextDouble();
				if(ran <= config.getDouble("island_gen_prob")) {
					Location lc1 = new Location(c.getWorld(), c.getX()*16-12, 255, c.getZ()*16-12);
					Location lc2 = new Location(c.getWorld(), c.getX()*16+12, 255, c.getZ()*16-12);
					Location lc3 = new Location(c.getWorld(), c.getX()*16+12, 255, c.getZ()*16+12);
					Location lc4 = new Location(c.getWorld(), c.getX()*16-12, 255, c.getZ()*16+12);
					boolean can = true;
					for(Location lc : disabled_locations) {
						if(Math.abs(lc.getBlockX()-c.getX()*16) <= 300 && Math.abs(lc.getBlockZ()-c.getZ()*16) <= 300) {
							can = false;
							break;
						}
					}
					if(can && lc1.getBlock().getType() != Material.BARRIER && lc2.getBlock().getType() != Material.BARRIER && 
							lc3.getBlock().getType() != Material.BARRIER && lc4.getBlock().getType() != Material.BARRIER) {
						
						double r1 = new Random().nextDouble();
						int diff = (int) (Math.ceil(1.8*Math.log((double) (1/r1))));
						
						String dif = String.valueOf(diff);
						
						int max_num = 1;
						while(config.contains("islands_types."+dif+"-"+String.valueOf(max_num+1))) {
							max_num++;
						}
						
						int num = new Random().nextInt(max_num)+1;
						String nu = String.valueOf(num);
						
						Location center = new Location(c.getWorld(), c.getX()*16, 50 + new Random().nextInt(50), c.getZ()*16);
						generateRandomIsland(diff, nu, center);
					}
				}
				isLoaded.add(c.getX());
			}
		}
	}
	
	public void LOG(int a) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			send(p, String.valueOf(a));
		}
	}
	
	public void LOG(String a) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			send(p, a);
		}
	}
	
	List<Long> toCoins(long l) {
		ArrayList<Long> list = new ArrayList<Long>();
		long med_money = l%1000;
		long ost = (l-med_money)/1000;
		long gold_money = ost%1000;
		long plat_money = (ost-gold_money)/1000;
		list.add(med_money);
		list.add(gold_money);
		list.add(plat_money);
		return list;
	}
	
	int getLevel(Player p) {
		return config2.getInt("islands."+p.getName()+".level");
	}
	
	double getStrength(Player p) {
		return config2.getDouble("islands."+p.getName()+".strength");
	}
	
	double round(double a) {
		return Math.round(a*100.0)/100.0;
	}
	
	public void addStrength(Player p, double value) {
		config2.set("islands."+p.getName()+".strength", round((getStrength(p)+value)));
		saveConfig2();
		reloadConfig2();
	}
	
	public void addProt(Player p, double value) {
		config2.set("islands."+p.getName()+".prot", round(getProt(p)+value));
		saveConfig2();
		reloadConfig2();
	}
	
	double getProt(Player p) {
		return config2.getDouble("islands."+p.getName()+".prot");
	}
	
	double getMin(Player p) {
		int lvl = getLevel(p);
		if(lvl >= 200) return 3.0;
		else if(lvl >= 190) return 2.9;
		else if(lvl >= 180) return 2.8;
		else if(lvl >= 170) return 2.7;
		else if(lvl >= 160) return 2.6;
		else if(lvl >= 150) return 2.5;
		else if(lvl >= 140) return 2.4;
		else if(lvl >= 130) return 2.3;
		else if(lvl >= 120) return 2.2;
		else if(lvl >= 110) return 2.1;
		else if(lvl >= 100) return 2.0;
		else if(lvl >= 90) return 1.9;
		else if(lvl >= 80) return 1.8;
		else if(lvl >= 70) return 1.7;
		else if(lvl >= 60) return 1.6;
		else if(lvl >= 50) return 1.5;
		else if(lvl >= 40) return 1.4;
		else if(lvl >= 30) return 1.3;
		else if(lvl >= 20) return 1.2;
		else if(lvl >= 10) return 1.1;
		return 1.0;
	} 
	
	public void update(Player p) {
		double value = getMin(p);
		if(getProt(p) < value) {
			config2.set("islands."+p.getName()+".prot", value);
		}
		if(getStrength(p) < value) {
			config2.set("islands."+p.getName()+".strength", value);
		}
		saveConfig2();
		reloadConfig2();
		scoreboardUpdate(p);
	}
	
	public void addLevel(Player p) {
		int nl = getLevel(p)+1;
		config2.set("islands."+p.getName()+".level", nl);
		config2.set("islands."+p.getName()+".xp", 0);
		config2.set("islands."+p.getName()+".need_xp", config2.getInt("islands."+p.getName()+".need_xp")+10);

		if(TAB) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player "+p.getName()+" tabsuffix &7 [&c"+String.valueOf(nl)+"&7]");
		
		send(p, lang.getString("you_got_new_lvl").replaceAll("%lvl%", String.valueOf(nl)));
		
		if(giveMoney) {
			send(p, lang.getString("new_lvl_money").replaceAll("%money%", String.valueOf(nl*mult1)));
			econ.depositPlayer(p.getName(), nl*mult1);
		}
		
		saveConfig2();
		reloadConfig2();
		
		update(p);
	}
	
	public void minus10(Player p) {
		double p1 = Math.round((getStrength(p) - getStrength(p)*0.05)*100.0)/100.0;
		double p2 = Math.round((getProt(p) - getProt(p)*0.05)*100.0)/100.0;
		int xp = config2.getInt("islands."+p.getName()+".xp");
		int p3 = xp - (int) (xp*0.5);
		double min = getMin(p);
		if(p1 < min) p1 = min;
		if(p2 < min) p2 = min;
		
		config2.set("islands."+p.getName()+".strength",p1);
		config2.set("islands."+p.getName()+".prot",p2);
		config2.set("islands."+p.getName()+".xp",p3);
		
		saveConfig2();
		reloadConfig2();
		scoreboardUpdate(p);
	}
	
	public void addXp(Player p, int xp) {
		int need_xp = config2.getInt("islands."+p.getName()+".need_xp");
		int xp2 = config2.getInt("islands."+p.getName()+".xp");
		
		if(xp2 + xp >= need_xp) {
			addLevel(p);
			if(xp2+xp-need_xp != 0) addXp(p, xp2+xp-need_xp);
		}
		else {
			config2.set("islands."+p.getName()+".xp", config2.getInt("islands."+p.getName()+".xp")+xp);
			saveConfig2();
			reloadConfig2();
		}
	}
	
	String getProgressBar(Player p) {
		int need_xp = config2.getInt("islands."+p.getName()+".need_xp");
		int xp = config2.getInt("islands."+p.getName()+".xp");
		
		int f = (int)((10*xp)/need_xp);
		
		String str = "§c";
		boolean did = false;
		for(int i = 0; i < 10; ++i) {
			if(i >= f && !did) {
				str += "§f";
				did = true;
			}
			str += "|";
		}
		return str;
	}
	
	Inventory getKits(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, lang.getString("kits"));
		List<String> lore = new ArrayList<String>();
		lore.add(lang.getString("click_to_get"));
		try {
			if(isKit.contains(p.getName())) {
				lore.set(0, lang.getString("you_already_got_kit_gui"));
			}
		} catch(Exception ee) {};
		
		ItemStack it = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("kit")+ " §7"+lang.getString("kit_1"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(0, it);
		
		it = new ItemStack(Material.SLIME_BALL);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("kit")+" §6VIP");
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(1, it);
		
		it = new ItemStack(Material.SLIME_BALL);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("kit")+" §6VIP§b+");
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(2, it);

		it = new ItemStack(Material.SLIME_BALL);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("kit")+" §bMVP");
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(3, it);

		it = new ItemStack(Material.SLIME_BALL);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("kit")+" §bMVP§6+");
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(4, it);

		it = new ItemStack(Material.SLIME_BALL);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("kit")+" §1Premium");
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(5, it);
		
		
		return inv;
	}
	
	public void scoreboardUpdate(Player p) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("score", "dummy");        
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§b§lSkyBlock");
        long bal = ((long)econ.getBalance(p));
    	Score sc = objective.getScore(lang.getString("scoreboard.1")+bal);
        sc.setScore(9);
        sc = objective.getScore(lang.getString("scoreboard.2"));
        sc.setScore(8);
        sc = objective.getScore(lang.getString("scoreboard.3")+p.getName());
        sc.setScore(7);
        sc = objective.getScore(lang.getString("scoreboard.4")+String.valueOf(getLevel(p)));
        sc.setScore(6);
        sc = objective.getScore(lang.getString("scoreboard.5")+"—"+getProgressBar(p)+"§b—");
        sc.setScore(5);
        sc = objective.getScore(lang.getString("scoreboard.6")+String.valueOf(getStrength(p)));
        sc.setScore(4);
        sc = objective.getScore(lang.getString("scoreboard.7")+String.valueOf(getProt(p)));
        sc.setScore(3);
        sc = objective.getScore(" ");
        sc.setScore(2);
        sc = objective.getScore(lang.getString("scoreboard.8"));
        sc.setScore(1);
        p.setScoreboard(board);
	}
	
	Inventory getTop() {
		Inventory inv = Bukkit.createInventory(null, 9*5, lang.getString("top.1"));
		
		int i = 0;
		
		for(Island is : top) {
			ItemStack it = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta metas = (SkullMeta) it.getItemMeta();
			metas.setDisplayName("§e"+is.player);
			if(i <= 5) metas.setOwner(is.player);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(lang.getString("top.2")+String.valueOf(is.lvl));
			lore.add(lang.getString("top.3")+String.valueOf(is.strength));
			lore.add(lang.getString("top.4")+String.valueOf(is.prot));
			metas.setLore(lore);
			it.setItemMeta(metas);
			inv.setItem(i, it);
			
			++i;
		}
		
		return inv;
	}
	
	Inventory Shop() {
		Inventory inv = Bukkit.createInventory(null, 9*5, lang.getString("shop.1"));
		
		List<String> sh = config.getStringList("shop");
		int i = 0;
		for(String item : sh) {
			String[] spl = item.split(", ");
			ItemStack it = new ItemStack(Material.getMaterial(spl[0]), Integer.valueOf(spl[1]));
			ItemMeta meta = it.getItemMeta();
			if(spl.length == 4) {
				meta.setDisplayName(spl[3]);
			}
			List<String> lore = new ArrayList<String>();
			lore.add(lang.getString("shop.2").replaceAll("%money%", spl[2]));
			meta.setLore(lore);
			it.setItemMeta(meta);
			inv.setItem(i, it);
			++i;
		}
		
		return inv;
	}
	
	Inventory Guide(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*5, lang.getString("guide.1"));
		
		ItemStack it = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta metas = (SkullMeta) it.getItemMeta();
		metas.setDisplayName(lang.getString("guide.2"));
		metas.setOwner(p.getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(lang.getString("guide.3"));
		lore.add(lang.getString("guide.4"));
		lore.add(lang.getString("guide.5"));
		metas.setLore(lore);
		it.setItemMeta(metas);
		inv.setItem(13, it);

		it = new ItemStack(Material.SLIME_BALL);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.6"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.7"));
		lore.add(lang.getString("guide.8"));
		lore.add(lang.getString("guide.9"));
		lore.add(lang.getString("guide.10"));
		lore.add(lang.getString("guide.11"));
		lore.add(lang.getString("guide.12"));
		lore.add(lang.getString("guide.13"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(11, it);
		
		it = new ItemStack(Material.CHEST);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.14"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.15"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(4, it);
		
		it = new ItemStack(Material.FIRE_CHARGE);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.16"));
		it.setItemMeta(meta);
		inv.setItem(15, it);
		
		it = new ItemStack(Material.BOOK);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.17"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.18"));
		lore.add(lang.getString("guide.19"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(28, it);
		
		it = new ItemStack(Material.NETHER_STAR);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.20"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.21"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(34, it);

		it = new ItemStack(Material.DRAGON_HEAD);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.22"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.23"));
		lore.add(lang.getString("guide.24"));
		lore.add(lang.getString("guide.25"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(21, it);
		
		if(!config2.contains("islands."+p.getName()+".store")) {
			config2.set("islands."+p.getName()+".store", 5);
			config2.set("islands."+p.getName()+".money", 0);
		}
		int store = config2.getInt("islands."+p.getName()+".store");
		int money = config2.getInt("islands."+p.getName()+".money");
		
		it = new ItemStack(Material.GOLD_NUGGET);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.26"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.27").replaceAll("%money%", String.valueOf(money)));
		lore.add(lang.getString("guide.28").replaceAll("%money%", String.valueOf(store*1000)));
		lore.add(lang.getString("guide.29").replaceAll("%money%", String.valueOf((int)(store*1000/5))));
		lore.add(lang.getString("guide.30"));
		lore.add(lang.getString("guide.31"));
		lore.add(lang.getString("guide.32"));
		lore.add(lang.getString("guide.33"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(22, it);
		
		if(!config2.contains("islands."+p.getName()+".ore")) {
			config2.set("islands."+p.getName()+".ore", 1);
		}
		int ore = config2.getInt("islands."+p.getName()+".ore");
		
		int price = 100*ore;
		int lvl = ore;
        
        double o1 = Math.round(100.0*(ore-(double)ore/2))/100.0;
        double o2 = Math.round(100.0*((double)ore/2-(double)ore/4))/100.0;
        double o3 = Math.round(100.0*((double)ore/3-(double)ore/5))/100.0;
        double o4 = Math.round(100.0*((double)ore/3-(double)ore/5))/100.0;
        double o5 = Math.round(100.0*((double)ore/4-(double)ore/6))/100.0;
        double o6 = Math.round(100.0*((double)ore/5-(double)ore/7))/100.0;
        double o7 = Math.round(100.0*((double)ore/7-(double)ore/8))/100.0;
        double o8 = Math.round(100.0*((double)ore/12-(double)ore/14))/100.0;
		
		it = new ItemStack(Material.IRON_ORE);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.34"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.35").replaceAll("%money%", String.valueOf(price)));
		lore.add(lang.getString("guide.36")+String.valueOf(lvl));
		lore.add(" ");
		lore.add(lang.getString("guide.37") + String.valueOf(o1)+"%");
		lore.add(lang.getString("guide.38") + String.valueOf(o2)+"%");
		lore.add(lang.getString("guide.39") + String.valueOf(o3)+"%");
		lore.add(lang.getString("guide.40")+ String.valueOf(o4)+"%");
		lore.add(lang.getString("guide.41") + String.valueOf(o5)+"%");
		lore.add(lang.getString("guide.42") + String.valueOf(o6)+"%");
		lore.add(lang.getString("guide.43") + String.valueOf(o7)+"%");
		lore.add(lang.getString("guide.44") + String.valueOf(o8)+"%");
		lore.add(" ");
		lore.add(lang.getString("guide.45"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(23, it);
		
		
		it = new ItemStack(Material.OAK_SIGN);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("guide.46"));
		lore = new ArrayList<String>();
		lore.add(lang.getString("guide.47"));
		lore.add(lang.getString("guide.48"));
		lore.add(lang.getString("guide.49"));
		lore.add(lang.getString("guide.50"));
		lore.add(lang.getString("guide.51"));
		lore.add(lang.getString("guide.52"));
		lore.add(lang.getString("guide.53"));
		lore.add(lang.getString("guide.54"));
		lore.add(lang.getString("guide.55"));
		lore.add(lang.getString("guide.56"));
		lore.add(lang.getString("guide.57"));
		lore.add(lang.getString("guide.58"));
		lore.add(lang.getString("guide.59"));
		lore.add(lang.getString("guide.60"));
		lore.add(lang.getString("guide.61"));
		lore.add(lang.getString("guide.62"));
		lore.add(lang.getString("guide.63"));
		lore.add(lang.getString("guide.64"));
		lore.add(lang.getString("guide.65"));
		lore.add(lang.getString("guide.66"));
		lore.add(lang.getString("guide.67"));
		lore.add(lang.getString("guide.68"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(31, it);
		
		return inv;
	}
	
	Inventory Properties(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*1, lang.getString("properties.1"));

		int size = getIslandSize(p);
		int price = ((size+10)*(size+10))/2;
		int lvl = (int) Math.ceil((double)(((size+10)*(size+10))/720));
		
		
		ItemStack it = new ItemStack(Material.BOOK);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("properties.2"));
		it.setItemMeta(meta);
		inv.setItem(3, it);

		it = new ItemStack(Material.WRITABLE_BOOK);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("properties.3"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(lang.getString("properties.4"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(5, it);
		
		it = new ItemStack(Material.SLIME_BALL);
		meta = it.getItemMeta();
		meta.setDisplayName("§f§l←");
		it.setItemMeta(meta);
		inv.setItem(0, it);
		
		Location lc = getIslandCenter(p);
		it = new ItemStack(Material.IRON_SWORD);
		meta = it.getItemMeta();
		if(isPvP(lc)) {
			meta.setDisplayName(lang.getString("properties.5"));
		}
		else {
			meta.setDisplayName(lang.getString("properties.6"));
		}
		lore = new ArrayList<String>();
		lore.add(lang.getString("properties.7"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(8, it);
		
		it = new ItemStack(Material.FLINT_AND_STEEL);
		meta = it.getItemMeta();
		if(isPortal(lc)) {
			meta.setDisplayName(lang.getString("properties.8"));
		}
		else {
			meta.setDisplayName(lang.getString("properties.9"));
		}
		lore = new ArrayList<String>();
		lore.add(lang.getString("properties.10"));
		lore.add(lang.getString("properties.11"));
		lore.add(lang.getString("properties.12"));
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(7, it);
		
		it = new ItemStack(Material.BARRIER);
		meta = it.getItemMeta();
		meta.setDisplayName(lang.getString("properties.13"));
		lore = new ArrayList<String>();
		if(size < 200) {
			lore.add("§c"+String.valueOf(size) + "§7§l → §c" + String.valueOf(size+10));
			lore.add(lang.getString("properties.14")+price);
			lore.add(lang.getString("properties.15")+String.valueOf(lvl));
		}
		else {
			lore.add(lang.getString("properties.16"));
		}
		meta.setLore(lore);
		it.setItemMeta(meta);
		inv.setItem(4, it);
		
		return inv;
	}
	
	Inventory getMembers(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*5, lang.getString("members.1"));
		
		ArrayList<String> members = (ArrayList<String>) config2.get("islands."+p.getName()+".members");
		if(members != null && !members.isEmpty()) {
			int i = 0;
			for(String member : members) {
				ItemStack it = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) it.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(lang.getString("members.2"));
				meta.setOwner(member);
				meta.setDisplayName("§e"+member);
				meta.setLore(lore);
				it.setItemMeta(meta);
				inv.setItem(i, it);
				++i;
			}
		}
		
		return inv;
	}
	
	boolean haveMember(Player p, String member) {
		ArrayList<String> members = (ArrayList<String>) config2.get("islands."+p.getName()+".members");
		if(members.contains(member)) return true;
		return false;
	}
	
	boolean inIsland(Player p, String island) {
		ArrayList<String> islands = (ArrayList<String>) config2.get("islands."+p.getName()+".in_islands");
		if(islands.contains(island)) return true;
		return false;
	}
	
	Inventory getAddMembers(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*5, lang.getString("members.3"));
		int i = 0;
		
		for(Player pl : Bukkit.getOnlinePlayers()) {
			if(!pl.getName().equals(p.getName()) && getDistance(pl.getLocation(), p.getLocation()) <= 100 && !inIsland(pl, p.getName())) {
				ItemStack it = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) it.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(lang.getString("members.4"));
				meta.setOwner(pl.getName());
				meta.setDisplayName("§e"+pl.getName());
				meta.setLore(lore);
				it.setItemMeta(meta);
				inv.setItem(i, it);
				++i;
			}
		}
		
		return inv;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getType() == InventoryType.ANVIL) {
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().getType() == Material.MOJANG_BANNER_PATTERN || e.getCurrentItem().getType() == Material.SNOWBALL) {
					e.setCancelled(true);
				}
			}
		}
		else if(e.getInventory().equals(bosses)) {
			ItemStack it = e.getCurrentItem();
			if(it != null) {
				if(it.getType() == Material.MAGMA_CREAM) {
					ItemMeta meta = it.getItemMeta();
					try {
						List<String> lore = meta.getLore();
						for(String s : lore) {
							if(s.contains(lang.getString("bosses.8"))) {
								send(p, s);
								p.closeInventory();
								break;
							}
						}
					} catch (Exception ee) {};
				}
			}
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(player_kit.get(p.getName()))) {
			ItemStack it = e.getCurrentItem();
			if(it != null && it.getType() == Material.SLIME_BALL) {
				if(isKit.contains(p.getName())) {
					send(p, lang.getString("you_already_got_kit"));
				}
				else {
					try {
						List<String> lore = it.getItemMeta().getLore();
						if(lore.size() > 0) {
							ItemStack comp = new ItemStack(Material.COMPASS);
							ItemMeta meta = comp.getItemMeta();
							meta.setDisplayName("§f§o[x] [y] [z]");
							List<String> lore2 = new ArrayList<String>();
							lore2.add(lang.getString("portals.7"));
							lore2.add(lang.getString("portals.8"));
							lore2.add(lang.getString("portals.9"));
							meta.setLore(lore2);
							comp.setItemMeta(meta);
							
							String s = it.getItemMeta().getDisplayName().split(" ")[1].substring(2);
							if(s.contains(lang.getString("kit_1"))) {
								send(p, lang.getString("got_kit"));
								p.closeInventory();
								isKit.add(p.getName());
								p.getInventory().addItem(new ItemStack(Material.ANVIL));
								p.getInventory().addItem(comp);
								p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
								p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
								p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
								p.updateInventory();
							}
							else if(s.contains("Premium")) {
								if(p.hasPermission("API.premium")) {
									send(p, lang.getString("got_kit"));
									isKit.add(p.getName());
									p.closeInventory();
									p.getInventory().addItem(new ItemStack(Material.ANVIL));
									p.getInventory().addItem(comp);
									p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
									p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 16));
									p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 16));
									p.getInventory().addItem(new ItemStack(Material.DIAMOND, 4));
									p.getInventory().addItem(new ItemStack(Material.EMERALD, 16));
									p.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP, 2));
									p.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 64));
									p.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
									p.getInventory().addItem(new ItemStack(Material.DIRT, 64));
									p.updateInventory();
								}
								else {
									send(p, lang.getString("no_access"));
								}
							}
							else if(s.contains("MVP§6+")) {
								if(p.hasPermission("API.mvp+")) {
									send(p, lang.getString("got_kit"));
									isKit.add(p.getName());
									p.closeInventory();
									p.getInventory().addItem(new ItemStack(Material.ANVIL));
									p.getInventory().addItem(comp);
									p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
									p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
									p.getInventory().addItem(new ItemStack(Material.EMERALD, 16));
									p.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP, 1));
									p.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 32));
									p.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 64));
									p.updateInventory();
								}
								else {
									send(p, lang.getString("no_access"));
								}
							}
							else if(s.contains("MVP")) {
								if(p.hasPermission("API.mvp")) {
									send(p, lang.getString("got_kit"));
									isKit.add(p.getName());
									p.closeInventory();
									p.getInventory().addItem(new ItemStack(Material.ANVIL));
									p.getInventory().addItem(comp);
									p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
									p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.DIAMOND, 2));
									p.getInventory().addItem(new ItemStack(Material.EMERALD, 8));
									p.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 32));
									p.updateInventory();
								}
								else {
									send(p, lang.getString("no_access"));
								}
							}
							else if(s.contains("VIP§b+")) {
								if(p.hasPermission("API.vip+")) {
									send(p, lang.getString("got_kit"));
									isKit.add(p.getName());
									p.closeInventory();
									p.getInventory().addItem(new ItemStack(Material.ANVIL));
									p.getInventory().addItem(comp);
									p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
									p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
									p.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 16));
									p.updateInventory();
								}
								else {
									send(p, lang.getString("no_access"));
								}
							}
							else if(s.contains("VIP")) {
								if(p.hasPermission("API.vip")) {
									send(p, lang.getString("got_kit"));
									isKit.add(p.getName());
									p.closeInventory();
									p.getInventory().addItem(new ItemStack(Material.ANVIL));
									p.getInventory().addItem(comp);
									p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
									p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
									p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 8));
									p.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 16));
									p.updateInventory();
								}
								else {
									send(p, lang.getString("no_access"));
								}
							}
						}
					} catch(Exception ee) {
						e.setCancelled(true);
						p.closeInventory();
					};
				}
			}
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(player_guide.get(p.getName()))) {
			if(e.getCurrentItem() != null) {
				ItemStack it = e.getCurrentItem();
				if(it.getType() == Material.PLAYER_HEAD) {
					if(onIsland(p.getLocation(), p)) {
						if(player_properties.containsKey(p.getName())) {
							player_properties.replace(p.getName(), Properties(p));
						}
						else player_properties.put(p.getName(), Properties(p));
						p.openInventory(player_properties.get(p.getName()));
					}
					else send(p, lang.getString("guide.69"));
				}
				else if(it.getType() == Material.SLIME_BALL) {
					p.chat(config.getString("auction_cmd"));
				}
				else if(it.getType() == Material.NETHER_STAR) {
					p.openInventory(top45);
				}
				else if(it.getType() == Material.GOLD_NUGGET) {
					int money = config2.getInt("islands."+p.getName()+".money");
					int store = 1000*config2.getInt("islands."+p.getName()+".store");
					if(e.getClick() == ClickType.LEFT) {
						if(econ.getBalance(p.getName()) >= 100) {
							if(money+100 <= store) {
								econ.withdrawPlayer(p.getName(), 100);
								config2.set("islands."+p.getName()+".money", money+100);
								saveConfig2();
								reloadConfig2();
								send(p, lang.getString("guide.70"));
								p.closeInventory();
								if(!player_guide.containsKey(p.getName())) {
									player_guide.put(p.getName(), Guide(p));
								}
								else player_guide.replace(p.getName(), Guide(p));
								p.openInventory(player_guide.get(p.getName()));
								scoreboardUpdate(p);
							}
							else send(p, lang.getString("guide.71"));
						}
						else send(p, lang.getString("guide.72"));
					}
					else if(e.getClick() == ClickType.RIGHT) {
						if(money >= 100) {
							econ.depositPlayer(p.getName(), 100);
							config2.set("islands."+p.getName()+".money", money-100);
							saveConfig2();
							reloadConfig2();
							send(p, lang.getString("guide.73"));
							p.closeInventory();
							if(!player_guide.containsKey(p.getName())) {
								player_guide.put(p.getName(), Guide(p));
							}
							else player_guide.replace(p.getName(), Guide(p));
							p.openInventory(player_guide.get(p.getName()));
							scoreboardUpdate(p);
						}
						else send(p, lang.getString("guide.74"));
					}
					else if(e.getClick() == ClickType.SHIFT_RIGHT) {
						if(econ.getBalance(p.getName()) >= ((int) (store/5))) {
							econ.withdrawPlayer(p.getName(), ((int) (store/5)));
							config2.set("islands."+p.getName()+".store", (int) (store/1000)+5);
							saveConfig2();
							reloadConfig2();
							send(p, lang.getString("guide.75"));
							p.closeInventory();
							scoreboardUpdate(p);
						}
						else send(p, lang.getString("guide.76"));
					}
					else if(e.getClick() == ClickType.SHIFT_LEFT) {
						if(econ.getBalance(p.getName()) >= 1000) {
							if(money+1000 <= store) {
								econ.withdrawPlayer(p.getName(), 1000);
								config2.set("islands."+p.getName()+".money", money+1000);
								saveConfig2();
								reloadConfig2();
								send(p, lang.getString("guide.77"));
								p.closeInventory();
								if(!player_guide.containsKey(p.getName())) {
									player_guide.put(p.getName(), Guide(p));
								}
								else player_guide.replace(p.getName(), Guide(p));
								p.openInventory(player_guide.get(p.getName()));
								scoreboardUpdate(p);
							}
							else send(p, lang.getString("guide.71"));
						}
						else send(p, lang.getString("guide.72"));
					}
				}
				else if(it.getType() == Material.CHEST) {
					if(player_kit.containsKey(p.getName())) {
						player_kit.replace(p.getName(), getKits(p));
					}
					else {
						player_kit.put(p.getName(), getKits(p));
					}
					p.openInventory(player_kit.get(p.getName()));
				}
				else if(it.getType() == Material.BOOK) {
					if(player_quests.containsKey(p)) {
						player_quests.replace(p, getTask(p));
					}
					else player_quests.put(p, getTask(p));
					p.openInventory(player_quests.get(p));
				}
				else if(it.getType() == Material.FIRE_CHARGE) {
					p.openInventory(shop);
				}
				else if(it.getType() == Material.DRAGON_HEAD) {
					p.openInventory(bosses);
				}
				else if(it.getType() == Material.IRON_ORE) {
					int ore = config2.getInt("islands."+p.getName()+".ore");
					
					int price = 100*ore;
					int lvl = ore;
					
					if(getLevel(p) >= lvl) {
						if(econ.getBalance(p) >= price) {
							econ.withdrawPlayer(p, price);
							config2.set("islands."+p.getName()+".ore", ore+1);
							saveConfig2();
							reloadConfig2();
							send(p, lang.getString("guide.75"));
							p.closeInventory();
						}
						else send(p, lang.getString("guide.72"));
					}
					else send(p, lang.getString("guide.78"));
				}
			}
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(player_properties.get(p.getName()))) {
			if(e.getCurrentItem() != null) {
				ItemStack it = e.getCurrentItem();
				if(it.getType() == Material.BOOK) {
					if(player_members.containsKey(p.getName())) {
						player_members.replace(p.getName(), getMembers(p));
					}
					else player_members.put(p.getName(), getMembers(p));
					p.openInventory(player_members.get(p.getName()));
				}
				else if(it.getType() == Material.WRITABLE_BOOK) {
					if(player_addmember.containsKey(p.getName())) {
						player_addmember.replace(p.getName(), getAddMembers(p));
					}
					else player_addmember.put(p.getName(), getAddMembers(p));
					p.openInventory(player_addmember.get(p.getName()));
				}
				else if(it.getType() == Material.BARRIER) {
					int size = getIslandSize(p);
					int price = ((size+10)*(size+10))/2;
					int lvl = (int) Math.ceil((double)(((size+10)*(size+10))/720));
					if(econ.getBalance(p) >= price) {
						if(getLevel(p) >= lvl) {
							if(size < 200) {
								econ.withdrawPlayer(p, price);
								setIslandSize(p, size+10);
								send(p, lang.getString("guide.79"));
								p.closeInventory();
							}
							else send(p, lang.getString("guide.80"));
						}
						else send(p, lang.getString("guide.78"));
					}
					else send(p, lang.getString("guide.81"));
				}
				else if(it.getType() == Material.SLIME_BALL) {
					p.openInventory(player_guide.get(p.getName()));
				}
				else if(it.getType() == Material.IRON_SWORD) {
					changePvP(p);
					boolean pvp = isPvP(getIslandCenter(p));
					if(pvp) {
						send(p, lang.getString("guide.82"));
					}
					else {
						send(p, lang.getString("guide.83"));
					}
					p.closeInventory();
				}
				else if(it.getType() == Material.FLINT_AND_STEEL) {
					changePortal(p);
					boolean portal = isPortal(getIslandCenter(p));
					if(portal) {
						send(p, lang.getString("guide.84"));
					}
					else {
						send(p, lang.getString("guide.85"));
					}
					p.closeInventory();
				}
			}
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(player_members.get(p.getName()))) {
			if(e.getCurrentItem() != null) {
				ItemStack it = e.getCurrentItem();
				if(it.getType() == Material.PLAYER_HEAD) {
					ItemMeta meta = it.getItemMeta();
					String name = meta.getDisplayName().substring(2);
					ArrayList<String> members = (ArrayList<String>) config2.get("islands."+p.getName()+".members");
					if(members != null && !members.isEmpty() && members.contains(name)) {
						members.remove(name);
						config2.set("islands."+p.getName()+".members", members);
						send(p, lang.getString("guide.86").replaceAll("%player%", name));
						Player pl = Bukkit.getPlayer(name);
						if(pl != null && pl.isOnline()) {
							send(pl, lang.getString("guide.87").replaceAll("%player%", p.getName()));
						}
						ArrayList<String> inIslands = (ArrayList<String>) config2.get("islands."+name+".in_islands");
						if(inIslands != null && !inIslands.isEmpty() && inIslands.contains(p.getName())) {
							inIslands.remove(p.getName());
							config2.set("islands."+name+".in_islands", inIslands);
						}
					}
					saveConfig2();
					reloadConfig2();
					p.closeInventory();
				}
			}
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(player_addmember.get(p.getName()))) {
			if(e.getCurrentItem() != null) {
				ItemStack it = e.getCurrentItem();
				if(it.getType() == Material.PLAYER_HEAD) {
					ItemMeta meta = it.getItemMeta();
					String name = meta.getDisplayName().substring(2);
					ArrayList<String> members = (ArrayList<String>) config2.get("islands."+p.getName()+".members");
					if(members.size() < 9*5) {
						members.add(name);
						config2.set("islands."+p.getName()+".members", members);
						send(p, lang.getString("guide.88").replaceAll("%player%", name));
						Player pl = Bukkit.getPlayer(name);
						if(pl != null && pl.isOnline()) {
							send(pl, lang.getString("guide.89").replaceAll("%player%", p.getName()));
						}
						ArrayList<String> inIslands = (ArrayList<String>) config2.get("islands."+name+".in_islands");
						if(inIslands != null) {
							inIslands.add(p.getName());
							config2.set("islands."+name+".in_islands", inIslands);
						}
						saveConfig2();
						reloadConfig2();
						p.closeInventory();
					}
					else send(p, lang.getString("guide.90"));
				}
			}
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(player_quests.get(p))) {
			if(e.getCurrentItem() != null) {
				ItemStack it = e.getCurrentItem();
				ItemMeta meta = it.getItemMeta();
				List<String> lore = meta.getLore();
				if(lore == null || lore.isEmpty() || it.getType() == Material.END_CRYSTAL) {
					e.setCancelled(true);
				}
				else {
					String l = lore.get(0);
					String[] parse = l.split(" ");
					int price = Integer.valueOf(parse[2].substring(2));
					int done = Integer.valueOf(lore.get(1).split(" ")[2].substring(2, 3));
					List<String> pair = new ArrayList<String>();
					pair.add(p.getName());
					pair.add(it.getType().toString());
					if(done == 5) {
						p.sendMessage(lang.getString("guide.91"));
					}
					if(hasItem(p.getInventory(), it.getType(), it.getAmount())) {
						econ.depositPlayer(p, price);
						p.sendMessage(lang.getString("guide.92"));
						removeItem(p.getInventory(), it.getType(), it.getAmount());
						if(limit.containsKey(pair)) {
							limit.replace(pair, limit.get(pair)+1);
						}
						else limit.put(pair, 1);
					}
					else p.sendMessage(lang.getString("guide.93"));
				}
			}
			player_quests.replace(p, getTask(p));
			p.openInventory(player_quests.get(p));
			e.setCancelled(true);
		}
		else if(e.getInventory().equals(top45)) e.setCancelled(true);
		else if(e.getInventory().equals(shop)) {
			ItemStack it = e.getCurrentItem();
			if(it != null) {
				ItemMeta meta = it.getItemMeta();
				List<String> lore = meta.getLore();
				if(lore != null && !lore.isEmpty()) {
					String s = lore.get(0);
					try {
						int price = Integer.valueOf(s.split(" ")[1].substring(2));
						if(econ.getBalance(p) >= price) {
							econ.withdrawPlayer(p, price);
							ItemStack it2 = it.clone();
							ItemMeta meta2 = it2.getItemMeta();
							meta2.setLore(new ArrayList<String>());
							it2.setItemMeta(meta2);
							
							p.getInventory().addItem(it2);
							send(p, lang.getString("guide.94"));
							p.updateInventory();
						}
						else send(p, lang.getString("guide.95"));
					} catch (Exception ee) {};
				}
			}
			e.setCancelled(true);
		}
		if(!e.isCancelled()) {
			ItemStack it = e.getCurrentItem();
			ItemMeta meta = it.getItemMeta();
			if(it.getType() == Material.OAK_BUTTON && meta.getDisplayName().equals(getMed().getItemMeta().getDisplayName())) {
				int am = it.getAmount();
				p.sendMessage(lang.getString("got_money").replaceAll("%money%", String.valueOf(am)));
				econ.depositPlayer(p.getName(), am);
				try {
					it.setAmount(0);
					it.setType(null);
				} catch (Exception ee) {};
				p.updateInventory();
			}
		}
	}
	
	@EventHandler
	public void PickupItem(PlayerPickupItemEvent e) {
	    Player p = e.getPlayer();
	    ItemStack item = e.getItem().getItemStack();
	    if (item.isSimilar(getMed())) {
	        p.sendMessage(lang.getString("got_money").replaceAll("%money%", String.valueOf(item.getAmount())));
	        econ.depositPlayer(p.getName(), item.getAmount());
		    e.setCancelled(true);
		    e.getItem().remove();
		    for(int i = 0; i < 1 + item.getAmount()/8; ++i) p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 5, 5);
	    }
	}
	
	Inventory getTask(Player p) {
        String name = p.getName();
        Inventory task = Bukkit.createInventory(null, 45, lang.getString("quests.3"));
        int i = 0;
        boolean all = true;
        for (ItemStack it : items_today) {
            ItemStack it2 = new ItemStack(it.getType(), it.getAmount());
            List<String> pair = new ArrayList<String>();
            pair.add(name);
            pair.add(it.getType().toString());
            if (limit.containsKey(pair)) {
                ItemMeta meta = it2.getItemMeta();
                ItemMeta meta2 = it.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(meta2.getLore().get(0));
                lore.add(lang.getString("quests.4") + String.valueOf(this.limit.get(pair)) + "§7/§c5");
                if (limit.get(pair) < 5) {
                    all = false;
                }
                meta.setLore(lore);
                it2.setItemMeta(meta);
                task.setItem(i, it2);
            }
            else {
                all = false;
                task.setItem(i, it);
            }
            ++i;
        }
        if (!done.contains(p.getName()) && all) {
            p.sendMessage(lang.getString("quests.5").replaceAll("%money%", String.valueOf(config.getInt("money_complete_all_quests"))));
            econ.depositPlayer(p, Double.valueOf(config.getInt("money_complete_all_quests")));
            done.add(p.getName());
        }
        ItemStack dones = new ItemStack(Material.END_CRYSTAL);
        ItemMeta dm = dones.getItemMeta();
        dm.setDisplayName(lang.getString("quests.6"));
        List<String> lr = new ArrayList<String>();
        for (String pl_name : this.done) {
            lr.add("§f- §7" + pl_name);
            if (lr.size() >= 10) {
                lr.add(lang.getString("quests.7"));
                break;
            }
        }
        if (!lr.isEmpty()) {
            dm.setLore(lr);
        }
        dones.setItemMeta(dm);
        task.setItem(40, dones);
        return task;
    }
	
	@EventHandler
	public void interact(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof Villager) {
			if(p.getItemInHand().getType() == Material.NAME_TAG) e.setCancelled(true);
			Villager vil = (Villager) e.getRightClicked();
			if(!vil.getScoreboardTags().isEmpty()) {
				for(String tag : vil.getScoreboardTags()) {
					if(tag.equals(p.getName())) {
						ArrayList<String> guide = (ArrayList<String>) config.get("guide_messages");
						int ran = new Random().nextInt(guide.size());
						if(!player_guide.containsKey(p.getName())) {
							player_guide.put(p.getName(), Guide(p));
						}
						else player_guide.replace(p.getName(), Guide(p));
						p.openInventory(player_guide.get(p.getName()));
						send(p, guide.get(ran));
						e.setCancelled(true);
						break;
					}
					else if(!vil.getCustomName().contains("❤")) {
						ArrayList<String> guide = (ArrayList<String>) config.get("guide_messages");
						int ran = new Random().nextInt(guide.size());
						if(!player_guide.containsKey(p.getName())) {
							player_guide.put(p.getName(), Guide(p));
						}
						else player_guide.replace(p.getName(), Guide(p));
						p.openInventory(player_guide.get(p.getName()));
						send(p, guide.get(ran));
						e.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
	Location randomLocation(Location center, int R) {
		int x = center.getBlockX();
		int y = center.getBlockY();
		int z = center.getBlockZ();
		
		int at = 0;
		if(R > 0) {
			while(at <= 15) {
				++at;
				Location loc = new Location(center.getWorld(), (x-R) + new Random().nextInt(2*R), y, (z-R) + new Random().nextInt(2*R));
				if(x == loc.getBlockX() && z == loc.getBlockZ()) continue;
				if(getDistance(loc, center) <= R) return loc;
			}
		}
		return null;
	}
	
	Location getPortalByCenter(Location center) {
		for(int x = center.getBlockX()-1; x <= center.getBlockX()+1; ++x) {
			for(int z = center.getBlockZ()-4; z <= center.getBlockZ()-2; ++z) {
				Location lc = new Location(center.getWorld(), x, center.getBlockY()+1, z);
				if(lc.getBlock().getType() == Material.NETHER_PORTAL) {
					return getPortal(lc);
				}
			}
		}
		return null;
	}
	
	Location randomLocation3(Location center) {
		int x = center.getBlockX();
		int y = center.getBlockY();
		int z = center.getBlockZ();
		
		Location loc = new Location(center.getWorld(), (x-1) + new Random().nextInt(3), y, z - 2 - new Random().nextInt(3));
		return loc;
	}
	
	Location randomLocation2(Location center, int R) {
		int x = center.getBlockX();
		int y = center.getBlockY();
		int z = center.getBlockZ();
		
		int at = 0;
		if(R > 0) {
			while(at <= 15) {
				++at;
				Location loc = new Location(center.getWorld(), (x-R) + new Random().nextInt(2*R), y, (z-R) + new Random().nextInt(2*R));
				if(x == loc.getBlockX() || z == loc.getBlockZ()) continue;
				if(getDistance(loc, center) <= R) return loc;
			}
		}
		return null;
	}
	
	public void genGenerator(Location loc, String TYPE) {
		if(loc == null) return;
		loc.getBlock().setType(Material.COBBLESTONE);
		if(!TYPE.equals("player")) {
			delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));;
		}
		loc.add(-1, 0, 0).getBlock().setType(Material.LAVA);
		if(!TYPE.equals("player")) {
			delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		}
		loc.add(2, 0, 0).getBlock().setType(Material.AIR);
		if(!TYPE.equals("player")) {
			delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		}
		loc.add(0, -1, 0).getBlock().setType(Material.AIR);
		if(!TYPE.equals("player")) {
			delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		}
		loc.add(1, 1, 0).getBlock().setType(Material.WATER);
		if(!TYPE.equals("player")) {
			delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		}
	}
	
	public void genPortal(Location loc, String TYPE) {
		if(loc == null) return;
		for(int x = loc.getBlockX()-1; x <= loc.getBlockX()+2; ++x) {
			Location lc = new Location(loc.getWorld(), x, loc.getBlockY(), loc.getBlockZ());
			lc.getBlock().setType(Material.OBSIDIAN);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
			lc = new Location(loc.getWorld(), x, loc.getBlockY()+4, loc.getBlockZ());
			lc.getBlock().setType(Material.OBSIDIAN);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
		}
		for(int y = loc.getBlockY()+1; y <= loc.getBlockY()+3; ++y) {
			Location lc = new Location(loc.getWorld(), loc.getBlockX()-1, y, loc.getBlockZ());
			lc.getBlock().setType(Material.OBSIDIAN);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
			lc = new Location(loc.getWorld(), loc.getBlockX()+2, y, loc.getBlockZ());
			lc.getBlock().setType(Material.OBSIDIAN);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
			lc = new Location(loc.getWorld(), loc.getBlockX(), y, loc.getBlockZ());
			lc.getBlock().setType(Material.NETHER_PORTAL);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
			lc = new Location(loc.getWorld(), loc.getBlockX()+1, y, loc.getBlockZ());
			lc.getBlock().setType(Material.NETHER_PORTAL);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
		}
	}
	
	public void genChest(Location loc, String TYPE) {
		if(loc == null) return;
		if(loc.getBlock().getType() != Material.LAVA && loc.getBlock().getType() != Material.WATER && loc.getBlock().getType() != Material.AIR) {
			Location lc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ());
			if(lc.getBlock().getType() == Material.AIR) {
				lc.getBlock().setType(Material.CHEST);
				chests.add(lc);
				if(!TYPE.equals("player")) delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
		}
	}
	
	public void genBlock(Location loc, Material mat, String TYPE) {
		if(loc == null) return;
		if(loc.getBlock().getType() != Material.LAVA && loc.getBlock().getType() != Material.WATER && loc.getBlock().getType() != Material.AIR) {
			Location lc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ());
			if(lc.getBlock().getType() == Material.AIR) {
				lc.getBlock().setType(mat);
				if(!TYPE.equals("player")) {
					delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
				}
			}
		}
	}
	
	public void genTree(Location loc, Material log, Material leaf, String TYPE) {
		if(loc == null) return;
		int h = 3 + new Random().nextInt(5);
		int R = (int) Math.ceil((double)h/2);
		if(loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.LAVA) return;
		for(int y = loc.getBlockY()+1; y <= loc.getBlockY()+h+R; ++y) {
			Location lc = new Location(loc.getWorld(), loc.getBlockX(), y, loc.getBlockZ());
			if(lc.getBlock().getType() != Material.AIR) {
				return;
			}
		}
		for(int y = loc.getBlockY()+1; y <= loc.getBlockY()+h; ++y) {
			Location lc = new Location(loc.getWorld(), loc.getBlockX(), y, loc.getBlockZ());
			lc.getBlock().setType(log);
			if(!TYPE.equals("player")) {
				delete.add(new Loc(lc.getBlockX(), lc.getBlockY(), lc.getBlockZ()));
			}
		}
		Location lc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY()+h, loc.getBlockZ());
		for(int x = lc.getBlockX()-R; x <= lc.getBlockX()+R; ++x) {
			for(int z = lc.getBlockZ()-R; z <= lc.getBlockZ()+R; ++z) {
				for(int y = lc.getBlockY()-R; y <= lc.getBlockY()+R; ++y) {
					Location lc2 = new Location(loc.getWorld(), x, y, z);
					if(lc2.getBlock().getType() == Material.AIR && getDistance3d(lc, lc2) <= R) {
						lc2.getBlock().setType(leaf);
						if(!TYPE.equals("player")) {
							delete.add(new Loc(lc2.getBlockX(), lc2.getBlockY(), lc2.getBlockZ()));
						}
					}
				}
			}
		}
	}
	
	public void send(Player p, String msg) {
		p.sendMessage(msg.replaceAll("&", "§"));
	}
	
	boolean haveIsland(Player p) {
		try {
			if(config2.getInt("islands."+p.getName()+".size") > 0) return true;
		} catch(Exception ee) {};
		return false;
	}
	
	boolean haveIsland(String p_name) {
		try {
			if(config2.getInt("islands."+p_name+".size") > 0) return true;
		} catch(Exception ee) {};
		return false;
	}
	
	public void deleteIsland(Player p) {
		Location loc = getIslandCenter(p);
		int size = getIslandSize(p);
		for(int x = loc.getBlockX()-size; x <= loc.getBlockX()+size; ++x) {
			for(int z = loc.getBlockZ()-size; z <= loc.getBlockZ()+size; ++z) {
				for(int y = 0; y <= 255; ++y) {
					Location nl = new Location(loc.getWorld(), x, y, z);
					nl.getBlock().setType(Material.AIR);
				}
			}
		}
		
		List<Entity> entList = Bukkit.getWorld("world").getEntities();
		for(Entity current : entList) {
	        if (current instanceof Villager) {
	            Villager vil2 = (Villager) current;
	            for(String tag : vil2.getScoreboardTags()) {
	            	if(tag.equals(p.getName())) {
	            		vil2.remove();
	            	}
	            }
	        }
	    }
		
		config2.set("islands."+p.getName(), null);
		saveConfig2();
		reloadConfig2();
	}
	
	public void deleteIsland(String p_name) {
		Location loc = getIslandCenter(p_name);
		int size = getIslandSize(p_name);
		if(centers.contains(loc)) centers.remove(loc);
		for(int x = loc.getBlockX()-size; x <= loc.getBlockX()+size; ++x) {
			for(int z = loc.getBlockZ()-size; z <= loc.getBlockZ()+size; ++z) {
				for(int y = 0; y <= 255; ++y) {
					Location nl = new Location(loc.getWorld(), x, y, z);
					nl.getBlock().setType(Material.AIR);
				}
			}
		}
		
		List<Entity> entList = Bukkit.getWorld("world").getEntities();
		for(Entity current : entList) {
	        if (current instanceof Villager) {
	            Villager vil2 = (Villager) current;
	            for(String tag : vil2.getScoreboardTags()) {
	            	if(tag.equals(p_name)) {
	            		vil2.remove();
	            	}
	            }
	        }
	    }
		
		config2.set("islands."+p_name, null);
		saveConfig2();
		reloadConfig2();
	}
	
	int getIslandSize(Player p) {
		return config2.getInt("islands."+p.getName()+".size");
	}
	
	public void setIslandSize(Player p, int size) {
		Location c = getIslandCenter(p);
		config2.set("islands."+p.getName()+".size", size);
		
		for(int x = c.getBlockX()-size; x <= c.getBlockX()+size; ++x) {
			for(int z = c.getBlockZ()+(size-10); z <= c.getBlockZ()+size; ++z) {
				Location lc = new Location(c.getWorld(), x, 255, z);
				lc.getBlock().setType(Material.BARRIER);
			}
		}
		
		for(int x = c.getBlockX()-size; x <= c.getBlockX()+size; ++x) {
			for(int z = c.getBlockZ()-(size-10); z >= c.getBlockZ()-size; --z) {
				Location lc = new Location(c.getWorld(), x, 255, z);
				lc.getBlock().setType(Material.BARRIER);
			}
		}
		
		for(int z = c.getBlockZ()-(size-10); z <= c.getBlockZ()+(size-10); ++z) {
			for(int x = c.getBlockX()-size; x <= c.getBlockX()-(size-10); ++x) {
				Location lc = new Location(c.getWorld(), x, 255, z);
				lc.getBlock().setType(Material.BARRIER);
			}
		}
		
		for(int z = c.getBlockZ()-(size-10); z <= c.getBlockZ()+(size-10); ++z) {
			for(int x = c.getBlockX()+(size-10); x <= c.getBlockX()+size; ++x) {
				Location lc = new Location(c.getWorld(), x, 255, z);
				lc.getBlock().setType(Material.BARRIER);
			}
		}
		
		saveConfig2();
		reloadConfig2();
	}
	
	int getIslandSize(String p_name) {
		return config2.getInt("islands."+p_name+".size");
	}
	
	@EventHandler
	public void expChange(PlayerExpChangeEvent e) {
		Player p = e.getPlayer();
		addXp(p, (int) (e.getAmount()/2));
		scoreboardUpdate(p);
	}
	
	Location createBossIsland() {
		boolean can = false;
		int attempts = 0;
		while(!can) {
			can = true;
			
			if(attempts > 5) {
				return null;
			}
			
			int x = new Random().nextInt(border-500);
			int z = new Random().nextInt(border-500);
			attempts++;
			
			ConfigurationSection cs = config2.getConfigurationSection("islands");
			for(Location lc : disabled_locations) {
				if(Math.abs(lc.getBlockX()-x) <= 300 && Math.abs(lc.getBlockZ()-z) <= 300) {
					can = false;
					break;
				}
			}
			if(config2.contains("islands")) {
				for(String name : cs.getKeys(false)) {
					Location lc = config2.getLocation("islands."+name+".loc");
					
					if(Math.abs(lc.getBlockX()-x) <= 300 && Math.abs(lc.getBlockZ()-z) <= 300) {
						can = false;
						break;
					}
				}
			}
			if(can) {
				Location center = new Location(Bukkit.getWorld("world"), x, new Random().nextInt(50)+50, z);
				
				generateIslandFromConfig(center, 8+new Random().nextInt(2), 0.4+new Random().nextDouble()*0.1,
						3+new Random().nextInt(3), 3+new Random().nextInt(2), 2+ new Random().nextInt(2),
						new HashSet<Location>(), "boss");
				return getPortalByCenter(center);
				}
		}
		return null;
	}
	
	public void createIsland(Player p) {
		boolean can = false;
		int attempts = 0;
		while(!can) {
			can = true;
			
			if(attempts > 25) {
				send(p, lang.getString("error_on_island_creating"));
				toLobby(p);
				break;
			}
			
			int x = new Random().nextInt(border-500);
			int z = new Random().nextInt(border-500);
			attempts++;
			
			ConfigurationSection cs = config2.getConfigurationSection("islands");
			for(Location lc : disabled_locations) {
				if(Math.abs(lc.getBlockX()-x) <= 300 && Math.abs(lc.getBlockZ()-z) <= 300) {
					can = false;
					break;
				}
			}
			if(config2.contains("islands")) {
				for(String name : cs.getKeys(false)) {
					Location lc = config2.getLocation("islands."+name+".loc");
					
					if(Math.abs(lc.getBlockX()-x) <= 300 && Math.abs(lc.getBlockZ()-z) <= 300) {
						can = false;
						break;
					}
				}
			}
			if(can) {
				Location center = new Location(p.getWorld(), x, new Random().nextInt(50)+50, z);

				List<String> npcs = new ArrayList<String>();
				
				npcs.add(lang.getString("NPC_name"));
				
				config2.set("islands."+p.getName()+".loc", center);
				config2.set("islands."+p.getName()+".time_join", System.currentTimeMillis()/86400000);
				config2.set("islands."+p.getName()+".size", 50);
				config2.set("islands."+p.getName()+".npcs", npcs);
				config2.set("islands."+p.getName()+".level", 1);
				config2.set("islands."+p.getName()+".strength", 1.0);
				config2.set("islands."+p.getName()+".hp", 20.0);
				config2.set("islands."+p.getName()+".prot", 1.0);
				config2.set("islands."+p.getName()+".need_xp", 5);
				config2.set("islands."+p.getName()+".xp", 0);
				config2.set("islands."+p.getName()+".members", new ArrayList<String>());
				config2.set("islands."+p.getName()+".in_islands", new ArrayList<String>());
				
				saveConfig2();
				reloadConfig2();
				
				if(config.getBoolean("display_lvl")) {
					p.setDisplayName(lang.getString("display").replaceAll("%player%", p.getName()).replaceAll("%lvl%", String.valueOf(getLevel(p))));
				}
				

				if(TAB) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player "+p.getName()+" tabsuffix &7 [&c"+String.valueOf(getLevel(p))+"&7]");
				
				generateIslandFromConfig(center, 8+new Random().nextInt(2), 0.4+new Random().nextDouble()*0.1,
						3+new Random().nextInt(3), 3+new Random().nextInt(2), 2+ new Random().nextInt(2),
						new HashSet<Location>(), "player");
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						for(int xx = center.getBlockX(); xx <= center.getBlockX()+50; ++xx) {
							for(int zz = center.getBlockZ(); zz <= center.getBlockZ()+50; ++zz) {
								Location lc = new Location(center.getWorld(), xx, 255, zz);
								lc.getBlock().setType(Material.BARRIER);
							}
						}
					}
				}, 5);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						for(int xx = center.getBlockX()-50; xx <= center.getBlockX(); ++xx) {
							for(int zz = center.getBlockZ(); zz <= center.getBlockZ()+50; ++zz) {
								Location lc = new Location(center.getWorld(), xx, 255, zz);
								lc.getBlock().setType(Material.BARRIER);
							}
						}
					}
				}, 15);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						for(int xx = center.getBlockX()-50; xx <= center.getBlockX(); ++xx) {
							for(int zz = center.getBlockZ()-50; zz <= center.getBlockZ(); ++zz) {
								Location lc = new Location(center.getWorld(), xx, 255, zz);
								lc.getBlock().setType(Material.BARRIER);
							}
						}
					}
				}, 30);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						for(int xx = center.getBlockX(); xx <= center.getBlockX()+50; ++xx) {
							for(int zz = center.getBlockZ()-50; zz <= center.getBlockZ(); ++zz) {
								Location lc = new Location(center.getWorld(), xx, 255, zz);
								lc.getBlock().setType(Material.BARRIER);
							}
						}
					}
				}, 45);
				
				spawnNPC(p.getName(), lang.getString("NPC_name"));
				
				p.teleport(center.add(0.5, 1, 0.5));
				
				send(p, lang.getString("start_message"));
			}
		}
	}
	
	boolean onIsland(Location loc, Player p) {
		try {
			Location center = getIslandCenter(p.getName());
			int size = getIslandSize(p.getName());
			if(loc.getBlockX() <= center.getBlockX()+size && loc.getBlockX() >= center.getBlockX()-size &&
					loc.getBlockZ() <= center.getBlockZ()+size && loc.getBlockZ() >= center.getBlockZ()-size) {
				return true;
			}
		} catch (Exception ee) {};
		return false;
	}
	
	boolean onIsland(Location loc, String p_name) {
		try {
			Location center = getIslandCenter(p_name);
			int size = getIslandSize(p_name);
			if(loc.getBlockX() <= center.getBlockX()+size && loc.getBlockX() >= center.getBlockX()-size &&
					loc.getBlockZ() <= center.getBlockZ()+size && loc.getBlockZ() >= center.getBlockZ()-size) {
				return true;
			}
		} catch (Exception ee) {};
		return false;
	}
	
	boolean onOtherIsland(Location loc, Player p) {
		Location lc = new Location(p.getWorld(), loc.getBlockX(), 255, loc.getBlockZ());
		
		if(lc.getBlock().getType() == Material.BARRIER && !onIsland(loc, p.getName())) return true;
		return false;
	}
	
	boolean canChange(Player p, Location loc) {
		if(onOtherIsland(loc, p)) {
			List<String> in_islands = (List<String>) config2.get("islands."+p.getName()+".in_islands");
			for(String island : in_islands) {
				if(onIsland(loc, island)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public void changePvP(Player p) {
		Location center = getIslandCenter(p);
		
		Location lc = new Location(center.getWorld(), center.getBlockX(), 255, center.getBlockZ());
		
		while(lc.add(1, 0, 0).getBlock().getType() == Material.BARRIER) {};
		lc.add(-1, 0, 0);
		
		while(lc.add(0, 0, 1).getBlock().getType() == Material.BARRIER) {};
		lc.add(0, 0, -1);
		
		Location lc2 = new Location(lc.getWorld(), lc.getBlockX(), 254, lc.getBlockZ());
		if(lc2.getBlock().getType() == Material.BARRIER) lc2.getBlock().setType(Material.AIR);
		else lc2.getBlock().setType(Material.BARRIER);
	}
	
	public void changePortal(Player p) {
		Location center = getIslandCenter(p);
		
		Location lc = new Location(center.getWorld(), center.getBlockX(), 255, center.getBlockZ());
		
		while(lc.add(-1, 0, 0).getBlock().getType() == Material.BARRIER) {};
		lc.add(1, 0, 0);
		
		while(lc.add(0, 0, -1).getBlock().getType() == Material.BARRIER) {};
		lc.add(0, 0, 1);
		
		Location lc2 = new Location(lc.getWorld(), lc.getBlockX(), 254, lc.getBlockZ());
		if(lc2.getBlock().getType() == Material.BARRIER) lc2.getBlock().setType(Material.AIR);
		else lc2.getBlock().setType(Material.BARRIER);
	}
	
	boolean isPvP(Location loc) {
		Location lc = new Location(loc.getWorld(), loc.getBlockX(), 255, loc.getBlockZ());
		
		while(lc.add(20, 0, 0).getBlock().getType() == Material.BARRIER) {};
		lc.add(-20, 0, 0);
		while(lc.add(1, 0, 0).getBlock().getType() == Material.BARRIER) {};
		lc.add(-1, 0, 0);
		
		while(lc.add(0, 0, 20).getBlock().getType() == Material.BARRIER) {};
		lc.add(0, 0, -20);
		while(lc.add(0, 0, 1).getBlock().getType() == Material.BARRIER) {};
		lc.add(0, 0, -1);
		
		Location lc2 = new Location(lc.getWorld(), lc.getBlockX(), 254, lc.getBlockZ());
		
		if(lc2.getBlock().getType() == Material.BARRIER) return true;
		
		return false;
	}
	
	boolean isPortal(Location loc) {
		Location lc = new Location(loc.getWorld(), loc.getBlockX(), 255, loc.getBlockZ());
		
		while(lc.add(-20, 0, 0).getBlock().getType() == Material.BARRIER) {};
		lc.add(20, 0, 0);
		while(lc.add(-1, 0, 0).getBlock().getType() == Material.BARRIER) {};
		lc.add(1, 0, 0);
		
		while(lc.add(0, 0, -20).getBlock().getType() == Material.BARRIER) {};
		lc.add(0, 0, 20);
		while(lc.add(0, 0, -1).getBlock().getType() == Material.BARRIER) {};
		lc.add(0, 0, 1);
		
		Location lc2 = new Location(lc.getWorld(), lc.getBlockX(), 254, lc.getBlockZ());
		
		if(lc2.getBlock().getType() != Material.BARRIER) return true;
		
		return false;
	}
	
	boolean isIsland(Location loc) {
		Location lc = new Location(loc.getWorld(), loc.getX(), 255, loc.getZ());
		if(lc.getBlock().getType() != Material.BARRIER) return false;
		return true;
	}
	
	@EventHandler
	public void blockgen(BlockFormEvent e) {
		Location loc = e.getBlock().getLocation();
		if(e.getBlock().getType() == Material.REDSTONE_BLOCK || e.getBlock().getType() == Material.OBSERVER || e.getBlock().getType() == Material.REDSTONE_TORCH || e.getBlock().getType() == Material.REDSTONE_WALL_TORCH || e.getBlock().getType() == Material.REPEATER || e.getBlock().getType() == Material.COMPARATOR) {
			if(!canMech) e.setCancelled(true);
		}
		else {
			if(!isIsland(loc)) {
				if(delete.size() < 1500000) {
					delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
				}
				else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void place(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Location loc = e.getBlockPlaced().getLocation();
		if(e.getItemInHand().equals(getMed())) e.setCancelled(true);
		if(e.getBlockPlaced().getType() == Material.REDSTONE_BLOCK || e.getBlockPlaced().getType() == Material.OBSERVER || e.getBlockPlaced().getType() == Material.REDSTONE_TORCH || e.getBlockPlaced().getType() == Material.REDSTONE_WALL_TORCH || e.getBlockPlaced().getType() == Material.REPEATER || e.getBlockPlaced().getType() == Material.COMPARATOR) {
			if(!canMech) {
				p.sendMessage(lang.getString("you_cant_place_this_item"));
				e.setCancelled(true);
			}
		}
		if(!canChange(p, loc)) {
			send(p, lang.getString("you_cant_edit_this_island"));
			e.setCancelled(true);
			
		}
		else {
			if(!isIsland(loc)) {
				if(!canPlace) {
					send(p, lang.getString("you_cant_place_blocks_out_of_island"));
					e.setCancelled(true);
				}
				else {
					if(infromed.isEmpty() || !infromed.contains(p)) {
						infromed.add(p);
						send(p, lang.getString("warning_edit"));
					}
					boolean cancel = false;
					if(block_limit != null && !block_limit.isEmpty()) {
						if(block_limit.containsKey(p) && block_limit.get(p) >= config.getInt("block_limit")) {
							send(p, lang.getString("block_limit"));
							e.setCancelled(true);
							cancel = true;
						}
						else if(block_limit.containsKey(p) && block_limit.get(p) >= config.getInt("block_limit")-5) {
							send(p, lang.getString("block_limit_close") + "&f(&3"+String.valueOf(block_limit.get(p)) + "&f/&3"+String.valueOf(config.getInt("block_limit")) + "&f)");
						}
					}
					if(!cancel) {
						delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
						if(block_limit.containsKey(p)) {
							block_limit.replace(p, block_limit.get(p)+1);
						}
						else {
							block_limit.put(p, 1);
						}
					}
				}
			}
			else {
				if(infromed.contains(p)) infromed.remove(p);
			}
		}
	}
	
	@EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
		List<Block> destroyed = e.blockList();
        Iterator<Block> it = destroyed.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            if(centers.contains(block.getLocation())) it.remove();
        }
    }
	
	@EventHandler
	public void breakk(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Location loc = e.getBlock().getLocation();
		Location lcc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		if(!canChange(p, loc)) {
			send(p, lang.getString("you_cant_edit_this_island"));
			e.setCancelled(true);
			
		}
		else if(centers.contains(lcc)) {
			send(p, lang.getString("you_cant_break_start_point"));
			e.setCancelled(true);
		}
		else {
			if(!isIsland(loc)) {
				if(infromed.isEmpty() || !infromed.contains(p)) {
					infromed.add(p);
					send(p, lang.getString("warning_edit"));
				}
			}
			else {
				if(e.getBlock().getType() == Material.COBBLESTONE) {
					int ore = config2.getInt("islands."+p.getName()+".ore");
			        
			        double o1 = Math.round(100.0*(ore-(double)ore/2))/100.0;
			        double o2 = Math.round(100.0*((double)ore/2-(double)ore/4))/100.0;
			        double o3 = Math.round(100.0*((double)ore/3-(double)ore/5))/100.0;
			        double o4 = Math.round(100.0*((double)ore/3-(double)ore/5))/100.0;
			        double o5 = Math.round(100.0*((double)ore/4-(double)ore/6))/100.0;
			        double o6 = Math.round(100.0*((double)ore/5-(double)ore/7))/100.0;
			        double o7 = Math.round(100.0*((double)ore/7-(double)ore/8))/100.0;
			        double o8 = Math.round(100.0*((double)ore/12-(double)ore/14))/100.0;
			        
			        if(new Random().nextDouble()*100 <= o1) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.COAL_ORE));
			        else if(new Random().nextDouble()*100 <= o2) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.IRON_ORE));
			        else if(new Random().nextDouble()*100 <= o3) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLD_ORE));
			        else if(new Random().nextDouble()*100 <= o4) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE_ORE));
			        else if(new Random().nextDouble()*100 <= o5) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.LAPIS_ORE));
			        else if(new Random().nextDouble()*100 <= o6) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.EMERALD_ORE));
			        else if(new Random().nextDouble()*100 <= o7) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND_ORE));
			        else if(new Random().nextDouble()*100 <= o8) p.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.ANCIENT_DEBRIS));;
				}
			}

			if(e.getBlock().getType() == Material.CHEST) {
				if(chests.contains(loc)) chests.remove(loc);
			}
		}
	}

	
	@EventHandler
	public void enspawn(CreatureSpawnEvent e) {
		LivingEntity en = e.getEntity();
		if(e.getSpawnReason() == SpawnReason.BREEDING) {
			if(en instanceof Villager) {
				Location loc = en.getLocation();
				e.setCancelled(true);
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(getDistance(p.getLocation(), loc) <= 50) {
						send(p, lang.getString("you_cant_multiple_villagers"));
					}
				}
			}
		}
		if(e.getSpawnReason() != SpawnReason.CUSTOM) {
			Location loc = en.getLocation();
			Location lc = new Location(loc.getWorld(), loc.getBlockX(), 255, loc.getBlockZ());
			if(lc.getBlock().getType() != Material.BARRIER) {
				//if (en.getCustomName() != null) LOG(en.getCustomName());
				e.setCancelled(true);
			}
			else {
				en.setCustomNameVisible(true);
				if(en instanceof Monster) {
					Player close = null;
					double min_dis = 1000;
					for(Player pl : Bukkit.getOnlinePlayers()) {
						if(getDistance(pl.getLocation(), en.getLocation()) < min_dis) {
							min_dis = getDistance(pl.getLocation(), en.getLocation());
							close = pl;
						}
					}
					int hp = 10+new Random().nextInt(30);
					if(min_dis < 1000) {
						int lvl = getLevel(close);
						hp = (int) (10 + lvl + new Random().nextInt((int)(2*lvl+1)));
					}
					en.setCustomName((String.valueOf((int)hp)) + " §a§l❤");
					en.setMaxHealth(hp);
					en.setHealth(hp);
				}
				else {
					int hp = (int) en.getHealth();
					en.setCustomName((String.valueOf((int)hp)) + " §a§l❤");
					en.setMaxHealth(hp);
					en.setHealth(hp);
				}
			}
		}
	}
	
	@EventHandler
	public void inter(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(p.getItemInHand().getType() == Material.SNOWBALL) {
				ItemStack it = p.getItemInHand();
				ItemMeta meta = it.getItemMeta();
				if(meta.getDisplayName().contains(lang.getString("auto_builder"))) {
					if(snowball_limits.containsKey(p.getName()) && snowball_limits.get(p.getName()) >= config.getInt("autobuild_limit")) {
						send(p, lang.getString("auto_builder_limit"));
						e.setCancelled(true);
					}
					else {
						BlockIterator iterator = new BlockIterator(p.getWorld(), p
			                    .getLocation().toVector(), p.getLocation().getDirection().normalize(), 0.0D,
			                    30);
			                           Block hitBlock = null;
			              boolean ch = true;
			              while (iterator.hasNext()) {
			                  hitBlock = iterator.next();
			                  if(!ch) {
				                  if (hitBlock.getType() == Material.AIR) {
				                      if(canChange(p, hitBlock.getLocation())) {
				                    	  hitBlock.setType(Material.COBBLESTONE);
				                    	  Location lc = new Location(hitBlock.getWorld(), hitBlock.getLocation().getBlockX(), 255, hitBlock.getLocation().getBlockZ());
				                    	  if(lc.getBlock().getType() != Material.BARRIER) {
				                    		  delete.add(new Loc(lc.getBlockX(), hitBlock.getLocation().getBlockY(), lc.getBlockZ()));
				                    	  }
				                      }
				                  }
			                  }
			                  ch = false;
			              }
			              if(snowball_limits.containsKey(p.getName())) {
			            	  snowball_limits.replace(p.getName(), snowball_limits.get(p.getName())+1);
			              }
			              else snowball_limits.put(p.getName(), 1);
					}
				}
			}
			if(p.getItemInHand().getType() == Material.MOJANG_BANNER_PATTERN) {
				ItemStack it = p.getItemInHand();
				ItemMeta meta = it.getItemMeta();
				String name = meta.getDisplayName();
				String[] split = name.split(" ");
				if(name.contains(lang.getString("scrolls.2"))) {
					try {
						double hp = Double.valueOf(split[0].substring(3));
						p.getItemInHand().setAmount(0);
						double nhp = p.getMaxHealth()+hp;
						p.setMaxHealth(nhp);
						config2.set("islands."+p.getName()+".hp", nhp);
						send(p, lang.getString("scroll_used"));
					} catch(Exception ee) {};
				}
				else if(name.contains(lang.getString("scrolls.3"))) {
					try {
						double s = Double.valueOf(split[0].substring(3));
						p.getItemInHand().setAmount(0);
						addStrength(p, s);
						scoreboardUpdate(p);
						send(p, lang.getString("scroll_used"));
					} catch(Exception ee) {};
				}
				else if(name.contains(lang.getString("scrolls.4"))) {
					try {
						double s = Double.valueOf(split[0].substring(3));
						p.getItemInHand().setAmount(0);
						addProt(p, s);
						scoreboardUpdate(p);
						send(p, lang.getString("scroll_used"));
					} catch(Exception ee) {};
				}
				else if(split.length == 4) {
					if(name.equals(lang.getString("scrolls.1"))) {
						toIsland(p);
						p.getItemInHand().setAmount(0);
						send(p, lang.getString("scroll_used"));
					}
				}
			}
		}
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.LEVER) {
				if(lever.contains(p.getName())) {
					p.sendMessage(lang.getString("lever_cooldown"));
					e.setCancelled(true);
				}
				else {
					lever.add(p.getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
						public void run() {
							if(lever.contains(p.getName())) lever.remove(p.getName());
						}
					}, 20*2);
				}
			}
			if(!canChange(p, e.getClickedBlock().getLocation())) {
				e.setCancelled(true);
				if(p.getItemInHand().getType().isEdible() && e.getHand() == EquipmentSlot.HAND) {
					send(p, lang.getString("eating_other_island"));
				}
			}
			else if(canChange(p, e.getClickedBlock().getLocation()) && onOtherIsland(e.getClickedBlock().getLocation(), p) && p.getItemInHand().getType() == Material.LAVA_BUCKET) {
				send(p, lang.getString("you_cant_place_lava"));
				e.setCancelled(true);
			}
			else {
				if(e.getHand() == EquipmentSlot.HAND) return;
				if(e.getClickedBlock().getType() == Material.NETHER_PORTAL) {
					Location loc = getPortal(e.getClickedBlock().getLocation());
					if(p.getItemInHand().getType() == Material.COMPASS) {
						ItemStack it = p.getItemInHand();
						ItemMeta meta = it.getItemMeta();
						String name = meta.getDisplayName();
						try {
							int x = Integer.valueOf(name.split(" ")[0]);
							int y = Integer.valueOf(name.split(" ")[1]);
							int z = Integer.valueOf(name.split(" ")[2]);
							
							String key = String.valueOf(loc.getBlockX())+","+String.valueOf(loc.getBlockY())+","+String.valueOf(loc.getBlockZ());
							config2.set("portals."+key, new Location(loc.getWorld(), x, y, z));
							saveConfig2();
							reloadConfig2();
							send(p, lang.getString("portals.1"));
							send(p, lang.getString("portals.2"));
						} catch(Exception ee) {
							send(p, lang.getString("portals.3"));
							send(p, lang.getString("portals.4"));
						}
					}
					else {
						send(p, lang.getString("portals.5"));
						send(p, lang.getString("portals.4"));
						send(p, lang.getString("portals.6")+String.valueOf(loc.getBlockX())+" "+String.valueOf(loc.getBlockY())+" "+String.valueOf(loc.getBlockZ()));
					}
				}
			}
		}
		else if(e.getAction() == Action.PHYSICAL) {
			if(!canChange(p, e.getClickedBlock().getLocation())) {
				e.setCancelled(true);
			}
		}
		else if(p.getItemInHand().getType() == Material.WATER_BUCKET || p.getItemInHand().getType() == Material.LAVA_BUCKET) {
			try {
				if(!canChange(p, e.getClickedBlock().getLocation())) {
					e.setCancelled(true);
				}
				else if(onOtherIsland(e.getClickedBlock().getLocation(), p) && p.getItemInHand().getType() == Material.LAVA_BUCKET) {
					send(p, lang.getString("you_cant_place_lava"));
					e.setCancelled(true);
				}
			} catch(Exception ee) {};
		}
	}
	
	@EventHandler
	public void bucket(PlayerBucketEmptyEvent  e) {
		Player p = e.getPlayer();
		Location loc = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		Location lc = new Location(p.getWorld(), loc.getBlockX(), 255, loc.getZ());
		if(lc.getBlock().getType() != Material.BARRIER) {
			if(infromed.isEmpty() || !infromed.contains(p)) {
				infromed.add(p);
				send(p, lang.getString("warning_edit"));
			}
		}
		else if(!onIsland(loc, p)) {
			if(block_limit != null && !block_limit.isEmpty()) {
				if(block_limit.containsKey(p) && block_limit.get(p) >= config.getInt("block_limit")) {
					send(p, lang.getString("block_limit"));
					e.setCancelled(true);
				}
				else if(block_limit.containsKey(p) && block_limit.get(p) >= config.getInt("block_limit")-5) {
					send(p, lang.getString("block_limit_close") + " &f(&3"+String.valueOf(block_limit.get(p)) + "&f/&3"+String.valueOf(config.getInt("block_limit")) + "&f)");
				}
			}
			delete.add(new Loc(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
			if(block_limit.containsKey(p)) {
				block_limit.replace(p, block_limit.get(p)+1);
			}
			else {
				block_limit.put(p, 1);
			}
		}
		if(p.getItemInHand().getType() == Material.LAVA_BUCKET || p.getItemInHand().getType() == Material.WATER_BUCKET) {
			if(!canChange(p, loc)) {
				e.setCancelled(true);
			}
			else if(onOtherIsland(loc, p) && p.getItemInHand().getType() == Material.LAVA_BUCKET) {
				send(p, lang.getString("you_cant_place_lava"));
				e.setCancelled(true);
			}
		}
	}
	
	Location getIslandCenter(Player p) {
		return config2.getLocation("islands."+p.getName()+".loc");
	}
	
	Location getIslandCenter(String p_name) {
		return config2.getLocation("islands."+p_name+".loc");
	}
	
	public void toIsland(Player p) {
		Location loc = getIslandCenter(p);
		Location lc = new Location(loc.getWorld(), loc.getX()+0.5, loc.getY()+1, loc.getZ()+0.5);
		p.teleport(lc);
		if(lc.getBlock().getType() != Material.AIR) {
			lc.getBlock().setType(Material.AIR);
		}
		send(p, lang.getString("island_tp_success"));
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				boolean ch = false;
				List<Entity> enList = p.getWorld().getEntities();
				for(Entity en : enList) {
					if(en instanceof Villager) {
						if(ch) break;
						if(!en.getScoreboardTags().isEmpty()) {
							for(String tag : en.getScoreboardTags()) {
								if(tag.equals(p.getName())) {
									ch = true;
									break;
								}
							}
						}
					}
				}
				if(!ch) {
					spawnNPC(p.getName(), lang.getString("NPC_name"));
				}
			}
		}, 10);
	}
	
	public void spawnNPC(String p_name, String name) {
		World w = Bukkit.getWorld("world");
		List<Entity> enList = w.getEntities();
		
		for(Entity en : enList) {
			if(en instanceof Villager) {
				if(!en.getScoreboardTags().isEmpty()) {
					for(String tag : en.getScoreboardTags()) {
						if(tag.equals(p_name)) return;
					}
				}
			}
		}
		
		Location loc = config2.getLocation("islands."+p_name+".loc");
		int x = loc.getBlockX();
		int z = loc.getBlockZ();
		
		boolean success = false;
		
		int attemps = 0;
		int radius = 5;
		
		while(attemps <= 15 && !success) {
			int xx = (x-radius) + new Random().nextInt(2*radius);
			int zz = (z-radius) + new Random().nextInt(2*radius);
			

			Location lc = new Location(loc.getWorld(), xx, loc.getBlockY()+4, zz);
			if(lc.getBlock().getType() == Material.AIR) {
				for(int y = loc.getBlockY()+3; y >= loc.getBlockY()-5; --y) {
					lc = new Location(loc.getWorld(), xx, y, zz);
					if(lc.getBlock().getType() != Material.AIR && lc.getBlock().getType() != Material.LAVA && lc.getBlock().getType() != Material.WATER) {
						Villager vil = (Villager) loc.getWorld().spawnEntity(lc.add(0, 1, 0), EntityType.VILLAGER);
						vil.addScoreboardTag(p_name);
						vil.setCustomName("§a§l"+name);
						vil.setCustomNameVisible(true);
						success = true;
						break;
					}
				}
			}
			attemps++;
		}
		if(!success) {
			Villager vil = (Villager) loc.getWorld().spawnEntity(loc.add(0, 1, 0), EntityType.VILLAGER);
			vil.addScoreboardTag(p_name);
			vil.setCustomName("§a§l"+name);
			vil.setCustomNameVisible(true);
		}
	}
	
	int getLoosePercent(Player p) {
		if(p.hasPermission("API.helper")) return getConfig().getInt("loose_percents.helper");
		if(p.hasPermission("API.youtube")) return getConfig().getInt("loose_percents.youtube");
		if(p.hasPermission("API.premium")) return getConfig().getInt("loose_percents.premium");
		if(p.hasPermission("API.mvp+")) return getConfig().getInt("loose_percents.mvp+");
		if(p.hasPermission("API.mvp")) return getConfig().getInt("loose_percents.mvp");
		if(p.hasPermission("API.vip+")) return getConfig().getInt("loose_percents.vip+");
		if(p.hasPermission("API.vip")) return getConfig().getInt("loose_percents.vip");
		return getConfig().getInt("loose_percents.player");
	}
	
	public class Pair2 {
		private List<ItemStack> it1, it2;
		public Pair2(List<ItemStack> i1, List<ItemStack> i2) {
			this.it1 = i1;
			this.it2 = i2;
		}
	}
	
	public class Pair {
		private Material mat;
		private double probability;
		public Pair(Material i1, double i2) {
			this.mat = i1;
			this.probability = i2;
		}
	}
	
	public class CustomEntity {
		private EntityType type;
		private double hp;
		private double damage;
		private double prob;
		private ArrayList<String> abilities;
		public CustomEntity(EntityType type, double hp, double damage, double prob, ArrayList<String> abilities) {
			this.type = type;
			this.hp = hp;
			this.damage = damage;
			this.prob = prob;
			this.abilities = abilities;
		}
	}
	
	Pair2 returnItems(List<ItemStack> list, int percent) {
		List<ItemStack> res1 = new ArrayList<ItemStack>();
		List<ItemStack> res2 = new ArrayList<ItemStack>();
		double sp = (double)(100-percent)/100;
		for(int i = 0; i < list.size(); ++i) {
			double r = new Random().nextDouble();
			if(r <= sp) {
				res1.add(list.get(i));
			}
			else {
				res2.add(list.get(i));
			}
		}
		Pair2 pair = new Pair2(res1, res2);
		return pair;
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(items.containsKey(p.getName())) {
			p.sendMessage(lang.getString("loose_items").replaceAll("%percent%", String.valueOf(getLoosePercent(p))));
			List<ItemStack> list = items.get(p.getName());
			items.remove(p.getName());
			for(int i = 0; i < list.size(); ++i) {
				p.getInventory().addItem(list.get(i));
			}
		}
	}
	
	double getDistance(Location loc1, Location loc2) {
		return (double) (Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(), 2) + Math.pow(loc1.getBlockZ()-loc2.getBlockZ(), 2)));
	}
	
	double getDistance3d(Location loc1, Location loc2) {
		return (double) (Math.sqrt(Math.pow(loc1.getBlockX()-loc2.getBlockX(), 2) + Math.pow(loc1.getBlockZ()-loc2.getBlockZ(), 2) + Math.pow(loc1.getBlockY()-loc2.getBlockY(), 2)));
	}
	
	public void respawn(Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> p.spigot().respawn(), 1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				toIsland(p);
				if(food_level.containsKey(p)) {
					p.setFoodLevel(food_level.get(p));
				}
			}
		}, 3);
	}
	
	public void choose(Player p) {
		if(p.getBedSpawnLocation() == null) {
			respawn(p);
		}
		else {
			p.setGameMode(GameMode.SPECTATOR);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					p.spigot().respawn();
					p.teleport(new Location(p.getWorld(), -100000, 250, -100000));
				}
			}, 1);
			send(p, lang.getString("spawn_detected"));
			json.json2(p, lang);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					if(p != null && p.isOnline()) {
						if(p.getGameMode() == GameMode.SPECTATOR) {
							respawn(p);
							p.setGameMode(GameMode.SURVIVAL);
						}
					}
				}
			}, 20*10);
		}
	}
	
	@EventHandler
	public void pdeath(PlayerDeathEvent e) {
		Player p = (Player) e.getEntity();
		e.setDeathMessage(null);
		if(food_level.containsKey(p)) {
			food_level.replace(p, p.getFoodLevel());
		}
		else {
			food_level.put(p, p.getFoodLevel());
		}
		int percent = getLoosePercent(p);
		if(percent != 100) {
			Pair2 s = returnItems(e.getDrops(), percent);
			items.put(p.getName(), s.it1);
			e.getDrops().clear();
			e.getDrops().addAll(s.it2);
		}
		choose(p);
		send(p, lang.getString("player_dead"));
		minus10(p);
	}
	
	@EventHandler
	public void death(EntityDeathEvent e) {
		if(!e.getEntity().getScoreboardTags().isEmpty()) {
			Entity en = e.getEntity();
			for(String tag : en.getScoreboardTags()) {
				if(tag.contains("drop: ")) {
					try {
						Random r = new Random();
						int drop = Integer.valueOf(tag.split(" ")[1]);
						List<ItemStack> list2 = (List<ItemStack>) getConfig().get("chests."+String.valueOf(drop));
			            List<ItemStack> list = new ArrayList<ItemStack>(list2);
			            for(int i = 0; i < list.size(); ++i) {
			            	ItemStack item = list.get(i);
			            	int amount = item.getAmount();
			            	int chance = getChance(item);
			            	int rand = (int)(r.nextDouble() * (100));
			            	if(rand <= chance) {
			            		if(amount > 1) {
				            		item.setAmount(r.nextInt(2*amount)+1);
				            		double rand2 = r.nextDouble();
					            	if(rand2 <= 0.3) {
					            		ItemStack item2 = item.clone();
					            		item2.setAmount((int)(amount/2));
					            		list.add(item2);
					            	}
				            	}
			            		ItemStack item2 = item.clone();
			            		ItemMeta meta = item2.getItemMeta();
			                	meta.setLore(null);
			                	item2.setItemMeta(meta);
			                	
			                	en.getWorld().dropItemNaturally(en.getLocation(), item2);
			            	}
			            }
					} catch (Exception ee) {};
				}
				else if(tag.contains("location: ")) {
					int x = Integer.valueOf(tag.split(" ")[1]);
					int y = Integer.valueOf(tag.split(" ")[2]);
					int z = Integer.valueOf(tag.split(" ")[3]);
					Location l = new Location(en.getWorld(), x, y, z);
					if(is_alive.containsKey(l)) {
						is_alive.replace(l, false);
						updateBossesInv();
					}
				}
			}
		}
		if(e.getEntity() instanceof Villager) {
			Villager vil = (Villager) e.getEntity();
			if(!vil.getScoreboardTags().isEmpty()) {
				for(String p_name : vil.getScoreboardTags()) {
					if(haveIsland(p_name)) {
						if(Bukkit.getPlayer(p_name) != null && Bukkit.getPlayer(p_name).isOnline()) {
							List<Entity> entList = Bukkit.getWorld("world").getEntities();
						    boolean can = true;
							for(Entity current : entList) {
						        if (current instanceof Villager) {
						            Villager vil2 = (Villager) current;
						            for(String tag : vil2.getScoreboardTags()) {
						            	if(tag.equals(p_name)) {
						            		can = false;
						            		break;
						            	}
						            }
						        }
						    }
							if(can) {
								Player p = Bukkit.getPlayer(p_name);
								send(p, lang.getString("NPC_dead").replaceAll("%npc%", vil.getCustomName()));
								
								Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									public void run() {
										if(haveIsland(p_name)) {
											spawnNPC(p_name, lang.getString("NPC_name"));
											if(p != null && p.isOnline()) {
												send(p, lang.getString("NPC_respawn").replaceAll("%npc%", vil.getCustomName()));
											}
										}
									}
								}, 20*60);
							}
						}
					}
				}
			}
		}
		else if(e.getEntity() instanceof Monster) {
			LivingEntity en = e.getEntity();
			int hp = (int) en.getMaxHealth();
			if(en.getKiller() != null && en.getKiller() instanceof Player) {
				Random r = new Random();
				int ran = r.nextInt(1+(int)(hp/10));
				Location loc = e.getEntity().getLocation();
				Location nl = new Location(loc.getWorld(), loc.getX(), loc.getY()+0.5, loc.getZ());
				if(ran > 64) ran = 64;
				if(ran > 0) spawnMoney(ran, nl);
			}
			e.setDroppedExp((e.getDroppedExp()/3)*(int)(1 + (double)hp/100.0));
		}
	}
	
	ItemStack getMed(int count) {
		ItemStack med = new ItemStack(Material.OAK_BUTTON, count);
	    ItemMeta med_meta = med.getItemMeta();
	    med_meta.setDisplayName(lang.getString("money_item"));
		med.setItemMeta(med_meta);
		return med;
	}
	
	ItemStack getMed() {
		ItemStack med = new ItemStack(Material.OAK_BUTTON);
	    ItemMeta med_meta = med.getItemMeta();
	    med_meta.setDisplayName(lang.getString("money_item"));
		med.setItemMeta(med_meta);
		return med;
	}
	
	public void spawnMoney(int count, Location loc) {
		loc.getWorld().dropItemNaturally(loc, getMed(count));
	}
	
	public void toLobby(Player p ) {
		if(bungee) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
	        out.writeUTF("Connect");
	        out.writeUTF(hub_server);
	        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
		}
		else {
			p.kickPlayer(lang.getString("kick_player"));
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		boolean can = true;
		if(islands_all.containsKey(p.getName().toLowerCase())) {
			if(!islands_all.get(p.getName().toLowerCase()).equals(p.getName())) {
				send(p, lang.getString("please_rename_yourself")+" "+islands_all.get(p.getName().toLowerCase()));
				can = false;
				toLobby(p);
			}
		}
		if(can) {
			if(!haveIsland(p)) {
				LOAD = false;
				createIsland(p);
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					public void run() {
						LOAD = true;
					}
				}, 20*1);
				if(!centers.contains(getIslandCenter(p))) centers.add(getIslandCenter(p));
				send(p, lang.getString("pls_get_start_kit"));
			}
			else {
				if(config.getBoolean("display_lvl")) {
					p.setDisplayName(lang.getString("display").replaceAll("%player%", p.getName()).replaceAll("%lvl%", String.valueOf(getLevel(p))));
				}
				if(!isJoined.contains(p.getName())) {
					choose(p);
					List<Entity> entList = Bukkit.getWorld("world").getEntities();
					can = true;
					for(Entity current : entList) {
				        if (current instanceof Villager) {
				            Villager vil2 = (Villager) current;
				            for(String tag : vil2.getScoreboardTags()) {
				            	if(tag.equals(p.getName())) {
				            		can = false;
				            		break;
				            	}
				            }
				        }
				    }
					if(can) {
						spawnNPC(p.getName(), lang.getString("NPC_name"));
					}
				}
			}
		}
		
		if(config2.contains("islands."+p.getName()+".hp")) {
			p.setMaxHealth(config2.getDouble("islands."+p.getName()+".hp"));
		}
		else {
			config2.set("islands."+p.getName()+".hp", 20.0);
		}

		if(!isJoined.contains(p.getName())) {
			isJoined.add(p.getName());
		}
		if(TAB) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tab player "+p.getName()+" tabsuffix &7 [&c"+String.valueOf(getLevel(p))+"&7]");
		
		int days = (int) (System.currentTimeMillis()/86400000);
		config2.set("islands."+p.getName()+".time_join", days);
		scoreboardUpdate(p);
		update(p);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if(p.getGameMode() == GameMode.SPECTATOR) {
			p.setGameMode(GameMode.SURVIVAL);
			toIsland(p);
		}
	}
	
	int getChance(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		String value = meta.getLore().get(0);
		return Integer.valueOf(value.substring(0, value.length()-1));
	}
	

	@EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e) {
        if (e.getInventory().getHolder() instanceof Chest) {
        	Random r = new Random();
            Location loc = e.getInventory().getLocation();
            Location tc = new Location(loc.getWorld(), loc.getBlockX(), 255, loc.getBlockZ());
            if(chests.contains(loc)) {
            	if(island_diff.isEmpty() && tc.getBlock().getType() != Material.BARRIER) return;
            	String diff = "0";
            	if(tc.getBlock().getType() == Material.BARRIER) diff = "player";
            	else {
	            	int min_dis = 100;
	            	for(Location lc : island_diff.keySet()) {
	            		int dis = (int) getDistance(lc, loc);
	            		if(dis <= min_dis) {
	            			min_dis = dis;
	            			diff = String.valueOf(island_diff.get(lc));
	            		}
	            	}
            	}
            	if(diff != "0") {
		            e.getInventory().clear();
		            List<ItemStack> list2 = (List<ItemStack>) config.get("chests."+diff);
		            List<ItemStack> list = new ArrayList<ItemStack>(list2);
		            for(int i = 0; i < list.size(); ++i) {
		            	ItemStack item = list.get(i).clone();
		            	int amount = item.getAmount();
		            	int chance = getChance(item);
		            	int rand = (int)(r.nextDouble() * (100));
		            	if(rand <= chance) {
		            		if(amount > 1) {
			            		item.setAmount(r.nextInt(2*amount)+1);
			            		double rand2 = r.nextDouble();
				            	if(rand2 <= 0.3) {
				            		ItemStack item2 = item.clone();
				            		item2.setAmount((int)(amount/2));
				            		list.add(item2);
				            	}
			            	}
		            		ItemStack item2 = item.clone();
		                	int index = (int)(Math.random() * (26));
		                	int attempts = 0;
		                	while(attempts <= 5) {
			                	if(e.getInventory().getItem(index) == null || e.getInventory().getItem(index).getType() == Material.AIR) {
				                	ItemMeta meta = item2.getItemMeta();
				                	meta.setLore(null);
				                	item2.setItemMeta(meta);
				            		e.getInventory().setItem(index, item2);
				            		break;
			                	}
			                	attempts++;
		                	}
		            	}
		            }
            	}
	            chests.remove(loc);
            }
        }
    }
	
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		if (!setupEconomy()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
	    }
		Bukkit.getLogger().log(Level.INFO, Bukkit.getVersion().toString());
		setupJSON();
		if(json == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Only 1.16+ !!!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		file = new File(getDataFolder(), "config.yml");
		if(!file.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
		reloadConfig();
		
		file2 = new File(getDataFolder(), "islands.yml");
		if(!file2.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig2();
		}
		reloadConfig2();
		
		file3 = new File(getDataFolder(), "lang.yml");
		if(!file3.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig3();
		}
		reloadConfig3();
		

		World w = Bukkit.getWorld("world");
		List<Entity> enList = w.getEntities();
		for(Entity en : enList) {
			Location loc = en.getLocation();
			Location lc = new Location(loc.getWorld(), loc.getBlockX(), 255, loc.getBlockY());
			if(lc.getBlock().getType() != Material.BARRIER) {
				en.remove();
			}
		}
		
		border = config.getInt("border");
		
		ConfigurationSection cs = config2.getConfigurationSection("islands");
		int days2 = (int) (System.currentTimeMillis()/86400000);
		HashSet<String> del = new HashSet<String>();
		ArrayList<Island> top_all = new ArrayList<Island>();
		
		for(String name : cs.getKeys(false)) {
			Location cen = getIslandCenter(name);
			centers.add(cen);
			int days1 = config2.getInt("islands."+name+".time_join");
			if(days2-days1 >= config.getInt("delete_after_days")) {
				deleteIsland(name);
				del.add(name);
			}
			else {
				Bukkit.getLogger().info(name);
				islands_all.put(name.toLowerCase(), name);
				config2.set("islands."+name+".strength", round(config2.getDouble("islands."+name+".strength")));
				config2.set("islands."+name+".prot", round(config2.getDouble("islands."+name+".prot")));
				saveConfig2();
				reloadConfig2();
				Island is = new Island(name, config2.getInt("islands."+name+".level"), config2.getDouble("islands."+name+".strength"), config2.getDouble("islands."+name+".prot"));
				top_all.add(is);
			}
		}
		
		LOAD = true;
		
		Collections.sort(top_all, new Comparator<Island>() {
		    @Override
		    public int compare(Island is1, Island is2) {
		        return is1.lvl > is2.lvl ? -1 : (is1.lvl < is2.lvl) ? 1 : 0;
		    }
		});
		
		int i = 0;
		for(Island is : top_all) {
			if(i < 45) {
				top.add(is);
				++i;
			}
			else break;
		}
		
		top45 = getTop();
		shop = Shop();

		WorldBorder wb = Bukkit.getWorld("world").getWorldBorder();
		wb.setCenter(0, 0);
		wb.setSize(border*2);
		canPlace = config.getBoolean("can_place_blocks_out_of_islands");
		bungee = config.getBoolean("BungeeCord");
		TAB = config.getBoolean("TAB");
		hub_server = config.getString("hub_server");
		canMech = !config.getBoolean("disable_mechanisms");
		giveMoney = config.getBoolean("give_money_on_new_lvl");
		mult1 = config.getInt("multiple_money_give");
		
		cs = config2.getConfigurationSection("portals");
		
		for(String key : cs.getKeys(false)) {
			Location loc = new Location(Bukkit.getWorld("world"), Integer.valueOf(key.split(",")[0]), Integer.valueOf(key.split(",")[1]), Integer.valueOf(key.split(",")[2]));
			if(loc.getBlock().getType() != Material.NETHER_PORTAL || !loc.equals(getPortal(loc))) {
				config2.set("portals."+key, null);
			}
		}
		saveConfig2();
		reloadConfig2();
		
		List<String> q = config.getStringList("quests");
		for(String quest : q) {
			String[] spl = quest.split(", ");
			quests.put(new ItemStack(Material.getMaterial(spl[0]), Integer.valueOf(spl[1])+(new Random().nextInt(Integer.valueOf(spl[2])*2)-Integer.valueOf(spl[2]))), Integer.valueOf(spl[3]));
		}
		
		for(ItemStack it : quests.keySet()) {
			int rand = new Random().nextInt(10)-5;
			int price = quests.get(it)+rand;
			ItemMeta met = it.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(lang.getString("quests.1").replaceAll("%money%", String.valueOf(price)));
			lore.add(lang.getString("quests.2"));
			met.setLore(lore);
			it.setItemMeta(met);
		}
		
		for(ItemStack it : quests.keySet()) {
			int rand = new Random().nextInt(100);
			if(rand <= config.getInt("quests_prob")*100) {
				items_today.add(it);
				if(items_today.size() > 30) break;
			}
		}
		HashMap<EntityType, String> entities = new HashMap<EntityType, String>();
		List<String> abilities = new ArrayList<String>();
		
		List<String> boss_entities = config.getStringList("boss_entities");
		for(String b : boss_entities) {
			String[] s = b.split(", ");
			entities.put(EntityType.fromName(s[0]), s[1]);
		}
		
		abilities.add(lang.getString("abilities.1"));
		abilities.add(lang.getString("abilities.2"));
		abilities.add(lang.getString("abilities.3"));
		abilities.add(lang.getString("abilities.4"));
		abilities.add(lang.getString("abilities.5"));
		abilities.add(lang.getString("abilities.6"));
		abilities.add(lang.getString("abilities.7"));

		//String name, EntityType type, Location loc, int hp, int min_lvl, List<String> abilities, double damage
		for(EntityType ent : entities.keySet()) {
			double ran = new Random().nextDouble();
			if(ran <= config.getDouble("boss_prob")) {
				Location portal = createBossIsland();
				if(portal != null) {
					disabled_locations.add(portal);
					int hp = 500 + new Random().nextInt(4501);
					List<String> ab = new ArrayList<String>();
					for(String abb : abilities) {
						double ran2 = new Random().nextDouble();
						if(ran2 <= 0.2) {
							ab.add(abb);
						}
					}
					double damage = 1 + new Random().nextDouble();
					damage = Math.round(damage*100.0)/100.0;
					int min_lvl = 3 + (int) (3*ab.size()) + (int)(hp/1000) + (int)((damage-1)*10);
					Boss boss = new Boss(entities.get(ent), ent, portal, hp, min_lvl, ab, damage);
					location_boss.put(portal, boss);
					is_alive.put(portal, true);
					list_bosses.add(boss);
				}
			}
		}
		bosses = Bukkit.createInventory(null, 9*5, lang.getString("bosses.14"));
		
		updateBossesInv();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				World w = Bukkit.getWorld("world");
				List<Entity> enList = w.getEntities();
				HashMap<String, Integer> hash = new HashMap<String, Integer>();
				
				for(Entity en : enList) {
					if(en instanceof Villager) {
						if(!en.getScoreboardTags().isEmpty() && !en.getCustomName().contains("❤")) {
							for(String tag : en.getScoreboardTags()) {
								if(hash.containsKey(tag)) {
									hash.replace(tag, hash.get(tag)+1);
								}
								else {
									hash.put(tag, 1);
								}
							}
						}
					}
				}
				
				for(Entity en : enList) {
					if(en instanceof Villager) {
						if(!en.getScoreboardTags().isEmpty() && !en.getCustomName().contains("❤")) {
							for(String tag : en.getScoreboardTags()) {
								Location lc = new Location(en.getWorld(), en.getLocation().getX(), 255, en.getLocation().getZ());
								if(hash.get(tag) > 1 || lc.getBlock().getType() != Material.BARRIER) {
									Villager vil = (Villager) en;
									vil.setHealth(0.0);
									hash.replace(tag, hash.get(tag)-1);
								}
							}
						}
					}
				}
			}
		}, 0, 20*60);
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Player pl : Bukkit.getOnlinePlayers()) {
					if(pl != null) scoreboardUpdate(pl);
				}
			}
		}, 0, 20*3);
	}
	
	@EventHandler
	public void tp(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if(e.getCause() == TeleportCause.SPECTATE) e.setCancelled(true);
	}
	
	public void updateBossesInv() {
		if(!list_bosses.isEmpty()) {
			int j = 0;
			for(Boss b : list_bosses) {
				Location loc = b.loc;
				
				ItemStack it = new ItemStack(Material.MAGMA_CREAM);
				ItemMeta meta = it.getItemMeta();
				meta.setDisplayName("§c§l"+b.name);
				List<String> lore = new ArrayList<String>();
				if(is_alive.get(loc)) {
					lore.add(lang.getString("bosses.1"));
				}
				else lore.add(lang.getString("bosses.2"));
				lore.add(lang.getString("bosses.3")+String.valueOf(b.min_lvl));
				lore.add(lang.getString("bosses.4")+String.valueOf(b.hp) + "§c§l❤");
				lore.add(lang.getString("bosses.5")+String.valueOf(b.damage));
				if(b.abilities.isEmpty()) {
					lore.add(lang.getString("bosses.6"));
				}
				else {
					lore.add(lang.getString("bosses.7"));
					for(String ab : b.abilities) {
						lore.add("§f - §7"+ab);
					}
				}
				Location loc2 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
				//loc2.add(-5 + new Random().nextInt(11), -5 + new Random().nextInt(11), -5 + new Random().nextInt(11));
				String res = "";
				if(!localka.isEmpty() && localka.containsKey(j)) {
					res = localka.get(j);
				}
				else {
					String s1 = String.valueOf(loc2.getBlockX());
					String s2 = String.valueOf(loc2.getBlockY());
					String s3 = String.valueOf(loc2.getBlockZ());
					int a1 = 0, a2 = 0, a3 = 0;
					for(int i = 0; i < s1.length(); ++i) {
						double ran = new Random().nextDouble();
						if(ran <= 0.1 && a1 == 0) {
							s1 = s1.substring(0, i)+"?"+s1.substring(i+1);
							a1++;
						}
					}
					for(int i = 0; i < s2.length(); ++i) {
						double ran = new Random().nextDouble();
						if(ran <= 0.2 && a2 == 0) {
							s2 = s2.substring(0, i)+"?"+s2.substring(i+1);
							a2++;
						}
					}
					for(int i = 0; i < s3.length(); ++i) {
						double ran = new Random().nextDouble();
						if(ran <= 0.1 && a3 == 0) {
							s3 = s3.substring(0, i)+"?"+s3.substring(i+1);
							a3++;
						}
					}
					res = s1 + " " + s2 + " " + s3;
					localka.put(j, res);
				}
				lore.add(lang.getString("bosses.8")+res);
				lore.add(" ");
				lore.add(lang.getString("bosses.9"));
				lore.add(lang.getString("bosses.10"));
				lore.add(lang.getString("bosses.11"));
				lore.add(lang.getString("bosses.12"));
				lore.add(lang.getString("bosses.13"));
				meta.setLore(lore);
				it.setItemMeta(meta);
				bosses.setItem(j, it);
				++j;
			}
		}
	}
	
	public void onDisable() {
		
		Location lc, lc2;
		World w = Bukkit.getWorld("world");
		List<Entity> enList = w.getEntities();
		
		for(Loc loc : delete) {
			lc = new Location(w, loc.x, loc.y, loc.z);
			lc2 = new Location(w, loc.x, 255, loc.z);
			if(lc.getBlock().getType() != Material.AIR && lc2.getBlock().getType() != Material.BARRIER) lc.getBlock().setType(Material.AIR);
		}
		for(Entity en : enList) {
			Location loc = en.getLocation();
			lc = new Location(loc.getWorld(), loc.getBlockX(), 255, loc.getBlockY());
			if(lc.getBlock().getType() != Material.BARRIER) {
				en.remove();
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().toLowerCase().equals("skyblock")) {
			if(sender instanceof Player) {
				if(p.hasPermission("API.glav")) {
					if(args.length > 0) {
						String arg = args[0];
						if(arg.equals("delete")) {
							deleteIsland(p);
							send(p, lang.getString("island_deleted"));
						}
						else if(arg.equals("add")) {
							List<ItemStack> its = new ArrayList<ItemStack>();
							List<Double> probs = new ArrayList<Double>();
							
							String type = args[1];
							double prob = Double.valueOf(args[3]);
							
							if(config.contains("islands_types."+type+".layers."+args[2]+".materials")) {
								its = (List<ItemStack>) config.get("islands_types."+type+".layers."+args[2]+".materials");
								probs = (List<Double>) config.get("islands_types."+type+".layers."+args[2]+".probs");
							}
							
							its.add(p.getItemInHand());
							probs.add(prob);
							
							config.set("islands_types."+type+".layers."+args[2]+".materials", its);
							config.set("islands_types."+type+".layers."+args[2]+".probs", probs);
							
							saveConfig2();
							reloadConfig2();
							return false;
						}
						else if(args[0].equals("chestadd")) {
							if(args.length == 3) {
								String key = args[1];
								int chance = Integer.valueOf(args[2]);
								List<ItemStack> list = new ArrayList<ItemStack>();
								if(config.contains("chests."+key)) {
									list = (List<ItemStack>) getConfig().get("chests."+key);
								}
								ItemStack item = p.getItemInHand();
								ItemMeta meta = item.getItemMeta();
								List<String> lore = new ArrayList<String>();
								lore.add(String.valueOf(chance)+"%");
								meta.setLore(lore);
								item.setItemMeta(meta);
								list.add(item);
								config.set("chests."+key, list);
								saveConfig2();
								reloadConfig2();
								return false;
							}
						}
						else if(args[0].equals("getblock")) {
							int x = Integer.valueOf(args[1]);
							double y = Double.valueOf(args[2]);
							ItemStack it = null;
							ItemMeta meta;
							if(x == 1) {
								it = new ItemStack(Material.MOJANG_BANNER_PATTERN);
								meta = it.getItemMeta();
								meta.setDisplayName(lang.getString("scrolls.1"));
								it.setItemMeta(meta);
							}
							else if(x == 2) {
								it = new ItemStack(Material.MOJANG_BANNER_PATTERN);
								meta = it.getItemMeta();
								meta.setDisplayName("§7+"+String.valueOf(y)+" "+lang.getString("scrolls.2"));
								it.setItemMeta(meta);
							}
							else if(x == 3) {
								it = new ItemStack(Material.MOJANG_BANNER_PATTERN);
								meta = it.getItemMeta();
								meta.setDisplayName("§7+"+String.valueOf(y)+" "+lang.getString("scrolls.3"));
								it.setItemMeta(meta);
							}
							else if(x == 4) {
								it = new ItemStack(Material.MOJANG_BANNER_PATTERN);
								meta = it.getItemMeta();
								meta.setDisplayName("§7+"+String.valueOf(y)+" "+lang.getString("scrolls.4"));
								it.setItemMeta(meta);
							}
							p.getInventory().addItem(it);
						}
					}
				}
				else send(p, lang.getString("no_access"));
			}
		}
		else if(cmd.getName().toLowerCase().equals("dis")) {
			if(args.length == 3) {
				try {
					int x = Integer.valueOf(args[0]);
					int y = Integer.valueOf(args[1]);
					int z = Integer.valueOf(args[2]);
					Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
					send(p, lang.getString("island_distance").replaceAll("%distance%", String.valueOf((int)getDistance3d(loc, p.getLocation()))));
				} catch (Exception ee) {
				};
			}
		}
		else if(cmd.getName().toLowerCase().equals("bedtp")) {
			if(p.getGameMode() == GameMode.SPECTATOR) {
				if(p.getBedSpawnLocation() != null) {
					p.teleport(p.getBedSpawnLocation());
					send(p, lang.getString("bed_tp_success"));
				}
				else {
					toIsland(p);
					send(p, lang.getString("bed_tp_fail"));
				}
				p.setGameMode(GameMode.SURVIVAL);
			}
		}
		else if(cmd.getName().toLowerCase().equals("suicide")) {
			p.setHealth(0.0);
		}
		return false;
	}
}
