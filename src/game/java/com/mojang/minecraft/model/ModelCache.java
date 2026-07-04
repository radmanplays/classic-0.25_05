package com.mojang.minecraft.model;

public final class ModelCache {
	private HumanoidModel humanoidModel = new HumanoidModel(0.0F);
	private HumanoidModel humanoidArmorModel = new HumanoidModel(1.0F);
	private CreeperModel creeperModel = new CreeperModel();
	private SkeletonModel skeletonModel = new SkeletonModel();
	private ZombieModel zombieModel = new ZombieModel();
	private QuadrupedModel quadrupedModel = new QuadrupedModel();

	public final BaseModel getModel(String var1) {
		return (BaseModel)(var1.equals("humanoid") ? this.humanoidModel : (var1.equals("humanoid.armor") ? this.humanoidArmorModel : (var1.equals("creeper") ? this.creeperModel : (var1.equals("skeleton") ? this.skeletonModel : (var1.equals("zombie") ? this.zombieModel : (var1.equals("pig") ? this.quadrupedModel : null))))));
	}
}
