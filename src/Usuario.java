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
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private String email;
    private String usr;
    private String nome;
    private String sobrenome;
    private BigInteger passwordHash;

    /**
     * Inicializador da classe de Usuário, que salva as informações do banco de dados na memória
     *
     * @param rs : resultado de uma query SQL com os dados do usuário
     */
    public Usuario(ResultSet rs) throws SQLException {
        this.usr = rs.getString("usr");
        this.email = rs.getString("email");
        this.nome = rs.getString("nome");
        this.sobrenome = rs.getString("sobrenome");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public BigInteger getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = FuncoesDB.hashPass(password.toCharArray());
    }
}
