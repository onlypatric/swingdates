# Swing Dates Library

The Swing Dates Library provides a set of Swing-based JFrame classes for easy date and date-time selection in Java applications. The library includes `JDateChooser` for date selection and `JDateTimeChooser` for date and time selection.

## JDateChooser

### Features

- Select a specific date or retrieve the current date.
- User-friendly Swing-based UI.
- Callback mechanism for date selection.

### Example Usage

```java
import swingdates.JDateChooser;

public class MainApp {
    public static void main(String[] args) {
        JDateChooser dateChooser = new JDateChooser(date -> {
            System.out.println("Selected Date: " + date);
        });
        dateChooser.setVisible(true);
    }
}
```

## JDateTimeChooser

### Features

- Select a specific date and time or retrieve the current date and time.
- Year, month, day, hour, minute, and second input fields.
- Callback mechanism for date and time selection.

### Example basic usage

```java
import swingdates.JDateTimeChooser;

public class MainApp {
    public static void main(String[] args) {
        JDateTimeChooser dateTimeChooser = new JDateTimeChooser(dateTime -> {
            System.out.println("Selected Date and Time: " + dateTime);
        });
        dateTimeChooser.setVisible(true);
    }
}
```

## How to Use

1. Import the `swingdates` package into your project.
2. Create an instance of `JDateTimeChooser` with the appropriate callback.
3. Display the frame using the `setVisible(true)` method.

The `JDateTimeChooser` provides an extended set of input fields for both date and time. Users can select the desired year, month, day, hour, minute, and second, offering a comprehensive solution for date and time selection in your Java applications.

### Callback Mechanism

Enclose your date and time selection logic within the provided callback to handle the selected date and time. The callback receives a `LocalDateTime` object, allowing you to seamlessly integrate the chosen date and time into your application.

### Example Showcase

See the included `MainApp` class for a simple example of how to use the library for date and time selection.

```java
import swingdates.JDateTimeChooser;

public class MainApp {
    public static void main(String[] args) {
        JDateTimeChooser dateTimeChooser = new JDateTimeChooser(dateTime -> {
            System.out.println("Selected Date and Time: " + dateTime);
        });
        dateTimeChooser.setVisible(true);
    }
}
```
