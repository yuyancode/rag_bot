package org.wcw.chat.ai.service;


import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wcw.chat.ai.assistant.IAssistant.SummarizeAssistant;
import org.springframework.context.ApplicationContext;

import java.time.Duration;
import java.util.List;



@Service
@RequiredArgsConstructor
@Slf4j
public class ChatCompressionService {
    private static final String COMPRESSED_KEY_PREFIX = "chat:compressed:";
    private static final String COMPRESSION_TIME_KEY_PREFIX = "chat:compressed:last_time:";

    private final StringRedisTemplate stringRedisTemplate;
    private final ApplicationContext applicationContext;

    @Value("${chat.context.window-size}")
    private int CONTEXT_WINDOW_SIZE;

    @Value("${chat.context.expire-time}")
    private int CONTEXT_EXPIRE;

    /**
     * 异步压缩并更新redis中的对话上下文
     * @param memoryId 会话ID
     * @param messages 聊天记录
     */
    @Async("chatCompressionExecutor")
    public void compressAndUpdateContext(String memoryId, List<ChatMessage> messages) {
        long startTime = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();

        log.info("开启异步压缩，memoryId:{}, 线程：{}, 消息数：{}", memoryId, threadName, messages.size());

        try {
            String context = ChatMessageSerializer.messagesToJson(messages);

            // 压缩当前消息列表
            List<ChatMessage> compressedMessages = compressMessages(context);
            String compressedJson = ChatMessageSerializer.messagesToJson(compressedMessages);

            // 保存到redis
            String key = COMPRESSED_KEY_PREFIX + memoryId;
            stringRedisTemplate.opsForValue().set(key, compressedJson, Duration.ofHours(24));

            String timeKey = COMPRESSION_TIME_KEY_PREFIX + memoryId;
            // 24小时过期，比压缩间隔长
            stringRedisTemplate.opsForValue().set(timeKey, System.currentTimeMillis() + "", Duration.ofHours(24));

            long totalTime = System.currentTimeMillis() - startTime;
            log.info("异步压缩完成，memoryId: {}， 线程：{}, 原始{}条 -> 压缩{}条，总耗时{}ms",
                    memoryId, threadName, messages.size(), compressedMessages.size());

        } catch (Exception e) {
            long totalTime = System.currentTimeMillis() - startTime;
            log.warn("异步压缩失败，memoryId: {}, 线程：{},总耗时{}ms,错误：{}",
                    memoryId, threadName, totalTime, e.getMessage());
            // 不影响主流程，继续执行
        }
    }

    /**
     * 压缩对话上下文
     * @param context
     * @return
     */
    private List<ChatMessage> compressMessages(String context) {
        long startTime = System.currentTimeMillis();

        // 先反序列化一次，避免重复操作
        List<ChatMessage> originalMessages = ChatMessageDeserializer.messagesFromJson(context);
        int originalSize = originalMessages.size();

        try {
            SummarizeAssistant summarizeAssistant = getSummarizeAssistant();
            String summarizedContext = summarizeAssistant.multiQuerySummarize(context, CONTEXT_WINDOW_SIZE);
            List<ChatMessage> result = ChatMessageDeserializer.messagesFromJson(summarizedContext);


            // 记录压缩指标
            long compressionTime = System.currentTimeMillis() - startTime;
            double compressionRatio = (double) result.size() / originalSize;

            log.info("AI压缩成功：原始{}条 -> 压缩{}条，压缩比{：.2f},耗时{}ms",
                    originalSize, result.size(), compressionRatio, compressionTime);

            return result;
        } catch (Exception e) {
            log.warn("AI压缩失败，采取简单截取策略：{}",e.getMessage());
            // 降级策略：使用以及反序列化的消息，避免重复操作
            List<ChatMessage> turncatedResult = simpleMessageTruncation(originalMessages);
            log.info("降级截取完成：原始{}条 -> 截取{}条", originalSize, turncatedResult.size());
            return turncatedResult;

        }
    }
    /**
     * 简单截取策略
     * @param messages
     * @return
     */
    private List<ChatMessage> simpleMessageTruncation(List<ChatMessage> messages) {
        if (messages.size() <= CONTEXT_WINDOW_SIZE * 2) {
            return messages;
        }

        // 保留最近的消息
        int keepCount = CONTEXT_WINDOW_SIZE * 2;
        int startIndex = Math.max(0, messages.size() - keepCount);

        return messages.subList(startIndex, messages.size());
    }

    /**
     * 获取AI摘要助手
     * @return
     */
    private SummarizeAssistant getSummarizeAssistant() {
        return applicationContext.getBean(SummarizeAssistant.class);
    }
}
