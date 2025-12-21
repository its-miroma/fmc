package com.example.docs.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import com.example.docs.ExampleMod;
import com.example.docs.item.ModItems;

// #region datagen-tags--provider
public class ExampleModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	// #endregion datagen-tags--provider
	// #region datagen-tags--tag-key
	public static final TagKey<Item> SMELLY_ITEMS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ExampleMod.MOD_ID, "smelly_items"));
	// #endregion datagen-tags--tag-key
	// #region datagen-tags--provider
	public ExampleModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// #endregion datagen-tags--provider
		// #region datagen-tags--build
		valueLookupBuilder(SMELLY_ITEMS)
				.add(Items.SLIME_BALL)
				.add(Items.ROTTEN_FLESH)
				.addOptionalTag(ItemTags.DIRT)
				.add(Items.OAK_PLANKS)
				.forceAddTag(ItemTags.BANNERS)
				.setReplace(true);
		// #endregion datagen-tags--build
		valueLookupBuilder(ItemTags.DYEABLE).add(ModItems.LEATHER_GLOVES);
		// #region datagen-tags--provider
	}
}
// #endregion datagen-tags--provider
