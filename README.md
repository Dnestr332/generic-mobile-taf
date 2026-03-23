# Generic Mobile Test Automation Framework (TAF)

A robust, scalable mobile test automation framework designed for cross-platform (Android & iOS) application testing. Built with a focus on maintainability, reusability, and detailed reporting.

## 🚀 Tech Stack

- **Language:** Java 21
- **Frameworks:** Spring Framework (Context & Dependency Injection)
- **Automation:** Appium (Mobile), Selenium, Rest-Assured (API)
- **BDD Engine:** Cucumber (Gherkin)
- **Test Runner:** TestNG
- **Package Manager:** Maven
- **Reporting:** Allure Reports
- **Assertions:** AssertJ
- **Logging:** SLF4J + Logback

---

## 📋 Requirements

To run this project, you need:

- **JDK 21** installed and configured in `JAVA_HOME`.
- **Maven 3.x+** installed.
- **Appium Server** (v2.x recommended) installed and running.
- **Android SDK** (for Android testing).
- **Xcode** (for iOS testing, macOS only).
- **Node.js & NPM** (to manage Appium drivers).

---

## 🛠 Project Structure

```text
src
├── main
│   ├── java/com/generic
│   │   ├── assertions/     # Custom hard and soft assertions
│   │   ├── base/           # Base classes for Screens and API
│   │   ├── config/         # Property and Environment configuration
│   │   ├── context/        # Scenario and Spring context management
│   │   ├── enums/          # Enumerations (Platform, Strategy, etc.)
│   │   ├── interfaces/     # Shared interfaces (Screen, Clickable)
│   │   ├── logs/           # Logging utilities and styles
│   │   ├── mobile/         # Driver factory and Environment detection
│   │   ├── runners/        # Abstract TestNG/Cucumber runners
│   │   └── utils/          # Common utilities (Allure, DB, Api, Mobile)
│   └── resources/          # application.properties, logback.xml
└── test
    ├── java/com/generic
    │   ├── api/            # API clients and endpoints
    │   ├── flows/          # Business logic flows (multi-screen actions)
    │   ├── pages/          # Page Object Model (Screens)
    │   ├── runners/        # Concrete TestNG runners (Android/iOS)
    │   ├── spring/         # Spring configuration for tests
    │   ├── steps/          # Cucumber Step Definitions
    │   └── tests/          # Unit/Integration tests
    └── resources
        ├── features/       # Cucumber Gherkin feature files
        ├── testng/         # TestNG XML suite configurations
        └── test-data.properties
```

---

## ⚙️ Configuration

### Environment Variables & `.env`
The framework uses `dotenv` to load environment variables. You can create a `.env` file in the project root:

```env
# Example .env content
CI=false
# TODO: Add other necessary environment variables here
```

### Application Properties
Central configuration is located in `src/main/resources/application.properties`.

Key properties to update:
- `android.app.appPackage` & `android.app.appActivity`
- `ios.app.bundleId`
- `appium.androidUrl` (Default: `http://127.0.0.1:4723`)
- `appium.iosUrl` (Default: `http://127.0.0.1:4725`)

---

## 🏃 Running Tests

### Using Maven Profiles
The project uses Maven profiles to manage execution for different platforms:

- **Run Android Tests:**
  ```bash
  mvn clean test -Ptestng-android
  ```

- **Run iOS Tests:**
  ```bash
  mvn clean test -Ptestng-ios
  ```

- **Run Failed Scenarios (Rerun):**
  ```bash
  mvn clean test -Ptestng-rerun-android
  # or
  mvn clean test -Ptestng-rerun-ios
  ```

### Platform Override
You can override the platform via system property:
```bash
mvn clean test -Dplatform=ANDROID
```

---

## 📊 Reporting

### Allure Reports
Test results are generated in `target/allure-results`.

1. **Generate Report:**
   ```bash
   mvn allure:report
   ```
2. **Open Report:**
   ```bash
   mvn allure:serve
   ```

---

## 📝 Scripts & CI
- **CI Mode:** Controlled via the `ci` profile or `CI=true` environment variable. In CI mode, wait timeouts are extended, and retry counts are increased (configured in `pom.xml`).
- **TODO:** Document any custom shell scripts if added to the repository root.

---

## 📜 License
This project is licensed under the **TODO: Add License Type** - see the [LICENSE](LICENSE) file for details (if applicable).
