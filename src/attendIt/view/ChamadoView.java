package attendIt.view;

import attendIt.controller.ChamadoController;
import attendIt.controller.CategoriaController;
import attendIt.model.Chamado;
import attendIt.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChamadoView extends JPanel {

    private final ChamadoController chamadoController = new ChamadoController();
    private final CategoriaController categoriaController = new CategoriaController();

    private final DefaultTableModel tableModel;
    private final JTable tabela;
    private final JTextField txtTitulo    = new JTextField(20);
    private final JTextField txtDescricao = new JTextField(30);
    private final JComboBox<String> cbPrioridade = new JComboBox<>(
            new String[]{"Baixa", "Media", "Alta", "Critica"});
    private final JComboBox<Categoria> cbCategoria = new JComboBox<>();

    public ChamadoView() {
        setLayout(new BorderLayout(0, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário de cadastro com GridBagLayout
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Novo Chamado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(txtTitulo, gbc);

        // Descrição
        gbc.gridx = 2; gbc.weightx = 0;
        form.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 3; gbc.weightx = 2;
        form.add(txtDescricao, gbc);

        // Prioridade
        gbc.gridx = 4; gbc.weightx = 0;
        form.add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0;
        form.add(cbPrioridade, gbc);

        // Linha 1: Categoria
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 2;
        form.add(cbCategoria, gbc);
        gbc.gridwidth = 1;

        // Botão Salvar
        gbc.gridx = 5; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnSalvar = new JButton("Salvar");
        form.add(btnSalvar, gbc);

        add(form, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"ID", "Título", "Status", "Prioridade"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões de ação
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnFechar    = new JButton("Fechar chamado");
        JButton btnEditar    = new JButton("Editar");
        JButton btnExcluir   = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        acoes.add(btnFechar);
        acoes.add(btnEditar);
        acoes.add(btnExcluir);
        acoes.add(btnAtualizar);
        add(acoes, BorderLayout.SOUTH);

        // Eventos
        btnSalvar.addActionListener(e -> salvar());
        btnFechar.addActionListener(e -> fechar());
        btnExcluir.addActionListener(e -> excluir());
        btnEditar.addActionListener(e -> atualizar());
        btnAtualizar.addActionListener(e -> carregar());

        carregarCategorias();
        carregar();
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems();
        StringBuilder msg = new StringBuilder();
        List<Categoria> categorias = categoriaController.listarTodos(msg);
        for (Categoria c : categorias) {
            cbCategoria.addItem(c);
        }
        if (!msg.isEmpty()) {
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvar() {
        Categoria categoriaSelecionada = (Categoria) cbCategoria.getSelectedItem();
        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Chamado chamado = new Chamado();
        chamado.setTitulo(txtTitulo.getText().trim());
        chamado.setDescricao(txtDescricao.getText().trim());
        chamado.setPrioridade((String) cbPrioridade.getSelectedItem());
        chamado.setIdUsuario(1);
        chamado.setIdCategoria(categoriaSelecionada.getId());

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

        // Bloqueia se já fechado
        int linha = tabela.getSelectedRow();
        String status = (String) tableModel.getValueAt(linha, 2);
        if ("Fechado".equals(status)) {
            JOptionPane.showMessageDialog(this, "Este chamado já está fechado.");
            return;
        }

        StringBuilder msg = new StringBuilder();
        if (chamadoController.fecharChamado(id, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
        carregar();
    }

    private void excluir() {
        int id = idSelecionado();
        if (id < 0) return;

        int linha = tabela.getSelectedRow();
        String status = (String) tableModel.getValueAt(linha, 2);

        if ("Fechado".equals(status) || "Resolvido".equals(status) || "Em_Atendimento".equals(status)) {
            JOptionPane.showMessageDialog(this,
                    "Apenas chamados com status 'Aberto' podem ser excluídos.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir chamado #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        StringBuilder msg = new StringBuilder();
        chamadoController.excluir(id, msg);
        JOptionPane.showMessageDialog(this, msg.toString());
        carregar();
    }

    private void atualizar() {
        int id = idSelecionado();
        if (id < 0) return;

        // Bloqueia se fechado
        int linha = tabela.getSelectedRow();
        String status = (String) tableModel.getValueAt(linha, 2);
        if ("Fechado".equals(status)) {
            JOptionPane.showMessageDialog(this, "Chamados fechados não podem ser editados.");
            return;
        }

        txtTitulo.setText((String) tableModel.getValueAt(linha, 1));
        cbPrioridade.setSelectedItem(tableModel.getValueAt(linha, 3));

        int conf = JOptionPane.showConfirmDialog(this,
                "Confirmar edição do chamado #" + id + " com os novos dados?",
                "Editar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        Categoria categoriaSelecionada = (Categoria) cbCategoria.getSelectedItem();

        Chamado c = new Chamado();
        c.setId(id);
        c.setTitulo(txtTitulo.getText().trim());
        c.setDescricao(txtDescricao.getText().trim());
        c.setPrioridade((String) cbPrioridade.getSelectedItem());
        if (categoriaSelecionada != null) c.setIdCategoria(categoriaSelecionada.getId());

        StringBuilder msg = new StringBuilder();
        if (chamadoController.atualizarChamado(c, msg)) {
            JOptionPane.showMessageDialog(this, msg.toString());
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, msg.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
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

    public void recarregar() {
        carregarCategorias();
        carregar();
    }
}