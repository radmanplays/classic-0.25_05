package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.particle.Particle;

final class CreeperAI extends BasicAttackAI {
	private Creeper creeper;

	CreeperAI(Creeper var1) {
		this.creeper = var1;
	}

	public final void hurt(Entity var1) {
		super.hurt(var1);
		this.creeper.hurt(var1, 4);
	}

	public final void beforeRemove() {
		float var1 = 4.0F;
		this.level.explode(this.mob, this.mob.x, this.mob.y, this.mob.z, var1);

		for(int var2 = 0; var2 < 500; ++var2) {
			float var3 = (float)this.random.nextGaussian() * var1 / 4.0F;
			float var4 = (float)this.random.nextGaussian() * var1 / 4.0F;
			float var5 = (float)this.random.nextGaussian() * var1 / 4.0F;
			float var6 = (float)Math.sqrt((double)(var3 * var3 + var4 * var4 + var5 * var5));
			float var7 = var3 / var6 / var6;
			float var8 = var4 / var6 / var6;
			var6 = var5 / var6 / var6;
			this.level.particleEngine.addParticle(new Particle(this.level, this.mob.x + var3, this.mob.y + var4, this.mob.z + var5, var7, var8, var6, Tile.leaf));
		}

	}
}
