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
            return ModConfigs.BLOCKS.eyeColors.get(endRemLoc(eyeType.toString()));
        }
        return null;
    }

    public static List<ResourceLocation> getAllEyes() {
        return Arrays.stream(ERFrameProperties.values()).map(eye -> endRemLoc(eye.toString())).toList();
    }
}
