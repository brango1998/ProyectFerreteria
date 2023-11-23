/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carritodecompras_ferreteria;

/**
 *
 * @author neon
 */
public class nodo {

    public String nom, id, marca;
    public double precio;
    public int unidades;

    nodo sig, ant;

    public nodo(String nom, String id, String marca, double precio, int unidades) {
        this.nom = nom;
        this.id = id;
        this.marca = marca;
        this.precio = precio;
        this.unidades = unidades;
        sig = null;
        ant = null;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public nodo getSig() {
        return sig;
    }

    public void setSig(nodo sig) {
        this.sig = sig;
    }

    public nodo getAnt() {
        return ant;
    }

    public void setAnt(nodo ant) {
        this.ant = ant;
    }

}
