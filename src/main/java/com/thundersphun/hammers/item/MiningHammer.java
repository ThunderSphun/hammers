package com.thundersphun.hammers.item;

import com.google.common.collect.ImmutableSet;
import com.thundersphun.hammers.mixin.PickaxeItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Set;

public class MiningHammer extends MiningToolItem {
	public MiningHammer(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
		super(attackDamage, attackSpeed, material, PickaxeItemAccessor.getEffectiveBlocks(), settings);
	}

	public static Set<BlockPos> getTargetBlocks(World world, BlockPos pos, PlayerEntity player, BlockState state) {
		boolean isAir = world.getBlockState(pos).isAir();
		if (isAir) world.setBlockState(pos, state);
		BlockHitResult hit = raycast(world, player, RaycastContext.FluidHandling.NONE);
		if (isAir) world.setBlockState(pos, Blocks.AIR.getDefaultState());
		return getTargetBlocks(pos, hit);
	}

	public static Set<BlockPos> getTargetBlocks(BlockPos pos, BlockHitResult hit) {
		switch (hit.getSide()) {
			case DOWN:
			case UP:
				return ImmutableSet.of(pos.north(), pos.south(), pos.east(), pos.west());
			case NORTH:
			case SOUTH:
				return ImmutableSet.of(pos.up(), pos.down(), pos.east(), pos.west());
			case WEST:
			case EAST:
				return ImmutableSet.of(pos.up(), pos.down(), pos.north(), pos.south());
		}
		return ImmutableSet.of();
	}

	/**
	 * copied from {@link net.minecraft.item.PickaxeItem#isEffectiveOn(BlockState)}
	 */
	@Override
	public boolean isEffectiveOn(BlockState state) {
		int miningLevel = this.getMaterial().getMiningLevel();
		if ((state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.CRYING_OBSIDIAN)) || state.isOf(Blocks.NETHERITE_BLOCK) ||
				state.isOf(Blocks.RESPAWN_ANCHOR) || state.isOf(Blocks.ANCIENT_DEBRIS)) {
			return miningLevel >= 3;
		}
		if (state.isOf(Blocks.DIAMOND_BLOCK) || state.isOf(Blocks.DIAMOND_ORE) || state.isOf(Blocks.EMERALD_ORE) ||
				state.isOf(Blocks.EMERALD_BLOCK) || state.isOf(Blocks.GOLD_BLOCK) || state.isOf(Blocks.GOLD_ORE) || state.isOf(Blocks.REDSTONE_ORE)) {
			return miningLevel >= 2;
		}
		if (state.isOf(Blocks.IRON_BLOCK) || state.isOf(Blocks.IRON_ORE) || state.isOf(Blocks.LAPIS_BLOCK) || state.isOf(Blocks.LAPIS_ORE)) {
			return miningLevel >= 1;
		}
		Material material = state.getMaterial();
		return material == Material.STONE || material == Material.METAL || material == Material.REPAIR_STATION || state.isOf(Blocks.NETHER_GOLD_ORE);
	}
}
