import java.sql.*;
import java.util.ResourceBundle;

public class FuncoesLogin {
    public static String usuarioLocal() {
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        String userRetorno = null;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("LoginDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/login.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);
            ResultSet pesq = query.executeQuery(res.getString("SELECT_LOGIN"));

            while (pesq.next())
                userRetorno = pesq.getString("usr");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return userRetorno;
    }
}
