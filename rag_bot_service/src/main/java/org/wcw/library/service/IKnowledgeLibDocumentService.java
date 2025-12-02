package org.wcw.library.service;


import org.wcw.library.domain.entity.KnowledgeLibDocumentDO;
import org.wcw.library.domain.vo.request.CreateKnowledgeLibDocCommand;
import org.wcw.library.domain.vo.request.DeleteKnowledgeLibDocCommand;
import org.wcw.library.domain.vo.request.UpdateKnowledgeLibDocCommand;
import org.wcw.library.domain.vo.response.KnowledgeLibDocumentResponse;

import java.util.List;

public interface IKnowledgeLibDocumentService {
    /**
     * 添加文档
     * @param command
     */
    void addDocument(CreateKnowledgeLibDocCommand command);

    /**
     * 批量添加文档
     * @param
     */
    void batchAddDocument(List<KnowledgeLibDocumentDO> documents);

    /**
     * 根据知识库ID和文档ID查询文档
     * @param knowledgeLibId
     * @param documentId
     * @return
     */
    KnowledgeLibDocumentDO queryDocument(String knowledgeLibId, String documentId);


    /**
     * 根据知识库ID查询文档列表
     * @param knowledgeLibId
     * @return
     */
    List<KnowledgeLibDocumentResponse> queryDocumentList(String knowledgeLibId);

    /**
     * 更新文档信息
     * @param command
     */
    void updateDocument(UpdateKnowledgeLibDocCommand  command);

    /**
     * 更新文档状态
     * @param knowledgeLibId
     * @param documentId
     * @param status
     */
    void updateDocumentStatus(String knowledgeLibId, String documentId, Integer status);

    /**
     * 删除文档
     * @param command
     */
    void deleteDocument(DeleteKnowledgeLibDocCommand command);

    /**
     * 批量删除文档
     * @param knowledgeLibId
     * @param documentIds
     */
    void batchDeleteDocuments(String knowledgeLibId, List<String> documentIds);

    /**
     * 查询文档数量
     * @param knowledgeLibId
     * @return
     */
    int queryDocumentCount(String knowledgeLibId);



}
