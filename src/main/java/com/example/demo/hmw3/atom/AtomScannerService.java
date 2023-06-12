package com.example.demo.hmw3.atom;

import org.springframework.stereotype.Service;

@Service
public class AtomScannerService {

    public void start() {
        AtomBotStatus build = AtomBotStatus.builder()
                .msg("Atom scanner started!")
                .code("1")
                .build();

        System.out.println("log msg: " + build.toString());
    }

    public void stop() {
        AtomBotStatus build = AtomBotStatus.builder()
                .msg("Atom scanner stopped!")
                .code("0")
                .build();

        System.out.println("log msg: " + build.toString());
    }

}
