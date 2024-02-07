package gay.lemmaeof.featureful.impl;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import gay.lemmaeof.featureful.api.FeatureRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.level.LevelProperties;

import java.util.Set;

public class Featureful implements ModInitializer {
	public static final String MODID = "featureful";

	@Override
	public void onInitialize() {
		ArgumentTypeRegistry.registerArgumentType(new Identifier(MODID, "feature_flag"), FeatureFlagArgumentType.class, ConstantArgumentSerializer.of(FeatureFlagArgumentType::featureFlag));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LiteralCommandNode<ServerCommandSource> featuresNode = CommandManager.literal("features")
					.build();

			LiteralCommandNode<ServerCommandSource> listNode = CommandManager.literal("list")
							.executes(context -> {
								Set<Identifier> features = FeatureFlags.FEATURE_MANAGER.toId(context.getSource().getEnabledFeatures());
								Text text = Texts.join(features, id -> Text.literal(id.toString()));
								context.getSource().sendFeedback(() -> Text.translatable("commands.featureful.list", features.size(), text), false);
								return features.size();
							}).build();
			featuresNode.addChild(listNode);

			LiteralCommandNode<ServerCommandSource> enableNode = CommandManager.literal("enable")
					.requires(source -> source.hasPermissionLevel(2))
					.build();

			ArgumentCommandNode<ServerCommandSource, FeatureFlag> flagNode = CommandManager.argument("flag", FeatureFlagArgumentType.featureFlag())
					.executes(context -> {
						World world = context.getSource().getWorld();
						FeatureFlag flag = context.getArgument("flag", FeatureFlag.class);
						if (world.getEnabledFeatures().contains(flag)) {
							context.getSource().sendError(Text.translatable("commands.featureful.enable.already_added", FeatureRegistry.getId(flag)));
							return 0;
						} else {
							WorldProperties properties = world.getLevelProperties();
							if (properties instanceof LevelProperties level) {
								DataConfiguration config = level.getDataConfiguration();
								level.updateLevelInfo(config.withFeaturesAdded(FeatureSet.of(flag)));
								context.getSource().sendFeedback(() -> Text.translatable("commands.featureful.enable.success"), true);
								return 1;
							} else {
								context.getSource().sendError(Text.translatable("commands.featureful.enable.not_level"));
								return 0;
							}
						}
					}).build();
			enableNode.addChild(flagNode);
			featuresNode.addChild(enableNode);

			dispatcher.getRoot().addChild(featuresNode);
		});
	}
}
