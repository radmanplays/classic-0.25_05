package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class Item extends Entity {
	private static ItemModel[] models = new ItemModel[256];
	private float xd;
	private float yd;
	private float zd;
	private float rot;
	private int resource;
	private int tickCount;
	private int age = 0;

	public Item(Level var1, float var2, float var3, float var4, int var5) {
		super(var1);
		this.setSize(0.25F, 0.25F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(var2, var3, var4);
		this.resource = var5;
		this.rot = (float)(Math.random() * 360.0D);
		this.xd = (float)(Math.random() * (double)0.2F - (double)0.1F);
		this.yd = 0.2F;
		this.zd = (float)(Math.random() * (double)0.2F - (double)0.1F);
		this.makeStepSound = false;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.yd -= 0.04F;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
			this.yd *= -0.5F;
		}

		++this.tickCount;
		++this.age;
		if(this.age >= 6000) {
			this.remove();
		}

	}

	public void render(Textures var1, float var2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		int var4 = var1.loadTexture("/terrain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		float var5 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		float var3 = this.rot + ((float)this.tickCount + var2) * 3.0F;
		GL11.glPushMatrix();
		GL11.glColor4f(var5, var5, var5, 1.0F);
		var5 = (float)Math.sin((double)(var3 / 10.0F)) * 0.1F + 0.1F;
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 + var5, this.zo + (this.z - this.zo) * var2);
		GL11.glRotatef(var3, 0.0F, 1.0F, 0.0F);
		models[this.resource].render();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var5 = (float)Math.sin((double)(var3 / 10.0F)) * 0.5F + 0.5F;
		var5 *= var5;
		var5 *= var5;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, var5 * 0.4F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		models[this.resource].render();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public void playerTouch(Player var1) {
		if(var1.addResource(this.resource)) {
			this.level.addEntity(new TakeItemAnim(this.level, this, var1));
			this.remove();
		}

	}

	static {
		for(int var0 = 0; var0 < 256; ++var0) {
			Tile var1 = Tile.tiles[var0];
			if(var1 != null) {
				models[var0] = new ItemModel(var1.tex);
			}
		}

	}
}
