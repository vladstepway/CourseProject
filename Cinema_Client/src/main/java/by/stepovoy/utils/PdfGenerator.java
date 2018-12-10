package by.stepovoy.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

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