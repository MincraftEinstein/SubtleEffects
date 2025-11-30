package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record MobSkullShaderData(ItemStackHolder stackHolder, Identifier shaderId) {

    public static final Codec<MobSkullShaderData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStackHolder.CODEC.fieldOf("item").forGetter(MobSkullShaderData::stackHolder),
            Identifier.CODEC.fieldOf("shader").forGetter(MobSkullShaderData::shaderId)
    ).apply(instance, MobSkullShaderData::new));

    public record ItemStackHolder(Item item, DataComponentMap components) {

        public static final Codec<ItemStackHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStackHolder::item),
                DataComponentMap.CODEC.optionalFieldOf("components", DataComponentMap.EMPTY).forGetter(ItemStackHolder::components)
        ).apply(instance, ItemStackHolder::new));

        public boolean matches(ItemStack stack) {
            if (stack.is(item)) {
                for (TypedDataComponent<?> component : components) {
                    DataComponentType<?> type = component.type();

                    if (!stack.has(type) || !component.value().equals(stack.get(type))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }
}
