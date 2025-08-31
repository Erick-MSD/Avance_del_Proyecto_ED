package model;

import java.time.LocalDateTime;

public class Registro {
    private int id;
    private int pacienteId;
    private LocalDateTime fecha;
    private int triageId;

    // Constructor, getters y setters
    public Registro(int id, int pacienteId, LocalDateTime fecha, int triageId) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.fecha = fecha;
        this.triageId = triageId;
    }

    public int getId() { return id; }
    public int getPacienteId() { return pacienteId; }
    public LocalDateTime getFecha() { return fecha; }
    public int getTriageId() { return triageId; }

    public void setId(int id) { this.id = id; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setTriageId(int triageId) { this.triageId = triageId; }
}
