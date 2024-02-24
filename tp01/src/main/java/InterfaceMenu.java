import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceMenu extends JFrame {
    private JTextField nomeTextField = new JTextField(20);
    private JTextField artistaTextField = new JTextField(20);
    private JTextField popularidadeTextField = new JTextField(20);
    private JTextField dataLancamentoTextField = new JTextField(20);
    private JTextField generoTextField = new JTextField(20);
    private JTextField dancabilidadeTextField = new JTextField(20);
    private JTextField hashTextField = new JTextField(20);
    private JButton adicionarButton = new JButton("Adicionar");
    private JButton atualizarButton = new JButton("Atualizar");
    private JButton pesquisarButton = new JButton("Pesquisar");
    private JButton deletarButton = new JButton("Deletar");

    public InterfaceMenu() {
        setTitle("CRUD Swing");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nomeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Artista:"), gbc);
        gbc.gridx = 1;
        add(artistaTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Popularidade:"), gbc);
        gbc.gridx = 1;
        add(popularidadeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Data de Lançamento:"), gbc);
        gbc.gridx = 1;
        add(dataLancamentoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1;
        add(generoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Dançabilidade:"), gbc);
        gbc.gridx = 1;
        add(dancabilidadeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Hash:"), gbc);
        gbc.gridx = 1;
        add(hashTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(adicionarButton, gbc);

        gbc.gridy = 8;
        add(atualizarButton, gbc);

        gbc.gridy = 9;
        add(pesquisarButton, gbc);

        gbc.gridy = 10;
        add(deletarButton, gbc);

        adicionarButton.setBackground(new Color(30, 144, 255));
        adicionarButton.setForeground(Color.WHITE);

        atualizarButton.setBackground(new Color(30, 144, 255));
        atualizarButton.setForeground(Color.WHITE);

        pesquisarButton.setBackground(new Color(30, 144, 255));
        pesquisarButton.setForeground(Color.WHITE);

        deletarButton.setBackground(new Color(30, 144, 255));
        deletarButton.setForeground(Color.WHITE);

        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        adicionarButton.setFont(buttonFont);
        atualizarButton.setFont(buttonFont);
        pesquisarButton.setFont(buttonFont);
        deletarButton.setFont(buttonFont);

        setVisible(true);
    }

    public boolean Fechar() {
        try {
            this.dispose();
            System.out.println("Janela fechada com sucesso!");
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao fechar a janela: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfaceMenu::new);
    }
}
