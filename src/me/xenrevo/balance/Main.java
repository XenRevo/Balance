package me.xenrevo.balance;

import java.io.File;
import java.text.DecimalFormat;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
  File configFile;
  FileConfiguration config;
  String header = ChatColor.RED + "[" + ChatColor.AQUA + "RevoTools" + ChatColor.RED + "] ";
  String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "Brutal" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;
  public static Economy economy = null;
  DecimalFormat format = new DecimalFormat("$###,###.##");
  String version = "1.6.1";
  
  public Permission playerPermission = new Permission("balance.others");
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("balance")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage("This command can only be run by a player.");
      } else {
        //Arg 0 = Player name
        int length = args.length;
        Player player = (Player)sender;
        if (length == 1){
        	
        	for (Player playerBal : Bukkit.getServer().getOnlinePlayers()){
        		if(playerBal.getName().equalsIgnoreCase(args[0])){
        			if (!player.hasPermission("balance.others")){
        				player.sendMessage(this.prefix + ChatColor.GRAY + "You do not have permission to use this command");
        			}else if (player.hasPermission("balance.others")){
        				player.sendMessage(format(this.prefix + ChatColor.GOLD + args[0] + "'s " + "&7balance is &c" + this.format.format(economy.getBalance(playerBal.getName()))));
        			}
        		}
        	}
        	
        }
        if (length == 0){
        	player.sendMessage(format("&aBalance: &c" + this.format.format(economy.getBalance(player.getName()))));
        }
       // player.sendMessage(format("&aBalance: &c" + this.format.format(economy.getBalance(player.getName()))));
      }
      return true;
    }
    return false;
  }

  public void onEnable()
  {
    Bukkit.getConsoleSender().sendMessage(this.header + ChatColor.LIGHT_PURPLE + "Loading Please Wait....");
    PluginManager pm = getServer().getPluginManager();
    if (!setupEconomy()) {
      Bukkit.getConsoleSender().sendMessage(this.header + "Disabled because no vault dependency!!!");
      pm.disablePlugin(this);
      pm.addPermission(playerPermission);
      return;
    }

    Bukkit.getConsoleSender().sendMessage(this.header + ChatColor.LIGHT_PURPLE + "PrisonBalance" + " " + this.version );
  }

  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }
    economy = (Economy)rsp.getProvider();
    return economy != null;
  }

  public void onDisable()
  {
  }

  private String format(String message)
  {
    return message.replaceAll("(?i)&([a-z0-9])", "§$1");
  }
}