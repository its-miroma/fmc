package com.example.docs.item;

import java.util.List;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

import com.example.docs.ExampleMod;
import com.example.docs.block.ModBlocks;
import com.example.docs.component.ModComponents;
import com.example.docs.entity.ModEntityTypes;
import com.example.docs.item.armor.GuiditeArmorMaterial;
import com.example.docs.item.custom.CounterItem;
import com.example.docs.item.custom.LightningStick;

// #region mod-items-class
public class ModItems {
	// #endregion mod-items-class

	// #region guidite-tool-material
	public static final ToolMaterial GUIDITE_TOOL_MATERIAL = new ToolMaterial(
			BlockTags.INCORRECT_FOR_WOODEN_TOOL,
			455,
			5.0F,
			1.5F,
			22,
			GuiditeArmorMaterial.REPAIRS_GUIDITE_ARMOR
	);
	// #endregion guidite-tool-material

	// #region create-armor-items
	public static final Item GUIDITE_HELMET = register(
			"guidite_helmet",
			Item::new,
			new Item.Properties().humanoidArmor(GuiditeArmorMaterial.INSTANCE, ArmorType.HELMET)
					.durability(ArmorType.HELMET.getDurability(GuiditeArmorMaterial.BASE_DURABILITY))
	);
	public static final Item GUIDITE_CHESTPLATE = register("guidite_chestplate",
			Item::new,
			new Item.Properties().humanoidArmor(GuiditeArmorMaterial.INSTANCE, ArmorType.CHESTPLATE)
					.durability(ArmorType.CHESTPLATE.getDurability(GuiditeArmorMaterial.BASE_DURABILITY))
	);

	public static final Item GUIDITE_LEGGINGS = register(
			"guidite_leggings",
			Item::new,
			new Item.Properties().humanoidArmor(GuiditeArmorMaterial.INSTANCE, ArmorType.LEGGINGS)
					.durability(ArmorType.LEGGINGS.getDurability(GuiditeArmorMaterial.BASE_DURABILITY))
	);

