package com.example.docs.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.example.docs.block.entity.ModBlockEntities;

// #region 1
public class CounterBlockEntity extends BlockEntity {
	// #endregion 1

	// #region 2
	private int clicks = 0;
	// #endregion 2

	private int ticksSinceLast = 0;

	// #region 1
	public CounterBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.COUNTER_BLOCK_ENTITY, pos, state);
	}

	// #endregion 1

	// #region 2
	public int getClicks() {
		return clicks;
	}

	public void incrementClicks() {
		// #endregion 2

		// #region 6
		if (ticksSinceLast < 10) return;
		ticksSinceLast = 0;
		// #endregion 6

		// #region 2
		clicks++;
		setChanged();
	}

	// #endregion 2

	// #region 3
	@Override
	protected void saveAdditional(ValueOutput writeView) {
		writeView.putInt("clicks", clicks);

		super.saveAdditional(writeView);
	}

	// #endregion 3

	// #region 4
	@Override
	protected void loadAdditional(ValueInput readView) {
		super.loadAdditional(readView);

		clicks = readView.getIntOr("clicks", 0);
	}

	// #endregion 4

	// #region 5
	public static void tick(Level world, BlockPos blockPos, BlockState blockState, CounterBlockEntity entity) {
		entity.ticksSinceLast++;
	}

	// #endregion 5

	// #region 7
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}

	// #endregion 7

	// #region 1
}
// #endregion 1
