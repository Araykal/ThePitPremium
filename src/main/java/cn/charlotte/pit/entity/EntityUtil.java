package cn.charlotte.pit.entity;

import cn.charlotte.pit.entity.ai.PathfinderGoalFollow;
import cn.charlotte.pit.entity.ai.PathfinderGoalOwnerHurt;
import cn.charlotte.pit.entity.ai.PathfinderGoalTargetOwner;
import net.minecraft.server.v1_8_R3.*;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/11 15:53
 */
public class EntityUtil {
    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    public static void insertAi(CustomOwnableEntity entity, EntityPlayer player) {

        List goalB = (List) EntityUtil.getPrivateField("b", PathfinderGoalSelector.class, entity.getCreature().goalSelector);
        goalB.clear();
        List goalC = (List) EntityUtil.getPrivateField("c", PathfinderGoalSelector.class, entity.getCreature().goalSelector);
        goalC.clear();
        List targetB = (List) EntityUtil.getPrivateField("b", PathfinderGoalSelector.class, entity.getCreature().targetSelector);
        targetB.clear();
        List targetC = (List) EntityUtil.getPrivateField("c", PathfinderGoalSelector.class, entity.getCreature().targetSelector);
        targetC.clear();

//        entity.getCreature().getNavigation().a(true);
        entity.getCreature().goalSelector.a(1, new PathfinderGoalFloat(entity.getCreature()));
        entity.getCreature().goalSelector.a(3, new PathfinderGoalLeapAtTarget(entity.getCreature(), 0.4F));
        entity.getCreature().goalSelector.a(5, new PathfinderGoalFollow(entity, 1.0D, 10.0F, 2.0F));
        entity.getCreature().goalSelector.a(9, new PathfinderGoalLookAtPlayer(entity.getCreature(), EntityHuman.class, 8.0F));
        entity.getCreature().goalSelector.a(9, new PathfinderGoalRandomLookaround(entity.getCreature()));
        entity.getCreature().targetSelector.a(1, new PathfinderGoalTargetOwner(entity));
        entity.getCreature().targetSelector.a(2, new PathfinderGoalOwnerHurt(entity));

        entity.setOwner(player);
    }

}
