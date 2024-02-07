package gay.lemmaeof.featureful.mixin;

import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(FeatureManager.class)
public interface FeatureManagerAccessor {
	@Accessor
	Map<Identifier, FeatureFlag> getFeatureFlags();
}
