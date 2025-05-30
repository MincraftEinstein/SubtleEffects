package einstein.subtle_effects.compat;

import com.teamremastered.endrem.blocks.AncientPortalFrame;
import com.teamremastered.endrem.blocks.ERFrameProperties;
import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static einstein.subtle_effects.compat.CompatHelper.endRemLoc;

public class EndRemasteredCompat {

    @Nullable
    public static ValidatedColor.ColorHolder getEyeColor(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(AncientPortalFrame.EYE)) {
            ERFrameProperties eyeType = state.getValue(AncientPortalFrame.EYE);
            if (eyeType != ERFrameProperties.EMPTY) {
                return ModConfigs.BLOCKS.eyeColors.get(endRemLoc(eyeType.getSerializedName()));
            }
        }
        return null;
    }

    public static List<ResourceLocation> getAllEyes() {
        return Arrays.stream(ERFrameProperties.values())
                .filter(eye -> eye != ERFrameProperties.EMPTY)
                .map(eye -> endRemLoc(eye.getSerializedName()))
                .toList();
    }
}
