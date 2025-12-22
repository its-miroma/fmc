package com.example.docs.networking.basic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

// #region lightning-tater-item
public class LightningTaterItem extends Item {
	public LightningTaterItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
		// #region client-check
		if (world.isClientSide()) {
			return InteractionResult.PASS;
		}
		// #endregion client-check

		// #region payload-instance
		SummonLightningS2CPayload payload = new SummonLightningS2CPayload(user.blockPosition());
		// #endregion payload-instance

		// #region lookup
		for (ServerPlayer player : PlayerLookup.world((ServerLevel) world)) {
			ServerPlayNetworking.send(player, payload);
		}
		// #endregion lookup

		return InteractionResult.SUCCESS;
	}
}
// #endregion lightning-tater-item
