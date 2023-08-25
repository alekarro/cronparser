package org.aro.cron;


import java.util.LinkedHashMap;
import java.util.Map;

public interface CronParser {

    default Map<String, String> parseCron(final String cron) throws WrongCronException {
        final Map<String, String> resultMap = new LinkedHashMap<>();

        final String[] cronFields = cron.split("\\s");

        for (int i = 0; i < cronFields.length; i++) {
            resultMap.put(getLabel(i), parseField(cronFields[i], i));
        }
        return resultMap;
    }

    String parseField(final String cronField, final int fieldIndexInCron) throws WrongCronException;

    String getLabel(final int fieldIndexInCron) throws WrongCronException;

}
