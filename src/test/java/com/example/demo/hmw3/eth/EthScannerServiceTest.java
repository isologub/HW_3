package com.example.demo.hmw3.eth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EthScannerServiceTest {

    @InjectMocks
    private EthScannerService ethScannerService;

    @Test
    public void testStartCheckMessage() {
        EthBotStatus actual = ethScannerService.start();
        Assert.assertEquals(EthScannerService.ETH_SCANNER_STARTED, actual.getMsg());
    }

    @Test
    public void testStartCheckCode() {
        EthBotStatus actual = ethScannerService.start();
        Assert.assertEquals(EthScannerService.ETH_SCANNER_STARTED_CODE, actual.getCode());
    }

    @Test
    public void testStopCheckSMessage() {
        EthBotStatus actual = ethScannerService.stop();
        Assert.assertEquals(EthScannerService.ETH_SCANNER_STOPPED, actual.getMsg());
    }

    @Test
    public void testStopCheckCode() {
        EthBotStatus actual = ethScannerService.stop();
        Assert.assertEquals(EthScannerService.ETH_SCANNER_STOPPED_CODE, actual.getCode());
    }
}