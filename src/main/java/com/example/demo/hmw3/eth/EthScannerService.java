package com.example.demo.hmw3.eth;

import com.example.demo.hmw3.atom.AtomBotStatus;
import org.springframework.stereotype.Service;

@Service
public class EthScannerService {

    public void start() {
        EthBotStatus build = EthBotStatus.builder()
                .msg("ETH scanner started!")
                .code("1")
                .build();

        System.out.println("log msg: " + build.toString());
    }

    public void stop() {
        EthBotStatus build = EthBotStatus.builder()
                .msg("ETH scanner stopped!")
                .code("0")
                .build();

        System.out.println("log msg: " + build.toString());
    }

}
