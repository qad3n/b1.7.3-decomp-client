package net.minecraft.src;


public interface ICrafting {
    public void onSlotChanged(Container var1, int var2, ItemStack var3);

    public void onDataChanged(Container var1, int var2, int var3);
}

