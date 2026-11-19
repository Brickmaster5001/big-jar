package rs.doyoulikestai.bigjar.util;

import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.Random;

public class RNG {
    public static final Random RANDOM = new Random();

    public static Integer getIntFromRange(Integer min, Integer max) {
        return Math.round((RANDOM.nextFloat() * max) + min);
    }

    public static Vector3f getSafePointInAABB(AABB box) {
        double x = box.minX + (box.maxX - box.minX) * RANDOM.nextDouble();
        double y = box.minY + (box.maxY - box.minY) * RANDOM.nextDouble();
        double z = box.minZ + (box.maxZ - box.minZ) * RANDOM.nextDouble();
        return new Vector3f((float) x, (float) y, (float) z);
    }

}
