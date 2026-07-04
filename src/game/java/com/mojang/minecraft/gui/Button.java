package com.mojang.minecraft.gui;

public class Button extends GuiComponent {
	int w;
	int h;
	public int x;
	public int y;
	public String msg;
	public int id;
	public boolean enabled;
	public boolean visible;

	public Button(int var1, int var2, int var3, String var4) {
		this(var1, var2, var3, 200, 20, var4);
	}

	protected Button(int var1, int var2, int var3, int var4, int var5, String var6) {
		this.w = 200;
		this.h = 20;
		this.enabled = true;
		this.visible = true;
		this.id = var1;
		this.x = var2;
		this.y = var3;
		this.w = var4;
		this.h = 20;
		this.msg = var6;
	}
}
