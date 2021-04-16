package com.thundersphun.hammers.util;

import com.thundersphun.hammers.Hammers;
import com.thundersphun.hammers.item.MiningHammer;
import com.thundersphun.hammers.mixin.PickaxeItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class AreaEffect {
	public static void useOnArea(@Nullable PlayerEntity player, World world, BlockPos pos, AreaEffectEvent event) {
		useOnArea(player, world, pos, Blocks.BARRIER.getDefaultState(), event);
	}

	public static void useOnArea(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, AreaEffectEvent event) {
		if (player == null) {
			return;
		}
		ItemStack hand = player.getStackInHand(Hand.MAIN_HAND);
		Item holding = hand.getItem();
		if (holding.isIn(Hammers.MINING_TAG) && holding instanceof MiningHammer) {
			Set<Block> effectiveBlocks = PickaxeItemAccessor.getEffectiveBlocks();
			Set<BlockPos> positions = MiningHammer.getTargetBlocks(world, pos, player, state);

			positions.forEach(e -> {
				BlockState s = world.getBlockState(e);
				if (s.getHardness(world, e) >= 0 && (effectiveBlocks.contains(s.getBlock()) || holding.isEffectiveOn(s))) {
					event.exec(e, hand);
				}
			});
		}
	}

	@FunctionalInterface
	public interface AreaEffectEvent {
		void exec(BlockPos pos, ItemStack hand);
	}
}
