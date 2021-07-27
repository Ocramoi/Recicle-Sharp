import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ArrayList<Ponto> pontos = FuncoesDB.retornaPontos();
        respostaJson(httpExchange, pontos);
    }

    private void respostaJson(HttpExchange httpExchange, ArrayList<Ponto> pontos) throws IOException {
        StringBuilder respostaStr = new StringBuilder();

        respostaStr.append("{'httpCode': 200, 'data': [");
        for (Ponto p : pontos) {
            respostaStr.append("{");
            respostaStr.append(p.toJSON());
            respostaStr.append("}, ");
        }
        respostaStr.append("] }");

        String respostaHttp = respostaStr.toString();
        byte[] byteSetResposta = respostaHttp.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Content-type", "application/json");
        httpExchange.sendResponseHeaders(200, byteSetResposta.length);

        OutputStream oStream = httpExchange.getResponseBody();
        oStream.write(byteSetResposta);
        oStream.flush();
        oStream.close();
    }
}
