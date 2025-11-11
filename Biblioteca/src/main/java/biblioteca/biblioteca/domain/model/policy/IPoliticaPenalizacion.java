package biblioteca.biblioteca.domain.model.policy;

import java.time.LocalDate;

public interface IPoliticaPenalizacion {
    int getMaxPrestamos();
    int getDiasPrestamo();
    int getPenalizacionPorDia();

    default int diasBloqueoPorAtraso(int diasAtraso) {
        return Math.max(0, diasAtraso) * getPenalizacionPorDia();
    }

    default LocalDate calcularVencimiento(LocalDate inicio) {
        return inicio.plusDays(getDiasPrestamo());
    }
}
