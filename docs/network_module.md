# Wątek Sieci

Otrzymuje, przetwarza i wysyła wiadomości w sieci Nodów. Monitoruje ```heart beat``` wszystkich Nodów i zamyka połączenia po przekroczeniu ustalonego czasu bez wiadomości. Wywołuje metody na obiekcie ```Stanu``` i otrzymuje od niego zgłoszenia w ramach subskrypcji. 

Generuje wątek ```Serwera```, który nasłuchuje nadchodzące połączenia. Wątek serwera w momencie otrzymania połączenia generuje wątek ```Połączenia```, który od teraz odbiera dane i kolejkuje je do obsłużenia przez wątek ```Sieci```. Dodatkowo przy uruchamianiu programu, wątek ```Sieci``` tworzy wątki ```Połączenia``` dla każdego publicznego Noda w sieci. 

Wątek ```Sieci```, gdy nie ma nic do roboty, czeka nieaktywnie. Budzony jest przez wątki ```Stanu```, ```Połączenia```, lub ```Serwera``` (niekoniecznie). Wątki ```Serwera``` oraz ```Połączenia``` czekają nieaktywnie na operacjach odpowiednio: akceptacji połączenia oraz czytania ze strumienia (powinno być zaimplementowane w bibliotekach).

Aby wysłać wiadomość, nie potrzeba synchronizacji z wątkiem odbierającym. Potrzebna jest ona natomiast przy kolejkowaniu i odbieraniu wiadomości na drodze wątek ```Połączenia``` -> wątek ```Sieci```. Jest to problem ```NP1C1B```. Rozwiązujemy za pomocą ```LinkedBlockingQueue```.

Wątek ```Heart``` wysyła wiadomość broadcast heart beat do wszystkich nodów w sieci (albo zleca wysłanie wątkowi ```Sieci```).

Kolejną współdzeloną strukturą danych jest tablica aktywnych Nodów, która musi być chroniona. Wątek ```Serwera``` dodaje do niej nowe połączenia, wątki ```Połączenia``` usuwają z niej zapisy po wykryciu, że połączenie zostało urwane. Wymuszone zamknięcie połączenia na skutek nie otrzymania wiadomości ```heart beat``` realizowane przez wątek ```Sieci``` zostanie zauważone przez wątek ```Połączenia```. Problem można uprościć do problemu ```1PNC1B```. Rozwiązujemy za pomocą synchronizowanej tablicy/listy.

Gdy wątek ```Sieci``` otrzyma wiadomość powitalną od noda publicznego i sam jest nodem prywatnym, nawiązuje połączenie z nadawcą. (w pozostałych przypadnkach, połączenie nawiązuje adresat, lub połączenia ma nie być)

[Home](./index.md)