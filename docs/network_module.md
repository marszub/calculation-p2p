# Moduł sieci

Jest odpowiedzialny za przetwarzanie ruchu w sieci oraz wysyłanie wiadmości zleconych przez moduł ```State```. 

Aby uniknąć aktywnego oczekiwania, wątek ```Network``` zasypia gdy nie ma wiadomości do przetworzenia, ani wysłania. Ma on dwa źródła zadań: wątki ```Connections``` i ```State```. Każdy z nich ma możliwość wybudzenia wątku za pomocą metody ```Thread.interrupt()```.

## Wysyłanie wiadomości

Aby wysłać wiadomość, wywoływana jest metoda ```NetworkInterface.send(Message message)``` na obiekcie odpowiadającym id węzła docelowego. Relacja węzła i obiektu ```NetworkInterface``` jest 1:1. Jeśli adresat jest prywatny, zostaje wylosowany publiczny pośrednik. Wiadomość jest wysyłana przez odpowiednie gniazdo, a w przypadku niepowodzenia jest kolejkowana w ramach obiektu ```NetworkInterface``` i wysyłana gdy połączenie zostanie wznowione. 

## Odbieranie wiadomości

Wątek ```Network``` odbiera wiadomości z kolejki ```messagesToProcess```, a nastęnie je przetwarza.

## Wznawianie połączenia

Wątek, gdy zostanie poinformowany o odnwieniu połączenia, prubuje wysłać wszystkie oczekujące wiadomości.

## Zmiana stanu

Gdy wątek otrzyma wiadomość, która informuje o zmianie stanu, zostaje wywołana odpowiednia metoda na obiekcie ```StateUpdater``` pełni on funkcję proxy dla modułu ```State```.

## Heart beat

Czas od ostatniego broadcastu ```heart beat``` jest monitorowany i gdy przekroczy wartość podaną w pliku konfiguracyjnym, wysyła kolejne=y broadcast, a czas jest resetowany. Kiedy wątek zasypia, jako maksymalny czas bezczynności podaje taki, aby po wybudzeniu należało wysłać kolejny ```heart beat```.

[Home](./index.md)