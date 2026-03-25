package com.example.docs.item;

import net.fabricmc.api.ModInitializer;

// #region 1
public class ExampleModItems implements ModInitializer {
	@Override
	public void onInitialize() {
		ModItems.initialize();
	}
}
// #endregion 1
