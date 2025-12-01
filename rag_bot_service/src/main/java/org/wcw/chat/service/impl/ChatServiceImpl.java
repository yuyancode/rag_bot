package org.wcw.chat.service.impl;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.wcw.chat.ai.assistant.AssistantService;
import org.wcw.chat.ai.assistant.IAssistant.StreamingAssistant;
import org.wcw.chat.ai.assistant.IAssistant.SummarizeAssistant;
import org.wcw.chat.ai.assistant.IAssistant.WebSearchAssistant;
import org.wcw.chat.domain.convert.ChatMessageConverter;
import org.wcw.chat.domain.entity.ChatMessageDO;
import org.wcw.chat.domain.vo.request.ChatRequest;
import org.wcw.chat.domain.vo.request.UpdateConversationTitleCommand;
import org.wcw.chat.domain.vo.response.ChatMessageResponse;
import org.wcw.chat.domain.vo.response.FileUploadResponse;
import org.wcw.chat.mapper.ChatMessageMapper;
import org.wcw.chat.service.IChatService;
import org.wcw.chat.service.IConversationSideBarService;
import org.wcw.common.dto.FileUploadDTO;
import org.wcw.upload.LocalUploadFileStrategy;
import org.wcw.upload.UploadFileStrategy;
import org.wcw.utils.FileUtils;

import dev.langchain4j.data.document.DocumentParser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final ChatMessageMapper chatMessageMapper;
    private final ChatMessageConverter chatMessageConverter;
    private final AssistantService assistantService;
    private final SummarizeAssistant summarizeAssistant;
    private final WebSearchAssistant webSearchAssistant;;
    private final IConversationSideBarService conversationSideBarService;
    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    private final Map<String, String> filePathMap = new HashMap<>();

    @Override
    public SseEmitter chat(String memoryId, ChatRequest request) {
        SseEmitter emitter = new SseEmitter(-1L);
        String fileId = request.getFileId();

        // 判断是否是首次提问，更新标题
        boolean isFirstQuestion = isFirstQuestion(memoryId);
        if (isFirstQuestion) {
            log.info("用户{}在会话{}中首次提问", request.getUserId(), memoryId);
            String newTitle = summarizeAssistant.summarize(request.getUserMessage());
            conversationSideBarService.updateChatConversationTitle(
                   UpdateConversationTitleCommand
                           .builder()
                           .memoryId(memoryId)
                           .newTitle(newTitle)
                           .build()
            );
        }

        // 上传了附件
        if(!StringUtils.isEmpty(fileId)) {
            String filePath = filePathMap.get(fileId);
            loadFile2Store(filePath);
        }

        try {
            StreamingAssistant assistant = assistantService.getRAGAssistant(memoryId, request.getUserId());
            // 开启联网搜索
            if(request.getIsWebSearchRequest()) {
                assistant = webSearchAssistant;
            }
            TokenStream tokenStream = assistant.chat(memoryId, request.getUserMessage());
            tokenStream
                    .onPartialResponse(token -> {
                        try{
                            emitter.send(SseEmitter.event()
                                    .data(token)
                                    .id(String.valueOf(System.currentTimeMillis()))
                                    .name("message"));
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .onCompleteResponse(chatResponse -> {
                        try{
                            emitter.send(SseEmitter.event()
                                    .data("[DONE]")
                                    .id("done")
                                    .name("done"));
                            emitter.complete();
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .onError(e -> emitter.completeWithError(e))
                    .start();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    /**
     * 查询历史消息
     * @param memoryId
     * @return
     */
    @Override
    public List<ChatMessageResponse> queryHistoryMessages(String memoryId) {
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectByMemoryId(memoryId);
        return chatMessageConverter.toDtoList(chatMessageDOS);
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @Override
    public FileUploadResponse uploadFile4Chat(MultipartFile file) {
        UploadFileStrategy uploadFileStrategy = new LocalUploadFileStrategy();
        FileUploadDTO uploadDTO = uploadFileStrategy.upload(file, "/tmp");
        filePathMap.put(uploadDTO.getFileId(), uploadDTO.getFilePath());
        return FileUploadResponse.builder()
                .fileId(uploadDTO.getFileId())
                .fileUrl(uploadDTO.getFileUrl())
                .filePath(uploadDTO.getFilePath())
                .fileName(uploadDTO.getFileName())
                .build();
    }

    private void loadFile2Store(String filePath) {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = loadDocument(path.toString());
        // 删除临时文件
        FileUtils.deleteFile(filePath);
        embeddingStoreIngestor.ingest(document);
    }

    private boolean isFirstQuestion(String memoryId) {
        // 查询该会话下用户发动的消息数量
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectByMemoryIdAndRole(memoryId, "user");
        return chatMessageDOS.isEmpty();
    }


}
