package Converters;

public class TypeConverter {
    private int id;
    private String systemA;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private double omegaX;
    private double omegaY;
    private double omegaZ;
    private double m;
    private double a;
    private double b;
    private double e2;
    public final static double PARAMSECTORAD = 206264.806;

    public TypeConverter(double deltaX, double deltaY, double deltaZ, double omegaX, double omegaY, double omegaZ, double m, double a, double b, double e2) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.omegaX = omegaX;
        this.omegaY = omegaY;
        this.omegaZ = omegaZ;
        this.m = m;
        this.a = a;
        this.b = b;
        this.e2 = e2;
    }

    public TypeConverter() {
    }

    public int getId() {
        return id;
    }

    public String getSystemA() {
        return systemA;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getOmegaX() {
        /*return (omegaX / 1000) * 4.85 / 1_000_000 ;*/
        return omegaX / PARAMSECTORAD;
    }

    public double getOmegaY() {
       /* return (omegaY / 1000) * 4.85 / 1_000_000;*/
        return omegaY / PARAMSECTORAD;
    }

    public double getOmegaZ() {
        /*return (omegaZ / 1000) * 4.85 / 1_000_000;*/
        return omegaZ / PARAMSECTORAD;
    }

    public double getM() {
        return m / 1000000;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getE2() {
        return e2;
    }


    @Override
    public String toString() {
        return "TypeConverter{" +
                "id=" + id +
                ", systemA='" + systemA + '\'' +
                ", deltaX=" + deltaX +
                ", deltaY=" + deltaY +
                ", deltaZ=" + deltaZ +
                ", omegaX=" + omegaX +
                ", omegaY=" + omegaY +
                ", omegaZ=" + omegaZ +
                ", m=" + m +
                '}';
    }
}
