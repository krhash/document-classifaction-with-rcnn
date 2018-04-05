package app.snc.controller;


import app.snc.DumpSummaries;
import app.snc.MonitorDir;
import app.snc.PdfToTxt;
import app.snc.improved_summary;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

public class SnC_Controller {

    @FXML
    JFXToggleButton toggle_btn_RTMonitor,toggle_btn_RunOnStartup;
    @FXML
    AnchorPane home_tab, about_tab, directory_tab, summary_tab;
    @FXML
    JFXTextField textf_dir, textf_file, textf_tag;
    @FXML
    Label label_nDir, label_nFiles, label_lastModified;

    @FXML
    JFXTextArea textArea_summary;

    Preferences preferences;
    String prefDir,tag;
    int n_dir = 0, n_files = 0;
    Boolean isSet_RunOnStartup, isSetRTMonitor;

    public void initialize(){

        directory_tab.setVisible(false);
        summary_tab.setVisible(false);
        about_tab.setVisible(false);
        home_tab.setVisible(true);
        preferences = Preferences.userRoot().node("prefDir");
        prefDir = preferences.get("PREF_DIR","C:/SnC/pdfs");
        File directory = new File(prefDir);
        if(!directory.exists())
        {
            directory.mkdir();
        }
        textf_dir.setText(prefDir);
        isSetRTMonitor = preferences.getBoolean("BOOL_RTM",false);
        isSet_RunOnStartup = preferences.getBoolean("BOOL_ROS",false);

        if(isSet_RunOnStartup){
            toggle_btn_RunOnStartup.selectedProperty().setValue(true);
            toggle_btn_RunOnStartup.setText("On");
        }

        if(isSetRTMonitor){
            toggle_btn_RTMonitor.selectedProperty().setValue(true);
            toggle_btn_RTMonitor.setText("On");
            Path path = Paths.get(prefDir);
            MonitorDir md = new MonitorDir(path);
            Thread t = new Thread(md);
            t.start();
        }

        populate_dir_stats();
    }
    public void btn_exit_clicked(MouseEvent e){
        System.exit(0);
    }

    public void btn_home_clicked(MouseEvent e){
        directory_tab.setVisible(false);
        summary_tab.setVisible(false);
        about_tab.setVisible(false);
        home_tab.setVisible(true);

    }

    public void btn_dumpSummaries_clicked(MouseEvent e){
        if(new File(prefDir).listFiles().length == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SnC");
            alert.setHeaderText(null);
            alert.setContentText("No files to process!");
            alert.show();
            return;
        }

        File summary_dir = new File(prefDir+"\\Summaries");
        if(!summary_dir.exists())
        {
            summary_dir.mkdir();
        }

        File dir = new File(prefDir);
        File file_list[] = dir.listFiles();


        for(File file : file_list){
            if(file.isFile()){
                if(file.getName().endsWith(".pdf")){
                    DumpSummaries ds = new DumpSummaries(file.getAbsolutePath());
                    Thread t = new Thread(ds);
                    t.start();
                }
            }
        }
        /*Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try{
                    String cmd = "C:/Python27/python.exe C:/Summarizer/summarize.py -d "+prefDir+"/ -t "+summary_dir+"/";
                    Process p = Runtime.getRuntime().exec(cmd);
                    p.waitFor();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SnC");
            alert.setHeaderText(null);
            alert.setContentText("Summaries saved to \""+summary_dir+"\\\"");
            alert.show();
            System.out.println("Summaries dumped!");
        });*/

        //new Thread(task).start();

    }

    public void btn_dir_clicked(MouseEvent e) throws  Exception{
        home_tab.setVisible(false);
        summary_tab.setVisible(false);
        about_tab.setVisible(false);
        directory_tab.setVisible(true);

        //Path path = Paths.get("c:/tempio");
        //MonitorDir md = new MonitorDir(path);
        //Thread t = new Thread(md);
        //t.start();
    }

