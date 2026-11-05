package net.minecraft.src;

import java.util.Comparator;

public class RenderSorter implements Comparator<WorldRenderer> {
    private EntityLiving baseEntity;

    public RenderSorter(EntityLiving camera) {
        this.baseEntity = camera;
    }

    public int compare(WorldRenderer var1, WorldRenderer var2) {
        boolean flag = var1.isInFrustum;
        boolean flag1 = var2.isInFrustum;
        if (flag && !flag1) {
            return 1;
        } else if (flag1 && !flag) {
            return -1;
        } else {
            double d0 = var1.distanceToEntitySquared(this.baseEntity);
            double d1 = var2.distanceToEntitySquared(this.baseEntity);
            if (d0 < d1) {
                return 1;
            } else if (d0 > d1) {
                return -1;
            } else {
                return var1.chunkIndex < var2.chunkIndex ? 1 : -1;
            }
        }
    }
}
