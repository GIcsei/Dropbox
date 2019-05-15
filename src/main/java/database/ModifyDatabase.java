package database;

import engine.Character;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;



public class ModifyDatabase {
    /**Az adatbázis manipulációért felelős osztály
     *
     */
    /**
     * A különböző módosítók (osztály, faj) két különböző adatbázisban
     * vannak eltárolva, így azokat a nevek alapján tudjuk megkeresni
     * Mivel csak legördülő listából választhat a user, így elvileg
     * nem kell attól félni, hogy az adatbázisban nem szereplő nevet választ ki
     * (Azért van hibakezelés, mert a fejlesztő is elronthatja)
     * @param className
     * @return
     */
    public ArrayList<Integer> getClassModifiers(String className){
        ArrayList<Integer> modifier=new ArrayList<Integer>();
        String connectionString = "jdbc:sqlite:file:///c:\\LOTR\\races.db";
        try(Connection conn = DriverManager.getConnection(connectionString)) {
            String sql="SELECT * from Classes where instr(Name,?)";
            try (PreparedStatement pstmt  = conn.prepareStatement(sql)) {
                pstmt.setString(1, className);
                ResultSet rs  = pstmt.executeQuery();
                while(rs.next()) {
                    modifier.add(rs.getInt("Id"));
                    modifier.add(rs.getInt("Strength"));
                    modifier.add(rs.getInt("Dexterity"));
                    modifier.add(rs.getInt("Intelligence"));
                    modifier.add(rs.getInt("Constitution"));
                    modifier.add(rs.getInt("Luck"));
                }
                rs.close();
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return modifier;
    }
    public ArrayList<Integer> getRaceModifiers(String raceName){
        ArrayList<Integer> modifier=new ArrayList<>();
        String connectionString = "jdbc:sqlite:file:///c:\\LOTR\\races.db";
        try(Connection conn = DriverManager.getConnection(connectionString)) {
            String sql="SELECT * from Races where instr(Name,?)";
            try (PreparedStatement pstmt  = conn.prepareStatement(sql)) {
                pstmt.setString(1, raceName);
                ResultSet rs  = pstmt.executeQuery();
                while(rs.next()) {
                    modifier.add(rs.getInt("Id"));
                    modifier.add(rs.getInt("Strength"));
                    modifier.add(rs.getInt("Dexterity"));
                    modifier.add(rs.getInt("Intelligence"));
                    modifier.add(rs.getInt("Constitution"));
                    modifier.add(rs.getInt("Luck"));
                }
                rs.close();
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return modifier;
    }

    /**
     * Új karakter adatbázisba illesztéséért felelős
     * Mindig visszaadja a generált sor Id-ját,
     * így a properties tárolhatja, hogy melyik karaktert
     * hoztuk utoljára létre
     * @param data
     * @return
     */
    public int newCharacter(ArrayList<String> data){
        int characterId=0;
        ArrayList<Integer> rc=getRaceModifiers(data.get(2));
        ArrayList<Integer> cl=getClassModifiers(data.get(1));
        ArrayList<Integer> finalPoints=FinalPoints(data, rc,cl);
        String connectionString = "jdbc:sqlite:file:///c:\\LOTR\\races.db";
        try(Connection conn = DriverManager.getConnection(connectionString)) {
            String sql="Insert into Characters (Race_id, Classes_id, Name, Strength, Dexterity, Intelligence, Constitution, Luck, Experience) values (?,?,?,?,?,?,?,?,0)";
            try (PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1,rc.get(0));
                ps.setInt(2,cl.get(0));
                ps.setString(3,data.get(0));
                ps.setInt(4,finalPoints.get(0));
                ps.setInt(5,finalPoints.get(1));
                ps.setInt(6,finalPoints.get(2));
                ps.setInt(7,finalPoints.get(3));
                ps.setInt(8,finalPoints.get(4));
                ps.execute();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    characterId = rs.getInt(1);
                }
                rs.close();
            }
        }
        catch (SQLException ex) {
            ex.getErrorCode();
            ex.getSQLState();
            ex.getCause();
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return characterId;
    }

    /**
     * Abban az esetben, ha már korábbi karaktert szeretnénk betölteni,
     * úgy szükséges megtudni az Id-ját. Ezt is a properties fogja elmenteni
     * ezért szükséges int-et visszaadni
     * @param name
     * @return
     */
   /* public int getCharId(String name){
        int characterId=0;
        name.replace(name, "%"+name+"%");
        String connectionString = "jdbc:sqlite:file:///c:\\LOTR\\races.db";
        try(Connection conn = DriverManager.getConnection(connectionString)) {
            String sql = "SELECT Id, Name from Characters where name like ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
               while(rs.next()) {
                   if (rs.getString("Name")==name) {
                       characterId = rs.getInt("Id");
                       return characterId;
                   }
               }
            }
        }
        catch (SQLException ex) {
            ex.getErrorCode();
            ex.getSQLState();
            ex.getCause();
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        System.out.println("Gáz van!");
        return characterId;
    }
*/
    /**
     * A módosítok hatására kialakult végső pontszámokat számolja ki
     * Segédfüggvény
     * @param data
     * @param rc
     * @param cl
     * @return
     */
    public ArrayList<Integer> FinalPoints(ArrayList<String> data,ArrayList<Integer> rc,ArrayList<Integer> cl){
        ArrayList<Integer> finalPoints=new ArrayList<>();
        for (int i=3;i<data.size();i++){
            finalPoints.add((parseInt(data.get(i)))+rc.get(i-2)+cl.get(i-2));
        }
        return finalPoints;
    }

    /**
     * A táblázat kirajzolásához szükséges, az összes karaktert kiolvassa
     * és azokat egy collection-ben tárolja
     *
     * @return
     */
    public ArrayList<Character> getCharacters(){
        Character character=new Character();
        ArrayList<Character> characters=new ArrayList<>();
        String connectionString = "jdbc:sqlite:file:///c:\\LOTR\\races.db";
        try(Connection conn = DriverManager.getConnection(connectionString)) {
            String sql="SELECT Characters.Id as Id, Characters.name as Name,Characters.Strength as Strength, Characters.Dexterity as Dexterity," +
                    " Characters.Constitution as Constitution, Characters.Intelligence as Intelligence, Characters.Luck as Luck, Characters.Experience as Experience, Characters.Armor as Armor," +
                    " Races.name as RaceName, Classes.name as ClassName from Characters inner join Races on Race_id=Races.Id inner join Classes on Classes_id=Classes.id";
            try (PreparedStatement pstmt  = conn.prepareStatement(sql)) {
                ResultSet rs  = pstmt.executeQuery();
                while(rs.next()) {
                    character=new Character();
                    character.setId(rs.getInt("Id"));
                    character.setName(rs.getString("Name"));
                    character.setLevel(rs.getInt("Experience")/2000);
                    character.setRace(Character.Races.valueOf(rs.getString("RaceName")));
                    character.setCharacterClass(Character.Classes.valueOf(rs.getString("ClassName")));
                    character.setExperiencePoints(rs.getInt("Experience"));
                    character.setConstitution(rs.getInt("Constitution"));
                    character.setDexterity(rs.getInt("Dexterity"));
                    character.setIntelligence(rs.getInt("Intelligence"));
                    character.setLuck(rs.getInt("Luck"));
                    character.setStrength(rs.getInt("Strength"));
                    characters.add(character);
                }
                rs.close();
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return characters;
    }

    /**
     * Karakterkiválasztáskor lekérdezi az adatbázisból
     * id alapján a karakter összes adatát és
     * visszaadja azokat
     * @param id
     * @return
     */
    public Character getCharacter(int id){
        Character character=new Character();
        String connectionString = "jdbc:sqlite:file:///c:\\LOTR\\races.db";
        try(Connection conn = DriverManager.getConnection(connectionString)) {
            String sql="SELECT Characters.name as Name,Characters.Strength as Strength, Characters.Dexterity as Dexterity," +
                    " Characters.Constitution as Constitution, Characters.Intelligence as Intelligence, Characters.Luck as Luck, Characters.Experience as Experience, Characters.Armor as Armor," +
                    " Races.name as RaceName, Classes.name as ClassName from Characters inner join Races on Race_id=Races.Id inner join Classes on Classes_id=Classes.id where Characters.id=?";
            try (PreparedStatement pstmt  = conn.prepareStatement(sql)) {
                pstmt.setString(1,String.valueOf(id));
                ResultSet rs  = pstmt.executeQuery();
                while(rs.next()) {
                    character.setName(rs.getString("Name"));
                    character.setLevel(rs.getInt("Experience")/2000);
                    character.setRace(Character.Races.valueOf(rs.getString("RaceName")));
                    character.setCharacterClass(Character.Classes.valueOf(rs.getString("ClassName")));
                    character.setExperiencePoints(rs.getInt("Experience"));
                    character.setConstitution(rs.getInt("Constitution"));
                    character.setDexterity(rs.getInt("Dexterity"));
                    character.setIntelligence(rs.getInt("Intelligence"));
                    character.setLuck(rs.getInt("Luck"));
                    character.setStrength(rs.getInt("Strength"));
                }
                rs.close();
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return character;
    }
}


