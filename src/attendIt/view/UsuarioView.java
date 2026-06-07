package attendIt.view;

import attendIt.model.Usuario;
import attendIt.controller.UsuarioController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioView extends JPanel {

    private final UsuarioController controller = new UsuarioController();

    private final DefaultTableModel tableModel;
    private final JTable            tabela;

    public UsuarioView() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] cols = {"ID", "Nome", "E-mail", "Setor", "Perfil"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnNovo      = new JButton("Novo usuário");
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

    // Abre o modal para novo cadastro ou edição
    private void abrirModal(Usuario usuarioExistente) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                usuarioExistente == null ? "Novo usuário" : "Editar usuário",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(8, 8));

        // Campos do formulário
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JTextField txtNome  = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtSetor = new JTextField();
        JComboBox<String> cbPerfil = new JComboBox<>(
                new String[]{"SOLICITANTE", "TECNICO", "ADMIN"});

        // Pré-preenche se for edição
        if (usuarioExistente != null) {
            txtNome.setText(usuarioExistente.getNome());
            txtEmail.setText(usuarioExistente.getEmail());
            txtSetor.setText(usuarioExistente.getSetor());
            cbPerfil.setSelectedItem(usuarioExistente.getPerfil());
        }

        form.add(new JLabel("Nome:"));       form.add(txtNome);
        form.add(new JLabel("E-mail:"));     form.add(txtEmail);
        form.add(new JLabel("Setor:"));      form.add(txtSetor);
        form.add(new JLabel("Perfil:"));     form.add(cbPerfil);
        dialog.add(form, BorderLayout.CENTER);

        // Botões do modal
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSalvar   = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);
        dialog.add(botoes, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnSalvar.addActionListener(e -> {
            Usuario u = new Usuario();
            if (usuarioExistente != null) u.setId(usuarioExistente.getId());
            u.setNome(txtNome.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setSetor(txtSetor.getText().trim());
            u.setPerfil((String) cbPerfil.getSelectedItem());

            StringBuilder msg = new StringBuilder();
            boolean ok = usuarioExistente == null
                    ? controller.cadastrar(u, msg)
                    : controller.atualizar(u, msg);

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
            JOptionPane.showMessageDialog(this, "Selecione um usuário na lista.");
            return;
        }
        Usuario u = new Usuario();
        u.setId((int) tableModel.getValueAt(linha, 0));
        u.setNome((String) tableModel.getValueAt(linha, 1));
        u.setEmail((String) tableModel.getValueAt(linha, 2));
        u.setSetor((String) tableModel.getValueAt(linha, 3));
        u.setPerfil((String) tableModel.getValueAt(linha, 4));
        abrirModal(u);
    }

    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na lista.");
            return;
        }
        int id = (int) tableModel.getValueAt(linha, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir usuário #" + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
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
        List<Usuario> lista = controller.listarTodos(msg);
        for (Usuario u : lista)
            tableModel.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getEmail(), u.getSetor(), u.getPerfil()});
        if (!msg.isEmpty())
            JOptionPane.showMessageDialog(this, msg.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
