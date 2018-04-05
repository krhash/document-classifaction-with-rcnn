package app.snc;
import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.nio.file.*;

public class MonitorDir implements Runnable{
    Path path;
    public MonitorDir(Path path){
        this.path = path;
    }

    public void process() throws Exception
    {
        FileSystem fs = FileSystems.getDefault();
        WatchService ws = fs.newWatchService();
        path.register(ws, new WatchEvent.Kind[]{ENTRY_CREATE});

        while (true)
        {
            WatchKey k = ws.take();
            for(WatchEvent<?> e : k.pollEvents())
            {
                Object c = e.context();
                System.out.printf("\n%s %s",e.kind(),c);
                if(StandardWatchEventKinds.ENTRY_CREATE.equals(e.kind())){

                    String fname = path.toAbsolutePath().toString()+"\\"+e.context().toString();
                    System.out.println("File created : "+fname);
                    File dir = new File(path.toAbsolutePath().toString());
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

                }
            }
            k.reset();
            Thread.sleep(3);
        }
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
