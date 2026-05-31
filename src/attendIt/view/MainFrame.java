package attendIt.view;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Attend IT System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Chamados", new ChamadoView());
        abas.addTab("Técnicos", new TecnicoView());
        add(abas);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
