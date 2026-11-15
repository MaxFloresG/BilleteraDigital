
package com.billetera.servlet;

import com.billetera.model.Movimiento;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/billetera")
public class BilleteraApiServlet extends HttpServlet {

    private static final BigDecimal SALDO_INICIAL =
            new BigDecimal("250.00");

    private void configurarRespuesta(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private void inicializarSesion(HttpSession session) {
        synchronized (session) {
            if (session.getAttribute("saldo") == null) {
                session.setAttribute("saldo", SALDO_INICIAL);
            }

            if (session.getAttribute("movimientos") == null) {
                session.setAttribute(
                    "movimientos",
                    new ArrayList<Movimiento>()
                );
            }
        }
    }

    private JsonObject obtenerEstado(HttpSession session) {
        JsonObject resultado = new JsonObject();

        BigDecimal saldo =
                (BigDecimal) session.getAttribute("saldo");

        @SuppressWarnings("unchecked")
        List<Movimiento> movimientos =
                (List<Movimiento>) session.getAttribute("movimientos");

        BigDecimal recargas = BigDecimal.ZERO;
        BigDecimal gastos = BigDecimal.ZERO;

        JsonArray historial = new JsonArray();

        for (Movimiento movimiento : movimientos) {
            JsonObject item = new JsonObject();

            item.addProperty("tipo", movimiento.getTipo());
            item.addProperty("monto", movimiento.getMonto());
            item.addProperty(
                "fecha",
                movimiento.getFechaFormateada()
            );

            historial.add(item);

            if ("Recarga".equals(movimiento.getTipo())) {
                recargas = recargas.add(movimiento.getMonto());
            } else {
                gastos = gastos.add(movimiento.getMonto());
            }
        }

        resultado.addProperty("saldo", saldo);
        resultado.addProperty("totalRecargado", recargas);
        resultado.addProperty("totalGastado", gastos);
        resultado.add("movimientos", historial);

        return resultado;
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        configurarRespuesta(response);

        HttpSession session = request.getSession();

        synchronized (session) {
            inicializarSesion(session);

            response.getWriter().print(
                obtenerEstado(session).toString()
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        configurarRespuesta(response);
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        try {
            JsonObject datos = JsonParser.parseReader(
                request.getReader()
            ).getAsJsonObject();

            String tipo = datos.get("tipo").getAsString();

            if (!tipo.equals("Recarga") && !tipo.equals("Gasto")) {
                throw new IllegalArgumentException(
                    "Tipo de movimiento inválido."
                );
            }

            BigDecimal monto = datos.get("monto").getAsBigDecimal();

            if (monto.scale() > 2 ||
                monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                    "Ingrese un monto válido mayor que cero, " +
                    "con máximo dos decimales."
                );
            }

            monto = monto.setScale(2, RoundingMode.UNNECESSARY);

            synchronized (session) {
                inicializarSesion(session);

                BigDecimal saldo =
                    (BigDecimal) session.getAttribute("saldo");

                @SuppressWarnings("unchecked")
                List<Movimiento> movimientos =
                    (List<Movimiento>) session.getAttribute("movimientos");

                if ("Recarga".equals(tipo)) {
                    saldo = saldo.add(monto);
                } else {
                    if (monto.compareTo(saldo) > 0) {
                        throw new IllegalArgumentException(
                            "Saldo insuficiente."
                        );
                    }

                    saldo = saldo.subtract(monto);
                }

                movimientos.add(0, new Movimiento(tipo, monto));

                session.setAttribute("saldo", saldo);

                JsonObject resultado = obtenerEstado(session);
                resultado.addProperty(
                    "mensaje",
                    tipo + " registrada correctamente."
                );

                response.getWriter().print(resultado.toString());
            }

        } catch (IllegalArgumentException |
                 IllegalStateException |
                 NullPointerException ex) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            JsonObject error = new JsonObject();
            error.addProperty(
                "error",
                "Datos inválidos o movimiento no permitido."
            );

            response.getWriter().print(error.toString());
        }
    }
}
