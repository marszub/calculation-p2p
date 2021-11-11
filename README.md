# Obliczenia peer to peer

# Sieć peer to peer

Składa się z nodów:
- <font color="red">publicznych</font> - o adresie ip publicznym (czerwone)
- <font color="green">prywatnych</font> - bez publicznego ip (zielone)
- **<font color="red">publicznego głównego</font>** - o publicznym adresie ip, z którym ma się łączyć każdy node dołączający do sieci. (czerwony pogrubiony)

Rodzaje połączeń:

-   czysty p2p

    Każdy node publiczny komunikuje się bezpośrednio z pozostałymi. Nody prywatne tworzą pośrednie połączenia z innymi nodami prywatnymi poprzez nody publiczne (jeden node prywatny ma nawet lilka połączeń z nodem publicznym. Każde do obsługi innego połączenia nodów prywatnych).

    Broadcast w sieci:

    <img style="height:300px" src="./img/broadcast_pure.PNG">

    Minus - niepotrzebne mnożenie obciążenia publicznych nodów, nierównomierne obciążenie

    Plus - prosta implementacja. Osobne połączenie dla każdego noda. Broadcast: wysyłamy każdym połączeniem. Unicast: wysyłamy połączeniem przydzielonym do noda, nie musimy wiedzieć, czy jest bezpośrednie, czy nie.

-   Wspólne połączenie

    Jak wyżej, ale "wiązki" połączeń są jednym połączeniem. Node publiczny na podstawie zawartości wiadomości (informacji o adresacie) wybiera dalszą drogę wiadomości. 

    <img style="height:300px" src="./img/komunikacja_pure.PNG">

-   wybór reprezentanta

    <img style="height:300px" src="./img/komunikacja_reprezentant.PNG">

    Każdy prywatny node wybiera swojego publicznego reprezentanta, do którego przesyła całą komunikację. Może go wybrać jednorazowo, lub losować przy każdej próbie wysłania waidomości do noda prywatnego. Publiczny node musi przetworzyć i dalej rozesłać wiadomości. Broadcast może składać się tylko z jednej wiadomości do noda publicznego, który potem kopiuje i rozsyła wiadomość dalej:

    <img style="height:300px" src="./img/broadcast_opt.PNG">

    Minus - publiczne nody muszą przetwarzać wiadomości prywatnych nodów. Prywatny node ma bezpośrednie połączenie z innymi publicznymi nodami, którego nie wykorzystuje. Nody inaczej wysyłają wiadomości w zależności od publiczności ich adresu ip (komplikacja kodu)

    Plus - dwukrotnie zmniejszamy ruch broadcastowy. Nie tworzymy osobnych połączeń dla każdej pary nodów prywatnych.

-   **hybryda**

    Prywatny node wysyła wiadomości bezpośrednio do nodów publicznych, ale za pośrednictwem reprezentanta do prywatnych. Broadcast wygląda następująco:

    <img style="height:300px" src="./img/broadcast_hybryda.PNG">

    Minus - każdy node przy wysyłaniu i pośredniczeniu musi sprawdzać, czy odbiorca jest publiczny, czy prywatny. Dodatkowo musi wiedzieć, czy sam jest publiczny, czy prywatny. Dodatkowe skomplikowanie kodu.

    Plus - Rozwiązanie maksymalnie optymalne pod względem obciążenia sieci oraz nodów publicznych. 


# Wiadomości

Odbiorca:
- Unicast
- Broadcast

```
{
    header:
    {
        sender: <priority number>, 
        receiver: <broadcast=-1 | receiver_id>, 
        message_type: <enum>
    }, 
    body:
    { 
        --payload--
    }
}
```

