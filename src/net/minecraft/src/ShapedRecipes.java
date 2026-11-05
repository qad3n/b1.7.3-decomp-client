package net.minecraft.src;

public class ShapedRecipes implements IRecipe {
    private int recipeWidth;
    private int recipeHeight;
    private ItemStack[] recipeItems;
    private ItemStack recipeOutput;
    public final int recipeOutputItemID;

    public ShapedRecipes(int width, int height, ItemStack[] ingredients, ItemStack result) {
        this.recipeOutputItemID = result.itemID;
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = ingredients;
        this.recipeOutput = result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
    public boolean matches(InventoryCrafting inventory) {
        for (int i = 0; i <= 3 - this.recipeWidth; i++) {
            for (int j = 0; j <= 3 - this.recipeHeight; j++) {
                if (this.matches(inventory, i, j, true)) {
                    return true;
                }

                if (this.matches(inventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(InventoryCrafting inventory, int x, int y, boolean rotate) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int k = i - x;
                int l = j - y;
                ItemStack itemstack = null;
                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (rotate) {
                        itemstack = this.recipeItems[this.recipeWidth - k - 1 + l * this.recipeWidth];
                    } else {
                        itemstack = this.recipeItems[k + l * this.recipeWidth];
                    }
                }

                ItemStack itemstack1 = inventory.getItem(i, j);
                if (itemstack1 != null || itemstack != null) {
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) {
                        return false;
                    }

                    if (itemstack.itemID != itemstack1.itemID) {
                        return false;
                    }

                    if (itemstack.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return new ItemStack(this.recipeOutput.itemID, this.recipeOutput.stackSize, this.recipeOutput.getItemDamage());
    }

    @Override
    public int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }
}
