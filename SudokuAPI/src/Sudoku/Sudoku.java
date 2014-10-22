/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package Sudoku;

import Coord2D.Coord2D;
import IMBT.Shaker;
import IMBT.InMemoryBTCheck;
import IMBT.Solutions;
import SudoLib.ISudoku;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * <b><code>Sudoku</code></b> est l'implémentation de {@link SudoLib.ISudoku},
 * la rendant donc compatible avec la librairie {@link SudoLib}.</p>
 *
 * <p>
 * La classe <b><code>Sudoku</code></b> permet de générer, de résoudre des
 * grilles de sudoku et d'afficher les solutions. La génération et la résoltion
 * doivent être supportées par des classes qui implémentent respectvement les
 * interfaces {@link IGen} et {@link ICheck}.</p>
 *
 * <p>
 * Les tailles sont mesurées en cases par arête de bloc.</p>
 *
 * <p>
 * Les grilles de sudoku peuvent être passé en paramètre à l'appel du
 * constructeur.</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see SudoLib.ISudoku
 * @see ICheck
 * @see IGen
 *
 * @since 1.0
 */
public class Sudoku implements ISudoku {

    /**
     * <p>
     * Taille minimum de l'arête d'un bloc.</p>
     *
     * @see #DEFAULT_MAX_SIZE
     *
     * @since 1.0
     */
    private final static int DEFAULT_MIN_SIZE = 3;

    /**
     * <p>
     * Taille maximum de l'arête d'un bloc.</p>
     *
     * @see #DEFAULT_MIN_SIZE
     *
     * @since 1.0
     */
    private final static int DEFAULT_MAX_SIZE = 10;

    /**
     * <p>
     * Taille de l'arête d'un bloc du sudoku courant. Doit être comprise entre
     * {@link #DEFAULT_MIN_SIZE} et {@link #DEFAULT_MAX_SIZE}.
     *
     * @see #DEFAULT_MIN_SIZE
     * @see #DEFAULT_MAX_SIZE
     *
     * @since 1.0
     */
    public final int SIZE;

    /**
     * <p>
     * Nombre de valeur devant être initialisées à la génération du sudoku.</p>
     *
     * @see #Sudoku(int)
     * @see #Sudoku(java.util.ArrayList, int)
     * @see #Sudoku(int, int)
     *
     * @since 1.0
     */
    private final int NB_INITIALIZED_VALUES;

    /**
     * <p>
     * Grille du sudoku. Il est important de noter que la grille est stockée
     * dans un tableau 1D, ce qui peut entrainer des conversion de coordonnées
     * si les autres classes ou méthodes ayant besoin de
     * <b><code>_grid</code><b> utilisent des tableaux 2D ou autres.</p>
     *
     * @see Coord2D#convCoord(int)
     * @see Coord2D#convCoord(int, int)
     *
     * @since 1.0
     */
    private Integer[] _grid;

    /**
     * <p>
     * Ensemble des solutions de la grille du sudoku. Si le sudoku ne possède
     * aucune solutions, cet attribut peut être soit nul, soit vide.</p>
     *
     * @since 1.0
     */
    private ArrayList<Integer[]> solutions;

    /**
     * <p>
     * Générateur du sudoku implémentant l'interface {@link ICheck}.</p>
     *
     * @see ICheck
     *
     * @since 1.0
     */
    private ICheck _check;

    /**
     * <p>
     * Résolveur du sudoku implémentant l'interface {@link ISudoku}.</p>
     *
     * @see IGen
     *
     * @since 1.0
     */
    private IGen _gen;

