package ehu.isad.controller.ui;

import com.google.gson.Gson;
import ehu.isad.Main;
import org.apache.commons.codec.binary.Hex;
import ehu.isad.controller.db.DBHasiera;
import ehu.isad.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.File;


public class HasieraKud implements Initializable {


    @FXML
    private Button btnCheck;

    @FXML
    private TableView<Model> tbvTaula;

    @FXML
    private TableColumn<Model, String> tvUrl;

    @FXML
    private TableColumn<Model, String> tvMd5;

    @FXML
    private TableColumn<Model, String> tvVersion;

    @FXML
    private TextField txtTestua;

    @FXML
    private Label lblWarning;


    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void onClick(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        Button btn = (Button) event.getSource();
        if(btn.equals(btnCheck)){
            check();
        }
    }

    private void check() throws IOException, NoSuchAlgorithmException {

        if(this.txtTestua.equals(null)){
            lblWarning.setText("Mesedez sar ezazu url bat");
        }
        else{
            String line = md5Kalkulatu(txtTestua.getText());
            if(DBHasiera.getInstance().jadaDatuBasean(line,txtTestua.getText()).size()!=0){
                taulaBeteBai(line);
                lblWarning.setText("Datubasean zegoen");
            }
            else{
                Model model = new Model(txtTestua.getText(),line,"");
                taulaBeteEz(line,model);
                Model m = null;
                for(int i=0;i<tbvTaula.getItems().size();i++){
                    if(tvMd5.getCellObservableValue(i).getValue().equals(model.getMd5())) {
                        m = new Model(model.getUrl(), model.getMd5(),
                                tvVersion.getCellObservableValue(i).getValue());
                    }
                }
                DBHasiera.getInstance().datuBaseanTxertatu(m);
                lblWarning.setText("md5 eta bertsio berria datubasean sartu egin dira");
            }
        }
    }

    public void taulaBeteBai(String line){
        List<Model> emaitza = DBHasiera.getInstance().jadaDatuBasean(line,txtTestua.getText());
        ObservableList<Model> taula = FXCollections.observableArrayList(emaitza);



        if(tbvTaula.getItems().size()==0){

            tbvTaula.setItems(taula);
            tbvTaula.setEditable(true);
            tvUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
            tvMd5.setCellValueFactory(new PropertyValueFactory<>("md5"));
            tvVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        }
        else{
            tbvTaula.getItems().addAll(taula);
            tvUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
            tvMd5.setCellValueFactory(new PropertyValueFactory<>("md5"));
            tvVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        }

    }

    public void taulaBeteEz(String line,Model model){

        List<Model> emaitza = DBHasiera.getInstance().jadaDatuBasean(line,txtTestua.getText());
        emaitza.add(model);
        ObservableList<Model> taula = FXCollections.observableArrayList(emaitza);

        if(tbvTaula.getItems().size()==0){

            tbvTaula.setItems(taula);
            tbvTaula.setEditable(true);
            tvUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
            tvMd5.setCellValueFactory(new PropertyValueFactory<>("md5"));
            tvVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        }
        else{
            tbvTaula.getItems().addAll(taula);
            tvUrl.setCellValueFactory(new PropertyValueFactory<>("url"));
            tvMd5.setCellValueFactory(new PropertyValueFactory<>("md5"));
            tvVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        }
        versionEditatu();
        versionCommit();

    }

    public String md5Kalkulatu(String lala) throws IOException, NoSuchAlgorithmException {

        URL url = new URL(lala+"/README");
        InputStream is = url.openStream();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String digest = getDigest(is, md, 2048);

        return digest;
    }

    public static String getDigest(InputStream is, MessageDigest md, int byteArraySize)
            throws NoSuchAlgorithmException, IOException {

        md.reset();
        byte[] bytes = new byte[byteArraySize];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            md.update(bytes, 0, numBytes);
        }
        byte[] digest = md.digest();
        String result = new String(Hex.encodeHex(digest));
        return result;
    }

    public void versionEditatu(){
        Callback<TableColumn<Model, String>, TableCell<Model, String>> defaultTextFieldCellFactory
                = TextFieldTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return null;
            }

            @Override
            public String fromString(String string) {
                return null;
            }
        });

        tvVersion.setCellFactory(col -> {
            TableCell<Model, String> cell = defaultTextFieldCellFactory.call(col);

            cell.setOnMouseClicked(event -> {
                    cell.setEditable(true);
            });

            return cell ;
        });
    }

    public void versionCommit(){
        tvVersion.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow())
                            .setVersion(t.getNewValue());
                });
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
