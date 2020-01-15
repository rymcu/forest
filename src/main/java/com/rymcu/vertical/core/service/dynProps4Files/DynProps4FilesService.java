package com.rymcu.vertical.core.service.dynProps4Files;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 动态配置文件,可以设置更新周期
 * 配置读取读取服务
 * @author ronger
 */
@Component
public class DynProps4FilesService {
    private Logger _log = LoggerFactory.getLogger(DynProps4FilesService.class);
    /**
     * 属性文件
     */
    private File[] fileArray;
    /**
     * 启动延时
     */
    private long delay;
    /**
     * 更新周期
     */
    private long period;
    /**
     * 属性对象
     */
    private Properties property = new Properties();
    /**
     * 文件监控器
     */
    private List<FileMonitor> monitors;

    /**
     * @param files  属性文件
     * @param delay  从<code>DynProps</code>被创建到第一次动态监视的时间间隔. 约束范围delay > 0
     * @param period 动态监视的时间间隔. 约束范围period >= 0；等于0表示不执行动态监视，退化为静态配置文件．
     */
    public DynProps4FilesService(File[] files, long delay, long period) throws IOException {
        this.fileArray = files;
        this.delay = delay;
        this.period = period;
        init();
    }

    public DynProps4FilesService(List<String> fileNames, long delay, long period) throws IOException {
        this.delay = delay;
        this.period = period;
        fileArray = new File[fileNames.size()];
        int index = 0;
        for (String oriFileName : fileNames) {
            String fileName = oriFileName.trim();
            if (StringUtils.indexOfIgnoreCase(fileName, "classpath:") == 0) {
                fileArray[index++] = new File(
                        this.getClass().getClassLoader().getResource("").getPath() + File.separator +
                                fileName.substring("classpath:".length()));
            } else {
                fileArray[index++] = new File(fileName);
            }
        }

        init();
    }


    public DynProps4FilesService(String fileNames, long delay, long period) throws IOException {
        this.delay = delay;
        this.period = period;
        boolean isClassPath = false;
        if (fileNames.startsWith("classpath")) {
            fileNames = fileNames.substring("classpath:".length());
            isClassPath = true;
        }
        String[] fileName = fileNames.split("[,|，|;|；]");
        fileArray = new File[fileName.length];
        if (isClassPath) {
            for (int i = 0; i < fileName.length; i++) {
                fileArray[i] = new File(this.getClass().getClassLoader().getResource("").getPath() + fileName[i]);
            }
        } else {
            for (int i = 0; i < fileName.length; i++) {
                fileArray[i] = new File(fileName[i]);
            }
        }

        init();
    }

    public DynProps4FilesService(File[] files, long period) throws IOException {
        this(files, 0, period);
    }

    public DynProps4FilesService(String fileNames, long period) throws IOException {
        this.period = period;
        this.delay = 0;
        String[] fileName = fileNames.split("[,|，|;|；]");

        File[] files = new File[fileName.length];
        for (int i = 0; i < fileName.length; i++) {
            files[i] = new File(fileName[i]);
        }
        init();
    }

    public DynProps4FilesService() {
    }

    /**
     * 加载属性文件,启动监控
     *
     * @throws IOException 加载文件时出现IO异常
     */
    protected void load() throws IOException {
        update();
        if (monitors == null) {
            monitors = new ArrayList<FileMonitor>(fileArray.length);
        } else {
            for (FileMonitor monitor : monitors) {
                try {
                    monitor.timer.cancel();
                } catch (Exception e) {
                    _log.warn(String.format("Timer for file [%s] cancelling failed.", monitor.file.getAbsolutePath()));
                }
            }
        }

        for (File file : fileArray) {
            long lastModify = file.lastModified();
            FileMonitor monitor = new FileMonitor(file, lastModify);
            this.monitors.add(monitor);
            monitor.doTask();
        }
    }

    /**
     * 如果文件有更新调用此方法载入
     *
     * @throws IOException 没有找到文件或读文件错误时抛出
     */
    protected void update() throws IOException {
        for (File file : fileArray) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                this.property.load(in);
            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw (IOException) e;
                }

