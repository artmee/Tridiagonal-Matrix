import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        //userInput();

        Uxx(100);

    }

    private static void Uxx(int n){
        MathContext context = new MathContext(3, RoundingMode.HALF_UP);

        double h = 1.0 / n;
        double[] x = new double[n + 1];
        System.out.print("Аргумент: \t");
        for (int i = 0; i <= n; i++) {
            //Аргументы
            x[i] = h * i;
            BigDecimal result = new BigDecimal(x[i], context);
            System.out.printf("%6.3f \t\t", result);
        }
        System.out.println();

        double[] solution = new double[n + 1];
        solution[0] = 0;
        solution[n] = 0;
        for (int i = 1; i < n; i++) {
            //Точные решения
            solution[i] = x[i] * (x[i] - 1);
        }
        System.out.print("Точное: \t");
        for (int i = 0; i <= n; i++) {
            BigDecimal result = new BigDecimal(solution[i], context);
            System.out.printf("%6.3f \t\t", result);
        }
        System.out.println();

        int count = n - 1;

        Double[] A = new Double[count];
        Double[] C = new Double[count];
        Double[] B = new Double[count];
        Double[] F = new Double[count];

        for (int i = 0; i < count; i++) {
            A[i] = 1.0;
            C[i] = -2.0;
            B[i] = 1.0;
            F[i] = 2 * h * h;
        }

        double[] algoCut = Algorithm.calculate(A, B, C, F, count);
        double[] algo = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            if (i == 0 || i == n) {
                algo[i] = 0.0;
                continue;
            }
            //Решения методом прогонки
            algo[i] = algoCut[i - 1];
        }

        System.out.print("Прогонка: \t");
        for (int i = 0; i <= n; i++) {
            BigDecimal result = new BigDecimal(algo[i], context);
            System.out.printf("%6.3f \t\t", result);
        }
        System.out.println();

        System.out.println("Решения совпадают: " + check(solution, algo, n, 11));

        for (int i = 0; i <= n; i++) {
            //System.out.println(x[i] +"\t"+ solution[i] +"\t"+ algo[i]);
            System.out.printf(Locale.FRANCE, "%f\t%f\t%f\n", x[i], solution[i], algo[i]);
        }
    }

    private static void userInput() {
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
    }

    private static boolean check(double[] a, double[] b, int n, int precision){
        MathContext context = new MathContext(precision, RoundingMode.HALF_UP);

        for (int i = 0; i < n; i++) {
            BigDecimal ai = new BigDecimal(a[i], context);
            BigDecimal bi = new BigDecimal(b[i], context);

            if(ai.compareTo(bi) != 0){
                return false;
            }
        }
        return true;
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
                B[i] = scannedNumbers[1];
                continue;
            }

            if (i == (n - 1)) {
                double[] scannedNumbers = scanNumberLine("Два элемента строки #" + (i + 1) + ": ", 2, scanner);
                A[i] = scannedNumbers[0];
                C[i] = scannedNumbers[1];
                continue;
            }

            double[] scannedNumbers = scanNumberLine("Три элемента строки #" + (i + 1) + ": ", 3, scanner);
            A[i] = scannedNumbers[0];
            C[i] = scannedNumbers[1];
            B[i] = scannedNumbers[2];
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
                System.out.printf("%6.2f ", B[i]);
            } else if (i == (n - 1)) {
                System.out.printf("%6.2f ", A[i]);
                System.out.printf("%6.2f ", C[i]);
                restCount += 1;
            } else {
                System.out.printf("%6.2f ", A[i]);
                System.out.printf("%6.2f ", C[i]);
                System.out.printf("%6.2f ", B[i]);
            }

            for (int j = 0; j < restCount; j++) {
                System.out.printf("%6.2f ", 0.0);
            }

            System.out.println(" | " + F[i]);
        }
    }
}