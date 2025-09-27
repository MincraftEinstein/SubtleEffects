package einstein.subtle_effects.particle.option;

import com.mojang.serialization.MapCodec;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

// This is very cursed I know
public record FallenLeafParticleOptions(@Nullable TextureAtlasSprite sprite, float quadSize,
                                        float bbWidth, float bbHeight, boolean onGround,
                                        Vector3f color, float alpha, float rotation) implements ParticleOptions {

    public static final FallenLeafParticleOptions EMPTY = new FallenLeafParticleOptions(null, 0, 0, 0, false, new Vector3f(), 0, 0);
    public static final MapCodec<FallenLeafParticleOptions> CODEC = MapCodec.unit(EMPTY);
    public static final StreamCodec<RegistryFriendlyByteBuf, FallenLeafParticleOptions> STREAM_CODEC = StreamCodec.unit(EMPTY);

    @Override
    public ParticleType<?> getType() {
        return ModParticles.FALLEN_LEAF.get();
    }
}
