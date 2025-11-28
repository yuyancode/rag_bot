package org.wcw.chat.mapper;


import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.wcw.chat.domain.entity.ChatMessageDO;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    // 插入单条聊天记录
    void insert(@Param("record") ChatMessageDO chatMessageDO);

    // 批量插入聊天记录
    int batchInsert(@Param("records") List<ChatMessageDO> records);

    // 根据memoryId查询所有聊天记录, 按时间正序排列
    List<ChatMessageDO> selectByMemoryId(@Param("memoryId") String memoryId);

    // 根据memoryId查询最近的N条聊天记录
    List<ChatMessageDO> selectRecentByMemoryId(@Param("memoryId") String memoryId, @Param("limit") int limit);

    // 删除指定memoryId的聊天记录
    int deleteByMemoryId(@Param("memoryId") String memoryId);

    // 统计指定memoryId的聊天总token数
    int sumTokensByMemoryId(@Param("memoryId") String memoryId);

    /**
     * 根据会话ID和角色查询消息
     * @param memoryId 会话ID
     * @param role 角色（user/assistant）
     * @return 消息列表
     */
    @Select("SELECT * FROM chat_message WHERE memory_id = #{memoryId} AND role = #{role}")
    List<ChatMessageDO> selectByMemoryIdAndRole(@Param("memoryId") String memoryId, @Param("role") String role);
}
