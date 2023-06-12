package com.example.demo.hmw3;

import com.example.demo.hmw3.atom.AtomScannerService;
import com.example.demo.hmw3.eth.EthScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScannerControlService {

    @Autowired
    private EthScannerService ethScannerService;

    @Autowired
    private AtomScannerService atomScannerService;

    public void startAll(){
        ethScannerService.start();
        atomScannerService.start();
    }

    public void stopAll(){
        ethScannerService.stop();
        atomScannerService.stop();
    }

}
