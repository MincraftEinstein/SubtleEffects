package einstein.subtle_effects.platform.services;

import einstein.subtle_effects.mixin.client.particle.SpriteSetAccessor;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ParticleHelper {

    <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, Function<SpriteSet, ParticleProvider<V>> provider);

    <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, ParticleProvider<V> provider);

    void registerParticleGroup(ParticleRenderType group, Function<ParticleEngine, ParticleGroup<?>> provider);

    @Nullable
    default List<TextureAtlasSprite> getSpritesFromSet(SpriteSet spriteSet) {
        return ((SpriteSetAccessor) spriteSet).getSprites();
    }

    default TextColor getRarityColor(Rarity rarity) {
        return TextColor.fromLegacyFormat(rarity.color());
    }
}
