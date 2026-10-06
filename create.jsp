<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create User Account - LankaJobsHub</title>
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
                    <a href="/lankajobshub/users/logout" class="ui-btn-secondary focus-ring px-4 py-2">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-10">
        <div class="workspace-nav mb-8">
            <a href="/lankajobshub/dashboard" class="workspace-nav-link focus-ring">Dashboard</a>
            <a href="/lankajobshub/users/create" class="workspace-nav-link active focus-ring">Create User</a>
            <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
        </div>

        <div class="mb-8">
            <p class="text-blue-700 font-semibold">Account administration</p>
            <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">Create User Account</h1>
            <p class="mt-2 text-slate-500">As a ${currentUser.role.displayName}, create only the roles currently permitted for your account.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="ui-alert ui-alert-success mb-6">${success}</div>
        </c:if>

        <div class="grid lg:grid-cols-3 gap-6">
            <section class="lg:col-span-2 ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Account Details</h2>
                <form action="/lankajobshub/users/create" method="POST" class="space-y-6">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <div class="grid md:grid-cols-2 gap-5">
                        <div>
                            <label for="email" class="field-label">Email Address *</label>
                            <input id="email" name="email" type="email" required autocomplete="email" class="ui-input focus-ring" placeholder="user@company.com" value="${user.email}">
                        </div>
                        <div>
                            <label for="role" class="field-label">Role *</label>
                            <select id="role" name="role" required class="ui-input focus-ring">
                                <option value="">Select a role</option>
                                <c:forEach items="${creatableRoles}" var="role">
                                    <option value="${role}" ${user.role == role ? 'selected' : ''}>${role.displayName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label for="password" class="field-label">Password *</label>
                            <input id="password" name="password" type="password" required autocomplete="new-password" class="ui-input focus-ring" placeholder="Minimum 6 characters">
                        </div>
                        <div>
                            <label for="confirmPassword" class="field-label">Confirm Password *</label>
                            <input id="confirmPassword" name="confirmPassword" type="password" required autocomplete="new-password" class="ui-input focus-ring" placeholder="Confirm password">
                        </div>
                    </div>
                    <div class="flex flex-col sm:flex-row justify-end gap-3">
                        <a href="/lankajobshub/dashboard" class="ui-btn-secondary focus-ring px-6 py-3">Cancel</a>
                        <button type="submit" class="ui-btn-primary focus-ring px-6 py-3">Create User Account</button>
                    </div>
                </form>
            </section>

            <aside class="ui-card rounded-3xl bg-white p-6 lg:p-8 h-fit">
                <h2 class="text-xl font-bold text-slate-950 mb-4">Role Access</h2>
                <p class="text-sm text-slate-500 mb-4">Your role: <span class="font-bold text-slate-900">${currentUser.role.displayName}</span></p>
                <div class="space-y-2">
                    <c:forEach items="${creatableRoles}" var="role">
                        <span class="inline-flex rounded-full bg-blue-50 px-3 py-1 text-sm font-semibold text-blue-700">${role.displayName}</span>
                    </c:forEach>
                </div>
            </aside>
        </div>
    </main>
</body>
</html>
