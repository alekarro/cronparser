package com.aro.service.impl;

import com.aro.CronWithCommandFieldsEnum;
import com.aro.WrongCronException;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ExpandedCronWithCommandParserServiceImplTests {
    private ExpandedCronWithCommandParserServiceImpl service;


    @Before
    public void init() {
        service = new ExpandedCronWithCommandParserServiceImpl();
    }

    @Test
    public void testParseField_success () throws WrongCronException {
        assertEquals("17", service.parseField("17", CronWithCommandFieldsEnum.MINUTE));
        assertEquals("28 39 44", service.parseField("28,39,44", CronWithCommandFieldsEnum.MINUTE));
        assertEquals("17 18 19 20 21", service.parseField("17-21", CronWithCommandFieldsEnum.HOUR));
        assertEquals("7 18 29 40 51", service.parseField("7/11", CronWithCommandFieldsEnum.MINUTE));
        assertEquals("0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59",
                service.parseField("*", CronWithCommandFieldsEnum.MINUTE));
        assertEquals("1 3 5 7", service.parseField("1/2", CronWithCommandFieldsEnum.DAY_OF_WEEK));
        assertEquals("20 21 22 23", service.parseField("20-23", CronWithCommandFieldsEnum.DAY_OF_MONTH));
        assertEquals("7 8 9 10 11 12", service.parseField("7/1", CronWithCommandFieldsEnum.MONTH));
        assertEquals("/usr/bin/find", service.parseField("/usr/bin/find", CronWithCommandFieldsEnum.COMMAND));
    }

    @Test
    public void testParseField_exception ()  {
        WrongCronException exception = assertThrows(WrongCronException.class, () ->
            service.parseField("97", CronWithCommandFieldsEnum.MINUTE)
        );
        assertEquals("WrongCronException: cron is wrong =97; field =CronFieldsEnum{label='minute', startNumber=0, endNumber=59}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("1,24", CronWithCommandFieldsEnum.HOUR)
        );
        assertEquals("WrongCronException: cron is wrong =1,24; field =CronFieldsEnum{label='hour', startNumber=0, endNumber=23}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("0-6", CronWithCommandFieldsEnum.DAY_OF_WEEK)
        );
        assertEquals("WrongCronException: cron is wrong =0-6; field =CronFieldsEnum{label='day of week', startNumber=1, endNumber=7}", exception.getMessage());

        exception = assertThrows(WrongCronException.class, () ->
                service.parseField("13/1", CronWithCommandFieldsEnum.MONTH)
        );
        assertEquals("WrongCronException: cron is wrong =13/1; field =CronFieldsEnum{label='month', startNumber=1, endNumber=12}", exception.getMessage());

    }

    @Test
    public void testParse_success () throws WrongCronException {
        final Map<String,String> resultMap = service.parse("*/15 17 1,3,15 7-12 * /usr/bin/find");
        assertEquals("0 15 30 45", resultMap.get(CronWithCommandFieldsEnum.MINUTE.getLabel()));
        assertEquals("17", resultMap.get(CronWithCommandFieldsEnum.HOUR.getLabel()));
        assertEquals("1 3 15", resultMap.get(CronWithCommandFieldsEnum.DAY_OF_MONTH.getLabel()));
        assertEquals("7 8 9 10 11 12", resultMap.get(CronWithCommandFieldsEnum.MONTH.getLabel()));
        assertEquals("1 2 3 4 5 6 7", resultMap.get(CronWithCommandFieldsEnum.DAY_OF_WEEK.getLabel()));
        assertEquals("/usr/bin/find", resultMap.get(CronWithCommandFieldsEnum.COMMAND.getLabel()));
    }

    @Test
    public void testParse_exception () {
        WrongCronException exception = assertThrows(WrongCronException.class, () ->
                service.parse("*/15 17 1,3,15 7-13 * /usr/bin/find")
        );
        assertEquals("WrongCronException: cron is wrong =7-13; field =CronFieldsEnum{label='month', startNumber=1, endNumber=12}", exception.getMessage());
    }

}
