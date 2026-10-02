# Starlib

A Minecraft mod called Starlib.

This project uses [Stonecraft](https://stonecraft.meza.gg) to build for Fabric and NeoForge. It includes:

- a shared mod entrypoint across the selected loaders;
- example data generation for selected loaders that provide an example generator;
- an access widener that Stonecraft converts for loaders that require it;
- Renovate configuration for dependency updates.


## Requirements

- JDK 25

The Gradle wrapper downloads the required Gradle distribution automatically.

## Build and verify

Run the available commands from the project root:

```shell
./gradlew buildAndCollect
./gradlew runDatagen
```

`buildAndCollect` builds every configured Minecraft-version and loader pair, then collects the resulting JARs under `build/libs`.
`runDatagen` runs data generation for every configured pair and writes into each `versions/*/src/main/generated` directory. The example advancement provider is registered for selected Fabric and NeoForge pairs.

## Generated resources

Data generation writes to `src/main/generated` inside each generated Stonecutter version project. Stonecraft includes that directory in loader builds automatically.

## Access widening

Add access-widener entries to `src/main/resources/starlib.accesswidener`. Stonecraft uses it directly where supported and converts it for loaders that use access transformers.

## Dependency updates

The repository includes `renovate.json`. Enable Renovate through your hosting platform's integration or a self-hosted Renovate instance. See [Installing & Onboarding](https://docs.renovatebot.com/getting-started/installing-onboarding/) for the available options. Renovate then reads the committed configuration and begins opening dependency update pull requests.
