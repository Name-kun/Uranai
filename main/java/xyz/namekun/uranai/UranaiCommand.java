package xyz.namekun.uranai;

import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UranaiCommand implements CommandExecutor {

    //愛すべきメソッド集
    private final Uranai plugin = Uranai.getPlugin(Uranai.class);
    private final File playerFile = new File(plugin.getDataFolder(), "players.yml");
    private final UranaiConfigManager conf = new UranaiConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("prefix"));

        if (command.getName().equalsIgnoreCase("uranai")) {
            if (args.length < 1) { //引数がない場合、ヘルプを表示
                for (String help : UranaiConfigManager.config.getStringList("message.help")) {
                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', help));
                }
            } else { //引数がある場合
                if (args[0].equalsIgnoreCase("reload")) { //引数がreloadだった場合
                    if (!sender.hasPermission("uranai")) { //権限がない場合の処理
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.noPerm")));
                        return true;
                    }
                    conf.createFiles();
                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.reload")));
                } else if (args[0].equalsIgnoreCase("delete")) { //引数がdeleteだった場合
                    if (!sender.hasPermission("uranai")) { //権限がない場合の処理
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.noPerm")));
                        return true;
                    }
                    playerFile.delete();
                    conf.createFiles();
                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.resetList")));
                } else if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(leaderBoard());
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (!sender.hasPermission("uranai")) {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.noPerm")));
                        return true;
                    }
                    if (args.length <= 1) {
                        sender.sendMessage("§cクールダウンを解除したいプレイヤー名を指定してください。");
                        return true;
                    }
                    if (UranaiConfigManager.playerConf.getStringList("playerList").contains(getUuid(args[1]))) {
                        List<String> list = UranaiConfigManager.playerConf.getStringList("playerList");
                        list.remove(getUuid(args[1]));
                        UranaiConfigManager.playerConf.set("playerList", list);
                        try {
                            UranaiConfigManager.playerConf.save(playerFile);
                            sender.sendMessage(ChatColor.GOLD + args[1] + "§aのクールダウンを解除しました。");
                        } catch (IOException e) {
                            Bukkit.getLogger().info("§cセーブできませんでした。");
                        }
                    } else {
                        sender.sendMessage("§cそのプレイヤーは存在しないか占っていません。");
                    }
                } else { //正しい引数ではない場合
                    for (String help : UranaiConfigManager.config.getStringList("message.help")) {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', help));
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("playtime")) {
            if (args.length == 0) {
                sender.sendMessage("プレイヤー名を指定してください。");
            } else {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player p = Bukkit.getPlayer(args[0]);
                    int statistic = p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
                    sender.sendMessage(String.valueOf(TimeUnit.SECONDS.toHours(statistic)));
                }
            }
        } else if (command.getName().equalsIgnoreCase("getunko")) {
            if (sender.hasPermission("getunko")) {
                Player p = (Player) sender;
                p.getInventory().addItem(unko());
            }
        } else if (command.getName().equalsIgnoreCase("getuuid")) {
            sender.sendMessage(getUuid(args[0]));
        } else if (command.getName().equalsIgnoreCase("sendtext")) {
            if (!sender.hasPermission("sendtext")) {
                sender.sendMessage("§c権限がありません。");
                return true;
            }
            if (args.length <= 0) {
                sender.sendMessage("§a/sendtext <player> <message>");
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (args[0].equals("all")) {
                    if (args.length < 2) {
                        sender.sendMessage("§c送りたい内容を指定してください。");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            builder.append(args[i]).append(" ");
                        }
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', builder.toString().replaceAll("``", "\n")));
                    }
                } else if (target == null) {
                    sender.sendMessage("§cそのプレイヤーは存在しません。");
                } else {
                    if (args.length < 2) {
                        sender.sendMessage("§c送りたい内容を指定してください。");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            builder.append(args[i]).append(" ");
                        }
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', builder.toString()).replaceAll("``", "\n"));
                    }
                }
            }
        }
        return true;
    }

    private String leaderBoard() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> lucks = new ArrayList<>(Arrays.asList("daikichi", "kichi", "chukichi", "shokichi", "suekichi", "kyo", "daikyo"));
        stringBuilder.append("§3§l占いリーダーボード\n").append("§7-----------------\n");
        for (String luck : lucks) {
            stringBuilder.append(UranaiConfigManager.config.getString(luck).replaceAll("&", "§")).append("§7: §f");
            UranaiConfigManager.playerConf.getStringList(luck).forEach(player -> stringBuilder.append(player).append(" "));
            stringBuilder.append("\n");
        }
        stringBuilder.append("§7-----------------");
        return stringBuilder.toString();
    }

    public String getUuid(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            @SuppressWarnings("deprecation")
            String UUIDJson = IOUtils.toString(new URL(url));
            if (UUIDJson.isEmpty()) return "invalid name";
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "error";
    }

    public ItemStack unko() {
        ItemStack item = new ItemStack(Material.BROWN_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a光§bる§cう§dん§eこ");
        List<String> lore = new ArrayList<>();
        lore.add("§2光る謎のうんこ。");
        lore.add("§2シベリアの永久凍土から発掘されたらしい。");
        lore.add("§2強烈な運気を持っている...？");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        //nbt関係
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("Unko", "Kusai");
        nbtItem.applyNBT(item);
        return item;
    }
}
