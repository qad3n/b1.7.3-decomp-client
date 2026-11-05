package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderBiped<T extends EntityLiving> extends RenderLiving<T> {
    protected ModelBiped modelBipedMain;

    public RenderBiped(ModelBiped model, float shadowSize) {
        super(model, shadowSize);
        this.modelBipedMain = model;
    }

    @Override
    protected void renderEquippedItems(T entity, float tickDelta) {
        ItemStack itemstack = entity.getHeldItem();
        if (itemstack != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            if (itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
                float f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f2 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
            } else if (Item.itemsList[itemstack.itemID].isFull3D()) {
                float f = 0.625F;
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f, -f, f);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                float f1 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f1, f1, f1);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(entity, itemstack);
            GL11.glPopMatrix();
        }
    }
}
