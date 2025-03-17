package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static einstein.subtle_effects.util.MathUtil.nextDouble;

@Mixin(Entity.class)
public abstract class ClientEntityMixin {

    @Shadow
    @Final
    protected RandomSource random;
    @Unique
    private final Entity subtleEffects$me = (Entity) (Object) this;

    @Inject(method = "playEntityOnFireExtinguishedSound", at = @At("TAIL"))
    private void addExtinguishParticles(CallbackInfo ci) {
        Level level = subtleEffects$me.level();

        if (level.isClientSide() && ModConfigs.ENTITIES.burning.extinguishSteam) {
            AABB boundingBox = subtleEffects$me.getBoundingBox();

            for (int x = Mth.floor(boundingBox.minX); x < Mth.ceil(boundingBox.maxX); x++) {
                for (int y = Mth.floor(boundingBox.minY); y < Mth.ceil(boundingBox.maxY); y++) {
                    for (int z = Mth.floor(boundingBox.minZ); z < Mth.ceil(boundingBox.maxZ); z++) {
                        BlockPos pos = new BlockPos(x, y, z);

                        if (level.getFluidState(pos).is(Fluids.WATER)) {
                            BlockPos abovePos = pos.above();

                            if (!Util.isSolidOrNotEmpty(level, abovePos)) {

                                // More particles than 5 will be displayed, due to this method being called by multiple ticks
                                for (int i = 0; i < 5; i++) {
                                    level.addParticle(ModParticles.STEAM.get(),
                                            pos.getX() + random.nextDouble(),
                                            pos.getY() + 0.875 + nextDouble(random, 0.5),
                                            pos.getZ() + random.nextDouble(),
                                            0, 0, 0
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
