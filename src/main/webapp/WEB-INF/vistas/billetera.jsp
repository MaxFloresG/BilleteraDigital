<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mi Billetera Digital</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f3f6fa; }
        .navbar { background: #17263b; }
        .saldo { color: #12865c; font-size: 2.2rem; font-weight: 700; }
        .card { border: 0; border-radius: 14px; }
        .table thead { background: #17263b; color: white; }
    </style>
</head>
<body>
<nav class="navbar navbar-dark py-3">
    <div class="container"><span class="navbar-brand fw-bold">Mi Billetera Digital</span><span class="badge bg-secondary">Servlet + JSP</span></div>
</nav>
<main class="container py-4">
    <div class="row g-4">
        <div class="col-md-5">
            <section class="card shadow-sm p-4">
                <h6 class="text-secondary">SALDO DISPONIBLE</h6>
                <div class="saldo">S/ <fmt:formatNumber value="${sessionScope.saldo}" minFractionDigits="2" maxFractionDigits="2"/></div>
                <small class="text-secondary">Movimientos registrados: ${sessionScope.movimientos.size()}</small>
            </section>
        </div>
        <div class="col-md-7">
            <section class="card shadow-sm p-4 h-100">
                <h5>Bienvenido a tu billetera</h5>
                <p class="text-secondary mb-0">Registra recargas y gastos. El saldo y el historial se conservan durante la sesión HTTP.</p>
            </section>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-4" role="alert"><c:out value="${error}"/></div>
    </c:if>

    <div class="row g-4 mt-1">
        <div class="col-lg-8">
            <section class="card shadow-sm p-3">
                <h5 class="mb-3">Historial de movimientos</h5>
                <div class="table-responsive">
                    <table class="table align-middle">
                        <thead><tr><th>Tipo</th><th>Monto (S/)</th><th>Fecha</th></tr></thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty sessionScope.movimientos}">
                                <tr><td colspan="3" class="text-center text-secondary py-4">Aún no tienes movimientos registrados.</td></tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="movimiento" items="${sessionScope.movimientos}">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${movimiento.tipo eq 'Recarga'}"><span class="badge bg-success">Recarga</span></c:when>
                                                <c:otherwise><span class="badge bg-danger">Gasto</span></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="fw-bold ${movimiento.tipo eq 'Recarga' ? 'text-success' : 'text-danger'}">
                                            <c:choose><c:when test="${movimiento.tipo eq 'Recarga'}">+</c:when><c:otherwise>-</c:otherwise></c:choose>
                                            <fmt:formatNumber value="${movimiento.monto}" minFractionDigits="2" maxFractionDigits="2"/>
                                        </td>
                                        <td><c:out value="${movimiento.fechaFormateada}"/></td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
        <div class="col-lg-4">
            <section class="card shadow-sm p-4">
                <h5>Nuevo movimiento</h5>
                <form action="${pageContext.request.contextPath}/billetera" method="post">
                    <div class="mb-3">
                        <label for="tipo" class="form-label">Tipo de movimiento</label>
                        <select id="tipo" name="tipo" class="form-select" required>
                            <option value="Recarga">Recarga</option>
                            <option value="Gasto">Gasto</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="monto" class="form-label">Monto (S/)</label>
                        <input id="monto" name="monto" type="number" class="form-control" min="0.01" step="0.01" placeholder="0.00" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Registrar movimiento</button>
                </form>
            </section>
        </div>
    </div>
</main>
</body>
</html>
