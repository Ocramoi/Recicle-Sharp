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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
     */
    private void getHistory() {
        ResourceBundle strs = ResourceBundle.getBundle("Strings");
        ArrayList<Ponto> pontosFiltro = FuncoesDB.retornaPontos("usr", currUser.getUsr());
        Collections.reverse(pontosFiltro);
        pnlHist.setLayout(new BoxLayout(pnlHist, BoxLayout.Y_AXIS));
        pnlHist.removeAll();

        if (pontosFiltro.size() == 0) {
            JLabel tmp = new JLabel("Nenhum registro encontrado.");
            tmp.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHist.add(tmp);
        }

        for (Ponto p : pontosFiltro) {
            JLabel tmp = new JLabel(String.format(strs.getString("COLETA_STR"), p.endereco, p.usuario, p.tipoStr(), p.situacaoStr(), p.quantidade, String.join(", ", p.diasStr()), p.horarios, p.latitude, p.longitude));
            tmp.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHist.add(tmp);
        }
    }

    /**
     * Atualiza a lista de pontos de coleta filtrados
     */
    private void getFilter() {
        ResourceBundle strs = ResourceBundle.getBundle("Strings");

        int filter_column_id = cmbValor.getSelectedIndex();

        ArrayList<Ponto> pontosFiltro;

        int dia = -1;

        switch (filter_column_id) {
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

        if (pontosFiltro.size() == 0) {
            JLabel tmp = new JLabel("Nenhum registro encontrado.");
            tmp.setHorizontalAlignment(SwingConstants.CENTER);
            pnlHist.add(tmp);
        }

        for (Ponto p : pontosFiltro) {
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
     */
    private void updateLayoutUser() {
        labelNome.setText("Olá, " + currUser.getNome() + " " + currUser.getSobrenome());
        this.frame.setTitle("Recicle # - " + currUser.getNome() + " " + currUser.getSobrenome());

        txtNome.setText(currUser.getNome());
        txtSobreNome.setText(currUser.getSobrenome());
        fTxtEmail.setText(currUser.getEmail());
        passConfPswrd.setText("");
        passPswrd.setText("");
        butAtualizar.setEnabled(false);
    }

    /**
     * Atualiza os dados do usuário
     */
    private void atualizaUsuario() {
        if (!butAtualizar.isEnabled()) {
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
     * @param frame    : frame onde estão os elementos gráficos
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
                switch (cmbValor.getSelectedIndex()) {
                    case 0:
                        DefaultComboBoxModel<String> model_vazio = new DefaultComboBoxModel<>(new String[]{});
                        cmbTipo.setModel(model_vazio);
                        cmbTipo.setEnabled(false);
                        break;

                    case 1:
                        DefaultComboBoxModel<String> model_tipo = new DefaultComboBoxModel<>(new String[]{"Eletrônico", "Reciclável", "Orgânico", "Comum"});
                        cmbTipo.setModel(model_tipo);
                        cmbTipo.setEnabled(true);
                        break;

                    default:
                        DefaultComboBoxModel<String> model_dia = new DefaultComboBoxModel<>(new String[]{"Domingo", "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado"});
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
                switch (tpnlPontos.getSelectedIndex()) {
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
                } else {
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


                if (fazCadastro()) {
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
                } else {
                    JOptionPane.showMessageDialog(frame, "Erro ao Cadastrar Ponto!");
                }
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
        pnlHub = new JPanel();
        pnlHub.setLayout(new FormLayout("fill:d:grow", "fill:d:noGrow"));
        tpnlPontos = new JTabbedPane();
        CellConstraints cc = new CellConstraints();
        pnlHub.add(tpnlPontos, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        tpnlPontos.addTab("Pontos", scrollPane1);
        pnlButsPontos = new JPanel();
        pnlButsPontos.setLayout(new FormLayout("fill:1pt:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:1pt:noGrow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:1pt:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
        pnlButsPontos.setVisible(true);
        scrollPane1.setViewportView(pnlButsPontos);
        btn_filter = new JButton();
        btn_filter.setText(" ↻ ");
        pnlButsPontos.add(btn_filter, cc.xy(11, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        pnlButsPontos.add(spacer1, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        pnlButsPontos.add(spacer2, cc.xy(15, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        cmbTipo = new JComboBox();
        cmbTipo.setEnabled(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("...");
        cmbTipo.setModel(defaultComboBoxModel1);
        pnlButsPontos.add(cmbTipo, cc.xy(7, 1));
        cmbValor = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Nenhum");
        defaultComboBoxModel2.addElement("Tipo de resíduo");
        defaultComboBoxModel2.addElement("Dias");
        cmbValor.setModel(defaultComboBoxModel2);
        cmbValor.setToolTipText("Selecione o campo de filtro");
        pnlButsPontos.add(cmbValor, cc.xy(5, 1));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        pnlButsPontos.add(spacer3, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        pnlButsPontos.add(spacer4, cc.xy(13, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        pnlPontos = new JPanel();
        pnlPontos.setLayout(new GridBagLayout());
        pnlButsPontos.add(pnlPontos, cc.xyw(3, 3, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JScrollPane scrollPane2 = new JScrollPane();
        pnlButsPontos.add(scrollPane2, cc.xyw(3, 5, 11, CellConstraints.FILL, CellConstraints.FILL));
        pnlFiltro = new JPanel();
        pnlFiltro.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        scrollPane2.setViewportView(pnlFiltro);
        final JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setHorizontalScrollBarPolicy(31);
        tpnlPontos.addTab("Novo ponto", scrollPane3);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:10pt:noGrow,left:4dlu:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:5dlu:noGrow,fill:10pt:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        scrollPane3.setViewportView(panel1);
        final JLabel label1 = new JLabel();
        label1.setText("Tipo de resíduo");
        panel1.add(label1, cc.xy(3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
        btnCadastro = new JButton();
        btnCadastro.setEnabled(false);
        btnCadastro.setText("Cadastrar");
        panel1.add(btnCadastro, cc.xyw(3, 27, 3));
        final JLabel label2 = new JLabel();
        label2.setText("Endereço");
        panel1.add(label2, cc.xy(3, 3, CellConstraints.CENTER, CellConstraints.DEFAULT));
        cmbTipoInsercao = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("Eletrônico");
        defaultComboBoxModel3.addElement("Reciclável");
        defaultComboBoxModel3.addElement("Orgânico");
        defaultComboBoxModel3.addElement("Comum");
        cmbTipoInsercao.setModel(defaultComboBoxModel3);
        panel1.add(cmbTipoInsercao, cc.xy(5, 1));
        txtEndereco = new JTextField();
        panel1.add(txtEndereco, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label3 = new JLabel();
        label3.setText("Peso~ (kg)");
        panel1.add(label3, cc.xy(3, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
        spnPeso = new JSpinner();
        panel1.add(spnPeso, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("Dias para coleta");
        panel1.add(label4, cc.xy(3, 7, CellConstraints.CENTER, CellConstraints.DEFAULT));
        domingoCheckBox = new JCheckBox();
        domingoCheckBox.setText("Domingo");
        panel1.add(domingoCheckBox, cc.xy(5, 7));
        segundaFeiraCheckBox = new JCheckBox();
        segundaFeiraCheckBox.setText("Segunda-feira");
        panel1.add(segundaFeiraCheckBox, cc.xy(5, 9));
        tercaFeiraCheckBox = new JCheckBox();
        tercaFeiraCheckBox.setText("Terça-feira");
        panel1.add(tercaFeiraCheckBox, cc.xy(5, 11));
        quartaFeiraCheckBox = new JCheckBox();
        quartaFeiraCheckBox.setText("Quarta-feira");
        panel1.add(quartaFeiraCheckBox, cc.xy(5, 13));
        quintaFeiraCheckBox = new JCheckBox();
        quintaFeiraCheckBox.setText("Quinta-feira");
        panel1.add(quintaFeiraCheckBox, cc.xy(5, 15));
        sabadoCheckBox = new JCheckBox();
        sabadoCheckBox.setText("Sábado");
        panel1.add(sabadoCheckBox, cc.xy(5, 19));
        sextaFeiraCheckBox = new JCheckBox();
        sextaFeiraCheckBox.setText("Sexta-feira");
        panel1.add(sextaFeiraCheckBox, cc.xy(5, 17));
        final JLabel label5 = new JLabel();
        label5.setText("Horários disponíveis");
        panel1.add(label5, cc.xy(3, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panel1.add(panel2, cc.xy(5, 21));
        spnHora = new JSpinner();
        panel2.add(spnHora, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label6 = new JLabel();
        label6.setText("h");
        panel2.add(label6, cc.xy(3, 3));
        spnMin = new JSpinner();
        panel2.add(spnMin, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label7 = new JLabel();
        label7.setText("min");
        panel2.add(label7, cc.xy(7, 3));
        txtPosicao = new JTextField();
        panel1.add(txtPosicao, cc.xy(5, 23));
        obterPosicaoButton = new JButton();
        obterPosicaoButton.setText("Obter posição");
        panel1.add(obterPosicaoButton, cc.xy(5, 25));
        final JLabel label8 = new JLabel();
        label8.setText("Posição no mapa");
        panel1.add(label8, cc.xy(3, 23, CellConstraints.CENTER, CellConstraints.DEFAULT));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        tpnlPontos.addTab("Histórico", panel3);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel3.add(scrollPane4, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
        pnlHist = new JPanel();
        pnlHist.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        scrollPane4.setViewportView(pnlHist);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        tpnlPontos.addTab("Perfil", panel4);
        pnlPerfil = new JPanel();
        pnlPerfil.setLayout(new FormLayout("fill:10pt:noGrow,fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,fill:458px:grow,left:0dlu:noGrow,fill:10pt:noGrow", "center:d:grow,top:4dlu:noGrow,center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
        panel4.add(pnlPerfil, cc.xy(1, 1));
        final JLabel label9 = new JLabel();
        label9.setText("Primeiro nome");
        pnlPerfil.add(label9, cc.xy(2, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label10 = new JLabel();
        label10.setText("Sobrenome");
        pnlPerfil.add(label10, cc.xy(2, 7, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label11 = new JLabel();
        label11.setText("Email");
        pnlPerfil.add(label11, cc.xy(2, 9, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label12 = new JLabel();
        label12.setText("Senha");
        pnlPerfil.add(label12, cc.xy(2, 11, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel label13 = new JLabel();
        label13.setText("Confirme a senha");
        pnlPerfil.add(label13, cc.xy(2, 13, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        labelNome = new JLabel();
        Font labelNomeFont = this.$$$getFont$$$(null, -1, 26, labelNome.getFont());
        if (labelNomeFont != null) labelNome.setFont(labelNomeFont);
        labelNome.setText("Olá, ");
        pnlPerfil.add(labelNome, cc.xyw(2, 3, 4, CellConstraints.CENTER, CellConstraints.DEFAULT));
        txtNome = new JTextField();
        pnlPerfil.add(txtNome, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        txtSobreNome = new JTextField();
        pnlPerfil.add(txtSobreNome, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        fTxtEmail = new JFormattedTextField();
        pnlPerfil.add(fTxtEmail, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        passPswrd = new JPasswordField();
        pnlPerfil.add(passPswrd, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        passConfPswrd = new JPasswordField();
        pnlPerfil.add(passConfPswrd, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        pnlPerfil.add(spacer5, cc.xyw(2, 1, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        pnlPerfil.add(spacer6, cc.xyw(2, 19, 4, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JSeparator separator1 = new JSeparator();
        pnlPerfil.add(separator1, cc.xy(4, 5, CellConstraints.FILL, CellConstraints.FILL));
        lblErroAtualizar = new JLabel();
        lblErroAtualizar.setEnabled(true);
        lblErroAtualizar.setForeground(new Color(-43691));
        lblErroAtualizar.setHorizontalAlignment(0);
        lblErroAtualizar.setText("Erro ao atualizar! Por favor tente novamente.");
        lblErroAtualizar.setVisible(false);
        pnlPerfil.add(lblErroAtualizar, cc.xyw(2, 17, 4));
        butAtualizar = new JButton();
        butAtualizar.setEnabled(false);
        Font butAtualizarFont = this.$$$getFont$$$(null, -1, 22, butAtualizar.getFont());
        if (butAtualizarFont != null) butAtualizar.setFont(butAtualizarFont);
        butAtualizar.setText("Atualizar");
        pnlPerfil.add(butAtualizar, cc.xy(5, 15));
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
        return pnlHub;
    }
}
