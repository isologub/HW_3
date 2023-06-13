package com.example.demo.hmw3.atom;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class AtomBotStatus {
    private String msg;
    private String code;
}
