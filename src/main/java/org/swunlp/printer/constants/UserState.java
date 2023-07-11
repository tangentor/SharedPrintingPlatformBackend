package org.swunlp.printer.constants;

public enum UserState {


    ACTIVE(1),
    BLOCKED(-1);

    private int value;

    UserState(int i) {
        this.value = i;
    }

    public int getValue(){
        return this.value;
    }

    public boolean equals(int i){
        return i == this.value;
    }

    public boolean equals(String i){
        return Integer.valueOf(i).equals(this.value);
    }
}
