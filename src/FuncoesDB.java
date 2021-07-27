import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FuncoesDB {
    public static boolean criaPonto(String usr,
                                    char tipo,
                                    String endereco,
                                    String dias,
                                    String horarios,
                                    char situacao,
                                    int qnt,
                                    float latitude,
                                    float longitude) {
        // Declara nova conexão
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        // Auxiliar de retorno
        boolean ret = false;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("ResourceDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);

            // Executa query com base nos recursos
            try (PreparedStatement ps = con.prepareStatement(res.getString("INSERT_PONTO"))) {
                ps.setString(1, usr);
                ps.setString (2, String.valueOf(tipo));
                ps.setString(3, endereco);
                ps.setInt(4, qnt);
                ps.setString(5, dias);
                ps.setString(6, horarios);
                ps.setString(7, String.valueOf(situacao));
                ps.setFloat(8, latitude);
                ps.setFloat(9, longitude);

                ps.executeUpdate();
            }

            ret = true;
        } catch (SQLException | MissingResourceException throwables) {
            // Confere erro de inserção / conexão
            throwables.printStackTrace();
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return ret;
    }

    private static ArrayList<Ponto> selectPontos(String resStr,
                                                 String campo,
                                                 String filtro) {
        // Declara nova conexão
        Connection con = null;

        ArrayList<Ponto> pontos = new ArrayList<>();

        try {
            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");

            // Declara nova entrada de recursos
            ResourceBundle res = null;

            // Carrega recursos
            res = ResourceBundle.getBundle("ResourceDB");

            String queryStr = res.getString(resStr);
            try (PreparedStatement ps = con.prepareStatement(queryStr)) {
                if (campo != null && filtro != null) {
                    ps.setString(1, campo);
                    ps.setString(2, filtro);
                }
                ResultSet pesq = ps.executeQuery();
                while (pesq.next()) {
                    String usr = pesq.getString("usr"),
                            tipo = pesq.getString("tipoResiduo"),
                            endereco = pesq.getString("endereco"),
                            dias = pesq.getString("dias"),
                            horarios = pesq.getString("horarios"),
                            situacao = pesq.getString("situacao");
                    float quantidade = pesq.getFloat("quantidade"),
                            latitude = pesq.getFloat("latitude"),
                            longitude = pesq.getFloat("longitude");
                    pontos.add(new Ponto(
                            usr,
                            tipo,
                            endereco,
                            quantidade,
                            dias,
                            horarios,
                            situacao,
                            latitude,
                            longitude
                    ));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return pontos;
    }

    public static ArrayList<Ponto> retornaPontos (String campo, String filto) {
        return selectPontos("PONTOS_FILTRO", campo, filto);
    }

    public static ArrayList<Ponto> retornaPontos () {
        return selectPontos("TODOS_PONTOS", null, null);
    }

    public static Usuario loginUsuario(String usr, char[] pass) {
        // Declara nova conexão
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        Usuario userRetorno = null;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("ResourceDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);

            // Executa query com base nos recursos
            ResultSet pesq = query.executeQuery(String.format(res.getString("LOGIN_STR"),
                    usr,
                    hashPass(pass)));
            if (pesq.next()) {
                userRetorno = new Usuario(pesq);
            }
        } catch (SQLException | MissingResourceException throwables) {
            // Confere erro de inserção / conexão
            throwables.printStackTrace();
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return userRetorno;
    }

    /**
     * Converte string de senha para BigInt por hashing MD5
     *
     * @param pass : Array de caracteres da senha textual
     * @return BigInt com hashing
     */
    public static BigInteger hashPass(char[] pass) {
        // Define digest para MD5
        MessageDigest md;

        // Lista de bytes para hashing
        byte[] bts = Arrays.toString(pass).getBytes(StandardCharsets.UTF_8);

        try {
            // Inicializa digest
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // Retorna caso erro de algoritmo
            e.printStackTrace();
            return BigInteger.valueOf(-1);
        }

        // Limpa digest
        md.reset();
        // Faz hash para array de bytes
        byte[] cripto = md.digest(bts);
        // Converte para BigInt para adição na DataBase
        return new BigInteger(1, cripto);
    }

    /**
     * Trata entrada de campos e insere no banco de dados com tais valores.
     *
     * @author Marco Toledo
     * @return boolean : Sucesso de inserção
     */
    public static boolean cadastroUsuario(String nome,
                                          String sobre,
                                          String usr,
                                          String email,
                                          BigInteger pass) {
        // Declara nova conexão
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        // Auxiliar de retorno
        boolean ret = true;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("ResourceDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);

            // Executa query com base nos recursos
            query.execute(String.format(res.getString("INSERT_STR"),
                    nome,
                    sobre,
                    usr,
                    email,
                    pass));
        } catch (SQLException | MissingResourceException throwables) {
            // Confere erro de inserção / conexão
            throwables.printStackTrace();
            ret = false;
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return ret;
    }

    public static boolean atualizaUsuario(String nome,
                                          String sobre,
                                          String usr,
                                          String email,
                                          BigInteger pass) {
        // Declara nova conexão
        Connection con = null;
        // Declara nova entrada de recursos
        ResourceBundle res = null;
        // Auxiliar de retorno
        boolean ret = true;
        try {
            // Carrega recursos
            res = ResourceBundle.getBundle("ResourceDB");

            // Conecta à database
            con = DriverManager.getConnection("jdbc:sqlite:./Data/database.sqlite");
            // Cria query SQL
            Statement query = con.createStatement();
            query.setQueryTimeout(10);

            // Executa query com base nos recursos
            query.execute(String.format(res.getString("UPDATE_STR"),
                    nome,
                    sobre,
                    email,
                    pass,
                    usr));
        } catch (SQLException | MissingResourceException throwables) {
            // Confere erro de inserção / conexão
            throwables.printStackTrace();
            ret = false;
        } finally {
            // Fecha conexão à database
            try {
                assert con != null;
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return ret;
    }
}
