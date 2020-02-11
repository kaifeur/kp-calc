import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CalcWindow extends JFrame {
    private static final Dimension windowDimension = new Dimension(300, 300);
    private static final Dimension fieldsDimension = new Dimension(300, 50);

    private BoxLayout mainLayout;

    public CalcWindow() throws HeadlessException {
        super("Calculator");
        setMinimumSize(windowDimension);
        initGUI();
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalcWindow());
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final var contentPane = getContentPane();
        mainLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(mainLayout);

        final JLabel tempResultLabel = new JLabel("Intermediate result:");
        final JTextField tempResultField = new JTextField();
        tempResultField.setHorizontalAlignment(SwingConstants.RIGHT);
        tempResultField.setEditable(false);

        final JPanel tempResPanel = new JPanel();
        tempResPanel.setMaximumSize(fieldsDimension);
        final BoxLayout tempResBoxLayout = new BoxLayout(tempResPanel, BoxLayout.Y_AXIS);
        tempResPanel.setLayout(tempResBoxLayout);
        tempResPanel.add(tempResultLabel);
        tempResPanel.add(tempResultField);
        contentPane.add(tempResPanel);

        final JLabel currentNumberLabel = new JLabel("Enter a number:");
        final JTextField currentNumberField = new JTextField();
        currentNumberField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentNumberField.setEditable(false);

        final JPanel currentNumberPanel = new JPanel();
        currentNumberPanel.setMaximumSize(fieldsDimension);
        final BoxLayout currentNumberBoxLayout = new BoxLayout(currentNumberPanel, BoxLayout.Y_AXIS);
        currentNumberPanel.setLayout(currentNumberBoxLayout);
        currentNumberPanel.add(currentNumberLabel);
        currentNumberPanel.add(currentNumberField);
        contentPane.add(currentNumberPanel);

        final GridLayout digitLayout = new GridLayout(4, 3);
        final JPanel digitPanel = new JPanel();
        digitPanel.setLayout(digitLayout);

        final ArrayList<JButton> buttonList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            buttonList.add(new JButton(String.valueOf(i)));
            digitPanel.add(buttonList.get(i));
        }

        final JButton btnComma = new JButton(",");
        final JButton btnCalc = new JButton("=");
        digitPanel.add(btnComma);
        digitPanel.add(btnCalc);

        final GridLayout opersLayout = new GridLayout(4, 1);
        final JPanel opersPanel = new JPanel();
        opersPanel.setLayout(opersLayout);

        final JButton btnDiv = new JButton("/");
        final JButton btnMultiply = new JButton("*");
        final JButton btnSub = new JButton("-");
        final JButton btnAdd = new JButton("+");

        opersPanel.add(btnDiv);
        opersPanel.add(btnMultiply);
        opersPanel.add(btnSub);
        opersPanel.add(btnAdd);

        final JPanel buttonPanel = new JPanel();
        final GridBagLayout buttonsLayout = new GridBagLayout();
        buttonPanel.setLayout(buttonsLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.75;
        constraints.gridy = 0;
        constraints.gridx = 0;      // нулевая ячейка таблицы по горизонтали
        buttonPanel.add(digitPanel, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.25;
        constraints.gridx = 1;      // нулевая ячейка таблицы по горизонтали
        buttonPanel.add(opersPanel, constraints);

        contentPane.add(buttonPanel);

        final JLabel clickCountDesc = new JLabel("Click count: ");
        final JLabel clickCount = new JLabel("0");

        final JPanel clickPanel = new JPanel();
        final BoxLayout clickLayout = new BoxLayout(clickPanel, BoxLayout.X_AXIS);
        clickPanel.setLayout(clickLayout);
        clickPanel.add(clickCountDesc);
        clickPanel.add(clickCount);

        contentPane.add(clickPanel);
    }
}
