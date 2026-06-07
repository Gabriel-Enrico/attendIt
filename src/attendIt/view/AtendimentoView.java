package attendIt.view;

import attendIt.controller.AtendimentoController;
import attendIt.controller.ChamadoController;
import attendIt.controller.TecnicoController;
import attendIt.model.Atendimento;
import attendIt.model.Chamado;
import attendIt.model.Tecnico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AtendimentoView extends JPanel {

    private final AtendimentoController controller   = new AtendimentoController();
    private final ChamadoController     chamadoCtrl  = new ChamadoController();
    private final TecnicoController     tecnicoCtrl  = new TecnicoController();

    private final JComboBox<Chamado>  cbChamado  = new JComboBox<>();
    private final JComboBox<Tecnico>  cbTecnico  = new JComboBox<>();
    private final JTextField          txtObs     = new JTextField(30);

    private final DefaultTableModel tableModel;
    private final JTable            tabela;

    public AtendimentoView() {
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Atribuir Técnico ao Chamado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Chamado e Técnico
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Chamado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 2;
        form.add(cbChamado, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        form.add(new JLabel("Técnico:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        form.add(cbTecnico, gbc);

        // Linha 1: Observação e botão
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Observação:"), gbc);
        gbc.gridx = 1; gbc.weightx = 2; gbc.gridwidth = 2;
        form.add(txtObs, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 3; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnAtribuir = new JButton("Atribuir");
        form.add(btnAtribuir, gbc);

        add(form, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"ID", "Chamado", "Técnico", "Dt. Atribuição", "Dt. Resolução", "Observação"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões de ação
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnResolver  = new JButton("Registrar Resolução");
        JButton btnExcluir   = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        acoes.add(btnResolver);
        acoes.add(btnExcluir);
        acoes.add(btnAtualizar);
        add(acoes, BorderLayout.SOUTH);

        // Eventos
        btnAtribuir.addActionListener(e -> atribuir());
        btnResolver.addActionListener(e -> registrarResolucao());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregar());

        carregarCombos();
        carregar();
    }

    private void carregarCombos() {
        cbChamado.removeAllItems();
        cbTecnico.removeAllItems();
        StringBuilder msg = new StringBuilder();

        List<Chamado> chamados = chamadoCtrl.listarTodos(msg);
        for (Chamado c : chamados) {
            // mostra apenas chamados abertos ou em atendimento
            if (!"Fechado".equals(c.getStatus()) && !"Resolvido".equals(c.getStatus())) {
                cbChamado.addItem(c);
            }
        }

        List<Tecnico> tecnicos = tecnicoCtrl.listarTodos(msg);
        for (Tecnico t : tecnicos) cbTecnico.addItem(t);

        if (!msg.isEmpty())
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void atribuir() {
        Chamado chamado = (Chamado) cbChamado.getSelectedItem();
        Tecnico tecnico = (Tecnico) cbTecnico.getSelectedItem();

        if (chamado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um chamado.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tecnico == null) {
            JOptionPane.showMessageDialog(this, "Selecione um técnico.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Atendimento a = new Atendimento();
        a.setIdChamado(chamado.getId());
        a.setIdTecnico(tecnico.getId());
        a.setObservacao(txtObs.getText().trim());

        StringBuilder msg = new StringBuilder();
        if (controller.atribuir(a, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            txtObs.setText("");
            carregarCombos();
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void registrarResolucao() {
        int id = idSelecionado();
        if (id < 0) return;

        // Bloqueia se já resolvido
        int linha = tabela.getSelectedRow();
        String dtResolucao = (String) tableModel.getValueAt(linha, 4);
        if (!"-".equals(dtResolucao)) {
            JOptionPane.showMessageDialog(this, "Este atendimento já foi resolvido.");
            return;
        }

        String obs = JOptionPane.showInputDialog(this, "Informe a observação de resolução:");
        if (obs == null) return;

        StringBuilder msg = new StringBuilder();
        if (controller.registrarResolucao(id, obs, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir atendimento #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        StringBuilder msg = new StringBuilder();
        controller.excluir(id, msg);
        JOptionPane.showMessageDialog(this, msg.toString());
        carregar();
    }

    private void carregar() {
        tableModel.setRowCount(0);
        StringBuilder msg = new StringBuilder();
        List<Atendimento> lista = controller.listarTodos(msg);
        for (Atendimento a : lista)
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getTituloChamado(),
                    a.getNomeTecnico(),
                    a.getDtAtribuicao() != null ? a.getDtAtribuicao().toString().replace("T", " ") : "-",
                    a.getDtResolucao()  != null ? a.getDtResolucao().toString().replace("T", " ")  : "-",
                    a.getObservacao() != null ? a.getObservacao() : ""
            });
        if (!msg.isEmpty())
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private int idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um atendimento na lista.");
            return -1;
        }
        return (int) tableModel.getValueAt(linha, 0);
    }

    public void recarregar() {
        carregarCombos();
        carregar();
    }
}