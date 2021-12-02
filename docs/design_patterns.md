# Wzorce projektowe

Podsumowanie użytych wzorców projektowych. 

## Strategy

Został użyty podczas implementacji różnych typów wiadomości w module [message](./message_module.md).

<img src="./img/dp_strategy.png">

*Rysunek 1: Wzorzec projektowy strategia.*

Klasa ```Message``` zawiera metodę ```process(context)```, która wywołuje metodę ```process(sender, context)``` na obiekcie klasy implementującej interfejs ```Body```. Metoda ta różni się w zależności od typu ciała. Wiadomość jest zatem przetwarzana w różny sposób w zależności od tego, jaki obiekt ```Body``` jest zawarty w obiekcjie ```Message```. Konkretna strategia dostarczana jest w konstruktorze klasy ```Message```.

## Template method

Został użyty podczas implementacji zamiany wiadomości na reprezentację ```json``` w module [message](./message_module.md).

<img src="./img/dp_template_method.png">

*Rysunek 2: Wzorzec projektowy metoda szablonowa.*

Metoda ```Message.serialize()``` jest metodą szablonową. Definiuje kolejność zapisywania informacji do reprezentacji ```json```. Jednak ich treść zależy od używanej implemetacji interfejsu ```Body```. 

## Active object

Cały moduł [state](./state_module.md) jest implementacją wzorca projektowego ```Active object```. Jest on opisany dokładnie w donumentacji modułu. 

---

[Home](./index.md)