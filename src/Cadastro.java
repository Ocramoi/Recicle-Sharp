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
        if(!butCad.isEnabled()){
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
     * @param frame : frame onde estão os elementos gráficos
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
}
