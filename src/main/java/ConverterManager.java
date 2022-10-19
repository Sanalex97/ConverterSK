import Converters.Converter;
import Converters.TypeConverter;


public class ConverterManager {

    public double[] convert(String fromCoord, String toCoord, double XorB, double YorL, double ZorH, String systemA, String systemB) {

        double[] convertedCoordinates = new double[3];
        // если из одной системы в нее же
        if (systemA.equals(systemB)) {
            if (fromCoord.equals("X,Y,Z")) {
                if (toCoord.equals("X,Y,Z")) {
                    convertedCoordinates = new double[]{XorB, YorL, ZorH};
                } else if (toCoord.equals("B,L,H")) {
                   // convertedCoordinates = convertPSKtoGSK(systemA, XorB, YorL, ZorH);
                    convertedCoordinates = convertGAUSStoGSK(systemA, XorB, YorL);
                }
            } else if (fromCoord.equals("B,L,H")) {
                if (toCoord.equals("X,Y,Z")) {
                    convertedCoordinates = convertGSKtoPSK(systemA, XorB, YorL, ZorH);
                } else if (toCoord.equals("B,L,H")) {
                    convertedCoordinates = convertGSKtoGAUSS(systemA, XorB, YorL);
                    // convertedCoordinates = new double[]{XorB, YorL, ZorH};
                }
            }
        } else {    // если из одной в другую
            if (fromCoord.equals("X,Y,Z")) {
                if (toCoord.equals("X,Y,Z")) {
                    // XYZ(A) - XYZ(B)
                    convertedCoordinates = convertPSK(systemA, systemB, XorB, YorL, ZorH);
                    // XYZ(A) - BLH(A) - BLH(B) - XYZ(A)
//                    convertedCoordinates = convertPSKtoGSK(systemA, XorB,YorL,ZorH);
//                    convertedCoordinates = convertGSK(systemA, systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);
//                    convertedCoordinates = convertGSKtoPSK(systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);
                } else if (toCoord.equals("B,L,H")) {
                    // XYZ(A) - XYZ(B) - BLH(B)
                  /*  convertedCoordinates = convertPSK(systemA, systemB, XorB,YorL,ZorH);
                    convertedCoordinates = convertPSKtoGSK(systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);*/
                    // XYZ(A) - BLH(A) - BLH(B)
                    convertedCoordinates = convertPSKtoGSK(systemA, XorB, YorL, ZorH);
                    convertedCoordinates = convertGSK(systemA, systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);
                }
            } else if (fromCoord.equals("B,L,H")) {
                if (toCoord.equals("X,Y,Z")) {
                    // BLH(A) - XYZ(A) - XYZ(B)
                    convertedCoordinates = convertGSKtoPSK(systemA, XorB, YorL, ZorH);
                    convertedCoordinates = convertPSK(systemA, systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);
                    // BLH(A) - BLH(B) - XYZ(B)
//                    convertedCoordinates = convertGSK(systemA, systemB, XorB,YorL,ZorH);
//                    convertedCoordinates = convertGSKtoPSK(systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);
                } else if (toCoord.equals("B,L,H")) {
                    // BLH(A) - XYZ(A) - XYZ(B) - BLH(B)
                   /* convertedCoordinates = convertGSKtoPSK(systemA, XorB, YorL, ZorH);
                    convertedCoordinates = convertPSK(systemA, systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);
                    convertedCoordinates = convertPSKtoGSK(systemB, convertedCoordinates[0], convertedCoordinates[1], convertedCoordinates[2]);*/
                    // BLH(A) - BLH(B)
                    // convertedCoordinates = convertGSK(systemA, systemB, XorB, YorL, ZorH);

                    convertedCoordinates = convertGSKtoGAUSS(systemA, XorB, YorL);
                }
            }
        }
        return convertedCoordinates;
    }

    private double[] convertPSKtoGSK(String systemA, double x, double y, double z) {
        TypeConverter system = Main.getTranslateTableCoordinateSystem().get(systemA);
        return Converter.convertPSKtoGSK(x, y, z, system.getA(), system.getE2());
    }

    private double[] convertPSK(String systemA, String systemB, double x, double y, double z) {
        TypeConverter sysA = Main.getTranslateTableCoordinateSystem().get(systemA);
        TypeConverter sysB = Main.getTranslateTableCoordinateSystem().get(systemB);
        return Converter.convertPSKtoPSK(x, y, z,
                sysA.getDeltaX() - sysB.getDeltaX(),
                sysA.getDeltaY() - sysB.getDeltaY(),
                sysA.getDeltaZ() - sysB.getDeltaZ(),
                sysA.getOmegaX() - sysB.getOmegaX(),
                sysA.getOmegaY() - sysB.getOmegaY(),
                sysA.getOmegaZ() - sysB.getOmegaZ(),
                sysA.getM() - sysB.getM());
    }

    private double[] convertGSK(String systemA, String systemB, double b, double l, double h) {
        TypeConverter sysA = Main.getTranslateTableCoordinateSystem().get(systemA);
        TypeConverter sysB = Main.getTranslateTableCoordinateSystem().get(systemB);
        return Converter.convertGSKtoGSK(b, l, h,
                sysA.getDeltaX() - sysB.getDeltaX(),
                sysA.getDeltaY() - sysB.getDeltaY(),
                sysA.getDeltaZ() - sysB.getDeltaZ(),
                (sysA.getOmegaX() - sysB.getOmegaX()) * TypeConverter.PARAMSECTORAD,
                (sysA.getOmegaY() - sysB.getOmegaY()) * TypeConverter.PARAMSECTORAD,
                (sysA.getOmegaZ() - sysB.getOmegaZ()) * TypeConverter.PARAMSECTORAD,
                sysA.getM() - sysB.getM(),
                sysA.getA(), sysB.getA(), sysA.getE2(), sysB.getE2());
    }

    private double[] convertGSKtoPSK(String systemA, double b, double l, double h) {
        TypeConverter system = Main.getTranslateTableCoordinateSystem().get(systemA);
        return Converter.convertGSKtoPSK(b, l, h, system.getA(), system.getE2());
    }

    private double[] convertGSKtoGAUSS(String systemA, double b, double l) {
        TypeConverter system = Main.getTranslateTableCoordinateSystem().get(systemA);
        return Converter.convertGSKtoGAUSS(b, l, system.getA(), system.getE2());
    }

    private double[] convertGAUSStoGSK(String systemA, double b, double l) {
        TypeConverter system = Main.getTranslateTableCoordinateSystem().get(systemA);
        return Converter.convertGAUSStoGSK(b, l, system.getA(), system.getB(), system.getE2());
    }

}
