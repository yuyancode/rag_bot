package org.wcw.library.mapper;


import io.lettuce.core.dynamic.annotation.Param;
import org.mapstruct.Mapper;
import org.wcw.library.domain.entity.KnowledgeLibDO;
import org.wcw.library.domain.entity.KnowledgeLibDocumentDO;

import java.util.List;

@Mapper
public interface KnowledgeLibDocumentMapper {
    /**
     * 插入知识库文档
     * @param documentDO
     */
    void insert(KnowledgeLibDocumentDO documentDO);

    /**
     * 批量插入知识库文档
     * @param documents
     */
    void batchInsert(List<KnowledgeLibDocumentDO> documents);

    /**
     * 根据知识库id和文档id查询知识库文档
     * @param knowledgeLibId
     * @param documentId
     * @return
     */
    KnowledgeLibDocumentDO selectById(@Param("knowledgeLibId") String knowledgeLibId, @Param("documentId") String documentId);

    /**
     * 根据知识库id查询知识库文档
     * @param knowledgeLibId
     * @return
     */
    List<KnowledgeLibDocumentDO> selectListByKnowledgeLibId(@Param("knowledgeLibId") String knowledgeLibId);

    /**
     * 更新知识库文档
     * @param document
     */
    void update(KnowledgeLibDocumentDO document);

    /**
     * 更新知识库文档状态
     */
    void updateStatus(@Param("knowledgeLibId") String knowledgeLibId, @Param("documentId") String documentId, @Param("status") Integer status);

    /**
     * 删除知识库文档
     * @param documentId
     */
    void deleteById(@Param("documentId") String documentId);

    /**
     * 批量删除知识库文档
     * @param documentIds
     */
    void batchDelete(@Param("knowledgeLibId") String knowledgeLibId, @Param("documentIds") List<String> documentIds);

    /**
     * 根据知识库id删除知识库文档
     * @param knowledgeLibId
     */
    void deleteByKnowledgeLibId(@Param("knowledgeLibId") String knowledgeLibId);

    /**
     * 根据知识库id查询知识库文档数量
     * @param knowledgeLibId
     * @return
     */
    int selectCountByKnowledgeLibId(@Param("knowledgeLibId") String knowledgeLibId);

}
