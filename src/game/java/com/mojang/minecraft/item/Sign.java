package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class Sign extends Entity {
	private static SignModel model = new SignModel();
	private float xd;
	private float yd;
	private float zd;
	private float rot;
	private Font font;
	private String[] messages = new String[]{"This is a test", "of the signs.", "Each line can", "be 15 chars!"};

	public Sign(Minecraft var1, float var2, float var3, float var4, float var5) {
		super(var1.level);
		this.setSize(0.5F, 1.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(var2, var3, var4);
		this.font = var1.font;
		this.rot = -var5;
		this.heightOffset = 1.5F;
		this.xd = -((float)Math.sin((double)this.rot * Math.PI / 180.0D)) * 0.05F;
		this.yd = 0.2F;
		this.zd = -((float)Math.cos((double)this.rot * Math.PI / 180.0D)) * 0.05F;
		this.makeStepSound = false;
	}

	public boolean isPickable() {
		return !this.removed;
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

	}

	public void render(Textures var1, float var2) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int var3 = var1.loadTexture("/item/sign.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
		float var4 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glColor4f(var4, var4, var4, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * var2);
		GL11.glRotatef(this.rot, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		SignModel var5 = model;
		var5.signBoard.render(1.0F / 16.0F);
		var5.signStick.render(1.0F / 16.0F);
		GL11.glPopMatrix();
		var4 = (float)(1.0D / 60.0D);
		GL11.glTranslatef(0.0F, 0.5F, 0.091F);
		GL11.glScalef(var4, -var4, var4);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * var4);
		GL11.glEnable(GL11.GL_BLEND);

		for(var3 = 0; var3 < this.messages.length; ++var3) {
			String var6 = this.messages[var3];
			this.font.draw(var6, -this.font.width(var6) / 2, var3 * 10 - this.messages.length * 5, 2105376);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
