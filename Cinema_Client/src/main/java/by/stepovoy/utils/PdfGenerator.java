package by.stepovoy.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;

public class PdfGenerator {

    public static void main(String[] args) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));

            document.open();

            BaseFont bf = BaseFont.createFont("C:\\Windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //подключаем файл шрифта, который поддерживает кириллицу
            Font font = new Font(bf);

            Paragraph paragraph = new Paragraph("Ффцпцфпфцпфцпфц", font);
            document.add(paragraph);


        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }


    }

}