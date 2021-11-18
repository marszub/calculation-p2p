# Protokół komunikacji

## Priorytet stanu

Używany podczas aktualizacji stanu od strony wątku Sieci. Ma zapewnić jednoznaczność stanu na podstawie zbioru przekształceń niezależnie od ich kolejności. Niższy numer - wyższy priorytet:
1. Obliczone
2. Zajęte
3. Wolne

W sytuacji konfliktu w ramach stanu wygrywa ten, którego właścicielem jest Node o niższym id.

## Dołączanie do sieci

1. Tworzy połączenie z nodem **<font color="red">głównym publicznym</font>**.
2. Wysyła wiadomość [pytanie o listę nodów](./messages.md#pytanie-o-liste-nodow-i-nowe-id) do **<font color="red">głównego publicznego</font>**.
3. Otrzymuje wiadomość [odpowiedź z listą nodów](./messages.md#odpowiedz-z-lista-nodow-i-nowym-id).
4. Tworzy połączenie ze wszystkimi pozostałymi nodami <font color="red">publiczneymi</font>.
5. Rozpoczyna kolejkowanie wiadomości z sieci.
6. Wysyła [wiadomość powitalną](./messages.md#wiadomosc-powitalna) do wszystkich nodów.
7. Wysyła [pytanie o postęp obliczeń](./messages.md#pytanie-o-postep-obliczen) do losowego noda.
8. Otrzymuje [odpowiedź z postępem obliczeń](./messages.md#odpowiedz-z-postepem-obliczen) i inicjalizuje [stan](./state_module.md).
9. Aplikuje otrzymane zmiany stanu do obiektu stanu.
10. Node rozpoczyna zwykłą pracę.

Protokół jest zawodny w pewnym scenariuszu. Dlatego potrzebna jest dodatkowa synchronizacja stanu obliczeń między nodami. Zapewnia ją [potwierdzenie zajęcia danych](./messages.md#potwierdzenie-zajecia-danych) oraz protokół [końcowej synchronizacji](#koncowa-synchronizacja).

## Heart beat

Potrzebne są dwie struktury danych: 
- słownik ```{node_id: beat_timeout}```. Dla każdego noda przechowuje czas przedawnienia ostatniej wiadomości [heart beat](./messages.md#heart-beat). Elementy są dodawane podczas inicjalizacji na podstawie listy nodów, lub gdy node otrzyma [wiadomość powitalną](./messages.md#wiadomosc-powitalna). Jest aktualizowana przy każdym otrzymaniu wiadomości heart beat.
- kolejka priorytetowa zaiwerająca pary ```(id_noda, beat_timeout)```. Priorytetem jest czas przedawnienia (od najwcześniejszych). Przy otrzymaniu wiadomości heart beat odpowiednia para jest dodawana do kolejki. Element jest ściągany, gdy czas przedawnienia upłynie. Wtedy jest sprawdzany czas przedawnienia w słowniku i jeśli również upłynął, odpowiedni node jest rozłączany. 

## Rezerwowanie zadania

1. Wątek [obliczeń](./calculation_module.md) prosi o zadanie.
2. Wątek [stanu](./state_module.md) daje zadanie i informuje wątek [sieci](./network_module.md) o rezerwacji.
3. Wątek sieci wysyła wiadomość "[zajmuję dane](./messages.md#zajmuje-dane)".
4. Pozostałe nody aktualizują swój stan na podstawie priorytetu i odsyłają [potwierdzenie zajęcia danych](./messages.md#potwierdzenie-zajecia-danych) ze stanem po aktualizacji.
5. Node otrzymuje potwierdzenia zajęcia danych.
6. Aktualizuje stan na podstawie odpowiedzi i jeśli to konieczne, informuje odpowiedni wątek obliczeń o zadaniu do przerwania.

## Kończenie zadania

1. Wątek [obliczeń](./calculation_module.md) po zakończeniu zadania informuje o tym wątek [stanu](./state_module.md).
2. Wątek stanu zmienia stan i informuje o tym wątek [sieci](./network_module.md).
3. Wątek sieci wysyła wiadomość broadcast "[obliczyłem](./messages.md#obliczylem)".
4. Pozostałe nody aktualizują stan według priorytetu. 

## Końcowa synchronizacja

1. Aktywuje się, gdy wszystkie zadania w stanie lokalnym staną się zajęte, lub obliczone. 
2. Wysyła [pytania o synchronizację niedokończonych zadań](./messages.md#pytanie-o-synchronizacje-niedokonczonych-zadan) do nodów, które nie ukończyły swojego zadania (według lokalnego stanu). Pytanie dla każdego noda dotyczy tylko powiązanych z nim zadań. 
3. Otrzymuje [odpowiedzi synchronizujące zadania](./messages.md#odpowiedz-synchronizujaca-zadania) i aktualizuje [stan](./state_module.md).

[Home](./index.md)