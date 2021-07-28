import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PagInicial {
    private JPanel pnlInit;
    private JButton loginCadastroButton;
    private static JFrame frame;

    /**
     * Cria eventos de interação da página
     *
     * @author Marco Toledo
     */
    public PagInicial() {
        new Servidor().start();
        loginCadastroButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.setContentPane(new Login(frame, pnlInit).pnlLogin);
                frame.pack();
            }
        });
    }

    /**
     * Altera estilo de frame para GTK se possível, usand o primeiro estilo
     * da lista de sistema caso GTK+ não disponível.
     *
     * @return boolean : sucesso de alteração
     * @author Marco Toledo
     */
    private static boolean iniciaEstiloUI(boolean useGTKStyleIfAvailable) {
        // Contabiliza erro de alteração
        boolean retorno = true;

        // Lê estilos dsponíveis para a interface
        ArrayList<String> estilos = new ArrayList();
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            estilos.add(info.getClassName());

        // Caso possível, usa estilo de sistema com GTK+
        try {
            if (useGTKStyleIfAvailable && estilos.contains("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            else
                UIManager.setLookAndFeel(estilos.get(0));
        } catch (UnsupportedLookAndFeelException e) { // Trata erro
            System.out.println("Visual indisponível, usando default.");
            e.printStackTrace();
            retorno = false;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            retorno = false;
        }

        return retorno;
    }

    /**
     * Inicializa frame de início
     *
     * @param args : argumentos de execução
     * @author Marco Toledo
     */
    public static void main(String[] args) {
        frame = new JFrame("Recicle#");

        iniciaEstiloUI(false);

        frame.setMinimumSize(new Dimension(500, 300));
        frame.setMaximumSize(new Dimension(500, 300));
        frame.setResizable(false);
        frame.setContentPane(new PagInicial().pnlInit);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
