Klasyczna gra Saper - stworzona w ramach projektu warsztat�w Back-end Developer.

Gra wykonana w oparciu o �rodowisko JavaFX oraz model MVC.

Zasada dzia�ania aplikacji:

Przy u�yciu MySQL zosta�a stworzona baza danych z tabel� zawieraj�c� rezultaty rozgrywki.

Do rozpocz�cia rozgrywki wymagane jest podanie imienia/pseudonimu gracza. Gracz mo�e dokona� r�wnie� wyboru ilo�ci min (trudno�� rozgrywki) jaka ma znajdowa� si� na planszy. Rozmiar planszy w ka�dej grze jest taki sam i wynosi 10x10. Domy�lna ilo�� min (je�eli gracz nie zmieni wyboru) to 10. Maksymalna to 50.

Po podaniu imienia/pseudonimu oraz ilo�ci min gracz klika przycisk GRAJ!

W tym momencie sprawdzana jest obecno�� wprowadzonego imieni/pseudonimu gracza w bazie danych przechowuj�cej wyniki. Je�eli gracz wprowadzi warto�� ju� istniej�c� w tabeli rezultat�w w�wczas zostanie poinformowany o swoim najlepszym czasie uzyskanym w poprzedniej rozgrywce przy okreslonej liczbie min. Je�eli gracz poda imi�/pseudonim nie wyst�puj�ce w bazie danych b�d� zdecyduje si� zagra� przy innej ni� poprzednio liczbie min w�wczas b�dzie to zarejestrowane jako nowa rozgrywka gracza.

W trakcie rozgrywki kiedy gracz trafi na min� gra jest przerywana komunikatem. Gracz ma mo�liwo�� wyboru nowej gry b�d� wyj�cia z programu.

Je�eli gracz rozbroi pole pokazuje si� okno z wynikiem i aktualnym miejscem w rankigu. Wy�wietlany ranking dotyczy tylko rezultat�w uzyskanych przy okreslonej liczbie min. Je�eli gracz nie poprawi swojego wcze�niejszego wyniku jest informowany, �e tego nie dokona� i w oknie z wynikami jest przedstawiany jego najlepszy wynik.