/**
 * Recycle#
 * Trabalho Final - Programação Orientada a Objetos
 *
 * Autores:
 *      Eduardo Rodrigues Amaral - 11735021
 *      Frederico Xavier Capanema - 12433364
 *      João Marcos Cardoso da Silva - 11795314
 *      Marco Antônio Ribeiro de Toledo - 11796419
 */

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Servidor extends Thread {
    private HttpServer server = null;
    private int PORTA = 8080;

    /**
     * Cria o servidor http
     *
     */
    private void criaServer() {
        try {
            server = HttpServer.create(
                    new InetSocketAddress("localhost", PORTA),
                    0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializador da classe de Servidor, na porta padrão de 8080
     *
     */
    public Servidor() {
        criaServer();
    }

    /**
     * Inicializador da classe de Servidor
     *
     * @param port : porta do servidor
     */
    public Servidor(int port) {
        this.PORTA = port;
        criaServer();
    }

    /**
     * Ativa o servidor na porta salva
     *
     */
    @Override
    public void run() {
        assert server != null;

        server.createContext("/data", new DataHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor inicializado na porta " +
                PORTA +
                "...");
    }
}
