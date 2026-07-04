package com.mojang.minecraft.gui;

import org.lwjgl.opengl.GL11;

public final class DeathScreen extends Screen {
	public final void init() {
		this.buttons.clear();
		this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4 + 72, "Generate new level..."));
	}

	protected final void buttonClicked(Button var1) {
		if(var1.id == 0) {
			this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
		}

		if(var1.id == 1) {
			this.minecraft.setScreen(new NewLevelScreen(this));
		}

	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		drawCenteredString(this.font, "Game over!", this.width / 2 / 2, 30, 16777215);
		GL11.glPopMatrix();
		drawCenteredString(this.font, "Score: &e" + this.minecraft.player.getScore(), this.width / 2, 100, 16777215);
		super.render(var1, var2);
	}
}
