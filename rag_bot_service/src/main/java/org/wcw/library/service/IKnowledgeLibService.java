package org.wcw.library.service;

import org.wcw.library.domain.entity.KnowledgeLibDO;
import org.wcw.library.domain.vo.request.*;
import org.wcw.library.domain.vo.response.KnowledgeLibNameResponse;
import org.wcw.library.domain.vo.response.KnowledgeLibResponse;

import java.util.List;

public interface IKnowledgeLibService {

    /**
     * 创建知识库
     * @param command
     */
    void createKnowledgeLib(CreateKnowledgeLibCommand command);

    /**
     * 获取知识库信息
     * @param knowledgeLibId
     * @return
     */
    KnowledgeLibDO getKnowledgeLib(String knowledgeLibId);

    /**
     * 获取知识库列表
     * @param request
     * @return
     */
    List<KnowledgeLibResponse> queryLibraryDetailList(QueryLibraryDetailListRequest request);

    /**
     * 更新知识库信息
     * @param command
     */
    void updateKnowledgeLib(UpdateKnowledgeLibCommand command);

    /**
     * 更新文档数量
     * @param knowledgeLibId
     * @param count
     */
    void updateDocumentCount(String knowledgeLibId, Integer count);

    /**
     * 删除知识库
     * @param command
     */
    void deleteKnowledgeLib(DeleteKnowledgeLibCommand command);

    /**
     * 获取知识库列表名字
     * @param request
     * @return
     */
    List<KnowledgeLibNameResponse> queryKnowledgeLibList(QueryLibraryListRequest request);
}
