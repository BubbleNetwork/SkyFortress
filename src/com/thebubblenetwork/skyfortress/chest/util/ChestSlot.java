package com.thebubblenetwork.skyfortress.chest.util;

public enum ChestSlot {
    HELMET(ChestType.ARMOR),
    CHESTPLATE(ChestType.ARMOR),
    LEGGINGS(ChestType.ARMOR),
    BOOTS(ChestType.ARMOR),
    SWORD(ChestType.TOOL),
    OTHER_WEAPON(ChestType.TOOL),
    AXE(ChestType.TOOL),
    PICK(ChestType.TOOL),
    FOOD(ChestType.OTHER),
    EXTRA(null),
    META(ChestType.OTHER),
    BLOCK(ChestType.OTHER);

    private ChestType type;

    ChestSlot(ChestType type){
        this.type = type;
    }

    public ChestType getType() {
        return type;
    }
}
