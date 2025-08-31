package controller;

import model.Paciente;
import model.NivelTriage;
import java.util.*;

public class TriageManager {
    // Pilas para urgencia máxima
    private Stack<Paciente> urgenciaMaxima;
    // Colas para urgencias medias
    private Queue<Paciente> urgenciaMedia;
    // Lista para otros pacientes
    private List<Paciente> otrosPacientes;
    // Tabla hash para consulta rápida por ID
    private Map<Integer, Paciente> pacientesPorId;

    public TriageManager() {
        urgenciaMaxima = new Stack<>();
        urgenciaMedia = new LinkedList<>();
        otrosPacientes = new ArrayList<>();
        pacientesPorId = new HashMap<>();
    }

    public void agregarPaciente(Paciente paciente) {
        pacientesPorId.put(paciente.getId(), paciente);
        switch (paciente.getNivelTriage()) {
            case ROJO:
                urgenciaMaxima.push(paciente);
                break;
            case NARANJA:
            case AMARILLO:
                urgenciaMedia.add(paciente);
                break;
            case VERDE:
            case AZUL:
                otrosPacientes.add(paciente);
                break;
        }
    }

    public Paciente buscarPacientePorId(int id) {
        return pacientesPorId.get(id);
    }

    public void eliminarPaciente(int id) {
        Paciente paciente = pacientesPorId.remove(id);
        if (paciente != null) {
            urgenciaMaxima.remove(paciente);
            urgenciaMedia.remove(paciente);
            otrosPacientes.remove(paciente);
        }
    }

    public List<Paciente> obtenerPacientesPorUrgencia() {
        List<Paciente> todos = new ArrayList<>();
        todos.addAll(urgenciaMaxima);
        todos.addAll(urgenciaMedia);
        todos.addAll(otrosPacientes);
        return todos;
    }

    public List<Paciente> obtenerPacientesPorDepartamento(String departamento) {
        List<Paciente> resultado = new ArrayList<>();
        for (Paciente p : obtenerPacientesPorUrgencia()) {
            if (p.getDepartamento().equalsIgnoreCase(departamento)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public Paciente atenderPaciente() {
        if (!urgenciaMaxima.isEmpty()) {
            return urgenciaMaxima.pop();
        } else if (!urgenciaMedia.isEmpty()) {
            return urgenciaMedia.poll();
        } else if (!otrosPacientes.isEmpty()) {
            return otrosPacientes.remove(0);
        }
        return null;
    }
}
