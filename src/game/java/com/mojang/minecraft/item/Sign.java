package com.mojang.minecraft.item;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class Sign extends Entity {
	public static final long serialVersionUID = 0L;
	private static SignModel model = new SignModel();
	private float xd;
	private float yd;
	private float zd;
	private float rot;
	private String[] messages = new String[]{"This is a test", "of the signs.", "Each line can", "be 15 chars!"};

	public Sign(Level var1, float var2, float var3, float var4, float var5) {
		super(var1);
		this.setSize(0.5F, 1.5F);
		this.heightOffset = this.bbHeight / 2.0F;
		this.setPos(var2, var3, var4);
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
		int var4 = var1.loadTexture("/item/sign.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		float var5 = this.level.getBrightness((int)this.x, (int)this.y, (int)this.z);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glColor4f(var5, var5, var5, 1.0F);
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 - this.heightOffset / 2.0F, this.zo + (this.z - this.zo) * var2);
		GL11.glRotatef(this.rot, 0.0F, 1.0F, 0.0F);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		SignModel var6 = model;
		var6.signBoard.render(1.0F / 16.0F);
		var6.signStick.render(1.0F / 16.0F);
		GL11.glPopMatrix();
		var5 = (float)(1.0D / 60.0D);
		GL11.glTranslatef(0.0F, 0.5F, 0.09F);
		GL11.glScalef(var5, -var5, var5);
		GL11.glNormal3f(0.0F, 0.0F, -1.0F * var5);
		GL11.glEnable(GL11.GL_BLEND);
		Font var8 = this.level.font;

		for(int var7 = 0; var7 < this.messages.length; ++var7) {
			String var3 = this.messages[var7];
			var8.draw(var3, -var8.width(var3) / 2, var7 * 10 - this.messages.length * 5, 2105376);
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
