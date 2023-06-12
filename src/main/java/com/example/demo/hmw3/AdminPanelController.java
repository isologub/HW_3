package com.example.demo.hmw3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bots/controll")
public class AdminPanelController {

    @Autowired
    private ScannerControlService scannerControlService;

    @GetMapping("/start")
    public String startBots(){
        scannerControlService.startAll();
        return "Bots started!";
    }

    @GetMapping("/stop")
    public String stopBots(){
        scannerControlService.stopAll();
        return "Bots stopped!";
    }
}
