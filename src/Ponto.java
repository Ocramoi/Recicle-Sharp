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

import java.util.ArrayList;

public class Ponto {
    public String usuario = null,
            endereco = null;
    public String horarios;
    public char tipo = ' ',
            situacao = ' ';
    public boolean[] dias = new boolean[7];
    public float quantidade,
            latitude,
            longitude;

    /**
     * Retorna a situação do ponto de coleta em string
     *
     */
    public String situacaoStr() {
        String retorno;
        switch (this.situacao) {
            case 'P':
                retorno = "Procurando catador";
                break;
            case 'C':
                retorno = "Em coleta";
                break;
            default:
                retorno = "Coletado";
                break;
        }
        return retorno;
    }

    /**
     * Retorna o tipo de material do ponto de coleta em string
     *
     */
    public String tipoStr() {
        String retorno;
        switch (this.tipo) {
            case 'E':
                retorno = "Eletrônico";
                break;
            case 'R':
                retorno = "Reciclável";
                break;
            case 'O':
                retorno = "Orgânico";
                break;
            default:
                retorno = "Comum";
                break;
        }
        return retorno;
    }

    /**
     * Retorna um vetor de strings com os dias da semana nos quais um ponto de coleta está disponível
     *
     * @return vetor de strings
     */
    public String[] diasStr() {
        ArrayList<String> diasFull = new ArrayList<>();
        String[] diasSemana = {
                "Domingo",
                "Segunda-feira",
                "Terça-feira",
                "Quarta-feira",
                "Quinta-feira",
                "Sexta-feira",
                "Sábado"
        };
        for (int i = 0; i < 7; ++i) {
            if (this.dias[i])
                diasFull.add(diasSemana[i]);
        }

        String[] arRetorno = new String[diasFull.size()];
        for (int i = 0; i < arRetorno.length; ++i)
            arRetorno[i] = diasFull.get(i);
        return arRetorno;
    }

    /**
     * Inicializador da classe de Ponto
     *
     */
    public Ponto(String usuario,
                 String tipo,
                 String endereco,
                 float qnt,
                 String dias,
                 String horarios,
                 String situacao,
                 float latitude,
                 float longitude
    ) {
        assert (usuario != null &&
                endereco != null &&
                horarios != null &&
                tipo != null &&
                situacao != null &&
                dias != null &&
                qnt >= 0);
        this.usuario = usuario;
        this.endereco = endereco;

        this.horarios = horarios;

        this.tipo = tipo.charAt(0);
        this.situacao = situacao.charAt(0);
        this.quantidade = qnt;
        this.latitude = latitude;
        this.longitude = longitude;

        assert dias.length() == 7;
        this.dias = new boolean[7];
        for (int i = 0; i < dias.length(); ++i)
            this.dias[i] = dias.charAt(i) == 'S';
    }

    /**
     * Cria uma representação do ponto de coleta em json
     *
     * @return string contendo a representação do ponto
     */
    public String toJSON() {
        StringBuilder diasStr = new StringBuilder("[");

        String[] dS = diasStr();
        for (int i = 0; i < dS.length - 1; ++i) {
            diasStr.append("\"");
            diasStr.append(dS[i]);
            diasStr.append("\", ");
        }
        if (dS.length > 0) {
            diasStr.append("\"");
            diasStr.append(dS[dS.length - 1]);
            diasStr.append("\"");
        }

        diasStr.append("]");

        return String.format(
                "\"usr\": \"%s\", " +
                        "\"tipoResiduo\": \"%s\", " +
                        "\"endereco\": \"%s\", " +
                        "\"qnt\": %s, " +
                        "\"dias\": %s, " +
                        "\"horarios\": \"%s\", " +
                        "\"situacao\": \"%s\", " +
                        "\"latitude\": %s, " +
                        "\"longitude\": %s",
                this.usuario,
                tipoStr(),
                this.endereco,
                this.quantidade,
                diasStr,
                this.horarios,
                situacaoStr(),
                this.latitude,
                this.longitude
        );
    }
}
