import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login {
    public JPanel panelLogin;
    private JTextField entradaUser;
    private JPasswordField entradaSenha;
    private JButton butLogin;
    private JLabel cadastrar;

    public Login() {
        cadastrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
