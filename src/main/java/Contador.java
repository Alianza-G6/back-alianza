import java.io.*;

public class Contador {
    private final String arquivo;

    public Contador(String arquivo) {
        this.arquivo = arquivo;
    }

    public int ler() {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine();
            return linha != null ? Integer.parseInt(linha) : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public void atualizar(int novaLinha) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            bw.write(String.valueOf(novaLinha));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
