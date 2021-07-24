import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login {
    public JPanel pnlLogin;
    private JTextField entradaUser;
    private JPasswordField entradaSenha;
    private JButton butLogin;
    private JLabel lblCadastrar;

    private JFrame frame;

    public Login(JFrame frame) {
        this.frame = frame;

        lblCadastrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.setContentPane(new Cadastro().pnlCadastro);
                frame.pack();
            }
        });
        butLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }
}
