/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package IMBT;

import Sudoku.Sudoku;
import Sudoku.ICheck;
import SudoLib.ExistArray;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * <p>
 * <b>InMemoryBTCheck</b> (In Memory BackTracking Check) contient les fonctions
 * principales de résolution de sudoku suivant
 * <a href="http://fr.openclassrooms.com/informatique/cours/le-backtracking-par-l-exemple-resoudre-un-sudoku">
 * un algorithme de backtracking</a>.</p>
 *
 * <p>
 * L'algorithme suit les optimisations proposées, c'est à dire le stockage de
 * l'état initial de la grille du sudoku dans 3 tableaux de boolean indiquant
 * ainsi la présence d'un chiffre dans une ligne, une colonne ou un bloc ( voir
 * la librairie SudoLib et le tri des cases vides en fonctions du nombre de
 * chiffres concurrents qu'elles peuvent accepter.</p>
 *
 * <p>
 * Elle propose deux méthodes publiques de résolution :</p>
 * <ul><li>une méthode renvoyant une unique solution</li>
 * <li>une méthode renvoyant toutes les solutions</li></ul>
 *
 * <p>
 * En effet la méthode renvoyant plusieurs solutions peut provoquer un
 * débordement de mémoire sur des grilles proposant énormément de résolution et
 * il faudra alors se contenter de la méthode renvoyant une unique solution pour
 * ces grilles là.</p>
 *
 *
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see SudoLib.ExistArray
 * @see CoordIMBTCheck
 * @see Solutions
 * @see Sudoku#Sudoku
 *
 * @since 1.0
 */
public class InMemoryBTCheck implements ICheck {

    /**
     * <p>
     * <b>CasesLeftToTest</b> est un ArrayList permettant de stocker les cases
     * vides qui vont être ensuite traitées dans l'algorithme.<br>
     * A l'ajout d'une nouvelle case, son nombre de chiffre concurrents va être
     * determiné.</p>
     *
     * @author Pascal Luttgens
     * @version 1.0
     *
     * @see InMemoryBTCheck#solve(Sudoku.Sudoku)
     *
     * @since 1.0
     */
    private class CasesLeftToTest extends ArrayList<CoordIMBTCheck> {

        /**
         * <p>
         * Spécialisation de la méthode {@link ArrayList#add(java.lang.Object) }
         * qui détermine ainsi les valeurs possibles qu'une case peut accepter
         * avant d'ajouter celle-ci à la liste.</p>
         *
         * @param coord La case vide à tester
         *
         * @return Vrai si la case a pu être ajoutée
         *
         * @see ArrayList#add(java.lang.Object)
         * @see InMemoryBTCheck#countContenders(IMBT.CoordIMBTCheck)
         *
         * @since 1.0 *
         */
        @Override
        public boolean add(CoordIMBTCheck coord) {
            countContenders(coord);
            return super.add(coord);
        }

    }

    /**
     * Taille de l'arête d'un bloc du sudoku.
     *
     * @since 1.0
     */
    private final int size;

    /**
     * <p>
     * Structure permettant de savoir si une valeur peut être placé à une
     * certaine position en fontion de l'état actuel du sudoku.</p>
     *
     * @see "La documentation de SudoLib"
     *
     * @since 1.0
     */
    private final ExistArray existArrays;

    /**
     * <p>
     * Ensemble des solutions possibles pour le sudoku.</p>
     *
     * @see Solutions
     *
     * @since 1.0
     */
    private final Solutions solutions;

    /**
     * <p>
     * Liste des cases vides à tester pour remplir le sudoku</p>
     *
     * @see InMemoryBTCheck.CasesLeftToTest
     *
     * @since 1.0
     */
    private CasesLeftToTest unsolvedCases;

    /**
     * <p>
     * Construit un InMemoryBTCheck à partir d'un sudoku. Les tableaux de
     * booléens et l'objet de solutions sont initialisés en fonction de l'état
     * de la grille au moment de l'appel du constructeur.</p>
     *
     * @param sudoku Le sudoku à résoudre
     *
     * @throws IllegalArgumentException Si le sudoku est déjà rempli et faux.
     *
     * @see Solutions
     * @see SudoLib.ExistArray
     *
     * @since 1.0
     */
    public InMemoryBTCheck(Sudoku sudoku) throws IllegalArgumentException {
        size = sudoku.getSize();
        existArrays = new ExistArray(sudoku);
        solutions = new Solutions(sudoku);
    }

    /**
     * <p>
     * Construit un InMemoryBTCheck à partir d'un sudoku. Les tableaux de
     * booléens et l'objet de solutions sont initialisés en fonction de l'état
     * de la grille au moment de l'appel du constructeur. On précise aussi le
     * nombre de solutions à retourner, ce qui peut s'avérer utile lorsqu'il
     * existe des millions de solutions pour une grille donnée.</p>
     *
     *
     * @param sudoku      Le sudoku à résoudre
     * @param nbSolutions Le nombre de solutions à retourner
     *
     * @throws IllegalArgumentException Si le nombre de solutions désiré est
     *                                  inférieur ou égal à 0
     *
     * @see Solutions
     * @see SudoLib.ExistArray
     *
     * @since 1.0
     */
    public InMemoryBTCheck(Sudoku sudoku, int nbSolutions) throws IllegalArgumentException {
        if (nbSolutions <= 0) {
            throw new IllegalArgumentException("Ammount of solutions desired must be > 0.");
        } else if (nbSolutions > 1) {
            solutions = new Solutions(sudoku, nbSolutions);
        } else {
            solutions = new Solutions(sudoku);
        }
        size = sudoku.getSize();
        existArrays = new ExistArray(sudoku);

    }

