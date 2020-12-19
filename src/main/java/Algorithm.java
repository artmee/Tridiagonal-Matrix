public class Algorithm {

    //Метод прогонки
    public static double[] calculate(Double[] A, Double[] B, Double[] C, Double[] F, int n) {
        double[] alpha = new double[n];
        double[] betta = new double[n];

        A[0] = 0.0;
        B[n-1] = 0.0;

        for (int i = 0; i < n; i++) {
            A[i] *= -1;
            B[i] *= -1;
        }

        alpha[0] = B[0] / C[0];
        betta[0] = F[0] / C[0];

        for (int i = 1; i < n; i++) {
            alpha[i] = B[i] / (C[i] - alpha[i - 1] * A[i]);
            betta[i] = (F[i] + betta[i - 1] * A[i]) / (C[i] - alpha[i - 1] * A[i]);
        }

        double[] result = new double[n];
        result[n - 1] = betta[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            result[i] = alpha[i] * result[i + 1] + betta[i];
        }

        return result;
    }
}
