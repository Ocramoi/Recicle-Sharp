import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

public class HubInit {
    private Usuario currUser;
    public JPanel pnlHub;
    private JTabbedPane tpnlPontos;
    private JPanel pnlButsPontos;
    private JButton btn_filter;
    private JComboBox cmbTipo;
    private JComboBox cmbValor;
    private JPanel pnlPontos;
    private JButton btnCadastro;
    private JComboBox cmbTipoInsercao;
    private JTextField txtEndereco;
    private JSpinner spnPeso;
    private JCheckBox domingoCheckBox;
    private JCheckBox segundaFeiraCheckBox;
    private JCheckBox tercaFeiraCheckBox;
    private JCheckBox quartaFeiraCheckBox;
    private JCheckBox quintaFeiraCheckBox;
    private JCheckBox sabadoCheckBox;
    private JCheckBox sextaFeiraCheckBox;
    private JSpinner spnHora;
    private JSpinner spnMin;
    private JButton obterPosicaoButton;
    private JTextField txtPosicao;
    private JPanel pnlPerfil;
    private JTextField txtNome;
    private JTextField txtSobreNome;
    private JFormattedTextField fTxtEmail;
    private JPasswordField passPswrd;
    private JPasswordField passConfPswrd;
    private JLabel lblErroAtualizar;
    private JButton butAtualizar;
    private JLabel labelNome;
    private JPanel pnlHist;
    private JPanel pnlFiltro;

    private JFrame frame;

    private final int MIN_FIELD_SIZE = 3,
            MAX_FIELD_SIZE = 256;

    /**
     * Atualiza o histórico de pontos do usuário
     *
     */
    private void getHistory(){
        ResourceBundle strs = ResourceBundle.getBundle("Strings");
        ArrayList<Ponto> pontosFiltro = FuncoesDB.retornaPontos("usr", currUser.getUsr());
        Collections.reverse(pontosFiltro);
        pnlHist.setLayout(new BoxLayout(pnlHist, BoxLayout.Y_AXIS));
        pnlHist.removeAll();

        if (pontosFiltro.size() == 0){
            JLabel tmp = new JLabel("Nenhum registro encontrado.");
            tmp.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHist.add(tmp);
        }

        for(Ponto p: pontosFiltro){
            JLabel tmp = new JLabel(String.format(strs.getString("COLETA_STR"), p.endereco,p.usuario,p.tipoStr(),p.situacaoStr(), p.quantidade, String.join(", ",p.diasStr()), p.horarios, p.latitude, p.longitude));
            tmp.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHist.add(tmp);
        }
    }

    /**
     * Atualiza a lista de pontos de coleta filtrados
     *
     */
    private void getFilter(){
        ResourceBundle strs = ResourceBundle.getBundle("Strings");

        int filter_column_id = cmbValor.getSelectedIndex();

        ArrayList<Ponto> pontosFiltro;

        int dia = -1;

        switch(filter_column_id){
            case 0:
                pontosFiltro = FuncoesDB.retornaPontos();
                break;
            case 1:
                String[] tipos = {"E", "R", "O", "C"};
                pontosFiltro = FuncoesDB.retornaPontos("tipoResiduo", tipos[cmbTipo.getSelectedIndex()]);
                break;
            default:
                dia = cmbTipo.getSelectedIndex();
                pontosFiltro = FuncoesDB.retornaPontos();
                break;
        }



        Collections.reverse(pontosFiltro);
        pnlFiltro.setLayout(new BoxLayout(pnlFiltro, BoxLayout.Y_AXIS));
        pnlFiltro.removeAll();

        if (pontosFiltro.size() == 0){
            JLabel tmp = new JLabel("Nenhum registro encontrado.");
            tmp.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHist.add(tmp);
        }

        for(Ponto p: pontosFiltro){
            if (dia == -1 || p.dias[dia]) {
                JLabel tmp = new JLabel(String.format(strs.getString("COLETA_STR"), p.endereco, p.usuario, p.tipoStr(), p.situacaoStr(), p.quantidade, String.join(", ", p.diasStr()), p.horarios, p.latitude, p.longitude));
                tmp.setHorizontalAlignment(SwingConstants.CENTER);
                pnlFiltro.add(tmp);
            }
        }
        pnlFiltro.revalidate();
        pnlFiltro.repaint();
    }

