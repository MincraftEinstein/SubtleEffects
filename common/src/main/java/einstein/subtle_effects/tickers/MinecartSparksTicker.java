package einstein.subtle_effects.tickers;

import einstein.subtle_effects.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.util.MathUtil.nextFloat;

public class MinecartSparksTicker extends Ticker<AbstractMinecart> {

    private int ticksInAir;
    private int sparksTimer;
    private int sparksCount;
    private Vec3 deltaMovement;

    public MinecartSparksTicker(AbstractMinecart entity) {
        super(entity);
        deltaMovement = entity.getDeltaMovement();
    }

    @Override
    public void tick() {
        Vec3 currentDeltaMovement = entity.getDeltaMovement();
        if (currentDeltaMovement.x() == 0 && currentDeltaMovement.z() == 0) {
            return;
        }

        if ((currentDeltaMovement.x() < 0 && deltaMovement.x() > 0)
                || (currentDeltaMovement.x() > 0 && deltaMovement.x() < 0)
                || (currentDeltaMovement.z() < 0 && deltaMovement.z() > 0)
                || (currentDeltaMovement.z() > 0 && deltaMovement.z() < 0)) {
            sparksTimer = 0;
            sparksCount = 0;
            return;
        }
        deltaMovement = currentDeltaMovement;

        BlockPos pos = entity.blockPosition();
        BlockPos belowPos = pos.below();
        if (level.getBlockState(belowPos).is(BlockTags.RAILS)) {
            pos = belowPos;
        }

        boolean onRails = BaseRailBlock.isRail(level, pos);
        if (onRails) {
            if (ticksInAir >= 10) {
                sparksTimer = 40;
                sparksCount = 20;
            }
            else if (ticksInAir >= 5) {
                sparksTimer = 20;
                sparksCount = 5;
            }

            ticksInAir = 0;
        }
        else {
            ticksInAir++;
        }

        if (sparksTimer > 0) {
            sparksTimer--;

            for (int i = 0; i < sparksCount; i++) {
                level.addParticle(ModParticles.METAL_SPARK.get(),
                        entity.position().x() + entity.getBbWidth() * random.nextDouble() - 1,
                        entity.getY() + Math.max(Math.min(random.nextFloat(), 0.5), 0.2),
                        entity.position().z() + entity.getBbWidth() * random.nextDouble() - 1,
                        nextFloat(10, 20) * (deltaMovement.x() * 1.5),
                        nextFloat(10, 20),
                        nextFloat(10, 20) * (deltaMovement.z() * 1.5)
                );
            }
            return;
        }
        sparksCount = 0;
    }
}
