package com.mojang.minecraft.model;

import org.lwjgl.opengl.GL11;

public final class Cube {
	public Vertex[] vertices;
	public Polygon[] polygons;
	private int xTexOffs;
	private int yTexOffs;
	private float x;
	private float y;
	private float z;
	public float xRot;
	public float yRot;
	public float zRot;
	public boolean compiled = false;
	public int list = 0;
	public boolean mirror = false;
	public boolean showModel = true;
	private boolean isHidden = false;

	public Cube(int var1, int var2) {
		this.xTexOffs = var1;
		this.yTexOffs = var2;
	}

	public final void addBox(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
		this.vertices = new Vertex[8];
		this.polygons = new Polygon[6];
		float var8 = var1 + (float)var4;
		float var9 = var2 + (float)var5;
		float var10 = var3 + (float)var6;
		var1 -= var7;
		var2 -= var7;
		var3 -= var7;
		var8 += var7;
		var9 += var7;
		var10 += var7;
		if(this.mirror) {
			var7 = var8;
			var8 = var1;
			var1 = var7;
		}

		Vertex var20 = new Vertex(var1, var2, var3, 0.0F, 0.0F);
		Vertex var11 = new Vertex(var8, var2, var3, 0.0F, 8.0F);
		Vertex var12 = new Vertex(var8, var9, var3, 8.0F, 8.0F);
		Vertex var18 = new Vertex(var1, var9, var3, 8.0F, 0.0F);
		Vertex var13 = new Vertex(var1, var2, var10, 0.0F, 0.0F);
		Vertex var15 = new Vertex(var8, var2, var10, 0.0F, 8.0F);
		Vertex var21 = new Vertex(var8, var9, var10, 8.0F, 8.0F);
		Vertex var14 = new Vertex(var1, var9, var10, 8.0F, 0.0F);
		this.vertices[0] = var20;
		this.vertices[1] = var11;
		this.vertices[2] = var12;
		this.vertices[3] = var18;
		this.vertices[4] = var13;
		this.vertices[5] = var15;
		this.vertices[6] = var21;
		this.vertices[7] = var14;
		this.polygons[0] = new Polygon(new Vertex[]{var15, var11, var12, var21}, this.xTexOffs + var6 + var4, this.yTexOffs + var6, this.xTexOffs + var6 + var4 + var6, this.yTexOffs + var6 + var5);
		this.polygons[1] = new Polygon(new Vertex[]{var20, var13, var14, var18}, this.xTexOffs, this.yTexOffs + var6, this.xTexOffs + var6, this.yTexOffs + var6 + var5);
		this.polygons[2] = new Polygon(new Vertex[]{var15, var13, var20, var11}, this.xTexOffs + var6, this.yTexOffs, this.xTexOffs + var6 + var4, this.yTexOffs + var6);
		this.polygons[3] = new Polygon(new Vertex[]{var12, var18, var14, var21}, this.xTexOffs + var6 + var4, this.yTexOffs, this.xTexOffs + var6 + var4 + var4, this.yTexOffs + var6);
		this.polygons[4] = new Polygon(new Vertex[]{var11, var20, var18, var12}, this.xTexOffs + var6, this.yTexOffs + var6, this.xTexOffs + var6 + var4, this.yTexOffs + var6 + var5);
		this.polygons[5] = new Polygon(new Vertex[]{var13, var15, var21, var14}, this.xTexOffs + var6 + var4 + var6, this.yTexOffs + var6, this.xTexOffs + var6 + var4 + var6 + var4, this.yTexOffs + var6 + var5);
		if(this.mirror) {
			for(int var16 = 0; var16 < this.polygons.length; ++var16) {
				Polygon var17 = this.polygons[var16];
				Vertex[] var19 = new Vertex[var17.vertices.length];

				for(var4 = 0; var4 < var17.vertices.length; ++var4) {
					var19[var4] = var17.vertices[var17.vertices.length - var4 - 1];
				}

				var17.vertices = var19;
			}
		}

	}

	public final void setPos(float var1, float var2, float var3) {
		this.x = var1;
		this.y = var2;
		this.z = var3;
	}

	public final void render(float var1) {
		if(this.showModel) {
			if(!this.compiled) {
				this.translateTo(var1);
			}

			float var2 = 57.29578F;
			GL11.glPushMatrix();
			GL11.glTranslatef(this.x * var1, this.y * var1, this.z * var1);
			GL11.glRotatef(this.zRot * var2, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(this.yRot * var2, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.xRot * var2, 1.0F, 0.0F, 0.0F);
			GL11.glCallList(this.list);
			GL11.glPopMatrix();
		}
	}

	public void translateTo(float var1) {
		this.list = GL11.glGenLists(1);
		GL11.glNewList(this.list, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);

		for(int var2 = 0; var2 < this.polygons.length; ++var2) {
			Polygon var10000 = this.polygons[var2];
			float var4 = var1;
			Polygon var3 = var10000;
			Vec3 var5 = var3.vertices[1].pos.subtract(var3.vertices[0].pos).normalize();
			Vec3 var6 = var3.vertices[1].pos.subtract(var3.vertices[2].pos).normalize();
			var5 = (new Vec3(var5.y * var6.z - var5.z * var6.y, var5.z * var6.x - var5.x * var6.z, var5.x * var6.y - var5.y * var6.x)).normalize();
			GL11.glNormal3f(var5.x, var5.y, var5.z);

			for(int var7 = 0; var7 < 4; ++var7) {
				Vertex var8 = var3.vertices[var7];
				GL11.glTexCoord2f(var8.u, var8.v);
				GL11.glVertex3f(var8.pos.x * var4, var8.pos.y * var4, var8.pos.z * var4);
			}
		}

		GL11.glEnd();
		GL11.glEndList();
		this.compiled = true;
	}
}
