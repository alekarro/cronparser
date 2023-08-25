package org.aro;

import org.aro.cron.CronParser;
import org.aro.cron.WrongCronException;
import org.aro.cron.commandcron.CommandCronParser;

public class Main {
    private final static int FIELD_NAME_COLUMN_SIZE = 14;

    private final CronParser cronParser = new CommandCronParser();

    public static void main(String[] args) {
        new Main().expandAndOutputCron(args[0]);
    }

    private void expandAndOutputCron(final String cron) {
        try {
            cronParser.parseCron(cron).forEach((key, value) -> System.out.println(padRight(key, FIELD_NAME_COLUMN_SIZE) + value));
        } catch (WrongCronException e) {
            System.out.println(e.getMessage());
        }
    }

    private String padRight(final String s, final int n) {
        return String.format("%-" + n + "s", s);
    }
}
