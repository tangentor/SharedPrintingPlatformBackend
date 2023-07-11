package org.swunlp.printer.constants;

public enum RecordState {

    PRINT(0),
    DOWNLOAD(1);

    private final int value;

    RecordState(int i) {
        this.value = i;
    }

    public int value(){
        return this.value;
    }


}
