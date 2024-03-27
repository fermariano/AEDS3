import java.io.Console;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Random;

public class Sorting {

    /**
     * InnerSorting
     */

    static boolean readAll = false;

    final static int setor = 4000; // Tamanho do setor em bytes
    final static int sector_quant_in_block = 10;// Quantidade de setores em um bloco
    final static int blockSize = setor * sector_quant_in_block; // Tamanho do bloco em bytes

    final static long OriginLength = Arq.getSize(); // Tamanho do arquivo original em bytes
    final static int songs_medium_bytes = Arq.getBytesMedium();
    final static int songs_in_block_length = (int) Math.ceil(blockSize / songs_medium_bytes);
    public static int block_quant_in_file = 0; // Quantidade de blocos em um arquivo temporário
    final static int files_quant = 2;

    // cada arquivo temporario terá 10 blocos de tamanho de 16 setores. os setores
    // serão ordenados em memoria e depois
    // intercalados em um arquivo final

    // multiplo do numero de arquivos temporarios

    // temporários

    protected static Musica[] getSectorFromOrigin() {
        int array_size = (int) Math.ceil(setor / songs_medium_bytes);
        Musica[] songs = new Musica[array_size];
        Musica buffer;
        int i = 0;
        for (i = 0; i < array_size; i++) {
            buffer = Arq.getRegistro();
            if (buffer != null) {
                songs[i] = buffer;
                continue;
            }
            readAll = true;
            break; // uncreachable code but when its null, it breaks the loop
        }
        return removeNulls(songs);

    }

    protected static Musica[] getBlockFromOrigin() {
        int musicLenght = songs_in_block_length;
        Musica[] songs_in_block = new Musica[musicLenght];
        Musica[] songs_in_sector;
        int i;
        for (i = 0; i < musicLenght;) {
            songs_in_sector = getSectorFromOrigin();
            for (Musica j : songs_in_sector) {
                if (j == null) {
                    break;
                }

                songs_in_block[i] = j;
                i++; // incrementa o contador
            }
            if (readAll)
                break;
        }
        return removeNulls(songs_in_block);
    }

    protected static Musica[] QuickSort(Musica[] songs) {
        if (songs == null) {
            Logs.Alert("Nenhum registro para ordenar!");
            return null;
        }
        try {
            if (songs.length == 0) {
                Logs.Alert("Nenhum registro para ordenar!");
                return songs;
            }
            return QuickSort(songs, 0, songs.length - 1);
        } catch (Exception error) {
            Logs.Alert("Erro ao ordenar os registros: " + error.getMessage());
            return null;
        }

    }

    protected static Musica[] QuickSort(Musica[] songs, int left, int right) {
        if (left < right) {
            int pivot = partition(songs, left, right);
            QuickSort(songs, left, pivot - 1);
            QuickSort(songs, pivot + 1, right);
        }
        return songs;
    }

    protected static int partition(Musica[] songs, int left, int right) {
        Musica pivot = songs[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (songs[j].getId() < pivot.getId()) {
                i++;
                Musica temp = songs[i];
                songs[i] = songs[j];
                songs[j] = temp;
            }
        }
        Musica temp = songs[i + 1];
        songs[i + 1] = songs[right];
        songs[right] = temp;
        return i + 1;
    }

    public static void IntercalacaoBalanceada() {
        // cria os arquivos temporarios
        // ordena os setores em memoria
        // intercala os setores ordenados
        FileManipulator[] tempFileManipulator = new FileManipulator[files_quant];

        for (int i = 0; i < files_quant; i++) {
            try {
                tempFileManipulator[i] = new FileManipulator("temp" + i + ".db");
            } catch (Exception e) {
                Logs.Alert("Erro crítico ao criar arquivos temporários: 1 " + e.getMessage());
                System.out.println("Erro ao escrever no arquivo temporário: 1 " + e.getMessage());
                return;
            }
        }
        Arq.IniciarLeituraSequencial(); // inicia a leitura sequencial do arquivo original
        int counts = 0;
        while (!readAll) { // colocar nos arquivos temporários (10 blocos por
                           // aqrquivo
            // temporário)
            for (FileManipulator manipulator : tempFileManipulator) { // escrever blocos nos arquivos temporários, 1
                                                                      // por vez e muda de arquivo
                Musica[] songs = getBlockFromOrigin();

                System.out.println("Songs: no bloco | " + songs.length);
                manipulator.blocks_Wrote++;
                songs = QuickSort(songs);

                try {
                    for (Musica song : songs) { // escrever 1 bloco ordenado no arquivo temporário
                        manipulator.metaData.writeMetaData((song.toByteArray().length)); // escreve o tamanho do
                                                                                         // registro
                        manipulator.file.write(song.toByteArray());// escreve o registro
                        counts++;
                    }
                    manipulator.addPointer(manipulator.file.getFilePointer()); // registrando o ponteiro dos blocos
                                                                              // ordenados

                } catch (Exception error) {
                    Logs.Alert("Erro ao escrever no arquivo temporário: " + error.getMessage());
                    System.out.println("Erro ao escrever no arquivo temporário: 2" + error.getMessage());
                    return;
                }
                if (readAll)
                    break;
            }
        }
        Logs.Details("Registros escritos: " + counts);
        try {
            intercalar(tempFileManipulator);

        } catch (Exception e) {
            Logs.Alert("Erro ao criar o arquivo final: " + e.getMessage());
        }
        // =====================[Sucesso]========================

    }

