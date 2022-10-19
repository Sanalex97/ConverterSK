package Converters;

public class Converter {

    public static double[] convertPSKtoPSK(double x, double y, double z, double deltaX, double deltaY,
                                           double deltaZ, double omegaX, double omegaY, double omegaZ, double m) {
        double xB, yB, zB;

        double x00 = (1 + m) * 1;
        double x01 = (1 + m) * omegaZ;
        double x02 = (1 + m) * (-omegaY);

        double y10 = (1 + m) * (-omegaZ);
        double y11 = (1 + m) * 1;
        double y12 = (1 + m) * omegaX;

        double z20 = (1 + m) * omegaY;
        double z21 = (1 + m) * (-omegaX);
        double z22 = (1 + m) * 1;

        xB = x00 * x + x01 * y + x02 * z + deltaX;
        yB = y10 * x + y11 * y + y12 * z + deltaY;
        zB = z20 * x + z21 * y + z22 * z + deltaZ;


/*        //todo применить коэффициент m к исходным координатам
        x = x * (1 + m);
        y = y * (1 + m);
        z = z * (1 + m);
        //todo перемножить матрицы (строка умножается на столбец поэлементно и складывается)
        xB = x + y * omegaZ + z * (-omegaY);
        yB = -omegaZ * x + y + omegaX * z;
        zB = omegaX * x + (-omegaX) * y + z;
        //todo добавить линейные элементы трансформирования
        xB = xB + deltaX;
        yB = yB + deltaY;
        zB = zB + deltaZ;*/

        return new double[]{xB, yB, zB};
    }

    public static double[] convertGSKtoGSK(double b, double l, double h, double deltaX, double deltaY,
                                           double deltaZ, double omegaX, double omegaY, double omegaZ, double m,
                                           double sysAa, double sysBa, double sysAe2, double sysBe2) {
        double bB;
        double lB;
        double hB;


        double a = (sysAa + sysBa) / 2;
        double e2 = (sysAe2 + sysBe2) / 2;
        double v = e2 * Math.pow(Math.sin(b), 2);
        double M = a * (1 - e2) * Math.pow((1 - v), -1.5);
        double N = a * (Math.pow((1 - v), -0.5));
        // double N = a * Math.pow((1 - (e2 * Math.pow(Math.sin(b), 2))), -0.5);
        double deltaA = sysBa - sysAa;
        double deltaE2 = sysBe2 - sysAe2;
        double ro = 206_264.806;
        double deltaB = 0;
        double deltaL = 0;
        double deltaH = 0;

        // for (int i = 0; i < 2; i++) {
        deltaB = ro / (M + h) * ((N / a) * e2 * Math.sin(b) * Math.cos(b)
                * deltaA + ((N * N) / (a * a) + 1) * N * Math.sin(b) * Math.cos(b)
                * (deltaE2 / 2) - (deltaX * Math.cos(l) + deltaY * Math.sin(l))
                * Math.sin(b) + deltaZ * Math.cos(b)) - omegaX * Math.sin(l)
                * (1 + e2 * Math.cos(2 * b)) + omegaY
                * Math.cos(l) * (1 + e2 * Math.cos(2 * b)) - ro * m
                * e2 * Math.sin(b) * Math.cos(b);

        deltaL = (ro / ((N + h) * Math.cos(b))) * (-deltaX
                * Math.sin(l) + deltaY * Math.cos(l))
                + Math.tan(b) * (1 - e2) * (omegaX * Math.cos(l) + omegaY
                * Math.sin(l)) - omegaZ;

        deltaH = -(a / N) * deltaA + N * Math.pow(Math.sin(b), 2) * (deltaE2 / 2)
                + (deltaX * Math.cos(l) + deltaY * Math.sin(l)) * Math.cos(b)
                + deltaZ * Math.sin(b) - N * e2 * Math.sin(b) * Math.cos(b)
                * ((omegaX / ro) * Math.sin(l)
                - (omegaY / ro) * Math.cos(l)) + (((a * a) / N) + h) * m;

        /*    if (i == 0) {
                b = (b * 180 / Math.PI * 3600 + deltaB) / ro;
                l = (l * 180 / Math.PI * 3600 + deltaL) / ro;
                h = h + deltaH;
            } else {
                b = (b * 180 / Math.PI * 3600 + (b * 180 / Math.PI * 3600 + deltaB)) / 2;
                l = (l * 180 / Math.PI * 3600 + (l * 180 / Math.PI * 3600 + deltaL)) / 2;
            }*/

        //  }

        bB = (b * 180 / Math.PI * 3600 + deltaB) / 3600;
        lB = (l * 180 / Math.PI * 3600 + deltaL) / 3600;
        hB = h + deltaH;

        return new double[]{bB, lB, hB};
    }

