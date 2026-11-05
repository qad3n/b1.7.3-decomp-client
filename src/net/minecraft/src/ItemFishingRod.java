package net.minecraft.src;


public class ItemFishingRod
extends Item {
    public ItemFishingRod(int n) {
        super(n);
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
    }

    public boolean isFull3D() {
        return true;
    }

    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.fishEntity != null) {
            int n = player.fishEntity.catchFish();
            stack.damageItem(n, player);
            player.swingItem();
        } else {
            world.playSoundAtEntity(player, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!world.multiplayerWorld) {
                world.entityJoinedWorld(new EntityFish(world, player));
            }
            player.swingItem();
        }
        return stack;
    }
}

