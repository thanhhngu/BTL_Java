package service_layer;

import model_layer.methodPayment;
import repository_layer.PaymentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DBconnection;

public class PaymentService {
    private final PaymentRepository paymentRepository = new PaymentRepository();

   
    public List<methodPayment> getAllPaymentMethods() {
        List<methodPayment> result = new ArrayList<methodPayment>();
        String sql = "SELECT payID, paymentMethod FROM Method_Payment ORDER BY payID";

        try (Connection con = DBconnection.openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("[PM-REPO] SQL = " + sql);

            while (rs.next()) {
                String payID = rs.getString("payID");
                String paymentMethod = rs.getString("paymentMethod");

                System.out.println("[PM-REPO] row -> payID=" + payID
                        + ", paymentMethod=" + paymentMethod);

                result.add(new methodPayment(payID, paymentMethod));
            }

            System.out.println("[PM-REPO] total rows = " + result.size());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
