package com.mojang.minecraft.model;

public final class PlayerModel extends HumanoidModel {
	public PlayerModel() {
		this(0.0F);
	}

	private PlayerModel(float var1) {
		super(0.0F);
		this.head.isHidden = true;
	}
}
