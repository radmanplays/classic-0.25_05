package com.mojang.minecraft;

import com.mojang.minecraft.renderer.Textures;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.opengl.ImageData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;

public final class Options {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	public boolean music = true;
	public boolean sound = true;
	public boolean invertYMouse = false;
	public boolean showFramerate = false;
	public int viewDistance = 0;
	public boolean bobView = true;
	public boolean anaglyph3d = false;
	public boolean limitFramerate = false;
	public KeyMapping forward = new KeyMapping("Forward", 17);
	public KeyMapping left = new KeyMapping("Left", 30);
	public KeyMapping back = new KeyMapping("Back", 31);
	public KeyMapping right = new KeyMapping("Right", 32);
	public KeyMapping jump = new KeyMapping("Jump", 57);
	public KeyMapping build = new KeyMapping("Build", 48);
	public KeyMapping chat = new KeyMapping("Chat", 20);
	public KeyMapping toggleFog = new KeyMapping("Toggle fog", 33);
	private KeyMapping save = new KeyMapping("Save location", 28);
	private KeyMapping load = new KeyMapping("Load location", 19);
	public KeyMapping[] keys = new KeyMapping[]{this.forward, this.left, this.back, this.right, this.jump, this.build, this.chat, this.toggleFog, this.save, this.load};
	private Minecraft minecraft;
	private VFile2 optionsFile;

	public Options(Minecraft var1) {
		this.minecraft = var1;
		this.optionsFile = new VFile2("options.txt");
		this.load();
	}

	public final String getKeyMessage(int var1) {
		return this.keys[var1].name + ": " + Keyboard.getKeyName(this.keys[var1].key);
	}

	public final void setKey(int var1, int var2) {
		this.keys[var1].key = var2;
		this.save();
	}

	public final void setOption(int var1, int var2) {
		if(var1 == 0) {
			this.music = !this.music;
		}

		if(var1 == 1) {
			this.sound = !this.sound;
		}

		if(var1 == 2) {
			this.invertYMouse = !this.invertYMouse;
		}

		if(var1 == 3) {
			this.showFramerate = !this.showFramerate;
		}

		if(var1 == 4) {
			this.viewDistance = this.viewDistance + var2 & 3;
		}

		if(var1 == 5) {
			this.bobView = !this.bobView;
		}

		if(var1 == 6) {
			this.anaglyph3d = !this.anaglyph3d;
			Textures var6 = this.minecraft.textures;
			Iterator var7 = var6.pixelsMap.keySet().iterator();

			int var3;
			ImageData var4;
			while(var7.hasNext()) {
				var3 = ((Integer)var7.next()).intValue();
				var4 = (ImageData)var6.pixelsMap.get(Integer.valueOf(var3));
				var6.addTexture(var4, var3);
			}

			var7 = var6.idMap.keySet().iterator();

			while(var7.hasNext()) {
				String var8 = (String)var7.next();

				var4 = ImageData.loadImageFile(EagRuntime.getResourceStream(var8));
				var3 = ((Integer)var6.idMap.get(var8)).intValue();
				var6.addTexture(var4, var3);
			}
		}
		this.minecraft.soundManager.settingsChanged();
		this.save();
	}

	public final String getMessage(int var1) {
		return var1 == 0 ? "Music: " + (this.music ? "ON" : "OFF") : (var1 == 1 ? "Sound: " + (this.sound ? "ON" : "OFF") : (var1 == 2 ? "Invert mouse: " + (this.invertYMouse ? "ON" : "OFF") : (var1 == 3 ? "Show FPS: " + (this.showFramerate ? "ON" : "OFF") : (var1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.viewDistance] : (var1 == 5 ? "View bobbing: " + (this.bobView ? "ON" : "OFF") : (var1 == 6 ? "3d anaglyph: " + (this.anaglyph3d ? "ON" : "OFF") : ""))))));
	}

	private void load() {
		try {
			if(this.optionsFile.exists()) {
				BufferedReader var1 = new BufferedReader(new InputStreamReader(this.optionsFile.getInputStream()));
				String var2 = null;

				while(true) {
					var2 = var1.readLine();
					if(var2 == null) {
						var1.close();
						return;
					}

					String[] var5 = var2.split(":");
					if(var5[0].equals("music")) {
						this.music = var5[1].equals("true");
					}

					if(var5[0].equals("sound")) {
						this.sound = var5[1].equals("true");
					}

					if(var5[0].equals("invertYMouse")) {
						this.invertYMouse = var5[1].equals("true");
					}

					if(var5[0].equals("showFrameRate")) {
						this.showFramerate = var5[1].equals("true");
					}

					if(var5[0].equals("viewDistance")) {
						this.viewDistance = Integer.parseInt(var5[1]);
					}

					if(var5[0].equals("bobView")) {
						this.bobView = var5[1].equals("true");
					}

					if(var5[0].equals("anaglyph3d")) {
						this.anaglyph3d = var5[1].equals("true");
					}

					for(int var3 = 0; var3 < this.keys.length; ++var3) {
						if(var5[0].equals("key_" + this.keys[var3].name)) {
							this.keys[var3].key = Integer.parseInt(var5[1]);
						}
					}
				}
			}
		} catch (Exception var4) {
			System.out.println("Failed to load options");
			var4.printStackTrace();
		}
	}

	private void save() {
		try {
			PrintWriter var1 = new PrintWriter(this.optionsFile.getOutputStream());
			var1.println("music:" + this.music);
			var1.println("sound:" + this.sound);
			var1.println("invertYMouse:" + this.invertYMouse);
			var1.println("showFrameRate:" + this.showFramerate);
			var1.println("viewDistance:" + this.viewDistance);
			var1.println("bobView:" + this.bobView);
			var1.println("anaglyph3d:" + this.anaglyph3d);

			for(int var2 = 0; var2 < this.keys.length; ++var2) {
				var1.println("key_" + this.keys[var2].name + ":" + this.keys[var2].key);
			}

			var1.close();
		} catch (Exception var3) {
			System.out.println("Failed to save options");
			var3.printStackTrace();
		}
	}
}
