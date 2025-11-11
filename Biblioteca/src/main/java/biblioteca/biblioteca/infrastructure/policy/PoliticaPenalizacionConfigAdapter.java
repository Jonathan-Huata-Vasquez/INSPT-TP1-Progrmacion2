package biblioteca.biblioteca.infrastructure.policy;

import biblioteca.biblioteca.domain.model.policy.IPoliticaPenalizacion;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "circulacion.politica")
@Getter
@ToString
public class PoliticaPenalizacionConfigAdapter implements IPoliticaPenalizacion {

    /** Lombok generará getMaxPrestamos(), getDiasPrestamo(), getPenalizacionPorDia() */
    private int maxPrestamos = 5;
    private int diasPrestamo = 21;
    private int penalizacionPorDia = 2;

    // setters para binding de Spring Boot
    public void setMaxPrestamos(int v) { this.maxPrestamos = v; }
    public void setDiasPrestamo(int v) { this.diasPrestamo = v; }
    public void setPenalizacionPorDia(int v) { this.penalizacionPorDia = v; }
}
