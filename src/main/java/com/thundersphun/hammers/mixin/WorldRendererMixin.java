package com.thundersphun.hammers.mixin;

import com.thundersphun.hammers.Hammers;
import com.thundersphun.hammers.item.MiningHammer;
import com.thundersphun.hammers.util.AreaEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(WorldRenderer.class)
abstract class WorldRendererMixin implements SynchronousResourceReloadListener, AutoCloseable {
	@Shadow
	private ClientWorld world;

	@Shadow
	private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
	}

	@Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
			at = @At("RETURN"))
	private void drawBlockOutline(MatrixStack matrix, VertexConsumer vertex, Entity entity, double x, double y, double z, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (entity instanceof PlayerEntity) {
			Item item = ((PlayerEntity) entity).getStackInHand(Hand.MAIN_HAND).getItem();
			if (item.isIn(Hammers.MINING_TAG) && item instanceof MiningHammer) {
				Set<Block> effectiveBlocks = PickaxeItemAccessor.getEffectiveBlocks();

				if (effectiveBlocks.contains(state.getBlock()) || item.isEffectiveOn(state)) {
					AreaEffect.useOnArea((PlayerEntity) entity, this.world, pos, state, (position, hand) ->
							drawShapeOutline(matrix, vertex, this.world.getBlockState(position).getOutlineShape(this.world, position, ShapeContext.of(entity)),
							position.getX() - x, position.getY() - y, position.getZ() - z, 0.0F, 0.0F, 0.0F, 0.4F));
				}
			}
		}
	}
}
