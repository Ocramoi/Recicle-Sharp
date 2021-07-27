import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Servidor extends Thread {
    private HttpServer server = null;
    private int PORTA = 8080;

    private void criaServer() {
        try {
            server = HttpServer.create(
                    new InetSocketAddress("localhost", PORTA),
                    0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Servidor() {
        criaServer();
    }

    public Servidor(int port) {
        this.PORTA = port;
        criaServer();
    }

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
