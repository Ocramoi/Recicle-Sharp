import javax.swing.*;
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
     * @author Marco Toledo
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
     * Trata entrada de campos e insere no banco de dados com tais valores.
     *
     * @author Marco Toledo
     * @return boolean : Sucesso de inserção
     */
    private boolean fazCadastro() {
        // Declara nova conexão
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        // Auxiliar de retorno
        boolean ret = true;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("ResourceDB");
            // Lê e trata campos
            String nome = txtNome.getText().trim(),
                    sobre = txtSobreNome.getText().trim(),
                    usr = txtUser.getText().trim(),
                    email = fTxtEmail.getText().trim();
            // Cria hash de senha lida
            BigInteger pass = criptoPass(passPswrd.getPassword());

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);

            // Executa query com base nos recursos
            query.execute(String.format(res.getString("INSERT_STR"),
                    nome,
                    sobre,
                    usr,
                    email,
                    pass));
        } catch (SQLException | MissingResourceException throwables) {
            // Confere erro de inserção / conexão
            throwables.printStackTrace();
            ret = false;
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return ret;
    }

    public Cadastro() {
        butCad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                fazCadastro();
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
}
