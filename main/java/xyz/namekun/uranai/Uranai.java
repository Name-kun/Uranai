package xyz.namekun.uranai;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Uranai extends JavaPlugin {

    private UranaiConfigManager configuration;

    @Override
    public void onEnable() {
        loadConfig();
        getCommand("uranai").setExecutor(new UranaiCommand());
        getCommand("uranai").setTabCompleter(new UranaiCommandTab());
        getServer().getPluginManager().registerEvents(new UranaiListener(), this);

        //惑星ループ
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String prefix = ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("prefix"));

                //時間の表記をフォーマットする
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

                //現在時刻がコンフィグと同じだった場合の処理
                if (simpleDateFormat.format(now).equals(UranaiConfigManager.config.get("time"))) {
                    //ファイルが存在しない場合の処理
                    if (!configuration.playerFile.exists()) {
                        getLogger().info(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.noFile")));
                    } else { //ファイルが存在する場合の処理(プレイヤーファイル削除->生成)
                        configuration.playerFile.delete();
                        configuration.createFiles();
                        getLogger().info(prefix + ChatColor.translateAlternateColorCodes('&', UranaiConfigManager.config.getString("message.resetList")));
                    }
                }
            }
        };
        task.runTaskTimer(Uranai.this, 0L, 20L);
    }

    //プラグインロード時のコンフィグ関係の動作
    public void loadConfig() {
        configuration = new UranaiConfigManager();
        configuration.createFiles();
    }
}
