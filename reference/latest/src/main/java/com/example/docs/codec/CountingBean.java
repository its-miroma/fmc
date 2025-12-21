package com.example.docs.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// #region TODO-give-me-a-name
// Another implementation
public class CountingBean implements Bean {
	public static final MapCodec<CountingBean> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.INT.fieldOf("counting_number").forGetter(CountingBean::getCountingNumber)
	).apply(instance, CountingBean::new));

	private int countingNumber;
	// #endregion TODO-give-me-a-name

	public CountingBean(int countingNumber) {
		this.countingNumber = countingNumber;
	}

	public int getCountingNumber() {
		return countingNumber;
	}

	// #region TODO-give-me-a-name

	@Override
	public BeanType<?> getType() {
		return BeanTypes.COUNTING_BEAN;
	}
}
// #endregion TODO-give-me-a-name
