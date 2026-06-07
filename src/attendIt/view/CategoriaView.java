package attendIt.view;

import attendIt.controller.CategoriaController;
import attendIt.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaView extends JPanel {

    private final CategoriaController controller = new CategoriaController();

    private final DefaultTableModel tableModel;
    private final JTable            tabela;

    public CategoriaView() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] cols = {"ID", "Descrição", "SLA (horas)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnNovo      = new JButton("Nova categoria");
        JButton btnEditar    = new JButton("Editar");
        JButton btnExcluir   = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        acoes.add(btnNovo);
        acoes.add(btnEditar);
        acoes.add(btnExcluir);
        acoes.add(btnAtualizar);
        add(acoes, BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> abrirModal(null));
        btnEditar.addActionListener(e -> abrirEdicao());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregar());

        carregar();
    }

    private void abrirModal(Categoria categoriaExistente) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                categoriaExistente == null ? "Nova categoria" : "Editar categoria",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(360, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JTextField txtDescricao = new JTextField();
        JSpinner   spnSla       = new JSpinner(new SpinnerNumberModel(8, 1, 720, 1));

        if (categoriaExistente != null) {
            txtDescricao.setText(categoriaExistente.getDescricao());
            spnSla.setValue(categoriaExistente.getSlaHoras());
        }

        form.add(new JLabel("Descrição:"));   form.add(txtDescricao);
        form.add(new JLabel("SLA (horas):")); form.add(spnSla);
        dialog.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSalvar   = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);
        dialog.add(botoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnSalvar.addActionListener(e -> {
            Categoria c = new Categoria();
            if (categoriaExistente != null) c.setId(categoriaExistente.getId());
            c.setDescricao(txtDescricao.getText().trim());
            c.setSlaHoras((Integer) spnSla.getValue());

            StringBuilder msg = new StringBuilder();
            boolean ok = categoriaExistente == null
                    ? controller.cadastrar(c, msg)
                    : controller.atualizar(c, msg);

            if (ok) {
                JOptionPane.showMessageDialog(dialog, msg.toString());
                dialog.dispose();
                carregar();
            } else {
                JOptionPane.showMessageDialog(dialog, msg.toString(),
                        "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void abrirEdicao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na lista.");
            return;
        }
        Categoria c = new Categoria();
        c.setId((int) tableModel.getValueAt(linha, 0));
        c.setDescricao((String) tableModel.getValueAt(linha, 1));
        c.setSlaHoras((int) tableModel.getValueAt(linha, 2));
        abrirModal(c);
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na lista.");
            return;
        }
        int id = (int) tableModel.getValueAt(linha, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir categoria #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
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
        List<Categoria> lista = controller.listarTodos(msg);
        for (Categoria c : lista)
            tableModel.addRow(new Object[]{c.getId(), c.getDescricao(), c.getSlaHoras()});
        if (!msg.isEmpty())
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
