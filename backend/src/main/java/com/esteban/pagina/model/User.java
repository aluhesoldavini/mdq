package com.esteban.pagina.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false, updatable = false)
    private Long idUsuario;
    @Column(name = "Nombre", unique = true, nullable = false, length = 31)
    private String nombre;
    @Column(name = "Contacto", length = 51)
    private String contacto;
    @Column(name = "Contrasena", nullable = false, length = 254)
    private String contrasena;
    @Column(name = "Tipo", nullable = false)
    private int tipo;

    public User() { }
    public User(Long idUsuario, String nombre, String contacto, String contrasena, int tipo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contacto = contacto;
        this.contrasena = contrasena;
        this.tipo = tipo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User otherUser = (User) obj;

        boolean idEqual = idUsuario != null ? idUsuario.equals(otherUser.idUsuario) : otherUser.idUsuario == null;
        boolean nameEqual = nombre != null ? nombre.equalsIgnoreCase(otherUser.nombre) : otherUser.nombre == null;
        boolean contactEqual = contacto != null ? contacto.equals(otherUser.contacto) : otherUser.contacto == null;
        boolean passwordEqual = contrasena != null ? contrasena.equals(otherUser.contrasena) : otherUser.contrasena == null;
        boolean typeEqual = tipo == otherUser.tipo;

        return idEqual && nameEqual && contactEqual && passwordEqual && typeEqual;
    }

    @Override
    public int hashCode() {
        int result = idUsuario != null ? idUsuario.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.toLowerCase().hashCode() : 0);
        result = 31 * result + (contacto != null ? contacto.hashCode() : 0);
        result = 31 * result + (contrasena != null ? contrasena.hashCode() : 0);
        result = 31 * result + tipo;
        return result;
    }
}
