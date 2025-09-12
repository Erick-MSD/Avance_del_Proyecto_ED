package model;

import java.time.LocalDateTime;

/** Representa una “tarea” pendiente de atención en ED. Enlaza al registro de BD. */
public class Task {
    private final int idRegistro;           // = registros.id_registro (clave para Hash)
    private final int pacienteId;           // = pacientes.id_paciente
    private final NivelTriage nivel;        // mapeado de triage.nivel
    private final String departamento;      // triage.area_atencion
    private final String descripcion;       // registros.observaciones
    private final LocalDateTime creada;     // registros.hora_asignacion
    private final String pacienteNombre;    // opcional para mostrar

    public Task(int idRegistro, int pacienteId, NivelTriage nivel,
                String departamento, String descripcion, LocalDateTime creada, String pacienteNombre) {
        this.idRegistro = idRegistro;
        this.pacienteId = pacienteId;
        this.nivel = nivel;
        this.departamento = departamento;
        this.descripcion = descripcion;
        this.creada = creada;
        this.pacienteNombre = pacienteNombre;
    }

    public int getIdRegistro() { return idRegistro; }
    public int getPacienteId() { return pacienteId; }
    public NivelTriage getNivel() { return nivel; }
    public String getDepartamento() { return departamento; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getCreada() { return creada; }
    public String getPacienteNombre() { return pacienteNombre; }

    @Override public String toString(){
        return String.format("#R%d Pac:%d (%s) [%s] Dep:%s - %s @%s",
            idRegistro, pacienteId, pacienteNombre, nivel.getColor(), departamento, descripcion, creada);
    }

    @Override public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return idRegistro == task.idRegistro;
    }
    @Override public int hashCode(){ return Integer.hashCode(idRegistro); }
}
