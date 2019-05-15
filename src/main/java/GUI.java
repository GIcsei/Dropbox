import database.ModifyDatabase;
import engine.Character;
import engine.DataHandler;
import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class GUI {
    /**
     * Szükséges konstansok, tagváltozók, amelyeket többször
     * is felhasználhatunk a későbbiekben
     */
    private static final int maxPoints=75;
    private static final int maxSkillPoints=20;
    private TableView table = new TableView();

    private final DataHandler setup=new DataHandler();
    private ColumnConstraints column = new ColumnConstraints();
    private RowConstraints row = new RowConstraints();

    /**
     * A menü létrehozásáért felelős
     * @param primaryStage
     */

    public void menu(Stage primaryStage) {

        DataHandler load=new DataHandler();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        column.setPercentWidth(60);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(1);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(19);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();

        grid.setPadding(new Insets(25, 25, 25, 25));
        BackgroundImage myBI = new BackgroundImage(new Image("file:///c:\\LOTR\\pictures\\sauron.gif", 1920, 1080, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        grid.setBackground(new Background(myBI));


        Button charGenerator = new Button("Új karakter létrehozása");
        charGenerator.setFont(Font.font(20));
        HBox hbcharGenerator = new HBox(10);
        hbcharGenerator.setAlignment(Pos.CENTER);
        hbcharGenerator.getChildren().add(charGenerator);
        grid.add(hbcharGenerator, 3, 1);
        try {
            charGenerator.setOnAction(e -> generateCharacter(primaryStage));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Button charLoad = new Button("Utolsó karakter betöltése");
        charLoad.setFont(Font.font(20));
        HBox hbcharLoad = new HBox(8);
        hbcharLoad.setAlignment(Pos.CENTER);
        hbcharLoad.getChildren().add(charLoad);
        grid.add(hbcharLoad, 3, 2);
        try {
            charLoad.setOnAction(e -> {
                CharInfo(primaryStage, load.Loader());});
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Button charList = new Button("Karakter kiválasztása");
        charList.setFont(Font.font(20));
        HBox hbcharList = new HBox(8);
        hbcharList.setAlignment(Pos.CENTER);
        hbcharList.getChildren().add(charList);
        grid.add(hbcharList, 3, 3);
        try {
            charList.setOnAction(e -> chooseCharacter()
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Button exit = new Button("Kilépés");
        exit.setFont(Font.font(20));
        HBox hbExit = new HBox(8);
        hbExit.setAlignment(Pos.CENTER);
        hbExit.getChildren().add(exit);
        grid.add(hbExit, 3, 4);
        try {
            exit.setOnAction(e -> primaryStage.close());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Text version=new Text();
        version.setFill(Color.WHITE);
        version.setTextAlignment(TextAlignment.LEFT);
        version.setText("Version number: "+load.versionNumber());
        grid.add(version, 0,6);
        grid.setGridLinesVisible(false);
        Scene scene = new Scene(grid, Screen.getPrimary().getVisualBounds().getWidth()/2, Screen.getPrimary().getVisualBounds().getHeight()/2);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Ahogy  a neve is utal rá, a menüből ide irányít át,
     * és létrehozhatunk egy új karaktert
     * A művelet kétlépcsős, ez az első fázis
     * Név megadása, faj és osztály kiválasztása
     * @param primaryStage
     */
    public void generateCharacter(Stage primaryStage){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        BackgroundImage myBI = new BackgroundImage(new Image("file:///c:\\LOTR\\pictures\\logo.jpg", 1920, 1080, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        //then you set to your node
        column.setPercentWidth(50);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(10);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        column.setPercentWidth(20);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        row.setPercentHeight(20);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        grid.setBackground(new Background(myBI));
        Text scenetitle = new Text("Karakter Háttere");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        scenetitle.setFill(Color.WHITE);
        scenetitle.setTextAlignment(TextAlignment.LEFT);
        grid.add(scenetitle, 3, 0, 2, 1);


        Label userName = new Label("Karakter neve: ");
        userName.setTextFill(Color.WHITE);
        grid.add(userName, 3, 1);
        TextField userTextField = new TextField();
        grid.add(userTextField, 4, 1);


        ChoiceBox<Character.Classes> cc = new ChoiceBox<>();
        cc.getItems().setAll(Character.Classes.values());
        grid.add(cc, 4, 2);
        Label cl = new Label("Osztály:");
        cl.setTextFill(Color.WHITE);
        grid.add(cl, 3, 2);


        ChoiceBox<Character.Races> cr = new ChoiceBox<>();
        cr.getItems().setAll(Character.Races.values());
        grid.add(cr, 4, 3);
        Label crs = new Label("Faj:");
        crs.setTextFill(Color.WHITE);
        grid.add(crs, 3, 3);


        Button btn = new Button("Következő");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 4, 4);
        try {
            btn.setOnAction(e -> {try{
                ArrayList<String> data = new ArrayList<>();
                data.add(userTextField.getText());
                data.add(cc.getValue().toString());
                data.add(cr.getValue().toString());
                details(primaryStage, data);
            }catch (Exception a) {
                menu(primaryStage);
            }});
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Scene scene = new Scene(grid, Screen.getPrimary().getVisualBounds().getWidth()/2, Screen.getPrimary().getVisualBounds().getHeight()/2);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * A generateCharacter folytatása
     * Az előzöek kiválasztása után itt megadhatjuk a tulajdonságokat
     * megj pedig elmenthetjük a karakterünket
     * Az itt utoljára elvégzett mentés lesz a default karakter
     * @param primaryStage
     * @param data
     */

    public void details(Stage primaryStage, ArrayList<String> data) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 0));
        BackgroundImage myBI = new BackgroundImage(new Image("file:///c:\\LOTR\\pictures\\logo.jpg", 1920, 1080, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        grid.setBackground(new Background(myBI));
        //0
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(1);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //1
        column.setPercentWidth(30);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(5);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //2
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //3
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //4
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //5
        column.setPercentWidth(5);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //6
        column.setPercentWidth(2);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //7
        column.setPercentWidth(10);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //8
        column.setPercentWidth(5);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();
        //9
        column.setPercentWidth(2);
        grid.getColumnConstraints().add(column);
        column = new ColumnConstraints();
        row.setPercentHeight(7);
        grid.getRowConstraints().add(row);
        row=new RowConstraints();

        Text scenetitle = new Text("Tulajdonságok meghatározása");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        scenetitle.setFill(Color.WHITE);
        grid.add(scenetitle, 4, 0, 6, 1);

        Label text=new Label("Felhasznált pontok");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(text, 4, 9, 2,1);
        text.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        text.setAlignment(Pos.CENTER);
        final Label usedPoints = new Label("0");
        grid.add(usedPoints, 6, 9);
        usedPoints.setAlignment(Pos.CENTER);
        usedPoints.setTextFill(Color.BLACK);
        usedPoints.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));



        //1. Strength

        final Label strengthPoints = new Label("0");
        strengthPoints.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(strengthPoints, 6, 2,1,2);
        strengthPoints.setAlignment(Pos.CENTER);
        strengthPoints.setTextFill(Color.BLACK);
        strengthPoints.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Button up1 = new Button("△");
        HBox hbUp1 = new HBox(5);
        hbUp1.setAlignment(Pos.BOTTOM_CENTER);
        hbUp1.getChildren().add(up1);
        grid.add(hbUp1, 5, 2);
        up1.setOnAction(e-> increase(usedPoints, strengthPoints));
        Label strength = new Label("Erő");
        strength.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        strength.setTextFill(Color.WHITE);
        strength.setAlignment(Pos.TOP_RIGHT);
        grid.add(strength, 4, 2,1,2);
        Button down1 = new Button("▽");
        HBox hbdown1 = new HBox(5);
        hbdown1.setAlignment(Pos.TOP_CENTER);
        hbdown1.getChildren().add(down1);
        grid.add(hbdown1, 5, 3);
        down1.setOnAction(e-> decrease(usedPoints, strengthPoints));


        //2. Dexterity

        final Label dexterityPoints = new Label("0");
        dexterityPoints.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(dexterityPoints, 6, 4,1,2);
        dexterityPoints.setAlignment(Pos.CENTER);
        dexterityPoints.setTextFill(Color.BLACK);
        dexterityPoints.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Button up2 = new Button("△");
        HBox hbUp2 = new HBox(5);
        hbUp2.setAlignment(Pos.BOTTOM_CENTER);
        hbUp2.getChildren().add(up2);
        grid.add(hbUp2, 5, 4);
        up2.setOnAction(e-> increase(usedPoints,dexterityPoints));
        Label dexterity = new Label("Ügyesség");
        dexterity.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        dexterity.setTextFill(Color.WHITE);
        dexterity.setAlignment(Pos.TOP_RIGHT);
        grid.add(dexterity, 4, 4,1,2);
        Button down2 = new Button("▽");
        HBox hbdown2 = new HBox(5);
        hbdown2.setAlignment(Pos.TOP_CENTER);
        hbdown2.getChildren().add(down2);
        grid.add(hbdown2, 5, 5);
        down2.setOnAction(e->decrease(usedPoints,dexterityPoints));


        //3. Intelligence

        final Label intelligencePoints = new Label("0");
        intelligencePoints.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(intelligencePoints, 6, 6,1,2);
        intelligencePoints.setAlignment(Pos.CENTER);
        intelligencePoints.setTextFill(Color.BLACK);
        intelligencePoints.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Button up3 = new Button("△");
        HBox hbUp3 = new HBox(5);
        hbUp3.setAlignment(Pos.BOTTOM_CENTER);
        hbUp3.getChildren().add(up3);
        grid.add(hbUp3, 5, 6);
        up3.setOnAction(e-> increase(usedPoints,intelligencePoints));
        Label intellgince = new Label("Intelligencia");
        intellgince.setTextFill(Color.WHITE);
        intellgince.setAlignment(Pos.TOP_RIGHT);
        intellgince.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(intellgince, 4, 6,1,2);
        Button down3 = new Button("▽");
        HBox hbdown3 = new HBox(5);
        hbdown3.setAlignment(Pos.TOP_CENTER);
        hbdown3.getChildren().add(down3);
        grid.add(hbdown3, 5, 7);
        down3.setOnAction(e-> decrease(usedPoints,intelligencePoints));


        //4.Constitution

        final Label constitutionPoints = new Label("0");
        constitutionPoints.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(constitutionPoints, 9, 3,1,2);
        constitutionPoints.setAlignment(Pos.CENTER);
        constitutionPoints.setTextFill(Color.BLACK);
        constitutionPoints.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Button up4 = new Button("△");
        HBox hbUp4 = new HBox(5);
        hbUp4.setAlignment(Pos.BOTTOM_CENTER);
        hbUp4.getChildren().add(up4);
        grid.add(hbUp4, 8, 3);
        up4.setOnAction(e-> increase(usedPoints,constitutionPoints));
        Label constitution = new Label("Kitartás");
        constitution .setTextFill(Color.WHITE);
        constitution.setAlignment(Pos.TOP_RIGHT);
        constitution.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(constitution , 7, 3,1,2);
        Button down4 = new Button("▽");
        HBox hbdown4 = new HBox(5);
        hbdown4.setAlignment(Pos.TOP_CENTER);
        hbdown4.getChildren().add(down4);
        grid.add(hbdown4, 8, 4);
        down4.setOnAction(e-> decrease(usedPoints,constitutionPoints));


        //5. Luck

        final Label luckPoints = new Label("0");
        luckPoints.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(luckPoints, 9, 5,1,2);
        luckPoints.setAlignment(Pos.CENTER);
        luckPoints.setTextFill(Color.BLACK);
        luckPoints.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Button up5 = new Button("△");
        HBox hbUp5 = new HBox(5);
        hbUp5.getChildren().add(up5);
        hbUp5.setAlignment(Pos.BOTTOM_CENTER);
        grid.add(hbUp5, 8, 5);
        up5.setOnAction(e-> increase(usedPoints,luckPoints));
        Label luck = new Label("Szerencse");
        luck.setTextFill(Color.WHITE);
        luck.setAlignment(Pos.TOP_RIGHT);
        luck.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        grid.add(luck, 7, 5,1,2);
        Button down5 = new Button("▽");
        HBox hbdown5 = new HBox(5);
        hbdown5.setAlignment(Pos.TOP_CENTER);
        hbdown5.getChildren().add(down5);
        grid.add(hbdown5, 8, 6);
        down5.setOnAction(e-> decrease(usedPoints,luckPoints));

        Button create = new Button("Karakter létrehozása");
        HBox hbCreate = new HBox(10);
        hbCreate.getChildren().add(create);
        grid.add(hbCreate, 7, 9,2,1);
        final Label warningtext = new Label();
        warningtext.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        warningtext.setTextFill(Color.RED);
        grid.add(warningtext,4,10, 5,3);
        create.setOnAction(e-> {if((parseInt(usedPoints.getText()))==maxPoints) {
            int Id=create(data, strengthPoints,dexterityPoints,intelligencePoints,constitutionPoints,luckPoints);
            if(Id!=0) {
                DataHandler save = new DataHandler();
                save.Saver(Id);
                menu(primaryStage);
            }
            else {
                System.out.println("Cannot save file");
                menu(primaryStage);
            }
        }
        else{
            warningtext.setText("Minden tulajdonságot ki kell osztani\nMég ennyi maradt: "+(maxPoints-parseInt(usedPoints.getText())));
            Timeline blinker = createBlinker(warningtext);
            blinker.setOnFinished(event -> warningtext.setText(""));
            SequentialTransition blinkThenFade = new SequentialTransition(
                    warningtext,
                    blinker
            );
            blinkThenFade.play();
        }
        });
        String path="file:///c:\\LOTR\\pictures\\"+data.get(1)+".jpg";
        HBox image=new HBox(new ImageView(new Image(path,350,450,false,false)));
        grid.add(image,1,3, 1,9);
        Scene scene = new Scene(grid, Screen.getPrimary().getVisualBounds().getWidth()/2, Screen.getPrimary().getVisualBounds().getHeight()/2);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * A korábban létrehozott karaktereket listázza ki táblázatban
     * Ha egyre rákattintunk (egyszer!), akkor onnantól fogva az lesz a
     * default karakter, majd pedig bezárja az ablakot és visszatérünk a
     * főoldalra
     */
    public void chooseCharacter(){
        ObservableList<Character> data= FXCollections.observableArrayList();
        Stage secondaryStage=new Stage();
        Scene scene = new Scene(new Group(), 1366,768);
        secondaryStage.setTitle("Karakterválasztó");
        secondaryStage.setWidth(1366);
        secondaryStage.setHeight(768);

        final Label label = new Label("Karakterválasztó");
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Color.WHITE);
        TableColumn IdCol = new TableColumn("Karakter Id-ja");
        IdCol.setCellValueFactory(new PropertyValueFactory<Character, String>("id"));
        TableColumn NameCol = new TableColumn("Karakter neve");
        NameCol.setCellValueFactory(new PropertyValueFactory<Character, String>("name"));
        NameCol.setMinWidth(50);
        TableColumn RaceCol = new TableColumn("Faj");
        RaceCol.setCellValueFactory(new PropertyValueFactory<Character, String>("race"));
        RaceCol.setMinWidth(50);
        TableColumn ClassCol = new TableColumn("Osztály");
        ClassCol.setCellValueFactory(new PropertyValueFactory<Character, String>("characterClass"));
        ClassCol.setMinWidth(50);
        TableColumn StrCol = new TableColumn("Erő");
        StrCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("strength"));
        StrCol.setMinWidth(50);
        TableColumn DexCol = new TableColumn("Ügyesség");
        DexCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("dexterity"));
        DexCol.setMinWidth(50);
        TableColumn IntCol = new TableColumn("Intelligencia");
        IntCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("intelligence"));
        IntCol.setMinWidth(50);
        TableColumn ConCol = new TableColumn("Kitartás");
        ConCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("constitution"));
        ConCol.setMinWidth(50);
        TableColumn LckCol = new TableColumn("Szerencse");
        LckCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("luck"));
        LckCol.setMinWidth(50);
        TableColumn ExpCol = new TableColumn("Tapasztalat");
        ExpCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("experiencePoints"));
        ExpCol.setMinWidth(50);
        TableColumn LevCol = new TableColumn("Aktuális Szint");
        LevCol.setCellValueFactory(new PropertyValueFactory<Character, Integer>("level"));
        LevCol.setMinWidth(50);
        ModifyDatabase list =new ModifyDatabase();
        final ArrayList<Character> characters=list.getCharacters();
        final VBox vbox = new VBox();
        data.addAll(characters);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        BackgroundImage myBI = new BackgroundImage(new Image("file:///c:\\LOTR\\pictures\\logo.jpg", 1920, 1080, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false));
        vbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        table.setItems(data);
        table.setEditable(false);
        table.getColumns().addAll(IdCol, NameCol,LevCol,ClassCol,RaceCol,StrCol,DexCol,IntCol,ConCol,LckCol,ExpCol);
        table.setBackground(new Background(myBI));

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        secondaryStage.setScene(scene);
        secondaryStage.show();

        /**
         * A kattintás esetén végrehajtandó művelet megvalósítása
         */
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    Node node = ((Node) event.getTarget()).getParent();
                    TableRow tRow;
                    if (node instanceof TableRow) {
                        tRow = (TableRow) node;
                    } else {
                        // clicking on text part
                        tRow = (TableRow) node.getParent();
                    }
                    Character index=(Character)tRow.getItem();
                    int id=(index.getId());
                    System.out.println(String.valueOf(id));
                    setup.Saver(id);
                    secondaryStage.close();
                }
            }
        });
    }

    /**
     * Default karakterről írja ki az összes elérhető információt
     * Lehetőség van visszatérni a főoldalra a Vissza gomb megnyomása után,
     * mivel alapjáraton ez csak egy információs felület
     * @param primaryStage
     * @param id
     */
    public void CharInfo(Stage primaryStage, int id){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(25, 25, 25, 25));
        BackgroundImage myBI = new BackgroundImage(new Image("file:///c:\\LOTR\\pictures\\logo.jpg", 1920, 1080, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));
        grid.setBackground(new Background(myBI));
        ModifyDatabase lastUsedCharacter=new ModifyDatabase();
        Character character=lastUsedCharacter.getCharacter(id);

        Text scenetitle = new Text("Karakterlap");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 36));
        scenetitle.setFill(Color.WHITE);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label charName=new Label("Karakter Neve: ");
        charName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charName, 0, 1);
        charName.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charName.setAlignment(Pos.CENTER);
        Label CharName=new Label(character.getName());
        CharName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharName.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharName.setAlignment(Pos.CENTER);
        grid.add(CharName, 1, 1);

        Label charClass=new Label("Karakter Osztálya: ");
        charClass.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charClass, 0, 2);
        charClass.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charClass.setAlignment(Pos.CENTER);
        Label CharClass=new Label(character.getCharacterClass().toString());
        CharClass.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharClass.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharClass.setAlignment(Pos.CENTER);
        grid.add(CharClass, 1, 2);


        Label charRace=new Label("Karakter Faja: ");
        charRace.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charRace, 0, 3);
        charRace.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charRace.setAlignment(Pos.CENTER);
        Label CharRace=new Label(character.getRace().toString());
        CharRace.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharRace.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharRace.setAlignment(Pos.CENTER);
        grid.add(CharRace, 1, 3);


        Label charLevel=new Label("Karakter Szintje: ");
        charLevel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charLevel, 0, 4);
        charLevel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charLevel.setAlignment(Pos.CENTER);
        Label CharLevel=new Label(String.valueOf(character.getLevel()));
        CharLevel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharLevel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharLevel.setAlignment(Pos.CENTER);
        grid.add(CharLevel, 1, 4);


        Label charExp=new Label("Tapasztalati pontok: ");
        charExp.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charExp, 0, 5);
        charExp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charExp.setAlignment(Pos.CENTER);
        Label CharExp=new Label(String.valueOf(character.getExperiencePoints()));
        CharExp.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharExp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharExp.setAlignment(Pos.CENTER);
        grid.add(CharExp, 1, 5);

        Label charStr=new Label("Erő: ");
        charStr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charStr, 3, 1);
        charStr.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charStr.setAlignment(Pos.CENTER);
        Label CharStr=new Label(String.valueOf(character.getStrength()));
        CharStr.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharStr.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharStr.setAlignment(Pos.CENTER);
        grid.add(CharStr, 4, 1);

        Label charDex=new Label("Ügyesség: ");
        charDex.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charDex, 3, 2);
        charDex.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charDex.setAlignment(Pos.CENTER);
        Label CharDex=new Label(String.valueOf(character.getDexterity()));
        CharDex.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharDex.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharDex.setAlignment(Pos.CENTER);
        grid.add(CharDex, 4, 2);

        Label charInt=new Label("Értelem: ");
        charInt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charInt, 3, 3);
        charInt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charInt.setAlignment(Pos.CENTER);
        Label CharInt=new Label(String.valueOf(character.getIntelligence()));
        CharInt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharInt.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharInt.setAlignment(Pos.CENTER);
        grid.add(CharInt, 4, 3);

        Label charCon=new Label("Kitartás: ");
        charCon.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charCon, 3, 4);
        charCon.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charCon.setAlignment(Pos.CENTER);
        Label CharCon=new Label(String.valueOf(character.getConstitution()));
        CharCon.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharCon.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharCon.setAlignment(Pos.CENTER);
        grid.add(CharCon, 4, 4);

        Label charLck=new Label("Szerencse: ");
        charLck.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(charLck, 3, 5);
        charLck.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        charLck.setAlignment(Pos.CENTER);
        Label CharLck=new Label(String.valueOf(character.getLuck()));
        CharLck.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        CharLck.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        CharLck.setAlignment(Pos.CENTER);
        grid.add(CharLck, 4, 5);

        Button create = new Button("Vissza");
        create.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        HBox hbCreate = new HBox(50);
        hbCreate.getChildren().add(create);
        grid.add(hbCreate, 4, 7);
        create.setOnAction(e-> menu(primaryStage));
        Scene scene = new Scene(grid, Screen.getPrimary().getVisualBounds().getWidth()/2, Screen.getPrimary().getVisualBounds().getHeight()/2);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Segédfüggvények, hogy számon tudjuk ratani a details oldalon,
     * hogy mi történt kattintás esetén, íggy biztosíthatjuk,
     * hogy nem kerülünk negatív pontszámba és nem lépjük túl az előre
     * megadott maximális pontszámot
     * @param usedPoints
     * @param actualPoints
     */

    public void increase(Label usedPoints, Label actualPoints){
        if((parseInt(usedPoints.getText())<maxPoints)&(parseInt(actualPoints.getText())<maxSkillPoints)) {
            usedPoints.setText(String.valueOf(parseInt(usedPoints.getText()) + 1));
            actualPoints.setText(String.valueOf(parseInt(actualPoints.getText()) + 1));
        }
    }
    public void decrease(Label usedPoints, Label actualPoints){
        if(parseInt(usedPoints.getText())>0&(parseInt(actualPoints.getText())>0)) {
            usedPoints.setText(String.valueOf(parseInt(usedPoints.getText()) - 1));
            actualPoints.setText(String.valueOf(parseInt(actualPoints.getText()) - 1));
        }
    }

    /**
     * Létrehozza a detailsben és generateCharacterben megadott
     * értékek alapján a karaktert, valamint implementálja
     * azt az adatbázisba. (Lehet, hogy sokszor adom át az id-t,
     * de itt garantáltan történik egy hibaellenőrzés)
     * @param data
     * @param strengthPoints
     * @param dexterityPoints
     * @param intelligencePoints
     * @param constitutionPoints
     * @param luckPoints
     * @return
     */
    public int create(ArrayList<String> data, Label strengthPoints,Label dexterityPoints,Label intelligencePoints,Label constitutionPoints,Label luckPoints ){
        int characterId=0;
        ArrayList<String> charData=new ArrayList<>();
        charData.add(data.get(0));
        charData.add(data.get(1));
        charData.add(data.get(2));
        charData.add(strengthPoints.getText());
        charData.add(dexterityPoints.getText());
        charData.add(intelligencePoints.getText());
        charData.add(constitutionPoints.getText());
        charData.add(luckPoints.getText());
        ModifyDatabase creator=new ModifyDatabase();
        if((characterId=creator.newCharacter(charData))!=0){
            return characterId;}
        else return 0;
    }

    /**
     * Felugró ablakért felelős függvények
     * @param node
     * @return
     */
    private Timeline createBlinker(Node node) {
        Timeline blink = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(
                                node.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(0.5),
                        new KeyValue(
                                node.opacityProperty(),
                                0,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(
                                node.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                )
        );
        blink.setCycleCount(3);
        return blink;
    }
}
