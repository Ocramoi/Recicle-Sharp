import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
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

        respostaStr.append("{\n'httpCode': 200,\n'data': [");
        for (Ponto p : pontos) {
            respostaStr.append("{");
            respostaStr.append(p.toJSON());
            respostaStr.append("}");
        }
        respostaStr.append("]\n}");

        String respostaHttp = respostaStr.toString();
        httpExchange.sendResponseHeaders(200, respostaHttp.length());
        oStream.write(respostaHttp.getBytes());
        oStream.flush();
        oStream.close();
    }
}
