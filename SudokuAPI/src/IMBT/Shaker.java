/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package IMBT;

import Sudoku.IGen;
import Sudoku.Sudoku;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * <p>
 * <b><code>Shaker</code></b> est un générateur de grille "valide" de sudoku.
 * Par valide, on entend que la grille admet au moins une solution.</p>
 *
 * <p>
 * L'algorithme de génération a été inspiré par
 * <a href="http://www.mathspace.com/comap/Training_Materials/Team2975_ProblemB.pdf">
 * cet algorithme</a>.</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 * 
 * @see Bloc
 * @see Matrix
 *
 * @since 1.0
 */
public class Shaker implements IGen {

    /**
     * <p>
     * Taille de l'arête d'un bloc du sudoku</p>
     */
    private final int _size;

    /**
     * <p>
     * Grille générée par le <b>Shaker<b>.</p>
     */
    private Integer[] _grid;

    /**
     * <p>
     * Construit un <b>Shaker</b> à partir de la taille de l'arête d'un bloc
     * d'un sudoku.</p>
     *
     * <p>
     * Un shaker est une grille générée à partir d'un bloc considéré comme une
     * matrice et de produits matriciels. Cependant les nouveaux blocs obtenus
     * se ressemblent fortement et ne devraient pas être utilisé tels quels dans
     * une grille de sudoku.</p>
     *
     * @param size La taille de l'arête du bloc d'un sudoku
     *
     * @see Bloc
     * @see Matrix
     *
     * @since 1.0
     */
    public Shaker(int size) {
        _size = size;
        Bloc b = new Bloc();
        _grid = new Integer[_size * _size * _size * _size];
        ArrayList<Bloc> gridBloc = b.generateGrid();
        ArrayList<Integer> gridInt = new ArrayList<>();
        for (int i = 0; i < _size; ++i) {
            for (int j = 0; j < _size; ++j) {
                for (int k = 0; k < _size; ++k) {
                    for (int l = 0; l < _size; ++l) {
                        gridInt.add(gridBloc.get(i * _size + k).getValueAt(j * _size + l));
                    }
                }
            }
        }
        for (int i = 0; i < _size * _size * _size * _size; ++i) {
            _grid[i] = gridInt.get(i);
        }
    }

    /*
     * Les deux fonctions suivantes permettent d'échanger des lignes et colonnes
     * entières malgré le fait qu'il n'y ait besoin de changer uniquement les
     * chiffres au sein d'un seul bloc, le sudoku étant formé selon le shéma
     * suivant :
     *
     * XOO OOX OXO
     *
     * Ce qui veut dire que l'on effectue un changement sur 2 * 9 cases alors
     * que l'on doit réellement en échanger 2 * 3 tout au plus.
     */
    
    /**
     * <p>
     * Echange deux lignes du sudoku aléatoirement à condition que celles-ci se
     * trouvent dans le même bloc.</p>
     *
     * <p>
     * Cela veut dire que la première ligne ne peut que être échangée avec la
     * deuxième et la troisième.</p>
     *
     * @since 1.0
     */
    private void swap_row() {
        Integer[] rowTmp = new Integer[_size * _size];
        Random rand = new Random();
        int row = rand.nextInt(_size * _size);
        for (int i = 0; i < _size * _size; ++i) {
            rowTmp[i] = _grid[row * _size * _size + i];
        }
        Random rand2 = new Random();
        int row2;
        if (row % _size == 0) {
            row2 = row + ((rand2.nextInt(2) == 0) ? 1 : 2);
        } else if (row % _size == 1) {
            row2 = row + ((rand2.nextInt(2) == 0) ? 1 : -1);
        } else {
            row2 = row + ((rand2.nextInt(2) == 0) ? -1 : -2);
        }
        for (int i = 0; i < _size * _size; ++i) {
            _grid[row * _size * _size + i] = _grid[row2 * _size * _size + i];
        }
        for (int i = 0; i < _size * _size; ++i) {
            _grid[row2 * _size * _size + i] = rowTmp[i];
        }
    }

