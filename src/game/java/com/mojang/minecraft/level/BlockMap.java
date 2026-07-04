package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Textures;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlockMap implements Serializable {
	public static final long serialVersionUID = 0L;
	private int width;
	private int depth;
	private int height;
	private BlockMap.Slot slot = new BlockMap.Slot((UnusedSyntheticClass)null);
	private BlockMap.Slot slot2 = new BlockMap.Slot((UnusedSyntheticClass)null);
	public List[] entityGrid;
	public List all = new ArrayList();
	private List tmp = new ArrayList();

	public BlockMap(int var1, int var2, int var3) {
		this.width = var1 / 16;
		this.depth = var2 / 16;
		this.height = var3 / 16;
		if(this.width == 0) {
			this.width = 1;
		}

		if(this.depth == 0) {
			this.depth = 1;
		}

		if(this.height == 0) {
			this.height = 1;
		}

		this.entityGrid = new ArrayList[this.width * this.depth * this.height];

		for(var1 = 0; var1 < this.width; ++var1) {
			for(var2 = 0; var2 < this.depth; ++var2) {
				for(var3 = 0; var3 < this.height; ++var3) {
					this.entityGrid[(var3 * this.depth + var2) * this.width + var1] = new ArrayList();
				}
			}
		}

	}

	public void insert(Entity var1) {
		this.all.add(var1);
		this.slot.init(var1.x, var1.y, var1.z).add(var1);
		var1.xOld = var1.x;
		var1.yOld = var1.y;
		var1.zOld = var1.z;
		var1.blockMap = this;
	}

	public void remove(Entity var1) {
		this.slot.init(var1.xOld, var1.yOld, var1.zOld).remove(var1);
		this.all.remove(var1);
	}

	public void moved(Entity var1) {
		BlockMap.Slot var2 = this.slot.init(var1.xOld, var1.yOld, var1.zOld);
		BlockMap.Slot var3 = this.slot2.init(var1.x, var1.y, var1.z);
		if(!var2.equals(var3)) {
			var2.remove(var1);
			var3.add(var1);
			var1.xOld = var1.x;
			var1.yOld = var1.y;
			var1.zOld = var1.z;
		}
	}

	public List getEntities(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.tmp.clear();
		return this.getEntities(var1, var2, var3, var4, var5, var6, var7, this.tmp);
	}

	public List getEntities(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7, List var8) {
		BlockMap.Slot var9 = this.slot.init(var2, var3, var4);
		BlockMap.Slot var10 = this.slot2.init(var5, var6, var7);

		for(int var11 = var9.xSlot - 1; var11 <= var10.xSlot + 1; ++var11) {
			for(int var12 = var9.ySlot - 1; var12 <= var10.ySlot + 1; ++var12) {
				for(int var13 = var9.zSlot - 1; var13 <= var10.zSlot + 1; ++var13) {
					if(var11 >= 0 && var12 >= 0 && var13 >= 0 && var11 < this.width && var12 < this.depth && var13 < this.height) {
						List var14 = this.entityGrid[(var13 * this.depth + var12) * this.width + var11];

						for(int var15 = 0; var15 < var14.size(); ++var15) {
							Entity var16 = (Entity)var14.get(var15);
							if(var16 != var1 && var16.intersects(var2, var3, var4, var5, var6, var7)) {
								var8.add(var16);
							}
						}
					}
				}
			}
		}

		return var8;
	}

	public void clear() {
		for(int var1 = 0; var1 < this.width; ++var1) {
			for(int var2 = 0; var2 < this.depth; ++var2) {
				for(int var3 = 0; var3 < this.height; ++var3) {
					this.entityGrid[(var3 * this.depth + var2) * this.width + var1].clear();
				}
			}
		}

	}

	public List getEntities(Entity var1, AABB var2) {
		this.tmp.clear();
		return this.getEntities(var1, var2.x0, var2.y0, var2.z0, var2.x1, var2.y1, var2.z1, this.tmp);
	}

	public List getEntities(Entity var1, AABB var2, List var3) {
		return this.getEntities(var1, var2.x0, var2.y0, var2.z0, var2.x1, var2.y1, var2.z1, var3);
	}

	public void tickAll() {
		for(int var1 = 0; var1 < this.all.size(); ++var1) {
			Entity var2 = (Entity)this.all.get(var1);
			var2.tick();
			if(var2.removed) {
				this.all.remove(var1--);
				this.slot.init(var2.xOld, var2.yOld, var2.zOld).remove(var2);
			} else {
				int var3 = (int)(var2.xOld / 16.0F);
				int var4 = (int)(var2.yOld / 16.0F);
				int var5 = (int)(var2.zOld / 16.0F);
				int var6 = (int)(var2.x / 16.0F);
				int var7 = (int)(var2.y / 16.0F);
				int var8 = (int)(var2.z / 16.0F);
				if(var3 != var6 || var4 != var7 || var5 != var8) {
					this.moved(var2);
				}
			}
		}

	}

	public void render(Frustum var1, Textures var2, float var3) {
		for(int var4 = 0; var4 < this.width; ++var4) {
			float var5 = (float)((var4 << 4) - 2);
			float var6 = (float)((var4 + 1 << 4) + 2);

			for(int var7 = 0; var7 < this.depth; ++var7) {
				float var8 = (float)((var7 << 4) - 2);
				float var9 = (float)((var7 + 1 << 4) + 2);

				for(int var10 = 0; var10 < this.height; ++var10) {
					List var11 = this.entityGrid[(var10 * this.depth + var7) * this.width + var4];
					if(var11.size() != 0) {
						float var12 = (float)((var10 << 4) - 2);
						float var13 = (float)((var10 + 1 << 4) + 2);
						boolean var14 = var1.cubeInFrustum(var5, var8, var12, var6, var9, var13);
						boolean var15 = var14 && var1.cubeFullyInFrustrum(var5, var8, var12, var6, var9, var13);
						if(var14) {
							for(int var16 = 0; var16 < var11.size(); ++var16) {
								Entity var17 = (Entity)var11.get(var16);
								if(var15 || var1.isVisible(var17.bb)) {
									((Entity)var11.get(var16)).render(var2, var3);
								}
							}
						}
					}
				}
			}
		}

	}

	class Slot implements Serializable {
		public static final long serialVersionUID = 0L;
		private int xSlot;
		private int ySlot;
		private int zSlot;

		private Slot() {
		}

		public BlockMap.Slot init(float var1, float var2, float var3) {
			this.xSlot = (int)(var1 / 16.0F);
			this.ySlot = (int)(var2 / 16.0F);
			this.zSlot = (int)(var3 / 16.0F);
			if(this.xSlot < 0) {
				this.xSlot = 0;
			}

			if(this.ySlot < 0) {
				this.ySlot = 0;
			}

			if(this.zSlot < 0) {
				this.zSlot = 0;
			}

			if(this.xSlot >= BlockMap.this.width) {
				this.xSlot = BlockMap.this.width - 1;
			}

			if(this.ySlot >= BlockMap.this.depth) {
				this.ySlot = BlockMap.this.depth - 1;
			}

			if(this.zSlot >= BlockMap.this.height) {
				this.zSlot = BlockMap.this.height - 1;
			}

			return this;
		}

		public void add(Entity var1) {
			if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
				BlockMap.this.entityGrid[(this.zSlot * BlockMap.this.depth + this.ySlot) * BlockMap.this.width + this.xSlot].add(var1);
			}

		}

		public void remove(Entity var1) {
			if(this.xSlot >= 0 && this.ySlot >= 0 && this.zSlot >= 0) {
				BlockMap.this.entityGrid[(this.zSlot * BlockMap.this.depth + this.ySlot) * BlockMap.this.width + this.xSlot].remove(var1);
			}

		}

		Slot(UnusedSyntheticClass var2) {
			this();
		}
	}
}
