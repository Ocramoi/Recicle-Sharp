import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HubInit {
    public JPanel pnlHub;
    private JTabbedPane tpnlPontos;
    private JPanel pnlButsPontos;
    private JButton button1;
    private JComboBox cmbTipo;
    private JComboBox cmbValor;
    private JTextField txtUsr;
    private JPanel pnlPontos;
    private JPanel pnlNovoPonto;

    private JFrame frame;

    public HubInit(JFrame frame) {
        this.frame = frame;
        tpnlPontos.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
            }
        });
    }
}
