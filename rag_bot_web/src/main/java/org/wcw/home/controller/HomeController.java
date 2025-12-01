package org.wcw.home.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wcw.coze.service.ICozeService;
import org.wcw.utils.EmailUtil;

@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final ICozeService iCozeService;
    private final EmailUtil emailUtil;
}
