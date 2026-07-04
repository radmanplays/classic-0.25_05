package com.mojang.minecraft.mob;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.ai.AI;
import com.mojang.minecraft.mob.ai.BasicAI;
import com.mojang.minecraft.model.BaseModel;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class Mob extends Entity {
	public static final int ATTACK_DURATION = 5;
	public static final int TOTAL_AIR_SUPPLY = 300;
	public int invulnerableDuration = 30;
	public float rot;
	public float timeOffs;
	public float speed;
	public float rotA = (float)(Math.random() + 1.0D) * 0.01F;
	protected float yBodyRot = 0.0F;
	protected float yBodyRotO = 0.0F;
	protected float oRun;
	protected float run;
	protected float animStep;
	protected float animStepO;
	protected int tickCount = 0;
	public boolean hasHair = true;
	protected String textureName = "/char.png";
	public boolean allowAlpha = true;
	public BaseModel model = null;
	public float rotOffs = 0.0F;
	public int health = 20;
	public int lastHealth;
	public int invulnerableTime = 0;
	public int airSupply = 300;
	public int hurtTime;
	public int hurtDuration;
	public float hurtDir = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float oTilt;
	public float tilt;
	protected boolean dead = false;
	public AI ai;

	public Mob(Level var1) {
		super(var1);
		this.setPos(this.x, this.y, this.z);
		this.timeOffs = (float)Math.random() * 12398.0F;
		this.rot = (float)(Math.random() * Math.PI * 2.0D);
		this.speed = 1.0F;
		this.ai = new BasicAI();
	}

	public boolean isPickable() {
		return !this.removed;
	}

	public boolean isPushable() {
		return !this.removed;
	}

	public final void tick() {
		super.tick();
		this.oTilt = this.tilt;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.invulnerableTime > 0) {
			--this.invulnerableTime;
		}

		if(this.health <= 0) {
			++this.deathTime;
			if(this.deathTime > 20) {
				if(this.ai != null) {
					this.ai.beforeRemove();
				}

				this.remove();
			}
		}

		if(this.isUnderWater()) {
			if(this.airSupply > 0) {
				--this.airSupply;
			} else {
				this.hurt((Entity)null, 2);
			}
		} else {
			this.airSupply = 300;
		}

		if(this.isInWater()) {
			this.fallDistance = 0.0F;
		}

		if(this.isInLava()) {
			this.hurt((Entity)null, 10);
		}

		this.animStepO = this.animStep;
		this.yBodyRotO = this.yBodyRot;
		this.yRotO = this.yRot;
		this.xRotO = this.xRot;
		++this.tickCount;
		this.aiStep();
		float var1 = this.x - this.xo;
		float var2 = this.z - this.zo;
		float var3 = (float)Math.sqrt((double)(var1 * var1 + var2 * var2));
		float var4 = this.yBodyRot;
		float var5 = 0.0F;
		this.oRun = this.run;
		float var6 = 0.0F;
		if(var3 > 0.05F) {
			var6 = 1.0F;
			var5 = var3 * 3.0F;
			var4 = (float)Math.atan2((double)var2, (double)var1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(!this.onGround) {
			var6 = 0.0F;
		}

		this.run += (var6 - this.run) * 0.3F;

		for(var1 = var4 - this.yBodyRot; var1 < -180.0F; var1 += 360.0F) {
		}

		while(var1 >= 180.0F) {
			var1 -= 360.0F;
		}

		this.yBodyRot += var1 * 0.1F;

		for(var1 = this.yRot - this.yBodyRot; var1 < -180.0F; var1 += 360.0F) {
		}

		while(var1 >= 180.0F) {
			var1 -= 360.0F;
		}

		boolean var7 = var1 < -90.0F || var1 >= 90.0F;
		if(var1 < -75.0F) {
			var1 = -75.0F;
		}

		if(var1 >= 75.0F) {
			var1 = 75.0F;
		}

		this.yBodyRot = this.yRot - var1;
		this.yBodyRot += var1 * 0.1F;
		if(var7) {
			var5 = -var5;
		}

		while(this.yRot - this.yRotO < -180.0F) {
			this.yRotO -= 360.0F;
		}

		while(this.yRot - this.yRotO >= 180.0F) {
			this.yRotO += 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO < -180.0F) {
			this.yBodyRotO -= 360.0F;
		}

		while(this.yBodyRot - this.yBodyRotO >= 180.0F) {
			this.yBodyRotO += 360.0F;
		}

		while(this.xRot - this.xRotO < -180.0F) {
			this.xRotO -= 360.0F;
		}

		while(this.xRot - this.xRotO >= 180.0F) {
			this.xRotO += 360.0F;
		}

		this.animStep += var5;
	}

	public void aiStep() {
		if(this.ai != null) {
			this.ai.tick(this.level, this);
		}

	}

	protected void bindTexture(Textures var1) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.loadTexture(this.textureName));
	}

	public void render(Textures var1, float var2) {
		if(this.model != null) {
			float var3 = (float)this.attackTime - var2;
			if(var3 < 0.0F) {
				var3 = 0.0F;
			}

			while(this.yBodyRotO - this.yBodyRot < -180.0F) {
				this.yBodyRotO += 360.0F;
			}

			while(this.yBodyRotO - this.yBodyRot >= 180.0F) {
				this.yBodyRotO -= 360.0F;
			}

			float var4;
			for(var4 = this.yBodyRotO + (this.yBodyRot - this.yBodyRotO) * var2; this.xRotO - this.xRot < -180.0F; this.xRotO += 360.0F) {
			}

			while(this.xRotO - this.xRot >= 180.0F) {
				this.xRotO -= 360.0F;
			}

			while(this.yRotO - this.yRot < -180.0F) {
				this.yRotO += 360.0F;
			}

			while(this.yRotO - this.yRot >= 180.0F) {
				this.yRotO -= 360.0F;
			}

			float var5 = this.oRun + (this.run - this.oRun) * var2;
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			float var6 = this.yRotO + (this.yRot - this.yRotO) * var2;
			float var7 = this.xRotO + (this.xRot - this.xRotO) * var2;
			var6 -= var4;
			GL11.glPushMatrix();
			float var8 = this.animStepO + (this.animStep - this.animStepO) * var2;
			float var9 = this.getBrightness(var2);
			GL11.glColor3f(var9, var9, var9);
			var9 = 1.0F / 16.0F;
			float var10 = (float)(-Math.abs(Math.cos((double)var8 * 0.6662D)) * 5.0D * (double)var5 - 23.0D);
			GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - 1.62F, this.zo + (this.z - this.zo) * var2);
			float var11 = (float)this.hurtTime - var2;
			if(var11 > 0.0F || this.health <= 0) {
				if(var11 < 0.0F) {
					var11 = 0.0F;
				} else {
					var11 /= (float)this.hurtDuration;
					var11 = (float)Math.sin((double)(var11 * var11 * var11 * var11) * Math.PI) * 14.0F;
				}

				float var12;
				if(this.health <= 0) {
					var12 = ((float)this.deathTime + var2) / 20.0F;
					var11 += var12 * var12 * 800.0F;
					if(var11 > 90.0F) {
						var11 = 90.0F;
					}
				}

				var12 = this.hurtDir;
				GL11.glRotatef(180.0F - var4 + this.rotOffs, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(1.0F, 1.0F, 1.0F);
				GL11.glRotatef(-var12, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-var11, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-(180.0F - var4 + this.rotOffs), 0.0F, 1.0F, 0.0F);
			}

			GL11.glScalef(1.0F, -1.0F, 1.0F);
			GL11.glTranslatef(0.0F, var10 * var9, 0.0F);
			GL11.glRotatef(180.0F - var4 + this.rotOffs, 0.0F, 1.0F, 0.0F);
			if(!this.allowAlpha) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
			} else {
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glScalef(-1.0F, 1.0F, 1.0F);
			this.model.rot = var3 / 5.0F;
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			this.bindTexture(var1);
			this.renderModel(var1, var8, var2, var5, var6, var7, var9);
			if(this.invulnerableTime > this.invulnerableDuration - 10) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 12.0F / 16.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				this.bindTexture(var1);
				this.renderModel(var1, var8, var2, var5, var6, var7, var9);
				GL11.glDisable(GL11.GL_BLEND);
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			if(this.allowAlpha) {
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
		}
	}

	public void renderModel(Textures var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.model.render(var2, var4, (float)this.tickCount + var3, var5, var6, var7);
	}

	public void heal(int var1) {
		if(this.health > 0) {
			this.health += var1;
			if(this.health > 20) {
				this.health = 20;
			}

			this.invulnerableTime = this.invulnerableDuration / 2;
		}
	}

	public void hurt(Entity var1, int var2) {
		if(this.health > 0) {
			if((float)this.invulnerableTime > (float)this.invulnerableDuration / 2.0F) {
				if(this.lastHealth - var2 >= this.health) {
					return;
				}

				this.health = this.lastHealth - var2;
			} else {
				this.lastHealth = this.health;
				this.invulnerableTime = this.invulnerableDuration;
				this.health -= var2;
				this.hurtTime = this.hurtDuration = 10;
			}

			this.hurtDir = 0.0F;
			if(var1 != null) {
				float var3 = var1.x - this.x;
				float var4 = var1.z - this.z;
				this.hurtDir = (float)(Math.atan2((double)var4, (double)var3) * 180.0D / Math.PI) - this.yRot;
				this.knockback(var1, var2, var3, var4);
			} else {
				this.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
			}

			if(this.health <= 0) {
				this.die(var1);
			}

		}
	}

	public void knockback(Entity var1, int var2, float var3, float var4) {
		float var5 = (float)Math.sqrt((double)(var3 * var3 + var4 * var4));
		float var6 = 0.4F;
		this.xd /= 2.0F;
		this.yd /= 2.0F;
		this.zd /= 2.0F;
		this.xd -= var3 / var5 * var6;
		this.yd += 0.4F;
		this.zd -= var4 / var5 * var6;
		if(this.yd > 0.4F) {
			this.yd = 0.4F;
		}

	}

	public void die(Entity var1) {
		this.dead = true;
	}

	protected void causeFallDamage(float var1) {
		int var2 = (int)Math.ceil((double)(var1 - 3.0F));
		if(var2 > 0) {
			this.hurt((Entity)null, var2);
		}

	}

	public void travel(float var1, float var2) {
		float var3;
		if(this.isInWater()) {
			var3 = this.y;
			this.moveRelative(var1, var2, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.8F;
			this.yd *= 0.8F;
			this.zd *= 0.8F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
				this.yd = 0.3F;
			}

		} else if(this.isInLava()) {
			var3 = this.y;
			this.moveRelative(var1, var2, 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.5F;
			this.yd *= 0.5F;
			this.zd *= 0.5F;
			this.yd = (float)((double)this.yd - 0.02D);
			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F - this.y + var3, this.zd)) {
				this.yd = 0.3F;
			}

		} else {
			this.moveRelative(var1, var2, this.onGround ? 0.1F : 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.91F;
			this.yd *= 0.98F;
			this.zd *= 0.91F;
			this.yd = (float)((double)this.yd - 0.08D);
			if(this.onGround) {
				var3 = 0.6F;
				this.xd *= var3;
				this.zd *= var3;
			}

		}
	}

	public boolean isShootable() {
		return true;
	}
}
