package com.hotel.Hotel.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReglasDominioTest {

    @Test
    @DisplayName("Calcula el costo de una reserva según noches y precio de habitación")
    void calculaCostoTotalDeReserva() {
        Cliente cliente = new Cliente("Ana Pérez", "ana@example.com");
        Habitacion habitacion = new HabitacionEstandar("301", 2, 150.0, 1);
        RangoFechas periodo = new RangoFechas(
                LocalDateTime.of(2026, 11, 1, 14, 0),
                LocalDateTime.of(2026, 11, 4, 14, 0));

        Reserva reserva = new Reserva(cliente, habitacion, periodo);

        assertEquals(450.0, reserva.getCostoTotal());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
    }

    @Test
    @DisplayName("Rechaza un rango cuya fecha final es anterior a la inicial")
    void rechazaRangoDeFechasInvertido() {
        LocalDateTime inicio = LocalDateTime.of(2026, 11, 5, 12, 0);
        LocalDateTime fin = inicio.minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> new RangoFechas(inicio, fin));
    }

    @Test
    @DisplayName("Desactiva al cliente al registrar su tercera penalización")
    void desactivaClienteTrasTresPenalizaciones() {
        Cliente cliente = new Cliente("Luis Díaz", "luis@example.com");

        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();
        assertTrue(cliente.puedeRealizarReservas());
        cliente.registrarPenalizacion();

        assertFalse(cliente.puedeRealizarReservas());
        assertEquals(3, cliente.getPenalizaciones());
    }

    @Test
    @DisplayName("Reactiva al cliente y reinicia sus penalizaciones")
    void reactivarClienteReiniciaPenalizaciones() {
        Cliente cliente = new Cliente("Marta Ruiz", "marta@example.com");
        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();
        cliente.registrarPenalizacion();

        cliente.reactivar();

        assertTrue(cliente.puedeRealizarReservas());
        assertEquals(0, cliente.getPenalizaciones());
    }

    @Test
    @DisplayName("Impide asignar a una reserva una habitación que ya está ocupada")
    void noPermiteOcuparDosVecesLaHabitacion() {
        Habitacion habitacion = new HabitacionEstandar("302", 2, 120.0, 1);
        habitacion.asignarAReserva();

        assertEquals(EstadoHabitacion.OCUPADA, habitacion.getEstado());
        assertThrows(IllegalStateException.class, habitacion::asignarAReserva);
    }
}