    public static double[] convertPSKtoGSK(double x, double y, double z, double a, double e2) {
        double B = 0, L = 0, H = 0;
        double r = Math.sqrt(x * x + y * y);                    // вспомогательная величина
        // todo анализ r
        if (r < 0.000000001) {
            B = (Math.PI * z) / (2 * Math.abs(z));
            L = 0;
            H = z * Math.sin(B) - a * Math.sqrt(1 - e2 * Math.sin(B) * Math.sin(B));

        } else if (r > 0.000000001) {
            double La = Math.abs(Math.asin(y / r));

            if (y >= 0 && x >= 0) {
                L = La;
            } else if (y >= 0 && x < 0) {
                L = Math.PI - La;
            } else if (y < 0 && x >= 0) {
                L = 2 * Math.PI - La;
            } else if (y < 0 && x < 0) {
                L = Math.PI + La;
            }
        }

        // todo анализ z
        if (Math.abs(z) < 0.000000001) {
            B = 0;
            H = r - a;
        } else {
            double ro = Math.sqrt(x * x + y * y + z * z);
            double c = Math.asin(z / ro);
            double p = (e2 * a) / (2 * ro);

            //todo реализуем итеративный процесс
            double s1, s2 = 0;
            double b;
            double timeStarted = System.currentTimeMillis();
            // стоп-таймер 5 сек
            while (true) {
                s1 = s2;
                b = c + s1;
                s2 = Math.asin(p * Math.sin(2 * b) / (Math.sqrt(1 - e2 * Math.sin(b) * Math.sin(b))));
                if ((Math.abs(s2 - s1) < 0.0000001) || ((System.currentTimeMillis() - timeStarted) > 25000)) {      // если заданная точность достигнута
                    B = b;

                    if (r / z < Math.pow(10, -8)) {
                        B = B - r / z;
                    }

                    H = r * Math.cos(B) + z * Math.sin(B) - a * Math.sqrt(1 - e2 * Math.sin(B) * Math.sin(B));

                    break;
                }
            }
        }

        return new double[]{B, L, H};
    }

    public static double[] convertGSKtoPSK(double b, double l, double h, double a, double e2) {
        double x, y, z;
        // геодезические параметры
        double N = a / (Math.sqrt(1 - e2 * Math.sin(b) * Math.sin(b)));  // радиус кривизны первого вертикала

        x = (N + h) * Math.cos(b) * Math.cos(l);
        y = (N + h) * Math.cos(b) * Math.sin(l);
        z = ((1 - e2) * N + h) * Math.sin(b);

        return new double[]{x, y, z};
    }

