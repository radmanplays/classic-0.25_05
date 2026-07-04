package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.tilerenderer.TileRenderer;
import org.lwjgl.opengl.GL11;
import com.mojang.util.GLAllocation;
import com.mojang.util.Vec3D_112;

import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.opengl.GlStateManager;

public final class GameRenderer {
	public Minecraft minecraft;
	public float fogColorMultiplier = 1.0F;
	public boolean displayActive = false;
	public float renderDistance = 0.0F;
	public TileRenderer tileRenderer;
	private volatile int unusedInt1 = 0;
	private volatile int unusedInt2 = 0;
	private FloatBuffer lb = GLAllocation.createFloatBuffer(16);
	public float fogRed;
	public float fogGreen;
	public float fogBlue;
	
	private static final Vec3D_112 LIGHT0_POS = (new Vec3D_112(0.0D, 1.0D, -0.5D))
			.normalize();
	private static final Vec3D_112 LIGHT1_POS = (new Vec3D_112(0.0D, 1.0D, 0.5D))
			.normalize();


	public GameRenderer(Minecraft var1) {
		this.minecraft = var1;
		this.tileRenderer = new TileRenderer(var1);
	}

	public void renderHurtFrames(float var1) {
		Player var3 = this.minecraft.player;
		float var2 = (float)var3.hurtTime - var1;
		if(var3.health <= 0) {
			var1 += (float)var3.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (var1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(var2 >= 0.0F) {
			var2 /= (float)var3.hurtDuration;
			var2 = (float)Math.sin((double)(var2 * var2 * var2 * var2) * Math.PI);
			var1 = var3.hurtDir;
			GL11.glRotatef(-var1, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var2 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(var1, 0.0F, 1.0F, 0.0F);
		}
	}

	public void cameraBob(float var1) {
		Player var4 = this.minecraft.player;
		float var2 = var4.walkDist - var4.walkDistO;
		var2 = var4.walkDist + var2 * var1;
		float var3 = var4.oBob + (var4.bob - var4.oBob) * var1;
		float var5 = var4.oTilt + (var4.tilt - var4.oTilt) * var1;
		GL11.glTranslatef((float)Math.sin((double)var2 * Math.PI) * var3 * 0.5F, -((float)Math.abs(Math.cos((double)var2 * Math.PI) * (double)var3)), 0.0F);
		GL11.glRotatef((float)Math.sin((double)var2 * Math.PI) * var3 * 3.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef((float)Math.abs(Math.cos((double)var2 * Math.PI + (double)0.2F) * (double)var3) * 5.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
	}

	public final void toggleLight(boolean var1) {
		if (!var1) {
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LIGHT0);
			GL11.glDisable(GL11.GL_LIGHT1);
			GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		} else {
			GL11.glEnable(GL11.GL_LIGHTING);
			GlStateManager.enableMCLight(0, 0.6f, LIGHT0_POS.xCoord, -LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D);
			GlStateManager.enableMCLight(1, 0.6f, LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D);
			GlStateManager.setMCLightAmbient(0.4f, 0.4f, 0.4f);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		}
	}

	public final void init() {
		int var1 = this.minecraft.width * 240 / this.minecraft.height;
		int var2 = this.minecraft.height * 240 / this.minecraft.height;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)var1, (double)var2, 0.0D, 100.0D, 300.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
	}

	public void setupFog() {
		Level var1 = this.minecraft.level;
		Player var2 = this.minecraft.player;
		GL11.glFog(GL11.GL_FOG_COLOR, this.getBuffer(this.fogRed, this.fogGreen, this.fogBlue, 1.0F));
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Tile var5 = Tile.tiles[var1.getTile((int)var2.x, (int)(var2.y + 0.12F), (int)var2.z)];
		if(var5 != null && var5.getLiquidType() != Liquid.none) {
			Liquid var6 = var5.getLiquidType();
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			float var3;
			float var4;
			float var7;
			float var8;
			if(var6 == Liquid.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
				var7 = 0.4F;
				var8 = 0.4F;
				var3 = 0.9F;
				if(this.minecraft.options.anaglyph3d) {
					var4 = (var7 * 30.0F + var8 * 59.0F + var3 * 11.0F) / 100.0F;
					var8 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
					var3 = (var7 * 30.0F + var3 * 70.0F) / 100.0F;
					var7 = var4;
					var8 = var8;
					var3 = var3;
				}

//				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(var7, var8, var3, 1.0F));
			} else if(var6 == Liquid.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
				var7 = 0.4F;
				var8 = 0.3F;
				var3 = 0.3F;
				if(this.minecraft.options.anaglyph3d) {
					var4 = (var7 * 30.0F + var8 * 59.0F + var3 * 11.0F) / 100.0F;
					var8 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
					var3 = (var7 * 30.0F + var3 * 70.0F) / 100.0F;
					var7 = var4;
					var8 = var8;
					var3 = var3;
				}

//				GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(var7, var8, var3, 1.0F));
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, this.renderDistance);
//			GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, this.getBuffer(1.0F, 1.0F, 1.0F, 1.0F));
		}

//		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
//		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private FloatBuffer getBuffer(float var1, float var2, float var3, float var4) {
		this.lb.clear();
		this.lb.put(var1).put(var2).put(var3).put(var4);
		this.lb.flip();
		return this.lb;
	}
}
