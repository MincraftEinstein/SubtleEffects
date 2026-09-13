package einstein.subtle_effects.configs.cache;

import einstein.subtle_effects.init.ModConfigs;
import net.minecraft.core.particles.ParticleType;

import java.util.Set;

/**
 * Cached particle culling settings used while rendering every particle each frame.
 */
public final class ParticleCullingCache {

    public static boolean enabled;
    public static boolean cullInUnloadedChunks;
    public static int renderDistanceSquared;
    public static Set<ParticleType<?>> cullingBlocklist = Set.of();

    private ParticleCullingCache() {
    }

    public static void refresh() {
        var general = ModConfigs.GENERAL;
        enabled = general.enableParticleCulling.get();
        cullInUnloadedChunks = general.cullParticlesInUnloadedChunks.get();

        int distanceBlocks = general.particleRenderDistance.get() * 16;
        renderDistanceSquared = distanceBlocks * distanceBlocks;

        cullingBlocklist = Set.copyOf(general.particleCullingBlocklist.get());
    }
}
