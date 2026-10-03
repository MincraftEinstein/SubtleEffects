package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.ParticleHelper;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet;
import net.fabricmc.fabric.api.client.particle.v1.ParticleGroupRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricParticleHelper implements ParticleHelper {

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, Function<SpriteSet, ParticleProvider<V>> provider) {
        ParticleProviderRegistry.getInstance().register(particleType.get(), provider::apply);
    }

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, ParticleProvider<V> provider) {
        ParticleProviderRegistry.getInstance().register(particleType.get(), provider);
    }

    @Override
    public void registerParticleGroup(ParticleRenderType group, Function<ParticleEngine, ParticleGroup<?>> provider) {
        ParticleGroupRegistry.register(group, provider);
    }

    @Override
    public List<TextureAtlasSprite> getSpritesFromSet(SpriteSet spriteSet) {
        if (spriteSet instanceof FabricSpriteSet spriteProvider) {
            return spriteProvider.getSprites();
        }
        return ParticleHelper.super.getSpritesFromSet(spriteSet);
    }
}
