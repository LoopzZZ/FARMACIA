/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proveedor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import conexion.Conexion;
import java.sql.ResultSet;

public class AgregarProveedor {

    private String nombres;
    private String apellidos;
    private String direccion;
    private String correo;

    public AgregarProveedor(String nombres, String apellidos, String direccion, String correo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.correo = correo;
    }

    //metodo usado para validar que las variables no esten vacias
    private boolean validar_campos() {
        return (this.nombres == null && this.nombres.trim().isEmpty())
                && (this.apellidos == null && this.apellidos.trim().isEmpty())
                && (this.direccion == null && this.direccion.trim().isEmpty())
                && (this.correo == null && this.correo.trim().isEmpty());
    }

    //metodo usado para validar si existe un proveedor con ese nombre y apellidos
    private boolean validar_existencia() {
        Conexion objetoConexion = new Conexion();
        objetoConexion.conectar();
        Connection conexion = objetoConexion.getConexion();
        boolean existe = false;
        try {

            //se utiliza consultas preparadas
            //se declara la consulta a hacer al servidor
            String query = "SELECT count(*) FROM proveedor WHERE nombres = ? AND apellidos = ?";

            //se prepara la consulta
            PreparedStatement stmt = conexion.prepareStatement(query);

            //se agrega los valores que vamos a enviar a consultar
            stmt.setString(1, this.nombres);
            stmt.setString(2, this.apellidos);

            //se ejecuta consulta
            ResultSet resultado = stmt.executeQuery();

            //se valida si se obtuvo registros
            if (resultado.next()) {

                //se valida si el registro es mayor a 1, es por que ya existe (devuelve un booleano)
                existe = resultado.getInt(1) > 0;
            }
            resultado.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            objetoConexion.desconectar();
        }
        return existe;
    }

    //metodo usado para de registrar en la base de datos
    private String agregar_proveedor() throws SQLException {

        Conexion conexionBD = new Conexion();
        conexionBD.conectar();
        Connection conexion = conexionBD.getConexion();
        /**
         * ***************************************************************
         * UTILIZAMOS TRANSACCIONES PARA HACER ROOLLBACK POR ALGUN ERROR *
         * *************************************************************
         */
      
        try {
            conexion.setAutoCommit(false);

            String insert_proveedor = "INSERT INTO proveedor (nombres, apellidos, direccion, correo) VALUES (?, ?, ?, ?)";
            PreparedStatement ejecutar = conexion.prepareStatement(insert_proveedor);
            ejecutar.setString(1, this.nombres);
            ejecutar.setString(2, this.apellidos);
            ejecutar.setString(3, this.direccion);
            ejecutar.setString(4, this.correo);
            ejecutar.executeUpdate();
            conexion.commit();

            return "Proveedor Agregado";
        } catch (SQLException e) {
            conexion.rollback();
            return "Error Inesperado " + e.getMessage();
        } finally {
            conexion.setAutoCommit(true);
            conexionBD.desconectar();
        }
    }

    public String registrar_proveedor() throws SQLException {
        System.out.println(this.validar_campos());
        if (this.validar_campos()) {
            return "Haz dejado datos vacios";
        }

        if (this.validar_existencia()) {
            return "Ya existe un proveedor con este nombre y apellido";
        }

        return this.agregar_proveedor();
    }

}
