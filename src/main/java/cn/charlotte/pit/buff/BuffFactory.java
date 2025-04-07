package cn.charlotte.pit.buff;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.command.util.ClassUtil;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/10 18:21
 */
//practicing and copying
@Getter
public class BuffFactory {
    private final List<AbstractPitBuff> buffs;

    public BuffFactory() {
        this.buffs = new ArrayList<>();
    }

    public AbstractPitBuff getBuffByInternalName(String internalName) {
        for (AbstractPitBuff buff : buffs) {
            if (buff.getInternalBuffName().equalsIgnoreCase(internalName)) {
                return buff;
            }
        }
        return null;
    }

    @SneakyThrows
    public void init() {
        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(ThePit.getInstance(), "cn.charlotte.pit.buff.impl");
        for (Class<?> clazz : classes) {
            if (AbstractPitBuff.class.isAssignableFrom(clazz)) {
                Object instance = clazz.newInstance();
                buffs.add((AbstractPitBuff) instance);
            }
        }
    }
}
