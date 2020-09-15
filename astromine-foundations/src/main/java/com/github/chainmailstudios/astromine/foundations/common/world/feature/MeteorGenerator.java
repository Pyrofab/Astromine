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

package com.github.chainmailstudios.astromine.foundations.common.world.feature;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFeatures;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.api.Quaternion;
import com.terraformersmc.shapes.api.Shape;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MeteorGenerator extends ScatteredStructurePiece {

	private static OpenSimplexNoise noise;

	public MeteorGenerator(Random random, int x, int z) {
		super(AstromineFoundationsFeatures.METEOR_STRUCTURE, random, x, 64, z, 16, 16, 16);
	}

	public MeteorGenerator(TemplateManager manager, CompoundNBT tag) {
		super(AstromineFoundationsFeatures.METEOR_STRUCTURE, tag);
	}

	public static void buildSphere(ISeedReader world, BlockPos originPos, int radius, BlockState state) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));

					// place blocks within spherical radius
					if (distance <= radius - ((radius * 1f / 3f) * noise.sample((originPos.getX() + x) / 10f, (originPos.getY() + y) / 10f, (originPos.getZ() + z) / 10f))) {
						world.setBlock(originPos.offset(x, y, z), state, 3);
					}
				}
			}
		}
	}

	@Override
	public boolean postProcess(ISeedReader world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		return generate(world, chunkPos, random, blockPos);
	}

	public boolean generate(ISeedReader world, ChunkPos chunkPos, Random random, BlockPos blockPos) {
		if (!world.getLevel().dimension().equals(World.OVERWORLD))
			return false;
		noise = new OpenSimplexNoise(world.getSeed());
		BlockPos originPos = world.getHeightmapPos(Heightmap.Type.OCEAN_FLOOR_WG, new BlockPos(chunkPos.getMinBlockX() + 8, 0, chunkPos.getMinBlockZ() + 8));
		originPos = emptySphere(world, originPos, 16, state -> {
			if (world.getRandom().nextInt(10) == 0) {
				return Blocks.FIRE.defaultBlockState();
			} else {
				return Blocks.AIR.defaultBlockState();
			}
		}, state -> Blocks.COBBLESTONE.defaultBlockState());
		buildSphere(world, originPos, 8, AstromineFoundationsBlocks.METEOR_STONE.defaultBlockState());

		Shape vein = Shapes.ellipsoid((float) 4, (float) 4, (float) 4).applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true))).applyLayer(TranslateLayer.of(Position.of(originPos.getX(), originPos.getY(), originPos.getZ())));

		Block metiteOre = Registry.BLOCK.getOptional(AstromineCommon.identifier("meteor_metite_ore")).orElse(null);
		if (metiteOre != null) {
			for (Position streamPosition : vein.stream().collect(Collectors.toSet())) {
				BlockPos orePosition = new BlockPos(streamPosition.getX(), streamPosition.getY(), streamPosition.getZ());

				if (world.getBlockState(orePosition).getBlock() == AstromineFoundationsBlocks.METEOR_STONE) {
					world.setBlock(orePosition, metiteOre.defaultBlockState(), 0b0110100);
				}
			}
		}

		return true;
	}

	private BlockPos emptySphere(ISeedReader world, BlockPos originPos, int radius, GroundManipulator bottom, GroundManipulator underneath) {
		boolean hasWater = false;
		List<BlockPos> placedPositions = new ArrayList<>();

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y * 1.3, 2));

					// place blocks within spherical radius
					if (distance <= radius + (5 * noise.sample((originPos.getX() + x) / 10f, (originPos.getZ() + z) / 10f))) {
						BlockPos offsetPos = originPos.offset(x, y, z);
						if (!hasWater && world.getFluidState(offsetPos).getType().isSame(Fluids.WATER)) {
							hasWater = true;
						}

						world.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 3);

						placedPositions.add(offsetPos);
					}
				}
			}
		}

		for (BlockPos placedPosition : placedPositions) {
			world.setBlock(placedPosition, hasWater && placedPosition.getY() < world.getSeaLevel() ? Fluids.WATER.getSource().defaultFluidState().createLegacyBlock() : Blocks.AIR.defaultBlockState(), 3);
		}

		List<BlockPos> bottomPositions = new ArrayList<>();
		List<BlockPos> underneathPositions = new ArrayList<>();

		for (BlockPos pos : placedPositions) {
			// store bottom block
			if (world.getBlockState(pos).isAir() && world.getBlockState(pos.below()).isRedstoneConductor(world, pos)) {
				bottomPositions.add(pos);
				underneathPositions.add(pos.below());
			}
		}

		for (BlockPos pos : bottomPositions) {
			world.setBlock(pos, hasWater && pos.getY() < world.getSeaLevel() ? Fluids.WATER.getSource().defaultFluidState().createLegacyBlock() : world.getRandom().nextInt(10) == 0 ? Blocks.FIRE.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
		}

		for (BlockPos pos : underneathPositions) {
			world.setBlock(pos, underneath.manipulate(world.getBlockState(pos)), 3);
		}

		return placedPositions.stream().filter(pos -> pos.getX() == originPos.getX() && pos.getZ() == originPos.getZ()).min(Comparator.comparingInt(Vector3i::getY)).orElse(originPos).relative(Direction.DOWN);
	}

	@FunctionalInterface
	public interface GroundManipulator {
		BlockState manipulate(BlockState state);
	}
}
