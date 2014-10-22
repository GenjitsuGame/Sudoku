/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package Sudoku;

/**
 * <p>
 * <b><code>IGen</code></b> définit le contrat avec les classes contenant les
 * méthodes de génération pour générer une grille de {@link Sudoku} valide.</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see Sudoku
 *
 * @since 1.0
 */
public interface IGen {

    /**
     * <p>
     * Génère une grille de sudoku en fonction du nombre de cases à initaliser
     * et la retourne.</p>
     *
     * @param solvedCases Le nombre de case à initaliser
     *
     * @return La grille générée
     *
     * @throws IllegalArgumentException Si le nombre de case à initialiser est
     *                                  invalide
     *
     * @see Sudoku
     *
     * @since 1.0
     */
    public Integer[] generate(int solvedCases) throws IllegalArgumentException;
}
