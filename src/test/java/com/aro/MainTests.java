package com.aro;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class MainTests {
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @Before
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;

        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testMain_success() {
        final String[] args = {"*/15 17 1,3,15 7-12 * /usr/bin/find"};
        Main.main(args);
        final String content = outContent.toString();
        assertTrue(content.contains("minute        0 15 30 45"));
        assertTrue(content.contains("hour          17"));
        assertTrue(content.contains("day of month  1 3 15"));
        assertTrue(content.contains("month         7 8 9 10 11 12"));
        assertTrue(content.contains("day of week   1 2 3 4 5 6 7"));
        assertTrue(content.contains("command       /usr/bin/find"));
    }

    @Test
    public void testMain_exception() {
        final String[] args = {"*/15 17 0,3,15 7-12 * /usr/bin/find"};
        Main.main(args);
        final String content = outContent.toString();
        assertTrue(content.contains("WrongCronException: cron is wrong, incorrect field value = 0,3,15; field = {label='day of month', minValue=1, maxValue=31}"));
    }

}
