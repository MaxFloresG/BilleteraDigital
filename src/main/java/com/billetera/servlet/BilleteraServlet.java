package com.billetera.servlet;

import com.billetera.model.Movimiento;
import jakarta.servlet.ServletException;
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

@WebServlet(name = "BilleteraServlet", urlPatterns = "/billetera")
public class BilleteraServlet extends HttpServlet {
    private static final String VISTA = "/WEB-INF/vistas/billetera.jsp";
    private static final BigDecimal SALDO_INICIAL = new BigDecimal("250.00");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        inicializarSesion(request.getSession());
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        inicializarSesion(session);

        String error = null;
        synchronized (session) {
            try {
                String tipo = request.getParameter("tipo");
                String montoTexto = request.getParameter("monto");

                if (tipo == null || (!tipo.equals("Recarga") && !tipo.equals("Gasto"))) {
                    throw new IllegalArgumentException("Seleccione un tipo de movimiento válido.");
                }
                if (montoTexto == null || montoTexto.isBlank()) {
                    throw new IllegalArgumentException("Ingrese el monto de la operación.");
                }

                BigDecimal monto;
                try {
                    monto = new BigDecimal(montoTexto.trim());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Ingrese un monto numérico válido.");
                }

                if (monto.scale() > 2) {
                    throw new IllegalArgumentException("El monto no puede tener más de dos decimales.");
                }
                if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("El monto debe ser mayor que cero.");
                }
                monto = monto.setScale(2, RoundingMode.UNNECESSARY);

                BigDecimal saldo = (BigDecimal) session.getAttribute("saldo");
                @SuppressWarnings("unchecked")
                List<Movimiento> movimientos = (List<Movimiento>) session.getAttribute("movimientos");

                if ("Recarga".equals(tipo)) {
                    saldo = saldo.add(monto);
                } else {
                    if (monto.compareTo(saldo) > 0) {
                        throw new IllegalArgumentException("Saldo insuficiente. Saldo disponible: S/ " + saldo.toPlainString());
                    }
                    saldo = saldo.subtract(monto);
                }

                movimientos.add(0, new Movimiento(tipo, monto));
                session.setAttribute("saldo", saldo);
            } catch (IllegalArgumentException ex) {
                error = ex.getMessage();
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/billetera");
    }

    private void inicializarSesion(HttpSession session) {
        synchronized (session) {
            if (session.getAttribute("saldo") == null) {
                session.setAttribute("saldo", SALDO_INICIAL);
            }
            if (session.getAttribute("movimientos") == null) {
                session.setAttribute("movimientos", new ArrayList<Movimiento>());
            }
        }
    }
}
