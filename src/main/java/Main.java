import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<String> tiposLogs = new ArrayList<>(List.of("INFORMATIVO", "ALERTA", "ERRO", "SUCESSO"));

        // Associação de Log's
        // INFORMATIVO = ...
        // ALERTA = !
        // ERRO = Falha
        // SUCESSO = êxito

        List<String> eventos = new ArrayList<>(List.of(
                "Buscando novas atualizações...",
                "Uma nova atualização foi encontrada!",
                "Instalando nova atualização!",
                "Atualização feita com êxito",
                "Iniciando aplicação!",
                "Carregando configurações!",
                "Abrindo nova conexão com banco de dados !",
                "Êxito em abrir conexão com banco de dados",
                "Buscando atualização de bases !",
                "Gerando gráficos!",
                "Falha ao buscar base",
                "Êxito ao buscar nova base",
                "Fechando conexão com banco de dados !",
                "Reiniciando serviço !",
                "Finalizando sessão do usuário x !",
                "Êxito ao finalizar sessão do usuário x",
                "Finalizando a aplicação...",
                "Aplicação encerrada com êxito"
        ));

        for (String evento : eventos) {
            String tipoLog = "INFORMATIVO";

            if (evento.contains("Falha")) {
                tipoLog = "ERRO";
            } else if (evento.contains("êxito")) {
                tipoLog = "SUCESSO";
            } else if (evento.contains("!")) {
                tipoLog = "ALERTA";
            }

            LocalDateTime now = LocalDateTime.now();
            String dataHora = now.format(formatter);

            System.out.println(dataHora + " [" + tipoLog + "] - " + evento);

            int tempoMs = ThreadLocalRandom.current().nextInt(1000, 8000);
            Thread.sleep(tempoMs);
        }
    }
}
