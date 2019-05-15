import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Set;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.SSLConfig;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javax.swing.*;

import static java.awt.SystemColor.menu;
import static java.nio.file.StandardOpenOption.*;


public class Main extends Application {

    private static GUI gui=new GUI();
    private static final String ACCESS_TOKEN = "IBUXhmVxy8AAAAAAAAAAKIezQFOh47lt7r7zMYFDcq_TUyamC0oGSpPrp_mFHlMl";
    private DbxRequestConfig config = null;
    DbxClientV2 client = null;

    public Main()
    {
        // Create Dropbox client
        config = new DbxRequestConfig("dropbox/java-tutorial");
        client = new DbxClientV2(config, ACCESS_TOKEN);

        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());
        }
        catch (DbxException dbxe)
        {
            dbxe.printStackTrace();
        }
    }

    public void uploadFile(String path, String foldername)
    {
        // Upload "test.txt" to Dropbox
        try {
            InputStream in = new FileInputStream(path);
            FileMetadata metadata = client.files().uploadBuilder(foldername).uploadAndFinish(in);
        }
        catch (FileNotFoundException fne)
        {
            fne.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (DbxException dbxe)
        {
            dbxe.printStackTrace();
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
        String folderName = "";
        try {
            m.downloadFile("/lotr.mp3","C:\\LOTR\\");
            m.downloadFile("/lastUsedCharacter.properties","C:\\LOTR\\");
            m.downloadFile("/races.db","C:\\LOTR\\");
            m.downloadFile("/pictures/logo.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/mage.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/scout.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/warrior.jpg","C:\\LOTR\\");
            m.downloadFile("/pictures/sauron.gif","C:\\LOTR\\");
            //m.uploadFile("C:/b2b/Overview of Archival and Purge Process.pdf", folderName + "/Overview of Archival and Purge Process.pdf");
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
    public void start(Stage primaryStage) {
        Main m = new Main();
        if(synchronize(m)){
            try {
                primaryStage.setTitle("LOTR Karakteralkotó");
                primaryStage.getIcons().add(new Image("file:///c:\\LOTR\\pictures\\logo.jpg"));
                music();
                gui.menu(primaryStage);
            } catch (Exception e){
                e.printStackTrace();
            }
            music();
        }

    }

    public static void main(String args[]){
        launch(args);
    }

    public void music(){
        final Task playMusic = new Task() {
            @Override
            protected Object call(){
                Media media = new Media(new File("file:///c:\\LOTR\\lotr.mp3").toURI().toString());
                MediaPlayer mediaPlayer;
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

