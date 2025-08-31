package model;

public class ReglaTriage {
    private int id;
    private String descripcion;
    private int nivelUrgencia;

    // Constructor, getters y setters
    public ReglaTriage(int id, String descripcion, int nivelUrgencia) {
        this.id = id;
        this.descripcion = descripcion;
        this.nivelUrgencia = nivelUrgencia;
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public int getNivelUrgencia() { return nivelUrgencia; }

    public void setId(int id) { this.id = id; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setNivelUrgencia(int nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
}
