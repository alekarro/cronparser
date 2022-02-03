package com.aro.cron.commandcron;

import com.aro.cron.WrongCronException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CommandCronParserTests {
    private CommandCronParser service;


    @Before
    public void init() {
        service = new CommandCronParser();
    }

    @Test
    public void testParseField_success() throws WrongCronException {
        assertEquals("17", service.parseField("17", CommandCronEnum.MINUTE.ordinal()));
        assertEquals("28 39 44", service.parseField("28,39,44", CommandCronEnum.MINUTE.ordinal()));
        assertEquals("17 18 19 20 21", service.parseField("17-21", CommandCronEnum.HOUR.ordinal()));
        assertEquals("7 18 29 40 51", service.parseField("7/11", CommandCronEnum.MINUTE.ordinal()));
        assertEquals("9 16 23 30 37 44 51 58", service.parseField("9/7", CommandCronEnum.MINUTE.ordinal()));
        assertEquals("0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59",
                service.parseField("*", CommandCronEnum.MINUTE.ordinal()));
        assertEquals("1 3 5 7", service.parseField("1/2", CommandCronEnum.DAY_OF_WEEK.ordinal()));
        assertEquals("20 21 22 23", service.parseField("20-23", CommandCronEnum.DAY_OF_MONTH.ordinal()));
        assertEquals("7 8 9 10 11 12", service.parseField("7/1", CommandCronEnum.MONTH.ordinal()));
        assertEquals("2 7 12", service.parseField("2/5", CommandCronEnum.MONTH.ordinal()));
        assertEquals("1 6 11", service.parseField("*/5", CommandCronEnum.MONTH.ordinal()));
        assertEquals("/usr/bin/find", service.parseField("/usr/bin/find", CommandCronEnum.COMMAND.ordinal()));
    }

    @Test
    public void testParseField_exception() {
        WrongCronException exception = assertThrows(WrongCronException.class, () ->
                service.parseField("97", CommandCronEnum.MINUTE.ordinal())
        );
        assertEquals("WrongCronException: cron is wrong, incorrect field value = 97; field = {label='minute', minValue=0, maxValue=59}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("1,24", CommandCronEnum.HOUR.ordinal())
        );
        assertEquals("WrongCronException: cron is wrong, incorrect field value = 1,24; field = {label='hour', minValue=0, maxValue=23}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("0-6", CommandCronEnum.DAY_OF_WEEK.ordinal())
        );
        assertEquals("WrongCronException: cron is wrong, incorrect field value = 0-6; field = {label='day of week', minValue=1, maxValue=7}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("13/1", CommandCronEnum.MONTH.ordinal())
        );
        assertEquals("WrongCronException: cron is wrong, incorrect field value = 13/1; field = {label='month', minValue=1, maxValue=12}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("28,,44", CommandCronEnum.MINUTE.ordinal())
        );
        assertEquals("WrongCronException: cron is wrong, incorrect field value = 28,,44; field = {label='minute', minValue=0, maxValue=59}", exception.getMessage());
    }

    @Test
    public void testParse_success() throws WrongCronException {
        final Map<String, String> resultMap = service.parseCron("*/15 17 1,3,15 7-12 * /usr/bin/find");
        assertEquals("0 15 30 45", resultMap.get(CommandCronEnum.MINUTE.getLabel()));
        assertEquals("17", resultMap.get(CommandCronEnum.HOUR.getLabel()));
        assertEquals("1 3 15", resultMap.get(CommandCronEnum.DAY_OF_MONTH.getLabel()));
        assertEquals("7 8 9 10 11 12", resultMap.get(CommandCronEnum.MONTH.getLabel()));
        assertEquals("1 2 3 4 5 6 7", resultMap.get(CommandCronEnum.DAY_OF_WEEK.getLabel()));
        assertEquals("/usr/bin/find", resultMap.get(CommandCronEnum.COMMAND.getLabel()));
    }

    @Test
    public void testParse_exception() {
        WrongCronException exception = assertThrows(WrongCronException.class, () ->
                service.parseCron("*/15 17 1,3,15 7-13 * /usr/bin/find")
        );
        assertEquals("WrongCronException: cron is wrong, incorrect field value = 7-13; field = {label='month', minValue=1, maxValue=12}", exception.getMessage());
    }

    @Test
    public void testGetLabel_success() throws WrongCronException {
        assertEquals(CommandCronEnum.MINUTE.getLabel(), service.getLabel(CommandCronEnum.MINUTE.ordinal()));
        assertEquals(CommandCronEnum.HOUR.getLabel(), service.getLabel(CommandCronEnum.HOUR.ordinal()));
        assertEquals(CommandCronEnum.DAY_OF_MONTH.getLabel(), service.getLabel(CommandCronEnum.DAY_OF_MONTH.ordinal()));
        assertEquals(CommandCronEnum.MONTH.getLabel(), service.getLabel(CommandCronEnum.MONTH.ordinal()));
        assertEquals(CommandCronEnum.DAY_OF_WEEK.getLabel(), service.getLabel(CommandCronEnum.DAY_OF_WEEK.ordinal()));
        assertEquals(CommandCronEnum.COMMAND.getLabel(), service.getLabel(CommandCronEnum.COMMAND.ordinal()));
    }

    @Test
    public void testGetLabel_exception() {
        WrongCronException exception = assertThrows(WrongCronException.class, () ->
                service.getLabel(6)
        );
        assertEquals("WrongCronException: cron is wrong, field index in cron exceeds maximum possible value, field index =6", exception.getMessage());
    }

}
