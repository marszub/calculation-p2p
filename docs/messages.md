# Wiadomości

```
{
    header:
    {
        sender: <node_id>, 
        receiver: <broadcast=-1 | node_id>, 
        message_type: <enum>
    }, 
    body:
    { 
        --payload--
    }
}
```

```header``` składa się z pól:
- ```sender``` zawiera identyfikator noda w sieci. Jest to dodatnia liczba całkowita różna dla każdego noda, będąca równocześnie jego priorytetem. Aby zapewnić unikatowość id, node otrzymuje je od publicznego noda głównego razem z listą nodów w sieci. 
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

## Pytanie o postęp obliczeń
```message_type: 1```

Jest to wiadomość unicast do losowego noda. 

```
body:
{ 

}
```

## Odpowiedź z postępem obliczeń
```message_type: 2```

Unicast od pytanego noda do noda pytającego. Zawiera stan obliczeń. 

```
body:
{
    progress: --suitable--
}
```

## Broadcast powitalny
```message_type: 3```

Pierwsza wiadomość służąca do poinformowania innych nodów o dołączeniu do sieci. Pole ```ip``` zawiera adres ip nadawcy jeśli posiada publiczny adres ip. W przeciwnym przypadku zawiera napis "None".

```
body:
{
    ip: <ip_address | None>,
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

## Unicast "ok"
```message_type: 6```

Wysyłane po otrzymaniu wiadomości "zajmuję dane" lub "obliczyłem" oraz zaktualizowaniu lokalnego ```Stanu```. Służy do potwierdzenia, że dany node może zajmować się danym zadaniem. Dodatkowo synchronizuje stany nodów. 

```
body:
{
    task_id: <int>
    state: <free|reserved|calculated>
    owner: <null|node_id>
    result: <null|result_obj>
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

## Pytanie o stan zadań

Unicast z listą zadań, które wymagają synchronizacji.

```
body:
{
    tasks: [
        task_id
    ]
}
```

## Odpowiedź ze stanem zadań

Odpowiedź unicast z listą stanów zadań, o które putał adresat.

```
body:
{
    tasks: [
        {
            task_id: <int>
            state: <free|reserved|calculated>
            owner: <null|node_id>
            result: <null|result_obj>
        }
    ]
}
```


## Konflikty rezerwowania danych

### Problem:

Jeśli dwa nody równocześnie wyślą wiadomość "zajmuję dane", powstaje konflikt, ponieważ do niektórych nodów dojdzie jedna wiadomość jako pierwsza, do innych druga.

### Rozwiązanie:

Każdy node podczas dołączania się do sieci, kiedy prosi node publiczny główny o listę adresów, dostaje od niego priorytet. Jako, że wszystkie priorytety ustala jeden node, każdy node będzie miał inny priorytet. Kiedy nastąpi konflikt, wygrywa go node z lepszym priorytetem. Jako, że wszystkie nody znają priorytety pozostałych, w tablicy zajętych danych będzie zapisany ten, o lepszym priorytecie, niezależnie od kolejności otrzymania wiadomości. Node, który przegrał konflikt, wybiera inne dane i ponawia próbę zajęcia ich. 

[Home](./index.md)