                throw new IOException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
    }

    /**
     * @param key 需要获取属性值的KEY
     * @param def 默认值
     *
     * @return 属性值
     */
    public String getProperty(String key, String def) {
        String val = this.property.getProperty(key);
        return val == null ? def : val.trim();
    }

    public String getProperty(String key) {
        String val = this.property.getProperty(key);
        return val == null ? null : val.trim();
    }

    /**
     * 设置属性值
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        this.property.setProperty(key, value);
    }

    /**
     * @param key 需要获取属性值的KEY
     * @param def 默认值
     *
     * @return 属性值
     *
     * @throws NumberFormatException 如果属性值不是整数形式
     */
    public int getInt(String key, int def) throws NumberFormatException {
        String val = this.getProperty(key);
        return val == null ? def : Integer.parseInt(val);
    }

    public int getInt(String key) throws NumberFormatException {
        return getInt(key, 0);
    }

    public float getFloat(String key, float def) throws NumberFormatException {
        String val = this.getProperty(key);
        return val == null ? def : Float.parseFloat(val);
    }

    public float getFloat(String key) throws NumberFormatException {
        return getFloat(key, 0.0f);
    }

    public double getDouble(String key, double def) {
        String val = this.getProperty(key);
        return val == null ? def : Double.parseDouble(val);
    }
    public double getDouble(String key) throws NumberFormatException {
        return getDouble(key,0.0);
    }



    public long getLong(String key, long def) {
        String val = this.getProperty(key);
        return val == null ? def : Long.parseLong(val);
    }

    public long getLong(String key) throws NumberFormatException {
        return getLong(key, 0L);
    }

    private void init() throws IOException {
        for (File file : fileArray) {
            if (!file.exists() || file.length() == 0) {
                throw new IllegalArgumentException("动态配置文件 " + file.getAbsolutePath() + " 不存在,或是空文件！");
            }
            if (delay <= 0) {
                throw new IllegalArgumentException("定时器延时时间不能为负数！");
            }
            if (period <= 0) {
                throw new IllegalArgumentException("定时器更新周期不能为负数！");
            }
            this.property = new Properties();
            this.load();// 初始构造时，执行第一次加载.
        }
        //当进程终止时，取消定时任务
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
    }

    private class ShutdownHook implements Runnable {
        private DynProps4FilesService dynProps4FilesService;

        @Override
        public void run() {
            System.out.println("Monitors cancelling start ...");
            if (monitors != null) {
                for (FileMonitor monitor : monitors) {
                    try {
                        monitor.timer.cancel();
                    } catch (Exception e) {
                        _log.warn(String.format("Timer for file [%s] cancelling failed.",
                                monitor.file.getAbsolutePath()));
                    }
                }
            }
        }
    }

    /**
     * 描述：一个内部私有类，实时监控文件有没有更新，如果更新则自动载入
     */
    private class FileMonitor {

        private long lastModifiedTime;
        private File file;
        /**
         * 定时器，以守护线程方式启动
         */
        private Timer timer = new Timer(true);

        /**
         * @param lastMonitorTime 最后的更新时间
         */
        private FileMonitor(File file, long lastMonitorTime) {
            this.file = file;
            this.lastModifiedTime = lastMonitorTime;
        }

        /**
         * 对文件进行实时监控，有更新则自动载入
         */
        private void doTask() {
            if (delay < 0) {
                delay = 0L;
            }
            if (period <= 0) {
                return;// 如果更新周期非正数，则退化成静态配置文件.
            }
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long t = file.lastModified();
                    // 文件被删除
                    // 如果动态更新过程中，配置文件被强制删除了，本次不执行任何更新.或者对配置文件进行恢复
                    if (t == 0) {
                        try {
                            if (file.createNewFile()) {
                                OutputStream fos = new FileOutputStream(file);
                                property.store(fos, "文件被删除，自动恢复．");
                                fos.close();
                            }
                        } catch (IOException ioe2) {
                            // 这里基本上只有磁盘空间满才会发生，暂时不处理
                        }
                        return;
                    }
                    // 文件被更新
                    if (t > lastModifiedTime) {
                        lastModifiedTime = t;
                        // 2秒后还在改变，则本次更新不做处理
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            // do nothing
                        }
                        if (t != file.lastModified()) {
                            _log.info("文件可能未更新完成，本次不更新！");
                        } else {
                            try {
                                property.clear();
                                update();
                                _log.info("UPDATED " + file.getAbsolutePath());
                            } catch (IOException ioe) {
                                _log.error("UPDATING " + file.getAbsolutePath() + " failed", ioe);
                            }
                        }
                        _log.debug("-----------------------:" + property.keySet());
                    }
                }// end run()
            }, delay, period);
        }
    }
}
