# Arabic Poetry Management System 🏛️✨

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-9.4-blue.svg)](https://www.mysql.com/)

A sophisticated, multi-layered Desktop Application for managing, analyzing, and exploring Arabic poetry. Built with **JavaFX** and a robust **3-Tier Architecture**, this system integrates advanced NLP capabilities to provide deep insights into poetic structures.

---

## 🌟 Key Features

- **📚 Library Management**: Import, edit, and organize Arabic poetry books, poems, and poets with ease.
- **🔍 Advanced Search**: Search by keywords, roots, or lemmas across the entire collection.
- **🔡 NLP Analysis**: Powered by **AlKhalil Morpho Sys**, the system provides:
  - Tokenization & Segmentation.
  - Root & Lemma extraction.
  - Morphological analysis of Arabic verses.
- **📊 Statistical Insights**:
  - Frequency analysis for tokens, roots, lemmas, and segments.
  - N-Gram generation for linguistic pattern discovery.
- **🎨 Premium UI**: A modern, responsive interface built with JavaFX, featuring a glass-morphic dashboard and intuitive navigation.
- **🧪 Quality Assured**: Comprehensive test suite using **JUnit 5**, **Mockito**, and **TestFX** for UI automation.

---

## 🏗️ Technical Architecture

The project follows a strict **Separation of Concerns** using a 3-tier architecture:

1. **Presentation Layer (PL)**: JavaFX-based GUI with FXML and CSS.
2. **Business Logic Layer (BL)**: Handles core calculations, NLP integration, and business rules.
3. **Data Access Layer (DAL)**: Manages persistence using MySQL and DAO patterns.
4. **DTOs**: Data Transfer Objects for seamless communication between layers.

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 21** or higher.
- **Maven** 3.9+.
- **MySQL Server** 8.0/9.0+.
- **AlKhalil NLP Library**: Ensure `AlkhalilMorphSys2.jar` is in the `lib` directory.

### Database Setup

1. Create a MySQL database:
   ```sql
   CREATE DATABASE testdatabase;
   ```
2. Update the credentials in `src/dal/DatabaseConfigure.java` if necessary:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/testdatabase";
   private static final String USER = "root";
   private static final String PASSWORD = "yourpassword";
   ```

### Running the Application

Clone the repository and run:

```bash
mvn clean javafx:run
```

### Building the Project

To generate an executable Uber-JAR (with dependencies):

```bash
mvn clean package
```

The artifact will be located in the `target/` directory.

---

## 🧪 Testing

The system is designed with testability in mind:

- **Unit Tests**: Business logic validation.
- **Mocking**: Services mocked via Mockito.
- **UI Testing**: Automated interface testing using TestFX.

Run tests via Maven:

```bash
mvn test
```

---

## 🛠️ Built With

- **JavaFX** - Rich client interface.
- **MySQL Connector/J** - Database connectivity.
- **AlKhalil Morpho Sys** - Arabic Morphological Analysis.
- **Log4j 2** - Logging framework.
- **JUnit 5 & TestFX** - Testing framework.

---

## 👥 Authors

- **M Shahnawaz** - [GitHub Profile](https://github.com/mshahnawaz1202)

*Developed with ❤️ for the Arabic language and its rich poetic heritage.*
