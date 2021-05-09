package xyz.namekun.uranai;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UranaiListener implements Listener {

    //愛すべきメソッド集
    private final Uranai plugin = Uranai.getPlugin(Uranai.class);
    private final File playerFile = new File(plugin.getDataFolder(), "players.yml");

    //プレイヤーが「占い」と発言した時のイベントを設定
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String prefix = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("prefix"));

        //Stringハラスメント
        String daikichi = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("daikichi"));
        String kichi = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("kichi"));
        String chukichi = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("chukichi"));
        String shokichi = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("shokichi"));
        String suekichi = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("suekichi"));
        String kyo = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("kyo"));
        String daikyo = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("daikyo"));

        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getMessage().equals("uranai") || e.getMessage().equals("占い")) {
                    if (UranaiConfigManager.playerConf.getStringList("playerList").contains(String.valueOf(p.getUniqueId()))) {
                        p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.alreadySent")));
                    } else {
                        List<String> list = UranaiConfigManager.playerConf.getStringList("playerList");
                        list.add(String.valueOf(p.getUniqueId()));
                        UranaiConfigManager.playerConf.set("playerList", list);
                        try { //プレイヤーのUUIDを保存する
                            UranaiConfigManager.playerConf.save(playerFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        int r = (int) (Math.random() * (100 - 1)) + 1;
                        if (r <= 5) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", daikichi).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item1 = new ItemStack(Material.NETHERITE_INGOT);
                            p.getInventory().addItem(item1);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", daikichi));
                        } else if (r <= 15) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", kichi).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item2 = new ItemStack(Material.DIAMOND, 3);
                            p.getInventory().addItem(item2);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", kichi));
                        } else if (r <= 35) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", chukichi).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item3 = new ItemStack(Material.IRON_INGOT, 3);
                            p.getInventory().addItem(item3);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", chukichi));
                        } else if (r <= 65) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", shokichi).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item4 = new ItemStack(Material.GOLDEN_APPLE);
                            p.getInventory().addItem(item4);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", shokichi));
                        } else if (r <= 85) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", suekichi).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item5 = new ItemStack(Material.TURTLE_EGG, 3);
                            p.getInventory().addItem(item5);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", suekichi));
                        } else if (r <= 95) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", kyo).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item6 = new ItemStack(Material.RABBIT_FOOT, 3);
                            p.getInventory().addItem(item6);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", kyo));
                        } else if (r <= 100) {
                            Bukkit.broadcastMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.luck")).replaceAll("%luck%", daikyo).replaceAll("%player%", p.getDisplayName()));
                            ItemStack item7 = new ItemStack(Material.POTION);
                            PotionMeta meta = (PotionMeta) item7.getItemMeta();
                            meta.addCustomEffect(new PotionEffect(PotionEffectType.LUCK, 6000, 0), true);
                            meta.setColor(Color.LIME);
                            meta.setDisplayName(ChatColor.GREEN + "幸運のポーション");
                            item7.setItemMeta(meta);
                            p.getInventory().addItem(item7);
                            p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.obtainBonus")).replaceAll("%luck%", daikyo));
                        }
                    }
                }
            }
        }.runTaskLater(plugin, 20L);
    }
}
