package com.aro.service;



import com.aro.CronWithCommandFieldsEnum;
import com.aro.WrongCronException;

import java.util.LinkedHashMap;
import java.util.Map;

public interface CronParserService {

    default Map<String, String> parse(final String cron)  throws WrongCronException {
        final Map<String, String> resultMap = new LinkedHashMap<>();

        final String[] cronArray = cron.split("\\s");

        for(int i = 0; i < CronWithCommandFieldsEnum.values().length; i++) {
            final CronWithCommandFieldsEnum fieldEnum = CronWithCommandFieldsEnum.values()[i];
            resultMap.put(fieldEnum.getLabel(), parseField(cronArray[i], fieldEnum));
        }
        return resultMap;
    }

    String parseField(final String cronField, CronWithCommandFieldsEnum fieldEnum) throws WrongCronException;
}
