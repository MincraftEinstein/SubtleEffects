package einstein.subtle_effects.util;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

public class CommonUtil {

    public static boolean isEntityInFluid(Entity entity, TagKey<Fluid> fluidTag) {
        if (entity.touchingUnloadedChunk()) {
            return false;
        }

        Level level = entity.level();
        AABB aabb = entity.getBoundingBox();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = Mth.floor(aabb.minX); x < Mth.ceil(aabb.maxX); x++) {
            for (int y = Mth.floor(aabb.minY); y < Mth.ceil(aabb.maxY); y++) {
                for (int z = Mth.floor(aabb.minZ); z < Mth.ceil(aabb.maxZ); z++) {
                    pos.set(x, y, z);
                    FluidState fluidState = level.getFluidState(pos);

                    if (fluidState.is(fluidTag)) {
                        double fluidHeight = y + fluidState.getHeight(level, pos);
                        if (fluidHeight >= aabb.minY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
