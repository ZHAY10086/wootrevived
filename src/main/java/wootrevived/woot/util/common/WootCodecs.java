package wootrevived.woot.util.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public class WootCodecs {
    public static final Codec<Float> NON_NEGATIVE_FLOAT = floatRangeWithMessage(
            0.0F, Float.MAX_VALUE, f -> "Value must be non-negative: " + f
    );

    private static Codec<Float> floatRangeWithMessage(float min, float max, Function<Float, String> msg) {
        return ExtraCodecs.validate(
                Codec.FLOAT,
                f -> f.compareTo(min) >= 0 && f.compareTo(max) <= 0
                        ? DataResult.success(f)
                        : DataResult.error(() -> msg.apply(f))
        );
    }
}