    /**
     * <p>
     * Génère une grille de sudoku avec le nombre de valeur spécifié en
     * paramètre.</p>
     *
     * @param n Le nombre de valeur initialisées
     *
     * @throws IllegalStateException    Si le sudoku est mal initalisé
     * @throws IllegalArgumentException Si le nombre de valeur à générer est
     *                                  invalide
     * @throws RuntimeException         Si il y'a une erreur lors de la
     *                                  génération de la grille
     *
     * @see Shaker
     * @see #DEFAULT_MAX_SIZE
     * @see #DEFAULT_MIN_SIZE
     *
     * @since 1.0
     */
    public Sudoku(int n) throws IllegalStateException, IllegalArgumentException, RuntimeException {
        if (DEFAULT_MAX_SIZE < DEFAULT_MIN_SIZE) {
            throw new IllegalStateException("DEFAULT_MIN_SIZE must be inferior to DEFAULT_MAX_SIZE");
        }

        if (DEFAULT_MIN_SIZE <= 0) {
            throw new IllegalStateException("DEFAULT_MIN_SIZE must be strictly superior to 0");
        }

        if (n > (DEFAULT_MIN_SIZE * DEFAULT_MIN_SIZE) * (DEFAULT_MIN_SIZE * DEFAULT_MIN_SIZE)) {
            throw new IllegalArgumentException("Number of solved cases must be inferior to the ammount of cases in the grid");
        }

        SIZE = DEFAULT_MIN_SIZE;
        NB_INITIALIZED_VALUES = n;
        _grid = new Integer[SIZE * SIZE * SIZE * SIZE];
        try {
            generate(n);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * Initialise un sudoku à partir d'une grille et de la taille de l'arête
     * d'un bloc de la grille</p>
     *
     * @param grid La grille
     * @param size La taille de l'arête d'un bloc
     *
     * @throws IllegalArgumentException Si la taille est invalide ou si la
     *                                  nombre de chiffre dans la grille ne
     *                                  correspond pas à la taille donnée
     *
     * @since 1.0
     */
    public Sudoku(ArrayList<Integer> grid, int size) throws IllegalStateException, IllegalArgumentException {
        if (DEFAULT_MAX_SIZE < DEFAULT_MIN_SIZE) {
            throw new IllegalStateException("DEFAULT_MIN_SIZE must be inferior to DEFAULT_MAX_SIZE");
        }

        if (DEFAULT_MIN_SIZE <= 0) {
            throw new IllegalStateException("DEFAULT_MIN_SIZE must be strictly superior to 0");
        }

        if (size > DEFAULT_MAX_SIZE) {
            throw new IllegalArgumentException("The size given in the second argument must be lower than DEFAULT_MAX_SIZE");
        }

        if (size < DEFAULT_MIN_SIZE) {
            throw new IllegalArgumentException("The size given in the second argument must be higher than DEFAULT_MIN_SIZE");
        }

        if (grid.size() > (size * size) * (size * size)) {
            throw new IllegalArgumentException("Number of cases given must be inferior to the ammount of cases in the grid");
        }

        SIZE = size;
        int count = 0;
        for (Integer i : grid) {
            if (i != 0) {
                count += 1;
            }
        }
        NB_INITIALIZED_VALUES = count;
        Object[] array = grid.toArray();
        _grid = Arrays.copyOf(array, array.length, Integer[].class);
    }

    /**
     * <p>
     * Spécifie la méthode de résolution à utiliser pour le sudoku.</p>
     *
     * @param check Un classe de résolution implémentant {@link ICheck}
     *
     * @see ICheck
     *
     * @since 1.0
     */
    public void setCheck(ICheck check) {
        _check = check;
    }

    /**
     * <p>
     * Retourne la taille de l'arête d'un bloc du sudoku</p>
     *
     * @return La taille de l'arête d'un bloc du sudoku
     *
     * @since 1.0
     */
    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * <p>
     * Retourne le nombre de valeur initialisées dans le sudoku.</p>
     *
     * @return Le nombre de valeur initialisées
     *
     * @since 1.0
     */
    @Override
    public int getNbInitializedValue() {
        return NB_INITIALIZED_VALUES;
    }

    /**
     * <p>
     * Retourne une valeur de la grille à une case donnée.</p>
     *
     * @param x Coordonnée de la case en abscisse
     * @param y Coordonnée de la case en ordonnée
     *
     * @return La valeur de la case, 0 si vide.
     *
     * @throws IllegalArgumentException Si les coordonnées sont invalides
     *
     * @see Coord2D
     *
     * @since 1.0
     */
    @Override
    public int getValueAt(int x, int y) throws IllegalArgumentException {
        if (x >= SIZE * SIZE || y >= SIZE * SIZE || x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid coordinates.\n"
                    + "Coordinates must be between 0 and " + (SIZE * SIZE - 1));
        }

        return _grid[new Coord2D(x, y).convCoord(SIZE * SIZE)];
    }

    /**
     * <p>
     * Génère une grille de sudoku en spécifiant le nomre de valeurs à
     * initialiser.</p>
     *
     * @param solvedCases Le nombre de valeur à initialiser
     *
     * @throws IllegalArgumentException Si le nombre de cases à résoudre est
     *                                  invalide
     * @throws IllegalStateException    Si le sudoku n'a pas été correctement
     *                                  initialisé
     *
     * @see Shaker
     *
     * @since 1.0
     */
    @Override
    public final void generate(int solvedCases) throws IllegalArgumentException, IllegalStateException {
        if ((solvedCases > SIZE * SIZE * SIZE * SIZE) || solvedCases <= 0) {
            throw new IllegalArgumentException("solvedCases must be between 1 and the ammount of sudoku cases.");
        }

        if (_gen == null) {
            try {
                _gen = new Shaker(SIZE);
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("Sudoku was not properly initialized", ex);
            }
        }
        _grid = _gen.generate(solvedCases);
    }

    /**
     * <p>
     * Résoud la grille stockée dans le sudoku.</p>
     *
     * @throws IllegalStateException Si le sudoku n'a pas été correctement
     *                               initalisé. (i.e, la grille est invalide).
     */
    @Override
    public void solve() throws IllegalStateException {
        if (_check == null) {
            try {
                _check = new InMemoryBTCheck(this);
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("Sudoku was not properly initialized", ex);
            }
        }
        if (_grid == null) {
            throw new IllegalStateException("No grid found to resolve");
        }

        if ((solutions = _check.solve(this)) == null) {
            throw new IllegalStateException("The grid submitted admit no solutions.");
        }

    }

    /**
     * <p>
     * Affiche l'ensemble des solutions de la grille si elle a été résolue ou
     * affiche la grille à résoudre.</p>
     *
     * @return La grille à résoudre ou ses solutions
     * 
     * @see InMemoryBTCheck#solve(Sudoku.Sudoku) 
     * 
     * @since 1.0
     */
    @Override
    public String toString() {
        String s = new String();

        if (solutions == null) {
            for (int i = 0; i < SIZE * SIZE * SIZE * SIZE; ++i) {
                if ((i % 9 == 0) && (i != 0)) {
                    s += "\n";
                }
                s += _grid[i] + " ";
            }
            s += "\n\n\n";
        } else {
            for (Integer[] array : solutions) {
                for (int i = 0; i < SIZE * SIZE * SIZE * SIZE; ++i) {
                    if (i % 9 == 0) {
                        s += "\n";
                    }
                    s += array[i] + " ";
                }
                s += "\n\n\n";
            }
        }
        return s;
    }
}
