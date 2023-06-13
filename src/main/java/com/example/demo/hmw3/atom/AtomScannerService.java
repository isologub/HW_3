package com.example.demo.hmw3.atom;

import org.springframework.stereotype.Service;

@Service
public class AtomScannerService {

    public static final String ATOM_SCANNER_STARTED = "Atom scanner started!";
    public static final String ATOM_SCANNER_STARTED_CODE = "1";
    public static final String ATOM_SCANNER_STOPPED = "Atom scanner stopped!";
    public static final String ATOM_SCANNER_STOPPED_CODE = "0";

    public AtomBotStatus start() {
        AtomBotStatus atomBotStatus = AtomBotStatus.builder()
                .msg(ATOM_SCANNER_STARTED)
                .code(ATOM_SCANNER_STARTED_CODE)
                .build();

        System.out.println("log msg: " + atomBotStatus.toString());
        return atomBotStatus;
    }

    public AtomBotStatus stop() {
        AtomBotStatus atomBotStatus = AtomBotStatus.builder()
                .msg(ATOM_SCANNER_STOPPED)
                .code(ATOM_SCANNER_STOPPED_CODE)
                .build();

        System.out.println("log msg: " + atomBotStatus.toString());
        return atomBotStatus;
    }

}
