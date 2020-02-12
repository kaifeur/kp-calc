import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

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
    public static final String ABOUT_ICON_PATH = "/icon/about_icon.png";
    private static final Dimension windowDimension = new Dimension(400, 300);
    private static final Dimension fieldsDimension = new Dimension(400, 50);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private BoxLayout mainLayout;
    private Calc calc;
    private JTextField currentNumberField;
    private JLabel clickCount;
    private String lastOperation = EMPTY_STRING;
    private JTextField tempResultField;
    final ActionListener calcBtnAListener = e -> {
        final JButton button = (JButton) e.getSource();
        updateClickCount();
        handleButtonClick(button.getText());
    };

    public CalcWindow() throws HeadlessException {
        super("Calculator");
        setMinimumSize(windowDimension);
        calc = new Calc();
        initGUI();
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalcWindow::new);
    }

    static void showErrorDialog(final String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static void showInfoDialog(final String message) {
        JOptionPane.showMessageDialog(null, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

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
//        tempResPanel.setMaximumSize(fieldsDimension);
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
//        currentNumberPanel.setMaximumSize(fieldsDimension);
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
        constraints.weightx = 0;
        constraints.gridwidth = 4;
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonPanel.add(clearButton, constraints);

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

    private void handleButtonClick(final String command) {
        if (command == null || command.length() != 1) {
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

    private void handleDigitButton(final String digit) {
        if (lastOperation.equals(CALCULATE)) {
            clearAll();
        }
        currentNumberField.setText(currentNumberField.getText().concat(String.valueOf(digit)));
    }

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

        lastOperation = operation;

        if (!isFieldEmpty(currentNumberField)
                && isFieldEmpty(tempResultField)
                && !currentNumberField.equals(SUBTRACT)) {
            tempResultField.setText(currentNumberField.getText());
            currentNumberField.setText(EMPTY_STRING);
            calc = new Calc(new BigDecimal(tempResultField.getText()));
        }
    }

    private boolean isFieldEmpty(JTextField currentNumberField) {
        return currentNumberField.getText().isEmpty();
    }

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
                return;
        }
        updateResult(resultValue);
    }

    private void updateClickCount() {
        clickCount.setText(String.valueOf(Integer.parseInt(clickCount.getText()) + 1));
    }

    private void updateResult(final BigDecimal val) {
        final String value = val.stripTrailingZeros().toPlainString();
        tempResultField.setText(value);
        currentNumberField.setText(EMPTY_STRING);
    }

    private void clearCurrentNumber() {
        currentNumberField.setText(EMPTY_STRING);
    }

    private void clearAll() {
        lastOperation = EMPTY_STRING;
        tempResultField.setText(EMPTY_STRING);
        clearCurrentNumber();
        calc = new Calc();
    }
}
