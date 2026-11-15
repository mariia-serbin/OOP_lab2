# OOP Lab 2 — Gem Fund

Лабораторна робота №2 з об'єктно-орієнтованого програмування.

Застосунок обробляє XML-документ «Алмазний фонд»: перевіряє його за XSD,
розбирає трьома способами (SAX, DOM, StAX), сортує камені та формує
згрупований XML за допомогою XSLT.

## Технології

- Java 21
- Maven Wrapper (Maven 3.9.9)
- JAXB / XJC для генерації моделі з XSD
- JUnit 5
- SAX, DOM, StAX
- XSD-валідація, XSLT 1.0
- `java.util.logging`

## Структура проєкту

```
src/main/java/lab2/gem/
├── Main.java                 — точка входу
├── GemApplication.java       — повний конвеєр обробки
├── logging/LoggingConfig.java
├── parser/                   — SAX, DOM, StAX та спільні компоненти
├── sort/GemComparator.java   — сортування колекції
├── transform/XsltTransformer.java
└── validation/XsdValidator.java

src/main/resources/
├── xml/gems.xml              — валідний вхідний документ
├── xsd/gems.xsd              — схема
├── xsl/group-by-preciousness.xsl
└── logging.properties

src/test/java/lab2/gem/       — Unit-тести
src/test/resources/xml/invalid-gems.xml
```

JAXB-модель генерується під час збірки в
`target/generated-sources/jaxb/lab2/gem/model/`.

## Збірка та тести

```powershell
.\mvnw.cmd clean test
```

Пакування:

```powershell
.\mvnw.cmd package
```

## Запуск

З вбудованим XML (`src/main/resources/xml/gems.xml`):

```powershell
java -jar target\oop-lab2-gems-1.0-SNAPSHOT.jar
```

З власними файлами:

```powershell
java -jar target\oop-lab2-gems-1.0-SNAPSHOT.jar input.xml output.xml
```

За замовчуванням результат записується в `target/grouped-gems.xml`.
Якщо вказано шлях до вихідного файлу, потрібні каталоги створюються
автоматично.

## Що робить застосунок

1. Валідує вхідний XML за `gems.xsd`.
2. Розбирає документ через SAX, DOM і StAX та порівнює кількість знайдених каменів.
3. Сортує камені за:
   - preciousness (`precious` перед `semiprecious`);
   - вагою за спаданням;
   - назвою за зростанням.
4. Виконує XSLT-перетворення з групуванням за `preciousness`.
5. Записує результат у вихідний XML-файл.

Приклад результату:

```xml
<GemGroups>
    <Group preciousness="precious" count="4">
        ...
    </Group>
    <Group preciousness="semiprecious" count="4">
        ...
    </Group>
</GemGroups>
```
