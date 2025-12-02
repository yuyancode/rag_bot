package org.wcw.library.service.Impl;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wcw.common.dto.FileUploadDTO;
import org.wcw.library.domain.convert.KnowledgeLibDocumentConvert;
import org.wcw.library.domain.entity.KnowledgeLibDocumentDO;
import org.wcw.library.domain.vo.request.CreateKnowledgeLibDocCommand;
import org.wcw.library.domain.vo.request.DeleteKnowledgeLibDocCommand;
import org.wcw.library.domain.vo.request.UpdateKnowledgeLibDocCommand;
import org.wcw.library.domain.vo.response.KnowledgeLibDocumentResponse;
import org.wcw.library.mapper.KnowledgeLibDocumentMapper;
import org.wcw.library.mapper.KnowledgeLibMapper;
import org.wcw.library.service.IKnowledgeLibDocumentService;
import org.wcw.upload.FileUploadFactory;
import org.wcw.upload.LocalUploadFileStrategy;
import org.wcw.utils.FileUtils;
import org.wcw.utils.IdGeneratorUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
@RequiredArgsConstructor
public class KnowledgeLibDocumentServiceImpl implements IKnowledgeLibDocumentService {
    private final KnowledgeLibDocumentMapper documentMapper;
    private final KnowledgeLibMapper knowledgeLibMapper;
    private final KnowledgeLibDocumentConvert knowledgeLibDocumentConvert;
    private final FileUploadFactory fileUploadFactory;
    private final EmbeddingStoreIngestor ingestor;


    @Override
    public void addDocument(CreateKnowledgeLibDocCommand command) {
        KnowledgeLibDocumentDO documentDO = new KnowledgeLibDocumentDO();
        documentDO.setDocumentName(command.getDocumentName());
        documentDO.setDocumentDesc(command.getDocumentDesc());
        documentDO.setDocumentId(IdGeneratorUtil.generateDocId());
        documentDO.setKnowledgeLibId(command.getKnowledgeLibId());
        FileUploadDTO upload = fileUploadFactory.getUploadStrategy().upload(command.getFile(), "doc");

        // todo 上传到本地，导入向量数据库后删除 - 向量数据库加载貌似必须从本地文件中导入，后面看看
        LocalUploadFileStrategy fileStrategy = new LocalUploadFileStrategy();
        FileUploadDTO dto = fileStrategy.upload(command.getFile(), "/tmp");

        documentDO.setUrl(dto.getFileUrl());
        documentDO.setPath(dto.getFilePath());
        documentDO.setDocumentSize(FileUtils.getFileSizeInMB(command.getFile()));

        documentMapper.insert(documentDO);

        // 更新向量数据库
        loadFile2Store(dto.getFilePath());

        // 更新文档数量
        updateKnowledgeLibDocumentCount(documentDO.getKnowledgeLibId());
    }

    /**
     * 加载文件到向量数据库
     * @param filePath
     */
    private void loadFile2Store(String filePath) {
        Path path = Paths.get(filePath).toAbsolutePath();
        DocumentParser parser = new ApacheTikaDocumentParser();
        Document document = loadDocument(path.toString(), parser);

        // 删除临时文件
        FileUtils.deleteFile(filePath);
        ingestor.ingest(document);
    }

    /**
     * 更新知识库文档数量
     * @param knowledgeLibId
     */
    private void updateKnowledgeLibDocumentCount(String knowledgeLibId) {
        knowledgeLibMapper.updateDocumentCount(knowledgeLibId, queryDocumentCount(knowledgeLibId));
    }


    @Override
    public void batchAddDocument(List<KnowledgeLibDocumentDO> documents) {
        if (!documents.isEmpty()) {
            documentMapper.batchInsert(documents);
            updateKnowledgeLibDocumentCount(documents.get(0).getKnowledgeLibId());
        }
    }

    @Override
    public KnowledgeLibDocumentDO queryDocument(String knowledgeLibId, String documentId) {
        return documentMapper.selectById(knowledgeLibId, documentId);
    }

    @Override
    public List<KnowledgeLibDocumentResponse> queryDocumentList(String knowledgeLibId) {
        return knowledgeLibDocumentConvert.toVO(documentMapper.selectListByKnowledgeLibId(knowledgeLibId));
    }

    @Override
    public void updateDocument(UpdateKnowledgeLibDocCommand command) {
        KnowledgeLibDocumentDO documentDO = new KnowledgeLibDocumentDO();
        documentDO.setDocumentName(command.getDocumentName());
        documentDO.setDocumentDesc(command.getDocumentDesc());
        documentDO.setDocumentId(command.getDocumentId());
        if (command.getFile() != null) {
            FileUploadDTO uploadDTO = fileUploadFactory.getUploadStrategy().upload(command.getFile(), "doc");
            documentDO.setPath(uploadDTO.getFilePath());
            documentDO.setDocumentSize(FileUtils.getFileSizeInMB(command.getFile()));
        }
        documentMapper.update(documentDO);
    }

    @Override
    public void updateDocumentStatus(String knowledgeLibId, String documentId, Integer status) {
        documentMapper.updateStatus(knowledgeLibId, documentId, status);
    }

    @Override
    public void deleteDocument(DeleteKnowledgeLibDocCommand command) {
        documentMapper.deleteById(command.getDocumentId());
        updateKnowledgeLibDocumentCount(command.getKnowledgeLibId());
    }

    @Override
    public void batchDeleteDocuments(String knowledgeLibId, List<String> documentIds) {
        documentMapper.batchDelete(knowledgeLibId, documentIds);
        updateKnowledgeLibDocumentCount(knowledgeLibId);
    }

    @Override
    public int queryDocumentCount(String knowledgeLibId) {
        return documentMapper.selectCountByKnowledgeLibId(knowledgeLibId);
    }

}