    public static double[] convertGSKtoGAUSS(double b, double l, double a, double e2) {
        l = l * TypeConverter.PARAMSECTORAD / 3600;
        double N = a / (Math.sqrt((1 - (e2 * Math.pow(Math.sin(b), 2))))); // радиус кривизны первого вертикал
        double n = Math.round((l + 6) / 6); // номер зоны, l - в градусы переводить
        double l0 = 6 * n - 3; // средний осевой мередиан
        double deltaL = (l - l0) * 3600;
        double nu2 = e2 / (1 - e2) * Math.pow(Math.cos(b), 2); //

        double d1 = a * (1 - e2 / 4 - 3 * Math.pow(e2, 2) / 64 - 5 * Math.pow(e2, 3) / 256);
        double d2 = a * (3 * e2 / 8 + 3 * Math.pow(e2, 2) / 32 + 45 * Math.pow(e2, 3) / 1024);
        double d3 = a * (15 * Math.pow(e2, 2) / 256 + 45 * Math.pow(e2, 3) / 1024);
        double d4 = a * (35 * Math.pow(e2, 3) / 3072);

        double X = d1 * b - d2 * Math.sin(2 * b) + d3 * Math.sin(4 * b) - d4 * Math.sin(6 * b); // b - в радианах

        double x = X + (Math.pow(deltaL, 2) * N * Math.cos(b) * Math.sin(b)) / (2 * Math.pow(TypeConverter.PARAMSECTORAD, 2))
                * (1 + Math.pow(deltaL, 2) * Math.pow(Math.cos(b), 2) / (12 * Math.pow(TypeConverter.PARAMSECTORAD, 2))
                * (5 - Math.pow(Math.tan(b), 2) + 9 * nu2 + Math.pow(nu2, 2)));

        double Y = deltaL * N * Math.cos(b) / TypeConverter.PARAMSECTORAD * (1 + Math.pow(deltaL, 2) * Math.pow(Math.cos(b), 2)
                / (6 * Math.pow(TypeConverter.PARAMSECTORAD, 2)) * (1 - Math.pow(Math.tan(b), 2) + nu2));

        double y = Y + n * Math.pow(10, 6) + 5 * Math.pow(10, 5);

        return new double[]{x, y};
    }

    public static double[] convertGAUSStoGSK(double x, double y, double a, double b, double e2) {
        int n = (int) (y * Math.pow(10, -6)); // номер зоны

        double g0 = 1 + 3 * e2 / 4 + 45 * Math.pow(e2, 2) / 64 + 175 * Math.pow(e2, 3) / 256 + 11205 * Math.pow(e2, 4) / 16384;
        double g1 = 3 * e2 / 8 + 3 * Math.pow(e2, 2) / 16 + 213 * Math.pow(e2, 3) / 2048 + 255 * Math.pow(e2, 4) / 4096;
        double g2 = 21 * Math.pow(e2, 2) / 256 + 21 * Math.pow(e2, 3) / 256 + 549 * Math.pow(e2, 4) / 8192;
        double g3 = 151 * Math.pow(e2, 3) / 6144 + 4065 * Math.pow(e2, 4) / 24576;

        double betta = x / (g0 * a * (1 - e2));
        double bX = (betta + g1 * Math.sin(2 * betta) + g2 * Math.sin(4 * betta)
                + g3 * Math.sin(6 * betta)); // геодезическая широта

        double eShtrih = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(b, 2);
        double nuX2 = eShtrih * Math.pow(Math.cos(bX), 2);

        double l0 = (6 * n - 3) * Math.PI / 180;

        double yd = y - (5 + 10 * n) * Math.pow(10, 5);
        double nX = (a * Math.sqrt(1 + eShtrih)) / (Math.sqrt(1 + nuX2));
        double l1 = yd / (nX * Math.cos(bX));

        double l2 = 1 - Math.pow(yd, 2) / (6 * Math.pow(nX, 2)) * (1 + 2 * Math.pow(Math.tan(bX), 2) + nuX2);
        double l3 = Math.pow(yd, 4) / (120 * Math.pow(nX, 4)) *
                (5 + 28 * Math.pow(Math.tan(bX), 2) + 24 * Math.pow(Math.tan(bX), 4)
                        + 6 * nuX2 + 8 * nuX2 * Math.pow(Math.tan(bX), 2));

        double l4 = l1 * (l2 + l3);
        double L =  l0 + l4; // долгота геодезическая

        //todo add ro
        double  B = bX - (Math.pow(yd, 2) * (1 + nuX2) * Math.tan(bX)) / (2 * Math.pow(nX, 2)) * (1 - Math.pow(yd, 2) / (12 * Math.pow(nX, 2))
                * (5 + 3 * Math.pow(Math.tan(bX), 2) + nuX2 - 9 * nuX2 * Math.pow(Math.tan(bX), 2))
                + Math.pow(yd, 4) / (360 * Math.pow(nX, 4)) * (61 + 90 * Math.pow(Math.tan(bX), 2) + 45 * Math.pow(Math.tan(bX), 4))); // широта геодезическая

        return new  double[] {B * TypeConverter.PARAMSECTORAD / 3600, L * TypeConverter.PARAMSECTORAD / 3600};
    }
}
