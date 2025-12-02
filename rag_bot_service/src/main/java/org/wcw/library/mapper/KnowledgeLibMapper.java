package org.wcw.library.mapper;


import io.lettuce.core.dynamic.annotation.Param;
import org.mapstruct.Mapper;
import org.wcw.library.domain.entity.KnowledgeLibDO;

import java.util.List;

@Mapper
public interface KnowledgeLibMapper {

    /**
     * 创建知识库
     * @param knowledgeLibDO
     */
    void insert(KnowledgeLibDO knowledgeLibDO);

    /**
     * 根据id查询知识库
     * @param knowledgeLibId
     * @return
     */
    KnowledgeLibDO selectById(@Param("knowledgeLibId") String knowledgeLibId);

    /**
     * 根据用户id查询知识库
     * @param userId
     * @return
     */
    List<KnowledgeLibDO> selectByUserId(@Param("userId") String userId);

    /**
     * 查询所有知识库
     * @return
     */
    List<KnowledgeLibDO> selectAll();

    /**
     * 更新知识库
     * @param knowledgeLibDO
     */
    void update(KnowledgeLibDO knowledgeLibDO);

    /**
     * 更新知识库文档数量
     * @param knowledgeLibId
     * @param count
     */
    void updateDocumentCount(@Param("knowledgeLibId") String knowledgeLibId, @Param("count") Integer count);

    /**
     * 删除知识库
     * @param knowledgeLibId
     */
    void deleteById(@Param("knowledgeLibId") String knowledgeLibId);
}
