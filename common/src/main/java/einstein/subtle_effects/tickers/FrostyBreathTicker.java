package einstein.subtle_effects.tickers;

import einstein.subtle_effects.compat.SereneSeasonsCompat;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class FrostyBreathTicker extends Ticker<LivingEntity> {

    private int breatheTimer = 0;
    private int breatheOutTimer = 0;

    public FrostyBreathTicker(LivingEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        BlockPos pos = entity.blockPosition();
        if (entity.isSpectator()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (entity instanceof Player player) {
            if (player.isCreative()) {
                return;
            }

            if (player.equals(minecraft.player) && !ENTITIES.frostyBreath.test(minecraft)) {
                return;
            }
        }

        if (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) >= 10) {
            return;
        }

        if (entity.isInWater() || entity.isEyeInFluid(FluidTags.LAVA)) {
            return;
        }

        if (level.getBiome(pos).value().coldEnoughToSnow(pos) || (Util.IS_SERENE_SEANSONS_LOADED.get() && SereneSeasonsCompat.isColdSeason(level))) {
            if (breatheTimer >= ENTITIES.frostyBreathTime.get()) {
                ParticleSpawnUtil.spawnEntityFaceParticle(ModParticles.FROSTY_BREATH.get(),
                        entity, level, random, new Vec3(0, -0.1, 0),
                        new Vec3(0, 0, Mth.nextDouble(random, 0.005, 0.01)),
                        minecraft.getTimer().getGameTimeDeltaPartialTick(false)
                );

                if (breatheOutTimer >= 7) {
                    breatheOutTimer = 0;
                    breatheTimer = 0;
                }
                breatheOutTimer++;
            }
            breatheTimer++;
        }
    }
}
