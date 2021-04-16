package com.thundersphun.hammers.util;

import com.thundersphun.hammers.Hammers;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;

public enum HammerToolMaterial implements ToolMaterial {
	WOOD(ToolMaterials.WOOD, false, ItemTags.LOGS),
	STONE(ToolMaterials.STONE, false, Hammers.STURDY_STONE_TAG),
	GOLD(ToolMaterials.GOLD, false, Items.GOLD_BLOCK),
	IRON(ToolMaterials.IRON, false, Items.IRON_BLOCK),
	DIAMOND(ToolMaterials.DIAMOND, false, Items.DIAMOND_BLOCK),
	NETHERITE(ToolMaterials.NETHERITE, true, Items.NETHERITE_BLOCK);

	private final float attackDamage;
	private final int durability;
	private final int enchantability;
	private final int miningLevel;
	private final float miningSpeed;
	private final boolean fireProof;
	private Ingredient repair;

	HammerToolMaterial(ToolMaterial base, boolean fireProof) {
		this.attackDamage = base.getAttackDamage();
		this.durability = (int) (base.getDurability() * 2.5);
		this.enchantability = base.getEnchantability();
		this.miningLevel = base.getMiningLevel();
		this.miningSpeed = base.getMiningSpeedMultiplier() * 0.75f;
		this.fireProof = fireProof;
		this.repair = null;
	}

	HammerToolMaterial(ToolMaterial base, boolean fireProof, Item... repairItems) {
		this(base, fireProof);
		this.repair = Ingredient.ofItems(repairItems);
	}

	HammerToolMaterial(ToolMaterial base, boolean fireProof, Tag<Item> repairItems) {
		this(base, fireProof);
		this.repair = Ingredient.fromTag(repairItems);
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
		return this.repair;
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
