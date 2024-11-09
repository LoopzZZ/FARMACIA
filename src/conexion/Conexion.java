/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    //cree un objeto de la clase conexion
    private Connection conexion;

    /**
     * este metodo realiza la conexion a la base de datos
     */
    public void conectar() {

        String url = " "; /* pendiente de ensalzar base de datos */
        String usuario = "root"; 
        String contraseña = ""; //agregar contraseña si se solicita

        try {
            //se establece la conexion
            conexion = DriverManager.getConnection(url, usuario, contraseña);
          
        } catch (SQLException e) {
            // se lanza una excepcion por algun error
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    //metodo para obtener la conexion
    public Connection getConexion() {
        return conexion;  //retornamos la conexion
    }

    // metodo para cerrar la conexion
    public void desconectar() {
        try {
            // se valida si hay alguna conexion ha cerrar
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
           // se lanza una excepcion por algun error
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    
}
