import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceMenu extends JFrame {
    private static boolean Running = true;
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
    private JButton listarButton = new JButton("Listar Registros");


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

        gbc.gridy = 11;
        add(listarButton, gbc);


        adicionarButton.setBackground(new Color(30, 144, 255));
        adicionarButton.setForeground(Color.WHITE);

        atualizarButton.setBackground(new Color(30, 144, 255));
        atualizarButton.setForeground(Color.WHITE);

        pesquisarButton.setBackground(new Color(30, 144, 255));
        pesquisarButton.setForeground(Color.WHITE);

        deletarButton.setBackground(new Color(30, 144, 255));
        deletarButton.setForeground(Color.WHITE);

        listarButton.setBackground(new Color(30, 144, 255));
        listarButton.setForeground(Color.WHITE);

        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        adicionarButton.setFont(buttonFont);
        atualizarButton.setFont(buttonFont);
        pesquisarButton.setFont(buttonFont);
        deletarButton.setFont(buttonFont);
        listarButton.setFont(buttonFont);

        setVisible(true);


    adicionarButton.addActionListener(new ActionListener() {
    @Override
        public void actionPerformed(ActionEvent e) {
            String nome = nomeTextField.getText();
            String artista = artistaTextField.getText();
            String popularidade = popularidadeTextField.getText();
            String dataLancamento = dataLancamentoTextField.getText();
            String genero = generoTextField.getText();
            String dancabilidade = dancabilidadeTextField.getText();
            String hash = hashTextField.getText();

            // Aqui você pode reunir todas essas informações em uma única string
            String informacoes = nome + "," +
                                 artista + "," +
                                 popularidade + "," +
                                  dataLancamento + "," +
                                 genero + "," +
                                 dancabilidade + "," +
                                hash;

            // Exibir a string com as informações reunidas
            System.out.println("Informações reunidas:\n" + informacoes + "\n\n");



            Arq.addRegistro(informacoes);
        }
    });
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adicionar função aqui
                System.out.println("Botão 'Atualizar' clicado!");
            }
        });
        
        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adicionar função aqui
                System.out.println("Botão 'Pesquisar' clicado!");
            }
        });
        
        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adicionar função aqui
                System.out.println("Botão 'Deletar' clicado!");
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adicionar função aqui
                Arq.PrintarRegistros();
            }
        });
    }

    

    public boolean Fechar() {
        try {
            this.dispose();
            Running = false;
            System.out.println("Janela fechada com sucesso!");
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao fechar a janela: " + e.getMessage());
            return false;
        }
    }

    public static void main(String args[]) {
        
        SwingUtilities.invokeLater(InterfaceMenu::new);
        Arq.Iniciar("songs.db");

        System.out.println("ultimo ID inserido foi: " + Musica.getLastID());
        while (Running) {
            
        }

        Arq.Finalizar();
    }
}
