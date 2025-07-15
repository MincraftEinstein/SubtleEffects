package einstein.subtle_effects.ticking.tickers.entity;

import einstein.subtle_effects.compat.CompatHelper;
import einstein.subtle_effects.compat.SereneSeasonsCompat;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

public class FrostyBreathTicker extends EntityTicker<LivingEntity> {

    private int delayTimer = 0;
    private int breatheTimer = 0;
    private int breatheOutTimer = 0;
    private final int startDelay = random.nextInt(40);

    public FrostyBreathTicker(LivingEntity entity) {
        super(entity, true);
    }

    @Override
    public void entityTick() {
        BlockPos pos = entity.blockPosition();
        if (entity.isSpectator()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (entity instanceof Player player) {
            if (player.equals(minecraft.player) && !ENTITIES.humanoids.frostyBreath.displayType.test(minecraft)) {
                return;
            }

            if (player.isCreative() && !ENTITIES.humanoids.player.enableBreathingEffectsInCreative) {
                return;
            }
        }

        if (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) >= 10) {
            return;
        }

        if (entity.isInWater() || entity.isEyeInFluid(FluidTags.LAVA)) {
            return;
        }

        Holder<Biome> biome = level.getBiome(pos);
        if (biome.value().coldEnoughToSnow(pos)
                || ENTITIES.humanoids.frostyBreath.additionalBiomes.contains(biome.unwrapKey().map(ResourceKey::location).orElse(null))
                || (CompatHelper.IS_SERENE_SEANSONS_LOADED.get() && SereneSeasonsCompat.isColdSeason(level, ENTITIES.humanoids.frostyBreath.seasons))) {
            if (delayTimer < startDelay) {
                delayTimer++;
                return;
            }

            if (breatheTimer >= ENTITIES.humanoids.frostyBreath.waitTime.get()) {
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
