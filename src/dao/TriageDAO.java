package dao;

import model.NivelTriage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TriageDAO {
    private final DBConnection db;

    public TriageDAO(DBConnection db){ this.db = db; }

    public record PendienteDTO(
            int idRegistro,
            int pacienteId,
            String pacienteNombre,
            NivelTriage nivel,
            String departamento,
            String descripcion,
            LocalDateTime creada
    ) {}

    public List<PendienteDTO> listarPendientes() throws Exception {
        String sql = """
            SELECT r.id_registro,
                   p.id_paciente,
                   p.nombre,
                   t.nivel,
                   t.area_atencion,
                   COALESCE(r.observaciones,'') AS observaciones,
                   r.hora_asignacion
            FROM registros r
            JOIN pacientes p ON p.id_paciente = r.id_paciente
            JOIN triage   t ON t.id_triage   = r.id_triage
            ORDER BY t.nivel ASC, r.hora_asignacion ASC
            """;
        try (Connection con = db.get();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<PendienteDTO> out = new ArrayList<>();
            while (rs.next()){
                out.add(new PendienteDTO(
                        rs.getInt("id_registro"),
                        rs.getInt("id_paciente"),
                        rs.getString("nombre"),
                        NivelTriage.fromNivel(rs.getInt("nivel")),
                        rs.getString("area_atencion"),
                        rs.getString("observaciones"),
                        rs.getTimestamp("hora_asignacion").toLocalDateTime()
                ));
            }
            return out;
        }
    }

    public void asignarPorSintoma(int idPaciente, String sintoma) throws Exception {
        try (Connection con = db.get();
             PreparedStatement ps = con.prepareStatement("SELECT asignar_triage_por_sintoma(?, ?)")) {
            ps.setInt(1, idPaciente);
            ps.setString(2, sintoma);
            ps.execute();
        }
    }
}
