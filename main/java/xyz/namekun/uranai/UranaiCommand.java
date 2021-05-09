package xyz.namekun.uranai;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UranaiCommand implements CommandExecutor {

    private final UranaiConfigManager configuration = new UranaiConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("prefix"));

        if (command.getName().equalsIgnoreCase("uranai")) {
            if (!sender.hasPermission("uranai")) { //権限がない場合の処理
                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.noPerm")));
            } else { //権限がある場合の処理
                if (args.length < 1) { //引数がない場合、ヘルプを表示
                    for (String help : UranaiConfigManager.config.getStringList("message.help")) {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', help));
                    }
                } else { //引数がある場合
                    if (args[0].equalsIgnoreCase("reload")) { //引数がreloadだった場合
                        configuration.createFiles();
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.reload")));
                    } else if (args[0].equalsIgnoreCase("delete")) { //引数がdeleteだった場合
                        configuration.playerFile.delete();
                        configuration.createFiles();
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.resetList")));
                    } else { //正しい引数ではない場合
                        for (String help : UranaiConfigManager.config.getStringList("message.help")) {
                            sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', help));
                        }
                    }
                }
            }
        }
        return true;
    }
}
