<img src="icon.png" align="right" width="180px"/>

# Featureful


[>> Downloads <<](https://github.com/LemmaEOF/Featureful/releases)

*Tread carefully!*

**This mod is open source and under a permissive license.** As such, it can be included in any modpack on any platform without prior permission. We appreciate hearing about people using our mods, but you do not need to ask to use them. See the [LICENSE file](LICENSE) for more details.

Featureful is a lightweight framework for adding and interacting with Minecraft's feature flag system designed for toggling experimental content like bundles or content from future updates. It also provides commands for listing currently-enabled feature flags and enabling feature flags outside of world creation.

**FEATURE FLAGS CANNOT BE DISABLED ONCE ENABLED WITHOUT EDITING THE LEVEL.DAT!** This is an *intentional design decision* by Mojang, and it would take significant effort in order to bypass it. If you really want to turn a feature flag off, just edit the level.dat. There are plenty of good NBT editors nowadays.

Mods can add their own feature flags by adding them to the `custom` block of their fabric.mod.json, added as an array with the key `featureful:flags`. They will *automatically* be namespaced to the mod's ID, so it's not necessary to add it repeatedly. Your registered feature flag can then be accessed by grabbing the ID from `FeatureRegistry`.