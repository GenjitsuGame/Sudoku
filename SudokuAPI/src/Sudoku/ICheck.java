/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package Sudoku;

import java.util.ArrayList;

/**
 * <p>
 * <b><code>ICheck</code></b> définit le contrat avec les classes contenant les
 * méthodes de résolution pour résoudre un {@link Sudoku}.</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see Sudoku
 *
 * @since 1.0
 */
public interface ICheck {

    /**
     * <p>
     * Résoud une grille de sudoku et renvoie l'ensemble de ses solutions.</p>
     *
     * @param sudoku Le sudoku à résoudre
     *
     * @return L'ensemble des solutions du sudoku
     *
     * @see Sudoku
     *
     * @since 1.0
     */
    public ArrayList<Integer[]> solve(Sudoku sudoku);

}
