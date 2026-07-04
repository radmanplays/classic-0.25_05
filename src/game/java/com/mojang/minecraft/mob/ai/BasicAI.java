package com.mojang.minecraft.mob.ai;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;
import java.util.List;
import net.lax1dude.eaglercraft.Random;

public class BasicAI extends AI {
	public Random random = new Random();
	public float xxa;
	public float yya;
	private float yRotA;
	public Level level;
	public Mob mob;
	public boolean jumping = false;
	private int attackDelay = 0;

	public final void tick(Level var1, Mob var2) {
		this.level = var1;
		this.mob = var2;
		if(this.attackDelay > 0) {
			--this.attackDelay;
		}

		if(var2.health <= 0) {
			this.jumping = false;
			this.xxa = 0.0F;
			this.yya = 0.0F;
			this.yRotA = 0.0F;
		} else {
			this.tick();
		}

		boolean var3 = var2.isInWater();
		boolean var4 = var2.isInLava();
		if(this.jumping) {
			if(var3) {
				var2.yd += 0.04F;
			} else if(var4) {
				var2.yd += 0.04F;
			} else if(var2.onGround) {
				var2.yd = 0.42F;
			}
		}

		this.xxa *= 0.98F;
		this.yya *= 0.98F;
		this.yRotA *= 0.9F;
		var2.travel(this.xxa, this.yya);
		List var5 = var1.findEntities(var2, var2.bb.grow(0.2F, 0.0F, 0.2F));
		if(var5 != null && var5.size() > 0) {
			for(int var6 = 0; var6 < var5.size(); ++var6) {
				Entity var7 = (Entity)var5.get(var6);
				if(var7.isPushable()) {
					var7.push(var2);
				}
			}
		}

	}

	protected void tick() {
		if(this.random.nextFloat() < 0.07F) {
			this.xxa = this.random.nextFloat() - 0.5F;
			this.yya = this.random.nextFloat();
		}

		if(this.random.nextFloat() < 0.04F) {
			this.yRotA = (this.random.nextFloat() - 0.5F) * 60.0F;
		}

		this.mob.yRot += this.yRotA;
		this.mob.xRot = (float)this.defaultLookAngle;
		this.jumping = this.random.nextFloat() < 0.01F;
		boolean var1 = this.mob.isInWater();
		boolean var2 = this.mob.isInLava();
		if(var1 || var2) {
			this.jumping = this.random.nextFloat() < 0.8F;
		}

	}

	protected final void attack(Entity var1) {
		if(var1 != null) {
			float var2 = var1.x - this.mob.x;
			float var3 = var1.z - this.mob.z;
			float var4 = (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
			if(var4 < 8.0F) {
				float var5 = var1.y - this.mob.y;
				this.mob.yRot = (float)(Math.atan2((double)var3, (double)var2) * 180.0D / Math.PI) - 90.0F;
				this.mob.xRot = -((float)(Math.atan2((double)var5, (double)var4) * 180.0D / Math.PI));
				var4 = (float)Math.sqrt((double)(var2 * var2 + var5 * var5 + var3 * var3));
				if(var4 < 2.0F && this.attackDelay == 0) {
					this.hurt(var1);
				}
			}

		}
	}

	public void hurt(Entity var1) {
		if(this.level.clip(new Vec3(this.mob.x, this.mob.y, this.mob.z), new Vec3(var1.x, var1.y, var1.z)) == null) {
			this.mob.attackTime = 5;
			this.attackDelay = this.random.nextInt(20) + 10;
			var1.hurt(this.mob, this.random.nextInt(4) + this.random.nextInt(4) + 1);
		}
	}

	public void beforeRemove() {
	}
}
