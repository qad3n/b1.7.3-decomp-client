package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiParticle extends Gui {
    private List<Particle> particles = new ArrayList<>();
    private Minecraft minecraft;

    public GuiParticle(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void tick() {
        for (int i = 0; i < this.particles.size(); i++) {
            Particle particle = this.particles.get(i);
            particle.tickColor();
            particle.tickPosition(this);
            if (particle.removed) {
                this.particles.remove(i--);
            }
        }
    }

    public void render(float tickDelta) {
        this.minecraft.renderEngine.bindTexture(this.minecraft.renderEngine.getTexture("/gui/particles.png"));

        for (int i = 0; i < this.particles.size(); i++) {
            Particle particle = this.particles.get(i);
            int j = (int)(particle.lastX + (particle.x - particle.lastX) * tickDelta - 4.0);
            int k = (int)(particle.lastY + (particle.y - particle.lastY) * tickDelta - 4.0);
            float f = (float)(particle.lastA + (particle.a - particle.lastA) * tickDelta);
            float f1 = (float)(particle.lastR + (particle.r - particle.lastR) * tickDelta);
            float f2 = (float)(particle.lastG + (particle.g - particle.lastG) * tickDelta);
            float f3 = (float)(particle.lastB + (particle.b - particle.lastB) * tickDelta);
            GL11.glColor4f(f1, f2, f3, f);
            this.drawTexturedModalRect(j, k, 40, 0, 8, 8);
        }
    }
}
