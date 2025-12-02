package org.wcw.library.domain.convert;


import org.mapstruct.Mapper;
import org.wcw.library.domain.entity.KnowledgeLibDO;
import org.wcw.library.domain.vo.response.KnowledgeLibResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KnowledgeLibConvert {
    List<KnowledgeLibResponse> toVOList(List<KnowledgeLibDO> list);
}
