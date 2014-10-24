Bienvenue dans le manuel de SudokuAPI :

Il est possible de generer une grille et de la resoudre en recuperant
la premiere solution trouve instantanement en ne tapant aucun 
parametre.

    java -jar SudokuAPI.jar

Commandes : 

-G n
generate n

    Genere une grille de sudoku valide
		
        n : le nombre de valeurs a initaliser.
		

-S r [n]
solve r [n]

    Resoud une grille de sudoku en precisant la methode de resolution 
    et le nombre de solution a afficher.
	
        r : imbt est la seule methode de resolution supportee pour 
            l'instant
        n : le nombre de solutions désire, si laisse vide, ne revoie 
            qu'une solution
		
-h
help

    Affiche le manuel d'aide.


Codes d'erreur :

    1 : Erreur de paramètres d'execution
    2 : Erreur lors du deroulement du programme
    3 : Erreur au niveau des entrees/sorties