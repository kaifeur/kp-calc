import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Класс представляет собой окно калькулятора,
 * в котором реализована вся логика управления и расчета.
 */
public class CalcWindow extends JFrame {
    public static final String DIVIDE = "/";
    public static final String MULTIPLY = "*";
    public static final String SUBTRACT = "-";
    public static final String ADD = "+";
    public static final String DOT = ".";
    public static final String CALCULATE = "=";
    public static final String ZERO = "0";
    public static final String EMPTY_STRING = "";
    public static final String CLEAR = "C";
    public static final String CHANGE_SIGN = "+/-";

    public static final String ABOUT_ICON_PATH = "/icon/about_icon.png";
    private static final Dimension windowDimension = new Dimension(400, 300);

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Обработчик нажатий на кнопки калькулятора,
     * инкрементирует счетчик нажатий и переходит в обработку нажатия.
     */
    private final ActionListener calcBtnAListener = e -> {
        final JButton button = (JButton) e.getSource();
        updateClickCount();
        handleButtonClick(button.getText());
    };

    private BoxLayout mainLayout;
    private Calc calc;
    private JTextField currentNumberField;
    private JLabel clickCount;
    private String lastOperation = EMPTY_STRING;
    private JTextField tempResultField;

    /**
     * Конструктор класса. Устанавливает минимальный размер окна,
     * создает экземпляр класса {@link Calc}.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     */
    public CalcWindow() throws HeadlessException {
        super("Calculator");
        setMinimumSize(windowDimension);
        calc = new Calc();
        initGUI();
        pack();
        setVisible(true);
    }

