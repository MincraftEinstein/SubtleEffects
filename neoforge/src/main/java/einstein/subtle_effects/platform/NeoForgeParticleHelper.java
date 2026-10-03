package einstein.subtle_effects.platform;

import einstein.subtle_effects.platform.services.ParticleHelper;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.client.event.RegisterParticleGroupsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeParticleHelper implements ParticleHelper {

    public static final List<Consumer<RegisterParticleProvidersEvent>> PARTICLE_PROVIDERS = new ArrayList<>();
    public static final List<Consumer<RegisterParticleGroupsEvent>> PARTICLE_GROUPS = new ArrayList<>();

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, Function<SpriteSet, ParticleProvider<V>> provider) {
        PARTICLE_PROVIDERS.add(event -> event.registerSpriteSet(particleType.get(), provider::apply));
    }

    @Override
    public <T extends ParticleType<V>, V extends ParticleOptions> void registerParticleProvider(Supplier<T> particleType, ParticleProvider<V> provider) {
        PARTICLE_PROVIDERS.add(event -> event.registerSpecial(particleType.get(), provider));
    }

    @Override
    public void registerParticleGroup(ParticleRenderType group, Function<ParticleEngine, ParticleGroup<?>> provider) {
        PARTICLE_GROUPS.add(event -> event.register(group, provider));
    }

    @Override
    public TextColor getRarityColor(Rarity rarity) {
        return rarity.getStyleModifier().apply(Style.EMPTY).getColor();
    }
}
