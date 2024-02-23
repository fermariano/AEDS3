import java.io.*;

public class MetaData {
    boolean lapide;
    int sizeBytes;

    MetaData() {
        this.lapide = false;
        this.sizeBytes = 0;
    }

    void readMetaData(DataInputStream dis) {
        try {
            this.lapide = dis.readBoolean(); // Lê a lapide
            this.sizeBytes = dis.readInt(); // Lê o tamanho do registro da música
        } catch (IOException e) {
            System.out.println("Erro ao ler os metadados: " + e.getMessage());
        }
    }


    void writeMetaData(DataOutputStream dos, byte[] sizebytes) {
        try {
            dos.writeBoolean(true); // Escreve a lapide como verdadeira
            dos.writeInt(sizebytes.length); // Escreve o tamanho do registro da música
        } catch (IOException e) {
            System.out.println("Erro ao escrever os metadados: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Lapide: " + lapide + "\nTamanho: " + sizeBytes;
    }
}
