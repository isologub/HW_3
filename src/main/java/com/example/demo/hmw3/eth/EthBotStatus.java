package com.example.demo.hmw3.eth;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class EthBotStatus {
    private String msg;
    private String code;
}
