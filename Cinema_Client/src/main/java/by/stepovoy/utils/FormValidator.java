package by.stepovoy.utils;

import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.regex.Pattern;

public class FormValidator {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Border greenBorder = BorderFactory.createLineBorder(Color.GREEN);
    private static final Border redBorder = BorderFactory.createLineBorder(Color.RED);

    public static boolean checkUserFields(JDatePickerImpl dateField, JTextField... fields) {
        boolean result = true;
        dateField.setBorder(greenBorder);
        for (JTextField field : fields) {
            if (field != null) {
                field.setBorder(greenBorder);
            }
        }
        result = checkFields(fields);
        if (dateField.getModel().getValue() == null || dateField.getModel().getValue().toString().isEmpty()) {
            dateField.setBorder(redBorder);
            result = false;
        }
        if (!fields[1].getText().equals(fields[2].getText())) {
            fields[1].setBorder(redBorder);
            fields[2].setBorder(redBorder);
            result = false;
        }
        if (!EMAIL_REGEX.matcher(fields[5].getText()).find()) {
            fields[5].setBorder(redBorder);
            result = false;
        }
        int[] sizes = {15, 15, 15, 15, 30};
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().length() > sizes[i]) {
                fields[i].setBorder(redBorder);
                result = false;
            }
        }
        return result;
    }

    public static boolean checkSeanceFields(JComboBox comboBox, JDatePickerImpl dateField,
                                            JFormattedTextField timeField, JTextField costField) {
        boolean result = true;
        if (comboBox.getSelectedIndex() == 0) {
            comboBox.setBorder(redBorder);
        } else {
            comboBox.setBorder(greenBorder);
        }
        if (dateField.getModel().getValue() == null || dateField.getModel().getValue().toString().isEmpty()) {
            dateField.setBorder(redBorder);
            result = false;
        } else {
            dateField.setBorder(greenBorder);
        }
        if (timeField.getText().contains("#")) {
            timeField.setBorder(redBorder);
            result = false;
        } else {
            String[] times = timeField.getText().split(":");
            if (Integer.parseInt(times[0]) > 24 || Integer.parseInt(times[1]) > 60) {
                timeField.setBorder(redBorder);
                result = false;
            } else {
                timeField.setBorder(greenBorder);
            }
        }
        if (costField.getText() == null || costField.getText().isEmpty()) {
            costField.setBorder(redBorder);
            result = false;
        } else {
            costField.setBorder(greenBorder);
        }
        return result;
    }

    public static boolean checkFilmFields(JTextArea description, JTextField... fields) {
        boolean result = true;
        description.setBorder(greenBorder);
        if (description.getText() == null || description.getText().isEmpty()) {
            description.setBorder(redBorder);
        }
        for (JTextField field : fields) {
            if (field != null) {
                field.setBorder(greenBorder);
            }
        }
        result = checkFields(fields);
        System.out.println(fields.length);
        int[] sizes = {40, 25, 25, 15, 30, 0};
        for (int i = 0; i < fields.length; i++) {
            if (i == 1) {
                if (!isInteger(fields[i].getText())) {
                    fields[i].setBorder(redBorder);
                    result = false;
                }
            } else if (i == 5) {
                if (!isInteger(fields[i].getText()) || fields[i].getText().length() > 2) {
                    fields[i].setBorder(redBorder);
                    result = false;
                }
            } else {
                if (fields[i].getText().length() > sizes[i]) {
                    fields[i].setBorder(redBorder);
                    result = false;
                }
            }
        }
        return result;
    }

    public static boolean checkFields(JTextField... fields) {
        boolean result = true;
        for (JTextField field : fields) {
            if (field == null) {
                continue;
            }
            if (field.getText() == null || field.getText().isEmpty()) {
                field.setBorder(redBorder);
                result = false;
            }
        }
        return result;
    }

    public static boolean isInteger(String string) {
        if (string.isEmpty()) return false;
        for (int i = 0; i < string.length(); i++) {
            if (i == 0 && string.charAt(i) == '-') {
                if (string.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(string.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }

}

