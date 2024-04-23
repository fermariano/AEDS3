
//metodos de escrita e leitura aqui 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Arq {

    private static class DataFinder {
        long seekSaver; // irá armazenar a posição do inicio do registro
        long metaSeek;// irá armazenar a posição do inicio da lapide
        MetaData metaData; // irá armazenar os metadados da musica Encontrada
        Musica musica; // irá armazenar a musica encontrada

        public boolean FindSong(int ID) { // procura a musica por id
            IniciarLeituraSequencial(); // funçao que le o ultimo id inserido
            int idSeacher = -1;
            try {
                Logs.Details("Procurando a música com ID: " + ID + "...");
                for (; idSeacher != ID || Arq.meta.lapide; raf.seek(raf.getFilePointer() + (Arq.meta.sizeBytes - 4))) {

                    this.metaSeek = raf.getFilePointer(); // salva a posição do inicio da lapide
                    Arq.meta.readMetaData(); // le os metadados
                    this.metaData = Arq.meta; // salva os metadados na classe
                    this.seekSaver = raf.getFilePointer(); // salva a posição do inicio do registro
                    idSeacher = raf.readInt(); // le o ID
                }
                Logs.Succeed("Musica encontrada: " + idSeacher);
                raf.seek(this.metaSeek); // volta para o inicio do registro
                this.musica = Arq.getRegistro(); // le o registro e cria o objeto

                return true;
            } catch (IOException e) {
                Logs.Alert("Erro ao buscar a música: " + e.getMessage());
                this.metaData = null;
                return false;
            }

        }

    }

    protected static boolean status = false; // se esta em operação
    protected static String file; // nome do arquivo
    protected static RandomAccessFile raf;
    protected static DataOutputStream dos;
    protected static DataInputStream dis;
    protected static MetaData meta;

    // inicia o arquivo/cria o raf para gerenciar
    public static void Iniciar(String filex) {
        Arq.file = "Source/DataBase/songs.db";
        try {
            Arq.raf = new RandomAccessFile(file, "rw"); // cria pra escrita/leitura
            Arq.status = true;
            Arq.meta = new MetaData(raf);
            Musica.setLastID(raf.readInt()); // pega o ultimo id inserido
            Logs.Succeed("Arquivo aberto com sucesso!\nClasse Arq Iniciada com sucesso!");
            Indices.start("Source/DataBase/indices.db");
            IndiceInvertido.start();
            Diretorio.start();
            Btree.start();
            
        } catch (IOException e) {
            Logs.Alert("Erro ao abrir o arquivo: " + e.getMessage());
        }

    }

    public static void updateFile(RandomAccessFile gg) {
        raf = gg;
        meta = new MetaData(raf);
        Logs.Succeed("Atualizado com sucesso!");
    }

    // procurar a musica
    public static Musica FindSongID(int ID) {
        Musica musica = null;
        DataFinder finder = new DataFinder();
        if (finder.FindSong(ID)) { // se ele achar a musica
            musica = finder.musica; // retorna a musica
        }
        return musica;
    }

    // pega o status do programa
    public static boolean getStatus() {
        return status;
    }

    // deleta o som pelo id
    public static boolean DeleteSong(int id) {
        boolean status = false;
        DataFinder finder = new DataFinder();

        if (finder.FindSong(id)) { // encontrou a musica a ser deletada
            try {
                raf.seek(finder.metaSeek); // coloca o cabeçote no inicio do registro que vai ser deletado
                raf.writeBoolean(true); // seta a lapide como true (que deus o tenha)
                status = true;
                Logs.Succeed("Musica deletada com sucesso!");
            } catch (IOException e) {
                Logs.Alert("Erro ao deletar a música: " + e.getMessage());
            }
        } else { // se nao encontrou a musica
            Logs.Alert("Musica não encontrada");
        }
        return status;
    }

    private static void MakeIndice(int ID, Long pos) {
        try {
            Indices.writeIndice(ID, pos);
        } catch (Exception e) {
            Logs.Alert("Erro ao criar o indice: " + e.getMessage());
        }
    }

    // atualiza os dados da musica
    public static void UpdateSong(int ID, String newSong) {
        DataFinder finder = new DataFinder();
        if (finder.FindSong(ID)) {
            try {
                Musica nova = Musica.StringToMusica(newSong); // cria um novo objeto com a string de parametro
                int novaSize = nova.toByteArray().length; // pega o tamanho da string em bytes

                if (novaSize <= finder.metaData.sizeBytes) { // pode sobrescrever (mesmo tamanho ou igual)
                    raf.seek(finder.seekSaver); // coloca o ponteiro no inicio do registro
                    raf.write(nova.toByteArray()); // sobrescreve
                    // volta para o inicio do registro (lapide)
                    Logs.Details("Registro Alterado é menor ou igual ao anterior, sobrescrevendo...");
                } else { // se não puder sobrescrever (for maior)
                    raf.seek(finder.metaSeek); // volta para o inicio do registro (lapide)
                    raf.writeBoolean(true); // marca como lapide
                    addRegistroExistenteEOF(nova);// update em fim de arquivo sem alterar ultimo ID
                    Logs.Details(
                            "Registro Alterado é maior que o anterior, deletando e adicionando no fim do arquivo...");
                }

                Logs.Succeed("Musica atualizada com sucesso!");
            } catch (IOException e) {
                Logs.Alert("Erro no update : " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Logs.Alert("Erro no Update :\n Illegal Argument Exception" + e.getMessage());
            } catch (NullPointerException e) {
                Logs.Alert("Erro no Update :\n Null Pointer Exception" + e.getMessage());
            }

        } else {
            Logs.Alert("Musica não encontrada");
        }
    }

    public static void IniciarLeituraSequencial() {

        try {
            raf.seek(4);// pula o lastID
            Logs.Details("Leitura sequencial iniciada!");
        } catch (IOException e) {
            Logs.Alert("Erro ao iniciar a leitura sequencial: " + e.getMessage());
        }
    }

    // le o registro
    public static Musica getRegistro() { // retorna registros validos
        Musica buffer = new Musica();
        try {

            byte[] bytes;
            do {
                Arq.meta.readMetaData();
                bytes = new byte[Arq.meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
                raf.readFully(bytes); // Lê o registro completo
            } while (Arq.meta.lapide); // so retorna reg valido

            buffer = Musica.fromByteArray(bytes);
            return buffer;
        } catch (IOException e) {
            if (e instanceof java.io.EOFException) {
                Logs.Succeed("Leitura Finalizada!\n Raf esta em EOF Exception :" + e.getMessage());
                return null;
            }
            Logs.Alert("Erro em Leitura de Registro \nException :" + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            Logs.Alert("Erro na Conversão do Registro : " + e.getMessage());
            Logs.KindaAlert("Registro Lido: " + buffer.toString());
            Logs.KindaAlert("Lapide: " + Arq.meta.lapide + " Tamanho: " + Arq.meta.sizeBytes);
            return null;
        }

    }

    // Adiciona um novo registro
    public static boolean addRegistro(String str) {
        byte[] ba;
        try {
            Musica nova = Musica.StringToMusica(str); // cria o objeto com a string
            nova.id = Musica.getLastID() + 1; // seta o id da nova musica

            ba = nova.toByteArray(); // atualiza lastID na classe mas não no arquivo
            Musica.setLastID(nova.getId()); // atualiza na classe
            writeLastID(Musica.getLastID()); // atualiza no arquivo
            
            raf.seek(raf.length()); // joga o ponteiro do RAF para o final do arquivo
            long pos = raf.getFilePointer();
            meta.writeMetaData(ba.length); // escreve os metadados do registro
            raf.write(ba); // escreve o registro
            Logs.Succeed("Registro Adicionado com sucesso!");
            MakeIndice(nova.getId(), pos);
            MetaIndice indice = new MetaIndice(nova.getId(), pos);
            Btree.add(indice);
            Diretorio.inserir(indice);
            IndiceInvertido.addIndice(indice);

            return true;
        } catch (IOException e) {
            Logs.Alert(" Erro em Adicionar Registro addRegistro()\n error = " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            Logs.Alert("Erro na Conversão da String : " + e.getMessage());
            return false;
        }
    }

    // adiciona um registro existente
    public static boolean addRegistroExistenteEOF(Musica newsong) {
        try {
            byte[] ba = newsong.toByteArray(); // tranforma o registro em array de bytes
            long pos = raf.getFilePointer();
            raf.seek(raf.length()); // leva o ponteiro do RAF para o final do arquivo
            meta.writeMetaData(ba.length); // escreve o metadado do registro
            raf.write(ba); // escreve o registro
            MetaIndice indice = new MetaIndice(newsong.getId(), pos);
            Btree.updateIndex(indice);// atualiza Btree
            Diretorio.updateIndex(indice);// atualiza Index Hash

            return true;
        } catch (IOException e) {
            Logs.Alert("Erro ao Adicionar registros em fim de arquivo!\n addRegistroExistenteEOF Exception :"
                    + e.getMessage());
            return false;
        }
    }

    // le o ultimo id inserido
    public static int ReadLastID() {
        try {
            raf.seek(0); // vai pro inicio do arquivo
            return raf.readInt(); // le o id
        } catch (IOException e) {
            Logs.Alert("Erro ao ler o último ID: " + e.getMessage());
            return -1;
        }

    }

    public static Musica[] getSongs(int quantitiy) {
        Musica[] songs = new Musica[quantitiy];
        IniciarLeituraSequencial();
        for (int i = 0; i < quantitiy; i++) {
            songs[i] = getRegistro();
        }
        return songs;
    }

    // escreve o ultimo id inserido
    public static void writeLastID(int id) {
        try {
            raf.seek(0); // vai pro inicio do arquivo
            raf.writeInt(id); // escreve o ultimo id
            Logs.Succeed("Ultimo ID escrito com sucesso!");
            Logs.Details("Ultimo ID: " + id);
        } catch (IOException e) {
            Logs.Alert("Erro ao escrever o último ID: " + e.getMessage());
        }
    }

    // finaliza o programa
    public static void Finalizar() {
        status = false;
        try {
            raf.close(); // fecha o arquivo
        } catch (IOException e) {
            Logs.Alert("Erro ao fechar o arquivo: " + e.getMessage());
        }

    }

    public static Musica getInvalidRegister(DataFinder recover) {
        Musica buffer = new Musica();
        try {
            byte[] bytes;
            do {
                if (recover != null) {
                    recover.metaSeek = raf.getFilePointer(); // salva a posição do inicio da lapide
                }
                Arq.meta.readMetaData();
                bytes = new byte[Arq.meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
                raf.readFully(bytes); // Lê o registro completo
            } while (!Arq.meta.lapide); // so retorna reg valido

            buffer = Musica.fromByteArray(bytes);
            return buffer;

        } catch (IOException e) {
            if (e instanceof java.io.EOFException) {
                Logs.Alert("Registro não encontrado!\n Raf esta em EOF Exception :" + e.getMessage());
                return null;
            }
            Logs.Alert("Erro em Leitura de Registro \nException :" + e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            Logs.Alert("Erro na Conversão do Registro : " + e.getMessage());
            Logs.KindaAlert("Registro Lido: " + buffer.toString());
            Logs.KindaAlert("Lapide: " + Arq.meta.lapide + " Tamanho: " + Arq.meta.sizeBytes);
            return null;
        }

    }

    public static boolean Recover(int id) throws Exception {
        boolean status = false;
        int idbuffer = -1;
        DataFinder recover = new DataFinder();
        while (idbuffer != id && !status) {
            Musica buffer = getInvalidRegister(recover);
            if (buffer != null) {
                idbuffer = buffer.getId();
                if (idbuffer == id) {
                    try {
                        raf.seek(recover.metaSeek); // coloca o ponteiro no inicio do registro
                        raf.writeBoolean(false); // seta a lapide como false
                        status = true;
                        Logs.Succeed("Musica recuperada com sucesso!");
                    } catch (IOException e) {
                        Logs.Alert("Erro ao recuperar a música: " + e.getMessage());
                    }
                    status = true;
                }
            } else {
                break;
            }
        }
        return status;
    }

    public static int getBytesMedium() {
        try {
            raf.seek(4);// pula o lastID
            int totalBytes = 0;
            int count = 0;
            while ((getRegistro()) != null) {
                totalBytes += Arq.meta.sizeBytes; // Arq tem o tamanho do registro da musica lida
                count++;
            }

            return totalBytes / count;
        } catch (Exception e) {
            Logs.Alert("Erro ao calcular a média de bytes: " + e.getMessage());
            return -1;
        }

    }

    public static long getSize() {
        try {
            return raf.length();
        } catch (IOException e) {
            Logs.Alert("Erro ao pegar o tamanho do arquivo: " + e.getMessage());
            return -1;
        }
    }

    public static void insertIntoBucket() {
        MetaIndice meta[];
        meta = Indices.getAllIndices("Source/DataBase/indices.db");
        for (int i = 0; i < meta.length; i++) {
            Diretorio.inserir(meta[i]);
            if (i % 1000 == 0) {
                Logs.Details("Inserindo : " + i);
            }
        }
        Diretorio.printDir();
    }

    public static void searchAllHash() {
        System.out.println("\n\n");
        System.out.println("Buscando todos os índices na Hash");
        MetaIndice meta[] = Indices.getAllIndices("Source/DataBase/indices.db");
        double start = System.currentTimeMillis();
        int notFound = 0;
        int found = 0;
        int totalIndices = meta.length;
        int searched = 0;

        for (int i = 0; i < totalIndices; i++) {
            searched++;

            Musica find = searchHash(meta[i].getId());
            if (find == null) {
                notFound++;
            } else {
                found++;
            }

            double percentage = (double) searched / totalIndices * 100;
            double elapsedTime = (System.currentTimeMillis() - start) / 1000.0;
            double assertiveness = (double) found / searched * 100;

            System.out.print("\rProgress " + String.format("%.2f", percentage) + "% - Tempo: "
                    + String.format("%.2f", elapsedTime) + " segundos - Assertividade: "
                    + String.format("%.2f", assertiveness) + "%");
        }
        System.out.println();
        double totalTime = (System.currentTimeMillis() - start) / 1000.0;
        double assertiveness = (double) found / searched * 100;
        Logs.Details("Busca concluída.");
        Logs.Details("Tempo total: " + totalTime + " segundos");
        Logs.Details("Total de índices encontrados: " + found);
        Logs.Details("Total de índices não encontrados: " + notFound);
        Logs.Details("Assertividade: " + String.format("%.2f", assertiveness) + "%");
        System.out.println("Fim do teste de busca em Hash \n\n");
    }

    public static Musica getByIndice(long pos) {
        try {
            raf.seek(pos);
            meta.readMetaData();
            if (meta.lapide) {
                return null;
            }
            byte[] bytes = new byte[meta.sizeBytes];
            raf.readFully(bytes);
            return Musica.fromByteArray(bytes);
        } catch (IOException e) {
            Logs.Alert("Erro ao pegar o indice: " + e.getMessage());
            return null;
        }
    }

    public static Musica searchBtree(int ID) {
        MetaIndice meta = Btree.search(ID);
        if (meta != null) {
            // Logs.Succeed("Encontrado em Btree: " + meta.getId());
            return getByIndice(meta.getPosicao());
        } else {
            Logs.Alert("Não encontrado");
            return null;
        }
    }

    static void searchAllBtree() {
        System.out.print("\n\n");
        try {
            MetaIndice meta[] = Indices.getAllIndices("Source/DataBase/indices.db");
            double start = System.currentTimeMillis();
            double end;
            double total = 0;
            double conferidos = 0;

            for (int i = 0; i < meta.length; i++) {
                if (meta[i] != null) {
                    Musica music = searchBtree(meta[i].getId());
                    total++;
                    if (music != null) {
                        conferidos++;
                    }
                }
                double porcentagem = (conferidos / total) * 100;
                double assertividade = (conferidos / total) * 100;
                double elapsedTime = (System.currentTimeMillis() - start) / 1000.0;
                System.out.print("\rProgress " + String.format("%.2f", porcentagem) + "% - Tempo: "
                        + String.format("%.2f", elapsedTime) + " segundos - Assertividade: "
                        + String.format("%.2f", assertividade) + "%");
            }
            System.out.println();
            end = System.currentTimeMillis();
            double totalTime = (end - start) / 1000.0;
            double assertividadeFinal = (conferidos / total) * 100;
            Logs.Succeed("Busca concluída.");
            Logs.Succeed("Tempo total: " + totalTime + " segundos");
            Logs.Succeed("Assertividade final: " + String.format("%.2f", assertividadeFinal) + "%");
            System.out.println("Fim do teste de busca em Btree \n\n");
        } catch (Exception e) {
            Logs.Alert("Erro ao buscar todos os índices na Btree: " + e.getMessage());
        }
    }
    public static Musica searchHash(int ID) {
        MetaIndice meta = Diretorio.search(ID);
        if (meta != null) {

            return getByIndice(meta.getPosicao());
        } else {
            Logs.Alert("Não encontrado");
            return null;
        }
    }
    static void addGenres() {
        MetaIndice indices[] = Indices.getAllIndices("Source/DataBase/indices.db");
        for (int i = 0; i < indices.length; i++) {
            IndiceInvertido.addIndice(indices[i]);
            if (i % 1000 == 0) {
                Logs.Details("Inserindo : " + i);
            }
        }
    }
    static void testePerformance() {
        MetaIndice chaves[] = Indices.getAllIndices("Source/DataBase/indices.db");
        double BtreeTime = System.currentTimeMillis();
        for (int i = 0; i < chaves.length; i++) {
            searchBtree(chaves[i].getId());
        }
        BtreeTime = System.currentTimeMillis() - BtreeTime;
        double hashTime = System.currentTimeMillis();
        for (int i = 0; i < chaves.length; i++) {
            searchHash(chaves[i].getId());
        }
        hashTime = System.currentTimeMillis() - hashTime;
        Logs.Details("Hash -> " + hashTime + "\nBtree -> " + BtreeTime + "");
    }
}
