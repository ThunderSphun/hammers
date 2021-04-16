package com.thundersphun.hammers.util;

import com.thundersphun.hammers.Hammers;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum HammerToolMaterial implements ToolMaterial {
	WOOD(ToolMaterials.WOOD, false, () -> Ingredient.fromTag(ItemTags.LOGS)),
	STONE(ToolMaterials.STONE, false, () -> Ingredient.fromTag(Hammers.STURDY_STONE_TAG)),
	GOLD(ToolMaterials.GOLD, false, () -> Ingredient.ofItems(Items.GOLD_BLOCK)),
	IRON(ToolMaterials.IRON, false, () -> Ingredient.ofItems(Items.IRON_BLOCK)),
	DIAMOND(ToolMaterials.DIAMOND, false, () -> Ingredient.ofItems(Items.DIAMOND_BLOCK)),
	NETHERITE(ToolMaterials.NETHERITE, true, () -> Ingredient.ofItems(Items.NETHERITE_BLOCK));

	private final float attackDamage;
	private final int durability;
	private final int enchantability;
	private final int miningLevel;
	private final float miningSpeed;
	private final boolean fireProof;
	private final Lazy<Ingredient> repair;

	HammerToolMaterial(ToolMaterial base, boolean fireProof, Supplier<Ingredient> repairItems) {
		this.attackDamage = base.getAttackDamage();
		this.durability = (int) (base.getDurability() * 2.5);
		this.enchantability = base.getEnchantability();
		this.miningLevel = base.getMiningLevel();
		this.miningSpeed = base.getMiningSpeedMultiplier() * 0.75f;
		this.fireProof = fireProof;
		this.repair = new Lazy<>(repairItems);
	}

	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public int getDurability() {
		return this.durability;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public int getMiningLevel() {
		return this.miningLevel;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return this.miningSpeed;
	}

	public boolean isFireProof() {
		return this.fireProof;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repair.get();
	}

	@Override
	public String toString() {
		switch (this) {
			case GOLD:
				return "golden";
			case WOOD:
				return "wooden";
			default:
				return name().toLowerCase();
		}
	}
}
