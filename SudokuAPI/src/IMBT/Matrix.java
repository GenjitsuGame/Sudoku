/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package IMBT;

import java.util.ArrayList;
import java.util.Random;

/**
 * <p>
 * <code><b>Matrix</b></code> permet de générer les matrices servant à générer
 * les blocs de la grille de sudoku.</p>
 *
 * <p>
 * L'idée est de générer un bloc aléatoire B et d'obtenir les autres blocs en
 * effectuant des multiplications matrices avec la matrice M généré par cette
 * classe.</p>
 *
 * <p>
 * Pour que cela soit possible, la matrice M génré doit respecter les conditions
 * suivantes :</p>
 *
 * <ul>
 * <li>La matrice doit être composée uniquement des 0 et des 1</li>
 * <li>La matrice ne doit pas comporter de 1 sur la diagonale</li>
 * <li>Il ne peut y'avoir que un seul 1 par ligne et par colonne</li>
 * </ul>
 *
 * <p>
 * Pour obtenir un bloc adjaccent de la même ligne, on effectue M*B.<br>
 * Pour obtenir un bloc adjaccent de la même colonne, on effectue B*M.</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see Bloc
 *
 * @since 1.0
 */
public class Matrix {

    /**
     * <p>
     * Taille de la longueur de l'arête d'un bloc</p>
     */
    private static final int SIZE = 3;

    /**
     * <p>
     * Représentation abstraite de la matrice.<br>
     * L'indice donne la ligne et la valeur donne la position du 1 sur la
     * ligne</p>
     *
     * <p>
     * Exemple :<br>
     * matrixRow[0]=1 010<br>
     * matrixRow[1]=2 001<br>
     * matrixRow[2]=0 100<br>
     * </p>
     *
     * @since 1.0
     */
    private final int[] matrixRow;

    /**
     * <p>
     * Représentation abstraite de la matrice.<br>
     * L'indice donne la colonne et la valeur donne la position du 1 sur la
     * colonne</p>
     *
     * <p>
     * Exemple :<br>
     * matrixCol[0]=1 001<br>
     * matrixCol[1]=2 100<br>
     * matrixCol[2]=0 010<br>
     * </p>
     *
     * @since 1.0
     */
    private final int[] matrixCol;

    /**
     * <p>
     * Construit une matrice de taille 3x3, l'unique taille d'arête de bloc
     * supportée étant 3 pour l'instant.</p>
     *
     * <p>
     * Pour une taille d'arête de 3, il n'existe que deux matrices respectant
     * les conditions imposées. Ces matrices sont donc codées en dur et l'une
     * d'entre elle est choisit aléatoirement lors de la nouvelle
     * instanciation.</p>
     *
     * @see Random
     *
     * @since 1.0
     */
    public Matrix() {
        ArrayList<int[]> rows = new ArrayList<>();
        ArrayList<int[]> cols = new ArrayList<>();
        rows.add(new int[]{1, 2, 0});
        cols.add(new int[]{2, 0, 1});
        rows.add(new int[]{2, 0, 1});
        cols.add(new int[]{1, 2, 0});

        Random rand = new Random();

        int ind = rand.nextInt(rows.size());
        matrixCol = cols.get(ind);
        matrixRow = rows.get(ind);
    }

    /**
     * <p>
     * Génère un bloc adjaccent de la même ligne au bloc passé au passé en
     * paramètre.</p>
     *
     * @param bloc Le bloc référence
     *
     * @return Le bloc généré à partir du bloc référence
     * 
     * @see Bloc
     * @see #matrixRow
     * 
     * @since 1.0
     */
    public Bloc generateRowBloc(Bloc bloc) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                values.add(bloc.getValueAt(matrixRow[i] * SIZE + j));
            }
        }
        return new Bloc(values);
    }

    /**
     * <p>
     * Génère un bloc adjaccent de la même colonne au bloc passé au passé en
     * paramètre.</p>
     *
     * @param bloc Le bloc référence
     *
     * @return Le bloc généré à partir du bloc référence
     * 
     * @see Bloc
     * @see #matrixCol
     * 
     * @since 1.0
     */
    public Bloc generateColBloc(Bloc bloc) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                values.add(bloc.getValueAt(matrixCol[j] + i * SIZE));
            }
        }
        return new Bloc(values);
    }
}
