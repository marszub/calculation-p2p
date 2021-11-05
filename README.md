# Obliczenia peer to peer

## Komunikaty

Odbiorca:
- Unicast
- Broadcast

### Wiadomości

Szkielet:
```
{
    header:
    {
        sender: <priority number>, 
        receiver: <broadcast=-1 | receiver_id>, 
        message_type:<enum>
    }, 
    body:
    { 
        --payload--
    }
}
```

#### pytanie o listę nodów
Jest to wiadomość unicast do wyróżnionego noda.

```
body:
{ 

}
```

#### odpowiedź z listą nodów

Unicast od wyróżnionego noda do noda pytającego. Zawiera listę adresów publicznych nodów, listę nodów prywatnych oraz identyfikator zgłaszającego się noda.

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
}
```

#### broadcast powitalny
Pierwsza wiadomość służąca do poinformowania innych nodów o dołączeniu do sieci.

```
body:
{
    
}
```

#### broadcast heart beat
Co ustalony czas broadcast informujący o aktywaności noda w sieci. Jeśli node nie ortzyma takiej wiadomości po pewnym czasie, uznaje node za odłączony.

```
body:
{
    
}
```

#### broadcast "zajmuję dane"
Informuje pozostałe nody, że zajmuje zadanie.
Może powodować konflikt.

```
body:
{
    task_id: <int>
}
```

#### unicast "ok, zajmuj dane"
Wysyłane po otrzymaniu wiadomości "zajmuję dane" oraz zaznaczeniu w swojej liście zadań jako zajęte. Służy do potwierdzenia, że dany node może zajmować się danym zadaniem.

```
body:
{

}
```

#### broadcast "obliczyłem"
Informuje, że dane zadanie zostało wykonane. Przesyła wynik zadania.

```
body:
{
    task_id: <int>
    result: --suitable--
}
```


### Konflikty pobierania danych

Jeśli dwa nody równocześnie wyślą wiadomość "zajmuję dane", powstaje konflikt, ponieważ do niektórych nodów dojdzie jedna wiadomość jako pierwsza, do innych druga.

Rozwiązanie:

Każdy node podczas dołączanie się do sieci, kiedy prosi node publiczny główny o listę adresów, dostaje od niego priorytet. Jako, że wszystkie priorytety ustala jeden node, każdy node będzie miał inny priorytet. Kiedy nastąpi konflikt, wygrywa go node z lepszym priorytetem. Jako, że wszystkie nody znają priorytety pozostałych, w tablicy zajętych danych będzie zapisany ten, o lepszym priorytecie, niezależnie od kolejności otrzymania wiadomości. Node, który przegrał konflikt, wybiera inne dane i ponawia próbę zajęcia ich. 

## Sieć peer to peer

Składa się z nodów:
- <font color="red">publicznych</font> - o adresie ip publicznym (czerwone)
- <font color="green">prywatnych</font> - bez publicznego ip (zielone)
- **<font color="red">publicznego głównego</font>** - o publicznym adresie ip, z którym ma się łączyć każdy node dołączający do sieci. (czerwony pogrubiony)

### Rodzaje połączeń

-   czysty p2p

    Każdy node publiczny komunikuje się bezpośrednio z pozostałymi. Nody prywatne tworzą pośrednie połączenia z innymi nodami prywatnymi poprzez nody publiczne (jeden node prywatny ma nawet lilka połączeń z nodem publicznym. Każde do obsługi innego połączenia nodów prywatnych).

    Broadcast w sieci:

    <img style="height:400px" src="./broadcastPure.PNG">

    Minus - niepotrzebne mnożenie obciążenia publicznych nodów, nierównomierne obciążenie

    Plus - prosta implementacja. Osobne połączenie dla każdego noda. Broadcast: wysyłamy każdym połączeniem. Unicast: wysyłamy połączeniem przydzielonym do noda, nie musimy wiedzieć, czy jest bezpośrednie, czy nie.

-   Wspólne połączenie

    Jak wyżej, ale "wiązki" połączeń są jednym połączeniem. Node publiczny na podstawie zawartości wiadomości (informacji o adresacie) wybiera dalszą drogę wiadomości. 

    <img style="height:400px" src="./komunikacjaPure.PNG">

-   wybór reprezentanta

    <img style="height:400px" src="./komunikacjaReprezentant.PNG">

    Każdy prywatny node wybiera swojego publicznego reprezentanta, do którego przesyła całą komunikację. Publiczny node musi przetworzyć i dalej rozesłać wiadomości. Broadcast może składać się tylko z jednej wiadomości do noda publicznego, który potem kopiuje i rozsyła wiadomość dalej:

    <img style="height:400px" src="./broadcastOpt.PNG">

    Minus - publiczne nody muszą przetwarzać wiadomości prywatnych nodów. Prywatny node ma bezpośrednie połączenie z innymi publicznymi nodami, którego nie wykorzystuje. Nody inaczej wysyłają wiadomości w zależności od publiczności ich adresu ip (komplikacja kodu)

    Plus - dwukrotnie zmniejszamy ruch broadcastowy. Nie tworzymy osobnych połączeń dla każdej pary nodów prywatnych.

-   **hybryda**

    Prywatny node wysyła wiadomości bezpośrednio do nodów publicznych, ale za pośrednikiem reprezentanta do prywatnych. Broadcast wygląda następująco:

    <img style="height:400px" src="./broadcastHybrid.PNG">

    Minus - każdy node przy wysyłaniu i pośredniczeniu musi sprawdzać, czy odbiorca jest publiczny, czy prywatny. Dodatkowo musi wiedzieć, czy sam jest publiczny, czy prywatny. Dodatkowe skomplikowanie kodu.

    Plus - Rozwiązanie maksymalnie optymalne pod względem obciążenia sieci oraz nodów publicznych. 
