package com.mojang.minecraft;

import com.mojang.minecraft.character.Cube;
import com.mojang.minecraft.character.Vec3;
import com.mojang.minecraft.gamemode.GameMode;
import com.mojang.minecraft.gamemode.SurvivalGameMode;
import com.mojang.minecraft.gui.ChatScreen;
import com.mojang.minecraft.gui.DeathScreen;
import com.mojang.minecraft.gui.ErrorScreen;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.gui.Gui;
import com.mojang.minecraft.gui.PauseScreen;
import com.mojang.minecraft.gui.Screen;
import com.mojang.minecraft.item.Arrow;
import com.mojang.minecraft.item.Sign;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelIO;
import com.mojang.minecraft.level.levelgen.LevelGen;
import com.mojang.minecraft.level.liquid.Liquid;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.model.HumanoidModel;
import com.mojang.minecraft.particle.ParticleEngine;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.player.Inventory;
import com.mojang.minecraft.player.KeyboardInput;
import com.mojang.minecraft.player.Player;
import com.mojang.minecraft.renderer.Chunk;
import com.mojang.minecraft.renderer.DirtyChunkSorter;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.GameRenderer;
import com.mojang.minecraft.renderer.LevelRenderer;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import com.mojang.minecraft.renderer.texture.DynamicTexture;
import com.mojang.minecraft.renderer.texture.LavaTexture;
import com.mojang.minecraft.renderer.texture.WaterTexture;
import com.mojang.minecraft.sound.SoundManager;
import com.mojang.minecraft.tilerenderer.TileRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import com.mojang.util.GLAllocation;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.EnumPlatformType;
import net.lax1dude.eaglercraft.internal.IWebSocketFrame;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.FloatBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public final class Minecraft implements Runnable {
	public GameMode gamemode = new SurvivalGameMode(this);
	private boolean fullscreen = false;
	public int width;
	public int height;
	private Timer timer = new Timer(20.0F);
	public Level level;
	public LevelRenderer levelRenderer;
	public Player player;
	public ParticleEngine particleEngine;
	public User user = null;
	public String host;
	public volatile boolean pause = false;
	public Textures textures;
	public Font font;
	public Screen screen = null;
	public LevelLoaderListener loadingScreen = new LevelLoaderListener(this);
	public GameRenderer lighting = new GameRenderer(this);
	public LevelIO levelIo = new LevelIO(this.loadingScreen);
	public SoundManager soundManager = new SoundManager(this);
	private int frames = 0;
	public String loadMapUser = null;
	public int loadMapId = 0;
	public Gui gui;
	public boolean hideScreen = false;
	public HitResult hitResult;
	public Options options;
	String server;
	int port;
	volatile boolean running;
	public String fpsString;
	public boolean mouseGrabbed;
	public int oFrames;

	public Minecraft(int var3, int var4, boolean var5) {
		new HumanoidModel(0.0F);
		this.hitResult = null;
		this.server = null;
		this.port = 0;
		this.running = false;
		this.fpsString = "";
		this.mouseGrabbed = false;
		this.oFrames = 0;
		this.width = var3;
		this.height = var4;
		this.fullscreen = false;
	}

	public final void setScreen(Screen var1) {
		if(!(this.screen instanceof ErrorScreen)) {
			if(this.screen != null) {
				this.screen.removed();
			}

			if(var1 == null && this.player.health <= 0) {
				var1 = new DeathScreen();
			}

			this.screen = (Screen)var1;
			if(var1 != null) {
				if(this.mouseGrabbed) {
					this.player.releaseAllKeys();
					this.mouseGrabbed = false;
					Mouse.setGrabbed(false);
				}

				int var2 = this.width * 240 / this.height;
				int var3 = this.height * 240 / this.height;
				((Screen)var1).init(this, var2, var3);
				this.hideScreen = false;
			} else {
				this.grabMouse();
			}
		}
	}

	private static void checkGlError(String var0) {
		int var1 = GL11.glGetError();
		if(var1 != 0) {
			String var2 = GLU.gluErrorString(var1);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + var0);
			System.out.println(var1 + ": " + var2);
			throw new RuntimeException(var1 + ": " + var2);
		}

	}

	public final void destroy() {

		Minecraft var5 = this;
		try {
			if(var5.level != null) {
				LevelIO.save(var5.level, new VFile2("level.dat"));
			}
		} catch (Exception var2) {
			var2.printStackTrace();
		}

		EagRuntime.destroy();
	}

	public final void run() {
		this.running = true;

		try {
			Minecraft var4 = this;
			if(this.fullscreen) {
				Display.toggleFullscreen();
				this.width = Display.getWidth();
				this.height = Display.getHeight();
			} else {
				this.width = Display.getWidth();
				this.height = Display.getHeight();
			}

			Display.setTitle("Minecraft 0.24_SURVIVAL_TEST_03");

			Display.create();
			Keyboard.create();
			Mouse.create();

			checkGlError("Pre startup");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glClearDepth(1.0D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			checkGlError("Startup");
			this.options = new Options(this);
			this.textures = new Textures(this.options);
			this.textures.addDynamicTexture(new LavaTexture());
			this.textures.addDynamicTexture(new WaterTexture());
			this.font = new Font("/default.png", this.textures);
			IntBuffer var8 = GLAllocation.createIntBuffer(256);
			var8.clear().limit(256);
			this.levelRenderer = new LevelRenderer(this, this.textures);
			GL11.glViewport(0, 0, this.width, this.height);
			if(this.server != null && this.user != null) {
				this.level = null;
			} else {
				boolean var9 = false;

				try {
					Level var10 = null;
					//disable 0.24 level saving to make it accurate to the original where it regenerated every time
//					var10 = var4.levelIo.load(new VFile2("level.dat"));
					var9 = var10 != null;
					if(var9) {
						var4.loadLegacy(var10);
					}
				} catch (Exception var41) {
					var41.printStackTrace();
				}
			}

			if(this.level == null) {
				this.generateLevel(1);
			}

			this.particleEngine = new ParticleEngine(this.level, this.textures);

			checkGlError("Post startup");
			this.gui = new Gui(this, this.width, this.height);
		} catch (Exception var49) {
			var49.printStackTrace();
			System.out.println("Failed to start Minecraft");
			return;
		}

		long var1 = System.currentTimeMillis();
		int var3 = 0;

		try {
			while(this.running) {
				if(Display.isCloseRequested()) {
						this.running = false;
				}

					try {
						Timer var50 = this.timer;
						long var54 = System.currentTimeMillis();
						long var57 = var54 - var50.msPerTick;
						long var62 = System.nanoTime() / 1000000L;
						double var77;
						if(var57 > 1000L) {
							long var71 = var62 - var50.passedTime;
							var77 = (double)var57 / (double)var71;
							var50.averageFrameTime += (var77 - var50.averageFrameTime) * (double)0.2F;
							var50.msPerTick = var54;
							var50.passedTime = var62;
						}

						if(var57 < 0L) {
							var50.msPerTick = var54;
							var50.passedTime = var62;
						}

						double var72 = (double)var62 / 1000.0D;
						var77 = (var72 - var50.lastTime) * var50.averageFrameTime;
						var50.lastTime = var72;
						if(var77 < 0.0D) {
							var77 = 0.0D;
						}

						if(var77 > 1.0D) {
							var77 = 1.0D;
						}

						var50.ticks = (float)((double)var50.ticks + var77 * (double)var50.fps * (double)var50.ticksPerSecond);
						var50.frames = (int)var50.ticks;
						if(var50.frames > 100) {
							var50.frames = 100;
						}

						var50.ticks -= (float)var50.frames;
						var50.alpha = var50.ticks;

						for(int var51 = 0; var51 < this.timer.frames; ++var51) {
							++this.frames;
							this.tick();
						}

						checkGlError("Pre render");
						this.gamemode.render(this.timer.alpha);
						float var55 = this.timer.alpha;
						GameRenderer var52 = this.lighting;
						if(var52.displayActive && !Display.isActive()) {
							var52.minecraft.pauseScreen();
						}
						
						this.soundManager.updatePosition(this.player, this.timer.alpha);

						var52.displayActive = Display.isActive();
						int var56;
						int var58;
						int var63;
						if(var52.minecraft.mouseGrabbed) {
							var56 = 0;
							var58 = 0;
							var56 = Mouse.getDX();
							var58 = Mouse.getDY();

							byte var61 = 1;
							if(var52.minecraft.options.invertYMouse) {
								var61 = -1;
							}

							var52.minecraft.player.turn((float)var56, (float)(var58 * var61));
						}

						if(!var52.minecraft.hideScreen) {
							if (Display.wasResized()) {
								if(Display.getHeight() != 0) {
									this.width = Display.getWidth();
									this.height = Display.getHeight();
									if(this.gui !=null) {
										this.gui = new Gui(this, this.width, this.height);
									}
									
									if(this.screen != null) {
										Screen sc = this.screen;
										this.setScreen((Screen)null);
										this.setScreen(sc);
									}
								}
							}
							var56 = var52.minecraft.width * 240 / var52.minecraft.height;
							var58 = var52.minecraft.height * 240 / var52.minecraft.height;
							int var65 = Mouse.getX() * var56 / var52.minecraft.width;
							var63 = var58 - Mouse.getY() * var58 / var52.minecraft.height - 1;
							if(var52.minecraft.level != null) {
								float var83 = var55;
								GameRenderer var79 = var52;
								int var81 = 0;

								while(true) {
									if(var81 >= 2) {
										GL11.glColorMask(true, true, true, false);
										break;
									}

									if(var79.minecraft.options.anaglyph3d) {
										if(var81 == 0) {
											GL11.glColorMask(false, true, true, false);
										} else {
											GL11.glColorMask(true, false, false, false);
										}
									}

									Player var5 = var79.minecraft.player;
									Level var6 = var79.minecraft.level;
									LevelRenderer var59 = var79.minecraft.levelRenderer;
									ParticleEngine var64 = var79.minecraft.particleEngine;
									GL11.glViewport(0, 0, var79.minecraft.width, var79.minecraft.height);
									Level var17 = var79.minecraft.level;
									Player var18 = var79.minecraft.player;
									float var19 = 1.0F / (float)(4 - var79.minecraft.options.viewDistance);
									var19 = 1.0F - (float)Math.pow((double)var19, 0.25D);
									float var20 = (float)(var17.skyColor >> 16 & 255) / 255.0F;
									float var21 = (float)(var17.skyColor >> 8 & 255) / 255.0F;
									float var68 = (float)(var17.skyColor & 255) / 255.0F;
									var79.fogRed = (float)(var17.fogColor >> 16 & 255) / 255.0F;
									var79.fogGreen = (float)(var17.fogColor >> 8 & 255) / 255.0F;
									var79.fogBlue = (float)(var17.fogColor & 255) / 255.0F;
									var79.fogRed += (var20 - var79.fogRed) * var19;
									var79.fogGreen += (var21 - var79.fogGreen) * var19;
									var79.fogBlue += (var68 - var79.fogBlue) * var19;
									var79.fogRed *= var79.fogColorMultiplier;
									var79.fogGreen *= var79.fogColorMultiplier;
									var79.fogBlue *= var79.fogColorMultiplier;
									Tile var22 = Tile.tiles[var17.getTile((int)var18.x, (int)(var18.y + 0.12F), (int)var18.z)];
									if(var22 != null && var22.getLiquidType() != Liquid.none) {
										Liquid var23 = var22.getLiquidType();
										if(var23 == Liquid.water) {
											var79.fogRed = 0.02F;
											var79.fogGreen = 0.02F;
											var79.fogBlue = 0.2F;
										} else if(var23 == Liquid.lava) {
											var79.fogRed = 0.6F;
											var79.fogGreen = 0.1F;
											var79.fogBlue = 0.0F;
										}
									}

									float var24;
									float var84;
									float var101;
									if(var79.minecraft.options.anaglyph3d) {
										var101 = (var79.fogRed * 30.0F + var79.fogGreen * 59.0F + var79.fogBlue * 11.0F) / 100.0F;
										var84 = (var79.fogRed * 30.0F + var79.fogGreen * 70.0F) / 100.0F;
										var24 = (var79.fogRed * 30.0F + var79.fogBlue * 70.0F) / 100.0F;
										var79.fogRed = var101;
										var79.fogGreen = var84;
										var79.fogBlue = var24;
									}

									GL11.glClearColor(var79.fogRed, var79.fogGreen, var79.fogBlue, 0.0F);
									GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
									var79.fogColorMultiplier = 1.0F;
									GL11.glEnable(GL11.GL_CULL_FACE);
									var79.renderDistance = (float)(512 >> (var79.minecraft.options.viewDistance << 1));
									GL11.glMatrixMode(GL11.GL_PROJECTION);
									GL11.glLoadIdentity();
									var19 = 0.07F;
									if(var79.minecraft.options.anaglyph3d) {
										GL11.glTranslatef((float)(-((var81 << 1) - 1)) * var19, 0.0F, 0.0F);
									}

									Player var70 = var79.minecraft.player;
									float var98 = 70.0F;
									if(var70.health <= 0) {
										var101 = (float)var70.deathTime + var83;
										var98 /= (1.0F - 500.0F / (var101 + 500.0F)) * 2.0F + 1.0F;
									}

									GLU.gluPerspective(var98, (float)var79.minecraft.width / (float)var79.minecraft.height, 0.05F, var79.renderDistance);
									GL11.glMatrixMode(GL11.GL_MODELVIEW);
									GL11.glLoadIdentity();
									if(var79.minecraft.options.anaglyph3d) {
										GL11.glTranslatef((float)((var81 << 1) - 1) * 0.1F, 0.0F, 0.0F);
									}

									var79.renderHurtFrames(var83);
									if(var79.minecraft.options.bobView) {
										var79.cameraBob(var83);
									}

									var70 = var79.minecraft.player;
									GL11.glTranslatef(0.0F, 0.0F, -0.1F);
									GL11.glRotatef(var70.xRotO + (var70.xRot - var70.xRotO) * var83, 1.0F, 0.0F, 0.0F);
									GL11.glRotatef(var70.yRotO + (var70.yRot - var70.yRotO) * var83, 0.0F, 1.0F, 0.0F);
									var98 = var70.xo + (var70.x - var70.xo) * var83;
									var101 = var70.yo + (var70.y - var70.yo) * var83;
									var84 = var70.zo + (var70.z - var70.zo) * var83;
									GL11.glTranslatef(-var98, -var101, -var84);
									GameRenderer var74 = var79;
									var18 = var79.minecraft.player;
									var19 = var18.xRotO + (var18.xRot - var18.xRotO) * var83;
									var20 = var18.yRotO + (var18.yRot - var18.yRotO) * var83;
									var21 = var18.xo + (var18.x - var18.xo) * var83;
									var68 = var18.yo + (var18.y - var18.yo) * var83;
									var98 = var18.zo + (var18.z - var18.zo) * var83;
									Vec3 var102 = new Vec3(var21, var68, var98);
									var84 = (float)Math.cos((double)(-var20) * Math.PI / 180.0D - Math.PI);
									var24 = (float)Math.sin((double)(-var20) * Math.PI / 180.0D - Math.PI);
									var20 = (float)Math.cos((double)(-var19) * Math.PI / 180.0D);
									float var25 = (float)Math.sin((double)(-var19) * Math.PI / 180.0D);
									var19 = var24 * var20;
									var24 = var25;
									var84 *= var20;
									var20 = var79.minecraft.gamemode.getPickRange();
									Vec3 var104 = var102.addVector(var19 * var20, var25 * var20, var84 * var20);
									var79.minecraft.hitResult = var79.minecraft.level.clip(var102, var104);
									var102 = new Vec3(var21, var68, var98);
									var68 = var20;
									if(var79.minecraft.hitResult != null) {
										Vec3 var75 = var79.minecraft.hitResult.vec;
										var21 = var102.x - var75.x;
										var98 = var102.y - var75.y;
										var68 = var102.z - var75.z;
										var68 = (float)Math.sqrt((double)(var21 * var21 + var98 * var98 + var68 * var68));
									}

									List var85 = var79.minecraft.level.blockMap.getEntities(var18, var18.bb.expand(var19 * var68, var25 * var68, var84 * var68));

									int var94;
									for(var94 = 0; var94 < var85.size(); ++var94) {
										Entity var95 = (Entity)var85.get(var94);
										if(var95.isPickable()) {
											var98 = 0.1F;
											AABB var103 = var95.bb.grow(var98, var98, var98);

											for(var25 = 0.0F; var25 < var68; var25 += 0.05F) {
												if(var103.contains(var102.addVector(var19 * var25, var24 * var25, var84 * var25))) {
													var68 = var25;
													var74.minecraft.hitResult = new HitResult(var95);
													break;
												}
											}
										}
									}

									Frustum var82 = Frustum.calculateFrustum();
									Frustum var86 = var82;
									LevelRenderer var76 = var79.minecraft.levelRenderer;

									int var87;
									for(var87 = 0; var87 < var76.chunks.length; ++var87) {
										var76.chunks[var87].isInFrustum(var86);
									}

									var76 = var79.minecraft.levelRenderer;
									List<Chunk> var90 = new ArrayList<>(var76.allDirtyChunks);
									var90.sort(new DirtyChunkSorter(var5));
									int var91 = 4;
									Iterator var96 = var90.iterator();

									while(var96.hasNext()) {
										Chunk var99 = (Chunk)var96.next();
										var99.rebuild(false);
										var76.allDirtyChunks.remove(var99);
										--var91;
										if(var91 == 0) {
											break;
										}
									}

									var79.setupFog();
									GL11.glEnable(GL11.GL_FOG);
									var59.render(var5, 0);
									int var53;
									if(var6.isSolid(var5.x, var5.y, var5.z, 0.1F)) {
										var53 = (int)var5.x;
										int var78 = (int)var5.y;
										int var88 = (int)var5.z;

										for(var87 = var53 - 1; var87 <= var53 + 1; ++var87) {
											for(var91 = var78 - 1; var91 <= var78 + 1; ++var91) {
												for(var94 = var88 - 1; var94 <= var88 + 1; ++var94) {
													var59.render(var87, var91, var94);
												}
											}
										}
									}

//									var79.toggleLight(true);
									var59.level.blockMap.render(var82, var59.textures, var83);
//									var79.toggleLight(false);
									var79.setupFog();
									var64.render(var5, var83);
									var59.renderSurroundingGround();
									GL11.glDisable(GL11.GL_LIGHTING);
									var79.setupFog();
									var59.renderClouds(var83);
									var79.setupFog();
									GL11.glEnable(GL11.GL_LIGHTING);
									if(var79.minecraft.hitResult != null) {
//										GL11.glDisable(GL11.GL_LIGHTING);
										GL11.glDisable(GL11.GL_ALPHA_TEST);
										var59.renderHit(var79.minecraft.hitResult, 0, var5.inventory.getSelected());
										boolean var89 = false;
										HitResult var80 = var79.minecraft.hitResult;
										GL11.glEnable(GL11.GL_BLEND);
										GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
										GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
										GL11.glLineWidth(2.0F);
										GL11.glDepthMask(false);
										float var92 = 0.002F;
										(new AABB((float)var80.x, (float)var80.y, (float)var80.z, (float)(var80.x + 1), (float)(var80.y + 1), (float)(var80.z + 1))).grow(var92, var92, var92).render();
										GL11.glDepthMask(true);
										GL11.glDisable(GL11.GL_BLEND);
										GL11.glEnable(GL11.GL_ALPHA_TEST);
//										GL11.glEnable(GL11.GL_LIGHTING);
									}

									GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
									var79.setupFog();
//									var59.renderSurroundingGround();
									GL11.glEnable(GL11.GL_BLEND);
//									GL11.glColorMask(false, false, false, false);
									var53 = var59.render(var5, 1);
//									GL11.glColorMask(true, true, true, true);
									if(var79.minecraft.options.anaglyph3d) {
										if(var81 == 0) {
											GL11.glColorMask(false, true, true, false);
										} else {
											GL11.glColorMask(true, false, false, false);
										}
									}

									if(var53 > 0) {
										GL11.glEnable(GL11.GL_TEXTURE_2D);
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, var59.textures.loadTexture("/terrain.png"));
										GL11.glEnable(GL11.GL_ALPHA_TEST);
										GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
										GL11.glCallLists(var59.ib);
										GL11.glDisable(GL11.GL_ALPHA_TEST);
										GL11.glDisable(GL11.GL_TEXTURE_2D);
									}

									GL11.glDepthMask(true);
									GL11.glDisable(GL11.GL_BLEND);
//									GL11.glDisable(GL11.GL_LIGHTING);
									GL11.glDisable(GL11.GL_FOG);
									GL11.glDisable(GL11.GL_TEXTURE_2D);
									GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
									var84 = var83;
									GL11.glLoadIdentity();
									if(var79.minecraft.options.anaglyph3d) {
										GL11.glTranslatef((float)((var81 << 1) - 1) * 0.1F, 0.0F, 0.0F);
									}

									var79.renderHurtFrames(var83);
									if(var79.minecraft.options.bobView) {
										var79.cameraBob(var83);
									}

									TileRenderer var97 = var79.tileRenderer;
									var21 = var97.oProgress + (var97.progress - var97.oProgress) * var83;
									var70 = var97.minecraft.player;
									GL11.glPushMatrix();
									GL11.glRotatef(var70.xRotO + (var70.xRot - var70.xRotO) * var83, 1.0F, 0.0F, 0.0F);
									GL11.glRotatef(var70.yRotO + (var70.yRot - var70.yRotO) * var83, 0.0F, 1.0F, 0.0F);
									var97.minecraft.lighting.toggleLight(true);
									GL11.glPopMatrix();
									GL11.glPushMatrix();
									var98 = 0.8F;
									if(var97.move) {
										var101 = ((float)var97.rot + var83) / 7.0F;
										var84 = (float)Math.sin((double)var101 * Math.PI);
										var24 = (float)Math.sin(Math.sqrt((double)var101) * Math.PI);
										GL11.glTranslatef(-var24 * 0.4F, (float)Math.sin(Math.sqrt((double)var101) * Math.PI * 2.0D) * 0.2F, -var84 * 0.2F);
									}

									GL11.glTranslatef(0.7F * var98, -0.65F * var98 - (1.0F - var21) * 0.6F, -0.9F * var98);
									GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
									GL11.glEnable(GL11.GL_NORMALIZE);
									if(var97.move) {
										var101 = ((float)var97.rot + var84) / 7.0F;
										var84 = (float)Math.sin((double)(var101 * var101) * Math.PI);
										var24 = (float)Math.sin(Math.sqrt((double)var101) * Math.PI);
										GL11.glRotatef(var24 * 80.0F, 0.0F, 1.0F, 0.0F);
										GL11.glRotatef(-var84 * 20.0F, 1.0F, 0.0F, 0.0F);
									}

									var101 = var97.minecraft.level.getBrightness((int)var70.x, (int)var70.y, (int)var70.z);
									GL11.glColor4f(var101, var101, var101, 1.0F);
									GL11.glEnable(GL11.GL_TEXTURE_2D);
									Tesselator var93 = Tesselator.instance;
									if(var97.tile != null) {
										var24 = 0.4F;
										GL11.glScalef(var24, var24, var24);
										GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
										GL11.glEnable(GL11.GL_ALPHA_TEST);
										GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, var97.minecraft.textures.loadTexture("/terrain.png"));
										var97.tile.renderGuiTile(var93);
										GL11.glDisable(GL11.GL_ALPHA_TEST);
									} else {
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, var97.minecraft.textures.loadTexture("/char.png"));
										GL11.glScalef(1.0F, -1.0F, -1.0F);
										GL11.glTranslatef(0.0F, 0.2F, 0.0F);
										GL11.glRotatef(-120.0F, 0.0F, 0.0F, 1.0F);
										GL11.glScalef(1.0F, 1.0F, 1.0F);
										var24 = 1.0F / 16.0F;
										Cube var100 = var97.minecraft.player.getModel().leftArm;
										if(!var100.isHidden) {
											if(!var100.compiled) {
												var100.translateTo(var24);
											}

											GL11.glCallList(var100.list);
										}
									}

									GL11.glDisable(GL11.GL_NORMALIZE);
									GL11.glDisable(GL11.GL_TEXTURE_2D);
									GL11.glPopMatrix();
									var97.minecraft.lighting.toggleLight(false);
									if(!var79.minecraft.options.anaglyph3d) {
										break;
									}

									++var81;
								}

								var52.minecraft.gui.render(var55, var52.minecraft.screen != null, var65, var63);
							} else {
								GL11.glViewport(0, 0, var52.minecraft.width, var52.minecraft.height);
								GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
								GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
								GL11.glMatrixMode(GL11.GL_PROJECTION);
								GL11.glLoadIdentity();
								GL11.glMatrixMode(GL11.GL_MODELVIEW);
								GL11.glLoadIdentity();
								var52.init();
							}

							if(var52.minecraft.screen != null) {
								var52.minecraft.screen.render(var65, var63);
							}

							Display.update();
						}

						checkGlError("Post render");
						++var3;
					} catch (Exception var45) {
						this.setScreen(new ErrorScreen("Client error", "The game broke! [" + var45 + "]"));
						var45.printStackTrace();
					}

					while(System.currentTimeMillis() >= var1 + 1000L) {
						this.fpsString = var3 + " fps, " + Chunk.updates + " chunk updates";
						Chunk.updates = 0;
						var1 += 1000L;
						var3 = 0;
					}
				}

			return;
		} catch (StopGameException var46) {
			return;
		} catch (Exception var47) {
			var47.printStackTrace();
		} finally {
			this.destroy();
		}

	}

	public final void grabMouse() {
		if(!this.mouseGrabbed) {
			this.mouseGrabbed = true;
				Mouse.setGrabbed(true);

			this.setScreen((Screen)null);
			this.oFrames = this.frames + 10000;
		}
	}

	public final void pauseScreen() {
		if(this.screen == null && (!(this.screen instanceof PauseScreen))) {
			this.setScreen(new PauseScreen());
		}
	}
	
	private int saveCountdown = 600;

	private void levelSave() {
	    if (level == null) return;

	    saveCountdown--;
	    if (saveCountdown <= 0) {
	    	LevelIO.save(this.level, new VFile2("level.dat"));
	        saveCountdown = 600;
	    }
	}

	private void clickMouse(int var1) {
		TileRenderer var6;
		if(var1 == 0) {
			var6 = this.lighting.tileRenderer;
			var6.rot = -1;
			var6.move = true;
		}

		int var2;
		if(var1 == 1) {
			var2 = this.player.inventory.getSelected();
			if(var2 > 0 && this.gamemode.removeResource(this.player, var2)) {
				var6 = this.lighting.tileRenderer;
				var6.progress = 0.0F;
				return;
			}
		}

		if(this.hitResult != null) {
			if(this.hitResult.type == 1) {
				this.hitResult.entity.hurt(this.player, 4);
			} else {
				if(this.hitResult.type == 0) {
					var2 = this.hitResult.x;
					int var3 = this.hitResult.y;
					int var4 = this.hitResult.z;
					if(var1 != 0) {
						if(this.hitResult.f == 0) {
							--var3;
						}

						if(this.hitResult.f == 1) {
							++var3;
						}

						if(this.hitResult.f == 2) {
							--var4;
						}

						if(this.hitResult.f == 3) {
							++var4;
						}

						if(this.hitResult.f == 4) {
							--var2;
						}

						if(this.hitResult.f == 5) {
							++var2;
						}
					}

					Tile var5 = Tile.tiles[this.level.getTile(var2, var3, var4)];
					if(var1 == 0) {
						if(var5 != Tile.unbreakable || this.player.userType >= 100) {
							this.gamemode.startDestroyBlock(var2, var3, var4);
							return;
						}
					} else {
						int var8 = this.player.inventory.getSelected();
						if(var8 <= 0) {
							return;
						}

						Tile var9 = Tile.tiles[this.level.getTile(var2, var3, var4)];
						if(var9 == null || var9 == Tile.water || var9 == Tile.calmWater || var9 == Tile.lava || var9 == Tile.calmLava) {
							AABB var10 = Tile.tiles[var8].getTileAABB(var2, var3, var4);
							if(var10 == null || (this.player.bb.intersects(var10) ? false : this.level.isFree(var10))) {
								if(!this.gamemode.removeResource(var8)) {
									return;
								}


								this.level.netSetTile(var2, var3, var4, var8);
								var6 = this.lighting.tileRenderer;
								var6.progress = 0.0F;
								Tile.tiles[var8].onPlace(this.level, var2, var3, var4);
							}
						}
					}
				}

			}
		}
	}

	private void tick() {
		SoundManager var1 = this.soundManager;
		if(System.currentTimeMillis() > var1.lastMusic && var1.playMusic()) {
			var1.lastMusic = System.currentTimeMillis() + (long)var1.random.nextInt(900000) + 300000L;
		}

		Gui var14 = this.gui;
		++var14.tickCounter;

		int var17;
		for(var17 = 0; var17 < var14.messages.size(); ++var17) {
			++((GuiMessage)var14.messages.get(var17)).counter;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textures.loadTexture("/terrain.png"));
		Textures var15 = this.textures;

		for(var17 = 0; var17 < var15.textureList.size(); ++var17) {
			DynamicTexture var3 = (DynamicTexture)var15.textureList.get(var17);
			var3.anaglyph = var15.options.anaglyph3d;
			var3.tick();
			var15.pixels.clear();
			var15.pixels.put(var3.pixels);
			var15.pixels.position(0).limit(var3.pixels.length);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var3.tex % 16 << 4, var3.tex / 16 << 4, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)var15.pixels);
		}

		int var4;
		int var41;
		int var46;

		if(this.screen == null && this.player.health <= 0) {
			this.setScreen((Screen)null);
		}

		if(this.screen == null) {
			if(Mouse.isMouseGrabbed() || Mouse.isActuallyGrabbed()) {
				this.mouseGrabbed = true;
			}
			int var18;
			while(Mouse.next()) {
				var18 = Mouse.getEventDWheel();
				if(var18 != 0) {
					this.player.inventory.swapPaint(var18);
				}

				if(this.screen == null) {
					if(!this.mouseGrabbed && Mouse.getEventButtonState()) {
						this.grabMouse();
					} else {
						if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
							this.clickMouse(0);
							this.oFrames = this.frames;
						}

						if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
							this.clickMouse(1);
							this.oFrames = this.frames;
						}

						if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.hitResult != null) {
							var17 = this.level.getTile(this.hitResult.x, this.hitResult.y, this.hitResult.z);
							if(var17 == Tile.grass.id) {
								var17 = Tile.dirt.id;
							}

							Inventory var24 = this.player.inventory;
							var41 = var24.containsTileAt(var17);
							if(var41 >= 0) {
								var24.selected = var41;
							}
						}
					}
				}

				if(this.screen != null) {
					this.screen.mouseEvent();
				}
			}

			label325:
			while(true) {
				do {
					do {
						if(!Keyboard.next()) {
							if(this.screen == null) {
								if(Mouse.isButtonDown(0) && (float)(this.frames - this.oFrames) >= this.timer.ticksPerSecond / 4.0F && this.mouseGrabbed) {
									this.clickMouse(0);
									this.oFrames = this.frames;
								}

								if(Mouse.isButtonDown(1) && (float)(this.frames - this.oFrames) >= this.timer.ticksPerSecond / 4.0F && this.mouseGrabbed) {
									this.clickMouse(1);
									this.oFrames = this.frames;
								}
							}

							boolean var27 = this.screen == null && Mouse.isButtonDown(0) && this.mouseGrabbed;
							boolean var30 = false;
							if(var27 && this.hitResult != null && this.hitResult.type == 0) {
								var4 = this.hitResult.x;
								var41 = this.hitResult.y;
								var46 = this.hitResult.z;
								this.gamemode.stopDestroyBlock(var4, var41, var46, this.hitResult.f);
							} else {
								this.gamemode.tick();
							}
							break label325;
						}

						this.player.setKey(Keyboard.getEventKey(), Keyboard.getEventKeyState());
					} while(!Keyboard.getEventKeyState());

					if(this.screen != null) {
						this.screen.keyboardEvent();
					}

					if(this.screen == null) {
						if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
							this.pauseScreen();
						}

						if(Keyboard.getEventKey() == this.options.build.key && true) {
							this.level.addEntity(new Sign(this, this.player.x, this.player.y, this.player.z, this.player.yRot));
						}

						if(Keyboard.getEventKey() == Keyboard.KEY_TAB && true) {
							this.level.addEntity(new Arrow(this, this.player, this.player.x, this.player.y, this.player.z, this.player.yRot, this.player.xRot));
						}

						Keyboard.getEventKey();
						if(Keyboard.getEventKey() == this.options.chat.key && false) {
							this.player.releaseAllKeys();
							this.setScreen(new ChatScreen());
						}
					}

					for(var18 = 0; var18 < 9; ++var18) {
						if(Keyboard.getEventKey() == var18 + 2) {
							this.player.inventory.selected = var18;
						}
					}
				} while(Keyboard.getEventKey() != this.options.toggleFog.key);

				this.options.setOption(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
			}
		}

		if(this.screen != null) {
			this.oFrames = this.frames + 10000;
		}

		if(this.screen != null) {
			this.screen.updateEvents();
			if(this.screen != null) {
				this.screen.tick();
			}
		}

		if(this.level != null) {
			GameRenderer var22 = this.lighting;
			TileRenderer var32 = var22.tileRenderer;
			var32.oProgress = var32.progress;
			if(var32.move) {
				++var32.rot;
				if(var32.rot == 7) {
					var32.rot = 0;
					var32.move = false;
				}
			}

			Player var28 = var32.minecraft.player;
			var4 = var28.inventory.getSelected();
			Tile var48 = null;
			if(var4 > 0) {
				var48 = Tile.tiles[var4];
			}

			float var51 = 0.4F;
			float var50 = var48 == var32.tile ? 1.0F : 0.0F;
			float var54 = var50 - var32.progress;
			if(var54 < -var51) {
				var54 = -var51;
			}

			if(var54 > var51) {
				var54 = var51;
			}

			var32.progress += var54;
			if(var32.progress < 0.1F) {
				var32.tile = var48;
			}

			LevelRenderer var25 = this.levelRenderer;
			++var25.cloudTickCounter;
			this.level.tickEntities();
//			if(!this.isOnlineClient()) {
				this.level.tick();
//			}

			this.particleEngine.tick();
			levelSave();
		}

	}

