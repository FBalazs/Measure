Az alkalmaz�s haszn�lata:
	
	Cs�sz�si s�rl�d�s m�r�s (Friction measurement):
		A telefont a m�rend� fel�letre fektetj�k, majd megnyomjuk a "Start" gombot.
		Megv�rjuk am�g a "state" param�ter �rt�ke "STILL"-re v�ltozik. Ezut�n
		hat�rozottan ell�kj�k a telefont. Amikor meg�ll, a "state" param�ternek
		"STOPPED" �rt�ken kell lennie, k�l�nben nem siker�lt a m�r�s. Ha siker�lt
		a m�r�s, a sc�sz�si s�rl�d�s �rt�k�t mutatja a "m�".
	
	Tapad�si s�rl�d�s m�r�s (Traction measurement):
		El�k�sz�tj�k a fel�letet �gy, hogy lass� temp�ban d�nteni tudjuk.
		A telefont a v�zszintes fel�letre helyezz�k, megnyomjuk a "Start" gombot,
		majd lassan d�nteni kezdj�k. Amikor a telefon megcs�szik, a gombnak inakt�vv�
		kell v�lnia, ha ezt nem tenn�k manu�lisan is befejezhetj�k a m�r�st, a
		megnyom�s�val. A "m�_0" �rt�ke a tapad�si s�rl�d�st mutatja a fel�let �s a
		telefon k�z�tt.

Az alkalmaz�s fizikai h�ttere:

	Mintav�telez�s:
		A program a telefon gyorsul�s szenzor�nak maxim�lis mintav�telez�si
		sebess�g�t haszn�lja, ez telefononk�nt v�ltozhaz, Samsung Galaxy Core Prime
		eset�ben 100 mintav�tel/m�sodperc.
		A m�rt �rt�kekben jelent�s ugr�l�s figyelhet� meg, �ll� telefon eset�ben is,
		ez�rt mindig az el�z� 5-10 mintav�tel �tlag�val sz�mol a program.
		A telefonok gyorsul�s �rz�kel�je alapvet�en arra van tervezve, hogy a telefon
		orient�ci�j�t meghat�rozhassuk, �gy a m�r�sek el�g pontatlanok lehetnek.
	
	Cs�sz�si s�rl�d�s m�r�s (Friction measurement):
		A program folyamatosan figyeli a telefon k�perny�j�vel p�rhuzamos gyorsul�st.
		Ez k�t vektor, melyeket �sszead �s az abszol�t �rt�k�ket logolja. A gomb
		megnyom�sakor a "STARTED" �llapotba ker�l, ekkor v�r f�l m�sodpercet, az
		ez alatt m�rt gyorsul�s �rt�keket �tlagolja, �s az ezut�ni mintav�telekb�l
		mindig kivonja ezt az �rt�ket.
		Amikor ell�kj�k a telefont, a gyorsul�s f�ggv�nyben egy nagy ugr�s j�n l�tre,
		hiszen felgyors�tjuk a k�sz�l�ket, ezt az ugr�st �rz�keli a telefon, �s "PUSH"
		�rt�kre v�lt az �llapot. Ebben az �llapotban annyira v�r, hogy a gyorsul�s
		abszol�t �rt�ke ism�t megk�zel�tse a 0-t, hiszen a l�k�s ut�n a telefon lassulni,
		azaz az ellenkez� ir�nyba fog gyorsulni, mint addig. Ekkor az �llapot "SLIDESTART"
		lesz, amiben arra v�r a telefon, hogy a gyorsul�s ism�t felvegyen valami 0-n�l
		nagyobb �rt�ket, amikor is �tv�lt az "SLIDE" �llapotra �s am�g a gyorsul�s �jra 0
		(k�zeli) nem lesz, �tlagolja a m�rt �rt�keket. Amikor meg�ll a telefon, a m�r�s
		befejez�dik, a "m�" �rt�ke pedig az (�tlagos lassul�s)/gravit�ci� lesz.
		
	Tapad�si s�rl�d�s m�r�s (Traction measurement):
		A program a k�perny�re mer�leges gyorsul�sb�l folyamatosan sz�molja, a v�zszintessel
		bez�rt sz�get �s a k�perny�vel p�rhuzamos m�rt gyorsul�sokb�l kivonja gravit�ci�
		megfelel� komponens�t. �gy a k�perny�vel p�rhuzamosan meg tudja �llap�tani, amikor
		megcs�szik a telefon. Amikor ez megt�rt�nik, "m�_0" �rt�ke a jelenlegi sz�g tangense lesz.