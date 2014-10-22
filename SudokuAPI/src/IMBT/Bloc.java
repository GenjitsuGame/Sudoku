/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package IMBT;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <p>
 * <b>Bloc</b> est une classe réprésentant un bloc de la grille du sudoku.</p>
 *
 * <p>
 * Il est important de noter que pour l'instant, la seule longueur d'arête
 * supportée est de 3.</p>
 *
 * <p>
 * Cette classe permet de générer un bloc aléatoire d'en obtenir un début de
 * grille résolvable. Ici, soit X un bloc généré et O un bloc vide, les méthodes
 * de la classe permettent actuellement d'obtenir une grille suivant le modèle
 * suivant :</p>
 *
 * <p>
 * XOO<br>
 * OOX<br>
 * OXO</p>
 *
 * <p>
 * Les nouveaux blocs sont obtenus par multiplications de matrices. (voir
 * {@link Matrix}).</p>
 *
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see Matrix
 *
 * @since 1.0
 */
public class Bloc {

    /**
     * <p>
     * Taille de l'arête d'un bloc de la grille. Pour l'instant, la seule taille
     * supportée est de 3.</p>
     *
     * @see #getSize()
     *
     * @since 1.0
     */
    private static final int SIZE = 3;

    /**
     * <p>
     * Liste des valeurs contenues dans le bloc dans l'ordre suivant :<br>
     * 012<br>
     * 345<br>
     * 678<br>
     * les chiffres représentants les indices des éléments de <code>_bloc</code>
     * </p>
     */
    private ArrayList<Integer> _bloc;

    /**
     * <p>
     * Génère un nouveau bloc de taille 3x3 aléatoirement. </p>
     *
     * @see Collections#shuffle(java.util.List)
     *
     * @since 1.0
     */
    public Bloc() {
        _bloc = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; ++i) {
            _bloc.add(i + 1);
        }
        Collections.shuffle(_bloc);
    }

    /**
     * <p>
     * Génère un bloc de taille 3x3 à partir d'une valeur passé en
     * paramètre.</p>
     *
     * <p>
     * Tous les chiffres du bloc auront la valeur de l'entier passé en
     * paramètre, ce qui est utile pour générer un bloc vide.</p>
     *
     * @param value Valeur des chiffres du nouveau bloc
     *
     * @since 1.0
     */
    public Bloc(int value) {
        _bloc = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; ++i) {
            _bloc.add(value);
        }
    }

    /**
     * <p>
     * Génère un Bloc à partir d'une liste de 9 valeurs. Il faut noter qu'aucun
     * test n'est fait pour vérifier si chaque valeur n'apparait qu'une seule
     * fois dans la liste, les méthodes de générations pouvant varier et ce
     * problème pouvant donc être réglé plus tard dans l'algorithme.</p>
     *
     * @param values Liste des valeurs du bloc.
     *
     * @throws IllegalArgumentException si la liste contient plus ou moins de 9
     *                                  valeurs.
     *
     * @see Matrix#generateColBloc(IMBT.Bloc)
     * @see Matrix#generateRowBloc(IMBT.Bloc)
     *
     * @since 1.0
     */
    public Bloc(ArrayList<Integer> values) throws IllegalArgumentException {
        if (values.size() != SIZE * SIZE) {
            throw new IllegalArgumentException("Illegal ammount of values for a"
                    + " 3x3 bloc.");
        }
        _bloc = values;
    }

    /**
     * <p>
     * Getter sur la taille de l'arête du bloc.</p>
     *
     * @return La taille de l'arête du bloc
     *
     * @since 1.0
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * <p>
     * Retourne la valeur à une position donnée dans le bloc.</p>
     *
     * @param i L'indice de la valeur
     *
     * @return La valeur
     *
     * @since 1.0
     */
    public Integer getValueAt(int i) {
        return _bloc.get(i);
    }

    /**
     * <p>
     * Génère une grille selon le modèle suivant suivant :<br>
     * XOO<br>
     * OOX<br>
     * OXO</p>
     * 
     * <ul>
     * <li>X est un bloc généré</li>
     * <li>O est un bloc vide</li>
     * </ul>
     * 
     * @return La grille générée
     * 
     * @see Matrix#generateColBloc(IMBT.Bloc) 
     * @see Matrix#generateRowBloc(IMBT.Bloc) 
     * 
     * @since 1.0
     */
    public ArrayList<Bloc> generateGrid() {
        ArrayList<Bloc> grid = new ArrayList<>();
        grid.add(this);
        Matrix m = new Matrix();

        grid.add(new Bloc(0));
        grid.add(new Bloc(0));
        grid.add(new Bloc(0));
        grid.add(new Bloc(0));
        grid.add(m.generateRowBloc(m.generateRowBloc(m.generateColBloc(this))));
        grid.add(new Bloc(0));
        grid.add(m.generateRowBloc(m.generateColBloc(m.generateColBloc(this))));
        grid.add(new Bloc(0));

        return grid;
    }

}
