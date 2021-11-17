# Protokół komunikacji

## Priorytet stanu

Używany podczas aktualizacji stanu od strony wątku Sieci. Ma zapewnić jednoznaczność stanu na podstawie zbioru przekształceń niezależnie od ich kolejności. Niższy numer - wyższy priorytet:
1. Obliczone
2. Zajęte
3. Wolne

W sytuacji konfliktu w ramach stanu wygrywa ten, którego właścicielem jest Node o niższym id.

## Dołączanie do sieci

1. Tworzy połączenie z [Nodem głównym](#siec-peer-to-peer).
2. Wysyła wiadomość [Pytanie o listę nodów](#pytanie-o-liste-nodow) do [Noda głównego](#siec-peer-to-peer).
3. Otrzymuje wiadomość [Odpowiedź z listą nodów](#odpowiedz-z-lista-nodow).
4. Tworzy połączenie ze wszystkimi pozostałymi [Nodami publicznymi](#siec-peer-to-peer).
5. Rozpoczyna kolejkowanie wiadomości z sieci.
6. Wysyła [Broadcast powitalny](#broadcast-powitalny).
7. Wysyła wiadomość [Pytanie o stan obliczeń](#pytanie-o-stan-obliczen) do losowego Noda.
8. Otrzymuje wiadomość [Odpowiedź ze stanem obliczeń](#odpowiedz-ze-stanem-obliczen) i inicjalizuje [Stan](#watek-stanu).
9. Aplikuje otrzymane zmiany stanu do obiektu [Stanu](#watek-stanu).
10. Node rozpoczyna zwykłą pracę

Protokół jest zawodny w pewnym mało prawdopodobnym scenariuszu. Dlatego potrzebna jest dodatkowa synchronizacja stanu obliczeń między nodami. Zapewnia ją protokół [Końcowa synchronizacja](#koncowa-synchronizacja)

## Heart beat

Potrzebne są dwie struktury danych: 
- słownik ```id_noda : czas przedawnienia ostatniej wiadomości heart beat```. Elementy są dodawane podczas inicjalizacji na podstawie listy nodów, lub gdy node otrzyma [Wiadomość powitalną](#wiadomosc-powitalna). Jest aktualizowana przy każdym otrzymaniu wiadomości [Heart beat](#heart-beat).
- kolejka priorytetowa zaiwerająca pary ```(id_noda, czas przedawnienia danej wiadomości heart beat)```. Priorytetem jest czas przedawnienia (od najwcześniejszych). Przy otrzymaniu wiadomości [Heart beat](#heart-beat) odpowiednia para jest dodawana do kolejki. Element jest ściągany, gdy czas przedawnienia upłynie. Wtedy jest sprawdzany czas przedawnienia w słowniku i jeśli również upłynął, odpowiedni Node jest rozłączany. 

## Rezerwowanie zadania

1. Wątek obliczeń prosi o zadanie.
2. Wątek stanu daje zadanie i informuje wątek sieci o rezerwacji.
3. Wątek sieci wysyła wiadomość [```Zajmuje dane```](#broadcast-zajmuje-dane).
4. Pozostałe nody aktualizują swój stan na podstawie priorytetu i odsyłają odpowiedź [```Ok```](#unicast-ok) ze stanem po aktualizacji.
5. Node otrzymuje odpowiedzi [```Ok```](#unicast-ok).
6. Aktualizuje stan na podstawie odpowiedzi i jeśli to konieczne, przerywa odpowiedni wątek obliczeń oraz na jego miejsce generuje nowy.

## Kończenie zadania

1. Wątek obliczeń po zakończeniu zadania informuje o tym wątek Stanu.
2. Wątek Stanu zmienia stan i informuje o tym wątek Sieci.
3. Wątek Sieci wysyła wiadomość broadcast ```Obliczyłem```.
4. Pozostałe nody aktualizują stan według priorytetu i odsyłają odpowiedź [```Ok```](#unicast-ok) ze stanem po aktualizacji. 
5. Node otrzymuje odpowiedzi [```Ok```](#unicast-ok).
6. Aktualizuje stan na podstawie odpowiedzi i jeśli to konieczne, przerywa odpowiedni wątek obliczeń oraz na jego miejsce generuje nowy.

## Końcowa synchronizacja

1. Kiedy wszystkie zadania w stanie lokalnym staną się zajęte, lub obliczone aktywowany zostaje protokół końcowej synchronizacji. 
2. Wysyła wiadomości [Pytanie o stan zadań](#pytanie-o-stan-zadan) do nodów, które nie ukończyły swojego zadania (według lokalnego stanu). Putanie dla każdego noda dotyczy tylko powiązanych z nim zadań. 
3. Otrzymuje wiadomości [Odpowiedź ze stanem zadań](#odpowiedz-ze-stanem-zadan) i aktualizuje [Stan](#watek-stanu).

[Home](./index.md)