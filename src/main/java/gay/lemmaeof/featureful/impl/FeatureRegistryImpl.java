package gay.lemmaeof.featureful.impl;

import gay.lemmaeof.featureful.api.FeatureRegistry;
import gay.lemmaeof.featureful.mixin.FeatureManagerAccessor;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FeatureRegistryImpl implements FeatureRegistry {
	public static final FeatureRegistryImpl INSTANCE = new FeatureRegistryImpl();

	private static final FeatureManager MANAGER = FeatureFlags.FEATURE_MANAGER;

	public Identifier getId(FeatureFlag flag) {
		return (Identifier) MANAGER.toId(FeatureSet.of(flag)).toArray()[0];
	}

	public boolean hasFeatureFlag(Identifier id) {
		return getFlags().containsKey(id);
	}

	public FeatureFlag getFeatureFlag(Identifier id) {
		return getFlags().get(id);
	}

	public Collection<Identifier> getFeatureFlagIds() {
		return getFlags().keySet();
	}

	public Collection<Identifier> getDisabledFeatureFlags(FeatureSet set) {
		Set<Identifier> addedIds = MANAGER.toId(set);
		return getFlags().keySet().stream().filter(id -> !addedIds.contains(id)).collect(Collectors.toSet());
	}

	private Map<Identifier, FeatureFlag> getFlags() {
		return ((FeatureManagerAccessor) MANAGER).getFeatureFlags();
	}
}
