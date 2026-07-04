package com.mojang.minecraft.level.levelgen.synth;

import net.lax1dude.eaglercraft.Random;

public final class PerlinNoise extends Synth {
	private ImprovedNoise[] noiseLevels;
	private int levels;

	public PerlinNoise(Random var1, int var2) {
		this.levels = var2;
		this.noiseLevels = new ImprovedNoise[var2];

		for(int var3 = 0; var3 < var2; ++var3) {
			this.noiseLevels[var3] = new ImprovedNoise(var1);
		}

	}

	public final double getValue(double var1, double var3) {
		double var5 = 0.0D;
		double var7 = 1.0D;

		for(int var9 = 0; var9 < this.levels; ++var9) {
			var5 += this.noiseLevels[var9].getValue(var1 / var7, var3 / var7) * var7;
			var7 *= 2.0D;
		}

		return var5;
	}
}
