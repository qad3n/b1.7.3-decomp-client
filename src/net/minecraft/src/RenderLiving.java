package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderLiving<T extends EntityLiving> extends Render<T> {
    protected ModelBase mainModel;
    protected ModelBase renderPassModel;

    public RenderLiving(ModelBase model, float shadowSize) {
        this.mainModel = model;
        this.shadowSize = shadowSize;
    }

    public void setRenderPassModel(ModelBase decorationModel) {
        this.renderPassModel = decorationModel;
    }

    public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
        GL11.glPushMatrix();
        GL11.glDisable(2884);
        this.mainModel.onGround = this.getAttackAnimationProgress((T)var1, var9);
        if (this.renderPassModel != null) {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = var1.isRiding();
        if (this.renderPassModel != null) {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        try {
            float f = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var9;
            float f1 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var9;
            float f2 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
            this.applyTranslation((T)var1, var2, var4, var6);
            float f3 = this.getBob((T)var1, var9);
            this.rotateCorpse((T)var1, f3, f, var9);
            float f4 = 0.0625F;
            GL11.glEnable(32826);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback((T)var1, var9);
            GL11.glTranslatef(0.0F, -24.0F * f4 - 0.0078125F, 0.0F);
            float f5 = var1.lastWalkAnimationSpeed + (var1.walkAnimationSpeed - var1.lastWalkAnimationSpeed) * var9;
            float f6 = var1.walkAnimationProgress - var1.walkAnimationSpeed * (1.0F - var9);
            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            this.loadDownloadableImageTexture(var1.skinUrl, var1.getEntityTexture());
            GL11.glEnable(3008);
            this.mainModel.setLivingAnimations(var1, f6, f5, var9);
            this.mainModel.render(f6, f5, f3, f1 - f, f2, f4);

            for (int i = 0; i < 4; i++) {
                if (this.shouldRenderPass((T)var1, i, var9)) {
                    this.renderPassModel.render(f6, f5, f3, f1 - f, f2, f4);
                    GL11.glDisable(3042);
                    GL11.glEnable(3008);
                }
            }

            this.renderEquippedItems((T)var1, var9);
            float f10 = var1.getEntityBrightness(var9);
            int j = this.getColorMultiplier((T)var1, f10, var9);
            if ((j >> 24 & 0xFF) > 0 || var1.hurtTime > 0 || var1.deathTime > 0) {
                GL11.glDisable(3553);
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if (var1.hurtTime > 0 || var1.deathTime > 0) {
                    GL11.glColor4f(f10, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(f6, f5, f3, f1 - f, f2, f4);

                    for (int k = 0; k < 4; k++) {
                        if (this.bindDecorationTexture((T)var1, k, var9)) {
                            GL11.glColor4f(f10, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(f6, f5, f3, f1 - f, f2, f4);
                        }
                    }
                }

                if ((j >> 24 & 0xFF) > 0) {
                    float f11 = (j >> 16 & 0xFF) / 255.0F;
                    float f7 = (j >> 8 & 0xFF) / 255.0F;
                    float f8 = (j & 0xFF) / 255.0F;
                    float f9 = (j >> 24 & 0xFF) / 255.0F;
                    GL11.glColor4f(f11, f7, f8, f9);
                    this.mainModel.render(f6, f5, f3, f1 - f, f2, f4);

                    for (int l = 0; l < 4; l++) {
                        if (this.bindDecorationTexture((T)var1, l, var9)) {
                            GL11.glColor4f(f11, f7, f8, f9);
                            this.renderPassModel.render(f6, f5, f3, f1 - f, f2, f4);
                        }
                    }
                }

                GL11.glDepthFunc(515);
                GL11.glDisable(3042);
                GL11.glEnable(3008);
                GL11.glEnable(3553);
            }

            GL11.glDisable(32826);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        GL11.glEnable(2884);
        GL11.glPopMatrix();
        this.passSpecialRender((T)var1, var2, var4, var6);
    }

    protected void applyTranslation(T entity, double dx, double dy, double dz) {
        GL11.glTranslatef((float)dx, (float)dy, (float)dz);
    }

    protected void rotateCorpse(T entity, float bob, float bodyYaw, float tickDelta) {
        GL11.glRotatef(180.0F - bodyYaw, 0.0F, 1.0F, 0.0F);
        if (entity.deathTime > 0) {
            float f = (entity.deathTime + tickDelta - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            GL11.glRotatef(f * this.getDeathMaxRotation((T)entity), 0.0F, 0.0F, 1.0F);
        }
    }

    protected float getAttackAnimationProgress(T entity, float tickDelta) {
        return entity.getSwingProgress(tickDelta);
    }

    protected float getBob(T entity, float tickDelta) {
        return entity.ticksExisted + tickDelta;
    }

    protected void renderEquippedItems(T entity, float tickDelta) {
    }

    protected boolean bindDecorationTexture(T var1, int var2, float var3) {
        return this.shouldRenderPass((T)var1, var2, var3);
    }

    protected boolean shouldRenderPass(T mob, int layer, float tickDelta) {
        return false;
    }

    protected float getDeathMaxRotation(T entity) {
        return 90.0F;
    }

    protected int getColorMultiplier(T entity, float brightness, float timeDelta) {
        return 0;
    }

    protected void preRenderCallback(T entity, float scale) {
    }

    protected void passSpecialRender(T entity, double dx, double dy, double dz) {
        if (Minecraft.isDebugInfoEnabled()) {
            this.renderLivingLabel((T)entity, Integer.toString(entity.entityId), dx, dy, dz, 64);
        }
    }

    protected void renderLivingLabel(T entity, String name, double dx, double dy, double dz, int range) {
        float f = entity.getDistanceToEntity(this.renderManager.livingPlayer);
        if (!(f > range)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f1 = 1.6F;
            float f2 = 0.016666668F * f1;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)dx + 0.0F, (float)dy + 2.3F, (float)dz);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f2, -f2, f2);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Tessellator tessellator = Tessellator.instance;
            byte b0 = 0;
            if (name.equals("deadmau5")) {
                b0 = -10;
            }

            GL11.glDisable(3553);
            tessellator.startDrawingQuads();
            int i = fontrenderer.getStringWidth(name) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex(-i - 1, -1 + b0, 0.0);
            tessellator.addVertex(-i - 1, 8 + b0, 0.0);
            tessellator.addVertex(i + 1, 8 + b0, 0.0);
            tessellator.addVertex(i + 1, -1 + b0, 0.0);
            tessellator.draw();
            GL11.glEnable(3553);
            fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, b0, 553648127);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, b0, -1);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
