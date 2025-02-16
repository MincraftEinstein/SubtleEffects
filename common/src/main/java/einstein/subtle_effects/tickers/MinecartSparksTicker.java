package einstein.subtle_effects.tickers;

import einstein.subtle_effects.configs.ModEntityConfigs;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.particle.SparkParticle;
import einstein.subtle_effects.util.SparkType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.util.MathUtil.nextNonAbsDouble;
import static net.minecraft.util.Mth.nextFloat;

public class MinecartSparksTicker extends Ticker<AbstractMinecart> {

    private static final Vec3[] WHEEL_POSITIONS = new Vec3[] {
            new Vec3(-1, 0, 1),
            new Vec3(1, 0, 1),
            new Vec3(-1, 0, -1),
            new Vec3(1, 0, -1)
    };

    private int derailedTicks;
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

        deltaMovement = currentDeltaMovement;
        if (ModConfigs.ENTITIES.minecartSparksDisplayType == ModEntityConfigs.MinecartSparksDisplayType.DEFAULT) {
            spawnSparks(10);
            return;
        }

        BlockPos pos = entity.blockPosition();
        BlockPos belowPos = pos.below();
        if (level.getBlockState(belowPos).is(BlockTags.RAILS)) {
            pos = belowPos;
        }

        if (BaseRailBlock.isRail(level, pos)) {
            if (derailedTicks >= 10) {
                sparksTimer = 40;
                sparksCount = 20;
            }
            else if (derailedTicks >= 5) {
                sparksTimer = 20;
                sparksCount = 5;
            }

            derailedTicks = 0;
        }
        else {
            derailedTicks++;
        }

        if (sparksTimer > 0) {
            sparksTimer--;
            spawnSparks(sparksCount);
            return;
        }
        sparksCount = 0;
    }

    private void spawnSparks(int count) {
        count *= ModConfigs.ENTITIES.minecartSparksDensity.get();
        double wHalf = entity.getBbWidth() / 2;

        for (Vec3 vec3 : WHEEL_POSITIONS) {
            double xSize = wHalf * vec3.x;
            double zSize = wHalf * vec3.z;
            boolean isXNeg = deltaMovement.x() < 0;
            boolean isZNeg = deltaMovement.z() < 0;
            boolean isFrontX = isXNeg == vec3.x < 0;
            boolean isFrontZ = isZNeg == vec3.z < 0;
            double x = entity.getX() + (xSize + (deltaMovement.x() != 0 ? (isFrontX ? -0.03 : 0.3) * (isXNeg ? -1 : 1) : 0));
            double z = entity.getZ() + (zSize + (deltaMovement.z() != 0 ? (isFrontZ ? -0.03 : 0.3) * (isZNeg ? -1 : 1) : 0));

            for (int i = 0; i < (count / ((isFrontX || isFrontZ ? 2 : 1))); i++) {
                double xSpeed = isFrontX ? 0 : nextFloat(random, 0.1F, 0.2F) * (-deltaMovement.x() * 1.5);
                double zSpeed = isFrontZ ? 0 : nextFloat(random, 0.1F, 0.2F) * (-deltaMovement.z() * 1.5);

                level.addParticle(SparkParticle.create(SparkType.METAL, random),
                        x + nextNonAbsDouble(random, (isFrontX ? 0.1 : 0.05)),
                        entity.getY(),
                        z + nextNonAbsDouble(random, (isFrontZ ? 0.1 : 0.05)),
                        xSpeed,
                        xSpeed != 0 || zSpeed != 0 ? nextFloat(random, 0.1F, 0.2F) : 0,
                        zSpeed
                );
            }
        }
    }
}
