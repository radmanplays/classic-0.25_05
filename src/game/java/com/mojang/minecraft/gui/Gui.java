package com.mojang.minecraft.gui;

import com.mojang.minecraft.GuiMessage;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.Display;
import net.lax1dude.eaglercraft.Mouse;
import net.lax1dude.eaglercraft.Random;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public final class Gui extends GuiComponent {
	public List messages = new ArrayList();
	private Random random = new Random();
	private Minecraft minecraft;
	private int scaledWidth;
	private int scaledHeight;
	public String hoveredUsername = null;
	public int tickCounter = 0;

	public Gui(Minecraft var1, int var2, int var3) {
		this.minecraft = var1;
		this.scaledWidth = var2 * 240 / var3;
		this.scaledHeight = var3 * 240 / var3;
	}

	public final void render(float var1, boolean var2, int var3, int var4) {
		Font var5 = this.minecraft.font;
	    if (!Display.isActive() || !Mouse.isMouseGrabbed() || !Mouse.isActuallyGrabbed()) {
	        if (System.currentTimeMillis() - minecraft.oFrames > 250L) {
	            if (minecraft.screen == null) {
	            	minecraft.pauseScreen();
	            }
	        }
	    }
		this.minecraft.lighting.init();
		Textures var6 = this.minecraft.textures;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/gui.png"));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		Tesselator var7 = Tesselator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		Inventory var8 = this.minecraft.player.inventory;
		this.blitOffset = -90.0F;
		this.blit(this.scaledWidth / 2 - 91, this.scaledHeight - 22, 0, 0, 182, 22);
		this.blit(this.scaledWidth / 2 - 91 - 1 + var8.selected * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.minecraft.textures.loadTexture("/gui/icons.png"));
		this.blit(this.scaledWidth / 2 - 7, this.scaledHeight / 2 - 7, 0, 0, 16, 16);
		boolean var9 = this.minecraft.player.invulnerableTime / 3 % 2 == 1;
		if(this.minecraft.player.invulnerableTime < 10) {
			var9 = false;
		}

		int var10 = this.minecraft.player.health;
		int var11 = this.minecraft.player.lastHealth;
		this.random.setSeed((long)(this.tickCounter * 312871));

		int var12;
		int var14;
		int var15;
		for(var12 = 0; var12 < 10; ++var12) {
			byte var13 = 0;
			if(var9) {
				var13 = 1;
			}

			var14 = this.scaledWidth / 2 - 91 + (var12 << 3);
			var15 = this.scaledHeight - 32;
			if(var10 <= 4) {
				var15 += this.random.nextInt(2);
			}

			this.blit(var14, var15, 16 + var13 * 9, 0, 9, 9);
			if(var9) {
				if((var12 << 1) + 1 < var11) {
					this.blit(var14, var15, 70, 0, 9, 9);
				}

				if((var12 << 1) + 1 == var11) {
					this.blit(var14, var15, 79, 0, 9, 9);
				}
			}

			if((var12 << 1) + 1 < var10) {
				this.blit(var14, var15, 52, 0, 9, 9);
			}

			if((var12 << 1) + 1 == var10) {
				this.blit(var14, var15, 61, 0, 9, 9);
			}
		}

		int var25;
		if(this.minecraft.player.isUnderWater()) {
			var12 = (int)Math.ceil((double)(this.minecraft.player.airSupply - 2) * 10.0D / 300.0D);
			var25 = (int)Math.ceil((double)this.minecraft.player.airSupply * 10.0D / 300.0D) - var12;

			for(var14 = 0; var14 < var12 + var25; ++var14) {
				if(var14 < var12) {
					this.blit(this.scaledWidth / 2 - 91 + (var14 << 3), this.scaledHeight - 32 - 9, 16, 18, 9, 9);
				} else {
					this.blit(this.scaledWidth / 2 - 91 + (var14 << 3), this.scaledHeight - 32 - 9, 25, 18, 9, 9);
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);

		String var21;
		for(var12 = 0; var12 < var8.slots.length; ++var12) {
			var25 = this.scaledWidth / 2 - 90 + var12 * 20;
			var14 = this.scaledHeight - 16;
			var15 = var8.slots[var12];
			if(var15 > 0) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float)var25, (float)var14, -50.0F);
				if(var8.popTime[var12] > 0) {
					float var18 = ((float)var8.popTime[var12] - var1) / 5.0F;
					float var19 = -((float)Math.sin((double)(var18 * var18) * Math.PI)) * 8.0F;
					float var23 = (float)Math.sin((double)(var18 * var18) * Math.PI) + 1.0F;
					float var16 = (float)Math.sin((double)var18 * Math.PI) + 1.0F;
					GL11.glTranslatef(10.0F, var19 + 10.0F, 0.0F);
					GL11.glScalef(var23, var16, 1.0F);
					GL11.glTranslatef(-10.0F, -10.0F, 0.0F);
				}

				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 0.0F);
				GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.5F, 0.5F, 0.5F);
				GL11.glScalef(-1.0F, -1.0F, -1.0F);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
				int var20 = var6.loadTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var20);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				var7.begin();
				Tile.tiles[var15].render(var7, this.minecraft.level, 0, -2, 0, 0);
				var7.end();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
				if(var8.count[var12] > 1) {
					var21 = "" + var8.count[var12];
					var5.drawShadow(var21, var25 + 19 - var5.width(var21), var14 + 6, 16777215);
				}
			}
		}

		var5.drawShadow("0.24_SURVIVAL_TEST_03", 2, 2, 16777215);
		if(this.minecraft.options.showFramerate) {
			var5.drawShadow(this.minecraft.fpsString, 2, 12, 16777215);
		}

		byte var24 = 10;
		boolean var26 = false;
		if(this.minecraft.screen instanceof ChatScreen) {
			var24 = 20;
			var26 = true;
		}

		for(var14 = 0; var14 < this.messages.size() && var14 < var24; ++var14) {
			if(((GuiMessage)this.messages.get(var14)).counter < 200 || var26) {
				var5.drawShadow(((GuiMessage)this.messages.get(var14)).message, 2, this.scaledHeight - 8 - var14 * 9 - 20, 16777215);
			}
		}

		var14 = this.scaledWidth / 2;
		var15 = this.scaledHeight / 2;
		this.hoveredUsername = null;
		if(Keyboard.isKeyDown(Keyboard.KEY_TAB) && false) {
//			List var22 = this.minecraft.networkClient.getUsernames();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
			GL11.glVertex2f((float)(var14 + 128), (float)(var15 - 68 - 12));
			GL11.glVertex2f((float)(var14 - 128), (float)(var15 - 68 - 12));
			GL11.glColor4f(0.2F, 0.2F, 0.2F, 0.8F);
			GL11.glVertex2f((float)(var14 - 128), (float)(var15 + 68));
			GL11.glVertex2f((float)(var14 + 128), (float)(var15 + 68));
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			var21 = "Connected players:";
			var5.drawShadow(var21, var14 - var5.width(var21) / 2, var15 - 64 - 12, 16777215);

//			for(var11 = 0; var11 < var22.size(); ++var11) {
//				int var27 = var14 + var11 % 2 * 120 - 120;
//				int var17 = var15 - 64 + (var11 / 2 << 3);
//				if(var2 && var3 >= var27 && var4 >= var17 && var3 < var27 + 120 && var4 < var17 + 8) {
//					this.hoveredUsername = (String)var22.get(var11);
//					var5.draw((String)var22.get(var11), var27 + 2, var17, 16777215);
//				} else {
//					var5.draw((String)var22.get(var11), var27, var17, 15658734);
//				}
//			}
		}

	}

	public final void addMessage(String var1) {
		this.messages.add(0, new GuiMessage(var1));

		while(this.messages.size() > 50) {
			this.messages.remove(this.messages.size() - 1);
		}

	}
}
