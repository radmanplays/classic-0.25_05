package com.mojang.minecraft;

import com.mojang.minecraft.character.Vec3;

public final class HitResult {
	public int type;
	public int x;
	public int y;
	public int z;
	public int f;
	public Vec3 vec;
	public Entity entity;

	public HitResult(int var1, int var2, int var3, int var4, Vec3 var5) {
		this.type = 0;
		this.x = var1;
		this.y = var2;
		this.z = var3;
		this.f = var4;
		this.vec = new Vec3(var5.x, var5.y, var5.z);
	}

	public HitResult(Entity var1) {
		this.type = 1;
		this.entity = var1;
	}
}
