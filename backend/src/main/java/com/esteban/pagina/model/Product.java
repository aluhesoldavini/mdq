package com.esteban.pagina.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Producto")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false, updatable = false)
    private Long idProducto;

    @ManyToOne
    @JoinColumn(name = "IdCategoria", nullable = false)
    private Category category;

    @Column(name = "Nombre", nullable = false, unique = true, length = 31)
    private String nombre;
    @Column(name = "Stock", nullable = false)
    private boolean stock;
    @Column(name = "Imagenes", length = 501)
    private String imagenes;
    @Column(name = "Minorista", nullable = false)
    private boolean minorista;
    @Column(name = "Mayorista", nullable = false)
    private boolean mayorista;
    @Column(name = "Descripcion", nullable = false, length = 2001)
    private String descripcion;
    @Column(name = "Recomendado", nullable = false)
    private boolean recomendado;

    public Product() { }
    public Product(Category category, String nombre, boolean stock, String imagenes, boolean minorista, boolean mayorista, String descripcion, boolean recomendado) {
        this.category = category;
        this.nombre = nombre;
        this.stock = stock;
        this.imagenes = imagenes;
        this.minorista = minorista;
        this.mayorista = mayorista;
        this.descripcion = descripcion;
        this.recomendado = recomendado;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }

    public String getImagenes() {
        return imagenes;
    }

    public void setImagenes(String imagenes) {
        this.imagenes = imagenes;
    }

    public boolean isMinorista() {
        return minorista;
    }

    public void setMinorista(boolean minorista) {
        this.minorista = minorista;
    }

    public boolean isMayorista() {
        return mayorista;
    }

    public void setMayorista(boolean mayorista) {
        this.mayorista = mayorista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public void setRecomendado(boolean recomendado) {
        this.recomendado = recomendado;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Product otherProduct = (Product) obj;

        boolean categoriesEqual = category != null ? category.equals(otherProduct.category) : otherProduct.category == null;
        boolean namesEqual = nombre != null ? nombre.equals(otherProduct.nombre) : otherProduct.nombre == null;
        boolean stockEqual = stock == otherProduct.stock;
        boolean imagesEqual = imagenes != null ? imagenes.equals(otherProduct.imagenes) : otherProduct.imagenes == null;
        boolean minoristaEqual = minorista == otherProduct.minorista;
        boolean mayoristaEqual = mayorista == otherProduct.mayorista;
        boolean descriptionEqual = descripcion != null ? descripcion.equals(otherProduct.descripcion) : otherProduct.descripcion == null;
        boolean recommendedEqual = recomendado == otherProduct.recomendado;

        return categoriesEqual && namesEqual && stockEqual && imagesEqual && minoristaEqual && mayoristaEqual &&
                descriptionEqual && recommendedEqual;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (stock ? 1 : 0);
        result = 31 * result + (imagenes != null ? imagenes.hashCode() : 0);
        result = 31 * result + (minorista ? 1 : 0);
        result = 31 * result + (mayorista ? 1 : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (recomendado ? 1 : 0);
        return result;
    }
}