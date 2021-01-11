package ehu.isad.controller;

import com.google.gson.Gson;
import ehu.isad.Main;
import ehu.isad.model.CaptchaTaula;
import ehu.isad.utils.Utils;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.io.File;


public class HasieraKud implements Initializable {

    @FXML
    private Button btnTxertatu;

    @FXML
    private Button btnGorde;

    @FXML
    private TableView<CaptchaTaula> tbvTaula;

    @FXML
    private TableColumn<CaptchaTaula, Integer> tvID;

    @FXML
    private TableColumn<CaptchaTaula, String> tvPath;

    @FXML
    private TableColumn<CaptchaTaula, Integer> tvContent;

    @FXML
    private TableColumn<CaptchaTaula, String> tvDate;

    @FXML
    private TableColumn<CaptchaTaula, Image> tvIrudia;


    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void onClick(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        if(btn.equals(btnTxertatu)){
            txertatu();
        }
        else if(btn.equals(btnGorde)){
            gorde();
        }

    }

    private void txertatu() throws IOException {
        String path = irudiaGorde("http://45.32.169.98/captcha.php");
        LocalDate date = LocalDate.now();
        CaptchaTaula captchaTaula = new CaptchaTaula(DBHasiera.getInstance().azkenIdLortu()+1,path,null,date.toString(),irudiaLortu(path));
        elementuaGehituListari(captchaTaula);
        DBHasiera.getInstance().datuBaseanTxertatu(captchaTaula);


    }

    private void gorde(){
        for (int i = 0; i < tbvTaula.getItems().size(); i++) {
            if (!tvContent.getCellObservableValue(i).getValue().equals(0)) {
                DBHasiera.getInstance().datuBaseaAktualizatu(tvContent.getCellObservableValue(i).getValue(),tvID.getCellObservableValue(i).getValue());
            }
        }
    }

    //IRUDIAK KARGATU
    public Image createImage(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        try (InputStream stream = conn.getInputStream()) {
            return new Image(stream);
        }
    }

    private String saveToFile(Image image) throws IOException {
        File outputFile = File.createTempFile("captcha",".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputFile.getAbsolutePath();
    }

    public String irudiaGorde(String irudia) throws IOException {
        Image i=createImage(irudia);
        String path=saveToFile(i);
        return path;
    }

    public Image irudiaLortu(String path){
        BufferedImage img = null;
        try
        {
            img = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Image image = SwingFXUtils.toFXImage(img, null);
        return image;
    }

    private void elementuaGehituListari(CaptchaTaula captchaTaula){
        List<CaptchaTaula> kargatzekoa = DBHasiera.getInstance().datuBaseaKargatu();
        kargatzekoa.add(captchaTaula);
        ObservableList<CaptchaTaula> taula = FXCollections.observableArrayList(kargatzekoa);

        //add your data to the table here.
        tbvTaula.setItems(taula);


        tvID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        tvContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        tvDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tvIrudia.setCellValueFactory(new PropertyValueFactory<>("irudia"));

        irudiaKargatu();
        contentEditatu();
        contentCommit();
    }

    private void hasieratu(){
        List<CaptchaTaula> kargatzekoa = DBHasiera.getInstance().datuBaseaKargatu();
        ObservableList<CaptchaTaula> taula = FXCollections.observableArrayList(kargatzekoa);

        //add your data to the table here.
        tbvTaula.setItems(taula);
        tbvTaula.setEditable(true);


        tvID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvPath.setCellValueFactory(new PropertyValueFactory<>("path"));
        tvContent.setCellValueFactory(new PropertyValueFactory<>("content"));
        tvDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tvIrudia.setCellValueFactory(new PropertyValueFactory<>("irudia"));
        tvContent.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        irudiaKargatu();
        contentEditatu();
        contentCommit();


    }

    private void irudiaKargatu(){
        tvIrudia.setCellFactory(p -> new TableCell<>() {
            public void updateItem(Image image, boolean empty) {
                if (image != null && !empty){
                    final ImageView imageview = new ImageView();
                    imageview.setFitHeight(50);
                    imageview.setFitWidth(50);
                    imageview.setImage(image);
                    setGraphic(imageview);
                    setAlignment(Pos.CENTER);
                    // tbData.refresh();
                }else{
                    setGraphic(null);
                    setText(null);
                }
            };
        });
    }

    public void contentEditatu(){
        Callback<TableColumn<CaptchaTaula, Integer>, TableCell<CaptchaTaula, Integer>> defaultTextFieldCellFactory
                = TextFieldTableCell.forTableColumn(new IntegerStringConverter());

        tvContent.setCellFactory(col -> {
            TableCell<CaptchaTaula, Integer> cell = defaultTextFieldCellFactory.call(col);

            cell.setOnMouseClicked(event -> {
                if(cell.isEmpty())
                    cell.setEditable(true);
            });

            return cell ;
        });
    }

    public void contentCommit(){
        tvContent.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setContent(t.getNewValue());
                });
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hasieratu();
    }


}
