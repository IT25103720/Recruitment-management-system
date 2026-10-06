<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interview - LankaJobsHub</title>
    <link rel="icon" type="image/svg+xml" href="/lankajobshub/static/images/favicon.svg">
    <link rel="apple-touch-icon" href="/lankajobshub/static/images/lankajobshub-logo.svg">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="/lankajobshub/static/css/lankajobshub-ui.css">
</head>
<body class="management-shell">
    <main class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-10">
        <div class="workspace-nav mb-8">
            <a href="/lankajobshub/dashboard" class="workspace-nav-link focus-ring">Dashboard</a>
            <a href="/lankajobshub/jobs/my-jobs" class="workspace-nav-link focus-ring">My Vacancies</a>
            <a href="/lankajobshub/jobs/post" class="workspace-nav-link focus-ring">Post Job</a>
            <a href="/lankajobshub/interviews" class="workspace-nav-link active focus-ring">Interviews</a>
            <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
            <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
        </div>

        <div class="mb-8">
            <p class="text-blue-700 font-semibold">Interview management</p>
            <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">Interview</h1>
            <c:if test="${not empty application}">
                <p class="mt-2 text-slate-500">${application.job.title} • ${application.applicant.email}</p>
            </c:if>
        </div>

        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>

        <form action="${formAction}" method="post" class="space-y-6">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <c:if test="${not empty application}">
                <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                    <h2 class="text-xl font-bold text-slate-950 mb-4">Candidate & Application</h2>
                    <div class="grid md:grid-cols-2 gap-4 text-sm">
                        <div class="rounded-2xl bg-slate-50 p-4"><span class="block text-xs font-semibold uppercase text-slate-400">Candidate</span><span class="mt-1 block font-semibold text-slate-900 break-all">${application.applicant.email}</span></div>
                        <div class="rounded-2xl bg-slate-50 p-4"><span class="block text-xs font-semibold uppercase text-slate-400">Job</span><span class="mt-1 block font-semibold text-slate-900">${application.job.title}</span></div>
                    </div>
                </section>
            </c:if>

            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Schedule</h2>
                <div class="grid md:grid-cols-3 gap-5">
                    <div>
                        <label class="field-label">Date and time *</label>
                        <input type="datetime-local" name="scheduledDate" value="${interview.scheduledDate != null ? interview.scheduledDate.toString().substring(0, 16) : ''}" required data-min-now="true" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Duration minutes *</label>
                        <input type="number" name="durationMinutes" value="${interview.durationMinutes != null ? interview.durationMinutes : 60}" min="1" required class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Interview type *</label>
                        <select name="interviewType" required class="ui-input focus-ring">
                            <option value="">Select type</option>
                            <c:forEach items="${interviewTypes}" var="type">
                                <option value="${type}" ${interview.interviewType == type ? 'selected' : ''}>${type.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </section>

            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8 space-y-5">
                <h2 class="text-xl font-bold text-slate-950">Meeting & Instructions</h2>
                <div class="grid md:grid-cols-2 gap-5">
                    <div>
                        <label class="field-label">Location</label>
                        <input type="text" name="location" value="${interview.location}" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label class="field-label">Meeting link</label>
                        <input type="text" name="meetingLink" value="${interview.meetingLink}" class="ui-input focus-ring">
                    </div>
                </div>
                <div>
                    <label class="field-label">Panel / interviewer information</label>
                    <textarea name="panelInfo" rows="3" class="ui-input focus-ring">${interview.panelInfo}</textarea>
                </div>
                <div>
                    <label class="field-label">Notes / instructions</label>
                    <textarea name="notes" rows="4" class="ui-input focus-ring">${interview.notes}</textarea>
                </div>
            </section>

            <div class="flex flex-col sm:flex-row justify-end gap-3">
                <a href="/lankajobshub/interviews" class="ui-btn-secondary focus-ring px-6 py-3">Cancel</a>
                <button type="submit" class="ui-btn-primary focus-ring px-6 py-3">Save Interview</button>
            </div>
        </form>
    </main>
    <script src="/lankajobshub/static/js/validation.js"></script>
</body>
</html>
