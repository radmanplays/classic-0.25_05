package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.mob.ai.BasicAttackAI;
import com.mojang.minecraft.particle.Particle;

public class Creeper extends Mob {
	public static final long serialVersionUID = 0L;

	public Creeper(Level var1, float var2, float var3, float var4) {
		super(var1);
		this.heightOffset = 1.62F;
		this.modelName = "creeper";
		this.textureName = "/mob/creeper.png";
		this.ai = new BasicAttackAI() {
			public final boolean attack(Entity var1) {
				if(!super.attack(var1)) {
					return false;
				} else {
					this.mob.hurt(var1, 6);
					return true;
				}
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
		};
		this.ai.defaultLookAngle = 45;
		this.setPos(var2, var3, var4);
	}

	public float getBrightness(float var1) {
		float var2 = (float)(20 - this.health) / 20.0F;
		var2 = (float)(Math.sin((double)((float)this.tickCount + var1)) * 0.5D + 0.5D) * var2 * 0.5F + 0.25F + var2 * 0.25F;
		return var2 * super.getBrightness(var1);
	}

	public void die(Entity var1) {
		if(var1 != null) {
			var1.awardKillScore(this, 250);
		}

		super.die(var1);
	}
}
