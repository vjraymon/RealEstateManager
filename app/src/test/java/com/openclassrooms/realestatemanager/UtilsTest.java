package com.openclassrooms.realestatemanager;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {

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
    public void getTodayDate() {
        String date = Utils.getTodayDate();
// TODO Enhance the test by mocking DATE
        String[] tokens = date.split("/");
        assertEquals(3,tokens.length);
        assertEquals(4,tokens[2].length());
        assertTrue((Integer.parseInt(tokens[1])>=1));
        assertTrue((Integer.parseInt(tokens[1])<=12));
        assertTrue((Integer.parseInt(tokens[0])>=1));
        assertTrue((Integer.parseInt(tokens[0])<=31));
    }
}