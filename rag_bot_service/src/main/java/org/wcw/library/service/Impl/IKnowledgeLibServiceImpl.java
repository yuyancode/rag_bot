package org.wcw.library.service.Impl;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wcw.library.domain.convert.KnowledgeLibConvert;
import org.wcw.library.domain.entity.KnowledgeLibDO;
import org.wcw.library.domain.vo.request.*;
import org.wcw.library.domain.vo.response.KnowledgeLibNameResponse;
import org.wcw.library.domain.vo.response.KnowledgeLibResponse;
import org.wcw.library.mapper.KnowledgeLibDocumentMapper;
import org.wcw.library.mapper.KnowledgeLibMapper;
import org.wcw.library.service.IKnowledgeLibService;
import org.wcw.utils.IdGeneratorUtil;

import java.util.List;

import static dev.langchain4j.data.document.Metadata.metadata;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Service
@RequiredArgsConstructor
public class IKnowledgeLibServiceImpl implements IKnowledgeLibService {
    private final KnowledgeLibDocumentMapper documentMapper;
    private final KnowledgeLibMapper knowledgeLibMapper;
    private final KnowledgeLibConvert knowledgeLibConvert;
    final EmbeddingStore<TextSegment> embeddingStore;

    @Override
    public void createKnowledgeLib(CreateKnowledgeLibCommand command) {
        KnowledgeLibDO knowledgeLibDO = new KnowledgeLibDO();
        knowledgeLibDO.setUserId(command.getUserId());
        knowledgeLibDO.setKnowledgeLibId(IdGeneratorUtil.generateLibId());
        knowledgeLibDO.setKnowledgeLibName(command.getKnowledgeLibName());
        knowledgeLibDO.setKnowledgeLibDesc(command.getKnowledgeLibDesc());
        knowledgeLibDO.setDocumentCount(0);
        knowledgeLibMapper.insert(knowledgeLibDO);
    }

    @Override
    public KnowledgeLibDO getKnowledgeLib(String knowledgeLibId) {
        return knowledgeLibMapper.selectById(knowledgeLibId);
    }

    @Override
    public List<KnowledgeLibResponse> queryLibraryDetailList(QueryLibraryDetailListRequest request) {
        List<KnowledgeLibDO> knowledgeLibDOS = knowledgeLibMapper.selectByUserId(request.getUserId());
        if(knowledgeLibDOS.isEmpty()) return null;
        return knowledgeLibConvert.toVOList(knowledgeLibDOS);
    }

    @Override
    public void updateKnowledgeLib(UpdateKnowledgeLibCommand command) {
        KnowledgeLibDO knowledgeLibDO = new KnowledgeLibDO();
        knowledgeLibDO.setKnowledgeLibId(command.getKnowledgeLibId());
        knowledgeLibDO.setKnowledgeLibName(command.getKnowledgeLibName());
        knowledgeLibDO.setKnowledgeLibDesc(command.getKnowledgeLibDesc());
        knowledgeLibMapper.update(knowledgeLibDO);
    }

    @Override
    public void updateDocumentCount(String knowledgeLibId, Integer count) {
        knowledgeLibMapper.updateDocumentCount(knowledgeLibId, count);
    }

    @Override
    public void deleteKnowledgeLib(DeleteKnowledgeLibCommand command) {
        // 1.删除知识库关联的文档
        documentMapper.deleteByKnowledgeLibId(command.getKnowledgeLibId());

        // 2.删除知识库
        knowledgeLibMapper.deleteById(command.getKnowledgeLibId());

        // 3.删除向量数据库中的数据 - 根据元数据knowledgeLibId来删
        embeddingStore.removeAll(metadataKey("knowledgeLibId").isEqualTo(command.getKnowledgeLibId()));
    }

    @Override
    public List<KnowledgeLibNameResponse> queryKnowledgeLibList(QueryLibraryListRequest request) {
        List<KnowledgeLibDO> list = knowledgeLibMapper.selectByUserId(request.getUserId());
        return list.stream()
                .map(item -> KnowledgeLibNameResponse.builder()
                        .knowledgeLibName(item.getKnowledgeLibName())
                        .knowledgeLibId(item.getKnowledgeLibId())
                        .build())
                .toList();
    }
}
