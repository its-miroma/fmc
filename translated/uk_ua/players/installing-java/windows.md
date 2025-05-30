---
title: Встановлення Java у Windows
description: Покрокова інструкція щодо встановлення Java на Windows.
authors:
  - IMB11
---

Інструкція по тому як встановити Java 21 у Windows.

Запускач Minecraft постачається з власною інсталяцією Java, тому цей розділ актуальний, лише якщо ви хочете використовувати встановлювач на основі Fabric `.jar` або якщо ви хочете використовувати сервер Minecraft `.jar`.

## 1. Перевірте, чи Java уже встановлено {#1-check-if-java-is-already-installed}

Щоб перевірити, чи Java вже встановлено, потрібно спочатку відкрити командний рядок.

Ви можете зробити це, натиснувши <kbd>Win</kbd> <kbd>R</kbd> і ввівши `cmd.exe` у поле, що з’явиться.

![Діалогове вікно запуску Windows із "cmd.exe" на панелі запуску](/assets/players/installing-java/windows-run-dialog.png)

Відкривши командний рядок, введіть `java -version` і натисніть <kbd>Enter</kbd>.

Якщо команда виконана успішно, ви побачите щось подібне. Якщо команда не вдалася, перейдіть до наступного кроку.

![Командний рядок із введеним "java -version"](/assets/players/installing-java/windows-java-version.png)

:::warning
Щоб використовувати Minecraft 1.21, вам знадобиться встановити принаймні Java 21. Якщо ця команда показує будь-яку версію, нижчу за 21, вам потрібно буде оновити встановлювач Java.
:::

## 2. Завантажити завантажувач Java 21 {#2-download-the-java-installer}

Щоб установити Java 21, вам потрібно буде завантажити програму встановлення з Adoptium.

Ви захочете завантажити версію `Windows Installer (.msi)`:

![Сторінка завантаження Adoptium із виділеним встановлювачем Windows (.msi)](/assets/players/installing-java/windows-download-java.png)

Вам слід вибрати «x86», якщо у вас 32-розрядна операційна система, або «x64», якщо у вас 64-розрядна операційна система.

Більшість сучасних комп’ютерів матиме 64-розрядну операційну систему. Якщо ви не впевнені, спробуйте скористатися 64-розрядним завантаженням.

## 3. Запустіть встановлювач! {#3-run-the-installer}

Виконайте кроки встановлювача, щоб встановити Java 21. Коли ви перейдете на цю сторінку, ви повинні встановити такі функції на «Уся функція буде встановлена ​​на локальному жорсткому диску»:

- `Установити змінну середовища JAVA_HOME` - її буде додано до вашого ШЛЯХУ.
- `JavaSoft (Oracle) registry keys`

![Встановлювач Java 21 із виділеними параметрами «Встановити змінну JAVA_HOME» і «ключами реєстру JavaSoft (Oracle)»](/assets/players/installing-java/windows-wizard-screenshot.png)

Зробивши це, ви можете натиснути `Next` і продовжити встановлення.

## 4. Перевірте, чи встановлено Java 21 {#4-verify-that-java-is-installed}

Після завершення встановлення ви можете переконатися, що Java 21 встановлено, знову відкривши командний рядок і ввівши `java -version`.

Якщо команда виконана успішно, ви побачите щось подібне до показаного раніше, де показана версія Java:

![Командний рядок із введеним "java -version"](/assets/players/installing-java/windows-java-version.png)

Якщо у вас виникнуть проблеми, не соромтеся звертатися по допомогу до [Fabric Discord](https://discord.gg/v6v4pMv) на каналі #player-support.
