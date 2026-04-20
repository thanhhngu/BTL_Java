package repository_layer;

import config.DBconnection;
import model_layer.methodPayment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentRepository {

    private static final Logger LOGGER = Logger.getLogger(PaymentRepository.class.getName());

    public List<methodPayment> findAll() {
        List<methodPayment> list = new ArrayList<methodPayment>();

        String sql = "SELECT paymentID, paymentMethod " +
                     "FROM PaymentMethod " +
                     "ORDER BY paymentID";

        try (
                Connection con = DBconnection.openConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                methodPayment item = new methodPayment();
                item.setPayID(rs.getString("paymentID"));
                item.setPaymentMethod(rs.getString("paymentMethod"));
                list.add(item);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE,
                    "findAll payment methods failed. SQLState=" + ex.getSQLState() + ", ErrorCode=" + ex.getErrorCode(),
                    ex);
        }

        return list;
    }
}
