package com.mojang.minecraft.player;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.mob.Mob;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.model.PlayerModel;
import com.mojang.minecraft.renderer.Textures;
import java.util.List;

public class Player extends Mob {
	public static final int MAX_HEALTH = 20;
	private Input input;
	public Inventory inventory = new Inventory();
	public byte userType = 0;
	public float oBob;
	public float bob;
	public int score = 0;

	public Player(Level var1, Input var2) {
		super(var1);
		var1.player = this;
		var1.removeEntity(this);
		var1.addEntity(this);
		System.out.println(var1.player);
		this.heightOffset = 1.62F;
		this.input = var2;
		this.health = 20;
		this.model = new PlayerModel();
		this.rotOffs = 180.0F;
		this.ai = new PlayerInput(this, var2);
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
		Inventory var1 = this.inventory;

		int var2;
		for(var2 = 0; var2 < var1.popTime.length; ++var2) {
			if(var1.popTime[var2] > 0) {
				--var1.popTime[var2];
			}
		}

		this.oBob = this.bob;
		this.input.tick();
		super.aiStep();
		float var3 = (float)Math.sqrt((double)(this.xd * this.xd + this.zd * this.zd));
		float var4 = (float)Math.atan((double)(-this.yd * 0.2F)) * 15.0F;
		if(var3 > 0.1F) {
			var3 = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			var3 = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			var4 = 0.0F;
		}

		this.bob += (var3 - this.bob) * 0.4F;
		this.tilt += (var4 - this.tilt) * 0.8F;
		List var5 = this.level.findEntities(this, this.bb.grow(1.0F, 0.0F, 1.0F));
		if(var5 != null) {
			for(var2 = 0; var2 < var5.size(); ++var2) {
				((Entity)var5.get(var2)).playerTouch(this);
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
		Inventory var3 = this.inventory;
		int var2 = var3.containsTileAt(var1);
		if(var2 < 0) {
			var2 = var3.containsTileAt(-1);
		}

		if(var2 < 0) {
			return false;
		} else if(var3.count[var2] >= 99) {
			return false;
		} else {
			var3.slots[var2] = var1;
			++var3.count[var2];
			var3.popTime[var2] = 5;
			return true;
		}
	}

	public int getScore() {
		return this.score;
	}

	public HumanoidModel getModel() {
		return (HumanoidModel)this.model;
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
}