```header``` składa się z pól:
- ```sender``` zawiera identyfikator noda w sieci. Jest to dodatnia liczba całkowita różna dla każdego noda, będąca równocześnie jego priorytetem. Aby zapewnić unikatowość id, node otrzymuje je od        publicznego noda głównego razem z listą nodów w sieci. 
- ```receiver``` zawiera id noda, do którego wiadomość jest skierowana, lub ```-1``` gdy wiadomość jest typu broadcast.
- ```message_type``` przechowuje liczbę będącą identyfikatorem typu wiadomości. Poniżej znajduje się lista typów wraz z ich identyfikatorem oraz opisem ciała.

Pole ```body``` zawiera ciało odpowiednie dla danego typu wiadomości.

## Pytanie o listę nodów
```message_type: 1```

Jest to wiadomość unicast do wyróżnionego noda. 

```
body:
{ 

}
```

## Odpowiedź z listą nodów
```message_type: 2```

Unicast od wyróżnionego noda do noda pytającego. Zawiera listę adresów publicznych nodów, listę nodów prywatnych oraz identyfikator zgłaszającego się noda. Informuje również o postępie obliczeń. 

```
body:
{
    public_nodes:
    [
        {id, adress_ip}
    ],
    connected_nodes:
    [
        id
    ],
    your_new_id: <int>,
    progress: --suitable--
}
```

## Broadcast powitalny
```message_type: 3```

Pierwsza wiadomość służąca do poinformowania innych nodów o dołączeniu do sieci.

```
body:
{
    
}
```

## Broadcast heart beat
```message_type: 4```

Co ustalony czas broadcast informujący o aktywaności noda w sieci. Jeśli node nie ortzyma takiej wiadomości po pewnym czasie, uznaje node za odłączony.

```
body:
{
    
}
```

## Broadcast "zajmuję dane"
```message_type: 5```

Informuje pozostałe nody, że zajmuje zadanie.
Może powodować konflikt.

```
body:
{
    task_id: <int>
}
```

## Bnicast "ok, zajmuj dane"
```message_type: 6```

Wysyłane po otrzymaniu wiadomości "zajmuję dane" oraz zaznaczeniu w swojej liście zadań jako zajęte. Służy do potwierdzenia, że dany node może zajmować się danym zadaniem.

```
body:
{

}
```

## Broadcast "obliczyłem"
```message_type: 7```

Informuje, że dane zadanie zostało wykonane. Przesyła wynik zadania.

```
body:
{
    task_id: <int>
    result: --suitable--
}
```


## Konflikty rezerwowania danych

### Problem:

Jeśli dwa nody równocześnie wyślą wiadomość "zajmuję dane", powstaje konflikt, ponieważ do niektórych nodów dojdzie jedna wiadomość jako pierwsza, do innych druga.

### Rozwiązanie:

Każdy node podczas dołączanie się do sieci, kiedy prosi node publiczny główny o listę adresów, dostaje od niego priorytet. Jako, że wszystkie priorytety ustala jeden node, każdy node będzie miał inny priorytet. Kiedy nastąpi konflikt, wygrywa go node z lepszym priorytetem. Jako, że wszystkie nody znają priorytety pozostałych, w tablicy zajętych danych będzie zapisany ten, o lepszym priorytecie, niezależnie od kolejności otrzymania wiadomości. Node, który przegrał konflikt, wybiera inne dane i ponawia próbę zajęcia ich. 

# Node

Każdy node składa się z dwóch głównych wątków, które muszą się komunikować: 
- **<font color="blue">Sieci</font>**
- **<font color="green">Obliczeń</font>**

Komunikacja między nimi przebiega z użyciem wzorca ```Active Object```.

<img style="height:300px" src="./img/koncept_watkow.PNG">

