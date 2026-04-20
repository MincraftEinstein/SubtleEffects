package einstein.subtle_effects.compat;

import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EndRemasteredCompat {
    // TODO fix when it matters

    public static void init() {
//        ModBlockTickers.REGISTERED_SPECIAL.put(state -> state.is(CommonBlockRegistry.ANCIENT_PORTAL_FRAME), (state, level, pos, random) -> {
//            if (BLOCKS.endPortalFrameParticlesDisplayType == ModBlockConfigs.EndPortalFrameParticlesDisplayType.OFF) {
//                return;
//            }
//
//            BlockEntity blockEntity = level.getBlockEntity(pos);
//            if (blockEntity instanceof AncientPortalFrameEntity frameBlockEntity) {
//                if (frameBlockEntity.isEmpty()) {
//                    return;
//                }
//
//                ParticleSpawnUtil.spawnEndPortalParticles(level, pos, random, BLOCKS.endPortalFrameParticlesDisplayType.particle.apply(level, pos), BLOCKS.endPortalFrameParticlesDisplayType.count);
//            }
//        });
    }

    @Nullable
    public static ValidatedColor.ColorHolder getEyeColor(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
//        if (blockEntity instanceof AncientPortalFrameEntity frameBlockEntity) {
//            return BLOCKS.eyeColors.get(frameBlockEntity.getEyeID());
//        }
        return null;
    }

    public static List<Identifier> getAllEyes() {
//        return JsonEye.getEyes().stream().map(eye -> endRemLoc(eye.getID()).getPath()).toList();
        return new ArrayList<>();
    }
}
