package com.example.docs.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

// #region 1
public class PrismarineLampBlock extends Block {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	// #endregion 1
	// #region 3
	public PrismarineLampBlock(Properties settings) {
		super(settings);

		// Set the default state of the block to be deactivated.
		registerDefaultState(defaultBlockState().setValue(ACTIVATED, false));
	}

	// #endregion 3
	// #region 2
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED);
	}

	// #endregion 2
	// #region 4
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!player.getAbilities().mayBuild) {
			// Skip if the player isn't allowed to modify the world.
			return InteractionResult.PASS;
		} else {
			// Get the current value of the "activated" property
			boolean activated = state.getValue(ACTIVATED);

			// Flip the value of activated and save the new blockstate.
			world.setBlockAndUpdate(pos, state.setValue(ACTIVATED, !activated));

			// Play a click sound to emphasise the interaction.
			world.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);

			return InteractionResult.SUCCESS;
		}
	}

	// #endregion 4
	// #region 5
	public static int getLuminance(BlockState currentBlockState) {
		// Get the value of the "activated" property.
		boolean activated = currentBlockState.getValue(PrismarineLampBlock.ACTIVATED);

		// Return a light level if activated = true
		return activated ? 15 : 0;
	}

	// #endregion 5
	// #region 1
}
// #endregion 1
