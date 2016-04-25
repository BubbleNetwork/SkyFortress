package com.thebubblenetwork.skyfortress.chest.gen;

@Deprecated
public enum __INVALID__ChestSlot {
    HELMET(__INVALID__ChestSlotType.ARMOR),
    CHESTPLATE(__INVALID__ChestSlotType.ARMOR),
    LEGGINGS(__INVALID__ChestSlotType.ARMOR),
    BOOTS(__INVALID__ChestSlotType.ARMOR),
    SWORD(__INVALID__ChestSlotType.WEAPON),
    OTHER_WEAPON(__INVALID__ChestSlotType.WEAPON),
    AXE(__INVALID__ChestSlotType.TOOL),
    PICK(__INVALID__ChestSlotType.TOOL),
    FOOD(__INVALID__ChestSlotType.OTHER),
    EXTRA(null),
    //META(ChestType.OTHER),
    BLOCK(__INVALID__ChestSlotType.OTHER);

    private __INVALID__ChestSlotType type;

    __INVALID__ChestSlot(__INVALID__ChestSlotType type) {
        this.type = type;
    }

    public __INVALID__ChestSlotType getType() {
        return type;
    }
}
