import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestStopper extends JavaPlugin implements Listener{
	private String noperm;
	private String reload;
	private String denyopen;
	protected String prefix;
	private List<String> worlds;
	
	protected boolean toggle = true;
	protected boolean debug = false;
	protected boolean update = false;
	
	protected FileConfiguration config;

	
	// PLUGIN MAIN FUNCTIONS ------------------------------------------------------------------------------------------------------------------
	
	
	public void onEnable() {
		loadconfig();
		new Updater(this);
		
    	getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	private void loadconfig(){
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		
		debug = config.getBoolean("debug");
		
		noperm = ChatColor.translateAlternateColorCodes('&', config.getString("msg.noperm"));
		prefix = ChatColor.translateAlternateColorCodes('&', config.getString("msg.prefix"));
		denyopen = ChatColor.translateAlternateColorCodes('&', config.getString("msg.denyopen"));
		reload = ChatColor.translateAlternateColorCodes('&', config.getString("msg.reload"));
		
		worlds = config.getStringList("worlds");
	}
	
	
	// ON COMMAND ------------------------------------------------------------------------------------------------------------------
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		boolean isplayer = false;
		Player p = null;
		
		if ((sender instanceof Player)) {
			p = (Player)sender;
			isplayer = true;
		}
			
		if(cmd.getName().equalsIgnoreCase("cheststopper") && args.length == 1){
								
			// reload
			if(args[0].equalsIgnoreCase("reload")){
				if(isplayer){
					if(p.hasPermission("cheststopper.reload")){
						loadconfig();
						p.sendMessage(prefix + " " + reload);
					return true;
				}
					else{
						p.sendMessage(noperm);
						return true;
					}
				}else {
					loadconfig();
					System.out.println(ChatColor.stripColor(prefix + " " + reload));
					return true;
				}
			}			
		}
		
		// nothing to do here \o/
		return false;
	}
	
	
	// EVENTS ------------------------------------------------------------------------------------------------------------------
	
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		String world = e.getPlayer().getLocation().getWorld().getName();
			for(String name : worlds) {
				if(world.equalsIgnoreCase(name)) {
					e.setCancelled(true);
					try {
					((Player) e.getPlayer()).sendMessage(prefix + " " + denyopen);
					}catch(Exception e2) {}
					break;
				}
			}
		if(e.getPlayer().hasPermission("cheststopper.bypass")) {
			e.setCancelled(false);
		}
	}

	
	
	// UPDATER ------------------------------------------------------------------------------------------------------------------
	
	
	protected void say(Player p, boolean b) {
		if(b) {
			System.out.println(ChatColor.stripColor(prefix + "------------------------------------------------"));
			System.out.println(ChatColor.stripColor(prefix + " ChestStopper is outdated. Get the new version here:"));
			System.out.println(ChatColor.stripColor(prefix + " http://www.pokemon-online.xyz/plugin"));
			System.out.println(ChatColor.stripColor(prefix + "------------------------------------------------"));
		}else {
		   	p.sendMessage(prefix + "------------------------------------------------");
		   	p.sendMessage(prefix + " ChestStopper is outdated. Get the new version here:");
		   	p.sendMessage(prefix + " http://www.pokemon-online.xyz/plugin");
		   	p.sendMessage(prefix + "------------------------------------------------");
		}
	}

	
}
