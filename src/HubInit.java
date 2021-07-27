import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HubInit {
    public JPanel pnlHub;
    private JTabbedPane tpnlPontos;
    private JPanel pnlButsPontos;
    private JButton button1;
    private JComboBox cmbTipo;
    private JComboBox cmbValor;
    private JTextField txtUsr;
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

    private JFrame frame;

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
        switch (cmbTipo.getSelectedIndex()) {
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
        String horarios = String.format("%s%s",
                spnHora.getValue(), spnMin.getValue());

        String vals = txtPosicao.getText().split("@")[1],
                latStr = vals.split(",")[0],
                longStr = vals.split(",")[1];
        float lat = Float.parseFloat(latStr),
                lon = Float.parseFloat(longStr);

        System.out.printf("%s %s %s %s %s %s %s %s %s%n",
                FuncoesLogin.usuarioLocal(),
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
                FuncoesLogin.usuarioLocal(),
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

    private boolean confereCampos() {
        return (txtEndereco.getText().length() > 0 &&
                (domingoCheckBox.isSelected() || segundaFeiraCheckBox.isSelected() || tercaFeiraCheckBox.isSelected() || quartaFeiraCheckBox.isSelected() || quintaFeiraCheckBox.isSelected() || sextaFeiraCheckBox.isSelected() || sabadoCheckBox.isSelected()) &&
                txtPosicao.getText().length() > 0);
    }

    public HubInit(JFrame frame) {
        this.frame = frame;
        this.spnPeso.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        this.spnHora.setModel(new SpinnerNumberModel(0, 0, 23, 1));
        this.spnMin.setModel(new SpinnerNumberModel(0, 0, 59, 1));

        tpnlPontos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
            }
        });

        obterPosicaoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(frame, "A página do Google Maps deve ser carregada em seguida. Clique na busca de posição atual e copie a URL para a caixa de texto que sua latitude/longitude serão extraídos.");

                String url = "https://www.google.com.br/maps/";
//                if (Desktop.isDesktopSupported()) {
                if (false) {
                    System.out.println("ASAAAA");
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI(url));
                    } catch (IOException | URISyntaxException exception) {
                        exception.printStackTrace();
                    }
                } else{
                    Runtime runtime = Runtime.getRuntime();
                    try {
//                        Process p = runtime.exec("/usr/bin/xdg-open " + url);
                        runtime.exec("/usr/bin/firefox " + url);
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
                fazCadastro();
            }
        });
    }
}
