package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    /*
    MODIFICACIÓN
    @JorgeMarin
    - Agregar sentencia "finally" como buena práctica de programación
    - Cerrar la conexión con la base de datos
    - Liberar la memoria de los objetos rs, ps
    - Utiliza constructor de clase login
     */
    public login log(String correo, String pass) {
        login l = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND pass = ?";
        try ( Connection con = cn.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, pass);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    l = new login(rs.getInt("id"), rs.getString("nombre"),
                            rs.getString("correo"), rs.getString("pass"),
                            rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return l;
    }

    /*
    MODIFICACIÓN
    @Jorge Marin
    - Agregar sentencia "finally" como buena práctica de programación
    - Cerrar la conexión con la base de datos
    - Liberar la memoria de los objetos rs, ps.
     */
    public boolean Registrar(login reg) {
        String sql = "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, reg.getNombre());
            ps.setString(2, reg.getCorreo());
            ps.setString(3, reg.getPass());
            ps.setString(4, reg.getRol());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close(); // cerrar la conexión
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }


    /*
    MODIFICACIÓN
    @Jorge Marin
    - Agregar sentencia "finally" como buena práctica de programación
    - Cerrar la conexión con la base de datos
    - Liberar la memoria de los objetos rs, ps.
     */
    public List<login> ListarUsuarios() {
        List<login> Lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                login lg = new login();
                lg.setId(rs.getInt("id"));
                lg.setNombre(rs.getString("nombre"));
                lg.setCorreo(rs.getString("correo"));
                lg.setRol(rs.getString("rol"));
                Lista.add(lg);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return Lista;
    }

}
