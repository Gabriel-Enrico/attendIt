package attendIt.view;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("AttendIt de TI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Chamados",    new ChamadoView());
        abas.addTab("Atendimento", new AtendimentoView());
        abas.addTab("Técnicos",    new TecnicoView());
        abas.addTab("Usuários",    new UsuarioView());
        abas.addTab("Categorias",  new CategoriaView());
        abas.addTab("Relatório",  new RelatorioView());
        add(abas);

        abas.addChangeListener(e -> {
            Component aba = abas.getSelectedComponent();
            if (aba instanceof AtendimentoView) {
                ((AtendimentoView) aba).recarregar();
            }
            if (aba instanceof ChamadoView) {
                ((ChamadoView) aba).recarregar();
            }
            if (aba instanceof RelatorioView) {
                ((RelatorioView) aba).recarregar();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}