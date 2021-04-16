package com.thundersphun.hammers.mixin;

import com.thundersphun.hammers.util.AreaEffect;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BlockRenderManager.class)
abstract class BlockRenderManagerMixin implements SynchronousResourceReloadListener {
	@Shadow
	@Final
	private BlockModelRenderer blockModelRenderer;
	@Shadow
	@Final
	private BlockModels models;
	@Shadow
	@Final
	private Random random;

	@Inject(method = "renderDamage(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V",
			at = @At("RETURN"))
	private void renderBlockDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, CallbackInfo ci) {
		if (state.getRenderType() == BlockRenderType.MODEL) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			AreaEffect.useOnArea(player, (World) world, pos, (offsetPos, hand) -> {
				matrix.push();
				BlockState offsetState = world.getBlockState(offsetPos);
				BlockPos offset = pos.subtract(offsetPos);
				matrix.translate(-offset.getX(), -offset.getY(), -offset.getZ());
				this.blockModelRenderer.render(world, this.models.getModel(offsetState), offsetState, offsetPos, matrix,
						vertexConsumer, true, this.random, offsetState.getRenderingSeed(offsetPos), OverlayTexture.DEFAULT_UV);
				matrix.pop();
			});
		}
	}
}
