# Targets

Java/Gradle project with automated builds using GitHub Actions.

## Features

- ✅ Java support (JDK 17, JDK 21)
- ✅ Gradle build system
- ✅ Automated CI/CD with GitHub Actions
- ✅ Unit testing
- ✅ Code coverage reporting (Jacoco)

## Getting Started

### Prerequisites

- JDK 17 or higher
- Gradle 7.0+

### Installation

```bash
./gradlew build
```

### Development

```bash
./gradlew run
```

### Build

```bash
./gradlew build
```

### Testing

```bash
./gradlew test
```

## Automated Builds

This project uses GitHub Actions to automatically:

1. **Build** - Compiles Java code with Gradle
2. **Test** - Runs unit tests
3. **Report** - Uploads coverage to Codecov

Builds are triggered on:
- Push to `main`, `develop`, or `master` branches
- Pull requests to `main`, `develop`, or `master` branches

View workflow results in the **Actions** tab of this repository.

## License

MIT
