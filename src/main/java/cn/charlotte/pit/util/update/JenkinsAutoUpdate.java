package cn.charlotte.pit.util.update;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.runnable.RebootRunnable;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.update.eagletdl.EagletTask;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Base64;

public class JenkinsAutoUpdate {
    private final DecimalFormat format = new DecimalFormat("0.00");
    private String version = "lastSuccessfulBuild";

    public JenkinsAutoUpdate() {
    }

    public JenkinsAutoUpdate(String version) {
        this.version = version;
    }

    @SneakyThrows
    public void download() {
        Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
        getFileMethod.setAccessible(true);
        File pluginFile = (File) getFileMethod.invoke(ThePit.getInstance());

        String AUTH_TOKEN = "11423594d3605c02520513dbdfad4fc7ab";
        new EagletTask().url("http://ci.emptyirony.com:2021/job/ThePit/" + version + "/cn.charlotte.pit$ThePit/artifact/cn.charlotte.pit/ThePit/1.0-SNAPSHOT/ThePit-1.0-SNAPSHOT.jar")
                .file(pluginFile)
                .requestMethod("POST")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(("EmptyIrony:" + AUTH_TOKEN).getBytes()))
                .setThreads(1)
                .setOnConnected(event -> {
                    CC.boardCastWithPermission(CC.translate("&7[持续集成] &a连接至CI成功，正在进行更新..."), "pit.admin");
                })
                .setOnError(event -> {
                    System.out.println("Error " + event.getException());
                    CC.boardCastWithPermission(CC.translate("&7[持续集成] &c" + event.getException()), "pit.admin");
                    CC.boardCastWithPermission(CC.translate("&7[持续集成] &a更新失败，请在控制台检查错误"), "pit.admin");
                })
                .setOnProgress(event -> {
                    CC.boardCastWithPermission(CC.translate("&7[持续集成] &a正在进行更新..&e(" + format.format(event.getPercentage() * 100) + "%) &7下载速度: " + event.getSpeedFormatted() + "/s"), "pit.admin");
                })
                .setOnComplete(event -> {
                    CC.boardCastWithPermission(CC.translate("&7[持续集成] &a更新完成"), "pit.admin");
                    ThePit.getInstance()
                            .getRebootRunnable()
                            .addRebootTask(new RebootRunnable.RebootTask("游戏更新", System.currentTimeMillis() + 30 * 1000));
                })
                .start();
    }
}
