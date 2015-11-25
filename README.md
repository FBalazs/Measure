Az alkalmazás használata:
	
	Csúszási súrlódás mérés (Friction measurement):
		A telefont a mérendõ felületre fektetjük, majd megnyomjuk a "Start" gombot.
		Megvárjuk amíg a "state" paraméter értéke "STILL"-re változik. Ezután
		határozottan ellökjük a telefont. Amikor megáll, a "state" paraméternek
		"STOPPED" értéken kell lennie, különben nem sikerült a mérés. Ha sikerült
		a mérés, a scúszási súrlódás értékét mutatja a "mû".
	
	Tapadási súrlódás mérés (Traction measurement):
		Elõkészítjük a felületet úgy, hogy lassú tempóban dönteni tudjuk.
		A telefont a vízszintes felületre helyezzük, megnyomjuk a "Start" gombot,
		majd lassan dönteni kezdjük. Amikor a telefon megcsúszik, a gombnak inaktívvá
		kell válnia, ha ezt nem tennék manuálisan is befejezhetjük a mérést, a
		megnyomásával. A "mû_0" értéke a tapadási súrlódást mutatja a felület és a
		telefon között.

Az alkalmazás fizikai háttere:

	Mintavételezés:
		A program a telefon gyorsulás szenzorának maximális mintavételezési
		sebességét használja, ez telefononként változhaz, Samsung Galaxy Core Prime
		esetében 100 mintavétel/másodperc.
		A mért értékekben jelentõs ugrálás figyelhetõ meg, álló telefon esetében is,
		ezért mindig az elõzõ 5-10 mintavétel átlagával számol a program.
		A telefonok gyorsulás érzékelõje alapvetõen arra van tervezve, hogy a telefon
		orientációját meghatározhassuk, így a mérések elég pontatlanok lehetnek.
	
	Csúszási súrlódás mérés (Friction measurement):
		A program folyamatosan figyeli a telefon képernyõjével párhuzamos gyorsulást.
		Ez két vektor, melyeket összead és az abszolút értéküket logolja. A gomb
		megnyomásakor a "STARTED" állapotba kerül, ekkor vár fél másodpercet, az
		ez alatt mért gyorsulás értékeket átlagolja, és az ezutáni mintavételekbõl
		mindig kivonja ezt az értéket.
		Amikor ellökjük a telefont, a gyorsulás függvényben egy nagy ugrás jön létre,
		hiszen felgyorsítjuk a készüléket, ezt az ugrást érzékeli a telefon, és "PUSH"
		értékre vált az állapot. Ebben az állapotban annyira vár, hogy a gyorsulás
		abszolút értéke ismét megközelítse a 0-t, hiszen a lökés után a telefon lassulni,
		azaz az ellenkezõ irányba fog gyorsulni, mint addig. Ekkor az állapot "SLIDESTART"
		lesz, amiben arra vár a telefon, hogy a gyorsulás ismét felvegyen valami 0-nál
		nagyobb értéket, amikor is átvált az "SLIDE" állapotra és amíg a gyorsulás újra 0
		(közeli) nem lesz, átlagolja a mért értékeket. Amikor megáll a telefon, a mérés
		befejezõdik, a "mû" értéke pedig az (átlagos lassulás)/gravitáció lesz.
		
	Tapadási súrlódás mérés (Traction measurement):
		A program a képernyõre merõleges gyorsulásból folyamatosan számolja, a vízszintessel
		bezárt szöget és a képernyõvel párhuzamos mért gyorsulásokból kivonja gravitáció
		megfelelõ komponensét. Így a képernyõvel párhuzamosan meg tudja állapítani, amikor
		megcsúszik a telefon. Amikor ez megtörténik, "mû_0" értéke a jelenlegi szög tangense lesz.