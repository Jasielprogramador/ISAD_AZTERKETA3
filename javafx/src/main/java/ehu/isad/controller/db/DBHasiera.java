package ehu.isad.controller.db;


import ehu.isad.model.Model;


import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBHasiera {

    private static final DBHasiera instance = new DBHasiera();

    public static DBHasiera getInstance() {
        return instance;
    }

    public List<Model> jadaDatuBasean(String text,String url) {

        String query = "select md5,version from checksums where md5 = '"+text+"'";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);

        List<Model> emaitza = new ArrayList<>();

        try {
            while (rs.next()) {
                if(!rs.equals(null)){
                    String md5 = rs.getString("md5");
                    String version = rs.getString("version");
                    Model model = new Model(url,md5,version);
                    emaitza.add(model);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return emaitza;
    }

    public void datuBaseanTxertatu(Model model){
        String query = "insert into checksums values ('"+model.getUrl()+"','"+model.getMd5()+"','"+model.getVersion()+"')";
        DBKudeatzaile dbKudeatzaile = DBKudeatzaile.getInstantzia();
        ResultSet rs = dbKudeatzaile.execSQL(query);
    }

}
