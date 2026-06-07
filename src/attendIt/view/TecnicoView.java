package attendIt.view;

import attendIt.controller.TecnicoController;
import attendIt.model.Tecnico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TecnicoView extends JPanel {

    private final TecnicoController controller = new TecnicoController();

    private final DefaultTableModel tableModel;
    private final JTable            tabela;
    private final JTextField        txtNome          = new JTextField(20);
    private final JTextField        txtEspecialidade = new JTextField(20);
    private final JComboBox<String> cbNivel          = new JComboBox<>(new String[]{"N1", "N2", "N3"});
    private final JCheckBox         chkDisponivel    = new JCheckBox("Disponível", true);

    public TecnicoView() {
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário com GridBagLayout
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Novo técnico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(txtNome, gbc);

        // Linha 0 cont: Especialidade
        gbc.gridx = 2; gbc.weightx = 0;
        form.add(new JLabel("Especialidade:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        form.add(txtEspecialidade, gbc);

        // Linha 0 cont: Nível
        gbc.gridx = 4; gbc.weightx = 0;
        form.add(new JLabel("Nível:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0;
        form.add(cbNivel, gbc);

        // Linha 0 cont: Disponível
        gbc.gridx = 6;
        form.add(chkDisponivel, gbc);

        // Linha 1: Botão Salvar
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnSalvar = new JButton("Salvar");
        form.add(btnSalvar, gbc);

        add(form, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"ID", "Nome", "Especialidade", "Nível", "Disponível"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões de ação
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnEditar    = new JButton("Editar");
        JButton btnExcluir   = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        acoes.add(btnEditar);
        acoes.add(btnExcluir);
        acoes.add(btnAtualizar);
        add(acoes, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregar());

        carregar();
    }

    private void salvar() {
        Tecnico t = new Tecnico();
        t.setNome(txtNome.getText().trim());
        t.setEspecialidade(txtEspecialidade.getText().trim());
        t.setNivel((String) cbNivel.getSelectedItem());
        t.setDisponivel(chkDisponivel.isSelected());

        StringBuilder msg = new StringBuilder();
        if (controller.cadastrar(t, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            limparFormulario();
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editar() {
        int id = idSelecionado();
        if (id < 0) return;

        int linha = tabela.getSelectedRow();
        txtNome.setText((String) tableModel.getValueAt(linha, 1));
        txtEspecialidade.setText((String) tableModel.getValueAt(linha, 2));
        cbNivel.setSelectedItem(tableModel.getValueAt(linha, 3));
        chkDisponivel.setSelected(Boolean.TRUE.equals(tableModel.getValueAt(linha, 4)));

        int conf = JOptionPane.showConfirmDialog(this,
                "Confirmar edição do técnico #" + id + " com os novos dados?",
                "Editar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        Tecnico t = new Tecnico();
        t.setId(id);
        t.setNome(txtNome.getText().trim());
        t.setEspecialidade(txtEspecialidade.getText().trim());
        t.setNivel((String) cbNivel.getSelectedItem());
        t.setDisponivel(chkDisponivel.isSelected());

        StringBuilder msg = new StringBuilder();
        if (controller.atualizar(t, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            limparFormulario();
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir técnico #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        StringBuilder msg = new StringBuilder();
        if (controller.excluir(id, msg))
            JOptionPane.showMessageDialog(this, msg.toString());
        else
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
        carregar();
    }

    private void carregar() {
        tableModel.setRowCount(0);
        StringBuilder msg = new StringBuilder();
        List<Tecnico> lista = controller.listarTodos(msg);
        for (Tecnico t : lista)
            tableModel.addRow(new Object[]{
                    t.getId(), t.getNome(), t.getEspecialidade(), t.getNivel(), t.isDisponivel()});
        if (!msg.isEmpty())
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtEspecialidade.setText("");
        cbNivel.setSelectedIndex(0);
        chkDisponivel.setSelected(true);
    }

    private int idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um técnico na lista.");
            return -1;
        }
        return (int) tableModel.getValueAt(linha, 0);
    }
}