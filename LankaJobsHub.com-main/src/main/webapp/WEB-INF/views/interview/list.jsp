<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interviews - LankaJobsHub</title>
    <link rel="icon" type="image/svg+xml" href="/lankajobshub/static/images/favicon.svg">
    <link rel="apple-touch-icon" href="/lankajobshub/static/images/lankajobshub-logo.svg">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="/lankajobshub/static/css/lankajobshub-ui.css">
</head>
<body>
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
            <c:choose>
                <c:when test="${user.role == 'JOB_SEEKER'}">
                    <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Overview/Profile</a>
                    <a href="/lankajobshub/jobs" class="workspace-nav-link focus-ring">Find Jobs</a>
                    <a href="/lankajobshub/applications/my-applications" class="workspace-nav-link focus-ring">My Applications</a>
                    <a href="/lankajobshub/interviews" class="workspace-nav-link active focus-ring">Interviews</a>
                    <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
                </c:when>
                <c:otherwise>
                    <a href="/lankajobshub/dashboard" class="workspace-nav-link focus-ring">Dashboard</a>
                    <a href="/lankajobshub/jobs/my-jobs" class="workspace-nav-link focus-ring">My Vacancies</a>
                    <a href="/lankajobshub/jobs/post" class="workspace-nav-link focus-ring">Post Job</a>
                    <a href="/lankajobshub/interviews" class="workspace-nav-link active focus-ring">Interviews</a>
                    <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
                    <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="mb-8">
            <p class="text-blue-700 font-semibold">Interview schedule</p>
            <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">Interviews</h1>
            <p class="mt-2 text-slate-500">Review scheduled, completed, rescheduled, and cancelled interviews.</p>
        </div>

        <c:if test="${not empty success}">
            <div class="ui-alert ui-alert-success mb-6">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty interviews}">
                <div class="ui-card rounded-3xl bg-white p-10 text-center">
                    <h2 class="text-2xl font-bold text-slate-950">No interviews found</h2>
                    <p class="mt-3 text-slate-500">Scheduled interviews will appear here when available.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid gap-5">
                    <c:forEach items="${interviews}" var="interview">
                        <article class="ui-soft-card rounded-3xl bg-white p-6">
                            <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-5">
                                <div>
                                    <p class="text-sm font-semibold text-slate-500">
                                        <c:choose>
                                            <c:when test="${not empty interview.application.job.company}">${interview.application.job.company.name}</c:when>
                                            <c:otherwise>LankaJobsHub</c:otherwise>
                                        </c:choose>
                                    </p>
                                    <h2 class="mt-1 text-2xl font-bold text-slate-950">${interview.application.job.title}</h2>
                                    <div class="mt-3 flex flex-wrap gap-2">
                                        <span class="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">${interview.status.displayName}</span>
                                        <span class="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700">${interview.interviewType.displayName}</span>
                                    </div>
                                </div>
                                <a href="/lankajobshub/interviews/${interview.id}" class="ui-btn-primary focus-ring px-5 py-3">View Details</a>
                            </div>

                            <div class="mt-5 grid md:grid-cols-3 gap-3 text-sm">
                                <div class="rounded-2xl bg-blue-50 p-4">
                                    <span class="block text-xs font-semibold uppercase text-blue-500">Date and time</span>
                                    <span class="mt-1 block font-bold text-slate-950">${interview.scheduledDate}</span>
                                </div>
                                <div class="rounded-2xl bg-slate-50 p-4">
                                    <span class="block text-xs font-semibold uppercase text-slate-400">Location</span>
                                    <span class="mt-1 block font-semibold text-slate-800">${not empty interview.location ? interview.location : 'Not specified'}</span>
                                </div>
                                <div class="rounded-2xl bg-slate-50 p-4">
                                    <span class="block text-xs font-semibold uppercase text-slate-400">Meeting link</span>
                                    <c:choose>
                                        <c:when test="${not empty interview.meetingLink}">
                                            <a class="mt-1 block font-semibold text-blue-700 hover:text-blue-900 break-all" href="${interview.meetingLink}" target="_blank">Open meeting link</a>
                                        </c:when>
                                        <c:otherwise><span class="mt-1 block empty-value">Not added yet</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>
