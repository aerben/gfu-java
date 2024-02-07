Arbeitsblatt: Erstellung eines Spring Boot Custom Starter
---
## Schritt 1: Erstellen eines Maven-Projekts

Erstelle ein neues Maven-Projekt in einem Unterverzeichnis "module" mit folgender `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>digital.erben</groupId>
    <artifactId>gfu-sb3-starter-module</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <spring-boot.version>3.2.2</spring-boot.version>
        <maven-compiler-plugin.version>3.12.1</maven-compiler-plugin.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
            </plugin>
        </plugins>
    </build>

</project>
```

## Schritt 2: Implementierung der CustomService-Klasse

Erstelle eine neue Java-Klasse `CustomService.java` im Verzeichnis `src/main/java/com/example`:

```java
package com.example;

public class CustomService {

  private String message;

  public CustomService(String message) {
    this.message = message;
  }

  public void printMessage() {
    System.out.println(message);
  }

}
```

## Schritt 3: Implementierung der CustomServiceProperties

Erstelle eine neue Java-Klasse `CustomServiceProperties.java` im Verzeichnis `src/main/java/com/example`:

```java
package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom.service")
public class CustomServiceProperties {

  private String message;

  //Getters and Setters
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
```


## Schritt 4: Implementierung einer Custom AutoConfiguration

Erstelle eine neue Java-Klasse in `CustomServiceAutoConfiguration.java` im Verzeichnis `src/main/java/com/example`:

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomServiceProperties.class)
public class CustomServiceAutoConfiguration {

  @Bean
  public CustomService customService(CustomServiceProperties properties) {
    return new CustomService(properties.getMessage());
  }

}
```

## Schritt 5: spring.factories-Datei und imports-Datei erstellen

Erstelle eine Datei namens `spring.factories` im Verzeichnis `src/main/resources/META-INF` und füge folgende Zeile hinzu:

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.example.CustomServiceAutoConfiguration
```

Außerdem benötigst du seit Spring Boot 3 noch folgende Datei: `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` mit dem Inhalt
```
com.example.CustomServiceAutoConfiguration
```

## Schritt 6: Maven-Paket erstellen

Baue das Projekt im Projekt-Verzeichnis mit Maven:

```
mvn clean install
```

Nun sollte das Projekt erfolgreich gebaut und ein JAR in deinem lokalen Maven-Repository erstellt worden sein.

Als Nächstes wollen wir eine neue Spring Boot-Anwendung erstellen, die den neuen Starter benutzt.

## Schritt 7: Spring Boot Anwendung erstellen

Erstelle ein neues Maven-Projekt mit folgender `pom.xml` im Verzeichnis "app":

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>digital.erben</groupId>
    <version>1.0.0-SNAPSHOT</version>
    <artifactId>gfu-sb3-starter-app</artifactId>
    <packaging>jar</packaging>

    <properties>
        <spring-boot.version>3.2.2</spring-boot.version>
        <maven-compiler-plugin.version>3.12.1</maven-compiler-plugin.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
        <dependency>
            <groupId>digital.erben</groupId>
            <artifactId>gfu-sb3-starter-module</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
            </plugin>
        </plugins>
    </build>

</project>
```

## Schritt 8: `application.properties` erstellen

Erstelle eine Datei namens `application.properties` im Verzeichnis `src/main/resources` und füge folgende Zeile hinzu:

```
custom.service.message=Hallo, hier ist der Custom Starter!
```

## Schritt 9: Main-Klasse erstellen

Erstelle eine neue Java-Klasse `CustomStarterDemoApplication.java` im Verzeichnis `src/main/java/com/example/demo`:

```java
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomStarterDemoApplication implements CommandLineRunner {

  @Autowired
  private CustomService customService;

  public static void main(String[] args) {
    SpringApplication.run(CustomStarterDemoApplication.class, args);
  }

  @Override
  public void run(String... args) {
    customService.printMessage();
  }
}
```

## Schritt 10: Anwendung ausführen

Führe die Anwendung aus, indem du das folgende Maven-Kommando im Projekt-Verzeichnis ausführst:

```
mvn spring-boot:run
```

Wenn alles erfolgreich ist, wird die Anwendung starten und die Nachricht aus der `application.properties`-Datei ausgeben. In diesem Fall sollte "Hallo, hier ist der Custom Starter!" in der Konsole sichtbar sein.

## Schritt 11: Conditional Configuration

Ändere die Application-Klasse wie folgt:
```java
package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomStarterDemoApplication implements CommandLineRunner {

  @Autowired(required = false)
  private CustomService customService;

  public static void main(String[] args) {
    SpringApplication.run(CustomStarterDemoApplication.class, args);
  }

  @Override
  public void run(String... args) {
    if (customService != null) {
      customService.printMessage();
    } else {
      System.out.println("No service present");
    }
  }
}
```

Ändere danach die AutoConfiguration:

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomServiceProperties.class)
public class CustomServiceAutoConfiguration {

  @Bean
  @ConditionalOnProperty(value = "custom.service.enabled", havingValue = "true")
  public CustomService customService(CustomServiceProperties properties) {
    return new CustomService(properties.getMessage());
  }

}
```
Starte die Anwendung einmal und der Service fehlt.
Füge danach in die application.properties die Zeile `custom.service.enabled=true` ein und starte neu. 
Der Service ist da.