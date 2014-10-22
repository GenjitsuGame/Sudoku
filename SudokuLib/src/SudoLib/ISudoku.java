/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package SudoLib;

/**
 * <p>
 * <b>ISudoku</b> est l'interface définissant le contrat à respecté par
 * l'utilisateur pour que le sudoku puisse utiliser la librairie SudoLib.</p>
 * 
 * <p>
 * Elle impose en plus que le programme puisse générer et résoudre des grilles
 * de sudoku.</p>
 *
 * @author Luttgens Pascal
 * @version 1.0
 * @since 1.0
 */
public interface ISudoku {

    /**
     * <p>
     * Retourne la taille de l'arête d'un bloc du sudoku.</p>
     *
     * @return La taille de l'arête d'un bloc du sudoku
     */
    public int getSize();

    /**
     * <p>
     * Retourne le nombre de chiffres présent dans le sudoku.</p>
     *
     * @return Le nombre de chiffre présent dans le sudoku
     */
    public int getNbInitializedValue();

    /**
     * <p>
     * Retourne la valeur à la case indiquée par ses coordonnées dans le
     * plan.</p>
     *
     * @param x Coordonnée en abscisse
     * @param y Coordonnée en ordonnée
     *
     * @return La valeur pr2sente dans la case
     *
     * @throws IllegalArgumentException si les coordonnées sont invalides pour
     *                                  la grille courante
     */
    public int getValueAt(int x, int y) throws IllegalArgumentException;

    /**
     * <p>
     * Génère une grille de sudoku valide, c'est à dire résolvable.</p>
     *
     * <p>
     * Cependant, il n'y a pas de contrainte sur le nombre de solutions admises
     * par la grille et celle-ci peut donc en admettre plusieurs.</p>
     *
     * @param solvedCases Le nombre de case non-vides désiré
     *
     * @throws IllegalArgumentException Si solvedCase est considéré comme
     *                                  illégal, au minimum, solvedCases devrait
     *                                  être supérieur ou égal à 1.
     */
    public void generate(int solvedCases) throws IllegalArgumentException;

    /**
     * <p>
     * Résoud une grille de sudoku. Le type de retour est void pour ne pas
     * imposer de retourner les grilles de solutons dans le cas ou l'utilisateur
     * souhaiterait les afficher directement et non pas les stocker.</p>
     *
     * <p>
     * En effet, le nombre de solutions dépassant 6*10^21, il serait parfois
     * plus raisonnable de se contenter d'afficher les solutions que de toutes
     * les stocker.</p>
     *
     *
     * @throws IllegalStateException Si le sudoku ne peut être résolu dans son
     *                               état actuel
     */
    public void solve() throws IllegalStateException;
}
