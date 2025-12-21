package com.example.docs.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// #region 1
public record MyCustomComponent(float temperature, boolean burnt) {
	// #endregion 1
	// #region 2
	public static final Codec<MyCustomComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			Codec.FLOAT.fieldOf("temperature").forGetter(MyCustomComponent::temperature),
			Codec.BOOL.optionalFieldOf("burnt", false).forGetter(MyCustomComponent::burnt)
		).apply(builder, MyCustomComponent::new);
	});
	// #endregion 2
	// #region 1
}
// #endregion 1
