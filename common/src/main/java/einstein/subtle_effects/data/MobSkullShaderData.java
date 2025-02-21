package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record MobSkullShaderData(ItemStackHolder stackHolder, ResourceLocation shaderId) {

    public static final Codec<MobSkullShaderData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStackHolder.CODEC.fieldOf("item").forGetter(MobSkullShaderData::stackHolder),
            ResourceLocation.CODEC.fieldOf("shader").forGetter(MobSkullShaderData::shaderId)
    ).apply(instance, (item, shaderId) ->
            new MobSkullShaderData(item, shaderId.withPath("shaders/post/" + shaderId.getPath() + ".json"))
    ));

    public record ItemStackHolder(Item item) {

        public static final Codec<ItemStackHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStackHolder::item)
        ).apply(instance, ItemStackHolder::new));

        public boolean matches(ItemStack stack) {
            return stack.is(item);
        }
    }
}
