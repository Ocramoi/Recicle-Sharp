/**
 * Recycle#
 * Trabalho Final - Programação Orientada a Objetos
 *
 * Autores:
 *      Eduardo Rodrigues Amaral - 11735021
 *      Frederico Xavier Capanema - 12433364
 *      João Marcos Cardoso da Silva - 11795314
 *      Marco Antônio Ribeiro de Toledo - 11796419
 */

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
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

    /**
     * Faz o login na conta do usuário, caso o mesmo já tenha entrado nessa máquina
     *
     * @return boolean indicando se foi possível fazer o login automático
     */
    private boolean entraConta() {
        Usuario currUser = null;
        Connection con = null;
        Connection dbCon = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        String userRetorno = null;
        Date dateRetorno = null;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("LoginDB");
            ResourceBundle dbRes = ResourceBundle.getBundle("ResourceDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/login.sqlite");
            dbCon = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");
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

            Statement usrQuery = dbCon.createStatement();
            usrQuery.setQueryTimeout(10);
            ResultSet currUsrSet = usrQuery.executeQuery(String.format(dbRes.getString("LOGIN_LAST_USE"),userRetorno));
            if(currUsrSet.next()){
                currUser = new Usuario(currUsrSet);
            }

        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
                assert dbCon != null;
                dbCon.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (userRetorno != null && dateRetorno != null && currUser !=null) {

            frame.setContentPane(new HubInit(frame,currUser).pnlHub);
            frame.pack();
            return true;
        }
        return false;
    }

    /**
     * Cria eventos de interação da página
     *
     */
    public PagInicial() {
        new Servidor().start();
        loginCadastroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!entraConta()) {
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
     * @return boolean : sucesso de alteração
     */
    private static boolean iniciaEstiloUI(boolean useGTKStyleIfAvailable) {
        // Contabiliza erro de alteração
        boolean retorno = true;

        // Lê estilos dsponíveis para a interface
        ArrayList<String> estilos = new ArrayList();
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            estilos.add(info.getClassName());

        // Caso possível, usa estilo de sistema com GTK+
        try {
            if (useGTKStyleIfAvailable && estilos.contains("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))
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
     * @param args : argumentos de execução
     */
    public static void main(String[] args) {
        frame = new JFrame("Recicle#");

        iniciaEstiloUI(false);

        frame.setMinimumSize(new Dimension(500,300));
        frame.setMaximumSize(new Dimension(500,300));
        frame.setResizable(false);
        frame.setContentPane(new PagInicial().pnlInit);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
