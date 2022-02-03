package com.aro.cron.commandcron;

public enum CommandCronEnum {
    MINUTE("minute", 0, 59),
    HOUR("hour", 0, 23),
    DAY_OF_MONTH("day of month", 1, 31),
    MONTH("month", 1, 12),
    DAY_OF_WEEK("day of week", 1, 7),
    COMMAND("command");

    CommandCronEnum(String label) {
        this.label = label;
    }

    CommandCronEnum(String label, int minValue, int maxValue) {
        this.label = label;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    private String label;
    private int minValue;
    private int maxValue;

    public String getLabel() {
        return label;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public String toString() {
        return "{" +
            "label='" + label + '\'' +
            ", minValue=" + minValue +
            ", maxValue=" + maxValue +
            '}';
    }
}