	public static final Item GUIDITE_BOOTS = register(
			"guidite_boots",
			Item::new,
			new Item.Properties().humanoidArmor(GuiditeArmorMaterial.INSTANCE, ArmorType.BOOTS)
					.durability(ArmorType.BOOTS.getDurability(GuiditeArmorMaterial.BASE_DURABILITY))
	);
	// #endregion create-armor-items
	public static final Item LIGHTNING_STICK = register("lightning_stick", LightningStick::new, new Item.Properties());
	// #region guidite-sword
	public static final Item GUIDITE_SWORD = register(
			"guidite_sword",
			Item::new,
			new Item.Properties().sword(GUIDITE_TOOL_MATERIAL, 1f, 1f)
	);
	// #endregion guidite-sword
	// #region counter
	public static final Item COUNTER = register(
			"counter",
			CounterItem::new,
			new Item.Properties()
					// Initialize the click count component with a default value of 0
					.component(ModComponents.CLICK_COUNT_COMPONENT, 0)
	);
	// #endregion counter
	// #region custom-creative-tab
	public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(ExampleMod.MOD_ID, "creative_tab"));
	public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModItems.GUIDITE_SWORD))
			.title(Component.translatable("itemGroup.example-mod"))
			.displayItems((params, output) -> {
				output.accept(ModItems.SUSPICIOUS_SUBSTANCE);
				output.accept(ModItems.POISONOUS_APPLE);
				// #endregion custom-creative-tab
				output.accept(ModItems.GUIDITE_SWORD);
				output.accept(ModItems.GUIDITE_HELMET);
				output.accept(ModItems.GUIDITE_BOOTS);
				output.accept(ModItems.GUIDITE_LEGGINGS);
				output.accept(ModItems.GUIDITE_CHESTPLATE);
				output.accept(ModItems.LIGHTNING_STICK);
				// #region custom-creative-tab

				// The tab builder also accepts Blocks
				output.accept(ModBlocks.CONDENSED_OAK_LOG);
				output.accept(ModBlocks.PRISMARINE_LAMP);
				// #endregion custom-creative-tab
				output.accept(ModBlocks.COUNTER_BLOCK);
				output.accept(ModBlocks.ENGINE_BLOCK);
				output.accept(ModBlocks.RUBY_BLOCK);
				output.accept(ModBlocks.RUBY_STAIRS);
				output.accept(ModBlocks.RUBY_SLAB);
				output.accept(ModBlocks.RUBY_FENCE);
				output.accept(ModBlocks.RUBY_DOOR);
				output.accept(ModBlocks.RUBY_TRAPDOOR);
				output.accept(ModBlocks.VERTICAL_OAK_LOG_SLAB);
				// #region custom-creative-tab

				// And custom ItemStacks
				ItemStack stack = new ItemStack(Items.SEA_PICKLE);
				stack.set(DataComponents.ITEM_NAME, Component.literal("Pickle Rick"));
				stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("I'm pickle riiick!!"))));
				output.accept(stack);
			})
			.build();
	// #endregion custom-creative-tab
	// #region custom-food
	public static final Consumable POISON_FOOD_CONSUMABLE_COMPONENT = Consumables.defaultFood()
			// The duration is in ticks, 20 ticks = 1 second
			.onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 6 * 20, 1), 1.0f))
			.build();
	public static final FoodProperties POISON_FOOD_COMPONENT = new FoodProperties.Builder()
			.alwaysEdible()
			.build();
	// #endregion custom-food

	// #region poisonous-apple
	public static final Item POISONOUS_APPLE = register(
			"poisonous_apple",
			Item::new,
			new Item.Properties().food(POISON_FOOD_COMPONENT, POISON_FOOD_CONSUMABLE_COMPONENT)
	);
	// #endregion poisonous-apple

	// #region suspicious_substance
	public static final Item SUSPICIOUS_SUBSTANCE = register("suspicious_substance", Item::new, new Item.Properties());
	// #endregion suspicious_substance

	// #region custom-entity-spawn-egg
	public static final Item MINI_GOLEM_SPAWN_EGG = register(
			"mini_golem_spawn_egg",
					SpawnEggItem::new,
			new Item.Properties().spawnEgg(ModEntityTypes.MINI_GOLEM)
	);
	// #endregion custom-entity-spawn-egg

	public static final Item RUBY = register("ruby", Item::new, new Item.Properties());

	public static final Item GUIDITE_AXE = register("guidite_axe", settings -> new AxeItem(GUIDITE_TOOL_MATERIAL, 5.0F, -3.0F, settings), new Item.Properties());

	public static final Item LEATHER_GLOVES = register("leather_gloves", Item::new, new Item.Properties());

	public static final Item FLASHLIGHT = register("flashlight", settings -> new Item(settings) {
		@Override
		public InteractionResult use(Level level, Player user, InteractionHand hand) {
			user.startUsingItem(hand);
			return InteractionResult.CONSUME;
		}
	}, new Item.Properties());

	public static final Item BALLOON = register("balloon", Item::new, new Item.Properties());

	public static final Item ENHANCED_HOE = register("enhanced_hoe", settings -> new HoeItem(GUIDITE_TOOL_MATERIAL, -4.0F, 0.0F, settings), new Item.Properties());

	public static final Item DIMENSIONAL_CRYSTAL = register("dimensional_crystal", Item::new, new Item.Properties());

	public static final Item THROWING_KNIVES = register("throwing_knives", Item::new, new Item.Properties().stacksTo(3));

	// #region mod-items-class
	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(ExampleMod.MOD_ID, name));

		// Create the item instance.
		T item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

	// #endregion mod-items-class

	// #region initialize
	public static void initialize() {
		// #endregion initialize
		// #region add-to-creative-tab
		// Get the event for modifying entries in the ingredients group.
		// And register an event handler that adds our suspicious item to the ingredients group.
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
				.register((itemGroup) -> itemGroup.accept(ModItems.SUSPICIOUS_SUBSTANCE));
		// #endregion add-to-creative-tab

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
				.register((itemGroup) -> {
					itemGroup.accept(ModItems.GUIDITE_HELMET);
					itemGroup.accept(ModItems.GUIDITE_BOOTS);
					itemGroup.accept(ModItems.GUIDITE_LEGGINGS);
					itemGroup.accept(ModItems.GUIDITE_CHESTPLATE);
				});

		// #region add-guidite-sword-to-create-tab
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
				.register((itemGroup) -> itemGroup.accept(ModItems.GUIDITE_SWORD));
		// #endregion add-guidite-sword-to-create-tab

		// #region register-creative-tab
		// Register the group.
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
		// #endregion register-creative-tab

		// #region spawn-egg-creative-tab
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(itemGroup -> {
			itemGroup.accept(ModItems.MINI_GOLEM_SPAWN_EGG);
		});
		// #endregion spawn-egg-creative-tab
		ItemGroupEvents.modifyEntriesEvent(CUSTOM_CREATIVE_TAB_KEY).register(itemGroup -> {
			itemGroup.accept(ModItems.RUBY);
			itemGroup.accept(ModItems.GUIDITE_AXE);
			itemGroup.accept(ModItems.LEATHER_GLOVES);
			itemGroup.accept(ModItems.FLASHLIGHT);
			itemGroup.accept(ModItems.BALLOON);
			itemGroup.accept(ModItems.ENHANCED_HOE);
			itemGroup.accept(ModItems.DIMENSIONAL_CRYSTAL);
			itemGroup.accept(ModItems.THROWING_KNIVES);
		});

		// #region compostable-item
		// Add the suspicious substance to the composting registry with a 30% chance of increasing the composter's level.
		CompostingChanceRegistry.INSTANCE.add(ModItems.SUSPICIOUS_SUBSTANCE, 0.3f);
		// #endregion compostable-item

		// #region fuel-item
		// Add the suspicious substance to the registry of fuels, with a burn time of 30 seconds.
		// Remember, Minecraft deals with logical based-time using ticks.
		// 20 ticks = 1 second.
		FuelRegistryEvents.BUILD.register((builder, context) -> {
			builder.add(ModItems.SUSPICIOUS_SUBSTANCE, 30 * 20);
		});
		// #endregion fuel-item
		// #region 3
	}
	// #endregion 3
	// #region mod-items-class
}
// #endregion mod-items-class
