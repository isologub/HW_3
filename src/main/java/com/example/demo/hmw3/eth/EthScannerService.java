package com.example.demo.hmw3.eth;

import org.springframework.stereotype.Service;

@Service
public class EthScannerService {

    public static final String ETH_SCANNER_STARTED = "ETH scanner started!";
    public static final String ETH_SCANNER_STARTED_CODE = "1";
    public static final String ETH_SCANNER_STOPPED = "ETH scanner stopped!";
    public static final String ETH_SCANNER_STOPPED_CODE = "0";

    public EthBotStatus start() {
        EthBotStatus ethBotStatus = EthBotStatus.builder()
                .msg(ETH_SCANNER_STARTED)
                .code(ETH_SCANNER_STARTED_CODE)
                .build();

        System.out.println("log msg: " + ethBotStatus.toString());

        return ethBotStatus;
    }

    public EthBotStatus stop() {
        EthBotStatus ethBotStatus = EthBotStatus.builder()
                .msg(ETH_SCANNER_STOPPED)
                .code(ETH_SCANNER_STOPPED_CODE)
                .build();

        System.out.println("log msg: " + ethBotStatus.toString());
        return ethBotStatus;
    }

}
