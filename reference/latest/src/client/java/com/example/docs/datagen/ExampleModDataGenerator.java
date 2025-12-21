package com.example.docs.datagen;

import static com.example.docs.datagen.ExampleModDamageTypesProvider.TATER_DAMAGE_TYPE;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import com.example.docs.appearance.ExampleModAppearanceModelProvider;
import com.example.docs.damage.ExampleModDamageTypes;
import com.example.docs.datagen.internal.ExampleModInternalModelProvider;
import com.example.docs.network.basic.ExampleModNetworkingBasicModelProvider;

// #region datagen-setup--generator
public class ExampleModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		// #endregion datagen-setup--generator
		// #region datagen-setup--pack
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		// #endregion datagen-setup--pack

		pack.addProvider(ExampleModEnchantmentGenerator::new);

		pack.addProvider(ExampleModAdvancementProvider::new);

		pack.addProvider(ExampleModEnglishLangProvider::new);

		pack.addProvider(ExampleModItemTagProvider::new);

		pack.addProvider(ExampleModRecipeProvider::new);

		pack.addProvider(ExampleModBlockLootTableProvider::new);
		pack.addProvider(ExampleModChestLootTableProvider::new);

		pack.addProvider(ExampleModDamageTypesProvider.TaterDamageTypesGenerator::new);
		pack.addProvider(ExampleModDamageTypesProvider.TaterDamageTypeTagGenerator::new);

		pack.addProvider(ExampleModInternalModelProvider::new);

		pack.addProvider(ExampleModModelProvider::new);

		pack.addProvider(ExampleModNetworkingBasicModelProvider::new);

		pack.addProvider(ExampleModAppearanceModelProvider::new);

		// #region datagen-setup--generator
	}

	// #endregion datagen-setup--generator
	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.DAMAGE_TYPE, registerable -> {
			registerable.register(ExampleModDamageTypes.TATER_DAMAGE, TATER_DAMAGE_TYPE);
		});
	}

	// #region datagen-setup--generator
}
// #endregion datagen-setup--generator
