## Редактор игр камень-ножницы-бумага

Консольное приложение для игры в камень-ножницы-бумага + редактор (конфиг) для игры

#### Редактор (текстовый файл):

Игра при запуске читает из конфига правила:  
А, Б, В  
А>Б  
Б>В  
Что значит, что в игре три вида фигур А, Б и В, при этом А бьет Б, Б бьет В, Б и В играют в ничью

Для "камень-ножницы-бумага" это следующий формат:  
камень, ножницы, бумага  
камень > ножницы  
ножницы > бумага  
бумага > камень

#### Игра:
1. Пользователь видит на экране надпись "камень-ножницы-бумага раз-два-три" и ждет ввода пользователя
2. Пользователь делает ход печатая свою фигуру, например "камень"
3. Игра рандомно генерирует свой ход например "ножницы" и выдает в ответе "вы победили" / "вы проиграли" / "ничья"
4. В случае ничьи игра повторяется с шага 1
5. Иначе выводится счет игрок 1 - 0 машина и предлагается продолжить игру на счет дальше

Чтобы запустить игру нужно запустить [main file](src/main/kotlin/Main.kt). 
В аргументах можно указать путь до файла с конфигом, по дефолту конфиг будет взят из [этого файла](src/main/resources/config.txt).

В проекте представлены базовые тесты на функциональность приложения (с использованием JUnit5 и Mockito):
- [здесь](src/test/kotlin/GameConfigTest.kt) на корректность чтения из конфигурационного файла 
- [здесь](src/test/kotlin/RockPaperScissorsTest.kt) на правильность самого хода игры.

Чтобы запустить приложение из командой строки, можно запустить [скрипт](runMvn.sh)

В [Main.kt](src/main/kotlin/Main.kt) представлена игра между пользователем, запускающим игру из консоли, и машиной, но вообще игра может быть запущена для 2ух реальных пользователей, которые будут по очереди вводить команды

Также в проект добавлена поддержка английского (code = en) и русского (code = ru, by default) языка вывода. Для указания локали, нужно добавить аргумент запуска en/ru (опционально). По умолчанию будет взят русский язык.

Аргументы ожидаются в таком виде: --path src/main/resources/some_file --locale en