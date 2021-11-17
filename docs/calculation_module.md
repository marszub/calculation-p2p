# Wątek Obliczeń

Prosi o zadania obiekt ```Stanu```, oblicza zadanie, informuje o skończeniu zadania.

Jest informowany o zajęciu aktualnie wykonywanego zadania przez inny node. Wtedy przerywa obliczenia i znów prosi o zadanie.

Aby zmilimalizować szansę uśpienia, prosi o nowe zadanie jeszcze przed końcem poprzedniego.

Edge case: 
- Nie ma zadań, ale nie wszystko obliczone - zasypia w oczekiwaniu na powiadomienie o skończeniu wszystkich zadań, lub zwolnieniu zadania
- Wszystko obliczone - zakończ działanie wątku

[Home](./index.md)