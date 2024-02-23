import java.io.*;

public class MetaData {
    boolean lapide;
    int sizeBytes;

    MetaData() {
        this.lapide = false;
        this.sizeBytes = 0;
    }

    void readMetaData(RandomAccessFile file) {
        try {
            file.seek(0);
            this.lapide = file.readBoolean();
            this.sizeBytes = file.readInt();
        } catch (IOException e) {
            System.out.println("Erro ao ler os metadados: " + e.getMessage());
        }
    }

    boolean readLapide(RandomAccessFile file) {
        try {
            this.lapide = file.readBoolean();
        } catch (IOException e) {
            System.out.println("Erro ao ler a validade: " + e.getMessage());
        }
        return this.lapide;
    }

    int readSizeBytes(RandomAccessFile file) {
        try {
            this.sizeBytes = file.readInt();
        } catch (IOException e) {
            System.out.println("Erro ao ler o tamanho: " + e.getMessage());
        }
        return this.sizeBytes;
    }

    void writeMetaData(DataOutputStream dos, byte[] sizebytes) {
        try {
            dos.writeBoolean(true); // Escreve a lapide como verdadeira
            dos.writeInt(sizebytes.length); // Escreve o tamanho do registro da música
        } catch (IOException e) {
            System.out.println("Erro ao escrever os metadados: " + e.getMessage());
        }
    }
}
