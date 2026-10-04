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
//        return JsonEye.getEyes().stream().map(eye -> endRemLoc(getId(eye))).toList();
        return new ArrayList<>();
    }

    // As of writing this, the "getId" method has been changed to a string
    // on NeoForge, however, the Fabric version hasn't been updated yet and
    // still returns a ResourceLocation
//    private static String getId(JsonEye eye) {
//        Object id = eye.getID();
//        if (id instanceof String s) {
//            return s;
//        }
//        else if (id instanceof ResourceLocation loc) {
//            return loc.getPath();
//        }
//        throw new IllegalStateException();
//    }
}