    public static void intercalar(FileManipulator[] readingFiles) {

        FileManipulator[] tempFilesCopy = new FileManipulator[files_quant];

        // Criar cópias dos arquivos temporários
        for (int i = 0; i < files_quant; i++) {
            try {
                tempFilesCopy[i] = new FileManipulator("temp" + (i + 10) + ".db");
            } catch (Exception e) {
                Logs.Alert("Erro ao criar arquivos temporários: " + e.getMessage());
            }
        }

        while (!Oneblock(readingFiles)) { // Enquanto houver mais de um bloco em um arquivo temporário nos 2 arquivos
            try {
                // Resetar a posição de leitura de todos os arquivos de entrada
                for (FileManipulator manipulator : readingFiles) {
                    manipulator.file.seek(0);
                    manipulator.isEOF = false;
                    manipulator.stop = false;
                }
                int fileWriting = 0;

                for (FileManipulator manipulator : readingFiles) {
                    manipulator.registerPointer(); // indice de leitura
                }

                for (; !emptyListPointer(readingFiles);) {
                    System.out.println("quantidade de ponteiros: 1 ==" + readingFiles[0].lista.size());
                    System.out.println("quantidade de ponteiros: 2 ==" + readingFiles[1].lista.size());
                    intercalFile(readingFiles, tempFilesCopy[fileWriting], false);
                    for (FileManipulator manipulator : readingFiles) {
                        manipulator.registerPointer(); // atualiza indice do bloco
                    }
                    fileWriting = (fileWriting + 1) % files_quant;
                }

                System.out.println("Sai com ponteiros: 1 ==" + readingFiles[0].lista.size());
                System.out.println("Sai com ponteiros: 2 ==" + readingFiles[1].lista.size());

                for (FileManipulator manipulator : readingFiles) {
                    manipulator.reestartLista();
                }

                FileManipulator[] temp = readingFiles;
                readingFiles = tempFilesCopy;
                tempFilesCopy = temp;
                for (FileManipulator manipulator : tempFilesCopy) {
                    manipulator.file.setLength(0);
                    manipulator.lista = new LinkedList<Long>();
                }
            } catch (Exception e) {
                Logs.Alert("Erro ao intercalar os arquivos: " + e.getMessage());
            }
        }

        FileManipulator finalFile = new FileManipulator("Sorted.db");
        try {

            finalFile.file.seek(0);
            finalFile.file.writeInt(Musica.getLastID());
            finalFile.isEOF = false;
            finalFile.stop = false;

            for (FileManipulator manipulator : readingFiles) {
                manipulator.file.seek(0);
                manipulator.isEOF = false;
                manipulator.stop = false;
            }

        } catch (Exception e) {
            Logs.Alert("Erro ao escrever no arquivo final: " + e.getMessage());
        }

        intercalFile(readingFiles, finalFile, true);

        Arq.updateFile(finalFile.file);

    }

    protected static void intercalFile(FileManipulator[] readingFiles,
            FileManipulator tempFilesCopy, boolean Final) {

        Logs.Details("Intercalando os arquivos temporários!!!!!");

        while (!Stop(readingFiles)) {
            Musica songToWrite = getMinorSong(readingFiles, Final);
            if (songToWrite != null) {
                try {
                    tempFilesCopy.metaData.writeMetaData(songToWrite.toByteArray().length);
                    tempFilesCopy.file.write(songToWrite.toByteArray());
                } catch (Exception e) {
                    Logs.Alert("Erro ao intercalar os arquivos: " + e.getMessage());
                }
            }
        }

        try {
            tempFilesCopy.addPointer(tempFilesCopy.file.getFilePointer());
            for (FileManipulator file : readingFiles) {// resetar o status de leitura
                file.stop = false;
            }

        } catch (Exception error) {
            Logs.Alert("Erro ao intercalar os arquivos: " + error.getMessage());
        }

    }

