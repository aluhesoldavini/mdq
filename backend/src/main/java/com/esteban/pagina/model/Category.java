package com.esteban.pagina.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Categoria")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria", nullable = false, updatable = false)
    private Long idCategoria;

    @Column(name = "Nombre", nullable = false, unique = true, length = 16)
    private String nombre;
    @Column(name = "Mayorista", nullable = false)
    private boolean mayorista;
    @Column(name = "Minorista", nullable = false)
    private boolean minorista;

    public Category() { }
    public Category(String nombre, boolean mayorista, boolean minorista) {
        this.nombre = nombre;
        this.mayorista = mayorista;
        this.minorista = minorista;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isMayorista() {
        return mayorista;
    }

    public void setMayorista(boolean mayorista) {
        this.mayorista = mayorista;
    }

    public boolean isMinorista() {
        return minorista;
    }

    public void setMinorista(boolean minorista) {
        this.minorista = minorista;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Category otherCategory = (Category) obj;

        if (nombre != null ? !nombre.equalsIgnoreCase(otherCategory.nombre) : otherCategory.nombre != null) {
            return false;
        }

        if (mayorista != otherCategory.mayorista) {
            return false;
        }

        return minorista == otherCategory.minorista;
    }

    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.toLowerCase().hashCode() : 0;
        result = 31 * result + (mayorista ? 1 : 0);
        result = 31 * result + (minorista ? 1 : 0);
        return result;
    }
}
