package repository_layer;

import config.DBconnection;
import model_layer.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public List<category> getAllCategories() {
        List<category> list = new ArrayList<>();

        String sql = "SELECT catgID, name FROM Category ORDER BY name";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                category c = new category();
                c.setCatgID(rs.getString("catgID"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}