    /**
     * Atualiza o layout da tela de perfil do usuário
     *
     *
     */
    private void updateLayoutUser(){
        labelNome.setText("Olá, "+currUser.getNome()+" "+currUser.getSobrenome());
        this.frame.setTitle("Recicle # - "+currUser.getNome()+" "+currUser.getSobrenome());

        txtNome.setText(currUser.getNome());
        txtSobreNome.setText(currUser.getSobrenome());
        fTxtEmail.setText(currUser.getEmail());
        passConfPswrd.setText("");
        passPswrd.setText("");
        butAtualizar.setEnabled(false);
    }

    /**
     * Atualiza os dados do usuário
     *
     *
     */
    private void atualizaUsuario() {
        if(!butAtualizar.isEnabled()) {
            return;
        }

        boolean sucesso = FuncoesDB.atualizaUsuario(
                txtNome.getText().trim(),
                txtSobreNome.getText().trim(),
                this.currUser.getUsr(),
                fTxtEmail.getText().trim(),
                FuncoesDB.hashPass(passPswrd.getPassword())
        );

        if (sucesso) {
            this.currUser.setNome(txtNome.getText().trim());
            this.currUser.setSobrenome(txtSobreNome.getText().trim());
            this.currUser.setEmail(fTxtEmail.getText().trim());
            JOptionPane.showMessageDialog(frame, "Usuário Atualizado Com Sucesso!");
            this.updateLayoutUser();
        } else {
            lblErroAtualizar.setVisible(true);
        }
    }

