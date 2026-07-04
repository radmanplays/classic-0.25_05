package com.mojang.minecraft.renderer;

import com.mojang.minecraft.Options;
import com.mojang.minecraft.renderer.texture.DynamicTexture;
import com.mojang.util.GLAllocation;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.opengl.ImageData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class Textures {
	public HashMap idMap = new HashMap();
	public HashMap pixelsMap = new HashMap();
	public IntBuffer ib = GLAllocation.createIntBuffer(1);
	public ByteBuffer pixels = GLAllocation.createByteBuffer(262144);
	public List textureList = new ArrayList();
	public Options options;

	public Textures(Options var1) {
		this.options = var1;
	}

	public final int loadTexture(String var1) {
		if(this.idMap.containsKey(var1)) {
			return ((Integer)this.idMap.get(var1)).intValue();
		} else {
			this.ib.clear();
			GL11.glGenTextures(this.ib);
			int var2 = this.ib.get(0);
			this.addTexture(ImageData.loadImageFile(EagRuntime.getResourceStream(var1)), var2);

			this.idMap.put(var1, Integer.valueOf(var2));
			return var2;
		}
	}

	public void addTexture(ImageData var1, int var2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		var2 = var1.getWidth();
		int var3 = var1.getHeight();
		int[] var4 = new int[var2 * var3];
		byte[] var5 = new byte[var2 * var3 << 2];
		var1.getRGB(0, 0, var2, var3, var4, 0, var2);

		for(int var11 = 0; var11 < var4.length; ++var11) {
			int a = var4[var11] >>> 24;
			int r = var4[var11] >> 16 & 255;
			int g = var4[var11] >> 8 & 255;
			int b = var4[var11] & 255;
			if(this.options.anaglyph3d) {
				int var10 = (r * 30 + g * 59 + b * 11) / 100;
				g = (r * 30 + g * 70) / 100;
				b = (r * 30 + b * 70) / 100;
				r = var10;
				g = g;
				b = b;
			}

			var5[var11 << 2] = (byte)b;
			var5[(var11 << 2) + 1] = (byte)g;
			var5[(var11 << 2) + 2] = (byte)r;
			var5[(var11 << 2) + 3] = (byte)a;
		}

		this.pixels.clear();
		this.pixels.put(var5);
		this.pixels.position(0).limit(var5.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var2, var3, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.pixels);
	}

	public final void addDynamicTexture(DynamicTexture var1) {
		this.textureList.add(var1);
		var1.tick();
	}
}
