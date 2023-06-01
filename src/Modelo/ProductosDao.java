package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ProductosDao {
    private Connection con;
    private Conexion cn = new Conexion();

    public boolean registrarProductos(Productos pro) {
        String sql = "INSERT INTO productos (codigo, nombre, proveedor, stock, precio) VALUES (?,?,?,?,?)";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pro.getCodigo());
                ps.setString(2, pro.getNombre());
                ps.setInt(3, pro.getProveedor());
                ps.setInt(4, pro.getStock());
                ps.setDouble(5, pro.getPrecio());
                ps.execute();
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public void rellenarComboProd(JComboBox<String> combo) {
        String sql = "SELECT * FROM productos";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                combo.removeAllItems();
                while (rs.next()) {
                    combo.addItem(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e.toString());
        }
    }

    public List<Productos> listarProductos() {
        List<Productos> listaProductos = new ArrayList<>();
        String sql = "SELECT pr.id AS id_proveedor, pr.nombre AS nombre_proveedor, p.* FROM proveedor pr INNER JOIN productos p ON pr.id = p.proveedor ORDER BY p.id DESC";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Productos pro = new Productos();
                    pro.setId(rs.getInt("id"));
                    pro.setCodigo(rs.getString("codigo"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setProveedor(rs.getInt("id_proveedor"));
                    pro.setProveedorPro(rs.getString("nombre_proveedor"));
                    pro.setStock(rs.getInt("stock"));
                    pro.setPrecio(rs.getDouble("precio"));
                    listaProductos.add(pro);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return listaProductos;
    }

    public boolean eliminarProductos(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.execute();
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public boolean modificarProductos(Productos pro) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, proveedor=?, stock=?, precio=? WHERE id=?";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, pro.getCodigo());
                ps.setString(2, pro.getNombre());
                ps.setInt(3, pro.getProveedor());
                ps.setInt(4, pro.getStock());
                ps.setDouble(5, pro.getPrecio());
                ps.setInt(6, pro.getId());
                ps.execute();
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public Productos buscarPro(String cod) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        llenarLista(producto, cod, sql);
        return producto;
    }

    public Productos buscarProNom(String nom) {
        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE nombre = ?";
        llenarLista(producto, nom, sql);
        return producto;
    }
    
    public void llenarLista(Productos producto, String nomProd, String sql){
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nomProd);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt("id"));
                        producto.setNombre(rs.getString("nombre"));
                        producto.setCodigo(rs.getString("codigo"));
                        producto.setPrecio(rs.getDouble("precio"));
                        producto.setStock(rs.getInt("stock"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public Productos buscarId(int id) {
        Productos pro = new Productos();
        String sql = "SELECT pr.id AS id_proveedor, pr.nombre AS nombre_proveedor, p.* FROM proveedor pr INNER JOIN productos p ON p.proveedor = pr.id WHERE p.id = ?";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        pro.setId(rs.getInt("id"));
                        pro.setCodigo(rs.getString("codigo"));
                        pro.setNombre(rs.getString("nombre"));
                        pro.setProveedor(rs.getInt("proveedor"));
                        pro.setProveedorPro(rs.getString("nombre_proveedor"));
                        pro.setStock(rs.getInt("stock"));
                        pro.setPrecio(rs.getDouble("precio"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return pro;
    }

    public Proveedor buscarProveedor(String nombre) {
        Proveedor pr = new Proveedor();
        String sql = "SELECT * FROM proveedor WHERE nombre = ?";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        pr.setId(rs.getInt("id"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return pr;
    }

    public Config buscarDatos() {
        Config conf = new Config();
        String sql = "SELECT * FROM config";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        conf.setId(rs.getInt("id"));
                        conf.setRuc(rs.getString("ruc"));
                        conf.setNombre(rs.getString("nombre"));
                        conf.setTelefono(rs.getString("telefono"));
                        conf.setDireccion(rs.getString("direccion"));
                        conf.setMensaje(rs.getString("mensaje"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return conf;
    }

    public boolean modificarDatos(Config conf) {
        String sql = "UPDATE config SET ruc=?, nombre=?, telefono=?, direccion=?, mensaje=? WHERE id=?";
        try {
            con = cn.getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, conf.getRuc());
                ps.setString(2, conf.getNombre());
                ps.setString(3, conf.getTelefono());
                ps.setString(4, conf.getDireccion());
                ps.setString(5, conf.getMensaje());
                ps.setInt(6, conf.getId());
                ps.execute();
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
}