
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    private JTextField idTextField = new JTextField(20);
    private JButton adicionarButton = new JButton("Adicionar");
    private JButton atualizarButton = new JButton("Atualizar");
    private JButton pesquisarButton = new JButton("Pesquisar");
    private JButton deletarButton = new JButton("Deletar");
    private JButton listarButton = new JButton("Listar Registros");
    private JButton MockDataButton = new JButton("Mock Data");
    private DefaultTableModel tableModel;
    private JTable table;

    public InterfaceMenu() {
        setTitle("CRUD Swing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Painel para campos de entrada
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(idTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(nomeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Artista:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(artistaTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        fieldsPanel.add(new JLabel("Popularidade:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(popularidadeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        fieldsPanel.add(new JLabel("Data de Lançamento:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(dataLancamentoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        fieldsPanel.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(generoTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        fieldsPanel.add(new JLabel("Dançabilidade:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(dancabilidadeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        fieldsPanel.add(new JLabel("Hash:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(hashTextField, gbc);

        // Adicionando o painel de campos de entrada ao painel principal no norte
        mainPanel.add(fieldsPanel, BorderLayout.NORTH);

        // Adicionando botões em um painel separado
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.add(adicionarButton);
        buttonsPanel.add(atualizarButton);
        buttonsPanel.add(pesquisarButton);
        buttonsPanel.add(deletarButton);
        buttonsPanel.add(listarButton);
        buttonsPanel.add(MockDataButton);
        // Adicionando o painel de botões ao painel principal no sul
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Configuração da tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Artista");
        tableModel.addColumn("Popularidade");
        tableModel.addColumn("Data de Lançamento");
        tableModel.addColumn("Gênero");
        tableModel.addColumn("Dançabilidade");
        tableModel.addColumn("Hash");
        table = new JTable(tableModel);

        // Adiciona um JScrollPane para a tabela
        JScrollPane scrollPane = new JScrollPane(table);

        // Adicionando o JScrollPane ao painel principal no centro
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Configuração de cores e fontes para os botões
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        Color buttonColor = new Color(30, 144, 255);

        adicionarButton.setFont(buttonFont);
        adicionarButton.setForeground(Color.WHITE);
        adicionarButton.setBackground(buttonColor);

        atualizarButton.setFont(buttonFont);
        atualizarButton.setForeground(Color.WHITE);
        atualizarButton.setBackground(buttonColor);

        pesquisarButton.setFont(buttonFont);
        pesquisarButton.setForeground(Color.WHITE);
        pesquisarButton.setBackground(buttonColor);

        deletarButton.setFont(buttonFont);
        deletarButton.setForeground(Color.WHITE);
        deletarButton.setBackground(buttonColor);

        listarButton.setFont(buttonFont);
        listarButton.setForeground(Color.WHITE);
        listarButton.setBackground(buttonColor);

        MockDataButton.setFont(buttonFont);
        MockDataButton.setForeground(Color.WHITE);
        MockDataButton.setBackground(buttonColor);

       
        setVisible(true);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                     String nome = nomeTextField.getText();
                String artista = artistaTextField.getText();
                String popularidade = popularidadeTextField.getText();
                String dataLancamento = dataLancamentoTextField.getText();
                String genero = generoTextField.getText();
                String dancabilidade = dancabilidadeTextField.getText();
                String hash = hashTextField.getText();

                String informacoes = nome + "," +
                        artista + "," +
                        popularidade + "," +
                        dataLancamento + "," +
                        genero + "," +
                        dancabilidade + "," +
                        hash;
                Logs.Details("Informações reunidas:\n" + informacoes + "\n\n");

                Arq.addRegistro(informacoes);
                limparCampos();
                listarButton.doClick();
                }catch(IllegalArgumentException ex){
                    Logs.Alert("Erro ao adicionar a música:\n" + ex.getMessage());
                }catch (Exception ex){
                    Logs.Alert("Erro ao adicionar a música:\n" + ex.getMessage());
                }
               
                
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idTextField.getText(); 
                String nome = nomeTextField.getText();
                String artista = artistaTextField.getText();
                String popularidade = popularidadeTextField.getText();
                String dataLancamento = dataLancamentoTextField.getText();
                String genero = generoTextField.getText();
                String dancabilidade = dancabilidadeTextField.getText();
                String hash = hashTextField.getText();

                
                String informacoes = id+","+ nome + "," +
                        artista + "," +
                        popularidade + "," +
                        dataLancamento + "," +
                        genero + "," +
                        dancabilidade + "," +
                        hash;
                Logs.Details("Informações reunidas:\n" + informacoes + "\n\n");
                try {
                    Arq.UpdateSong(Integer.parseInt(id), informacoes);
                }catch (NumberFormatException ex){
                    Logs.Alert("Erro ao atualizar a música:\n Numero esperado " + ex.getMessage());
                }
                

               
                limparCampos();
            }
        });

        // Método para limpar os campos de texto após a atualização

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                   int ID = Integer.parseInt(idTextField.getText());
                Musica musica = Arq.FindSongID(ID);
                LimparTabela();
                if (musica != null) {
                    tableModel.addRow(new Object[] {
                        musica.getId(),
                        musica.getNome(),
                        musica.getArtista(),
                        musica.getPopularidade(),
                        musica.getDataLancamento(),
                        musica.getGenero(),
                        musica.getDancabilidade(),
                        musica.getHash()
                });
                Logs.Details(musica.toString());
                } else {
                    Logs.Alert("Música não encontrada!");
                } 
                }catch (NumberFormatException ex){
                    Logs.Alert("Erro ao pesquisar a música:\n Numero esperado " + ex.getMessage());
                }catch (Exception ex){
                    Logs.Alert("Erro ao pesquisar a música:\n" + ex.getMessage());
                }
                limparCampos();
            }
        });

        deletarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int ID = Integer.parseInt(idTextField.getText());
                Logs.Details("Status de deletar: " + Arq.DeleteSong(ID));
                }catch (NumberFormatException ex){
                    Logs.Alert("Erro ao deletar a música:\n Numero esperado " + ex.getMessage() +"\n" + ex.getClass());
                }catch (Exception ex){
                    Logs.Alert("Erro ao deletar a música:\n" + ex.getMessage()+"\n" + ex.getClass());
                }
                limparCampos();
            }
        });

        // Configuração da tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Artista");
        tableModel.addColumn("Popularidade");
        tableModel.addColumn("Data de Lançamento");
        tableModel.addColumn("Gênero");
        tableModel.addColumn("Dançabilidade");
        tableModel.addColumn("Hash");
        table = new JTable(tableModel);

       
        // Adiciona um JScrollPane para a tabela
        gbc.gridy = 12;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0; // Aumenta o peso na direção X
        gbc.weighty = 1.0; // Aumenta o peso na direção Y
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10); // Aumenta o espaçamento em todos os lados
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpa a tabela antes de listar os registros
                tableModel.setRowCount(0);
                Musica musica;
                Arq.IniciarLeituraSequencial();

                // Adicione aqui o código para preencher a tabela com os registros do banco de
                // dados
                while ((musica = Arq.getRegistro()) != null) {
                    tableModel.addRow(new Object[] {
                            musica.getId(),
                            musica.getNome(),
                            musica.getArtista(),
                            musica.getPopularidade(),
                            musica.getDataLancamento(),
                            musica.getGenero(),
                            musica.getDancabilidade(),
                            musica.getHash()
                    });
                }
            }
        });

        MockDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MockData Mock = new MockData();
                Musica musica = Mock.generateRandomData();
                Logs.Details("Mock Data gerado:\n" + musica.toString());
                //adicionar aos campos 
                nomeTextField.setText(musica.getNome());
                artistaTextField.setText(musica.getArtista());
                popularidadeTextField.setText(String.valueOf(musica.getPopularidade()));
                dataLancamentoTextField.setText(musica.getDataLancamento());
                generoTextField.setText(musica.getGenero());
                dancabilidadeTextField.setText(String.valueOf(musica.getDancabilidade()));
                hashTextField.setText(musica.getHash());
            
                listarButton.doClick();
            }
        });

        setVisible(true);
    }

    private void limparCampos() {
        idTextField.setText("");
        nomeTextField.setText("");
        artistaTextField.setText("");
        popularidadeTextField.setText("");
        dataLancamentoTextField.setText("");
        generoTextField.setText("");
        dancabilidadeTextField.setText("");
        hashTextField.setText("");
    }

    private void LimparTabela() {
        tableModel.setRowCount(0);
    }

    public boolean Fechar() {
        try {
            this.dispose();
            Running = false;
            Logs.Succeed("Janela fechada com sucesso!");
            return true;
        } catch (Exception e) {
            Logs.Alert("Erro ao fechar a janela:\n Erro:" + e.getMessage());
            return false;
        }
    }

    public static void main(String args[]) {
        Arq.Iniciar("songs.db");
        Musica.iniciar();
        Logs.Clear();
        Logs.Details("=================== Iniciando o programa ===================");
        Logs.Details("Iniciando a interface gráfica...");
        

        SwingUtilities.invokeLater(InterfaceMenu::new);
        Logs.Succeed("Interface gráfica iniciada com sucesso e Operando!");
        while (Running) {

        }

    
    }
}
