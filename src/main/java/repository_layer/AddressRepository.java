package repository_layer;

import config.DBconnection;
import model_layer.addressCustomer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressRepository {

    private static final Logger LOGGER = Logger.getLogger(AddressRepository.class.getName());

    public List<addressCustomer> findByCustomerID(String customerID) {
        List<addressCustomer> list = new ArrayList<>();

        String sql = "SELECT addressID, customerID, address " +
                     "FROM Address_Customer " +
                     "WHERE customerID = ? " +
                     "ORDER BY addressID";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, customerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    addressCustomer item = new addressCustomer();
                    item.setAddressID(rs.getString("addressID"));
                    item.setCustomerID(rs.getString("customerID"));
                    item.setAddress(rs.getString("address"));
                    list.add(item);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE,
                    "findByCustomerID failed. SQLState=" + ex.getSQLState()
                            + ", ErrorCode=" + ex.getErrorCode(),
                    ex);
        }

        return list;
    }
}