    /**
     * Confere as validades dos campos de informação do usuário
     *
     *
     * @return booleano indicando se os campos são válidos
     */
    private boolean confereCamposAtualizar() {
        // Confere entrada de nome
        boolean conf = (txtNome.getText().length() < MAX_FIELD_SIZE &&
                txtNome.getText().length() > MIN_FIELD_SIZE);
        // Confere entrada de sobrenome
        conf &= (txtSobreNome.getText().length() < MAX_FIELD_SIZE &&
                txtSobreNome.getText().length() > MIN_FIELD_SIZE);
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
     * Realiza o cadastro de um ponto de coleta
     *
     * @return booleano indicando se houve sucesso
     */
    private boolean fazCadastro() {
        String strDias = "";
        strDias += (domingoCheckBox.isSelected() ? 'S' : 'N');
        strDias += (segundaFeiraCheckBox.isSelected() ? 'S' : 'N');
        strDias += (tercaFeiraCheckBox.isSelected() ? 'S' : 'N');
        strDias += (quartaFeiraCheckBox.isSelected() ? 'S' : 'N');
        strDias += (quintaFeiraCheckBox.isSelected() ? 'S' : 'N');
        strDias += (sextaFeiraCheckBox.isSelected() ? 'S' : 'N');
        strDias += (sabadoCheckBox.isSelected() ? 'S' : 'N');

        char tipo = ' ';
        switch (cmbTipoInsercao.getSelectedIndex()) {
            case 0:
                tipo = 'E';
                break;
            case 1:
                tipo = 'R';
                break;
            case 2:
                tipo = 'O';
                break;
            default:
                tipo = 'C';
                break;
        }
        String horarios = String.format("%s:%s",
                spnHora.getValue(), spnMin.getValue());

        String vals = txtPosicao.getText().split("@")[1],
                latStr = vals.split(",")[0],
                longStr = vals.split(",")[1];
        float lat = Float.parseFloat(latStr),
                lon = Float.parseFloat(longStr);


        System.out.printf("%s %s %s %s %s %s %s %s %s%n",
                currUser.getUsr(),
                tipo,
                txtEndereco.getText(),
                strDias,
                horarios,
                'P',
                (Integer) spnPeso.getValue(),
                lat,
                lon
                );

        boolean r = FuncoesDB.criaPonto(
                currUser.getUsr(),
                tipo,
                txtEndereco.getText(),
                strDias,
                horarios,
                'P',
                (Integer) spnPeso.getValue(),
                lat,
                lon
        );
        return r;
    }

    /**
     * Confere as validades dos campos de um ponto de coleta
     *
     * @return booleano indicando se os campos são válidos
     */
    private boolean confereCampos() {
        return (txtEndereco.getText().length() > 0 &&
                (domingoCheckBox.isSelected() || segundaFeiraCheckBox.isSelected() || tercaFeiraCheckBox.isSelected() || quartaFeiraCheckBox.isSelected() || quintaFeiraCheckBox.isSelected() || sextaFeiraCheckBox.isSelected() || sabadoCheckBox.isSelected()) &&
                txtPosicao.getText().length() > 0);
    }

    /**
     * Inicializador da classe de Login
     *
     * @param frame : frame onde estão os elementos gráficos
     * @param currUser : usuário atual que está conectado
     */
    public HubInit(JFrame frame, Usuario currUser) {
        this.currUser = currUser;
        this.frame = frame;

        this.updateLayoutUser();

        butAtualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                atualizaUsuario();
            }
        });

        btn_filter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                getFilter();
            }
        });

        cmbValor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch(cmbValor.getSelectedIndex()){
                    case 0:
                        DefaultComboBoxModel<String> model_vazio = new DefaultComboBoxModel<>(new String[]{});
                        cmbTipo.setModel(model_vazio);
                        cmbTipo.setEnabled(false);
                        break;

                    case 1:
                        DefaultComboBoxModel<String>model_tipo = new DefaultComboBoxModel<>(new String[] {"Eletrônico", "Reciclável", "Orgânico", "Comum"});
                        cmbTipo.setModel(model_tipo);
                        cmbTipo.setEnabled(true);
                        break;

                    default:
                        DefaultComboBoxModel<String> model_dia = new DefaultComboBoxModel<>(new String[] {"Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"});
                        cmbTipo.setModel(model_dia);
                        cmbTipo.setEnabled(true);
                        break;
                }
            }
        });

        txtNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butAtualizar.setEnabled(confereCamposAtualizar());
            }
        });
        txtSobreNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butAtualizar.setEnabled(confereCamposAtualizar());
            }
        });
        fTxtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butAtualizar.setEnabled(confereCamposAtualizar());
            }
        });
        passPswrd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butAtualizar.setEnabled(confereCamposAtualizar());
            }
        });
        passConfPswrd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                butAtualizar.setEnabled(confereCamposAtualizar());
            }
        });


        this.spnPeso.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        this.spnHora.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        this.spnMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        tpnlPontos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                switch (tpnlPontos.getSelectedIndex()){
                    case 2:
                        getHistory();
                        break;
                }
            }
        });

        obterPosicaoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(frame, "A página do Google Maps deve ser carregada em seguida. Clique na busca de posição atual e copie a URL para a caixa de texto que sua latitude/longitude serão extraídos.");

                String url = "https://www.google.com.br/maps/";
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI(url));
                    } catch (IOException | URISyntaxException exception) {
                        exception.printStackTrace();
                    }
                } else{
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        Process p = runtime.exec("/usr/bin/xdg-open " + url);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        txtEndereco.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        txtPosicao.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        domingoCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        segundaFeiraCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        tercaFeiraCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        quartaFeiraCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        quintaFeiraCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        sextaFeiraCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        sabadoCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                btnCadastro.setEnabled(confereCampos());
            }
        });
        btnCadastro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


                if(fazCadastro()){
                    JOptionPane.showMessageDialog(frame, "Ponto Cadastrado com Sucesso!");
                    txtEndereco.setText("");
                    txtPosicao.setText("");
                    spnPeso.setValue(0);
                    spnHora.setValue(0);
                    spnMin.setValue(0);
                    domingoCheckBox.setSelected(false);
                    segundaFeiraCheckBox.setSelected(false);
                    tercaFeiraCheckBox.setSelected(false);
                    quartaFeiraCheckBox.setSelected(false);
                    quintaFeiraCheckBox.setSelected(false);
                    sabadoCheckBox.setSelected(false);
                    sextaFeiraCheckBox.setSelected(false);
                }else{
                    JOptionPane.showMessageDialog(frame, "Erro ao Cadastrar Ponto!");
                }
            }
        });
    }
}
