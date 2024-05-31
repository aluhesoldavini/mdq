package com.esteban.pagina.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Contacto")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto", nullable = false, updatable = false)
    private Long idContacto;

    @Column(name = "NumCelu", length = 31)
    private String numCelu;
    @Column(name = "info", length = 200)
    private String info;
    @Column(name = "Email", length = 51)
    private String email;
    @Column(name = "Direccion", length = 31)
    private String direccion;
    @Column(name = "Link1", length = 121)
    private String link1;
    @Column(name = "Link2", length = 121)
    private String link2;
    @Column(name = "Link3", length = 121)
    private String link3;

    public Contact() { }
    public Contact(Long idContacto, String numCelu, String info, String email, String direccion, String link1, String link2, String link3) {
        this.idContacto = idContacto;
        this.info = info;
        this.numCelu = numCelu;
        this.email = email;
        this.direccion = direccion;
        this.link1 = link1;
        this.link2 = link2;
        this.link3 = link3;
    }

    public Long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Long idContacto) {
        this.idContacto = idContacto;
    }

    public String getNumCelu() {
        return numCelu;
    }

    public void setNumCelu(String numCelu) {
        this.numCelu = numCelu;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Contact otherContact = (Contact) obj;

        if (numCelu != null ? !numCelu.equals(otherContact.numCelu) : otherContact.numCelu != null) {
            return false;
        }
        if (info != null ? !info.equals(otherContact.info) : otherContact.info != null) {
            return false;
        }
        if (email != null ? !email.equals(otherContact.email) : otherContact.email != null) {
            return false;
        }
        if (direccion != null ? !direccion.equals(otherContact.direccion) : otherContact.direccion != null) {
            return false;
        }
        if (link1 != null ? !link1.equals(otherContact.link1) : otherContact.link1 != null) {
            return false;
        }
        if (link2 != null ? !link2.equals(otherContact.link2) : otherContact.link2 != null) {
            return false;
        }
        return link3 != null ? link3.equals(otherContact.link3) : otherContact.link3 == null;
    }

    @Override
    public int hashCode() {
        int result = numCelu != null ? numCelu.hashCode() : 0;
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (link1 != null ? link1.hashCode() : 0);
        result = 31 * result + (link2 != null ? link2.hashCode() : 0);
        result = 31 * result + (link3 != null ? link3.hashCode() : 0);
        return result;
    }
}
