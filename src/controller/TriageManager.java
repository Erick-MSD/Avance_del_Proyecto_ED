package controller;

import dao.TriageDAO;
import dao.DBConnection;
import model.*;
import model.repository.TaskRepositoryInMemory;

// importa SOLO lo que necesitas de util:
import java.util.List;

public class TriageManager {

    private final TriageDAO triageDAO;   // <-- FALTA ESTA LÍNEA

    // ...
    private final TaskRepositoryInMemory repo = new TaskRepositoryInMemory();

    public TriageManager(DBConnection db){ this.triageDAO = new TriageDAO(db); }

    // ===== Carga desde BD a ED =====
    public int cargarPendientesDesdeBD() throws Exception {
        var lista = triageDAO.listarPendientes();
        int count = 0;
        for (var p : lista){
            Task t = new Task(
                    p.idRegistro(), p.pacienteId(), p.nivel(),
                    p.departamento(), p.descripcion(), p.creada(),
                    p.pacienteNombre()
            );
            repo.addTask(t);
            count++;
        }
        return count;
    }

    // ===== Altas nuevas usando tu función PL/pgSQL =====
    public void registrarPorSintomaEnBD(int idPaciente, String sintoma) throws Exception {
        triageDAO.asignarPorSintoma(idPaciente, sintoma);
    }

    // ===== Operaciones ED =====
    public Task atenderRojo(){ return repo.attend(NivelTriage.ROJO); }
    public Task atenderNaranja(){ return repo.attend(NivelTriage.NARANJA); }
    public Task atenderAmarillo(){ return repo.attend(NivelTriage.AMARILLO); }
    public Task atenderVerde(){ return repo.attend(NivelTriage.VERDE); }
    public Task atenderAzul(){ return repo.attend(NivelTriage.AZUL); }

    // Búsquedas/listado
    public Task buscarPorRegistro(int idRegistro){ return repo.findByRegistro(idRegistro); }
    public List<Task> buscarPorPaciente(int idPaciente){ return repo.findByPaciente(idPaciente); }
    public Task primeraPorDepartamento(String dpto){ return repo.firstByDepartment(dpto); }
    public boolean eliminarPrimeraPorDepartamento(String dpto){ return repo.removeFirstByDepartment(dpto) != null; }

    /** Vista ordenada por urgencia (nivel asc) y luego por depto y fecha. */
    public String verPendientesOrdenados(){
    var all = repo.getAllOrdered();
    var sb = new StringBuilder();
    all.forEach(t -> sb.append(t).append("\n"));
    return sb.toString();
    }

    /** Devuelve lista ordenada (copia inmutable) para la interfaz. */
    public java.util.List<Task> getPendientesOrdenados(){
        return java.util.List.copyOf(repo.getAllOrdered());
    }
}
