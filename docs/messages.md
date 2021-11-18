# Wiadomości

Każda wiadomość używana przy komunikacji między nodami ma postać pliku json o następującym formacie: 

```java
{
    "header":
    {
        "sender": <node_id>, 
        "receiver": <node_id | -1>, 
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
```java
"message_type": "get_init_data"
```

Jest to wiadomość unicast do publicznego głównego noda. Pole ```sender``` i ```receiver``` są puste, ponieważ wysyłający nie ma informacji o sieci.

```java
"body":
{ 

}
```

## Odpowiedź z listą nodów i nowym id
```java
"message_type": "give_init_data"
```

Unicast od publicznego głównego noda do noda pytającego. Zawiera listę adresów publicznych nodów, listę nodów prywatnych oraz identyfikator zgłaszającego się noda. 

```java
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
```java
"message_type": "hello"
```

Pierwsza wiadomość służąca do poinformowania innych nodów o dołączeniu do sieci. Pole ```ip``` zawiera adres ip nadawcy jeśli posiada publiczny adres ip. W przeciwnym przypadku jest puste.

```java
"body":
{
    "ip": <ip_address | null>,
}
```

## Pytanie o postęp obliczeń
```java
"message_type": "get_progress"
```

Jest to wiadomość unicast do losowego noda. Wysyłana po utworzeniu połączeń z pozostałymi nodami.

```java
"body":
{ 

}
```

## Odpowiedź z postępem obliczeń
```java
"message_type": "give_progress"
```

Unicast od pytanego noda do noda pytającego. Zawiera stan zadań. 

```java
"body":
{
    "progress": --suitable--
}
```

## Heart beat
```java
"message_type": "heart_beat"
```

Co ustalony czas broadcast informujący o aktywaności noda w sieci. Jeśli pierwszy node nie otrzyma takiej wiadomości od drugiego, po pewnym czasie uznaje ten drugi za odłączony.

```java
"body":
{
    
}
```

## Zajmuję dane
```java
"message_type": "reserve"
```

Broadcast informujący pozostałe nody, że nadawca zajmuje dane zadanie.

```java
"body":
{
    "task_id": <task_id>
}
```

## Potwierdzenie zajęcia danych
```java
"message_type": "confirmation"
```

Unicast od noda informowanego do informującego. Wysyłany po otrzymaniu wiadomości ```zajmuję dane``` oraz zaktualizowaniu lokalnego ```Stanu```. Służy do synchronizacji struktur stanów zadań. 

```java
"body":
{
    "task_id": <task_id>,
    "state": <"free" | "reserved" | "calculated">,
    "owner": <null | node_id>,
    "result": <null | result_obj>
}
```

## Obliczyłem
```java
"message_type": "calculated"
```

Informuje, że dane zadanie zostało wykonane. Przesyła wynik zadania.

```java
"body":
{
    "task_id": <task_id>,
    "result": --suitable--
}
```

## Pytanie o synchronizację niedokończonych zadań
```java
"message_type": "get_synchronization"
```

Unicast z listą zadań, które wymagają synchronizacji. Wysyłany w momencie, gdy ostatnie zadanie zostało zajęte według stanu lokalnego. Prośba o odesłanie stanu zadań skierowana jest bezpośrednio do noda, który miał liczyć dane zadania (jeśli więcej nodów liczy zadania, do każdego jest wysyłane osobne pytanie tylko o jego zadania).

```java
"body":
{
    "tasks": [
        {"task_id": <task_id>}
    ]
}
```

## Odpowiedź synchronizująca zadania
```java
"message_type": "give_synchronization"
```

Wiadomość unicast z listą stanów zadań, o które pytał adresat. 

```java
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

[Home](./index.md)