//	public final boolean isOnlineClient() {
//		return this.networkClient != null;
//	}

	public final void generateLevel(int var1) {
		String var2 = this.user != null ? this.user.name : "anonymous";
		LevelGen var3 = new LevelGen(this.loadingScreen);
		Level var4 = var3.generateLevel(var2, 128 << var1, 128 << var1, 64);
		this.loadLegacy(var4);
	}

	public final void loadLegacy(Level var1) {
//		if(!this.applet.getDocumentBase().getHost().toLowerCase().endsWith("minecraft.net")) {
//			var1 = null;
//		}

		this.level = var1;
		if(var1 != null) {
			var1.rendererContext = this;
		}

		if(this.levelRenderer != null) {
			LevelRenderer var2 = this.levelRenderer;
			if(var2.level != null) {
				var2.level.removeListener(var2);
			}

			var2.level = var1;
			if(var1 != null) {
				var1.addListener(var2);
				var2.compileSurroundingGround();
			}
		}

		if(this.particleEngine != null) {
			ParticleEngine var4 = this.particleEngine;
			var1.particleEngine = var4;
			var4.particles.clear();
		}

		this.player = new Player(var1, new KeyboardInput(this.options));
		this.player.resetPos();
		var1.player = this.player;
		System.gc();
	}

	static enum OS {
		linux,
		solaris,
		windows,
		macos,
		unknown;
	}
}
