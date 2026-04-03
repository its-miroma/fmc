package com.example.docs.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// #region component
public record MyCustomComponent(float temperature, boolean burnt) {
	// #endregion component
	// #region codec
	public static final Codec<MyCustomComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			Codec.FLOAT.fieldOf("temperature").forGetter(MyCustomComponent::temperature),
			Codec.BOOL.optionalFieldOf("burnt", false).forGetter(MyCustomComponent::burnt)
		).apply(builder, MyCustomComponent::new);
	});
	// #endregion codec
	// #region component
}
// #endregion component
