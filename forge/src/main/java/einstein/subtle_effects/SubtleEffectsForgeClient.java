package einstein.subtle_effects;

import einstein.subtle_effects.platform.ForgeNetworkHelper;
import einstein.subtle_effects.platform.ForgeRegistryHelper;
import einstein.subtle_effects.platform.services.NetworkHelper;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(value = SubtleEffects.MOD_ID)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class SubtleEffectsForgeClient {

    public SubtleEffectsForgeClient(IEventBus modEventBus) {
        SubtleEffectsClient.clientSetup();
        ForgeNetworkHelper.init(NetworkHelper.Direction.TO_SERVER);
        modEventBus.addListener((RegisterParticleProvidersEvent event) ->
                ForgeRegistryHelper.PARTICLE_PROVIDERS.forEach((particle, provider) -> registerParticle(event, particle, provider))
        );
        modEventBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                event.addRepositorySource(new BCWPSource());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            Minecraft minecraft = Minecraft.getInstance();
            SubtleEffectsClient.clientTick(minecraft, minecraft.level);
        });
        MinecraftForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> SubtleEffectsClient.registerClientCommands(event.getDispatcher(), event.getBuildContext()));
    }

    @SuppressWarnings("unchecked")
    private static <T extends ParticleType<V>, V extends ParticleOptions> void registerParticle(RegisterParticleProvidersEvent event, Supplier<? extends ParticleType<?>> particle, Function<SpriteSet, ? extends ParticleProvider<?>> provider) {
        Supplier<T> t = (Supplier<T>) particle;
        Function<SpriteSet, V> v = (Function<SpriteSet, V>) provider;
        event.registerSpriteSet(t.get(), sprites -> (ParticleProvider<V>) v.apply(sprites));
    }

    public static class BCWPSource implements RepositorySource {

        @Override
        public void loadPacks(Consumer<Pack> consumer) {
            IModInfo info = ModList.get().getModContainerById(SubtleEffects.MOD_ID).orElseThrow().getModInfo();

            consumer.accept(Pack.readMetaAndCreate(
                    "mod/" + Util.BCWP_PACK_LOCATION.get().toString(), Util.BCWP_PACK_NAME, false, path ->
                            new PathPackResources(path, info.getOwningFile().getFile().findResource(Util.BCWP_PACK_LOCATION.get().getPath()), true),
                    PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN
            ));
        }
    }
}
