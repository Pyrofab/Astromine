/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.item.*;
import com.github.chainmailstudios.astromine.common.item.base.EnergyVolumeItem;
import com.github.chainmailstudios.astromine.common.item.base.FluidVolumeItem;
import com.github.chainmailstudios.astromine.common.item.weapon.GravityGauntletItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class AstromineItems {
	// Things
	public static final Item ENERGY = register("energy", new Item(new Item.Settings()));
	public static final Item FLUID = register("fluid", new Item(new Item.Settings()));
	public static final Item ITEM = register("item", new Item(new Item.Settings()));

	// Spawn eggs
	public static final Item SPACE_SLIME_SPAWN_EGG = register("space_slime_spawn_egg", new UncoloredSpawnEggItem(AstromineEntityTypes.SPACE_SLIME, getBasicSettings()));
	public static final Item ROCKET = register("rocket", new UncoloredSpawnEggItem(AstromineEntityTypes.ROCKET, getBasicSettings()));

	// Misc materials
	public static final Item SPACE_SLIME_BALL = register("space_slime_ball", new Item(getBasicSettings()));
	public static final Item YEAST = register("yeast", new Item(getBasicSettings()));
	public static final Item GRAPHITE_SHEET = register("graphite_sheet", new Item(getBasicSettings()));

	// Fantasy weaponry
	public static final Item GRAVITY_GAUNTLET = register("gravity_gauntlet", new GravityGauntletItem(getBasicSettings().maxCount(1), AstromineConfig.get().gravityGauntletEnergy));

	// Realistic tooling
	public static final Item FIRE_EXTINGUISHER = register("fire_extinguisher", new FireExtinguisherItem(getBasicSettings().maxCount(1)));

	// Circuits
	public static final Item BASIC_CIRCUIT = register("basic_circuit", new Item(getBasicSettings()));
	public static final Item ADVANCED_CIRCUIT = register("advanced_circuit", new Item(getBasicSettings()));
	public static final Item ELITE_CIRCUIT = register("elite_circuit", new Item(getBasicSettings()));

	// Machine Chassis
	public static final Item PRIMITIVE_MACHINE_CHASSIS = register("primitive_machine_chassis", new Item(getBasicSettings()));
	public static final Item BASIC_MACHINE_CHASSIS = register("basic_machine_chassis", new Item(getBasicSettings()));
	public static final Item ADVANCED_MACHINE_CHASSIS = register("advanced_machine_chassis", new Item(getBasicSettings()));
	public static final Item ELITE_MACHINE_CHASSIS = register("elite_machine_chassis", new Item(getBasicSettings()));

	// Gas Canisters
	public static final Item GAS_CANISTER = register("gas_canister", FluidVolumeItem.of(getBasicSettings(), Fraction.of(8, 1)));
	public static final Item PRESSURIZED_GAS_CANISTER = register("pressurized_gas_canister", FluidVolumeItem.of(getBasicSettings(), Fraction.of(32, 1)));

	// Batteries
	public static final Item BASIC_BATTERY = register("basic_battery", EnergyVolumeItem.of(getBasicSettings(), 9000));
	public static final Item ADVANCED_BATTERY = register("advanced_battery", EnergyVolumeItem.of(getBasicSettings(), 24000));
	public static final Item ELITE_BATTERY = register("elite_battery", EnergyVolumeItem.of(getBasicSettings(), 64000));
	public static final Item CREATIVE_BATTERY = register("creative_battery", EnergyVolumeItem.ofCreative(getBasicSettings()));

	// Misc Tools
	public static final Item HOLOGRAPHIC_CONNECTOR = register("holographic_connector", new HolographicConnectorItem(getBasicSettings().maxCount(1)));
	public static final Item BRONZE_WRENCH = register("bronze_wrench", new WrenchItem(getBasicSettings()));

	// Drills
	public static final Item BASIC_DRILL = register("basic_drill", new DrillItem(AstromineToolMaterials.BASIC_DRILL, 1, -2.8F, 1, 90000, getBasicSettings().maxCount(1)));
	public static final Item ADVANCED_DRILL = register("advanced_drill", new DrillItem(AstromineToolMaterials.ADVANCED_DRILL, 1, -2.8F, 1, 240000, getBasicSettings().maxCount(1)));
	public static final Item ELITE_DRILL = register("elite_drill", new DrillItem(AstromineToolMaterials.ELITE_DRILL, 1, -2.8F, 1, 640000, getBasicSettings().maxCount(1)));

	public static void initialize() {
		for (UncoloredSpawnEggItem spawnEggItem : UncoloredSpawnEggItem.getAll()) {
			DispenserBlock.registerBehavior(spawnEggItem, new ItemDispenserBehavior() {
				@Override
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
					EntityType<?> entityType = ((UncoloredSpawnEggItem) stack.getItem()).getEntityType(stack.getTag());
					entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
					stack.decrement(1);
					return stack;
				}
			});
		}

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			Registry.register(Registry.ITEM, AstromineCommon.identifier("meteor_spawner"), new MeteorSpawnerDevItem(new Item.Settings()));
		}
	}

	/**
	 * @param name
	 *        Name of item instance to be registered
	 * @param item
	 *        Item instance to be registered
	 * @return Item instanced registered
	 */
	public static <T extends Item> T register(String name, T item) {
		return register(AstromineCommon.identifier(name), item);
	}

	/**
	 * @param name
	 *        Identifier of item instance to be registered
	 * @param item
	 *        Item instance to be registered
	 * @return Item instance registered
	 */
	public static <T extends Item> T register(Identifier name, T item) {
		return Registry.register(Registry.ITEM, name, item);
	}

	public static Item.Settings getBasicSettings() {
		return new Item.Settings().group(AstromineItemGroups.ASTROMINE);
	}
}
