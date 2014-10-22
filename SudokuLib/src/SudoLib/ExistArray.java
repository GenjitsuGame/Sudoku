/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package SudoLib;

import Coord2D.Coord2D;
import java.util.ArrayList;

/**
 * <p>
 * <code><b>ExistArray</b></code> est la classe permettant de garder en mémoire
 * l'état du sudoku de manière à optimiser les tests lorsqu'il s'agit de
 * rajouter une nouvelle valeur sur la grille.</p>
 *
 * <p>
 * ExistArray est composé de 3 tableaux représentants :</p>
 * <ul>
 * <li>les lignes</li>
 * <li>les colonnes</li>
 * <li>les blocs</li>
 * </ul>
 * <p>
 * et chaque tableau indique si une valeur est présentes sur un des blocs,
 * lignes ou colonnes du sudoku.</p>
 *
 * <p>
 * <b>Pour vérifier si un chiffre peut être ajouté, le nombre de test sera
 * toujours 3.</b></p>
 *
 * @author Luttgens Pascal
 * @version 1.0
 * @since 1.0
 */
public class ExistArray {

    /**
     * Taille de l'arête d'un bloc de la grille du sudoku.
     *
     * @see #ExistArray(SudoLib.ISudoku)
     *
     * @since 1.0
     */
    private final int _size;

    /**
     * <p>
     * Tableau réprésentant la grille du sudoku qui indique si un chiffre est
     * présent sur une ligne du sudoku.<p>
     *
     * <p>
     * Par exemple, pour un sudoku de taille 9x9 :
     * <ul>
     * <li>un <code><b>true</b></code> à la première case du tableau indique
     * qu'il y a un 1 sur la première ligne du Sudoku</li>
     * <li>un <code><b>false</b></code> à la dernière case du tableau indique
     * qu'il n'y a pas de 9 sur la dernière ligne du Sudoku</li></ul>
     *
     * @see #ExistArray(SudoLib.ISudoku)
     * @see #existsOnCol
     * @see #existsOnBloc
     *
     * @since 1.0
     */
    private final boolean[] existsOnRow;

    /**
     * <p>
     * Tableau réprésentant la grille du sudoku qui indique si un chiffre est
     * présent sur une colonne du sudoku.</p>
     *
     *
     * @see #ExistArray(SudoLib.ISudoku)
     * @see #existsOnRow
     * @see #existsOnBloc
     *
     * @since 1.0
     */
    private final boolean[] existsOnCol;

    /**
     * <p>
     * Tableau réprésentant la grille du sudoku qui indique si un chiffre est
     * présent dans un bloc du sudoku.</p>
     *
     * @see #ExistArray(SudoLib.ISudoku)
     * @see #existsOnRow
     * @see #existsOnCol
     *
     * @since 1.0
     */
    private final boolean[] existsOnBloc;

    /**
     * <p>
     * Construit un nouvel <b>ExistArray</b> à partir d'un sudoku.</p>
     *
     * <p>
     * Les tableaux sont initialisés en fonction de l'état du sudoku au moment
     * de l'appel de la fonction.</p>
     *
     * @param sudoku Le sudoku contenant la grille
     *
     * @throws IllegalArgumentException Si la grille est remplie est invalide.
     *
     * @see #existsOnBloc
     * @see #existsOnCol
     * @see #existsOnRow
     * @see ISudoku#getValueAt(int, int)
     * @see ISudoku#getNbInitializedValue()
     * @see ISudoku#getSize()
     *
     * @since 1.0
     */
    public ExistArray(ISudoku sudoku) throws IllegalArgumentException {
        _size = sudoku.getSize();
        existsOnRow = new boolean[_size * _size * _size * _size];
        existsOnCol = new boolean[_size * _size * _size * _size];
        existsOnBloc = new boolean[_size * _size * _size * _size];

        /*
         * Initialisation des tableaux de booléen en fonction de la grille.
         */
        for (int i = 0; i < _size * _size; ++i) {
            for (int j = 0; j < _size * _size; ++j) {
                int k;
                if ((k = sudoku.getValueAt(j, i)) != 0) {
                    existsOnRow[new Coord2D(k - 1, i).convCoord(_size * _size)]
                            = existsOnCol[new Coord2D(k - 1, j).convCoord(_size * _size)]
                            = existsOnBloc[new Coord2D(k - 1,
                                    _size * (i / _size) + (j / _size)).convCoord(_size * _size)] = true;
                }
            }
        }

        /*
         * Si la grille est pleine, on vérifie si elle est valide.
         */
        if (sudoku.getNbInitializedValue() == _size * _size * _size * _size) {
            for (boolean b : existsOnRow) {
                if (b == false) {
                    throw new IllegalArgumentException("Grid is invalid and full.");
                }
            }
            for (boolean b : existsOnCol) {
                if (b == false) {
                    throw new IllegalArgumentException("Grid is invalid and full.");
                }
            }
            for (boolean b : existsOnBloc) {
                if (b == false) {
                    throw new IllegalArgumentException("Grid is invalid and full.");
                }
            }
        }
    }

    /**
     * <p>
     * Modifie la valeur d'un booléen dans les <b>ExistArrays</b>.</p>
     *
     * @param coord Coordonnées de la case
     * @param value Valeur à ajouter/retirer
     * @param bool  Vrai si on ajoute, Faux si on retire
     */
    public void setBoolAt(Coord2D coord, int value, boolean bool) {
        existsOnRow[new Coord2D(value, coord.getY()).convCoord(_size * _size)] = bool;
        existsOnCol[new Coord2D(value, coord.getX()).convCoord(_size * _size)] = bool;
        existsOnBloc[new Coord2D(value, _size * (coord.getY() / _size) + (coord.getX() / _size)).convCoord(_size * _size)] = bool;
    }

    /**
     * <p>
     * Indique si une valeur peut être placée dans une certaine case en
     * vérifiant si celle-ci n'est pas déjà présente sur :
     * <ul>
     * <li>la ligne de la case</li>
     * <li>la colonne de la case</li>
     * <li>le bloc de la case</li>
     * </ul>
     *
     * @param coord Coordonnées de la case sur la grille
     * @param value Valeur à tester
     *
     * @return Vrai si la valeur est déjà présente sur la ligne, la colonne ou
     *         le bloc
     *
     * @see #existsOnRow
     * @see #existsOnCol
     * @see #existsOnBloc
     *
     * @since 1.0
     */
    public boolean isInArrays(Coord2D coord, int value) {
        return existsOnRow[new Coord2D(value, coord.getY()).convCoord(_size * _size)]
                || existsOnCol[new Coord2D(value, coord.getX()).convCoord(_size * _size)]
                || existsOnBloc[new Coord2D(value, _size * (coord.getY() / _size) + (coord.getX() / _size)).convCoord(_size * _size)];
    }

    /**
     * <p>
     * Détermine la liste des nombres qu'une case peut accepter en fonction de
     * l'état actuel du sudoku.</p>
     *
     * @param coord La case du sudoku
     *
     * @return la liste des valeurs possibles pour la case
     *
     * @see #isInArrays(Coord2D.Coord2D, int)
     *
     * @since 1.0
     */
    public ArrayList<Integer> countContenders(Coord2D coord) {
        ArrayList<Integer> contenders = new ArrayList<>();
        for (int i = 0; i < _size * _size; ++i) {
            if (!isInArrays(coord, i)) {
                contenders.add(i);
            }
        }
        return contenders;
    }

}
