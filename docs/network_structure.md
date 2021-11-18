# Struktura sieci

Sieć peer to peer się z nodów:
- <font color="red">publicznych</font> \- o publicznym adresie ip.
- <font color="lightgreen">prywatnych</font> \- bez publicznego adresu ip.
- **<font color="red">publicznego głównego</font>** \- o publicznym adresie ip, z którym ma się łączyć każdy node dołączający do sieci. Jego adres ip jest udostępniony wraz z programem.

## Połączenia w sieci

Ze względu na protokół NAT, niemożliwe jest połączenie się z Nodem prywatnym. Dlatego połączenie <font color="red">publiczny</font> -> <font color="lightgreen">prywatny</font> może być realizowane tylko przez połączenie Noda <font color="lightgreen">prywatnego</font> do <font color="red">publicznego</font>. Dodatkowo, bezpośrednie połączenie <font color="lightgreen">prywatny</font> -> <font color="lightgreen">prywatny</font> jest niemożliwe do realizacji. Takie połączenia realizowane są pośrednio poprzez dowolnego noda <font color="red">publicznego</font>.

<img style="height:300px" src="./img/siec_p2p.PNG">

*Rysunek 1: Połączenia w sieci peer to peer.*

Broadcast w sieci nadany przez noda <font color="red">publicznego</font> jest wysyłany w postaci wiadomości unicastowych do wszystkich pozostałych nodów bezpośrednimi połączeniami TCP. 

<img style="height:300px" src="./img/broadcast_public.PNG">

*Rysunek 2: Broadcast nadany przez node <font color="red">publiczny</font> (Node 3).*

Node <font color="lightgreen">prywatny</font> aby wysłać broadcast losuje wśród nodów publicznych swojego reprezentanta. Wysyła do niego wiadomość oznaczoną jako broadcast, a do pozostałych nodów <font color="red">publicznych</font> wysyła wiadomości unicast. Reprezentant po otrzymaniu wiadomości broadcast przetwarza ją, i wysyła do wszystkich nodów prywatnych jako wiadomość unicast. 

<img style="height:300px" src="./img/broadcast_prywatny.PNG">

*Rysunek 3: Broadcast nadany przez node <font color="lightgreen">prywatny</font> (Node 1).*

[Home](./index.md)