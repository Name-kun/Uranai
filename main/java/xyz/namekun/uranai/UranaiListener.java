package xyz.namekun.uranai;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UranaiListener implements Listener {

    //愛すべきメソッド集
    private final Uranai plugin = Uranai.getPlugin(Uranai.class);
    private final File playerFile = new File(plugin.getDataFolder(), "players.yml");

    String prefix = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("prefix"));

    //プレイヤーが「占い」と発言した時のイベントを設定
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getMessage().equals("uranai") || e.getMessage().equals("占い " + ChatColor.GRAY + "uranai") || e.getMessage().equals("占い")) {
                    if (UranaiConfigManager.playerConf.getStringList("playerList").contains(String.valueOf(p.getUniqueId()).replaceAll("-", ""))) {
                        p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.alreadySent")));
                    } else {
                        List<String> list = UranaiConfigManager.playerConf.getStringList("playerList");
                        list.add(String.valueOf(p.getUniqueId()).replaceAll("-", ""));
                        UranaiConfigManager.playerConf.set("playerList", list);
                        List<String> luckList;
                        CommandSender console = Bukkit.getConsoleSender();
                        int r = (int) (Math.random() * (100 - 1)) + 1;
                        String luck;
                        if (r <= 5) luck = "daikichi";
                        else if (r <= 15) luck = "kichi";
                        else if (r <= 35) luck = "chukichi";
                        else if (r <= 65) luck = "shokichi";
                        else if (r <= 85) luck = "suekichi";
                        else if (r <= 95) luck = "kyo";
                        else luck = "daikyo";
                        Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", UranaiConfigManager.config.getString(luck).replaceAll("&", "§")).replaceAll("%player%", p.getDisplayName()));
                        UranaiConfigManager.config.getStringList("execution." + luck).forEach(exec -> Bukkit.dispatchCommand(console, exec.replaceAll("%player%", p.getDisplayName())));
                        luckList = UranaiConfigManager.playerConf.getStringList(luck);
                        luckList.add(p.getDisplayName());
                        UranaiConfigManager.playerConf.set(luck, luckList);
                        try { //プレイヤーのUUIDを保存する
                            UranaiConfigManager.playerConf.save(playerFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }.runTaskLater(plugin, 20L);
    }
}
