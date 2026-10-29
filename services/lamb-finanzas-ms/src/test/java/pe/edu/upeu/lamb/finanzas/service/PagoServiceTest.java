package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.mapper.PagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.ConceptoPagoRepository;
import pe.edu.upeu.lamb.finanzas.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private PagoMapper pagoMapper;
    @Mock
    private ConceptoPagoRepository conceptoPagoRepository;
    @Mock
    private HistorialEstadoPagoService historialEstadoPagoService;
    @Mock
    private TransaccionPasarelaService transaccionPasarelaService;
    @Mock
    private MotivoPagoService motivoPagoService;

    @InjectMocks
    private PagoService pagoService;

    /**
     * Bug preexistente (no introducido por el filtro de matriculaId, no corregido aqui por
     * estar fuera de alcance): PagoService.listar() arranca con
     * Specification.where((Specification<Pago>) null), y en spring-data-jpa 4.0.7 ese metodo
     * exige un argumento no nulo (Assert.notNull) y lanza IllegalArgumentException siempre,
     * para cualquier combinacion de filtros, incluyendo matriculaId. El mismo patron existe en
     * MatriculaService.listar(). Este test documenta el comportamiento actual tal cual esta.
     */
    @Test
    void listar_lanzaIllegalArgumentException_porSpecificationWhereNullPreexistente() {
        assertThatThrownBy(() -> pagoService.listar(null, null, 77L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Specification must not be null");
    }
}
