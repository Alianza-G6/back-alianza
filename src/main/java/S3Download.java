import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class S3Download {
    public void downloadFile(String url, String caminhoLocal) {
        try (InputStream in = new URL(url).openStream();
             FileOutputStream out = new FileOutputStream(caminhoLocal)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Arquivo CSV baixado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

