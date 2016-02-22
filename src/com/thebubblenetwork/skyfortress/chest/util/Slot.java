package com.thebubblenetwork.skyfortress.chest.util;

public enum Slot {
    HELMET(Type.ARMOR),
    CHESTPLATE(Type.ARMOR),
    LEGGINGS(Type.ARMOR),
    BOOTS(Type.ARMOR),
    SWORD(Type.TOOL),
    OTHER_WEAPON(Type.TOOL),
    AXE(Type.TOOL),
    PICK(Type.TOOL),
    FOOD(Type.OTHER),
    EXTRA(null),
    META(Type.OTHER);

    private Type type;

    Slot(Type type){
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
