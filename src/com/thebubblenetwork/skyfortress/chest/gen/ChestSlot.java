package com.thebubblenetwork.skyfortress.chest.gen;

public enum ChestSlot {
    HELMET(ChestSlotType.ARMOR),
    CHESTPLATE(ChestSlotType.ARMOR),
    LEGGINGS(ChestSlotType.ARMOR),
    BOOTS(ChestSlotType.ARMOR),
    SWORD(ChestSlotType.TOOL),
    OTHER_WEAPON(ChestSlotType.TOOL),
    AXE(ChestSlotType.TOOL),
    PICK(ChestSlotType.TOOL),
    FOOD(ChestSlotType.OTHER),
    EXTRA(null),
    //META(ChestType.OTHER),
    BLOCK(ChestSlotType.OTHER);

    private ChestSlotType type;

    ChestSlot(ChestSlotType type) {
        this.type = type;
    }

    public ChestSlotType getType() {
        return type;
    }
}
