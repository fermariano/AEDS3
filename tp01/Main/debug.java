import java.io.RandomAccessFile;

public class debug {
    static RandomAccessFile indFile;
    static void recover(int x)throws Exception{

        System.err.println("recovering " + x);
        indFile.seek(0);
        indFile.writeInt(x);
        recover(x + 1);
    }

    public static void main(String[] args) throws Exception{
        indFile = new RandomAccessFile("indFile", "rw");
        System.out.println("debugging _" + indFile.readInt());
    }
}
