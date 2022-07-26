
import java.util.Arrays;
import java.util.Scanner;

public class Bancomat {
    public static int[] prog_dinamica(int suma, int[] denominatii, int[] monetar_disp) {

        int[][] bani_folositi = new int[suma + 1][];
        for (int i = 0; i < suma; i++) {
            bani_folositi[i] = new int[denominatii.length];
        }

        int[] min_bani = new int[suma + 1];
        for (int i = 1; i <= suma; ++i) {
            min_bani[i] = 9999;
        }

        int[] monetar_disp_copy = monetar_disp.clone();

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

                monetar_disp_copy[i] -= 1;
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
                System.out.println("Critical! Stoc Critic: "+ monetar_disp[4] + "\nAu ramas mai putin de 10% Bancnote de 100 lei");
            }
            else System.out.println("Warning! Stoc Limitat: "+ monetar_disp[4] + "\n Au ramas mai putin de 20% Bancnote de 100 lei");

        }
        if (monetar_disp[3] <= monetar_init[3] * 0.15)
            System.out.println("Warning! Stoc Limitat: "+ monetar_disp[3] + "\n Au ramas mai putin de 15% Bancnote de 50 lei");

    }

    public static void main(String[] args) {
        int[] denominatii = { 1, 5, 10, 50, 100 };
        int[] monetar_init = {100,100,100,50,50};
        int[] monetar_disp = monetar_init.clone();
        int[] bani_folositi;
        int stoc = 0, de_retras;

        //calcul stoc bani bancomat
        for (int i = 0; i < 5; i++) {
            stoc += denominatii[i] * monetar_disp[i];
        }

        //introducere date de catre client
        Scanner sc = new Scanner(System.in);

        while(true)
        {
            System.out.println("BancomatApp\n");
            // - Verificare stoc - inainte de introducere date
            alerta_stoc(monetar_disp, monetar_init);
            System.out.println("Apasa 1 pentru a retrage bani");
            System.out.println("Apasa 2 pentru EXIT\n");
            System.out.print("Introdu tasta: ");


            int alegere = sc.nextInt();
            switch(alegere)
            {
                case 1:
                    System.out.print("Introdu suma pentru retragere:");
                    de_retras = sc.nextInt();
                    if(stoc >= de_retras)
                    {
                           bani_folositi = prog_dinamica(de_retras, denominatii, monetar_disp);
                           if(bani_folositi[1] == -1)
                           {
                               System.out.println("Eroare la retragere.");
                           }
                           else {
                               stoc = stoc - de_retras;
                               System.out.println("Retragere reusita!");
                               System.out.println("Banii vor fi impartiti astfel: ");
                               for (int i = 0; i < bani_folositi.length; i++) {
                                   System.out.println(bani_folositi[i] + " bancnote de " + denominatii[i] + " lei");
                                   monetar_disp[i] -= bani_folositi[i];
                               }
                           }
                    }
                    else
                    {
                        System.out.println("Stoc insuficient pentru retragere suma!");
                    }
                    System.out.println("");

                    break;

                case 2:
                    //exit from the menu
                    System.exit(0);
            }
        }

    }
}
