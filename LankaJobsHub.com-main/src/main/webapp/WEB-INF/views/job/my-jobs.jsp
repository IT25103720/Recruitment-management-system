<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Vacancies - LankaJobsHub</title>
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
            <a href="/lankajobshub/jobs/my-jobs" class="workspace-nav-link active focus-ring">My Vacancies</a>
            <a href="/lankajobshub/jobs/post" class="workspace-nav-link focus-ring">Post Job</a>
            <a href="/lankajobshub/interviews" class="workspace-nav-link focus-ring">Interviews</a>
            <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
            <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
        </div>

        <div class="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-4 mb-8">
            <div>
                <p class="text-blue-700 font-semibold">Vacancy management</p>
                <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">My Vacancies</h1>
                <p class="mt-2 text-slate-500">Manage posted jobs, review candidates, and update openings.</p>
            </div>
            <a href="/lankajobshub/jobs/post" class="ui-btn-primary focus-ring px-6 py-3">Post New Job</a>
        </div>

        <c:if test="${not empty success}">
            <div class="ui-alert ui-alert-success mb-6">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty jobs}">
                <div class="ui-card rounded-3xl bg-white p-10 text-center">
                    <h2 class="text-2xl font-bold text-slate-950">No vacancies posted yet</h2>
                    <p class="mt-3 text-slate-500">Create your first vacancy and start receiving applications.</p>
                    <a href="/lankajobshub/jobs/post" class="ui-btn-primary focus-ring mt-6 px-6 py-3">Post Job</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid gap-5">
                    <c:forEach items="${jobs}" var="job">
                        <article class="ui-soft-card rounded-3xl bg-white p-6">
                            <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-5">
                                <div class="min-w-0">
                                    <h2 class="text-2xl font-bold text-slate-950">
                                        <a href="/lankajobshub/jobs/${job.id}" class="hover:text-blue-700 focus-ring rounded">${job.title}</a>
                                    </h2>
                                    <div class="mt-3 flex flex-wrap gap-2">
                                        <span class="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">${job.jobType.displayName}</span>
                                        <span class="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700">${job.experienceLevel.displayName}</span>
                                        <span class="rounded-full bg-white border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-700">${job.status.displayName}</span>
                                        <span class="rounded-full bg-white border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-700">${job.active ? 'Active' : 'Inactive'}</span>
                                    </div>
                                </div>
                                <div class="flex flex-col sm:flex-row lg:flex-col gap-3">
                                    <a href="/lankajobshub/jobs/${job.id}" class="ui-btn-secondary focus-ring px-4 py-2">View</a>
                                    <a href="/lankajobshub/jobs/${job.id}/edit" class="ui-btn-secondary focus-ring px-4 py-2">Edit</a>
                                    <a href="/lankajobshub/applications/job/${job.id}" class="ui-btn-primary focus-ring px-4 py-2">Applications</a>
                                    <form action="/lankajobshub/jobs/${job.id}/delete" method="post" onsubmit="return confirm('Are you sure you want to delete this job?')">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <button type="submit" class="w-full rounded-xl border border-red-200 px-4 py-2 font-semibold text-red-700 hover:bg-red-50 focus-ring">Delete</button>
                                    </form>
                                </div>
                            </div>

                            <div class="mt-5 grid sm:grid-cols-2 lg:grid-cols-4 gap-3 text-sm">
                                <div class="rounded-2xl bg-slate-50 p-3"><span class="block text-xs font-semibold uppercase text-slate-400">Location</span><span class="mt-1 block font-semibold text-slate-800">${job.location}</span></div>
                                <div class="rounded-2xl bg-slate-50 p-3"><span class="block text-xs font-semibold uppercase text-slate-400">Salary</span><span class="mt-1 block font-semibold text-slate-800"><c:choose><c:when test="${job.salaryMin != null || job.salaryMax != null}">${job.salaryMin != null ? job.salaryMin : 'N/A'} - ${job.salaryMax != null ? job.salaryMax : 'N/A'}</c:when><c:otherwise>Not specified</c:otherwise></c:choose></span></div>
                                <div class="rounded-2xl bg-slate-50 p-3"><span class="block text-xs font-semibold uppercase text-slate-400">Posted</span><span class="mt-1 block font-semibold text-slate-800">${job.postedDate.toString().substring(0, 10)}</span></div>
                                <div class="rounded-2xl bg-slate-50 p-3"><span class="block text-xs font-semibold uppercase text-slate-400">Deadline</span><span class="mt-1 block font-semibold text-slate-800">${job.deadline != null ? job.deadline.toString().substring(0, 10) : 'Open until filled'}</span></div>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>
