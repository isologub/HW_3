package com.example.demo.hmw3.atom;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AtomScannerServiceTest {

    @InjectMocks
    private AtomScannerService atomScannerService;

    @Test
    public void testStartCheckMessage() {
        AtomBotStatus actual = atomScannerService.start();
        Assert.assertEquals(AtomScannerService.ATOM_SCANNER_STARTED, actual.getMsg());
    }

    @Test
    public void testStartCheckCode() {
        AtomBotStatus actual = atomScannerService.start();
        Assert.assertEquals(AtomScannerService.ATOM_SCANNER_STARTED_CODE, actual.getCode());
    }

    @Test
    public void testStopCheckSMessage() {
        AtomBotStatus actual = atomScannerService.stop();
        Assert.assertEquals(AtomScannerService.ATOM_SCANNER_STOPPED, actual.getMsg());
    }

    @Test
    public void testStopCheckCode() {
        AtomBotStatus actual = atomScannerService.stop();
        Assert.assertEquals(AtomScannerService.ATOM_SCANNER_STOPPED_CODE, actual.getCode());
    }
}