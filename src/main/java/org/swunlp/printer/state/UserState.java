package org.swunlp.printer.state;

public enum UserState {


    NORMAL(1),
    BAN(-1);

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
