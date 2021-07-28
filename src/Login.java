import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    /**
     * Faz o login do usuário com as informações no formulário
     */
    private void logUsr() {
        if (!butLogin.isEnabled()) {
            return;
        }
        Usuario usrRegistrado = FuncoesDB.loginUsuario(txtUser.getText(), passPswrd.getPassword());
        if (usrRegistrado == null) {
            lblErro.setVisible(true);
            return;
        }

        frame.setContentPane(new HubInit(frame, usrRegistrado).pnlHub);
        frame.pack();
    }

    /**
     * Inicializador da classe de Login
     *
     * @param frame          : frame onde estão os elementos gráficos
     * @param previous_panel : painel anterior
     */
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
