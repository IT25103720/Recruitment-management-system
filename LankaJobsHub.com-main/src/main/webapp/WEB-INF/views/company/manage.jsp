<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Companies - LankaJobsHub</title>
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

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-10">
        <div class="workspace-nav mb-8">
            <a href="/lankajobshub/dashboard" class="workspace-nav-link focus-ring">Dashboard</a>
            <a href="/lankajobshub/companies" class="workspace-nav-link active focus-ring">Companies</a>
            <a href="/lankajobshub/jobs/my-jobs" class="workspace-nav-link focus-ring">My Vacancies</a>
            <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
        </div>

        <div class="mb-8">
            <p class="text-blue-700 font-semibold">Company directory</p>
            <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">Manage Companies</h1>
            <p class="mt-2 text-slate-500">Create company profiles used by vacancies and public company pages.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="ui-alert ui-alert-success mb-6">${success}</div>
        </c:if>

        <div class="grid lg:grid-cols-3 gap-6">
            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Add New Company</h2>
                <form action="/lankajobshub/companies/create" method="post" class="space-y-4">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <div>
                        <label class="field-label">Name *</label>
                        <input type="text" name="name" required class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Contact Person</label>
                        <input type="text" name="contactPerson" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Website</label>
                        <input type="url" name="websiteUrl" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Phone</label>
                        <input type="text" name="phoneNumber" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Address</label>
                        <textarea name="address" rows="2" class="ui-input focus-ring"></textarea>
                    </div>
                    <div>
                        <label class="field-label">Description</label>
                        <textarea name="description" rows="4" class="ui-input focus-ring"></textarea>
                    </div>
                    <button type="submit" class="ui-btn-primary focus-ring w-full px-5 py-3">Create Company</button>
                </form>
            </section>

            <section class="lg:col-span-2 ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Companies</h2>
                <c:choose>
                    <c:when test="${empty companies}">
                        <div class="rounded-2xl border border-dashed border-slate-300 bg-slate-50 p-8 text-center">
                            <h3 class="font-bold text-slate-950">No companies available</h3>
                            <p class="mt-2 text-slate-500">Created company profiles will appear here.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="grid gap-4">
                            <c:forEach items="${companies}" var="c">
                                <article class="ui-soft-card rounded-2xl bg-white p-5">
                                    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                                        <div class="min-w-0">
                                            <h3 class="text-lg font-bold text-slate-950">
                                                <a href="/lankajobshub/companies/${c.id}" class="hover:text-blue-700 focus-ring rounded">${c.name}</a>
                                            </h3>
                                            <p class="mt-1 text-sm text-slate-500 break-all">${not empty c.websiteUrl ? c.websiteUrl : 'Website not added'}</p>
                                        </div>
                                        <form action="/lankajobshub/companies/${c.id}/delete" method="post" onsubmit="return confirm('Delete this company?')">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                            <button type="submit" class="rounded-xl border border-red-200 px-4 py-2 font-semibold text-red-700 hover:bg-red-50 focus-ring">Delete</button>
                                        </form>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </main>
</body>
</html>
