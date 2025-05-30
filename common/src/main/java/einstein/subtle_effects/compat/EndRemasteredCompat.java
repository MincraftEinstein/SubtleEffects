package einstein.subtle_effects.compat;

import com.teamremastered.endrem.block.AncientPortalFrameEntity;
import com.teamremastered.endrem.item.JsonEye;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static einstein.subtle_effects.compat.CompatHelper.endRemLoc;

public class EndRemasteredCompat {

    @Nullable
    public static ValidatedColor.ColorHolder getEyeColor(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AncientPortalFrameEntity frameBlockEntity) {
            String eyeType = frameBlockEntity.getEye();
            return ModConfigs.BLOCKS.eyeColors.get(endRemLoc(eyeType));
        }
        return null;
    }

    public static List<ResourceLocation> getAllEyes() {
        return JsonEye.getEyes().stream().map(eye -> endRemLoc(eye.getID().getPath())).toList();
    }
}
