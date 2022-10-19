package src;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    //    private static final File FILEANOMALIYAHEIGHT = new File("src/main/resources/DB/аномалии высот для wgs.txt");
  private static final File FILEANOMALIYAHEIGHT = new File("src/main/resources/DB/аномалии высот для пз через полградуса.txt");
  //   private static final File FILEANOMALIYAHEIGHT = new File("src/main/resources/DB/аномалии высот для пз.txt");
    private static Supplier<Stream<String>> supplier;


    public static void main(String[] args) {
        Random random = new Random();
        double l = random.nextDouble((179.58 + 4.92) + 1) - 4.92;
        double b = random.nextDouble((90 - 6) + 1) + 6;


        if (FILEANOMALIYAHEIGHT.isFile()) {
            supplier = () -> {
                try {
                    return Files.lines(Paths.get(FILEANOMALIYAHEIGHT.getPath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
        }

        System.out.println("Долгота = " + l);
        System.out.println("Широта = " + b);

        double[] firstValAndStepL = getFirstValAndStep(true);
        double[] firstValAndStepB = getFirstValAndStep(false);

        int[] minMax = getMinAndMax(firstValAndStepL, firstValAndStepB, l, b);
        getAnomaliyasHeight(minMax, l, b);

    }

    public static double[] getFirstValAndStep(boolean isDolgota) {
        List<Double> listFirstValAndStep = getVal(new int[]{0, 1});

        double step = listFirstValAndStep.get(3) - listFirstValAndStep.get(0);

        if (isDolgota) {
            return new double[]{listFirstValAndStep.get(0), step};
        } else {
            return new double[]{listFirstValAndStep.get(1), step};
        }
    }

    public static int[] getMinAndMax(double[] firstValL, double[] firstValB, double valL, double valB) {
        int minL;
        int maxL;
        int minB;

        int minLminB;
        int minLmaxB;
        int maxLminB;
        int maxLmaxB;

        minB = (int) ((int) (((firstValB[0] - (int)valB)) * 185 / firstValB[1]) / firstValB [1]);


        minL = (int) (((valL - firstValL[0]) / firstValL[1])); // для первой сетки широт
        maxL = minL + 1; // для первой сетки широт

        if(valB % 1 >= 0.5 && firstValB[1] == 0.5) {
          minB = (int) (minB -  185 / firstValB[1]);
        }
        minLminB = minL + minB;
        minLmaxB = (int) (minLminB - 185 / firstValB[1]);
        maxLminB = maxL + minB; // для первой сетки широт
        maxLmaxB = (int) (maxLminB - 185 / firstValB[1]); // для первой сетки широт

        return new int[]{minLminB, minLmaxB, maxLminB, maxLmaxB};
    }

    public static double getAnomaliyasHeight(int[] diapozon, double l, double b) {
        List<Double> listAnomaliyasHeight = getVal(diapozon);
        System.out.println(listAnomaliyasHeight);
        double step = listAnomaliyasHeight.get(4) - listAnomaliyasHeight.get(1);
        double anomaliyaL1 = (listAnomaliyasHeight.get(5) - listAnomaliyasHeight.get(2)) * ((b - listAnomaliyasHeight.get(1)) / step) + listAnomaliyasHeight.get(2);
        double anomaliyaL2 = (listAnomaliyasHeight.get(11) - listAnomaliyasHeight.get(8)) * ((b - listAnomaliyasHeight.get(1)) / step) + listAnomaliyasHeight.get(8);

        double anomaliyaH = ((anomaliyaL2 - anomaliyaL1) * ((l - listAnomaliyasHeight.get(0)) / step)) + anomaliyaL1;

        System.out.println("Аномалия 1 = " + anomaliyaL1);
        System.out.println("Аномалия 2 = " + anomaliyaL2);
        System.out.println("Аномалия = " + anomaliyaH);

        return anomaliyaH;
    }

    public static List<Double> getVal(int[] diapozon) {
        List<Double> list = new ArrayList<>();
        StringBuilder strResult = new StringBuilder();

        for (int i = 0; i < diapozon.length; i++) {
            strResult.append(supplier.get().skip(diapozon[i]).findFirst().get());
        }

        for (String str : strResult.toString().split(" ")) {

            if (!str.equals("")) {
                list.add(Double.parseDouble(str));
            }
        }

        return list;
    }
}