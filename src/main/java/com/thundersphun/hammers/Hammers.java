package com.thundersphun.hammers;

import com.thundersphun.hammers.item.MiningHammer;
import com.thundersphun.hammers.mixin.PickaxeItemAccessor;
import com.thundersphun.hammers.util.AreaEffect;
import com.thundersphun.hammers.util.HammerToolMaterial;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.stat.Stats;
import net.minecraft.tag.Tag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.EnumMap;
import java.util.Set;

public class Hammers implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
	public static final EnumMap<HammerToolMaterial, Item> MINING_HAMMERS = new EnumMap<>(HammerToolMaterial.class);
	private static final String MOD_ID = "hammers";
	public static final Tag<Item> MINING_TAG = TagRegistry.item(id("mining_hammers"));
	public static final Tag<Item> STURDY_STONE_TAG = TagRegistry.item(id("sturdy_stones"));

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

	private static void mineEvent(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		Item item = player.getStackInHand(Hand.MAIN_HAND).getItem();
		if (item.isIn(MINING_TAG) && item instanceof MiningHammer) {
			Set<Block> effectiveBlocks = PickaxeItemAccessor.getEffectiveBlocks();

			if (effectiveBlocks.contains(state.getBlock()) || item.isEffectiveOn(state)) {
				AreaEffect.useOnArea(player, world, pos, state, (position, hand) -> {
					if (!hand.isEmpty()) {
						BlockState bs = world.getBlockState(position);
						BlockEntity be = world.getBlockEntity(position);
						world.setBlockState(position, Blocks.AIR.getDefaultState());
						if (!player.isCreative()) {
							hand.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
							player.incrementStat(Stats.USED.getOrCreateStat(hand.getItem()));
							player.incrementStat(Stats.MINED.getOrCreateStat(bs.getBlock()));
							Block.dropStacks(bs, world, position, be, player, player.getStackInHand(Hand.MAIN_HAND));
						}
					}
				});
			}
		}
	}

	@Override
	public void onInitialize() {
		this.registerMiningHammers();

		PlayerBlockBreakEvents.AFTER.register(Hammers::mineEvent);
	}

	@Override
	public void onInitializeClient() {
	}

	@Override
	public void onInitializeServer() {
	}

	private void registerMiningHammers() {
		for (HammerToolMaterial value : HammerToolMaterial.values()) {
			MINING_HAMMERS.put(value, Registry.register(Registry.ITEM, id(value + "_mining_hammer"), this.miningHammer(value)));
		}
	}

	private Item miningHammer(HammerToolMaterial material) {
		FabricItemSettings settings = new FabricItemSettings().group(ItemGroup.TOOLS);
		if (material.isFireProof()) {
			settings.fireproof();
		}
		return new MiningHammer(2f, -3f, material, settings);
	}
}
