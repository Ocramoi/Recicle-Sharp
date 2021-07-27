import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Login {
    public JPanel pnlLogin;
    private JTextField txtUser;
    private JPasswordField passPswrd;
    private JButton butLogin;
    private JLabel lblCadastrar;
    private JLabel lblErro;
    private JButton voltarButton;

    private JFrame frame;

    private boolean confereCampos() {
        return (txtUser.getText().length() > 0 && passPswrd.getPassword().length > 0);
    }

    private void logUsr() {
        String usrRegistrado = FuncoesDB.loginUsuario(txtUser.getText(), passPswrd.getPassword());
        if (usrRegistrado.equals("")) {
            lblErro.setVisible(true);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        String curDate = dateFormat.format(curTime);

        Connection con = null;
        try {
            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/login.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);

            // Executa query com base nos recursos
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO login (usr, lastLogin) VALUES (?, ?)")) {
                ps.setString(1, usrRegistrado);
                ps.setString(2, curDate);
                ps.executeUpdate();
            }
            frame.setContentPane(new HubInit(frame).pnlHub);
            frame.pack();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                lblErro.setVisible(true);
            }
        }
    }

    public Login(JFrame frame, JPanel previous_panel) {
        this.frame = frame;

        lblCadastrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.setContentPane(new Cadastro(frame, pnlLogin).pnlCadastro);
                frame.pack();
            }
        });
        voltarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.setContentPane(previous_panel);
                frame.pack();
            }
        });
        butLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                logUsr();
            }
        });
        txtUser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butLogin.setEnabled(confereCampos());
            }
        });
        passPswrd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butLogin.setEnabled(confereCampos());
            }
        });
    }
}
