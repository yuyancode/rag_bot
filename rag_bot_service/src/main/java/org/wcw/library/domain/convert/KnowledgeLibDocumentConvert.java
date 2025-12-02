package org.wcw.library.domain.convert;

import org.mapstruct.Mapper;
import org.wcw.library.domain.entity.KnowledgeLibDocumentDO;
import org.wcw.library.domain.vo.response.KnowledgeLibDocumentResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KnowledgeLibDocumentConvert {
    List<KnowledgeLibDocumentResponse> toVO(List<KnowledgeLibDocumentDO> list);
}
