package einstein.subtle_effects.init;

import einstein.subtle_effects.SubtleEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModAnimalFedEffectSettings {

    private static final Map<EntityType<?>, Settings> REGISTERED = new HashMap<>();
    public static final Settings DEFAULT_VALUE = new Settings(Vec3.ZERO, null, stack -> stack);

    public static void init() {
        REGISTERED.clear();
        if (ModConfigs.ENTITIES.animalFeedingSoundVolume.get() == 0 && !ModConfigs.ENTITIES.animalFeedingParticles) {
            return;
        }

        register(EntityType.COW, ModSounds.COW_EAT);
        register(EntityType.MOOSHROOM, SoundEvents.MOOSHROOM_EAT);
        register(EntityType.CHICKEN, 0.3, -0.7, ModSounds.CHICKEN_EAT);
        register(EntityType.CAT, 0, -0.4, SoundEvents.CAT_EAT);
        register(EntityType.OCELOT, 0, -0.3, ModSounds.OCELOT_EAT);
        register(EntityType.PANDA, -0.35, 0.3, SoundEvents.PANDA_EAT);
        register(EntityType.SHEEP, 0, -0.2, ModSounds.SHEEP_EAT);
        register(EntityType.PARROT, 0.2, -0.9, SoundEvents.PARROT_EAT);
        register(EntityType.AXOLOTL, new Vec3(0, 0, -0.4), ModSounds.AXOLOTL_EAT,
                stack -> stack.is(Items.TROPICAL_FISH_BUCKET) ? new ItemStack(Items.TROPICAL_FISH) : stack
        );
        register(EntityType.CAMEL, 1, 0.6);
        register(EntityType.BEE, 0, -0.7, ModSounds.BEE_EAT);
        register(EntityType.FROG, 0, -0.7, SoundEvents.FROG_EAT);
        register(EntityType.DOLPHIN, 0, -0.2, SoundEvents.DOLPHIN_EAT);
        register(EntityType.TADPOLE, 0, -0.9, ModSounds.TADPOLE_EAT);
        register(EntityType.FOX, 0, -0.6);
        register(EntityType.LLAMA, 0.4, -0.1);
        register(EntityType.TRADER_LLAMA, 0.4, -0.1);
        register(EntityType.RABBIT, 0, -0.8, ModSounds.RABBIT_EAT);
        register(EntityType.SNIFFER, 0, 1.5);
        register(EntityType.MULE, 0, 0.2);
        register(EntityType.DONKEY, 0, 0.2);
        register(EntityType.HORSE, 0.2, 0.4);
        register(EntityType.STRIDER, 0, -0.6);
        register(EntityType.ARMADILLO, 0, -0.4);
        register(EntityType.WOLF, 0, -0.3);
        register(EntityType.TURTLE, ModSounds.TURTLE_EAT);
        register(EntityType.HOGLIN, ModSounds.HOGLIN_EAT);
    }

    public static void register(EntityType<?> type, double y, double z) {
        register(type, y, z, (Supplier<SoundEvent>) null);
    }

    public static void register(EntityType<?> type, double y, double z, @Nullable SoundEvent sound) {
        register(type, y, z, () -> sound);
    }

    public static void register(EntityType<?> type, double y, double z, @Nullable Supplier<SoundEvent> sound) {
        register(type, new Vec3(0, y, z), sound);
    }

    public static void register(EntityType<?> type, @Nullable SoundEvent sound) {
        register(type, Vec3.ZERO, () -> sound);
    }

    public static void register(EntityType<?> type, @Nullable Supplier<SoundEvent> sound) {
        register(type, Vec3.ZERO, sound);
    }

    public static void register(EntityType<?> type, Vec3 offset, @Nullable Supplier<SoundEvent> sound) {
        register(type, offset, sound, stack -> stack);
    }

    public static void register(EntityType<?> type, Vec3 offset, @Nullable Supplier<SoundEvent> sound, UnaryOperator<ItemStack> stackReplacer) {
        if (REGISTERED.put(type, new Settings(offset, sound, stackReplacer)) != null) {
            SubtleEffects.LOGGER.error("Attempted to register multiple feeding effect overrides to entity: \"{}\"", BuiltInRegistries.ENTITY_TYPE.getKey(type));
        }
    }

    public static Settings getSetting(EntityType<?> type) {
        return REGISTERED.getOrDefault(type, DEFAULT_VALUE);
    }

    public record Settings(Vec3 offset, @Nullable Supplier<SoundEvent> sound,
                           UnaryOperator<ItemStack> stackReplacer) {

    }
}
