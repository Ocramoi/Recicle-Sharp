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
        OutputStream oStream = httpExchange.getResponseBody();
        StringBuilder respostaStr = new StringBuilder();

        respostaStr.append("{\n\t'httpCode': 200,\n\t'data': [");
        for (Ponto p : pontos) {
            respostaStr.append("{");
            respostaStr.append(p.toJSON());
            respostaStr.append("}, ");
        }
        respostaStr.append("]\n}");

        String respostaHttp = respostaStr.toString();
        byte[] byteSetResposta = respostaHttp.getBytes(StandardCharsets.UTF_8);
        httpExchange.sendResponseHeaders(200, byteSetResposta.length);
        oStream.write(byteSetResposta);
        oStream.flush();
        oStream.close();
    }
}
