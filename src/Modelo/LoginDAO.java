
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
    Objetivo: mejorar la eficiencia de la método "log()" se propone.
    */
    public login log(String correo, String pass){
        /*
        La variable 'l' se inicializa en null para evitar la creación 
        innecesaria de una conexión que pueda consumir recursos del 
        sistema en caso de que la ejecución del método falle por algún motivo.
        login l = NULL;
        */
        login l = new login();
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND pass = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, pass);
            rs= ps.executeQuery();
            if (rs.next()) {
                /*
                Si existe un registro, recien se debe inicializar
                el objeto login.
                l = new login();
                */
                l.setId(rs.getInt("id"));
                l.setNombre(rs.getString("nombre"));
                l.setCorreo(rs.getString("correo"));
                l.setPass(rs.getString("pass"));
                l.setRol(rs.getString("rol"));
                
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        /*
        Se recomienda agregar una cláusula 'finally' al usar sentencias 
        try-catch. Esto garantiza que se ejecute siempre cierto código, 
        independientemente de si se lanzó una excepción o no, lo que puede 
        ser útil para liberar recursos o realizar otras tareas de limpieza.
        
        finally {
        
            try {
                if (rs != null) {
                    rs.close();
                }
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
        
        Es importante cerrar la conexión a la base de datos al finalizar 
        las acciones. De esta forma, se evita consumir recursos innecesarios 
        en el servidor y se garantiza una ejecución eficiente y segura 
        del programa
        */
        return l;
    }
    
    /*
    MODIFICACIÓN
    @Jorge Marin
    - Agregar sentencia "finally" como buena práctica de programación
    - Cerrar la conexión con la base de datos
    - Liberar la memoria de los objetos rs, ps.
    */
    public boolean Registrar(login reg){
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
        }
    }
    
    /*
    MODIFICACIÓN
    @Jorge Marin
    No es recomendable traer todas las tuplas de datos, consume recursos del
    sistema y relentiza el programa.
    
    Se propone:
    - Evitar listar todos los usuarios
    - Consultar solo por clave de usuario específico
    - Agregar sentencia "finally" como buena práctica de programación
    - Cerrar la conexión con la base de datos
    - Liberar la memoria de los objetos rs, ps.
     */
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
