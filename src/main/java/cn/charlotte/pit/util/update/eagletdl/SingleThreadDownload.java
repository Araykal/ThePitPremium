package cn.charlotte.pit.util.update.eagletdl;

import java.io.*;
import java.net.HttpURLConnection;

class SingleThreadDownload implements Runnable {

    private final HttpURLConnection connection;
    private final File target;
    private final EagletTask task;

    private transient long currentProgress = 0, lastUpdateTime = System.currentTimeMillis();

    SingleThreadDownload(HttpURLConnection connection, File target, EagletTask task) {
        this.connection = connection;
        this.target = target;
        this.task = task;
    }

    long getLastUpdateTime() {
        return lastUpdateTime;
    }

    long getCurrentProgress() {
        return currentProgress;
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        int len = 0;
        try (BufferedInputStream stream = new BufferedInputStream(connection.getInputStream());
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(target))) {
            while ((len = stream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
                currentProgress += len;
                lastUpdateTime = System.currentTimeMillis();
            }
        } catch (IOException e) {
            task.onError.handle(new ErrorEvent(e, task));
        }
    }
}
