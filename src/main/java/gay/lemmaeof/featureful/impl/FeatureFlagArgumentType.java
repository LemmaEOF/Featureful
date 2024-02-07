package gay.lemmaeof.featureful.impl;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import gay.lemmaeof.featureful.api.FeatureRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class FeatureFlagArgumentType implements ArgumentType<FeatureFlag> {
	private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");
	private static final DynamicCommandExceptionType UNKNOWN_FEATURE_FLAG_EXCEPTION = new DynamicCommandExceptionType(
			id -> Text.translatable("featureful.featureFlagNotFound", id)
	);

	public static FeatureFlagArgumentType featureFlag() {
		return new FeatureFlagArgumentType();
	}

	@Override
	public FeatureFlag parse(StringReader reader) throws CommandSyntaxException {
		Identifier id = Identifier.fromCommandInput(reader);
		if (!FeatureRegistry.hasFeatureFlag(id)) throw UNKNOWN_FEATURE_FLAG_EXCEPTION.create(id);
		return FeatureRegistry.getFeatureFlag(id);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		S src = context.getSource();
		if (src instanceof ServerCommandSource server) {
			return CommandSource.suggestIdentifiers(FeatureRegistry.getDisabledFeatureFlags(server.getEnabledFeatures()), builder);
		} else {
			return CommandSource.suggestIdentifiers(FeatureRegistry.getFeatureFlagIds(), builder);
		}
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
