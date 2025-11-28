package org.wcw.chat.mapper;


import io.lettuce.core.dynamic.annotation.Param;
import org.mapstruct.Mapper;
import org.wcw.chat.domain.entity.ChatConversationDO;

import java.util.List;

@Mapper
public interface ChatConversationMapper {

    // 新建会话记忆
    int insert(@Param("record") ChatConversationDO record);

    // 根据memoryId查询会话记忆
    ChatConversationDO selectByMemoryId(@Param("memoryId") String memoryId);

    // 查询用户的所有会话记忆，按照更新时间倒序排列
    List<ChatConversationDO> selectByUserId(@Param("userId")Long userId);

    // 更新会话标题
    int updateTitle(@Param("memoryId") String memoryId, @Param("title") String title);

    // 更新会话状态（归档/删除）
    int updateStatus(@Param("memoryId") String memoryId, @Param("status") Integer status);

    // 删除会话（物理删除）
    int deleteByMemoryId(@Param("memoryId") String memoryId);

    // 检查会话是否属于指定用户
    boolean checkOwnership(@Param("memoryId") String memoryId, @Param("userId") Long userId);
}
