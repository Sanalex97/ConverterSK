import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(getADE(222.421, 244.212, 333.212, 432.221, 0, 0)));
    }

    public static double[] getADE(double xOr, double xCel, double yOr, double yCel, double hOr, double hCel) {
        double deltaX = xCel - xOr;
        double deltaY = yCel - yOr;
        double r = Math.atan(Math.abs(deltaY / deltaX)) * 180 / Math.PI; // румб
        double alfa = 0;

        if (deltaX > 0 && deltaY > 0) {
            alfa = r;
        } else if (deltaX < 0 && deltaY > 0) {
            alfa = 180 - r;
        } else if (deltaX < 0 && deltaY < 0) {
            alfa = 180 + r;
        } else if (deltaX > 0 && deltaY < 0) {
            alfa = 360 - r;
        }

        alfa = (int) (alfa / 0.06);

        double d = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        double deltaH = hCel - hOr;//превышение между двумя точками
        double umCel = 955 * deltaH / d;

        return new double[]{alfa, d, umCel};
    }
}