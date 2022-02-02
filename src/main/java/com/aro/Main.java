package com.aro;

import com.aro.service.CronParserService;
import com.aro.service.impl.CronWithCommandParserServiceImpl;

import java.util.Map;

public class Main {
    private final static int FIELD_COLUMN_SIZE = 14;

    private final CronParserService cronParserService = new CronWithCommandParserServiceImpl();

    public static void main(String[] args) {
        final Main main = new Main();
        main.expandAndOutputCron(args[0]);
    }

    private void expandAndOutputCron(final String cron) {
        try {
            final Map<String, String> resultMap = cronParserService.parse(cron);
            resultMap.forEach((key, value) -> System.out.println(padRight(key, FIELD_COLUMN_SIZE) + value));
        } catch (WrongCronException e) {
            System.out.println(e.getMessage());
        }
    }

    private String padRight(final String s, final int n) {
        return String.format("%-" + n + "s", s);
    }
}
