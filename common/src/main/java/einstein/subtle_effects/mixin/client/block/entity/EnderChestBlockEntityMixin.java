package einstein.subtle_effects.mixin.client.block.entity;

import einstein.subtle_effects.util.ChestAccessor;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderChestBlockEntity.class)
public class EnderChestBlockEntityMixin implements ChestAccessor {

    @Shadow
    @Final
    private ChestLidController chestLidController;

    @Override
    public ChestLidController subtleEffects$getLidController() {
        return chestLidController;
    }
}
