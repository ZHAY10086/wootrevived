package wootrevived.woot.util.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class SerializeEntityNBTHelper {
    public static CompoundTag serialize(Entity entity){
        CompoundTag tag = new CompoundTag();
        tag.putString("id", entity.getEncodeId());
        try {
            tag = entity.saveWithoutId(tag);
        } catch(Exception ignored){}
        return tag;
    }
}
