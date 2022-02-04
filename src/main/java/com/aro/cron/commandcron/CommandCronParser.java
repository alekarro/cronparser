package com.aro.cron.commandcron;

import com.aro.cron.CronParser;
import com.aro.cron.WrongCronException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class CommandCronParser implements CronParser {
    private final static Pattern CRON_PATTERN = Pattern.compile("(\\d+,[\\d,]*)*(\\d+-\\d+)*([\\d*]+/\\d+)*([*]+)*(\\d+)*");

    private final static String SPACE = " ";
    private final static String COMMA = ",";
    private final static String DASH = "-";
    private final static String SLASH = "/";
    private final static String ASTERISK = "*";

    @Override
    public String getLabel(int fieldIndexInCron) throws WrongCronException {
        try {
            return CommandCronEnum.values()[fieldIndexInCron].getLabel();
        } catch (final IndexOutOfBoundsException e) {
            throw new WrongCronException(createIndexOutOfBoundsExceptionMessage(fieldIndexInCron));
        }
    }

    public String parseField(final String cronField, final int fieldIndexInCron) throws WrongCronException {
        final CommandCronEnum fieldEnum;
        try {
            fieldEnum = CommandCronEnum.values()[fieldIndexInCron];
        } catch (final IndexOutOfBoundsException e) {
            throw new WrongCronException(createIndexOutOfBoundsExceptionMessage(fieldIndexInCron));
        }

        if (fieldEnum == CommandCronEnum.COMMAND) {
            return cronField;
        }

        final Matcher matcher = CRON_PATTERN.matcher(cronField);

        if (matcher.find()) {
            try {
                //numbers separated by commas, like 1,2,3,4
                String group = matcher.group(1);
                if (group != null) {
                    return expandComma(cronField, fieldEnum, group);
                }

                //numbers separated by dash, like 1-15
                group = matcher.group(2);
                if (group != null) {
                    return expandDash(cronField, fieldEnum, group);
                }

                //slash, like 0/15 or */15
                group = matcher.group(3);
                if (group != null) {
                    return expandSlash(cronField, fieldEnum, group);
                }

                //asterisk, just *
                if (matcher.group(4) != null) {
                    return String.join(SPACE,
                            () -> IntStream.rangeClosed(fieldEnum.getMinValue(), fieldEnum.getMaxValue()).
                                    mapToObj(x -> (CharSequence) String.valueOf(x)).iterator());
                }

                //single value, just value like 7
                if (matcher.group(5) != null) {
                    final int val = Integer.parseInt(cronField);
                    validateValue(val, fieldEnum, cronField);
                    return cronField;
                }

            } catch (RuntimeException e) {
                throw new WrongCronException(createExceptionMessage(cronField, fieldEnum));
            }
        }
        throw new WrongCronException(createExceptionMessage(cronField, fieldEnum));
    }

    private String expandSlash(String cronField, CommandCronEnum fieldEnum, String group) throws WrongCronException {
        final String[] valuesArr = group.split(SLASH);
        final int start;
        if (ASTERISK.equals(valuesArr[0])) {
            start = fieldEnum.getMinValue();
        } else {
            start = Integer.parseInt(valuesArr[0]);
        }

        validateValue(start, fieldEnum, cronField);

        final int step = Integer.parseInt(valuesArr[1]);
        return String.join(SPACE,
                () -> IntStream.rangeClosed(start, fieldEnum.getMaxValue()).
                        filter(n -> (n - start) % step == 0).
                        mapToObj(x -> (CharSequence) String.valueOf(x)).iterator());
    }

    private String expandDash(String cronField, CommandCronEnum fieldEnum, String group) throws WrongCronException {
        final String[] valuesArr = group.split(DASH);
        final int start = Integer.parseInt(valuesArr[0]);
        final int end = Integer.parseInt(valuesArr[1]);

        validateValue(start, fieldEnum, cronField);
        validateValue(end, fieldEnum, cronField);

        return String.join(SPACE, () -> IntStream.rangeClosed(start, end).
                mapToObj(x -> (CharSequence) String.valueOf(x)).iterator());
    }

    private String expandComma(String cronField, CommandCronEnum fieldEnum, String group) throws WrongCronException {
        final String result = group.replace(COMMA, SPACE);
        final String[] valuesArr = result.split("\\s");
        for (String s : valuesArr) {
            int val;
            try {
                val = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                throw new WrongCronException(createExceptionMessage(cronField, fieldEnum));
            }
            validateValue(val, fieldEnum, cronField);
        }
        return result;
    }

    private String createExceptionMessage(String cronField, CommandCronEnum fieldEnum) {
        return "WrongCronException: cron is wrong, incorrect field value = " + cronField + "; field = " + fieldEnum.toString();
    }

    private String createIndexOutOfBoundsExceptionMessage(final int fieldIndexInCron) {
        return "WrongCronException: cron is wrong, field index in cron exceeds maximum possible value, " +
                "field index = " + fieldIndexInCron + "; max possible index = " + (CommandCronEnum.values().length - 1);
    }

    private void validateValue(final int val, final CommandCronEnum fieldEnum, final String cronField) throws WrongCronException {
        if (val < fieldEnum.getMinValue() || val > fieldEnum.getMaxValue()) {
            throw new WrongCronException(createExceptionMessage(cronField, fieldEnum));
        }
    }
}
