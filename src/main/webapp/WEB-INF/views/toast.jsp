<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.toastError}">
    <div class="toast toast-error">
            ${sessionScope.toastError}
    </div>
    <c:remove var="toastError" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.toastWarning}">
    <div class="toast toast-warning">
            ${sessionScope.toastWarning}
    </div>
    <c:remove var="toastWarning" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.toastSuccess}">
    <div class="toast toast-success">
            ${sessionScope.toastSuccess}
    </div>
    <c:remove var="toastSuccess" scope="session"/>
</c:if>
