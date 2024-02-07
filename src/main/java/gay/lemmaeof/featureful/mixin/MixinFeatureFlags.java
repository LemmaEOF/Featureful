package gay.lemmaeof.featureful.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FeatureFlags.class)
public class MixinFeatureFlags {
	@Inject( method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/featuretoggle/FeatureManager$Builder;build()Lnet/minecraft/resource/featuretoggle/FeatureManager;"),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void on(CallbackInfo info, FeatureManager.Builder builder) {
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			ModMetadata mod = container.getMetadata();
			if (mod.containsCustomValue("featureful:flags")) {
				CustomValue flags = mod.getCustomValue("featureful:flags");
				if (flags.getType() == CustomValue.CvType.ARRAY) {
					CustomValue.CvArray array = flags.getAsArray();
					for (CustomValue entry : array) {
						if (entry.getType() == CustomValue.CvType.STRING) {
							builder.addFlag(new Identifier(mod.getId(), entry.getAsString()));
						}
					}
				}
			}
		}
	}
}