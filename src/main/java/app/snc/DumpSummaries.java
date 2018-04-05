package app.snc;

import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import sun.rmi.runtime.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DumpSummaries implements Runnable{

    String in_path, out_path;
    public DumpSummaries(String path){
        this.in_path = path;
        out_path = null;
    }

    @Override
    public void run() {
        String parsedText, tmp_path;
        File in_file = new File(in_path);
        out_path = in_file.getParent()+"\\Summaries\\"+in_file.getName()+".txt";
        tmp_path = "C:/SnC/Summarizer/tmp/"+in_file.getName()+".txt";
        File out_file = new File(tmp_path);

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

            PrintWriter out = new PrintWriter(out_file);    //extracted pdf
            out.println(text);
            out.close();

            SummaryTool summary = new SummaryTool(tmp_path);
            summary.init();
            summary.extractSentenceFromContext();
            summary.groupSentencesIntoParagraphs();
            summary.printSentences();
            summary.createIntersectionMatrix();

            //System.out.println("INTERSECTION MATRIX");
            //summary.printIntersectionMatrix();

            summary.createDictionary();
            summary.createSummary();
            summary.SaveSummary(out_path);

            Process p = Runtime.getRuntime().exec("python C:/SnC/Models/BBC/predict_v-2.0.py -f "+out_path);    //classify processed summary stored in tmp_out
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();

            while((line = stdInput.readLine())!=null)
            {
                sb.append(line);
            }

            File dir = new File(in_file.getParent()+"\\"+sb.toString());
            if(!dir.exists())
            {
                dir.mkdir();
                System.out.println(dir.getName());
            }
            in_file.renameTo(new File(dir.getAbsolutePath()+"\\"+in_file.getName()));   //move to specific class

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
