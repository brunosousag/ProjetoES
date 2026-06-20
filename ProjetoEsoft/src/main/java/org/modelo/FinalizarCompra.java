package org.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FinalizarCompra extends BaseFrame {
    private JPanel layoutborder;
    private JPanel resumo;
    private JPanel DadosCliente;
    private JTextField textField1; // email
    private JTextField textField2; // telemóvel
    private JTextField txtNome;
    private JTextField txtNif;
    private JPanel titulo;
    private JPanel MetodoPagamento;
    private JRadioButton multibancoRadioButton;
    private JRadioButton dinheiroRadioButton;
    private JPanel finalizarPagamento;
    private JButton voltarButton;
    private JButton finalizarCompraButton;
    private JCheckBox chkConsumidorFinal;

    public FinalizarCompra(String title) {
        super(title);

        setContentPane(layoutborder);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurarMetodoPagamento();
        povoarResumo();

        finalizarCompraButton.addActionListener(this::finalizarCompra);
        voltarButton.addActionListener(this::btnVoltarActionPerformed);
        chkConsumidorFinal.addActionListener(this::aplicarConsumidorFinal);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * "Consumidor final": preenche nome e NIF com dados genéricos e bloqueia
     * a edição desses campos. Ao desmarcar, limpa-os e volta a permitir editar.
     */
    private void aplicarConsumidorFinal(ActionEvent e) {
        boolean ativo = chkConsumidorFinal.isSelected();
        if (ativo) {
            txtNome.setText("Consumidor final");
            txtNif.setText("999999999");
        } else {
            txtNome.setText("");
            txtNif.setText("");
        }
        txtNome.setEnabled(!ativo);
        txtNif.setEnabled(!ativo);
    }

    private void configurarMetodoPagamento() {
        ButtonGroup grupoPagamento = new ButtonGroup();
        grupoPagamento.add(multibancoRadioButton);
        grupoPagamento.add(dinheiroRadioButton);
    }

    /**
     * Substitui o resumo hardcoded por uma lista dos itens reais do carrinho
     * (bilhetes + merch) com subtotais e total.
     */
    private void povoarResumo() {
        resumo.removeAll();
        resumo.setLayout(new BoxLayout(resumo, BoxLayout.Y_AXIS));

        for (ItemCarrinho item : CarrinhoStore.getInstance().getItens()) {
            resumo.add(criarLinhaResumo(item));
            resumo.add(Box.createVerticalStrut(4));
        }

        resumo.add(Box.createVerticalStrut(8));
        resumo.add(criarLinhaTotal());

        resumo.revalidate();
        resumo.repaint();
    }

    private JPanel criarLinhaResumo(ItemCarrinho item) {
        JPanel linha = new JPanel(new BorderLayout(8, 0));
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);

        String prefixo = (item.getTipo() == ItemCarrinho.Tipo.BILHETE) ? "🎟 " : "🛍 ";
        JLabel lblEsq = new JLabel(prefixo + item.getDescricao() + " — " + item.getDetalhe());
        JLabel lblDrt = new JLabel(item.getQuantidade() + " x "
                + formatarPreco(item.getPrecoUnitario()) + "  =  "
                + formatarPreco(item.getSubtotal()), SwingConstants.RIGHT);
        lblDrt.setFont(lblDrt.getFont().deriveFont(Font.BOLD));

        linha.add(lblEsq, BorderLayout.WEST);
        linha.add(lblDrt, BorderLayout.EAST);
        return linha;
    }

    private JPanel criarLinhaTotal() {
        JPanel linha = new JPanel(new BorderLayout());
        linha.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        JLabel lblTotal = new JLabel("TOTAL");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));
        JLabel lblValor = new JLabel(
                formatarPreco(CarrinhoStore.getInstance().getTotal()),
                SwingConstants.RIGHT);
        lblValor.setFont(lblValor.getFont().deriveFont(Font.BOLD, 14f));

        linha.add(lblTotal, BorderLayout.WEST);
        linha.add(lblValor, BorderLayout.EAST);
        return linha;
    }

    private String formatarPreco(double valor) {
        return String.format("%.2f€", valor).replace('.', ',');
    }

    private void finalizarCompra(ActionEvent actionEvent) {
        if (!multibancoRadioButton.isSelected() && !dinheiroRadioButton.isSelected()) {
            mostrarErro("Deve selecionar um método de pagamento.", "Método de pagamento obrigatório");
            return;
        }

        String nome = txtNome.getText() == null ? "" : txtNome.getText().trim();
        String nif = txtNif.getText() == null ? "" : txtNif.getText().trim();

        if (nome.isEmpty()) {
            mostrarErro("Indique o nome completo do cliente.", "Nome obrigatório");
            return;
        }
        if (!nif.matches("\\d{9}")) {
            mostrarErro("O NIF deve ter exatamente 9 dígitos.", "NIF inválido");
            return;
        }

        List<ItemCarrinho> itens = new ArrayList<>(CarrinhoStore.getInstance().getItens());
        if (itens.isEmpty()) {
            mostrarErro("O carrinho está vazio.", "Sem itens");
            return;
        }

        Venda.MetodoPagamento metodo = multibancoRadioButton.isSelected()
                ? Venda.MetodoPagamento.MULTIBANCO
                : Venda.MetodoPagamento.DINHEIRO;

        Venda venda = new Venda(
                gerarNumeroFatura(),
                nome,
                nif,
                LocalDateTime.now(),
                metodo,
                itens,
                CarrinhoStore.getInstance().getTotal()
        );

        RepositorioDados.adicionarVenda(venda);
        atualizarLugaresVendidos(itens);
        CarrinhoStore.getInstance().limpar();

        JOptionPane.showMessageDialog(
                this,
                "Pagamento efetuado com sucesso!\nFatura n.º " + venda.getNumFatura()
        );

        dispose();
        WindowManager.fecharTodasAsJanelas();
    }

    private String gerarNumeroFatura() {
        return "F-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Reflete a venda nos dados persistidos: o total de bilhetes vendidos do
     * jogo e a ocupação por bancada DESSE jogo (cada jogo tem a sua, o template
     * bancadas.dat não é alterado).
     */
    private void atualizarLugaresVendidos(List<ItemCarrinho> itens) {
        ArrayList<JogoCalendario> jogos = RepositorioDados.carregarJogosCalendario();
        boolean alterouJogos = false;

        for (ItemCarrinho item : itens) {
            if (item.getTipo() != ItemCarrinho.Tipo.BILHETE) continue;

            for (JogoCalendario jogo : jogos) {
                String descricao = jogo.getEquipaA() + " vs " + jogo.getEquipaB();
                if (descricao.equals(item.getJogoDescricao())) {
                    jogo.setBilhetesVendidos(jogo.getBilhetesVendidos() + item.getQuantidade());
                    jogo.registarVendaBancada(item.getBancadaNome(), item.getQuantidade());
                    alterouJogos = true;
                    break;
                }
            }
        }

        if (alterouJogos) RepositorioDados.guardarJogosCalendario(jogos);
    }

    private void mostrarErro(String mensagem, String titulo) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.WARNING_MESSAGE);
    }

    private void btnVoltarActionPerformed(ActionEvent actionEvent) {
        dispose();
    }
}
