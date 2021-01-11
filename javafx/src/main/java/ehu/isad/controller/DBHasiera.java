package ehu.isad.controller;


import ehu.isad.model.CaptchaTaula;
import javafx.embed.swing.SwingFXUtils;


import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBHasiera {

    private static final DBHasiera instance = new DBHasiera();

    public static DBHasiera getInstance() {
        return instance;
    }

    public List<CaptchaTaula> datuBaseaKargatu() {

        List<CaptchaTaula> emaitza = new ArrayList<>();

        String query = "select * from captchas";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);
        try {
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String path = rs.getString("filename");
                Integer content = rs.getInt("value");
                String date = rs.getString("date");
                CaptchaTaula captchaTaula = new CaptchaTaula(id, path, content, date, irudiaLortu(path));
                emaitza.add(captchaTaula);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return emaitza;
    }

    public Integer azkenIdLortu(){
        String query = "select * from captchas";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);
        Integer e = 0;
        try {
            while (rs.next()) {
                e = rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return e+1;
    }

    public void datuBaseanTxertatu(CaptchaTaula captchaTaula){
        String query = "insert into captchas values ("+captchaTaula.getId()+",'"+captchaTaula.getPath()+"',"+captchaTaula.getContent()+","+captchaTaula.getDate()+")";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);
    }

    public void datuBaseaAktualizatu(Integer content, Integer id){
        String query = "update captchas set value =" +content+" where id = "+id;
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);
    }

    private Image irudiaLortu(String path) {
        return new Image(path);
    }


}
