import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PagInicial {
    private JPanel pnlInit;
    private JButton loginCadastroButton;
    private static JFrame frame;

    private boolean entraConta() {
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        String userRetorno = null;
        Date dateRetorno = null;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("LoginDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/login.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);
            ResultSet pesq = query.executeQuery(res.getString("SELECT_LOGIN"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp curTime = new Timestamp(System.currentTimeMillis());
            String curDate = dateFormat.format(curTime);

            while (pesq.next()) {
                userRetorno = pesq.getString("usr");
                dateRetorno = dateFormat.parse(pesq.getString("lastLogin"));
            }

            try (PreparedStatement ps = con.prepareStatement(res.getString("UPDATE_LAST"))) {
                ps.setString(1, curDate);
                if (ps.executeUpdate() == 0)
                    throw new SQLException("Registro não atualizado!");
            }
        } catch (SQLException | ParseException throwables) {
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

        if (userRetorno != null && dateRetorno != null) {
            frame.setContentPane(new HubInit(frame).pnlHub);
            frame.pack();
            return true;
        }
        return false;
    }

    /**
     * Cria eventos de interação da página
     *
     * @author Marco Toledo
     */
    public PagInicial() {
        new Servidor().start();
        loginCadastroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (entraConta()) {
                    frame.setContentPane(new Login(frame, pnlInit).pnlLogin);
                    frame.pack();
                }
            }
        });
    }

    /**
     * Altera estilo de frame para GTK se possível, usand o primeiro estilo
     * da lista de sistema caso GTK+ não disponível.
     *
     * @author Marco Toledo
     * @return boolean : sucesso de alteração
     */
    private static boolean iniciaEstiloUI() {
        // Contabiliza erro de alteração
        boolean retorno = true;

        // Lê estilos dsponíveis para a interface
        ArrayList<String> estilos = new ArrayList();
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            estilos.add(info.getClassName());

        // Caso possível, usa estilo de sistema com GTK+
        try {
            if (estilos.contains("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            else
                UIManager.setLookAndFeel(estilos.get(0));
        } catch (UnsupportedLookAndFeelException e) { // Trata erro
            System.out.println("Visual indisponível, usando default.");
            e.printStackTrace();
            retorno = false;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            retorno = false;
        }

        return retorno;
    }

    /**
     * Inicializa frame de início
     *
     * @author Marco Toledo
     * @param args : argumentos de execução
     */
    public static void main(String[] args) {
        frame = new JFrame("Recicle#");

        iniciaEstiloUI();

        frame.setMinimumSize(new Dimension(500,300));
        frame.setContentPane(new PagInicial().pnlInit);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
