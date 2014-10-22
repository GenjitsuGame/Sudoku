/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package IMBT;

import java.util.ArrayList;
import Sudoku.Sudoku;

/**
 * <p>
 * <b><code>Solutions</code></b> permet de gérer l'ensemble des solutions d'une
 * grille de sudoku. Les méthodes proposées par la classe permette de gérér
 * facilement le cas où une grille admet plusieurs solutions</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @since 1.0
 */
public class Solutions {

    /**
     * <p>
     * Taille de l'arête d'un bloc du sudoku</p>
     *
     * @since 1.0
     */
    private final int SIZE;

    /**
     * <p>
     * Nombre de solutions maximum devant être stockées.</p>
     *
     * @since 1.0
     */
    private final int NB_SOLUTIONS;

    /**
     * <p>
     * Ensemble des solutions du sudoku.</p>
     *
     * @since 1.0
     */
    private final ArrayList<Integer[]> solutions;

    /**
     * <p>
     * Instancie un objet <b><code>Solution</code></b> à partir d'un sudoku. La
     * grille du sudoku est recopiée et la première grille de solution
     * s'obtiendra à partir d'elle.</p>
     *
     * @param sudoku Le sudoku à résoudre
     *
     * @see CoordIMBTCheck
     *
     * @since 1.0
     */
    public Solutions(Sudoku sudoku) {
        SIZE = sudoku.getSize();
        NB_SOLUTIONS = 1;
        Integer[] _grid;
        _grid = new Integer[SIZE * SIZE * SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE; ++i) {
            for (int j = 0; j < SIZE * SIZE; ++j) {
                _grid[new CoordIMBTCheck(j, i).convCoord(SIZE * SIZE)] = sudoku.getValueAt(j, i);
            }
        }
        solutions = new ArrayList<>();
        solutions.add(_grid);
    }

    /**
     * <p>
     * Instancie un objet <b><code>Solution</code></b> à partir d'un sudoku et
     * d'un nombre de solutions maximum à retourner. La grille du sudoku est
     * recopiée et la première grille de solution s'obtiendra à partir
     * d'elle.</p>
     *
     * @param sudoku      Le sudoku à résoudre
     * @param nbSolutions Le nombre de solutions maximum désiré
     *
     * @see CoordIMBTCheck
     *
     * @since 1.0
     */
    public Solutions(Sudoku sudoku, int nbSolutions) {
        SIZE = sudoku.getSize();
        NB_SOLUTIONS = nbSolutions;
        Integer[] _grid;
        _grid = new Integer[SIZE * SIZE * SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE; ++i) {
            for (int j = 0; j < SIZE * SIZE; ++j) {
                _grid[new CoordIMBTCheck(j, i).convCoord(SIZE * SIZE)] = sudoku.getValueAt(j, i);
            }
        }
        solutions = new ArrayList<>();
        solutions.add(_grid);
    }

    /**
     * <p>
     * Ecrit une une valeur dans la grille de solution à la position donnée.</p>
     *
     * @param coord La case où on écrit la valeur
     * @param value La valeur à incrémenter de 1 avant de l'ajouter
     *
     * @see CoordIMBTCheck
     *
     * @since 1.0
     */
    public void write(CoordIMBTCheck coord, int value) {
        solutions.get(solutions.size() - 1)[coord.convCoord(SIZE * SIZE)] = value + 1;
    }

    /**
     * <p>
     * Indique si l'ensemble des solutions contient au moins 1 grille</p>
     *
     * @return Vrai si le sudoku renvoie au moins une solutions
     *
     * @since 1.0
     */
    public boolean hasSolution() {
        return solutions.size() > 1;
    }

    /**
     * <p>
     * Retourne le nombre de solutions maximum à stocker</p>
     *
     * @return Le nombre de solutions maximum à stocker
     *
     * @since 1.0
     */
    public int getNbSolutions() {
        return NB_SOLUTIONS;
    }

    /**
     * <p>
     * Indique si assez de solutions ont été trouvées</p>
     *
     * @see #NB_SOLUTIONS
     *
     * @return Vrai si le nombre de solutions trouvées est supérieur au nombre
     *         de solutions désirées
     */
    public boolean hasEnoughSolutions() {
        return NB_SOLUTIONS < solutions.size();
    }

    /**
     * <p>
     * Retourne l'ensemble des solutions du sudoku</p>
     *
     * @return La liste des solutions du sudoku
     *
     * @since 1.0
     */
    public ArrayList<Integer[]> getSolutions() {
        solutions.remove(solutions.size() - 1);
        return solutions;
    }

    /**
     * <p>
     * Retourne la première solution de l'ensemble des solutions. Cette méthode
     * devrait être utilisée pour</p>
     *
     * @return La première solution de la liste
     *
     * @since 1.0
     */
    public ArrayList<Integer[]> getSolution() {
        return solutions;
    }

    /**
     * <p>
     * Ajoute une nouvelle solution à l'ensemble des solutions à partir de la
     * dernière solution trouvée.</p>
     *
     * @since 1.0
     */
    public void addSolution() {
        solutions.add(solutions.get(solutions.size() - 1).clone());
    }

}
