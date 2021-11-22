# Moduł message

Jest odpowiedzialny za przetwarzanie ruchu z sieci oraz wysyłanie wiadmości zleconych przez moduł ```State```. 

Aby uniknąć aktywnego oczekiwania, wątek ```Network``` zasypia gdy nie ma wiadomości do przetworzenia, ani wysłania. Ma on dwa źródła zadań: wątki ```Network``` i ```State```. Każdy z nich ma możliwość wybudzenia wątku za pomocą metody ```Thread.interrupt()```.

## Wysyłanie wiadomości

Aby wysłać wiadomość, wywoływana jest metoda ```network.routing.Router.send(Message message)```.

## Odbieranie wiadomości

Wątek ```Message``` odbiera wiadomości w postaci listy dzięki metodzie ```network.routing.Router.getMessages()```, a nastęnie je przetwarza.

## Zmiana stanu

Gdy wątek otrzyma wiadomość, która informuje o zmianie stanu, zostaje wywołana odpowiednia metoda na obiekcie ```StateUpdater``` pełni on funkcję proxy dla modułu ```State```.

## Heart beat

Czas od ostatniego broadcastu ```heart beat``` jest monitorowany i gdy przekroczy wartość podaną w pliku konfiguracyjnym, wysyła kolejny broadcast, a czas jest resetowany. Kiedy wątek zasypia, jako maksymalny czas bezczynności podaje taki, aby po wybudzeniu należało wysłać kolejny ```heart beat```.

[Home](./index.md)