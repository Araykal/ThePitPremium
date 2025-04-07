package cn.charlotte.pit.runnable;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.LeaderBoardEntry;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/3 12:57
 */

public class LeaderBoardRunnable implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(LeaderBoardRunnable.class.getName());
    private final ThePit instance;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public LeaderBoardRunnable(ThePit instance) {
        this.instance = instance;
        scheduler.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        try {
            List<Document> documents = loadDocuments();
            List<LeaderBoardEntry> entries = processDocuments(documents);
            updateLeaderBoardEntries(entries);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "更新排行榜数据时发生错误：", e);
        }
    }

    private List<Document> loadDocuments() {
        try (var cursor = instance.getMongoDB()
                .getCollection()
                .find()
                .sort(Filters.eq("totalExp", -1))
                .filter(Filters.gte("lastLogoutTime", System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000))
                .cursor()) {

            List<Document> documents = new ArrayList<>();
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
            return documents;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "从MongoDB加载数据时发生错误：", e);
            throw e;
        }
    }

    private List<LeaderBoardEntry> processDocuments(List<Document> documents) {
        List<LeaderBoardEntry> entries = new ArrayList<>();
        int rank = 1;
        for (Document document : documents) {
            try {
                String name = document.getString("playerName");
                String uuid = document.getString("uuid");
                Double experience = getExperience(document);
                int prestige = document.getInteger("prestige");
                entries.add(new LeaderBoardEntry(name, UUID.fromString(uuid), rank, experience, prestige));
                rank++;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "处理文档时发生错误：", e);
            }
        }
        return entries;
    }

    private Double getExperience(Document document) {
        final Object expObj = document.get("experience");
        try {
            return (Double) expObj;
        } catch (ClassCastException e) {
            try {
                return Double.valueOf(((Integer) expObj));
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "无法转换experience值：", ex);
                return 0.0;
            }
        }
    }

    private void updateLeaderBoardEntries(List<LeaderBoardEntry> entries) {
        synchronized (LeaderBoardEntry.class) {
            LeaderBoardEntry.setLeaderBoardEntries(entries);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.MINUTES)) {
                LOGGER.warning("排行榜更新任务未能在1分钟内停止，强制关闭...");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOGGER.severe("在等待排行榜更新任务停止时被中断。");
            Thread.currentThread().interrupt();
        }
    }
}