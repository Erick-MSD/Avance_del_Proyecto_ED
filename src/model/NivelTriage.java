package model;

/** Mapea el nivel (1..5) y color de la BD. */
public enum NivelTriage {
    ROJO(1, "Rojo"),
    NARANJA(2, "Naranja"),
    AMARILLO(3, "Amarillo"),
    VERDE(4, "Verde"),
    AZUL(5, "Azul");

    private final int nivel;
    private final String color;

    NivelTriage(int nivel, String color) { this.nivel = nivel; this.color = color; }

    public int getNivel() { return nivel; }
    public String getColor() { return color; }

    public static NivelTriage fromNivel(int n) {
        return switch (n) {
            case 1 -> ROJO;
            case 2 -> NARANJA;
            case 3 -> AMARILLO;
            case 4 -> VERDE;
            case 5 -> AZUL;
            default -> throw new IllegalArgumentException("Nivel triage inválido: " + n);
        };
    }

    public static NivelTriage fromColor(String c) {
        return switch (c.toLowerCase()) {
            case "rojo" -> ROJO;
            case "naranja" -> NARANJA;
            case "amarillo" -> AMARILLO;
            case "verde" -> VERDE;
            case "azul" -> AZUL;
            default -> throw new IllegalArgumentException("Color triage inválido: " + c);
        };
    }
}
