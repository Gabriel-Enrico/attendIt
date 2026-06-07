package attendIt.view;

import attendIt.dao.RelatorioDAO;
import attendIt.model.RelatorioChamado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class RelatorioView extends JPanel {

    private final RelatorioDAO dao = new RelatorioDAO();
    private final DefaultTableModel tableModel;
    private final JTable tabela;
    private final JComboBox<String> cbFiltro = new JComboBox<>(
            new String[]{"Todos", "Aberto", "Em_Atendimento", "Resolvido", "Fechado"});

    public RelatorioView() {
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filtro
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        topo.setBorder(BorderFactory.createTitledBorder("Filtrar por status"));
        topo.add(new JLabel("Status:"));
        topo.add(cbFiltro);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnExportar = new JButton("Atualizar");
        topo.add(btnFiltrar);
        topo.add(btnExportar);
        add(topo, BorderLayout.NORTH);

        // Tabela
        String[] cols = {
                "ID", "Título", "Status", "Prioridade",
                "Solicitante", "Setor", "Categoria", "SLA (h)",
                "Técnico", "Nível", "Dt. Abertura", "Dt. Atribuição",
                "Dt. Resolução", "Dt. Fechamento", "Observação"
        };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(7).setPreferredWidth(60);
        tabela.getColumnModel().getColumn(8).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(9).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(10).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(11).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(12).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(13).setPreferredWidth(140);
        tabela.getColumnModel().getColumn(14).setPreferredWidth(200);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tabela.setRowSorter(sorter);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Rodapé com contagem
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTotal = new JLabel("Total: 0 registros");
        rodape.add(lblTotal);
        add(rodape, BorderLayout.SOUTH);

        // Eventos
        btnFiltrar.addActionListener(e -> {
            carregar((String) cbFiltro.getSelectedItem());
            lblTotal.setText("Total: " + tableModel.getRowCount() + " registros");
        });
        btnExportar.addActionListener(e -> {
            carregar((String) cbFiltro.getSelectedItem());
            lblTotal.setText("Total: " + tableModel.getRowCount() + " registros");
        });

        carregar("Todos");
        lblTotal.setText("Total: " + tableModel.getRowCount() + " registros");
    }

    public void recarregar() {
        carregar((String) cbFiltro.getSelectedItem());
    }

    private void carregar(String filtro) {
        tableModel.setRowCount(0);
        try {
            List<RelatorioChamado> lista = "Todos".equals(filtro)
                    ? dao.listarTodos()
                    : dao.listarPorStatus(filtro);
            for (RelatorioChamado r : lista) {
                tableModel.addRow(new Object[]{
                        r.getIdChamado(),
                        r.getTitulo(),
                        r.getStatus(),
                        r.getPrioridade(),
                        r.getSolicitante(),
                        r.getSetor()       != null ? r.getSetor()       : "-",
                        r.getCategoria(),
                        r.getSlaHoras(),
                        r.getTecnico()     != null ? r.getTecnico()     : "-",
                        r.getNivelTecnico()!= null ? r.getNivelTecnico(): "-",
                        r.getDtAbertura()  != null ? r.getDtAbertura().toString().replace("T", " ") : "-",
                        r.getDtAtribuicao()!= null ? r.getDtAtribuicao().toString().replace("T", " "): "-",
                        r.getDtResolucao() != null ? r.getDtResolucao().toString().replace("T", " ") : "-",
                        r.getDtFechamento()!= null ? r.getDtFechamento().toString().replace("T", " "): "-",
                        r.getObservacao()  != null ? r.getObservacao()  : "-"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar relatório: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}