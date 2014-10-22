/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IO;

import IMBT.InMemoryBTCheck;

import java.util.ArrayList;

import Sudoku.Sudoku;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

/**
 * <p>
 * <b><code>InOut</code></b> est la classe d'entrée sortie du programme.</p>
 *
 * <p>
 * Elle permet d'éxécuter les actions en fonctions des paramètres d'éxécution du
 * programme. Les sudoku générés sont stockés dans un fichier et les sudoku
 * peuvent être résolus à partir de ce même fichier.</p>
 *
 * <p>
 * Il faut cependant veiller à fournir un fichier d'aide décrivant la liste des
 * commandes et leurs fonctions. (ReadMe.txt)</p>
 *
 * @author Pascal Luttgens
 * @version 1.0
 *
 * @since 1.0
 */
public class InOut {

    /**
     * <p>
     * Le sudoku</p>
     *
     * @since 1.0
     */
    private Sudoku sudoku;

    /**
     * <p>
     * Main du programme, récupère les paramètres en ligne de commande et
     * appelle la méthode {@link #action(java.lang.String[])}.
     *
     * @param args Les paramètres d'éxécution
     *
     * @see #action(java.lang.String[])
     *
     * @since 1.0
     */
    public static void main(String[] args) {
        long debut = System.currentTimeMillis();
        InOut run = new InOut();
        run.action(args);
        long fin = System.currentTimeMillis();
        System.out.println(fin - debut);
    }

    /**
     * <p>
     * Vérfie si les actions données en paramètres sont valides et reconnues par
     * le programme.</p>
     *
     * @param opt Les options possibles
     * @param arg L'option à tester
     *
     * @return Vrai si l'option est reconnue
     *
     * @since 1.0
     */
    private boolean is_opt(String[] opt, String arg) {

        boolean isOpt = false;
        for (String s : opt) {
            if (s.equals(arg)) {
                isOpt = true;
            }
        }
        return isOpt;
    }

    /**
     * <p>
     * Affiche la liste des paramètres entrés par l'utilisateur qui ne sont pas
     * reconnus par le programme.</p>
     *
     * @param args Les arguments entrés par l'utilisateur
     * @param pos  La position du premier argument non reconnu
     *
     * @since 1.0
     */
    private void show_unknown_opt(String[] args, int pos) {
        for (int i = pos; i < args.length; ++i) {
            System.err.println(args[i]);
        }
        System.err.println("Parametres inconnus\n"
                + "tapez -H generate ou -H pour plus d'informations");
        System.exit(1);
    }

    /**
     * <p>
     * Exécute une action en fonction des commandes passées en paramètre lors de
     * l'appel du programme.</p>
     *
     * @param args Les paramètres d'éxécution
     *
     * @see #show_unknown_opt(java.lang.String[], int)
     * @see #is_opt(java.lang.String[], java.lang.String)
     *
     * @since 1.0
     */
    public void action(String[] args) {
        String[] opt = new String[]{"-S", "solve",
            "-G", "generate",
            "-H", "help"};
        String sudokuPath = "grid.txt",
                solutionsPath = "solutions.txt",
                helpPath = "ReadMe.txt";

        if (args.length == 0) {
            try {
                sudoku = new Sudoku(20);
                System.out.println(sudoku);
                sudoku.solve();
                System.out.println(sudoku);
                return;
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.err.println(e);
                System.exit(2);
            }
        }

        if (!is_opt(opt, args[0])) {
            System.err.println("Commande inconnue.\n");
            System.exit(1);
        }

        int nb;

        switch (args[0]) {
            case "generate":
            case "-G":
                if (args.length == 1) {
                    System.err.println("Parametres non renseignés.\n"
                            + "tapez -H generate ou -H pour plus d'informations");
                    System.exit(1);
                } else if (args.length == 2) {
                    try {
                        nb = Integer.parseInt(args[1]);
                        sudoku = new Sudoku(nb);
                        System.out.println(sudoku);
                    } catch (NumberFormatException e) {
                        System.err.println(args[1] + " doit etre un entier.");
                        System.exit(1);
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.err.println(e);
                        System.exit(2);
                    }
                } else {
                    show_unknown_opt(args, 2);
                }
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sudokuPath), "utf-8"))) {
                    writer.write(sudoku.toString());
                } catch (IOException ex) {
                    System.err.println("Le sudoku n'a pas pu être sauvegarde.");
                    System.exit(3);
                }
                break;
            case "-S":
            case "solve":
                String[] supported = new String[]{"imbt"};
                ArrayList<Integer> loadedSudoku = new ArrayList<>();

                try (Scanner s = new Scanner(new FileReader(sudokuPath))) {
                    while (s.hasNext()) {
                        loadedSudoku.add(s.nextInt());
                    }
                    sudoku = new Sudoku(loadedSudoku, 3);
                } catch (IOException ex) {
                    System.err.println("Le sudoku n'a pas pu être lu");
                    System.exit(3);
                } catch (IllegalArgumentException | IllegalStateException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(2);
                }

                if (args.length == 1) {
                    try {
                        sudoku.solve();
                        System.out.println(sudoku);
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        System.err.println();
                    }
                } else if ((args.length == 2) || (args.length == 3)) {
                    if (is_opt(supported, args[1])) {
                        switch (args[1]) {
                            case "imbt":
                                if (args.length == 3) {
                                    try {
                                        nb = Integer.parseInt(args[2]);
                                        sudoku.setCheck(new InMemoryBTCheck(sudoku, nb));
                                    } catch (NumberFormatException e) {
                                        System.err.println(args[1] + " et " + args[2] + " doit etre un entier.");
                                        System.exit(1);
                                    } catch (IllegalArgumentException e) {
                                        System.err.println(e.getMessage());
                                        System.exit(1);
                                    }
                                } else {
                                    try {
                                        sudoku.setCheck(new InMemoryBTCheck(sudoku));
                                    } catch (IllegalArgumentException e) {
                                        System.err.println(e.getMessage());
                                    }
                                }
                                try {
                                    sudoku.solve();
                                    System.out.println(sudoku);
                                } catch (IllegalStateException | IllegalArgumentException e) {
                                    System.err.println(e.getMessage());
                                    System.exit(2);
                                }
                                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(solutionsPath), "utf-8"))) {
                                    writer.write(sudoku.toString());
                                } catch (IOException ex) {
                                    System.err.println("La ou les solutions n'ont pas pu être sauvegardées.");
                                    System.exit(3);
                                }
                                break;
                        }
                    } else {
                        show_unknown_opt(args, 1);
                    }
                }
                break;
            case "-H":
            case "help":
                String help = new String();
                try (BufferedReader br = new BufferedReader(new FileReader(helpPath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        help += line + "\n";
                    }
                } catch (IOException ex) {
                    System.err.println("Le fichier ReadMe n'a pas pu être lu");
                    System.exit(3);
                }
                System.out.println(help);
        }

    }
}
