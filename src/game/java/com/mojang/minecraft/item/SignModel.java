package com.mojang.minecraft.item;

import com.mojang.minecraft.character.Cube;

public final class SignModel {
	public Cube signBoard = new Cube(0, 0);
	public Cube signStick;

	public SignModel() {
		this.signBoard.addBox(-12.0F, -14.0F, -1.0F, 24, 12, 2, 0.0F);
		this.signStick = new Cube(0, 14);
		this.signStick.addBox(-1.0F, -2.0F, -1.0F, 2, 14, 2, 0.0F);
	}
}
