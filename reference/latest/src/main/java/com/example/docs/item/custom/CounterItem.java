package com.example.docs.item.custom;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import com.example.docs.component.ModComponents;

// #region 1
public class CounterItem extends Item {
	public CounterItem(Properties settings) {
		super(settings);
	}

	// #endregion 1

	@Override
	// #region 2
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);

		// Don't do anything on the client
		if (world.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		// Read the current count and increase it by one
		int count = stack.getOrDefault(ModComponents.CLICK_COUNT_COMPONENT, 0);
		stack.set(ModComponents.CLICK_COUNT_COMPONENT, ++count);

		return InteractionResult.SUCCESS;
	}

	// #endregion 2

	@Override
	// #region 3
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
		if (stack.has(ModComponents.CLICK_COUNT_COMPONENT)) {
			int count = stack.get(ModComponents.CLICK_COUNT_COMPONENT);
			textConsumer.accept(Component.translatable("item.example-mod.counter.info", count).withStyle(ChatFormatting.GOLD));
		}
	}

	// #endregion 3

	// #region 1
}
// #endregion 1
