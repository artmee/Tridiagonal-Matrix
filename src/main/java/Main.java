import java.util.Scanner;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Решение системы линейных уравнений вида Ax = F");

            System.out.println("Введите размер трехдиагональной матрицы A:");

            int n = (int) scanNumber("n = ", x -> x > 0,
                    "Размер n должен быть положительным числом, введите еще раз.", scanner);

            Double[] A = new Double[n];
            Double[] B = new Double[n];
            Double[] C = new Double[n];

            initMatrixA(A, B, C, n, scanner);


            Double[] F = new Double[n];
            System.out.println("Введите значения элементов столбца F");
            for (int i = 0; i < n; i++) {
                F[i] = scanNumber("F" + (i + 1) + " = ", x -> true, "Введите число", scanner);
            }

            System.out.println("Получившееся система:");
            printMatrix(A, B, C, F, n);

            System.out.println("Решение:");
            double[] x = Algorithm.calculate(A, B, C, F, n);
            for (int i = 0; i < n; i++) {
                System.out.println("X" + (i + 1) + " = " + x[i]);
            }
        }

/*
        int n = 5;
        Double[] A = {null, 3.0, 5.0, 6.00, 5.00};
        Double[] C = {2.0, 8.0, 12.0, 18.0, 10.0};
        Double[] B = {1.0, 1.0, -2.0, 4.0, null};
        Double[] F = {-25.0, 72.0, -69.0, -156.0, 20.0};

        System.out.println("Решение:");
        double[] x = Algorithm.calculate(A, B, C, F, n);
        MathContext context = new MathContext(4, RoundingMode.HALF_UP);
        for (int i = 0; i < n; i++) {
            BigDecimal result = new BigDecimal(x[i], context);
            System.out.println("X" + (i + 1) + " = " + result);
        }
*/

    }

    private static double scanNumber(String numberName, Predicate<Double> predicate,
                                     String errorMessage, Scanner scanner) {
        Double result = null;
        System.out.print(numberName);

        try {
            while (result == null) {
                result = Double.parseDouble(scanner.nextLine());
                if (!predicate.test(result)) {
                    throw new RuntimeException();
                }
            }
        } catch (RuntimeException exception) {
            System.out.println("WARNING: " + errorMessage);
            result = scanNumber(numberName, predicate, errorMessage, scanner);
        }

        return result;
    }

    private static double[] scanNumberLine(String lineName, int numberCount, Scanner scanner) {

        System.out.print(lineName);
        String line = scanner.nextLine();
        double[] result;

        try {
            String[] numbers = line.split(" ");
            if (numbers.length != numberCount) {
                throw new RuntimeException();
            }
            result = new double[numberCount];

            for (int i = 0; i < numberCount; i++) {
                result[i] = Double.parseDouble(numbers[i]);
            }
        } catch (RuntimeException exception) {
            System.out.println("WARNING: Введите " + numberCount + " числа!");
            result = scanNumberLine(lineName, numberCount, scanner);
        }

        return result;
    }

    private static void initMatrixA(Double[] A, Double[] B, Double[] C, int n, Scanner scanner) {
        System.out.println("Введите значения элементов строк матрицы A через пробел");
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                double[] scannedNumbers = scanNumberLine("Два элемента строки #" + (i + 1) + ": ", 2, scanner);
                C[i] = scannedNumbers[0];
                B[i] = scannedNumbers[1] * -1;
                continue;
            }

            if (i == (n - 1)) {
                double[] scannedNumbers = scanNumberLine("Два элемента строки #" + (i + 1) + ": ", 2, scanner);
                A[i] = scannedNumbers[0] * -1;
                C[i] = scannedNumbers[1];
                continue;
            }

            double[] scannedNumbers = scanNumberLine("Три элемента строки #" + (i + 1) + ": ", 3, scanner);
            A[i] = scannedNumbers[0] * -1;
            C[i] = scannedNumbers[1];
            B[i] = scannedNumbers[2] * -1;
        }
    }

    private static void printMatrix(Double[] A, Double[] B, Double[] C, Double[] F, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i - 1; j++) {
                System.out.printf("%6.2f ", 0.0);
            }

            int restCount = n - i - 2;
            if (i == 0) {
                System.out.printf("%6.2f ", C[i]);
                System.out.printf("%6.2f ", B[i] * -1);
            } else if (i == (n - 1)) {
                System.out.printf("%6.2f ", A[i] * -1);
                System.out.printf("%6.2f ", C[i]);
                restCount += 1;
            } else {
                System.out.printf("%6.2f ", A[i] * -1);
                System.out.printf("%6.2f ", C[i]);
                System.out.printf("%6.2f ", B[i] * -1);
            }

            for (int j = 0; j < restCount; j++) {
                System.out.printf("%6.2f ", 0.0);
            }

            System.out.println(" | " + F[i]);
        }
    }
}
