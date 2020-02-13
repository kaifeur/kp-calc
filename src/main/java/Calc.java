import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Calc {
    private BigDecimal currentResult;

    public Calc(final BigDecimal initialValue) {
        this.currentResult = initialValue;
    }

    public Calc() {
        this(new BigDecimal(BigInteger.ZERO).setScale(30, RoundingMode.HALF_UP));
    }

    BigDecimal add(final BigDecimal val) {
        currentResult = currentResult.add(val);
        return currentResult;
    }

    BigDecimal sub(final BigDecimal val) {
        currentResult = currentResult.subtract(val);
        return currentResult;
    }

    BigDecimal divide(final BigDecimal val) {
        currentResult = currentResult.divide(val, 30, RoundingMode.HALF_UP);
        return currentResult;
    }

    BigDecimal multiply(final BigDecimal val) {
        currentResult = currentResult.multiply(val);
        return currentResult;
    }
}
