package engine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;


public class DataHandler {
    /**
     * A properties-ért felelős osztály
     * Alapvetően két értéket tárolunk:
     * Az utoljára készített/használt karakter Id-ját,
     * így azt szabadon betölthetjük, valamint az aktuális
     * verziószámot, amit megjelenítünk a kezdőképernyőn
     */

    private static final String MY_KEY = "character_id";
    private static final String MY_KEY2 = "Version_Number";
    private static String Version="1.0";
    public void Saver(int id) {
    		try{
        Properties properties = new Properties();
        properties.setProperty(MY_KEY, (new String().valueOf(id)));
        properties.setProperty(MY_KEY2, "1.0");

        File file = new File("c:\\LOTR\\lastUsedCharacter.xml");
        Path p= Paths.get(String.valueOf(file));
        OutputStream fileOut = new BufferedOutputStream(Files.newOutputStream(p, CREATE, WRITE));
        properties.storeToXML(fileOut, "Utoljára kimentett karakter Id-ja");
        fileOut.close();
    } catch(
    FileNotFoundException e)

    {
        e.printStackTrace();
    } catch(
    IOException e)

    {
        e.printStackTrace();
    }

}
/*    public void Saver(int id) {
        Properties lastUsedCharacter = new Properties();
        lastUsedCharacter.setProperty(MY_KEY, (new String().valueOf(id)));
        System.out.println(lastUsedCharacter.getProperty(MY_KEY, "0"));
        lastUsedCharacter.setProperty(MY_KEY2, "1.0");
        try {
            lastUsedCharacter.store(new FileOutputStream("file:///c:\\LOTR\\lastUsedCharacter.properties"), "Utoljára kimentett karakter Id-ja");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

public int Loader(){
    try {
        File file = new File("c:\\LOTR\\lastUsedCharacter.xml");
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.loadFromXML(fileInput);
        fileInput.close();

        Enumeration enuKeys = properties.keys();
        Version=properties.getProperty((String)enuKeys.nextElement());
        int id=parseInt(properties.getProperty((String)enuKeys.nextElement()));
        return id;

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return 0;
}
   /* public int Loader () {
        Properties lastUsedCharacter = new Properties();
        try {
            lastUsedCharacter.load(new FileInputStream("file:///c:\\LOTR\\lastUsedCharacter.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int id= parseInt(lastUsedCharacter.getProperty(MY_KEY));
        return id;
    }*/
    public String versionNumber(){
        return Version;
    }
}
