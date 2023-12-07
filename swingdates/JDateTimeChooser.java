package swingdates;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.prefs.Preferences;

/**
 * JDateTimeChooser is a Swing-based JFrame class for selecting date and time.
 * It includes fields for year, month, day, hour, minute, and second.
 * Users can input a specific date and time or retrieve the current date and time.
 * The selected date and time can be obtained using a callback.
 *
 * @author Pintescul Patric
 * @version 1.6
 */
public class JDateTimeChooser extends JFrame {
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";
    private static final String POS_X = "x";
    private static final String POS_Y = "y";

    private JTextField yearField;
    private JComboBox<String> monthComboBox, dayComboBox, hourComboBox, minuteComboBox, secondComboBox;
    private Preferences preferences;
    private DateTimeSelectedCallback callback;

    /**
     * Creates an EmptyBorder with the specified padding value on all sides.
     *
     * @param paddingValue The padding value for all sides.
     * @return The created EmptyBorder.
     */
    public static EmptyBorder getPadding(int paddingValue) {
        return new EmptyBorder(paddingValue, paddingValue, paddingValue, paddingValue);
    }

    /**
     * Constructs a JDateTimeChooser with the specified callback.
     *
     * @param callback The callback for date and time selection.
     */
    public JDateTimeChooser(DateTimeSelectedCallback callback) {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        preferences = Preferences.userNodeForPackage(JDateTimeChooser.class);
        loadUserDimensions();
        setupUI();
        this.callback = callback;
    }

    /**
     * Loads user preferences for frame dimensions and location.
     */
    private void loadUserDimensions() {
        int width = preferences.getInt(WIDTH_KEY, 300);
        int height = preferences.getInt(HEIGHT_KEY, 400);
        int posX = preferences.getInt(POS_X, 100);
        int posY = preferences.getInt(POS_Y, 100);
        this.setSize(width, height);
        this.setLocation(posX, posY);
    }

    /**
     * Sets up the user interface with input fields and buttons.
     */
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
        inputJPanel.setLayout(new GridLayout(3, 6));

        inputJPanel.add(new JLabel("Year"));
        yearField = new JTextField();
        inputJPanel.add(yearField);
        inputJPanel.add(new JLabel("Month"));
        monthComboBox = new JComboBox<>(generateRangeArray(1, 12));
        inputJPanel.add(monthComboBox);
        inputJPanel.add(new JLabel("Day"));
        dayComboBox = new JComboBox<>(generateRangeArray(1, 31));
        inputJPanel.add(dayComboBox);
        inputJPanel.add(new JLabel("Hour"));
        hourComboBox = new JComboBox<>(generateRangeArray(0, 23));
        inputJPanel.add(hourComboBox);
        inputJPanel.add(new JLabel("Minutes"));
        minuteComboBox = new JComboBox<>(generateRangeArray(0, 59));
        inputJPanel.add(minuteComboBox);
        inputJPanel.add(new JLabel("Seconds"));
        secondComboBox = new JComboBox<>(generateRangeArray(0, 59));
        inputJPanel.add(secondComboBox);

        JLabel dLabel = new JLabel("Datetime input");
        dLabel.setAlignmentX(CENTER_ALIGNMENT);
        dLabel.setFont(dLabel.getFont().deriveFont(Font.BOLD, 20));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        JButton validateButton = new JButton("OK");
        validateButton.addActionListener(e -> handleValidation(e));
        JButton selectNowButton = new JButton("Get current time");
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

    /**
     * Handles the validation of date and time input.
     *
     * @param e The ActionEvent triggering the validation.
     */
    private void handleValidation(ActionEvent e) {
        if(e.getActionCommand().equals("Get current time")){
            LocalDateTime now = LocalDateTime.now();
            yearField.setText(String.valueOf(now.getYear()));
            monthComboBox.setSelectedIndex(now.getMonthValue() - 1);
            dayComboBox.setSelectedIndex(now.getDayOfMonth() - 1);
            hourComboBox.setSelectedIndex(now.getHour());
            minuteComboBox.setSelectedIndex(now.getMinute());
            secondComboBox.setSelectedIndex(now.getSecond());
        }
        else{
        try {
            int selectedYear = Integer.parseInt(yearField.getText());
            int selectedMonth = Integer.parseInt(monthComboBox.getSelectedItem().toString());
            int selectedDay = Integer.parseInt(dayComboBox.getSelectedItem().toString());
            int selectedHour = Integer.parseInt(hourComboBox.getSelectedItem().toString());
            int selectedMinute = Integer.parseInt(minuteComboBox.getSelectedItem().toString());
            int selectedSecond = Integer.parseInt(secondComboBox.getSelectedItem().toString());

            LocalDate date = LocalDate.of(selectedYear, selectedMonth, selectedDay);
            LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                    date.getDayOfMonth(), selectedHour, selectedMinute, selectedSecond);

            callback.onDateSelected(dateTime);
            this.dispose();

        } catch (NumberFormatException | DateTimeException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time input", "Error", JOptionPane.ERROR_MESSAGE);
        }
        }
    }

    /**
     * Generates an array of strings representing a range of integers.
     *
     * @param start The starting value of the range.
     * @param end   The ending value of the range.
     * @return An array of strings representing the range.
     */
    private static String[] generateRangeArray(int start, int end) {
        String[] stringArray = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            stringArray[i - start] = String.valueOf(i);
        }
        return stringArray;
    }

    /**
     * Saves user preferences for frame dimensions and location.
     */
    private void saveUserDimensions() {
        preferences.putInt(WIDTH_KEY, getWidth());
        preferences.putInt(HEIGHT_KEY, getHeight());
        preferences.putInt(POS_X, getX());
        preferences.putInt(POS_Y, getY());
    }

    /**
     * Gets the selected date and time.
     *
     * @return The selected date and time, or null if input is invalid.
     */
    public LocalDateTime getSelectedDateTime() {
        try {
            int selectedYear = Integer.parseInt(yearField.getText());
            int selectedMonth = Integer.parseInt(monthComboBox.getSelectedItem().toString());
            int selectedDay = Integer.parseInt(dayComboBox.getSelectedItem().toString());
            int selectedHour = Integer.parseInt(hourComboBox.getSelectedItem().toString());
            int selectedMinute = Integer.parseInt(minuteComboBox.getSelectedItem().toString());
            int selectedSecond = Integer.parseInt(secondComboBox.getSelectedItem().toString());

            return LocalDateTime.of(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute,
                    selectedSecond);

        } catch (NumberFormatException | DateTimeException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time input", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Main method for testing the JDateTimeChooser class.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {
        JDateTimeChooser dateChooser = new JDateTimeChooser(dateTime -> {
            System.out.println(dateTime);
        });
        dateChooser.setVisible(true);
    }
}
