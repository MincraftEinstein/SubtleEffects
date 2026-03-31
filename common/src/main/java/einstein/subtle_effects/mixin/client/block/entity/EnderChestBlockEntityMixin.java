package einstein.subtle_effects.mixin.client.block.entity;

import einstein.subtle_effects.util.ChestAccessor;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin implements ChestAccessor {

    @Override
    @Accessor("chestLidController")
    public abstract ChestLidController subtleEffects$getLidController();
}
