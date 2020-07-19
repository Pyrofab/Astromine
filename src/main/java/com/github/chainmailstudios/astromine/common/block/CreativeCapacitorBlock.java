package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedHorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.CreativeCapacitorBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeCapacitorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CreativeCapacitorBlock extends DefaultedHorizontalFacingBlockWithEntity {
	public CreativeCapacitorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CreativeCapacitorBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && !(player.getStackInHand(hand).getItem() instanceof BucketItem)) {
			player.openHandledScreen(state.createScreenHandlerFactory(world, blockPos));
			return ActionResult.CONSUME;
		} else if (player.getStackInHand(hand).getItem() instanceof BucketItem) {
			return super.onUse(state, world, blockPos, player, hand, hit);
		} else {
			return ActionResult.SUCCESS;
		}
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buffer) {
				buffer.writeBlockPos(pos);
			}

			@Override
			public Text getDisplayName() {
				return new TranslatableText(getTranslationKey());
			}

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new CreativeCapacitorScreenHandler(syncId, playerInventory, pos);
			}
		};
	}
}
