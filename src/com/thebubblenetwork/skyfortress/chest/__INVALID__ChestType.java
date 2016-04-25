package com.thebubblenetwork.skyfortress.chest;

@Deprecated
public enum __INVALID__ChestType {
    SINGLE(3);
    private int size;

    __INVALID__ChestType(int rows) {
        size = rows * 9;
    }

    public int getSize() {
        return size;
    }
}
