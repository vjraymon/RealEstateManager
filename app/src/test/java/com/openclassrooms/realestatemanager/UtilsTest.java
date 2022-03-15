package com.openclassrooms.realestatemanager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class) // prepares byte-code of the LegacyClass
public class UtilsTest {

    private final Date fakeNow = Date.from(Instant.parse("2010-12-03T10:15:30.00Z"));

    @Test
    public void convertDollarToEuro() {
        int quantity = Utils.convertDollarToEuro(0);
        assertEquals(0, quantity);
        quantity = Utils.convertDollarToEuro(1000);
        assertEquals(812, quantity);
    }

    @Test
    public void convertEuroToDollar() {
        int quantity = Utils.convertEuroToDollar(0);
        assertEquals(0, quantity);
        quantity = Utils.convertEuroToDollar(812);
        assertEquals(1000, quantity);
    }

    @Test
    public void getTodayDate() throws Exception {
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(fakeNow);
        String date = Utils.getTodayDate();

        String[] tokens = date.split("/");
        assertEquals(3,tokens.length);
        assertEquals(3,Integer.parseInt(tokens[0]));
        assertEquals(12,Integer.parseInt(tokens[1]));
        assertEquals(2010,Integer.parseInt(tokens[2]));
    }
}