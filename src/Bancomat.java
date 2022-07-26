
import jdk.swing.interop.SwingInterOpUtils;

import java.util.Arrays;
import java.util.Scanner;

public class Bancomat {
    //algoritmul de impartire a banilor in nr. minim de bancnote - programare dinamica
    public static int[] prog_dinamica(int suma, int[] denominatii, int[] monetar_disp) {
        //initiere lista de combinatii de bancnote pt intervalul (0,suma)
        int[][] bani_folositi = new int[suma + 1][];
        for (int i = 0; i < suma; i++) {
            bani_folositi[i] = new int[denominatii.length];
        }
        //initializere lista de nr minim de bancnote pt intervalul (0,suma)
        int[] min_bani = new int[suma + 1];
        for (int i = 1; i <= suma; ++i) {
            min_bani[i] = 9999;
        }

        int[] monetar_disp_copy = monetar_disp.clone();
        //comparare nr minim de bancnote pt intervalul (0,suma) pt calcul combinatii
        for (int i = 0; i < denominatii.length; ++i) {
            while (monetar_disp_copy[i] > 0) {
                for (int j = suma; j >= 0; --j) {
                    int suma_curenta = j + denominatii[i];
                    if (suma_curenta <= suma) {
                        if (min_bani[suma_curenta] > min_bani[j] + 1) {

                            min_bani[suma_curenta] = min_bani[j] + 1;
                            bani_folositi[suma_curenta] = bani_folositi[j].clone();
                            bani_folositi[suma_curenta][i] += 1;
                        }
                    }
                }

                monetar_disp_copy[i] -= 1;  //scadere din stoc bancnote
            }
        }
        int[] bani_fol_final = bani_folositi[suma];
        if (min_bani[suma] == 9999) {
            bani_fol_final[1]=-1;
        }
        return bani_fol_final;
    }
    public static void alerta_stoc(int[] monetar_disp, int[] monetar_init){
        if (monetar_disp[4] <= monetar_init[4] * 0.2) {
            if(monetar_disp[4] <= monetar_init[4] * 0.1) {
                System.out.println("\n--Critical! Stoc Critic: "+ monetar_disp[4] + "\nAu ramas mai putin de 10% Bancnote de 100 lei");
            }
            else System.out.println("\n--Warning! Stoc Limitat: "+ monetar_disp[4] + "\n Au ramas mai putin de 20% Bancnote de 100 lei");

        }
        if (monetar_disp[3] <= monetar_init[3] * 0.15)
            System.out.println("\n--Warning! Stoc Limitat: "+ monetar_disp[3] + "\n Au ramas mai putin de 15% Bancnote de 50 lei");

    }
    public static void alerta_client(int suma_retrasa)
    {
        if(suma_retrasa>200)
            System.out.println("\n--Alerta! S-au retras "+ suma_retrasa+ " de pe contul dvs. Daca nu ati efectuat dvs. aceasta operatiune, contactati banca de urgenta.");

    }

    public static void main(String[] args) {
        int[] denominatii = { 1, 5, 10, 50, 100 };
        int[] monetar_init = {100,100,100,50,50};
        int[] monetar_disp = monetar_init.clone();
        int[] bani_folositi;
        int stoc = 0, suma_retrasa;

        //calcul stoc bani bancomat
        for (int i = 0; i < 5; i++) {
            stoc += denominatii[i] * monetar_disp[i];
        }

        //introducere date de catre client
        Scanner sc = new Scanner(System.in);

        while(true)
        {
            System.out.println("\n---BancomatApp---\n");
            System.out.println("1 - Client");
            System.out.println("2 - Administrator");
            System.out.print("Introdu tasta: ");

            int alegere = sc.nextInt();

            switch(alegere)
            {
                case 1:
                    while(true) {
                        System.out.println("Apasa 1 pentru a retrage bani");
                        System.out.println("Apasa 2 pentru BACK\n");
                        System.out.print("Introdu tasta: ");

                        int alegere2 = sc.nextInt();
                        switch (alegere2) {
                            case 1 -> {
                                System.out.print("Introdu suma pentru retragere:");
                                suma_retrasa = sc.nextInt();
                                if (stoc >= suma_retrasa) {
                                    bani_folositi = prog_dinamica(suma_retrasa, denominatii, monetar_disp);
                                    if (bani_folositi[1] == -1) {
                                        System.out.println("Eroare la retragere.");
                                    } else {
                                        stoc = stoc - suma_retrasa;
                                        System.out.println("Retragere reusita!");
                                        System.out.println("Banii vor fi impartiti astfel: ");
                                        for (int i = 0; i < bani_folositi.length; i++) {
                                            System.out.println(bani_folositi[i] + " bancnote de " + denominatii[i] + " lei");
                                            monetar_disp[i] -= bani_folositi[i];
                                        }
                                        alerta_client(suma_retrasa);

                                    }
                                } else {
                                    System.out.println("Stoc insuficient pentru retragere suma!");
                                }
                            }
                            default -> {
                            }
                        }
                        if(alegere2 == 2) break;
                    }
                    break;



                case 2:

                    System.out.println("1 - Inspecteaza Stoc");
                    System.out.println("2 - EXIT");
                    System.out.print("Introdu tasta: ");

                    int alegere3 = sc.nextInt();
                    switch (alegere3) {
                        case 1 -> {
                            alerta_stoc(monetar_disp, monetar_init);
                            System.out.println("\nStocul actual: ");
                            for (int i = 0; i < monetar_disp.length; i++) {
                                System.out.println(monetar_disp[i] + " bancnote de " + denominatii[i] + " lei");
                            }
                        }
                        case 2 -> System.exit(0);
                        default -> {
                        }
                    }
                    break;
            }
        }





    }
}
