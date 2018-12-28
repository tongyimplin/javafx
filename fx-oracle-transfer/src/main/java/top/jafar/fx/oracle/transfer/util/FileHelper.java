package top.jafar.fx.oracle.transfer.util;

import java.io.*;

public class FileHelper implements Closeable {
    private static String WORK_DIR = System.getProperty("user.home") + "/fx-oracle-transfer/";

    /**
     * 获取文件
     * @param fileName
     * @return
     */
    public static File getFile(String fileName) {
        return new File(WORK_DIR, fileName);
    }

    public static FileHelper getWritableFile(String subDirName) {
        File file = getFile(subDirName + "/" + DateHelper.nowTimeStampPureNumber() + ".log");
        Logger.log("SQL日志文件: "+file.toString());
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new FileHelper(file);
    }

    private File file;
    private FileWriter writer;
    private FileHelper(File file) {
        this.file = file;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充写入字符串
     * @param obj
     */
    public void append(Object obj) {
        if (obj != null) {
            try {
                writer.append(obj.toString()).append( System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    public String getLogPath() {
        return file.toString();
    }
}
