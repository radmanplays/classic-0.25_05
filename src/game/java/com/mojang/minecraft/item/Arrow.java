package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class Arrow extends Entity {
	public static final long serialVersionUID = 0L;
	private float xd;
	private float yd;
	private float zd;
	private float yRot;
	private float xRot;
	private float yRotO;
	private float xRotO;
	private boolean hasHit = false;
	private int stickTime = 0;
	private Entity owner;
	private int time = 0;
	private int type = 0;
	private float gravity = 0.0F;
	private int damage;

	public Arrow(Level var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
		super(var1);
		this.owner = var2;
		this.setSize(0.3F, 0.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.damage = 3;
		if(!(var2 instanceof Player)) {
			this.type = 1;
		} else {
			this.damage = 7;
		}

		this.heightOffset = 0.25F;
		float var10 = (float)Math.cos((double)(-var6) * Math.PI / 180.0D - Math.PI);
		float var11 = (float)Math.sin((double)(-var6) * Math.PI / 180.0D - Math.PI);
		var6 = (float)Math.cos((double)(-var7) * Math.PI / 180.0D);
		var7 = (float)Math.sin((double)(-var7) * Math.PI / 180.0D);
		this.slide = false;
		this.gravity = 1.0F / var8;
		this.xo -= var10 * 0.2F;
		this.zo += var11 * 0.2F;
		var3 -= var10 * 0.2F;
		var5 += var11 * 0.2F;
		this.xd = var11 * var6 * var8;
		this.yd = var7 * var8;
		this.zd = var10 * var6 * var8;
		this.setPos(var3, var4, var5);
		var10 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
		this.yRotO = this.yRot = (float)(Math.atan2((double)this.xd, (double)this.zd) * 180.0D / Math.PI);
		this.xRotO = this.xRot = (float)(Math.atan2((double)this.yd, (double)var10) * 180.0D / Math.PI);
		this.makeStepSound = false;
	}

	public void tick() {
		++this.time;
		this.xRotO = this.xRot;
		this.yRotO = this.yRot;
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(this.hasHit) {
			++this.stickTime;
			if(this.type == 0) {
				if(this.stickTime >= 300 && Math.random() < (double)0.01F) {
					this.remove();
					return;
				}
			} else if(this.type == 1 && this.stickTime >= 20) {
				this.remove();
			}

		} else {
			this.xd *= 0.998F;
			this.yd *= 0.998F;
			this.zd *= 0.998F;
			this.yd -= 0.02F * this.gravity;
			float var1 = (float)Math.sqrt((double)(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd));
			int var9 = (int)(var1 / 0.2F + 1.0F);
			float var2 = this.xd / (float)var9;
			float var3 = this.yd / (float)var9;
			float var4 = this.zd / (float)var9;

			for(int var5 = 0; var5 < var9 && !this.collision; ++var5) {
				AABB var6 = this.bb.expand(var2, var3, var4);
				if(this.level.getCubes(var6).size() > 0) {
					this.collision = true;
				}

				List var11 = this.level.blockMap.getEntities(this, var6);

				for(int var7 = 0; var7 < var11.size(); ++var7) {
					Entity var8 = (Entity)var11.get(var7);
					if(var8.isShootable() && (var8 != this.owner || this.time > 5)) {
						var8.hurt(this, this.damage);
						this.collision = true;
						this.remove();
						return;
					}
				}

				if(!this.collision) {
					this.bb.move(var2, var3, var4);
					this.x += var2;
					this.y += var3;
					this.z += var4;
					this.blockMap.moved(this);
				}
			}

			if(this.collision) {
				this.hasHit = true;
				this.xd = this.yd = this.zd = 0.0F;
			}

			if(!this.hasHit) {
				float var10 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
				this.yRot = (float)(Math.atan2((double)this.xd, (double)this.zd) * 180.0D / Math.PI);

				for(this.xRot = (float)(Math.atan2((double)this.yd, (double)var10) * 180.0D / Math.PI); this.xRot - this.xRotO < -180.0F; this.xRotO -= 360.0F) {
				}

				while(this.xRot - this.xRotO >= 180.0F) {
					this.xRotO += 360.0F;
				}

				while(this.yRot - this.yRotO < -180.0F) {
					this.yRotO -= 360.0F;
				}

				while(this.yRot - this.yRotO >= 180.0F) {
					this.yRotO += 360.0F;
				}
			}

		}
	}

	public void render(Textures var1, float var2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		int var10 = var1.loadTexture("/item/arrows.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var10);
		float var11 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glColor4f(var11, var11, var11, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * var2);
		GL11.glRotatef(this.yRotO + (this.yRot - this.yRotO) * var2 - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(this.xRotO + (this.xRot - this.xRotO) * var2, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
		Tesselator var12 = Tesselator.instance;
		var2 = 0.5F;
		float var3 = (float)(0 + this.type * 10) / 32.0F;
		float var4 = (float)(5 + this.type * 10) / 32.0F;
		float var5 = 0.15625F;
		float var6 = (float)(5 + this.type * 10) / 32.0F;
		float var8 = (float)(10 + this.type * 10) / 32.0F;
		float var7 = 0.05625F;
		GL11.glScalef(var7, var7, var7);
		GL11.glNormal3f(var7, 0.0F, 0.0F);
		var12.begin();
		var12.vertexUV(-7.0F, -2.0F, -2.0F, 0.0F, var6);
		var12.vertexUV(-7.0F, -2.0F, 2.0F, var5, var6);
		var12.vertexUV(-7.0F, 2.0F, 2.0F, var5, var8);
		var12.vertexUV(-7.0F, 2.0F, -2.0F, 0.0F, var8);
		var12.end();
		GL11.glNormal3f(-var7, 0.0F, 0.0F);
		var12.begin();
		var12.vertexUV(-7.0F, 2.0F, -2.0F, 0.0F, var6);
		var12.vertexUV(-7.0F, 2.0F, 2.0F, var5, var6);
		var12.vertexUV(-7.0F, -2.0F, 2.0F, var5, var8);
		var12.vertexUV(-7.0F, -2.0F, -2.0F, 0.0F, var8);
		var12.end();

		for(int var9 = 0; var9 < 4; ++var9) {
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, -var7, 0.0F);
			var12.begin();
			var12.vertexUV(-8.0F, -2.0F, 0.0F, 0.0F, var3);
			var12.vertexUV(8.0F, -2.0F, 0.0F, var2, var3);
			var12.vertexUV(8.0F, 2.0F, 0.0F, var2, var4);
			var12.vertexUV(-8.0F, 2.0F, 0.0F, 0.0F, var4);
			var12.end();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void awardKillScore(Entity var1, int var2) {
		this.owner.awardKillScore(var1, var2);
	}

	public Entity getOwner() {
		return this.owner;
	}

	public void playerTouch(Player var1) {
		if(this.hasHit && this.owner == var1 && var1.arrows < 99) {
			this.level.addEntity(new TakeEntityAnim(this.level, this, var1));
			++var1.arrows;
			this.remove();
		}

	}
}
