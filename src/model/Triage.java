package model;

public class Triage {
    private int id;
    private int pacienteId;
    private int nivelUrgencia;
    private String color;

    // Constructor, getters y setters
    public Triage(int id, int pacienteId, int nivelUrgencia, String color) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.nivelUrgencia = nivelUrgencia;
        this.color = color;
    }

    public int getId() { return id; }
    public int getPacienteId() { return pacienteId; }
    public int getNivelUrgencia() { return nivelUrgencia; }
    public String getColor() { return color; }

    public void setId(int id) { this.id = id; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public void setNivelUrgencia(int nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
    public void setColor(String color) { this.color = color; }
}
