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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Cadastro {
    public JPanel pnlCadastro;
    private JTextField txtNome;
    private JTextField txtSobreNome;
    private JTextField txtUser;
    private JFormattedTextField fTxtEmail;
    private JPasswordField passPswrd;
    private JPasswordField passConfPswrd;
    private JButton butCad;
    private JLabel lblErro;
    private JButton voltarButton;
    private JPanel previous_panel;
    private JFrame frame;

    /**
     * Converte string de senha para BigInt por hashing MD5
     *
     * @param pass : Array de caracteres da senha textual
     * @return BigInt com hashing
     */
    private BigInteger criptoPass(char[] pass) {
        // Define digest para MD5
        MessageDigest md;

        // Lista de bytes para hashing
        byte[] bts = Arrays.toString(pass).getBytes(StandardCharsets.UTF_8);

        try {
            // Inicializa digest
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // Retorna caso erro de algoritmo
            e.printStackTrace();
            return BigInteger.valueOf(-1);
        }

        // Limpa digest
        md.reset();
        // Faz hash para array de bytes
        byte[] cripto = md.digest(bts);
        // Converte para BigInt para adição na DataBase
        return new BigInteger(1, cripto);
    }

    // Limites de tamanho de string de campo
    private final int MIN_FIELD_SIZE = 3,
            MAX_FIELD_SIZE = 256;

    /**
     * Confere campos de entrada para cadastro
     *
     * @return boolean : Todos os campos estão dentro da especificação
     */
    private boolean confereCampos() {
        // Confere entrada de nome
        boolean conf = (txtNome.getText().length() < MAX_FIELD_SIZE &&
                txtNome.getText().length() > MIN_FIELD_SIZE);
        // Confere entrada de sobrenome
        conf &= (txtSobreNome.getText().length() < MAX_FIELD_SIZE &&
                txtSobreNome.getText().length() > MIN_FIELD_SIZE);
        // Confere entrada de usuário
        conf &= (txtUser.getText().length() < MAX_FIELD_SIZE &&
                txtUser.getText().length() > MIN_FIELD_SIZE);
        // Confere entrada de email
        conf &= (fTxtEmail.getText().length() < MAX_FIELD_SIZE &&
                fTxtEmail.getText().length() > MIN_FIELD_SIZE);
        // Confere entrada de senha
        conf &= (passPswrd.getPassword().length > MIN_FIELD_SIZE &&
                passPswrd.getPassword().length < MAX_FIELD_SIZE);
        // Confere confirmação de senha
        conf &= Arrays.equals(passPswrd.getPassword(), passConfPswrd.getPassword());

        return conf;
    }

    /**
     * Cadastra o usuário com as informações inseridas
     *
     * @return BigInt com hashing
     */
    private void cadastra() {
        if (!butCad.isEnabled()) {
            return;
        }
        boolean sucesso = FuncoesDB.cadastroUsuario(
                txtNome.getText().trim(),
                txtSobreNome.getText().trim(),
                txtUser.getText().trim(),
                fTxtEmail.getText().trim(),
                FuncoesDB.hashPass(passPswrd.getPassword())
        );

        if (sucesso) {
            frame.setContentPane(previous_panel);
            frame.pack();
        } else
            lblErro.setVisible(true);
    }

    /**
     * Inicializador da classe de Cadastro
     *
     * @param frame          : frame onde estão os elementos gráficos
     * @param previous_panel : painel anterior
     * @return BigInt com hashing
     */
    public Cadastro(JFrame frame, JPanel previous_panel) {
        this.frame = frame;
        this.previous_panel = previous_panel;
        butCad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cadastra();
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
        txtNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butCad.setEnabled(confereCampos());
            }
        });
        txtSobreNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butCad.setEnabled(confereCampos());
            }
        });
        txtUser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butCad.setEnabled(confereCampos());
            }
        });
        fTxtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butCad.setEnabled(confereCampos());
            }
        });
        passPswrd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butCad.setEnabled(confereCampos());
            }
        });
        passConfPswrd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butCad.setEnabled(confereCampos());
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        pnlCadastro = new JPanel();
        pnlCadastro.setLayout(new FormLayout("fill:10pt:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,fill:458px:grow,left:0dlu:noGrow,fill:10pt:noGrow", "center:d:grow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
        final JLabel label1 = new JLabel();
        label1.setText("Primeiro nome");
        CellConstraints cc = new CellConstraints();
        pnlCadastro.add(label1, cc.xy(2, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setText("Sobrenome");
        pnlCadastro.add(label2, cc.xy(2, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("Usuário");
        pnlCadastro.add(label3, cc.xy(2, 9, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("Email");
        pnlCadastro.add(label4, cc.xy(2, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label5 = new JLabel();
        label5.setText("Senha");
        pnlCadastro.add(label5, cc.xy(2, 13, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label6 = new JLabel();
        label6.setText("Confirme a senha");
        pnlCadastro.add(label6, cc.xy(2, 15, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, -1, 26, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Cadastro");
        pnlCadastro.add(label7, cc.xyw(2, 3, 4, CellConstraints.CENTER, CellConstraints.DEFAULT));
        txtNome = new JTextField();
        pnlCadastro.add(txtNome, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        txtSobreNome = new JTextField();
        pnlCadastro.add(txtSobreNome, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        txtUser = new JTextField();
        pnlCadastro.add(txtUser, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        fTxtEmail = new JFormattedTextField();
        pnlCadastro.add(fTxtEmail, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        passPswrd = new JPasswordField();
        pnlCadastro.add(passPswrd, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        passConfPswrd = new JPasswordField();
        pnlCadastro.add(passConfPswrd, cc.xy(5, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        pnlCadastro.add(spacer1, cc.xyw(2, 1, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        pnlCadastro.add(spacer2, cc.xyw(2, 21, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JSeparator separator1 = new JSeparator();
        pnlCadastro.add(separator1, cc.xy(4, 5, CellConstraints.FILL, CellConstraints.FILL));
        lblErro = new JLabel();
        lblErro.setEnabled(true);
        lblErro.setForeground(new Color(-43691));
        lblErro.setHorizontalAlignment(0);
        lblErro.setText("Erro no cadastro! Por favor tente novamente.");
        lblErro.setVisible(false);
        pnlCadastro.add(lblErro, cc.xyw(2, 19, 4));
        butCad = new JButton();
        butCad.setEnabled(false);
        Font butCadFont = this.$$$getFont$$$(null, -1, 22, butCad.getFont());
        if (butCadFont != null) butCad.setFont(butCadFont);
        butCad.setText("Cadastrar");
        pnlCadastro.add(butCad, cc.xy(5, 17));
        voltarButton = new JButton();
        voltarButton.setText("Voltar");
        pnlCadastro.add(voltarButton, cc.xy(2, 17));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pnlCadastro;
    }
}
