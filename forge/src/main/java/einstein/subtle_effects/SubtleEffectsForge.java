package einstein.subtle_effects;

import einstein.subtle_effects.init.ModPackets;
import einstein.subtle_effects.init.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static einstein.subtle_effects.SubtleEffects.MOD_ID;

@Mod(MOD_ID)
public class SubtleEffectsForge {

    public SubtleEffectsForge(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        SubtleEffects.init();
        ModParticles.init(); // For whatever reason this needs to be here instead of common
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new SubtleEffectsForgeClient(modEventBus));
        modEventBus.addListener((FMLCommonSetupEvent event) -> ModPackets.init());
    }
}