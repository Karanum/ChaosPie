package eu.teamtime.chaospie;

import java.util.Map;

import com.google.common.collect.Maps;

public class EffectMetrics {

	private Map<ChaosEffectBase, Integer> effectRunCount;
	
	public EffectMetrics() {
		effectRunCount = Maps.newHashMap();
		for (ChaosEffectBase effect : ChaosPie.instance().getAllEffects()) {
			effectRunCount.put(effect, 0);
		}
	}
	
	public void addEffectCount(ChaosEffectBase effect) {
		effectRunCount.put(effect, effectRunCount.get(effect) + 1);
	}
	
	public int getEffectCount(ChaosEffectBase effect) {
		if (!effectRunCount.containsKey(effect))
			return -1;
		return effectRunCount.get(effect);
	}
	
}
