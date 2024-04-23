
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    // Buttons
    private JButton adicionarButton = new JButton("Adicionar");
    private JButton atualizarButton = new JButton("Atualizar");
    private JButton pesquisarButton = new JButton("Pesquisar");
    private JButton deletarButton = new JButton("Deletar");
    private JButton listarButton = new JButton("Listar Registros");
    private JButton MockDataButton = new JButton("Mock Data");
    private JButton listUnvalidRegistersButton = new JButton("Listar Inválidos");
    private JButton ordenarButton = new JButton("Ordenar");
    // Tabela
    private DefaultTableModel tableModel;
    private JTable table;

    public InterfaceMenu() {
        setTitle("CRUD Swing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Painel para campos de entrada
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Campos de Entrada"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
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
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(adicionarButton);
        buttonsPanel.add(atualizarButton);
        buttonsPanel.add(pesquisarButton);
        buttonsPanel.add(deletarButton);
        buttonsPanel.add(listarButton);
        buttonsPanel.add(MockDataButton);
        buttonsPanel.add(listUnvalidRegistersButton);
        buttonsPanel.add(ordenarButton);
        // Adicionando o painel de botões ao painel principal no sul
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

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

        listUnvalidRegistersButton.setFont(buttonFont);
        listUnvalidRegistersButton.setForeground(Color.BLACK);
        listUnvalidRegistersButton.setBackground(Color.RED);

        ordenarButton.setFont(buttonFont);
        ordenarButton.setForeground(Color.WHITE);
        ordenarButton.setBackground(buttonColor);

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
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registros"));
        scrollPane.setPreferredSize(new Dimension(700, 300));

        // Adicionando o JScrollPane ao painel principal no centro
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Centralizando o JFrame na tela
        setLocationRelativeTo(null);

        // Atualiza a exibição da interface gráfica
        setVisible(true);

        // Pare de editar aqui, para baixo tem metodos de ação dos botões

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
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
                } catch (IllegalArgumentException ex) {
                    Logs.Alert("Erro ao adicionar a música:\n" + ex.getMessage());
                } catch (Exception ex) {
                    Logs.Alert("Erro ao adicionar a música:\n" + ex.getMessage());
                }

            }
        });

        ordenarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sorting.IntercalacaoBalanceada();
                listarButton.doClick();
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

                String informacoes = id + "," + nome + "," +
                        artista + "," +
                        popularidade + "," +
                        dataLancamento + "," +
                        genero + "," +
                        dancabilidade + "," +
                        hash;
                Logs.Details("Informações reunidas:\n" + informacoes + "\n\n");
                try {
                    Arq.UpdateSong(Integer.parseInt(id), informacoes);
                    limparCampos();
                } catch (NumberFormatException ex) {
                    Logs.Alert("Erro ao atualizar a música:\n Numero esperado " + ex.getMessage());
                }

            }
        });

        // Método para limpar os campos de texto após a atualização

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = idTextField.getText().trim(); // Obtém o texto do campo de pesquisa e remove espaços
                                                                  // em branco
                Musica musica = null;
                try {
                    if (! idTextField.getText().equals("")) {
                        int ID = Integer.parseInt(searchText.replaceAll("[^0-9]", ""));
                        // Verifica se o texto contém "B" ou "H"
                        if (searchText.contains("B")) {
                            // Se contiver "B", chama a função para pesquisar na B-Tree
                            Logs.Succeed("Pesquisando na B-Tree");
                            musica = Arq.searchBtree(ID);
                        } else if (searchText.contains("H")) {
                            // Se contiver "H", chama a função para pesquisar na Hash
                            Logs.Succeed("Pesquisando em Hash");
                            musica = Arq.searchHash(ID);
                        } else {
                            // Caso contrário, realiza a pesquisa pelo ID
                            Logs.Succeed("Pesquisando por ID sequencial");
                            musica = Arq.FindSongID(ID);
                        }
                    } else {
                        String genre = generoTextField.getText();
                        searchByGenre(genre);
                        return;
                    }

                    if (musica != null) {
                        Logs.Details("Musica encontrada:\n" + musica.toString());
                        LimparTabela();

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
                    limparCampos();
                } catch (NumberFormatException ex) {
                    // Se não for possível extrair um número de ID, trata como pesquisa por outro
                    // critério
                    if (!generoTextField.getText().equals("")) {
                        searchByGenre(generoTextField.getText());
                        generoTextField.setText("");
                    }

                    Logs.Alert("Erro ao pesquisar a música:\nNúmero esperado " + ex.getMessage());
                } catch (Exception ex) {
                    Logs.Alert("Erro ao pesquisar a música:\n" + ex.getMessage());
                    if (!generoTextField.getText().equals("")) {
                        searchByGenre(generoTextField.getText());
                        generoTextField.setText("");
                    }
                }
            }
        });

        deletarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int ID = Integer.parseInt(idTextField.getText());
                    Logs.Details("Status de deletar: " + Arq.DeleteSong(ID));
                    limparCampos();
                } catch (NumberFormatException ex) {
                    Logs.Alert("Erro ao deletar a música:\n Numero esperado " + ex.getMessage() + "\n" + ex.getClass());
                } catch (Exception ex) {
                    Logs.Alert("Erro ao deletar a música:\n" + ex.getMessage() + "\n" + ex.getClass());
                }

                listarButton.doClick();
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpa a tabela antes de listar os registros
                tableModel.setRowCount(0);// limpar a tabela
                Musica musica;
                Arq.IniciarLeituraSequencial();

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
                // adicionar aos campos
                nomeTextField.setText(musica.getNome());
                artistaTextField.setText(musica.getArtista());
                popularidadeTextField.setText(String.valueOf(musica.getPopularidade()));
                dataLancamentoTextField.setText(musica.getDataLancamento());

                generoTextField.setText(musica.getGenero().replace(',', ';')); // repalce , with ; -> function
                dancabilidadeTextField.setText(String.valueOf(musica.getDancabilidade()));
                hashTextField.setText(musica.getHash());

                listarButton.doClick();
            }
        });

        listUnvalidRegistersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_to_recover = Integer.parseInt(idTextField.getText());
                    Arq.IniciarLeituraSequencial();

                    if (id_to_recover != 0) { // preguiç de fazer um botão para recuperar
                        if (Arq.Recover(id_to_recover)) {
                            Logs.Succeed("interface : Registro recuperado com sucesso!");
                        } else {
                            Logs.KindaAlert("Interface : Registro não encontrado!");
                        }
                    }
                    idTextField.setText(""); // limpar o campo de texto
                    listUnvalidRegistersButton.doClick(); // atualizar o campo de texto
                    return;
                } catch (Exception error) {
                    Logs.Details("Interface : ID não preenchido para recover ");
                }

                LimparTabela();
                Arq.IniciarLeituraSequencial();
                Musica musica;
                while ((musica = Arq.getInvalidRegister(null)) != null) {
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

    public void LimparTabela() {
        tableModel.setRowCount(0);
    }

    public void DisplaySong(Musica musica) {
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

    public static void Iniciar() {

        Logs.Succeed("=================== Programa Iniciado ===================");

        SwingUtilities.invokeLater(InterfaceMenu::new);

        Logs.Succeed("Interface gráfica iniciada com sucesso e Operando!");
        String red = "\033[0;31m";
        String reset = "\033[0m";
        Logs.KindaAlert("Coloque H ou B antes do ID para pesquisar na Hash ou B-Tree");
        while (Running) {

        }

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

    void searchByGenre(String genre) {
        LimparTabela();
        ArrayList<MetaIndice> musicas = IndiceInvertido.searchGenre(genre);
        ArrayList<Musica> musicasEncontradas = new ArrayList<>();
        for (MetaIndice metaIndice : musicas) {
            Musica musica = Arq.getByIndice(metaIndice.getPosicao());
            if (musica != null) {
                musicasEncontradas.add(musica);
            }
        }
        for (Musica musica : musicasEncontradas) {
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
            }
        }
    }

    public static void main(String args[]) {
        Arq.Iniciar("songs.db");
        Musica.iniciar();
        Iniciar();

        // Array de símbolos de operação para animação
    }
}
