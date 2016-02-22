package com.thebubblenetwork.skyfortress.chest;

public enum ChestType {
    SINGLE(3);
    private int size;

    ChestType(int rows){
        size = rows*9;
    }

    public int getSize() {
        return size;
    }
}
