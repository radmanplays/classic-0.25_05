package com.mojang.minecraft.level;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Textures;
import java.util.ArrayList;
import java.util.List;

public final class BlockMap {
	public int width;
	public int depth;
	public int height;
	Slot slot = new Slot(this, (UnusedSyntheticClass)null);
	Slot slot2 = new Slot(this, (UnusedSyntheticClass)null);
	public List[] entityGrid;
	public List all = new ArrayList();
	List tmp = new ArrayList();

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

	public final List getEntities(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7, List var8) {
		Slot var9 = this.slot.init(var2, var3, var4);
		Slot var10 = this.slot2.init(var5, var6, var7);

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

	public final List getEntities(Entity var1, AABB var2) {
		this.tmp.clear();
		return this.getEntities(var1, var2.x0, var2.y0, var2.z0, var2.x1, var2.y1, var2.z1, this.tmp);
	}

	public final void render(Frustum var1, Textures var2, float var3) {
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
}
