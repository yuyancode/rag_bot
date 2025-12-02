package org.wcw.library.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wcw.common.Result;
import org.wcw.library.domain.vo.request.*;
import org.wcw.library.service.IKnowledgeLibDocumentService;
import org.wcw.library.service.IKnowledgeLibService;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {
    private final IKnowledgeLibService knowledgeLibService;
    private final IKnowledgeLibDocumentService knowledgeLibDocumentService;


    @PostMapping("/createKnowledgeLib")
    public Result<Void> createKnowledgeLib(@RequestBody CreateKnowledgeLibCommand command) {
        knowledgeLibService.createKnowledgeLib(command);
        return Result.success();
    }

    @PostMapping("/updateKnowledgeLib")
    public Result<Void> updateKnowledgeLib(@RequestBody UpdateKnowledgeLibCommand command) {
        knowledgeLibService.updateKnowledgeLib(command);
        return Result.success(null);
    }

    @PostMapping("/createKnowledgeLibDocument")
    public Result<Void> createKnowledgeLibDocument(@RequestBody CreateKnowledgeLibDocCommand command) {
        knowledgeLibDocumentService.addDocument(command);
        return Result.success(null);
    }

    @PostMapping("/deleteKnowledgeLibDocument")
    public Result<Void> deleteKnowledgeLibDocument(@RequestBody DeleteKnowledgeLibDocCommand command) {
        knowledgeLibDocumentService.deleteDocument(command);
        return Result.success(null);
    }

    @PostMapping("/deleteKnowledgeLib")
    public Result<Void> deleteKnowledgeLib(@RequestBody DeleteKnowledgeLibCommand command) {
        knowledgeLibService.deleteKnowledgeLib(command);
        return Result.success(null);
    }
}
