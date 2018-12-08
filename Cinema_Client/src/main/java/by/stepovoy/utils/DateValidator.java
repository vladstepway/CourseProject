package by.stepovoy.utils;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateValidator extends JFormattedTextField.AbstractFormatter {

    private String pattern = "yyyy-MM-dd";
    private SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);

    @Override
    public Object stringToValue(String text) throws ParseException {
        return formatter.parseObject(text);
    }

    public String valueToString(Object value) {
        if (value != null) {
            Calendar calendar = (Calendar)value;
            return formatter.format(calendar.getTime());
        }
        return "";
    }

}
