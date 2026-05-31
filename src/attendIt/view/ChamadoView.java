package attendIt.view;

import attendIt.controller.ChamadoController;
import attendIt.model.Chamado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChamadoView extends JPanel {

    private final ChamadoController chamadoController = new ChamadoController();

    private final DefaultTableModel tableModel;
    private final JTable tabela;
    private final JTextField txtTitulo = new JTextField(20);
    private final JTextField txtDescricao = new JTextField(30);
    private final JComboBox<String> cbPrioridade = new JComboBox<>(
            new String[]{"Baixa", "Média", "Alta", "Crítica"});

    public ChamadoView() {
        setLayout(new BorderLayout(0, 0 ));
        setBorder(BorderFactory.createEmptyBorder(10, 10 , 10, 10));

        // Formulário de cadastro
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        form.setBorder(BorderFactory.createTitledBorder("Novo Chamado"));
        form.add(new JLabel("Título:"));
        form.add(txtTitulo);
        form.add(new JLabel("Descrição"));
        form.add(txtDescricao);
        form.add(new JLabel("Prioridade:"));
        form.add(cbPrioridade);
        JButton btnSalvar = new JButton("Salvar");
        form.add(btnSalvar);
        add(form, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"ID", "Título", "Status", "Prioridade"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões de ação
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnFechar = new JButton("Fechar chamado");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        acoes.add(btnFechar);
        acoes.add(btnExcluir);
        acoes.add(btnAtualizar);
        add(acoes, BorderLayout.SOUTH);

        // Eventos
        btnSalvar.addActionListener(e -> salvar());
        btnFechar.addActionListener(e -> fechar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> atualizar());

        carregar();
    }

    private void salvar() {
        Chamado chamado = new Chamado();
        chamado.setTitulo(txtTitulo.getText().trim());
        chamado.setDescricao(txtDescricao.getText().trim());
        chamado.setPrioridade((String) cbPrioridade.getSelectedItem());
        chamado.setId(1);

        StringBuilder msg = new StringBuilder();
        if (chamadoController.abrirChamado(chamado, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            txtTitulo.setText("");
            txtDescricao.setText("");
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void fechar() {
        int id = idSelecionado();
        if (id < 0) return;
        StringBuilder msg = new StringBuilder();
        if (chamadoController.fecharChamado(id, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
        carregar();
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir chamado #" + id + "?", "Confirmar",  JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        StringBuilder msg = new StringBuilder();
        chamadoController.excluir(id, msg);
        JOptionPane.showMessageDialog(this, msg.toString());
        carregar();
    }

    private void atualizar() {
        int id = idSelecionado();
        if (id < 0) return;

        // pré-preenche os campos com os dados da linha selecionada
        int linha = tabela.getSelectedRow();
        txtTitulo.setText((String) tableModel.getValueAt(linha, 1));
        cbPrioridade.setSelectedItem(tableModel.getValueAt(linha, 3));

        int conf = JOptionPane.showConfirmDialog(this,
                "Confirmar edição do chamado #" + id + " com os novos dados?",
                "Editar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        Chamado c = new Chamado();
        c.setId(id);
        c.setTitulo(txtTitulo.getText().trim());
        c.setDescricao(txtDescricao.getText().trim());
        c.setPrioridade((String) cbPrioridade.getSelectedItem());

        StringBuilder msg = new StringBuilder();
        if (chamadoController.atualizarChamado(c, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(),
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void carregar() {
        tableModel.setRowCount(0);
        StringBuilder msg = new StringBuilder();
        List<Chamado> lista = chamadoController.listarTodos(msg);
        for (Chamado c : lista)
            tableModel.addRow(new Object[]{
                    c.getId(), c.getTitulo(), c.getStatus(), c.getPrioridade()});
        if (!msg.isEmpty())
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private int idSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um chamado na lista.");
            return -1;
        }
        return (int) tableModel.getValueAt(linha, 0);
    }
}
