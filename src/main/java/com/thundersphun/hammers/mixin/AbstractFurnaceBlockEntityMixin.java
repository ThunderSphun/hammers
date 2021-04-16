package com.thundersphun.hammers.mixin;

import com.thundersphun.hammers.Hammers;
import com.thundersphun.hammers.util.HammerToolMaterial;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.Tickable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable {
	@Shadow
	protected static void addFuel(Map<Item, Integer> map, ItemConvertible item, int fuelTime) { }

	private AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	@Inject(method = "createFuelTimeMap()Ljava/util/Map;", at = @At("RETURN"), cancellable = true)
	private static void addFurnaceFuel(CallbackInfoReturnable<Map<Item, Integer>> cir) {
		Map<Item, Integer> map = cir.getReturnValue();
		addFuel(map, Hammers.MINING_HAMMERS.get(HammerToolMaterial.WOOD), 200);
		cir.setReturnValue(map);
	}
}
