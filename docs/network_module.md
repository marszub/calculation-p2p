# Moduł sieci

## Wątek "Server"

Nieaktywnie oczekuje na nadchodzące połączenia. 

Tworzy ```ServerSocket``` nasłuchujący na porcie podanym w pliku konfiguracyjnym lub przy starcie programu. Gdy połączenie nadejdzie, przekazuje otrzymany socket wątkowi ```Network``` za pośrednictwem [TODO]

## Wątek "Connections"

Nieaktywnie oczekuje na nadchodzące połączenia oraz nasłuchuje równocześnie na wszystkich połączeniach. Gdy z któregoś połączenia nadejdzie wiadomość, przekazuje ją wątkowi ```Network``` za pośrednictwem [TODO]. Jeśli zostanie utworzone nowe połączenie, żaden inny wątek nie jest informowany. 

Wątek ```Connections``` tworzy ```Selector selector```, w którym rejestrują się gniazda połączeń i serwera. 

### Otrzymanie połączenia

```ServerSocketChannel``` nasłuchuje na porcie podanym w pliku konfiguracyjnym lub przy starcie programu. Rejestruje się w obiekcie ```selector``` na zbiorze zdarzeń ```SelectionKey.OP_ACCEPT```.

Gdy nowe połączenie oczekuje na danym porcie, jest akceptowane. Zostaje utworzony nowy obiekt ```Connection(SocketChannel socket, Selector selector)```, do którego przekazywany zostaje zwrócony socket oraz ```selector```. Obiekt zostaje dodany do listy ```List<Connection> incomingConnections```. 

### Tworzenie połączenia

Nowe połączenia tworzy wątek ```Network``` po otrzymaniu adresu ip danego węzła (wiadomość [powitalna](./messages.md#wiadomosc-powitalna), lub [odpowiedź z listą nodów](./messages.md#odpowiedz-z-lista-nodow-i-nowym-id)). Najpierw towrzony jest ```Connection(InetSocketAddress ipTcpAddress, Selector selector)``` z odpowiednim adresem ip oraz obiektem ```selector```. Następnie zostaje dodany na listę ```List<Connection> outcomingConnections```

### Nadejście wiadomości

Każdemu połączeniu odpowiada osobny obiekt typu ```Connection```. W jego skład wchodzi ```SocketChannel socket``` oraz ```Selector selector```.

[TODO]










## Wątek "Network"

Jest odpowiedzialny za przetwarzanie ruchu w sieci oraz wysyłanie wiadmości zleconych przez moduł ```State```. 

Aby uniknąć aktywnego oczekiwania, wątek ```Network``` zasypia gdy nie ma wiadomości do przetworzenia, ani wysłania. Ma on jednak trzy różne źródła zadań: wątki ```Connections```, ```Server``` i ```State```. Każdy z nich ma możliwość wybudzenia wątku za pomocą metody ```Thread.interrupt()```.











Otrzymuje, przetwarza i wysyła wiadomości w sieci Nodów. Monitoruje ```heart beat``` wszystkich Nodów i zamyka połączenia po przekroczeniu ustalonego czasu bez wiadomości. Wywołuje metody na obiekcie ```Stanu``` i otrzymuje od niego zgłoszenia w ramach subskrypcji. 

Generuje wątek ```Serwera```, który nasłuchuje nadchodzące połączenia. Wątek serwera w momencie otrzymania połączenia generuje wątek ```Połączenia```, który od teraz odbiera dane i kolejkuje je do obsłużenia przez wątek ```Sieci```. Dodatkowo przy uruchamianiu programu, wątek ```Sieci``` tworzy wątki ```Połączenia``` dla każdego publicznego Noda w sieci. 

Wątek ```Sieci```, gdy nie ma nic do roboty, czeka nieaktywnie. Budzony jest przez wątki ```Stanu```, ```Połączenia```, lub ```Serwera``` (niekoniecznie). Wątki ```Serwera``` oraz ```Połączenia``` czekają nieaktywnie na operacjach odpowiednio: akceptacji połączenia oraz czytania ze strumienia (powinno być zaimplementowane w bibliotekach).

Aby wysłać wiadomość, nie potrzeba synchronizacji z wątkiem odbierającym. Potrzebna jest ona natomiast przy kolejkowaniu i odbieraniu wiadomości na drodze wątek ```Połączenia``` -> wątek ```Sieci```. Jest to problem ```NP1C1B```. Rozwiązujemy za pomocą ```LinkedBlockingQueue```.

Wątek ```Heart``` wysyła wiadomość broadcast heart beat do wszystkich nodów w sieci (albo zleca wysłanie wątkowi ```Sieci```).

Kolejną współdzeloną strukturą danych jest tablica aktywnych Nodów, która musi być chroniona. Wątek ```Serwera``` dodaje do niej nowe połączenia, wątki ```Połączenia``` usuwają z niej zapisy po wykryciu, że połączenie zostało urwane. Wymuszone zamknięcie połączenia na skutek nie otrzymania wiadomości ```heart beat``` realizowane przez wątek ```Sieci``` zostanie zauważone przez wątek ```Połączenia```. Problem można uprościć do problemu ```1PNC1B```. Rozwiązujemy za pomocą synchronizowanej tablicy/listy.

Gdy wątek ```Sieci``` otrzyma wiadomość powitalną od noda publicznego i sam jest nodem prywatnym, nawiązuje połączenie z nadawcą. (w pozostałych przypadnkach, połączenie nawiązuje adresat, lub połączenia ma nie być)

[Home](./index.md)