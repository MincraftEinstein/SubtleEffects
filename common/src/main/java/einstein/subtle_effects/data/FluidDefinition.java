package einstein.subtle_effects.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.subtle_effects.data.splash_types.SplashType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record FluidDefinition(ResourceLocation id, Fluid source, Fluid flowing,
                              Optional<AbstractCauldronBlock> cauldron,
                              Optional<SplashType> splashType, Optional<BucketItem> bucketItem,
                              DropletOptions dropletOptions,
                              int lightEmission) {

    public boolean is(@Nullable FluidDefinition fluidDefinition) {
        if (fluidDefinition == null) {
            return false;
        }
        return this == fluidDefinition;
    }

    @SuppressWarnings("deprecation")
    public boolean is(TagKey<Fluid> tag) {
        return source.is(tag) || flowing.is(tag);
    }

    public boolean is(Fluid fluid) {
        return fluid == source || fluid == flowing;
    }

    public boolean is(FluidState state) {
        Fluid type = state.getType();
        return type == source || type == flowing;
    }

    public boolean is(BlockState state) {
        return cauldron.filter(cauldronBlock -> state.getBlock() == cauldronBlock).isPresent();
    }

    public record Data(Fluid source, Fluid flowing, Optional<AbstractCauldronBlock> cauldron,
                       Optional<ResourceLocation> splashTypeId, Optional<BucketItem> bucketItem,
                       DropletOptions dropletOptions, int lightEmission) {

        public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.FLUID.byNameCodec().fieldOf("source").forGetter(Data::source),
                BuiltInRegistries.FLUID.byNameCodec().fieldOf("flowing").forGetter(Data::flowing),
                BuiltInRegistries.BLOCK.byNameCodec().comapFlatMap(block -> {
                    if (block instanceof AbstractCauldronBlock cauldron) {
                        return DataResult.success(cauldron);
                    }
                    return DataResult.error(() -> "Block is not a cauldron: '" + BuiltInRegistries.BLOCK.getKey(block) + "'");
                }, cauldronBlock -> cauldronBlock).optionalFieldOf("cauldron").forGetter(Data::cauldron),
                ResourceLocation.CODEC.optionalFieldOf("splash_type").forGetter(Data::splashTypeId),
                BuiltInRegistries.ITEM.byNameCodec().comapFlatMap(item -> {
                    if (item instanceof BucketItem bucketItem) {
                        return DataResult.success(bucketItem);
                    }
                    return DataResult.error(() -> "Item is not a bucket: '" + BuiltInRegistries.ITEM.getKey(item) + "'");
                }, bucketItem -> bucketItem).optionalFieldOf("bucket_item").forGetter(Data::bucketItem),
                DropletOptions.CODEC.optionalFieldOf("droplets", DropletOptions.DEFAULT).forGetter(Data::dropletOptions),
                Codec.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(Data::lightEmission)
        ).apply(instance, Data::new));
    }
}
