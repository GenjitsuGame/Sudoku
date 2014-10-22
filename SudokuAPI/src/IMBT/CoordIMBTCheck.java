/*
 * Projet de AAV - IUT Informatique Paris Descartes 2014/2015
 * Pascal Luttgens 201
 */
package IMBT;

import Coord2D.Coord2D;
import SudoLib.ExistArray;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>
 * <code><b>CoordIMBTCheck</b></code> est une extension de
 * {@link Coord2D} adapté au sudoku.</p>
 *
 * <p>
 * Une instance de cette classe représente une case de la grille et permet de
 * gérer la liste des valeurs que la case peut accepter en fonction d'une
 * certaine configuration. Il est notamment possible d'itérer sur la liste à
 * travers les méthodes proposées par la classe ce qui est utile pour les
 * algorithmes de résolution.</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @see Coord2D
 *
 * @since 1.0
 */
public class CoordIMBTCheck extends Coord2D implements Comparable<CoordIMBTCheck> {

    /**
     * <p>
     * La liste des valeurs que la case peut accepter pour une certaine
     * configuration de sudoku</p>
     *
     * @since 1.0
     */
    private ArrayList<Integer> _concurrents;

    /**
     * <p>
     * Itérateur sur la liste des valeurs que la case peut accepter</p>
     *
     * @since 1.0
     */
    private Iterator<Integer> _current;

    /**
     * <p>
     * Construit une case à partir de ses coordonnées sur la grille.</p>
     *
     * @param i Coordonnée en x
     * @param j Coordonnée en y
     *
     * @see Coord2D#Coord2D(int, int)
     *
     * @since 1.0
     */
    public CoordIMBTCheck(int i, int j) {
        super(i, j);
        _concurrents = new ArrayList<>();
    }

    /**
     * <p>
     * Ajoute la liste des valeurs possibles pour la case.</p>
     *
     * @param concurrents La liste des valeurs possibles
     *
     * @see ExistArray#countContenders(Coord2D.Coord2D)
     *
     * @since 1.0
     */
    public void addPossibleValues(ArrayList<Integer> concurrents) {
        _concurrents = (ArrayList<Integer>) concurrents.clone();
        _current = _concurrents.iterator();
    }

    /**
     * <p>
     * Retourne la valeur à la position donnée dans la liste.</p>
     *
     * @param i L'indice de la valeur
     *
     * @return La valeur à l'indice donné
     *
     * @see #_concurrents
     *
     * @since 1.0
     */
    public Integer getPossibleValue(int i) {
        return _concurrents.get(i);
    }

    /**
     * <p>
     * Retourne le nombre de valeurs possibles pour la case.</p>
     *
     * @return Le nombre de valeurs possibles pour la case
     *
     * @since 1.0
     */
    public int getNbPossibleValues() {
        return _concurrents.size();
    }

    /**
     * <p>
     * Retourne l'itérateur sur la liste des valeurs possibles</p>
     *
     * @return L'itérateur sur la liste des valeurs possibles
     *
     * @see #_concurrents
     *
     * @since 1.0
     */
    public Iterator<Integer> getCurrent() {
        return _current;
    }

    /**
     * <p>
     * Réinitialise l'itérateur sur la liste des valeurs possibles afin de le
     * remettre au début de la collection.</p>
     *
     * @see #_concurrents
     *
     * @since 1.0
     */
    public void resetCurrent() {
        _current = _concurrents.iterator();
    }

    /**
     * <p>
     * Spécialisation de la méthode {@link Comparable#compareTo(java.lang.Object)
     * }
     * permettant de comparer deux cases en fonctions de leur nombre de valeurs
     * possibles.
     *
     * @param coord La case à comparer
     *
     * @return 1 si <code>coord</code> à un nombre de valeurs possible inférieur
     *         à <code>this</code>
     *
     * @see Comparable
     *
     * @since 1.0
     */
    @Override
    public int compareTo(CoordIMBTCheck coord) {
        if (getNbPossibleValues() == coord.getNbPossibleValues()) {
            return 0;
        } else if (getNbPossibleValues() > coord.getNbPossibleValues()) {
            return 1;
        } else {
            return -1;
        }
    }

}
