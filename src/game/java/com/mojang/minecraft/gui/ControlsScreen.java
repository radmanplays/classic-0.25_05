package com.mojang.minecraft.gui;

import com.mojang.minecraft.Options;

public final class ControlsScreen extends Screen {
	private Screen parent;
	private String title = "Controls";
	private Options options;
	private int selectedKey = -1;

	public ControlsScreen(Screen var1, Options var2) {
		this.parent = var1;
		this.options = var2;
	}

	public final void init() {
		for(int var1 = 0; var1 < this.options.keys.length; ++var1) {
			this.buttons.add(new SmallButton(var1, this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 + 24 * (var1 >> 1), this.options.getKeyMessage(var1)));
		}

		this.buttons.add(new Button(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected final void buttonClicked(Button var1) {
		for(int var2 = 0; var2 < this.options.keys.length; ++var2) {
			((Button)this.buttons.get(var2)).msg = this.options.getKeyMessage(var2);
		}

		if(var1.id == 200) {
			this.minecraft.setScreen(this.parent);
		} else {
			this.selectedKey = var1.id;
			var1.msg = "> " + this.options.getKeyMessage(var1.id) + " <";
		}
	}

	protected final void keyPressed(char var1, int var2) {
		if(this.selectedKey >= 0) {
			this.options.setKey(this.selectedKey, var2);
			((Button)this.buttons.get(this.selectedKey)).msg = this.options.getKeyMessage(this.selectedKey);
			this.selectedKey = -1;
		} else {
			super.keyPressed(var1, var2);
		}
	}

	public final void render(int var1, int var2) {
		fillGradient(0, 0, this.width, this.height, 1610941696, -1607454624);
		drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
		super.render(var1, var2);
	}
}
