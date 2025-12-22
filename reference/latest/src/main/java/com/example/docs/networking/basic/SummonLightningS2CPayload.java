package com.example.docs.networking.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.example.docs.ExampleMod;

// #region summon-lightning-payload
public record SummonLightningS2CPayload(BlockPos pos) implements CustomPacketPayload {
	// #region resource-location
	public static final ResourceLocation SUMMON_LIGHTNING_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(ExampleMod.MOD_ID, "summon_lightning");
	// #endregion resource-location

	// #region payload-id
	public static final CustomPacketPayload.Type<SummonLightningS2CPayload> ID = new CustomPacketPayload.Type<>(SUMMON_LIGHTNING_PAYLOAD_ID);
	// #endregion payload-id

	// #region stream-codec
	public static final StreamCodec<RegistryFriendlyByteBuf, SummonLightningS2CPayload> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SummonLightningS2CPayload::pos, SummonLightningS2CPayload::new);
	// #endregion stream-codec

	// #region type
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	// #endregion type
}
// #endregion summon-lightning-payload
