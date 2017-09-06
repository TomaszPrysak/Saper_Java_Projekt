Klasyczna gra Saper - stworzona w ramach projektu warsztatów Back-end Developer.

Gra wykonana w oparciu o œrodowisko JavaFX oraz model MVC.

Zasada dzia³ania aplikacji:

Przy u¿yciu MySQL zosta³a stworzona baza danych z tabel¹ zawieraj¹c¹ rezultaty rozgrywki.

Do rozpoczêcia rozgrywki wymagane jest podanie imienia/pseudonimu gracza. Gracz mo¿e dokonaæ równie¿ wyboru iloœci min (trudnoœæ rozgrywki) jaka ma znajdowaæ siê na planszy. Rozmiar planszy w ka¿dej grze jest taki sam i wynosi 10x10. Domyœlna iloœæ min (je¿eli gracz nie zmieni wyboru) to 10. Maksymalna to 50.

Po podaniu imienia/pseudonimu oraz iloœci min gracz klika przycisk GRAJ!

W tym momencie sprawdzana jest obecnoœæ wprowadzonego imieni/pseudonimu gracza w bazie danych przechowuj¹cej wyniki. Je¿eli gracz wprowadzi wartoœæ ju¿ istniej¹c¹ w tabeli rezultatów wówczas zostanie poinformowany o swoim najlepszym czasie uzyskanym w poprzedniej rozgrywce przy okreslonej liczbie min. Je¿eli gracz poda imiê/pseudonim nie wystêpuj¹ce w bazie danych b¹dŸ zdecyduje siê zagraæ przy innej ni¿ poprzednio liczbie min wówczas bêdzie to zarejestrowane jako nowa rozgrywka gracza.

W trakcie rozgrywki kiedy gracz trafi na minê gra jest przerywana komunikatem. Gracz ma mo¿liwoœæ wyboru nowej gry b¹dŸ wyjœcia z programu.

Je¿eli gracz rozbroi pole pokazuje siê okno z wynikiem i aktualnym miejscem w rankigu. Wyœwietlany ranking dotyczy tylko rezultatów uzyskanych przy okreslonej liczbie min. Je¿eli gracz nie poprawi swojego wczeœniejszego wyniku jest informowany, ¿e tego nie dokona³ i w oknie z wynikami jest przedstawiany jego najlepszy wynik.