    /**
     * <p>
     * Echange deux colonnes du sudoku aléatoirement à condition que celles-ci
     * se trouvent dans le même bloc.</p>
     *
     * <p>
     * Cela veut dire que la première colonne ne peut que être échangée avec la
     * deuxième et la troisième.</p>
     *
     * @since 1.0
     */
    private void swap_col() {
        Integer[] colTmp = new Integer[_size * _size];
        Random rand = new Random();
        int col = rand.nextInt(_size * _size);
        for (int i = 0; i < _size * _size; ++i) {
            colTmp[i] = _grid[col + _size * _size * i];
        }
        Random rand2 = new Random();
        int col2;
        if (col % _size == 0) {
            col2 = col + ((rand2.nextInt(2) == 0) ? 1 : 2);
        } else if (col % _size == 1) {
            col2 = col + ((rand2.nextInt(2) == 0) ? 1 : -1);
        } else {
            col2 = col + ((rand2.nextInt(2) == 0) ? -1 : -2);
        }
        for (int i = 0; i < _size * _size; ++i) {
            _grid[col + _size * _size * i] = _grid[col2 + _size * _size * i];
        }
        for (int i = 0; i < _size * _size; ++i) {
            _grid[col2 + _size * _size * i] = colTmp[i];
        }
    }

    /**
     * <p>
     * Echange aléatoirement les lignes ou les colonnes du sudoku à conditions
     * qu'elles soient sur le même bloc puis résoud le sudoku avec l'algorithme
     * de résolution pour générer une grille entière valide.</p>
     *
     * @throws IllegalStateException Si la base du sudoku générée au préalable
     *                               n'est pas valide
     *
     * @see #swap_col()
     * @see #swap_row()
     * @see InMemoryBTCheck
     *
     * @since 1.0
     */
    private void shake() throws IllegalStateException {
        int nbOperations;
        Random rand = new Random();
        nbOperations = rand.nextInt(100) + 20;
        for (int i = 0; i < nbOperations; ++i) {
            if (rand.nextInt(2) == 0) {
                swap_row();
            } else {
                swap_col();
            }
        }

        InMemoryBTCheck shakeMore;
        try {
            shakeMore = new InMemoryBTCheck(new Sudoku(new ArrayList<>(Arrays.asList(_grid)), _size));
            _grid = shakeMore.solve(new Sudoku(new ArrayList<>(Arrays.asList(_grid)), _size)).get(0);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Error while loading a new grid", e);
        }
    }

    /**
     * <p>
     * Supprime aléatoirement le nombre de cases désiré de la grille.</p>
     *
     * @param solvedCase Le nombre de case à laisser remplies
     *
     * @since 1.0
     */
    private void removeCases(int solvedCase) {
        ArrayList<Integer> tested = new ArrayList<>();
        Random rand = new Random();
        int i;
        for (int j = 0; j < _size * _size * _size * _size - solvedCase; ++j) {
            while (tested.contains(i = rand.nextInt(_size * _size * _size * _size)));
            _grid[i] = 0;
            tested.add(i);
        }
    }

    /**
     * <p>
     * Génère une grille de sudoku valide.</p>
     *
     * @param solvedCases Le nombre de cases à laisser remplies
     *
     * @return La grille générée
     *
     * @throws IllegalStateException Si la base du sudoku générée au préalable
     *                               n'est pas valide
     *
     * @see #shake()
     * @see #removeCases(int)
     *
     * @since 1.0
     */
    @Override
    public Integer[] generate(int solvedCases) throws IllegalStateException {

        shake();
        removeCases(solvedCases);
        return _grid;
    }

    /**
     * <p>
     * Affiche la grille générée par le <b><code>Shaker</code></b>.</p>
     *
     * @return La grille générée par le <b><code>Shaker</code></b>
     */
    @Override
    public String toString() {
        String s = new String();
        for (int i = 0; i < _size * _size * _size * _size; ++i) {
            if (i % 9 == 0) {
                s += "\n";
            }
            s += _grid[i] + " ";
        }
        s += "\n\n\n";
        return s;
    }

}
