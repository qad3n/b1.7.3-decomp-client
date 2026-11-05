package net.minecraft.src;

public class ItemTool extends Item {
    private Block[] blocksEffectiveAgainst;
    private float efficiencyOnProperMaterial = 4.0F;
    private int damageVsEntity;
    protected Item.EnumToolMaterial toolMaterial;

    protected ItemTool(int id, int attackDamage, Item.EnumToolMaterial tier, Block[] effectiveBlocks) {
        super(id);
        this.toolMaterial = tier;
        this.blocksEffectiveAgainst = effectiveBlocks;
        this.maxStackSize = 1;
        this.setMaxDamage(tier.getMaxUses());
        this.efficiencyOnProperMaterial = tier.getEfficiencyOnProperMaterial();
        this.damageVsEntity = attackDamage + tier.getDamageVsEntity();
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        for (int i = 0; i < this.blocksEffectiveAgainst.length; i++) {
            if (this.blocksEffectiveAgainst[i] == block) {
                return this.efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLiving target, EntityLiving attacker) {
        stack.damageItem(2, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, int block, int x, int y, int z, EntityLiving entity) {
        stack.damageItem(1, entity);
        return true;
    }

    @Override
    public int getDamageVsEntity(Entity target) {
        return this.damageVsEntity;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }
}
