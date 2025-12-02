package org.wcw.library.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wcw.common.Result;
import org.wcw.library.domain.vo.request.QueryDocumentLibRequest;
import org.wcw.library.domain.vo.request.QueryLibraryDetailListRequest;
import org.wcw.library.domain.vo.request.QueryLibraryListRequest;
import org.wcw.library.domain.vo.response.KnowledgeLibDocumentResponse;
import org.wcw.library.domain.vo.response.KnowledgeLibNameResponse;
import org.wcw.library.domain.vo.response.KnowledgeLibResponse;
import org.wcw.library.service.IKnowledgeLibDocumentService;
import org.wcw.library.service.IKnowledgeLibService;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryQueryController {

    private final IKnowledgeLibService knowledgeLibService;
    private final IKnowledgeLibDocumentService knowledgeLibDocumentService;

    @GetMapping("/queryLibraryDetailList")
    public Result<List<KnowledgeLibResponse>> queryLibraryList(QueryLibraryDetailListRequest request) {
        return Result.success(knowledgeLibService.queryLibraryDetailList(request));
    }

    @GetMapping("/queryLibraryList")
    public Result<List<KnowledgeLibNameResponse>> queryLibraryList(QueryLibraryListRequest request) {
        return Result.success(knowledgeLibService.queryKnowledgeLibList(request));
    }

    @GetMapping("/queryLibraryDocumentList")
    public Result<List<KnowledgeLibDocumentResponse>> queryLibraryDocumentList(QueryDocumentLibRequest request) {
        return Result.success(knowledgeLibDocumentService.queryDocumentList(request.getKnowledgeLibId()));
    }
}
