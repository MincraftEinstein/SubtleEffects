package einstein.subtle_effects.particle.option;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import einstein.subtle_effects.init.ModParticles;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Locale;

// This is very cursed I know
@SuppressWarnings("deprecation")
public record FallenLeafParticleOptions(@Nullable TextureAtlasSprite sprite, float quadSize,
                                        float bbWidth, float bbHeight, boolean onGround,
                                        Vector3f color, float alpha, float rotation) implements ParticleOptions {

    public static final FallenLeafParticleOptions EMPTY = new FallenLeafParticleOptions(null, 0, 0, 0, false, new Vector3f(), 0, 0);
    public static final Codec<FallenLeafParticleOptions> CODEC = Codec.unit(EMPTY);
    public static final Deserializer<FallenLeafParticleOptions> DESERIALIZER = new Deserializer<>() {

        @Override
        public FallenLeafParticleOptions fromCommand(ParticleType<FallenLeafParticleOptions> type, StringReader reader) {
            return EMPTY;
        }

        @Override
        public FallenLeafParticleOptions fromNetwork(ParticleType<FallenLeafParticleOptions> type, FriendlyByteBuf buf) {
            return EMPTY;
        }
    };

    @Override
    public ParticleType<?> getType() {
        return ModParticles.FALLEN_LEAF.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s", BuiltInRegistries.PARTICLE_TYPE.getKey(getType()));
    }
}