    public static boolean emptyListPointer(FileManipulator[] readingFiles) {
        for (FileManipulator manipulator : readingFiles) {
            if (manipulator.lista.size() > 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean Oneblock(FileManipulator[] readingFiles) {
        for (FileManipulator manipulator : readingFiles) {
            if (manipulator.lista.size() >= 1) { // queremos size 0
                return false;
            }
        }
        return true;
    }

    private static boolean Stop(FileManipulator[] readingFiles) {
        try {

            for (FileManipulator manipulator : readingFiles) {
                if (!(manipulator.stop || manipulator.isEOF)) {
                    return false; // parar quanto ponteiro de arquivo for maior que o ponteiro de registro
                }

            }
            return true;
        } catch (Exception e) {
            Logs.Alert("Erro ao intercalar os arquivos: " + e.getMessage());
            return false;
        }
    }

    protected static Musica getMinorSong(FileManipulator[] source, boolean isFinal) {
        Musica minor = null;
        FileManipulator lastMinor = null;
        try {
            for (int i = 0; i < source.length; i++) {
                if (!isFinal) { // não pega parametro pelo pointer

                    if (source[i].file.getFilePointer() >= source[i].register) {
                        source[i].stop = true;
                        continue;
                    }

                    if (source[i].stop)
                        continue;
                }

                if (source[i].isEOF)
                    continue;

                Musica song = getRegistro(source[i]); 

                if (song == null) {
                    source[i].isEOF = true;
                    Logs.Details("EOF no arquivo: " + source[i].file.toString());
                    continue;
                }
                if (minor == null) { // primeira iteração
                    minor = song;
                    lastMinor = source[i];
                } else {
                    if (song.getId() < minor.getId()) {
                        minor = song;
                        retrocede(lastMinor); // retrocede o ponteiro do arquivo
                        lastMinor = source[i];

                    } else {
                        retrocede(source[i]);// retrocede o ponteiro do arquivo pois não é o menor dos que estão
                    }
                }
            }
            if (minor != null) {
                return minor;
            }
        } catch (

        Exception e) {
            Logs.Alert("Erro ao pegar a menor música: " + e.getMessage());
        }
        return null;
    }

    private static void retrocede(FileManipulator file) {
        try {
            file.file.seek(file.file.getFilePointer() - (file.metaData.sizeBytes + 4 + 1));// inteiro e um boolean

        } catch (Exception e) {
            Logs.Alert("Erro ao retroceder o ponteiro do arquivo: " + e.getMessage());
        }
    }

    protected static Musica getRegistro(FileManipulator tempFile) {// lê um registro do arquivo temporario enviado
        Musica buffer;
        try {
            if (!tempFile.metaData.readMetaData()) {// EOF ou outra excessão
                return null;
            }
            byte buffer_Byte[] = new byte[tempFile.metaData.sizeBytes];
            tempFile.file.readFully(buffer_Byte);
            buffer = Musica.fromByteArray(buffer_Byte);
            if (buffer != null) {
                return buffer;
            }
        } catch (Exception error) {
            Logs.Alert("Erro ao ler o registro: | getRegistro " + error.getMessage());
        }
        return null;
    }

    public static Musica[] removeNulls(Musica[] array) {
        int newSize = 0;
        for (Musica element : array) {
            if (element != null) {
                newSize++;
            }
        }
        Musica[] newArray = new Musica[newSize];

        int newIndex = 0;
        for (Musica element : array) {
            if (element != null) {
                newArray[newIndex++] = element;
            }
        }

        return newArray;
    }

}

class FileManipulator {
    int blocks_Wrote = 0;
    MetaData metaData;
    RandomAccessFile file;
    boolean isEOF = false;
    boolean stop = false;
    long register = 0;
    // fila
    public LinkedList<Long> lista;

    public void addPointer(long pointer) {
        lista.addLast(pointer);
    }

    public void registerPointer() {
        try{
          register = lista.removeFirst();  
          register = file.length();
        }catch(Exception e){
            stop = true;
            Logs.KindaAlert("Sem ponteiros para registrar");
        }
        
    }

    public void reestartLista() {
        lista = new LinkedList<Long>();
    }

    public FileManipulator(String filename) {
        try {
            file = new RandomAccessFile(filename, "rw");
            metaData = new MetaData(file);
            lista = new LinkedList<Long>();
        } catch (Exception e) {
            Logs.Alert("Erro ao criar o manipulador de arquivo: " + e.getMessage());
        }

    }

}