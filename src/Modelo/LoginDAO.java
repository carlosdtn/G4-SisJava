
package Modelo;

import static Modelo.Utilidades.encriptarPassword;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    
    /*
        MODIFICACIÓN
        @Carlos Tarmeño
        - Se verifica que la contraseña del label y la contraseña coincidan con el hash almacenado.
        - Si coinciden entonces se establece la contraseña hasheada para la autenticación.
    */
    public login log(String correo, String pass){
        login l = new login();
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs= ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("pass");
                if (BCrypt.verifyer().verify(pass.toCharArray(), hashedPassword).verified) { // Verificar la contraseña encriptada
                    l.setId(rs.getInt("id"));
                    l.setNombre(rs.getString("nombre"));
                    l.setCorreo(rs.getString("correo"));
                    l.setPass(rs.getString("pass"));
                    l.setRol(rs.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return l;
    }
    /*
        MODIFICACIÓN
        @Carlos Tarmeño
        - Al realizar el registro se llama al método estático 'encriptarPassword' que recibe la contraseña y retorna una contraseña hasheada.
        - La contraseña hasheada se inserta en la BD.
    */
    public boolean Registrar(login reg){
        String sql = "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, reg.getNombre());
            ps.setString(2, reg.getCorreo());
            String hashedPassword = encriptarPassword(reg.getPass()); // Encriptar la contraseña
            ps.setString(3, hashedPassword);
            ps.setString(4, reg.getRol());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    public List ListarUsuarios(){
       List<login> Lista = new ArrayList();
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
       }
       return Lista;
   }
}