    public void btn_browse_clicked(MouseEvent e){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a directory to monitor");;
        File defaultDirectory = new File(prefDir);
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(home_tab.getScene().getWindow());
        preferences.put("PREF_DIR",selectedDirectory.getAbsolutePath());
        prefDir = selectedDirectory.getAbsolutePath();
        textf_dir.setText(selectedDirectory.getAbsolutePath());
        populate_dir_stats();


        //MonitorDir md = new MonitorDir(Paths.get(selectedDirectory.getAbsolutePath()));
        //Thread t = new Thread(md);
        //t.start();
    }
    public void btn_summary_clicked(MouseEvent e){
        home_tab.setVisible(false);
        directory_tab.setVisible(false);
        about_tab.setVisible(false);
        summary_tab.setVisible(true);
    }

    public void btn_about_clicked(MouseEvent e){
        home_tab.setVisible(false);
        directory_tab.setVisible(false);
        summary_tab.setVisible(false);
        about_tab.setVisible(true);
    }

    public void handle_toggle_RTMonitor(MouseEvent e)
    {
        if(toggle_btn_RTMonitor.selectedProperty().getValue()){
            toggle_btn_RTMonitor.setText("On");
            isSetRTMonitor = true;
            preferences.putBoolean("BOOL_RTM",isSetRTMonitor);
        }
        else
        {
            toggle_btn_RTMonitor.setText("Off");
            isSetRTMonitor = false;
            preferences.putBoolean("BOOL_RTM",isSetRTMonitor);
        }
    }

    public void handle_toggle_RunOnStartup(MouseEvent e){
        if(toggle_btn_RunOnStartup.selectedProperty().getValue()){
            toggle_btn_RunOnStartup.setText("On");
            isSet_RunOnStartup = true;
            preferences.putBoolean("BOOL_ROS",isSet_RunOnStartup);
        }
        else
        {
            toggle_btn_RunOnStartup.setText("Off");
            isSet_RunOnStartup = false;
            preferences.putBoolean("BOOL_ROS",isSet_RunOnStartup);
        }
    }

    public void btn_browseFile_clicked(MouseEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to summarize");
        File defaultDirectory = new File(prefDir);
        fileChooser.setInitialDirectory(defaultDirectory);
        File selectedFile = fileChooser.showOpenDialog(home_tab.getScene().getWindow());
        textf_file.setText(selectedFile.getAbsolutePath());

		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try{
                    PdfToTxt pt = new PdfToTxt(selectedFile.getAbsolutePath());
                    pt.process();
                    String result_summary = new improved_summary().getResult(); //read summary string

                    //result = new String ( Files.readAllBytes( Paths.get("C:/Summarizer/tmp_out.txt") ) ); //read summary stored in file

                    Process p = Runtime.getRuntime().exec("python C:/SnC/Models/BBC/predict_v-2.0.py -f C:/SnC/Summarizer/tmp_out.txt");    //classify processed summary stored in tmp_out
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while((line = stdInput.readLine())!=null)
                    {
                        sb.append(line);
                    }

                    textArea_summary.setText(result_summary);
                    tag = sb.toString();
                    textf_tag.setText(tag);


                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SnC");
            alert.setHeaderText(null);
            alert.setContentText("Done!");
            alert.show();
        });

        new Thread(task).start();

    }

    void populate_dir_stats(){
        File folder = new File(prefDir);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles.length == 0){
            label_nDir.setText("0");
            label_nFiles.setText("0");
            label_lastModified.setText("-");
            return;
        }
        File lastModifiedFile = listOfFiles[0];
        for(File f : listOfFiles){
            if(f.isDirectory())
                n_dir++;
            else {
                n_files++;
                if(lastModifiedFile.lastModified() < f.lastModified()){
                    lastModifiedFile = f;
                }
            }
        }

        label_nDir.setText(Integer.toString(n_dir));
        label_nFiles.setText(Integer.toString(n_files));
        label_lastModified.setText(lastModifiedFile.getName());


    }
}
