package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.ParticleHelper;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.List;
import java.util.function.Supplier;

public class FabricParticleHelper implements ParticleHelper {

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, ParticleEngine.SpriteParticleRegistration<V> provider) {
        ParticleFactoryRegistry.getInstance().register(particleType.get(), provider::create);
    }

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, ParticleProvider<V> provider) {
        ParticleFactoryRegistry.getInstance().register(particleType.get(), provider);
    }

    @Override
    public List<TextureAtlasSprite> getSpritesFromSet(SpriteSet spriteSet) {
        if (spriteSet instanceof FabricSpriteProvider spriteProvider) {
            return spriteProvider.getSprites();
        }
        return ParticleHelper.super.getSpritesFromSet(spriteSet);
    }
}
