package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.npc.AbstractPitNPC;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;

public abstract class SkinNPC extends AbstractPitNPC {

    @Override
    public void initSkin(NPC npc) {
        final String skinValue = NewConfiguration.INSTANCE.getConfig().getString(this.getNpcInternalName() + "-npc-skin");
        if (skinValue != null) {
            String signature = NewConfiguration.INSTANCE
                    .getConfig()
                    .getString("not-netease-skins." + getNpcInternalName() + "-signature");
            npc.setSkin(
                    new Skin(skinValue, signature)
            );
        }
    }
}
