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

    /**
     * Cria um ponto de coleta no banco de dados
     *
     */
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

    /**
     * Retorna uma lista de pontos de coleta que satisfazem uma condição
     *
     * @param resStr : string com o comando SQL
     * @param campo : campo no qual deve ser feita a busca
     * @param filtro : valor do campo que deve ser buscado
     *
     * @return lista com os pontos de coleta
     */
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
            if (campo != null && filtro != null) {
                queryStr = String.format(queryStr,campo,filtro);
            }

            try (PreparedStatement ps = con.prepareStatement(queryStr)){
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

    /**
     * Retorna uma lista de pontos de coleta que satisfazem uma condição
     *
     * @param campo : campo no qual deve ser feita a busca
     * @param filtro : valor do campo que deve ser buscado
     *
     * @return lista com os pontos de coleta
     */
    public static ArrayList<Ponto> retornaPontos (String campo, String filtro) {
        return selectPontos("PONTOS_FILTRO", campo, filtro);
    }

    /**
     * Retorna uma lista de todos os pontos de coleta
     *
     * @return lista com os pontos de coleta
     */
    public static ArrayList<Ponto> retornaPontos () {
        return selectPontos("TODOS_PONTOS", null, null);
    }

    /**
     * Realiza o login e autenticação do usuário
     *
     * @param usr : nome de usuário
     * @param pass : senha
     *
     * @return Usuario que fez o login
     */
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

    /**
     * Atualiza as informações do usuário
     *
     */
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
