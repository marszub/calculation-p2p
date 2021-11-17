# Wiadomości

```json
{
    "header":
    {
        "sender": <node_id>, 
        "receiver": <broadcast=-1 | node_id>, 
        "message_type": <message_type>
    }, 
    "body":
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

## Pytanie o listę nodów i nowe id
```json
"message_type": "get_init_data"
```

Jest to wiadomość unicast do publicznego głównego noda. Pole ```sender``` i ```receiver``` są puste, ponieważ wysyłający nie ma informacji o sieci.

```json
"body":
{ 

}
```

## Odpowiedź z listą nodów i nowym id
```json
"message_type": "give_init_data"
```

Unicast od publicznego głównego noda do noda pytającego. Zawiera listę adresów publicznych nodów, listę nodów prywatnych oraz identyfikator zgłaszającego się noda. 

```json
"body":
{
    "your_new_id": <node_id>,
    "public_nodes":
    [
        {"id": <node_id>, "ip_address":<ip_address>}
    ],
    "connected_nodes":
    [
        {"id": <node_id>}
    ]
}
```

## Wiadomość powitalna
```json
"message_type": "hello"
```

Pierwsza wiadomość służąca do poinformowania innych nodów o dołączeniu do sieci. Pole ```ip``` zawiera adres ip nadawcy jeśli posiada publiczny adres ip. W przeciwnym przypadku jest puste.

```json
"body":
{
    "ip": <ip_address | null>,
}
```

## Pytanie o postęp obliczeń
```json
"message_type": "get_progress"
```

Jest to wiadomość unicast do losowego noda. Wysyłana po utworzeniu połączeń z pozostałymi nodami.

```json
"body":
{ 

}
```

## Odpowiedź z postępem obliczeń
```json
"message_type": "give_progress"
```

Unicast od pytanego noda do noda pytającego. Zawiera stan zadań. 

```json
"body":
{
    "progress": --suitable--
}
```

## Heart beat
```json
"message_type": "heart_beat"
```

Co ustalony czas broadcast informujący o aktywaności noda w sieci. Jeśli pierwszy node nie otrzyma takiej wiadomości od drugiego, po pewnym czasie uznaje ten drugi za odłączony.

```json
"body":
{
    
}
```

## Zajmuję dane
```json
"message_type": "reserve"
```

Broadcast informujący pozostałe nody, że nadawca zajmuje dane zadanie.

```json
"body":
{
    "task_id": <task_id>
}
```

## Potwierdzenie zajęcia danych lub zakończenia obliczeń
```json
"message_type": "confirmation"
```

Unicast od noda informowanego do informującego. Wysyłany po otrzymaniu wiadomości ```zajmuję dane``` lub ```obliczyłem``` oraz zaktualizowaniu lokalnego ```Stanu```. Służy do synchronizacji struktur stanów zadań. 

```json
"body":
{
    "task_id": <task_id>,
    "state": <"free" | "reserved" | "calculated">,
    "owner": <null | node_id>,
    "result": <null | result_obj>
}
```

## Obliczyłem
```json
"message_type": "calculated"
```

Informuje, że dane zadanie zostało wykonane. Przesyła wynik zadania.

```json
"body":
{
    "task_id": <task_id>,
    "result": --suitable--
}
```

## Pytanie o synchronizację niedokończonych zadań
```json
"message_type": "get_synchronization"
```

Unicast z listą zadań, które wymagają synchronizacji. Wysyłany w momencie, gdy ostatnie zadanie zostało zajęte według stanu lokalnego. Prośba o odesłanie stanu zadań skierowana jest bezpośrednio do noda, który miał liczyć dane zadania (jeśli więcej nodów liczy zadania, do każdego jest wysyłane osobne pytanie tylko o jego zadania).

```json
"body":
{
    "tasks": [
        {"task_id": <task_id>}
    ]
}
```

## Odpowiedź synchronizująca zadania
```json
"message_type": "give_synchronization"
```

Wiadomość unicast z listą stanów zadań, o które pytał adresat. 

```json
"body":
{
    "tasks": [
        {
            "task_id": <task_id>,
            "state": <"free" | "reserved" | "calculated">,
            "owner": <null | node_id>,
            "result": <null | result_obj>
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