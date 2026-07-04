package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.mob.ai.BasicAI;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.opengl.ImageData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class Player extends Mob {
	public static final long serialVersionUID = 0L;
	public static final int MAX_HEALTH = 20;
	public static final int MAX_ARROWS = 99;
	public transient Input input;
	public Inventory inventory = new Inventory();
	public byte userType = 0;
	public float oBob;
	public float bob;
	public int score = 0;
	public int arrows = 20;
	private static int texture = -1;
	public static ImageData newTexture;

	public Player(Level var1) {
		super(var1);
		var1.player = this;
		var1.removeEntity(this);
		var1.addEntity(this);
		this.heightOffset = 1.62F;
		this.health = 20;
		this.modelName = "humanoid";
		this.rotOffs = 180.0F;
		this.ai = new BasicAI() {
			protected final void update() {
				this.jumping = Player.this.input.jumping;
				this.xxa = Player.this.input.ya;
				this.yya = Player.this.input.xa;
			}
		};
	}

	public void resetPos() {
		this.heightOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.resetPos();
		this.level.player = this;
		this.health = 20;
		this.deathTime = 0;
	}

	public void aiStep() {
		this.inventory.tick();
		this.oBob = this.bob;
		this.input.tick();
		super.aiStep();
		float var1 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
		float var2 = (float)Math.atan((double)(-this.yd * 0.2F)) * 15.0F;
		if(var1 > 0.1F) {
			var1 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			var1 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			var2 = 0.0F;
		}

		this.bob += (var1 - this.bob) * 0.4F;
		this.tilt += (var2 - this.tilt) * 0.8F;
		if(this.health > 0) {
			List var3 = this.level.findEntities(this, this.bb.grow(1.0F, 0.0F, 1.0F));
			if(var3 != null) {
				for(int var4 = 0; var4 < var3.size(); ++var4) {
					((Entity)var3.get(var4)).playerTouch(this);
				}
			}
		}

	}

	public void render(Textures var1, float var2) {
	}

	public void releaseAllKeys() {
		this.input.releaseAllKeys();
	}

	public void setKey(int var1, boolean var2) {
		this.input.setKey(var1, var2);
	}

	public boolean addResource(int var1) {
		return this.inventory.addResource(var1);
	}

	public int getScore() {
		return this.score;
	}

	public HumanoidModel getModel() {
		return (HumanoidModel)modelCache.getModel(this.modelName);
	}

	public void die(Entity var1) {
		this.setSize(0.2F, 0.2F);
		this.setPos(this.x, this.y, this.z);
		this.yd = 0.1F;
		if(var1 != null) {
			this.xd = -((float)Math.cos((double)(this.hurtDir + this.yRot) * Math.PI / 180.0D)) * 0.1F;
			this.zd = -((float)Math.sin((double)(this.hurtDir + this.yRot) * Math.PI / 180.0D)) * 0.1F;
		} else {
			this.xd = this.zd = 0.0F;
		}

		this.heightOffset = 0.1F;
	}

	public void remove() {
	}

	public void awardKillScore(Entity var1, int var2) {
		this.score += var2;
	}

	public boolean isShootable() {
		return true;
	}

	public void bindTexture(Textures var1) {
		if(newTexture != null) {
			texture = var1.loadTexture(newTexture);
			newTexture = null;
		}

		if(texture < 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.loadTexture("/char.png"));
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		}
	}
	
	public void writeTo(DataOutputStream out) throws IOException {
		super.writeTo(out);
		out.writeInt(this.health);
		out.writeInt(this.score);
		out.writeInt(this.arrows);
		for(int i = 0; i < 9; ++i) {
			out.writeInt(this.inventory.slots[i]);
			out.writeInt(this.inventory.count[i]);
		}
	}

	public void readFrom(DataInputStream in) throws IOException {
		super.readFrom(in);
		this.health = in.readInt();
		this.score = in.readInt();
		this.arrows = in.readInt();
		for(int i = 0; i < 9; ++i) {
			this.inventory.slots[i] = in.readInt();
			this.inventory.count[i] = in.readInt();
		}
	}
}
