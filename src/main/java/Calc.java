import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Класс, который аккумулирует в себе текущий результат вычислений,
 * а также производит математические операции.
 */
public class Calc {
    private BigDecimal currentResult;

    /**
     * Создает новый экземпляр калькулятора с некоторым начальным значением.
     *
     * @param initialValue начальное значение
     */
    public Calc(final BigDecimal initialValue) {
        this.currentResult = initialValue;
    }

    /**
     * Создает новый экземпляр калькулятора, начальное значение – 0.
     */
    public Calc() {
        this(new BigDecimal(BigInteger.ZERO).setScale(30, RoundingMode.HALF_UP));
    }

    /**
     * Производит сложение двух чисел.
     *
     * @param val число, которое нужно прибавить
     * @return результат операции
     */
    BigDecimal add(final BigDecimal val) {
        currentResult = currentResult.add(val);
        return currentResult;
    }

    /**
     * Производит вычитание двух чисел.
     *
     * @param val число, которое нужно вычесть
     * @return результат операции
     */
    BigDecimal sub(final BigDecimal val) {
        currentResult = currentResult.subtract(val);
        return currentResult;
    }

    /**
     * Производит деление двух чисел.
     *
     * @param val число, на которое нужно разделить
     * @return результат операции
     */
    BigDecimal divide(final BigDecimal val) {
        currentResult = currentResult.divide(val, 30, RoundingMode.HALF_UP);
        return currentResult;
    }

    /**
     * Производит умножение двух чисел.
     *
     * @param val число, на которое нужно умножить
     * @return результат операции
     */
    BigDecimal multiply(final BigDecimal val) {
        currentResult = currentResult.multiply(val);
        return currentResult;
    }
}
