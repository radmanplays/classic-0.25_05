package com.mojang.minecraft;

public final class Timer {
	float ticksPerSecond;
	double lastTime;
	public int frames;
	public float alpha;
	public float fps = 1.0F;
	public float ticks = 0.0F;
	long msPerTick;
	long passedTime;
	double averageFrameTime = 1.0D;

	public Timer(float var1) {
		this.ticksPerSecond = var1;
		this.msPerTick = System.currentTimeMillis();
		this.passedTime = System.nanoTime() / 1000000L;
	}
}
