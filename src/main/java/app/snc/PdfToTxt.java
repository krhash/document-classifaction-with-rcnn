package app.snc;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

public class PdfToTxt {
    String in_path, out_path;

    public PdfToTxt(String path) {
        in_path = path;
        System.out.println("PDF: "+in_path);
        out_path = "C:/SnC/Summarizer/tmp_in.txt";
    }

    public void process() {


        String parsedText;
        File in_file = new File(in_path);
        File out_file = new File(out_path);

        try {
            File file = new File(in_path);
            PDDocument document = PDDocument.load(file);

            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();

            //Retrieving text from PDF document
            String text = pdfStripper.getText(document);
            //System.out.println(text);
            //Closing the document
            document.close();

            PrintWriter out = new PrintWriter(out_file);
            out.println(text);
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
