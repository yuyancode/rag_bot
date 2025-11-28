package org.wcw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class IdGeneratorUtil {
    // 序列号，用于同一毫秒内的区分
    private static final AtomicLong sequence = new AtomicLong(0);

    // 上次生成ID的时间戳
    private static volatile long lastTimestamp = -1L;

    // 序列号的最大值（9999）
    private static final long MAX_SEQUENCE = 9999L;

    public static String generateId() {
        // 获取当前时间戳
        Date now = new Date();

        // 格式化年月日部分
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String datePart = dateFormat.format(now);

        // 获取时间戳部分
        long timestamp = System.currentTimeMillis();

        // 处理序列号
        long seq = 0;
        synchronized (IdGeneratorUtil.class) {
            if (timestamp == lastTimestamp) {
                sequence.incrementAndGet();
                if (sequence.get() > MAX_SEQUENCE) {
                    // 序列号超过最大值，等待下一毫秒
                    timestamp = waitNextMillis(timestamp);
                    sequence.set(0);
                }
            } else {
                sequence.set(0);
            }
            seq = sequence.get();
            lastTimestamp = timestamp;
        }
        return String.format("%s%d%04d", datePart, timestamp, seq);
    }

    /**
     * 等待下一毫秒
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 下一毫秒的时间戳
     */
    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp == lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 生成图书ID
     *
     * @return 图书ID
     */
    private static String generateLibId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i ++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }

    /**
     * 生成文档ID
     *
     * @return 文档ID
     */
    public static String generateDocId() {
        StringBuilder sb = new StringBuilder();
        sb.append("doc");
        Random random = new Random();
        for (int i = 0; i < 7; i ++) {
            // 生成97到122之间的随机整数，对应小写字母的ASCII码
            int randomInt = random.nextInt(26) + 97;
            sb.append((char) randomInt);
        }
        return sb.toString();
    }

}