    /**
     * Точка входа в программу.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalcWindow::new);
    }

    /**
     * Отображает диалогове окно при возникновении ошибки.
     *
     * @param message сообщение, которое нужно отобразить
     */
    static void showErrorDialog(final String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Отображает информационное диалоговое окно.
     *
     * @param message сообщение, которое нужно отобразить
     */
    static void showInfoDialog(final String message) {
        JOptionPane.showMessageDialog(null, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Иницииализирует интерфейс калькулятора,
     * создаются все элементы интерфейса, вешаются обработчики на все кнопки.
     */
    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final var contentPane = getContentPane();
        mainLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(mainLayout);

        final JLabel tempResultLabel = new JLabel("Intermediate result:");
        tempResultField = new JTextField();
        tempResultField.setHorizontalAlignment(SwingConstants.RIGHT);
        tempResultField.setEditable(false);

        final JPanel tempResPanel = new JPanel();
        tempResPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final BoxLayout tempResBoxLayout = new BoxLayout(tempResPanel, BoxLayout.Y_AXIS);
        tempResPanel.setLayout(tempResBoxLayout);
        tempResPanel.add(tempResultLabel);
        tempResPanel.add(tempResultField);
        contentPane.add(tempResPanel);

        final JLabel currentNumberLabel = new JLabel("Enter a number:");
        currentNumberField = new JTextField();
        currentNumberField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentNumberField.setEditable(false);

        final JPanel currentNumberPanel = new JPanel();
        currentNumberPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final BoxLayout currentNumberBoxLayout = new BoxLayout(currentNumberPanel, BoxLayout.Y_AXIS);
        currentNumberPanel.setLayout(currentNumberBoxLayout);
        currentNumberPanel.add(currentNumberLabel);
        currentNumberPanel.add(currentNumberField);
        contentPane.add(currentNumberPanel);

        final GridLayout digitLayout = new GridLayout(4, 3);
        final JPanel digitPanel = new JPanel();
        digitPanel.setLayout(digitLayout);

        for (int i = 2; i > -1; i--) {
            for (int j = i * 3 + 1; j < i * 3 + 4; j++) {
                final JButton digitButton = new JButton(String.valueOf(j));
                digitButton.addActionListener(calcBtnAListener);
                digitPanel.add(digitButton);
            }
        }

        final JButton btnZero = new JButton(ZERO);
        btnZero.addActionListener(calcBtnAListener);
        final JButton btnComma = new JButton(DOT);
        btnComma.addActionListener(calcBtnAListener);
        final JButton btnCalc = new JButton(CALCULATE);
        btnCalc.addActionListener(calcBtnAListener);
        digitPanel.add(btnZero);
        digitPanel.add(btnComma);
        digitPanel.add(btnCalc);

        final GridLayout opersLayout = new GridLayout(4, 1);
        final JPanel opersPanel = new JPanel();
        opersPanel.setLayout(opersLayout);

        final ArrayList<JButton> operBtnList = new ArrayList<>(4);
        operBtnList.add(new JButton(DIVIDE));
        operBtnList.add(new JButton(MULTIPLY));
        operBtnList.add(new JButton(SUBTRACT));
        operBtnList.add(new JButton(ADD));

        operBtnList.forEach(b -> {
            b.addActionListener(calcBtnAListener);
            opersPanel.add(b);
        });

        final JPanel buttonPanel = new JPanel();
        final GridBagLayout buttonsLayout = new GridBagLayout();
        buttonPanel.setLayout(buttonsLayout);
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.75;
        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonPanel.add(digitPanel, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.25;
        constraints.gridx = 1;
        buttonPanel.add(opersPanel, constraints);

        final JButton clearButton = new JButton(CLEAR);
        clearButton.addActionListener(calcBtnAListener);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.75;
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonPanel.add(clearButton, constraints);

        final JButton changeSignButton = new JButton(CHANGE_SIGN);
        changeSignButton.addActionListener(calcBtnAListener);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.25;
        constraints.gridx = 1;
        constraints.gridy = 1;
        buttonPanel.add(changeSignButton, constraints);

        contentPane.add(buttonPanel);

        final JLabel clickCountDesc = new JLabel("Click count: ");
        clickCount = new JLabel(ZERO);

        final JPanel clickPanel = new JPanel();
        final BoxLayout clickLayout = new BoxLayout(clickPanel, BoxLayout.X_AXIS);
        clickPanel.setLayout(clickLayout);
        clickPanel.add(clickCountDesc);
        clickPanel.add(clickCount);

        contentPane.add(clickPanel);

        ImageIcon aboutIcon = null;
        try {
            aboutIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(ABOUT_ICON_PATH)));
            aboutIcon.setImage(aboutIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            logger.error("Can't load about icon", e);
        }

        final JMenuItem aboutMenuItem = new JMenuItem("About...");
        if (aboutIcon != null) {
            aboutMenuItem.setIcon(aboutIcon);
        }

        aboutMenuItem.addActionListener(e -> showInfoDialog("Author:\nNikolai Rubtsov,\n2020"));

        final JMenu helpMenu = new JMenu("Help");
        helpMenu.add(aboutMenuItem);

        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Обрабатывает нажатия на кнопки калькулятора, принимающий на вход строку-команду,
     * и в зависимости от типа кнопки перенаправляет вызов далее.
     *
     * @param command команда (кнопка) в строковом формате
     */
    private void handleButtonClick(final String command) {
        if (command == null || command.length() != 1 && !command.equals(CHANGE_SIGN)) {
            logger.error("Wrong command (length must be == 1)");
            throw new IllegalArgumentException("Wrong command!");
        }
        try {
            Integer.parseInt(command);
            handleDigitButton(command);
        } catch (NumberFormatException e) {
            handleOperationButton(command);
        }
    }

    /**
     * Обрабатывает нажатие на кнопки цифр.
     *
     * @param digit нажатая цифра в строковом формате
     */
    private void handleDigitButton(final String digit) {
        if (lastOperation.equals(CALCULATE)) {
            clearAll();
        }
        if (digit.equals(ZERO)
                && (currentNumberField.getText().equals(ZERO)
                || currentNumberField.getText().equals(SUBTRACT + ZERO))) {
            return;
        }
        currentNumberField.setText(currentNumberField.getText().concat(digit));
    }

    /**
     * Обрабатывает нажатие на кнопки операций, а также символа-разделителя (.).
     * Тут реализована основная логика приложения – обработка последовательности
     * нажатий, порядка выполнения операций.
     *
     * @param operation операция для выполнения в строковом формате
     */
    private void handleOperationButton(final String operation) {
        if (operation.equals(CALCULATE)) {
            if (!isFieldEmpty(currentNumberField) && (
                    lastOperation.equals(ADD)
                            || lastOperation.equals(SUBTRACT)
                            || lastOperation.equals(MULTIPLY)
                            || lastOperation.equals(DIVIDE))) {
                performOperation(lastOperation);
                lastOperation = CALCULATE;
            } else {
                logger.error("You are trying to perform illegal operation! NO.");
                showErrorDialog("What do you want???");
            }
            return;
        }

        if (operation.equals(DOT)) {
            if (!currentNumberField.getText().contains(DOT)) {
                if (lastOperation.equals(CALCULATE)) {
                    clearAll();
                }
                currentNumberField.setText(currentNumberField.getText().concat(DOT));
            }
            return;
        }

        if (operation.equals(CLEAR)) {
            if (isFieldEmpty(currentNumberField)) {
                clearAll();
            } else {
                clearCurrentNumber();
            }
            return;
        }

        if (operation.equals(SUBTRACT) && isFieldEmpty(currentNumberField) && isFieldEmpty(tempResultField)) {
            currentNumberField.setText(currentNumberField.getText().concat(SUBTRACT));
            return;
        }

        if (operation.equals(CHANGE_SIGN)) {
            if (currentNumberField.getText().contains(SUBTRACT)) {
                currentNumberField.setText(currentNumberField.getText().replace(SUBTRACT, EMPTY_STRING));
            } else {
                currentNumberField.setText(SUBTRACT + currentNumberField.getText());
            }
            return;
        }

        lastOperation = operation;

        if (!isFieldEmpty(currentNumberField)
                && isFieldEmpty(tempResultField)
                && !currentNumberField.getText().equals(SUBTRACT)) {
            tempResultField.setText(currentNumberField.getText());
            currentNumberField.setText(EMPTY_STRING);
            calc = new Calc(new BigDecimal(tempResultField.getText()));
        }
    }

    /**
     * Util-метод, проверящий любое JTextField поле на наличие текста.
     *
     * @param currentNumberField поле для проверки
     * @return true/false в зависимости от наличия текста
     */
    private boolean isFieldEmpty(JTextField currentNumberField) {
        return currentNumberField.getText().isEmpty();
    }

    /**
     * Производит выполнение одной из математических операций (/, *, -, +).
     * Перенаправляет вызов в экземпляр класса Calc, после обновляет поле результата.
     *
     * @param operation математическая операция, которую необходимо выполнить
     */
    private void performOperation(final String operation) {
        final BigDecimal resultValue;
        switch (operation) {
            case DIVIDE:
                if (new BigDecimal(currentNumberField.getText()).compareTo(BigDecimal.ZERO) == 0) {
                    logger.error("Division by zero");
                    showErrorDialog("Division by zero???");
                    return;
                }
                resultValue = calc.divide(new BigDecimal(currentNumberField.getText()));
                break;
            case MULTIPLY:
                resultValue = calc.multiply(new BigDecimal(currentNumberField.getText()));
                break;
            case SUBTRACT:
                resultValue = calc.sub(new BigDecimal(currentNumberField.getText()));
                break;
            case ADD:
                resultValue = calc.add(new BigDecimal(currentNumberField.getText()));
                break;
            default:
                logger.error("You are trying to perform illegal operation! NO.");
                lastOperation = EMPTY_STRING;
                return;
        }
        updateResult(resultValue);
    }

    /**
     * Обновляет текст в поле {@link CalcWindow#tempResultField} (поле результата).
     * Перед обновлением округляет значение до 15 знаков после запятой.
     *
     * @param val значение, которым необходимо заменить результат
     */
    private void updateResult(final BigDecimal val) {
        final String value = val.setScale(15, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        tempResultField.setText(value);
        currentNumberField.setText(EMPTY_STRING);
    }

    /**
     * Метод инкрементации счетчика нажатий на кнопки калькулятора.
     */
    private void updateClickCount() {
        clickCount.setText(String.valueOf(Integer.parseInt(clickCount.getText()) + 1));
    }

    /**
     * Выполняет очистку поля текущего числа.
     */
    private void clearCurrentNumber() {
        currentNumberField.setText(EMPTY_STRING);
    }

    /**
     * Сбрасывает калькулятор в начальное состояние.
     */
    private void clearAll() {
        lastOperation = EMPTY_STRING;
        tempResultField.setText(EMPTY_STRING);
        clearCurrentNumber();
        calc = new Calc();
    }
}
