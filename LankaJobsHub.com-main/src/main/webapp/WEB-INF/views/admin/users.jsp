<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Users - LankaJobsHub</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/svg+xml" href="/lankajobshub/static/images/favicon.svg">
    <link rel="apple-touch-icon" href="/lankajobshub/static/images/lankajobshub-logo.svg">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="/lankajobshub/static/css/lankajobshub-ui.css">
</head>
<body class="management-shell">
    <nav class="sticky top-0 z-50 bg-white/95 backdrop-blur border-b border-slate-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-20">
                <a href="/lankajobshub/" class="flex items-center gap-3 focus-ring rounded-lg">
                    <img src="/lankajobshub/static/images/lankajobshub-logo.svg" alt="LankaJobsHub logo" class="h-11 w-11 rounded-xl shadow-lg shadow-blue-200">
                    <span class="hidden sm:block text-xl font-bold text-slate-950">LankaJobsHub</span>
                </a>
                <div class="flex items-center gap-3">
                    <a href="/lankajobshub/dashboard" class="hidden sm:inline-flex px-4 py-2 rounded-lg text-slate-700 hover:text-blue-600 font-medium focus-ring">Dashboard</a>
                    <a href="/lankajobshub/analytics/dashboard" class="hidden sm:inline-flex px-4 py-2 rounded-lg text-slate-700 hover:text-blue-600 font-medium focus-ring">Analytics</a>
                    <a href="/lankajobshub/users/logout" class="ui-btn-secondary focus-ring px-4 py-2">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-10">
        <div class="workspace-nav mb-8">
            <a href="/lankajobshub/dashboard" class="workspace-nav-link focus-ring">Dashboard</a>
            <a href="/lankajobshub/users/admin" class="workspace-nav-link active focus-ring">Users</a>
            <a href="/lankajobshub/analytics/dashboard" class="workspace-nav-link focus-ring">Analytics</a>
            <a href="/lankajobshub/users/create" class="workspace-nav-link focus-ring">Create User</a>
        </div>

        <div class="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-4 mb-8">
            <div>
                <p class="text-blue-700 font-semibold">Admin workspace</p>
                <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">User Management</h1>
                <p class="mt-2 text-slate-500">Create, review, and remove platform users.</p>
            </div>
            <a href="/lankajobshub/users/create" class="ui-btn-primary focus-ring px-6 py-3">Create User</a>
        </div>

        <c:if test="${not empty success}">
            <div class="ui-alert ui-alert-success mb-6">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty users}">
                <div class="ui-card rounded-3xl bg-white p-10 text-center">
                    <h2 class="text-2xl font-bold text-slate-950">No users found</h2>
                    <p class="mt-3 text-slate-500">Created users will appear here.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid gap-4">
                    <c:forEach var="u" items="${users}">
                        <article class="ui-soft-card rounded-3xl bg-white p-6">
                            <div class="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-5">
                                <div class="min-w-0">
                                    <div class="flex flex-wrap gap-2 mb-3">
                                        <span class="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">${u.role.displayName}</span>
                                        <span class="rounded-full ${u.status == 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-slate-100 text-slate-700'} px-3 py-1 text-xs font-semibold">${u.status}</span>
                                    </div>
                                    <h2 class="text-xl font-bold text-slate-950"><c:choose><c:when test="${not empty u.fullName}">${u.fullName}</c:when><c:otherwise>${u.email}</c:otherwise></c:choose></h2>
                                    <p class="mt-1 text-slate-500 break-all">${u.email}</p>
                                    <p class="mt-2 text-sm text-slate-400">ID: ${u.id}<c:if test="${u.createdDate != null}"> • Joined ${u.createdDate}</c:if></p>
                                </div>
                                <form action="/lankajobshub/users/admin/${u.id}/delete" method="post" onsubmit="return confirm('Delete this user?');">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button type="submit" class="rounded-xl border border-red-200 px-5 py-3 font-semibold text-red-700 hover:bg-red-50 focus-ring">Delete</button>
                                </form>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>
