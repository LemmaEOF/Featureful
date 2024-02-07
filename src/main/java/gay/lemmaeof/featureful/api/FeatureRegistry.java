package gay.lemmaeof.featureful.api;

import gay.lemmaeof.featureful.impl.FeatureRegistryImpl;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

import java.util.Collection;

public interface FeatureRegistry {

	static Identifier getId(FeatureFlag flag) {
		return FeatureRegistryImpl.INSTANCE.getId(flag);
	}

	static boolean hasFeatureFlag(Identifier id) {
		return FeatureRegistryImpl.INSTANCE.hasFeatureFlag(id);
	}

	static FeatureFlag getFeatureFlag(Identifier id) {
		return FeatureRegistryImpl.INSTANCE.getFeatureFlag(id);
	}

	static Collection<Identifier> getFeatureFlagIds() {
		return FeatureRegistryImpl.INSTANCE.getFeatureFlagIds();
	}

	static Collection<Identifier> getDisabledFeatureFlags(FeatureSet set) {
		return FeatureRegistryImpl.INSTANCE.getDisabledFeatureFlags(set);
	}
}