    /**
     * <p>
     * Détermine les valeurs possibles pour une case du Sudoku.</p>
     *
     * @param coord La case du sudoku.
     *
     * @see CoordIMBTCheck#addPossibleValues(java.util.ArrayList)
     * @see SudoLib.ExistArray#countContenders(Coord2D.Coord2D)
     *
     * @since 1.0
     */
    public void countContenders(CoordIMBTCheck coord) {
        coord.addPossibleValues(existArrays.countContenders(coord));
    }

    /**
     * <p>
     * Résoud un sudoku et retourne un nombre de solutions selon la manière dont
     * l'objet à été instancié. (voir {@link #InMemoryBTCheck(Sudoku.Sudoku, int)} )
     * La méthode de résolution utilisée est un
     * <a href="http://fr.openclassrooms.com/informatique/cours/le-backtracking-par-l-exemple-resoudre-un-sudoku">
     * un algorithme de backtracking</a>.où l'état du sudoku est enregistré à
     * chaque modification afin de réduire le nombre de tests éffectués.</p>
     *
     * @param caseIterator Itérateur sur la liste de case à tester
     *
     * @see Solutions
     * @see CoordIMBTCheck#getCurrent()
     * @see CoordIMBTCheck#resetCurrent()
     *
     * @since 1.0
     */
    public void isValid(ListIterator<CoordIMBTCheck> caseIterator) {
        CoordIMBTCheck coord;

        if (solutions.hasEnoughSolutions()) {
            return;
        }

        if (!caseIterator.hasNext()) {
            solutions.addSolution();
            return;
        }

        coord = caseIterator.next();
        while (coord.getCurrent().hasNext()) {
            int i = coord.getCurrent().next();
            if (!existArrays.isInArrays(coord, i)) {
                existArrays.setBoolAt(coord, i, true);
                solutions.write(coord, i);
                isValid(caseIterator);
                existArrays.setBoolAt(coord, i, false);
            }
        }
        coord.resetCurrent();
        caseIterator.previous();
    }

    /**
     * <p>
     * Résoud un sudoku et retourne au plus une solution. La méthode de
     * résolution utilisée est un
     * <a href="http://fr.openclassrooms.com/informatique/cours/le-backtracking-par-l-exemple-resoudre-un-sudoku">
     * un algorithme de backtracking</a>.où l'état du sudoku est enregistré à
     * chaque modification afin de réduire le nombre de tests éffectués.</p>
     *
     * @param caseIterator Itérateur sur la liste de case à tester
     *
     * @return Vrai si une des valeurs possibles marche pour la configuration
     *         donnée
     *
     * @see Solutions @see CoordIMBTCheck#getCurrent() @see
     * CoordIMBTCheck#resetCurrent()
     *
     * @since 1.0
     */
    public boolean isValidUnique(ListIterator<CoordIMBTCheck> caseIterator) {
        CoordIMBTCheck coord;

        if (!caseIterator.hasNext()) {
            return true;
        }

        coord = caseIterator.next();
        while (coord.getCurrent().hasNext()) {
            int i = coord.getCurrent().next();
            if (!existArrays.isInArrays(coord, i)) {
                existArrays.setBoolAt(coord, i, true);
                if (isValidUnique(caseIterator)) {
                    solutions.write(coord, i);
                    return true;
                }
                existArrays.setBoolAt(coord, i, false);
            }
        }
        coord.resetCurrent();
        caseIterator.previous();
        return false;
    }

    /**
     * <p>
     * Fonction qui initialise la résolution et appelle la méthode proposant
     * l'algorithme adapté au choix de l'utilisateur.</p>
     *
     * <p>
     * Une liste est crée avec toutes les cases vides du sudoku et celles-ci
     * sont ensuite trié par ordre croissant en fonction de leur nombre de
     * possibilités.<br>
     * Enfin, en fonction du nombre de solutions désiré par l'utilisateur, la
     * fonction va appelé l'une des deux méthodes de résolutions de la classe et
     * retourner les solutions si elles existent ou <code>null</code> si il n'y
     * a pas de solutions.</p>
     *
     * @param sudoku Le sudoku à résoudre
     *
     * @return Les solutions si la grille en accepte ou null
     *
     * @see CasesLeftToTest
     * @see Solutions
     * @see CoordIMBTCheck#compareTo(IMBT.CoordIMBTCheck)
     * @see #isValid(java.util.ListIterator)
     * @see #isValidUnique(java.util.ListIterator)
     *
     * @since 1.0
     */
    @Override
    public ArrayList<Integer[]> solve(Sudoku sudoku) {

        unsolvedCases = new CasesLeftToTest();
        for (int i = 0; i < size * size; ++i) {
            for (int j = 0; j < size * size; ++j) {
                if (sudoku.getValueAt(j, i) == 0) {
                    unsolvedCases.add(new CoordIMBTCheck(j, i));
                }
            }
        }
        unsolvedCases.sort(null);

        LinkedList<CoordIMBTCheck> list = new LinkedList<>(unsolvedCases);
        ListIterator<CoordIMBTCheck> listIterator = list.listIterator();
        if (solutions.getNbSolutions() > 1) {
            isValid(listIterator);
            return (solutions.hasSolution()) ? solutions.getSolutions() : null;
        } else {
            return (isValidUnique(listIterator)) ? solutions.getSolution() : null;
        }
    }
}
