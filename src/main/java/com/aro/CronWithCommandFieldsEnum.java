package com.aro;

public enum CronWithCommandFieldsEnum {
    MINUTE("minute", 0, 59),
    HOUR("hour", 0, 23),
    DAY_OF_MONTH("day of month", 1, 31),
    MONTH("month", 1, 12),
    DAY_OF_WEEK("day of week", 1, 7),
    COMMAND("command");

    CronWithCommandFieldsEnum(String label) {
        this.label = label;
    }

    CronWithCommandFieldsEnum(String label, int startNumber, int endNumber) {
        this.label = label;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
    }

    private String label;
    private int startNumber = -1;
    private int endNumber = -1;

    public String getLabel() {
        return label;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public int getEndNumber() {
        return endNumber;
    }

    @Override
    public String toString() {
        return "CronFieldsEnum{" +
                "label='" + label + '\'' +
                ", startNumber=" + startNumber +
                ", endNumber=" + endNumber +
                '}';
    }
}
