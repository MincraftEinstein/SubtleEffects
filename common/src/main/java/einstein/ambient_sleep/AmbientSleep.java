package einstein.ambient_sleep;

import einstein.ambient_sleep.init.ModParticles;
import einstein.ambient_sleep.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO
//  Maybes
//  - Try to fix players being able to locally slide off beds (probably some kind of client desync)
//  - Try to fix third person in a bed as it is a little wierd

public class AmbientSleep {

    public static final String MOD_ID = "ambient_sleep";
    public static final String MOD_NAME = "Ambient Sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final int BREATH_DELAY = 60;
    public static final int SNORE_DELAY = 10;

    public static void init() {
        ModSounds.init();
        ModParticles.init();
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void playClientSound(SoundSource source, Entity entity, SoundEvent sound, float pitch) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level.isClientSide) {
            level.playSeededSound(minecraft.player, entity, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, 1, pitch, level.random.nextLong());
        }
    }

    public static void spawnSparks(Level level, RandomSource random, BlockPos pos, Vec3 offset, Vec3i maxSpeeds, int count, int size, boolean isSoulFlame) {
        if (random.nextInt(1) == 0) {
            for (int i = 0; i < count; i++) {
                SimpleParticleType type = (isSoulFlame ? ModParticles.SOUL_SPARK : ModParticles.SPARK).get();
                level.addParticle(type,
                        pos.getX() + offset.x() + random.nextDouble() / size * (random.nextBoolean() ? 1 : -1),
                        pos.getY() + offset.y(),
                        pos.getZ() + offset.z() + random.nextDouble() / size * (random.nextBoolean() ? 1 : -1),
                        random.nextInt(maxSpeeds.getX()) / 100D, random.nextInt(maxSpeeds.getY()) / 100D, random.nextInt(maxSpeeds.getZ()) / 100D
                );
            }
        }
    }
}