Dzięki temu żaden wątek nigdy nie zostaje zablokowany w ramach komunikacji (tak, jak to się dzieje np. przy użyciu wzorca ```Monitor```). ```Active Object``` operuje na strukturze danych obsługującej postęp oraz rezerwacje zadań. Implementacja ```Active object``` zakłada stworzenie kolejnego wątku, dalej nazywanego wątkiem **<font color="red">Planisty</font>**. Dzięki użyciu powyższej architektury, dodanie kolejnego wątku **<font color="green">Obliczeń</font>** nie wymaga żadnych zmian w kodzie. Dodatkowo, ```Active object``` udostępnia interfejs dla wątku **<font color="purple">GUI</font>**, dzięki któremu możliwe jest śledzenie stanu obiczeń. Zależności między wątkami będą zatem wyglądać następująco: 

<img style="height:300px" src="./img/komunikacja_miedzy_watkami.PNG">

# Wątek Sieci

# Wątek Stanu (Planisty)

Jest to wątek ```Scheduler``` we wzorcu projektowym ```Active object```. Z tego powodu w tej sekcji opisuję cały wzorzec projektowy, a nie tylko elementy, które działają w tym wątku. 

<img style="height:600px" src="./img/active_object_pl.svg">

*Źródło: https://upload.wikimedia.org/wikipedia/commons/f/f8/Active_object_pl.svg*

## Servant

Klasa Servant jest rdzeniem całego wzorca. To tutaj znajdują się dane, na których operuje ```Active Object```. Dlatego wszystkie publiczne metody dzielą się na dwie kategorie:
- Metody operacyjne - odpowiadają 1:1 wszystkim metodom ```Proxy``` oraz klasom implementującym ```MethodRequest```. Omawiam je w sekcji ```Proxy```.
- Predykaty - używane w implementacji metod ```boolean guard()``` klas implementujących ```MethodRequest```. Udostępniają informacje, dzięki którym ```Scheduler``` może stwierdzić, czy dana metoda może być aktualnie wywołana. 

Servant operuje na strukturach danych:
- lista zadań i ich stan (wolne, zajęte, obliczone)
- lista/y subskrybentów wzorca ```Observer``` jako obiekty Future zwrócone po użyciu odpowiednich metod. Służą do zawiadamiania innych wątków o zmianie stanu. 

Przykład: ```Wątek Obliczeń``` bierze zadanie i zaczyna liczyć. ```Wątek Sieci``` Zmienia stan tego samego zadania jako zajęte przez inny node na podstawie priorytetu. ```Wątek Obliczeń```, który zasubskrybował o otrzymywanie takiej informacji dostaje nakaz przerwania obliczeń za pośrednictwem zmiany stanu obiektu ```Future```

## Proxy

Jest interfejsem dla stanu obiektu. Jego metody są jedyną drogą komunikacji między innymi wątkami a obiektem. 

- ```Future Proxy.Initialize(myID, progress)```

    Wołane przez wątek ```Sieci```. Inicjuje tablicę postępu oraz informuje o przydzielonym id.

- ```Future Proxy.GetTask()```

    Wołane przez wątek ```Obliczeń```.

    Wybiera pierwsze dostępne zadanie i oznacza jako zajęte przez dany Node. Następnie informuje wątek ```Sieci``` o zajęciu zadania orza zwraca do ```Future``` zadanie.

    Informowanie wątku ```Sieci``` jest zaimplementowane za pomocą wzorca ```Observer``` subskrybującym jest obiekt ```Future```. Informowanie składa się ze zmiany stanu odpowiedniego ```Future``` oraz wybudzenia zainteresowanego wątku.

- ```Future Proxy.ReserveTask(id, task)```

    Wołane przez wątek ```Sieci``` w sytuacji konfliktu przegranego przez noda, dodatkowo informuje wątek ```Obliczeń``` o zmianie rezerwacji.

- ```Future Proxy.CompleteMyTask(task, result)```

    Wołane przez wątek ```Obliczeń``` oznacza zadanie jako wykonane oraz informuje wątek ```Sieci```.

- ```Future Proxy.CompleteTask(id, task, result)```

    Wołane przez wątek ```Sieci```

- Metody subskrybujące/odsubskrybujące

- Metody informacyjne dla GUI

# Wątek Obliczeń

# Wątek GUI