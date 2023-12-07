package swingdates;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.prefs.Preferences;

/**
 * JDateChooser is a Swing-based JFrame class for selecting a date.
 * Users can input a specific date or retrieve the current date.
 * The selected date can be obtained using a callback.
 *
 * @author Pintescul Patric
 * @version 1.6
 */
public class JDateChooser extends JFrame {
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";
    private static final String POS_X = "x";
    private static final String POS_Y = "y";

    private JTextField yearField;
    private JComboBox<String> monthComboBox, dayComboBox;
    private Preferences preferences;
    private DateSelectedCallback callback;

    /**
     * Creates an EmptyBorder with the specified padding value on all sides.
     *
     * @param paddingValue The padding value for all sides.
     * @return The created EmptyBorder.
     */
    public static EmptyBorder getPadding(int paddingValue) {
        return new EmptyBorder(paddingValue, paddingValue, paddingValue, paddingValue);
    }

    public void setCallback(DateSelectedCallback callback) {
        this.callback = callback;
    }

    public JDateChooser(DateSelectedCallback callback) {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        preferences = Preferences.userNodeForPackage(JDateChooser.class);
        loadUserDimensions();
        setupUI();
        this.callback = callback;
    }

    private void loadUserDimensions() {
        int width = preferences.getInt(WIDTH_KEY, 300);
        int height = preferences.getInt(HEIGHT_KEY, 400);
        int posX = preferences.getInt(POS_X, 100);
        int posY = preferences.getInt(POS_Y, 100);
        this.setSize(width, height);
        this.setLocation(posX, posY);
    }

    private void setupUI() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveUserDimensions();
                System.exit(0);
            }
        });

        JPanel inputPanelContainer = new JPanel();
        inputPanelContainer.setBorder(getPadding(10));
        inputPanelContainer.setLayout(new BoxLayout(inputPanelContainer, BoxLayout.PAGE_AXIS));

        JPanel inputJPanel = new JPanel();
        inputJPanel.setLayout(new GridLayout(3, 1));

        inputJPanel.add(new JLabel("Year"));
        yearField = new JTextField();
        inputJPanel.add(yearField);
        inputJPanel.add(new JLabel("Month"));
        monthComboBox = new JComboBox<>(generateRangeArray(1, 12));
        inputJPanel.add(monthComboBox);
        inputJPanel.add(new JLabel("Day"));
        dayComboBox = new JComboBox<>(generateRangeArray(1, 31));
        inputJPanel.add(dayComboBox);

        JLabel dLabel = new JLabel("Date input");
        dLabel.setAlignmentX(CENTER_ALIGNMENT);
        dLabel.setFont(dLabel.getFont().deriveFont(Font.BOLD, 20));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        JButton validateButton = new JButton("OK");
        validateButton.addActionListener(e -> handleValidation(e));
        JButton selectNowButton = new JButton("Get current date");
        selectNowButton.addActionListener(e -> handleValidation(e));

        validateButton.setAlignmentX(CENTER_ALIGNMENT);

        inputPanelContainer.add(dLabel);
        inputPanelContainer.add(inputJPanel);
        buttonsPanel.add(selectNowButton);
        buttonsPanel.add(validateButton);
        inputPanelContainer.add(buttonsPanel);

        this.getContentPane().add(inputPanelContainer);
        this.pack();
    }

    private void handleValidation(ActionEvent e) {
        if (e.getActionCommand().equals("Get current date")) {
            LocalDate now = LocalDate.now();
            yearField.setText(String.valueOf(now.getYear()));
            monthComboBox.setSelectedIndex(now.getMonthValue() - 1);
            dayComboBox.setSelectedIndex(now.getDayOfMonth() - 1);
        } else {
            try {
                int selectedYear = Integer.parseInt(yearField.getText());
                int selectedMonth = Integer.parseInt(monthComboBox.getSelectedItem().toString());
                int selectedDay = Integer.parseInt(dayComboBox.getSelectedItem().toString());

                LocalDate date = LocalDate.of(selectedYear, selectedMonth, selectedDay);

                callback.onDateSelected(date);
                this.dispose();

            } catch (NumberFormatException | DateTimeException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String[] generateRangeArray(int start, int end) {
        String[] stringArray = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            stringArray[i - start] = String.valueOf(i);
        }
        return stringArray;
    }

    private void saveUserDimensions() {
        preferences.putInt(WIDTH_KEY, getWidth());
        preferences.putInt(HEIGHT_KEY, getHeight());
        preferences.putInt(POS_X, getX());
        preferences.putInt(POS_Y, getY());
    }

    public LocalDate getSelectedDate() {
        try {
            int selectedYear = Integer.parseInt(yearField.getText());
            int selectedMonth = Integer.parseInt(monthComboBox.getSelectedItem().toString());
            int selectedDay = Integer.parseInt(dayComboBox.getSelectedItem().toString());

            return LocalDate.of(selectedYear, selectedMonth, selectedDay);

        } catch (NumberFormatException | DateTimeException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date input", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        JDateChooser dateChooser = new JDateChooser(date -> {
            System.out.println(date);
        });
        dateChooser.setVisible(true);
    }
}
