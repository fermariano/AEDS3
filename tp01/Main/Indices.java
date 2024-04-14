//classe dedicada para fazer indices dos arquivos com base em ID's

import java.io.RandomAccessFile;

/**
 * Indices
 */
public class Indices {
    // classe para coordenar o arquivo de indices
    /**
     * InnerIndices
     */

    private static RandomAccessFile file;

    Indices(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void start(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MetaIndice findIndice(int id) {// hope never use this, cause its O(n)
        try {
            file.seek(0);
            while (file.getFilePointer() < file.length()) {
                int currentId = file.readInt();
                long currentPos = file.readLong();
                if (currentId == id) {
                    return new MetaIndice(currentId, currentPos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void writeIndice(int id, long pos) {
        try {
            // pos is the position of the record in the file
            file.seek(file.length());
            file.writeInt(id);
            file.writeLong(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MetaIndice[] getAllIndices(String path) {
        try {
            file = new RandomAccessFile(path, "rw");
            file.seek(0);
            // le primeiras 12 indices errados
            for (int i = 0; i < 12; i++) {
                file.readInt();
                file.readLong();
            }

            MetaIndice[] indices = new MetaIndice[(int) (file.length() / 12)];
            for (int i = 0; i < indices.length - 12; i++) {
                indices[i] = new MetaIndice(file.readInt(), file.readLong());
            }
            return indices;
        } catch (Exception e) {
            if (e instanceof java.io.EOFException) {
            }
        }
        return null;
    }

}
