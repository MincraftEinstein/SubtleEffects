package einstein.subtle_effects.compat;

import com.teamremastered.endrem.blocks.AncientPortalFrame;
import com.teamremastered.endrem.blocks.ERFrameProperties;
import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static einstein.subtle_effects.compat.CompatHelper.endRemLoc;
import static einstein.subtle_effects.init.ModConfigs.BLOCKS;

public class EndRemasteredCompat {

    public static void init() {
        ModBlockTickers.REGISTERED_SPECIAL.put(state -> state.is(BuiltInRegistries.BLOCK.get(CompatHelper.endRemLoc("ancient_portal_frame"))), (state, level, pos, random) -> {
            if (BLOCKS.endPortalFrameParticlesDisplayType == ModBlockConfigs.EndPortalFrameParticlesDisplayType.OFF) {
                return;
            }

            if (state.hasProperty(AncientPortalFrame.EYE)) {
                if (state.getValue(AncientPortalFrame.EYE) != ERFrameProperties.EMPTY) {
                    return;
                }

                ParticleSpawnUtil.spawnEndPortalParticles(level, pos, random, BLOCKS.endPortalFrameParticlesDisplayType.particle.apply(level, pos), BLOCKS.endPortalFrameParticlesDisplayType.count);
            }
        });
    }

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
