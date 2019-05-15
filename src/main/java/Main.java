import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import static java.nio.file.StandardOpenOption.*;


public class Main extends Application {

    private static GUI gui=new GUI();
    private static final String ACCESS_TOKEN = "IBUXhmVxy8AAAAAAAAAAKIezQFOh47lt7r7zMYFDcq_TUyamC0oGSpPrp_mFHlMl";
    private DbxRequestConfig config = null;
    DbxClientV2 client = null;
    private static MediaPlayer mediaPlayer;
    private static Boolean starter=false;

    public Main()
    {
        // Create Dropbox client
        config = new DbxRequestConfig("dropbox/java-tutorial");
        client = new DbxClientV2(config, ACCESS_TOKEN);

        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());
            starter=true;
        }
        catch (DbxException dbxe)
        {
            dbxe.printStackTrace();
        }
    }

    public Boolean uploadFile(String path, String foldername)
    {
        // Upload "test.txt" to Dropbox
        try {
            InputStream in2 = new FileInputStream(path);
            FileMetadata metadata = client.files().uploadBuilder(foldername).withMode(WriteMode.OVERWRITE).uploadAndFinish(in2);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Download Dropbox File to Local Computer
     *
     * @param dropBoxFilePath
     *            The file path on the Dropbox cloud server -> [/foldername/something.txt]
     * @param localFileAbsolutePath
     *            The absolute file path of the File on the Local File System
     * @throws DbxException
     * @throws DownloadErrorException
     * @throws IOException
     */
    public void downloadFile(String dropBoxFilePath , String localFileAbsolutePath) throws DownloadErrorException , DbxException , IOException {

        //Create DbxDownloader
        if(dropBoxFilePath.contains("/pictures/")){
            dropBoxFilePath.replace("/pictures","");
            File file=new File(localFileAbsolutePath+"/pictures");
            file.mkdir();
        }
        DbxDownloader<FileMetadata> dl = client.files().download(dropBoxFilePath);
        File file=new File(localFileAbsolutePath+dropBoxFilePath);
        Path p = Paths.get(String.valueOf(file));
        OutputStream fOut = new BufferedOutputStream(Files.newOutputStream(p, CREATE, WRITE));
            System.out.println("Downloading .... " + dropBoxFilePath);
        //Add a progress Listener
        dl.download(new ProgressOutputStream(fOut, dl.getResult().getSize(), (long completed , long totalSize) -> System.out.println( ( completed * 100 ) / totalSize + " %")));
        System.out.println("Download successful!");
    }

    private static boolean synchronize(Main m) {
        try {
            File file=new File ("C:\\LOTR\\");
            file.mkdir();
            m.downloadFile("/lotr.wav","C:\\LOTR\\");
            m.downloadFile("/races.db","C:\\LOTR\\");
            m.downloadFile("/pictures/logo.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/mage.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/scout.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/warrior.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/sauron.gif","C:\\LOTR\\");
            } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * A program elindításáért és a zenelejátszásért felelős
     * függvények.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        Main m = new Main();
        final Boolean[] finished = {false};
        Task<Void> sync = new Task<Void>() {
            @Override
            protected Void call() {
                finished[0] = synchronize(m);
                return null;
            }
        };
        if(starter) {
            Thread backgroundThread = new Thread(sync);
            backgroundThread.setDaemon(true);
            backgroundThread.start();
            ProgressIndicator PI = new ProgressIndicator();

            StackPane root = new StackPane();
            Label text=new Label("Loading...");
            root.getChildren().addAll(PI, text);
            Scene scene = new Scene(root, 300, 200);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Loading data");
            primaryStage.show();
            primaryStage.setOnCloseRequest(e->{
                Platform.exit();
                System.exit(0);
            });
            sync.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            try {
                                primaryStage.setTitle("LOTR Karakteralkotó");
                                primaryStage.getIcons().add(new Image("file:///c:\\LOTR\\pictures\\logo.jpg"));
                                music();
                                gui.menu(primaryStage);
                                primaryStage.setOnHiding(event -> uploadFiles(m));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // now do something with result
                        }

                    });
        }
    }



/*private Boolean loadingScreen(Stage primaryStage, Main m)
    {
    final boolean[] finished = {false};

    Thread thread1 = new Thread(() -> {
        if(synchronize(m)){
            finished[0] =true;}
        else {
            System.exit(1);
        }
    });
    Thread thread2 = new Thread(() -> gui.Loading(primaryStage));
    thread1.start();
    thread2.start();
    return synchronize(m);
        }*/

    private void uploadFiles(Main m) {
        if (m.uploadFile("C:/LOTR/races.db", "/races.db")){
            System.out.println("Succes");
        }
        else{
            System.out.println("Problem!");
        }
    }

    public static void main(String[] args){
        launch(args);
    }

    public void music() {
               final Task playMusic = new Task() {
                    @Override
                    protected Object call(){
                        Media media = new Media(new File("c:\\LOTR\\lotr.wav").toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                        mediaPlayer.play();
                        return null;
                    }
                };
                Thread thread = new Thread(playMusic);
                thread.start();
    }


}


    //TODO szálkiosztás, grafikus felület


