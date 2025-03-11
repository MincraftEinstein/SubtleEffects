package einstein.subtle_effects.data;

import com.google.common.base.Suppliers;
import einstein.subtle_effects.SubtleEffects;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.platform.Services;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BCWPPackManager implements ResourceManagerReloadListener, NamedReloadListener {

    public static final Supplier<ResourceLocation> PACK_LOCATION = Suppliers.memoize(() -> SubtleEffects.loc("biome_color_water_particles").withPrefix(Services.PLATFORM.getPlatform().isForgeLike() ? "resourcepacks/" : ""));
    public static final Supplier<String> PACK_ID = Suppliers.memoize(() -> (Services.PLATFORM.getPlatform().isForgeLike() ? "mod/" : "") + PACK_LOCATION.get().toString());
    public static final Component PACK_NAME = Component.translatable("resourcePack.subtle_effects.biome_water_color_particles.name");
    public static final List<ParticleType<?>> BIOME_COLORED_PARTICLES = Util.make(new ArrayList<>(), particles -> {
        particles.add(ParticleTypes.BUBBLE);
        particles.add(ParticleTypes.FISHING);
        particles.add(ParticleTypes.BUBBLE_POP);
        particles.add(ParticleTypes.BUBBLE_COLUMN_UP);
        particles.add(ParticleTypes.CURRENT_DOWN);
        particles.add(ParticleTypes.RAIN);
        particles.add(ParticleTypes.SPLASH);
        particles.add(ParticleTypes.UNDERWATER);
        particles.add(ParticleTypes.DRIPPING_WATER);
        particles.add(ParticleTypes.FALLING_WATER);
        particles.add(ParticleTypes.DRIPPING_DRIPSTONE_WATER);
        particles.add(ParticleTypes.FALLING_DRIPSTONE_WATER);
        particles.add(ModParticles.DROWNING_BUBBLE.get());
        particles.add(ModParticles.DROWNING_BUBBLE_POP.get());
    });
    private static Supplier<Boolean> IS_PACK_LOADED = () -> false;

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        IS_PACK_LOADED = Suppliers.memoize(() ->
                Minecraft.getInstance()
                        .getResourcePackRepository()
                        .getSelectedIds()
                        .contains(PACK_ID.get())
        );
    }

    public static boolean isPackLoaded() {
        return IS_PACK_LOADED.get();
    }

    @Override
    public ResourceLocation getId() {
        return SubtleEffects.loc("biome_color_water_particles_pack_manager");
    }
}
