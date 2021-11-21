# Moduł połączeń

Nieaktywnie oczekuje na nadchodzące połączenia oraz nasłuchuje równocześnie na wszystkich połączeniach. Gdy z któregoś połączenia nadejdzie wiadomość, przekazuje ją wątkowi ```Network``` za pośrednictwem kolejki ```mssagesToProcess```. Jeśli zostanie utworzone nowe połączenie, żaden inny wątek nie jest informowany. 

Wątek ```Connections``` tworzy ```Selector selector```, w którym rejestrują się gniazda połączeń i serwera. 

## Otrzymanie połączenia

```ServerSocketChannel``` nasłuchuje na porcie podanym w pliku konfiguracyjnym lub przy starcie programu. Rejestruje się w obiekcie ```selector``` na zbiorze zdarzeń ```SelectionKey.OP_ACCEPT```.

Gdy nowe połączenie oczekuje na danym porcie, jest akceptowane. Zostaje utworzony nowy obiekt ```Connection(SocketChannel socket, Selector selector)```, do którego przekazywany zostaje zwrócony socket oraz ```selector```. Obiekt zostaje dodany do listy ```List<Connection> incomingConnections```. 

## Tworzenie połączenia

Nowe połączenia tworzy wątek ```Network``` po otrzymaniu adresu ip danego węzła (wiadomość [powitalna](./messages.md#wiadomosc-powitalna), lub [odpowiedź z listą nodów](./messages.md#odpowiedz-z-lista-nodow-i-nowym-id)). Najpierw towrzony jest ```Connection(InetSocketAddress ipTcpAddress, Selector selector)``` z odpowiednim adresem ip oraz obiektem ```selector```. Następnie zostaje dodany na listę ```List<Connection> outcomingConnections```

## Nadejście wiadomości

Każdemu połączeniu odpowiada osobny obiekt typu ```Connection```. W jego skład wchodzi ```SocketChannel socket``` oraz ```Selector selector```. Jeśli na kanale ```socket```pojawi się wiadomość, ```selector``` wybudzi wątek, wiadomość zostanie odebrana i skonwertowana na typ ```Message```. Utworzony obiekt zostaje położony na kolejkę ```BlockingQueue<Message> messagesToProcess```.

## Odnawianie połączenia

W sytuacji urwania połączenia przychodzącego zostaje usunięty powiązany z nim obiekt ```Connection```, a ```socket``` wyrejestrowuje się z selektora ```selector```. Za odnowienie połączenia odpowiedzialny jest przeciwny węzeł. Przebiega ono tak, jak w przypadku nadejścia nowego połączenia. 

Gdy zostanie urwane połączenie wychodzące, wątek ```Connections``` do wiaduje się o tym przez ```selector```. Podejmuje on próbę ponownego połączenia. Jeśli się powiedzie, ```selector``` informuje o tym, co skutkuje obudzeniem wątku ```Network```, który próbuje ponownie wysłać oczekujące wiadoości. 