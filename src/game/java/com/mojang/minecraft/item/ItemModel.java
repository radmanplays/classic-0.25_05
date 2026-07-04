package com.mojang.minecraft.item;

import com.mojang.minecraft.model.Cube;
import com.mojang.minecraft.model.Polygon;
import com.mojang.minecraft.model.Vertex;

public final class ItemModel {
	private Cube cube = new Cube(0, 0);

	public ItemModel(int var1) {
		Cube var10000 = this.cube;
		int var4 = var1;
		boolean var14 = true;
		var14 = true;
		var14 = true;
		float var3 = -2.0F;
		float var2 = -2.0F;
		float var15 = -2.0F;
		Cube var16 = var10000;
		var16.vertices = new Vertex[8];
		var16.polygons = new Polygon[6];
		Vertex var5 = new Vertex(var15, var2, var3, 0.0F, 0.0F);
		Vertex var6 = new Vertex(2.0F, var2, var3, 0.0F, 8.0F);
		Vertex var7 = new Vertex(2.0F, 2.0F, var3, 8.0F, 8.0F);
		Vertex var19 = new Vertex(var15, 2.0F, var3, 8.0F, 0.0F);
		Vertex var8 = new Vertex(var15, var2, 2.0F, 0.0F, 0.0F);
		Vertex var18 = new Vertex(2.0F, var2, 2.0F, 0.0F, 8.0F);
		Vertex var9 = new Vertex(2.0F, 2.0F, 2.0F, 8.0F, 8.0F);
		Vertex var17 = new Vertex(var15, 2.0F, 2.0F, 8.0F, 0.0F);
		var16.vertices[0] = var5;
		var16.vertices[1] = var6;
		var16.vertices[2] = var7;
		var16.vertices[3] = var19;
		var16.vertices[4] = var8;
		var16.vertices[5] = var18;
		var16.vertices[6] = var9;
		var16.vertices[7] = var17;
		float var10 = 0.25F;
		float var11 = 0.25F;
		float var12 = ((float)(var4 % 16) + (1.0F - var10)) / 16.0F;
		float var13 = ((float)(var4 / 16) + (1.0F - var11)) / 16.0F;
		var10 = ((float)(var4 % 16) + var10) / 16.0F;
		float var20 = ((float)(var4 / 16) + var11) / 16.0F;
		var16.polygons[0] = new Polygon(new Vertex[]{var18, var6, var7, var9}, var12, var13, var10, var20);
		var16.polygons[1] = new Polygon(new Vertex[]{var5, var8, var17, var19}, var12, var13, var10, var20);
		var16.polygons[2] = new Polygon(new Vertex[]{var18, var8, var5, var6}, var12, var13, var10, var20);
		var16.polygons[3] = new Polygon(new Vertex[]{var7, var19, var17, var9}, var12, var13, var10, var20);
		var16.polygons[4] = new Polygon(new Vertex[]{var6, var5, var19, var7}, var12, var13, var10, var20);
		var16.polygons[5] = new Polygon(new Vertex[]{var8, var18, var9, var17}, var12, var13, var10, var20);
	}

	public final void render() {
		this.cube.render(1.0F / 16.0F);
	}
}
