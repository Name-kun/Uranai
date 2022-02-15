package xyz.namekun.uranai;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class UranaiCommandTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<String>();
        if (command.getName().equalsIgnoreCase("uranai")) {
            if (args.length == 1) {
                if (sender.hasPermission("uranai")) {
                    tab.add("reload");
                    tab.add("delete");
                    tab.add("reset");
                }
                tab.add("list");
            } if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reset")) {
                    Bukkit.getOnlinePlayers().forEach(player -> tab.add(player.getDisplayName()));
                }
            }
        }
        return tab;
    }
}
