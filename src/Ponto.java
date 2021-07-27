import java.util.ArrayList;

public class Ponto {
    public String usuario = null,
            endereco = null;
    public String[][] horarios;
    public char tipo = ' ',
            situacao = ' ';
    public boolean[] dias = new boolean[7];
    public float quantidade,
            latitude,
            longitude;

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

        String[] entradasHorario = horarios.split(",");
        this.horarios = new String[entradasHorario.length/2][2];
        for (int i = 0; i < this.horarios.length; ++i) {
            this.horarios[i][0] = String.format("%s:%s",
                    entradasHorario[2*i].substring(0, 2),
                    entradasHorario[2*i].substring(2, 4)
            );
            this.horarios[i][1] = String.format("%s:%s",
                    entradasHorario[2*i + 1].substring(0, 2),
                    entradasHorario[2*i + 1].substring(2, 4)
            );
        }

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

    public String toJSON() {
        StringBuilder horariosStr = new StringBuilder("["),
                diasStr = new StringBuilder("[");

        for (String[] tupla : this.horarios) {
            horariosStr.append("['").append(tupla[0]).append("', '").append(tupla[1]).append("], ");
        }
        horariosStr.append("]");

        for (String dia : diasStr()) {
            diasStr.append("'");
            diasStr.append(dia);
            diasStr.append("', ");
        }
        diasStr.append("]");

        return String.format(
                "'usr': '%s'," +
                        "'tipoResiduo': '%s', " +
                        "'endereco': '%s', " +
                        "'qnt': %s, " +
                        "'dias': %s, " +
                        "'horarios': %s, " +
                        "'situacao': '%s', " +
                        "'latitude': %s, " +
                        "'longitude': %s",
                this.usuario,
                tipoStr(),
                this.endereco,
                this.quantidade,
                diasStr,
                horariosStr,
                situacaoStr(),
                this.latitude,
                this.longitude
        );
    }
}
