<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interview Details - LankaJobsHub</title>
    <link rel="icon" type="image/svg+xml" href="/lankajobshub/static/images/favicon.svg">
    <link rel="apple-touch-icon" href="/lankajobshub/static/images/lankajobshub-logo.svg">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="/lankajobshub/static/css/lankajobshub-ui.css">
</head>
<body>
    <main class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-10">
        <c:if test="${user.role != 'JOB_SEEKER'}">
            <div class="workspace-nav mb-8">
                <a href="/lankajobshub/dashboard" class="workspace-nav-link focus-ring">Dashboard</a>
                <a href="/lankajobshub/jobs/my-jobs" class="workspace-nav-link focus-ring">My Vacancies</a>
                <a href="/lankajobshub/jobs/post" class="workspace-nav-link focus-ring">Post Job</a>
                <a href="/lankajobshub/interviews" class="workspace-nav-link active focus-ring">Interviews</a>
                <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
                <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
            </div>
        </c:if>
        <a href="/lankajobshub/interviews" class="ui-nav-link focus-ring rounded text-sm font-semibold">Back to Interviews</a>

        <section class="ui-card rounded-3xl bg-white p-6 lg:p-8 mt-4">
            <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-5 mb-6">
                <div>
                    <p class="text-sm font-semibold text-blue-700">
                        <c:choose>
                            <c:when test="${not empty interview.application.job.company}">${interview.application.job.company.name}</c:when>
                            <c:otherwise>LankaJobsHub</c:otherwise>
                        </c:choose>
                    </p>
                    <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">${interview.application.job.title}</h1>
                    <p class="mt-2 text-slate-500">${interview.interviewType.displayName} interview</p>
                </div>
                <span class="rounded-full bg-blue-50 px-3 py-1 text-sm font-semibold text-blue-700">${interview.status.displayName}</span>
            </div>

            <c:if test="${not empty success}">
                <div class="ui-alert ui-alert-success mb-6">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="ui-alert ui-alert-error mb-6">${error}</div>
            </c:if>

            <div class="grid md:grid-cols-2 gap-4 mb-8">
                <div class="rounded-2xl bg-blue-50 p-5">
                    <p class="text-xs font-semibold uppercase text-blue-500">Date and time</p>
                    <p class="mt-2 text-xl font-bold text-slate-950">${interview.scheduledDate}</p>
                </div>
                <div class="rounded-2xl bg-slate-50 p-5">
                    <p class="text-xs font-semibold uppercase text-slate-400">Duration</p>
                    <p class="mt-2 font-bold text-slate-900">${interview.durationMinutes} minutes</p>
                </div>
                <div class="rounded-2xl bg-slate-50 p-5">
                    <p class="text-xs font-semibold uppercase text-slate-400">Location</p>
                    <p class="mt-2 font-bold text-slate-900">${not empty interview.location ? interview.location : 'Not specified'}</p>
                </div>
                <div class="rounded-2xl bg-slate-50 p-5">
                    <p class="text-xs font-semibold uppercase text-slate-400">Meeting link</p>
                    <c:choose>
                        <c:when test="${not empty interview.meetingLink}">
                            <a class="mt-2 block font-bold text-blue-700 hover:text-blue-900 break-all" href="${interview.meetingLink}" target="_blank">Open meeting link</a>
                        </c:when>
                        <c:otherwise><p class="mt-2 empty-value">Not added yet</p></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="space-y-5">
                <div>
                    <h2 class="text-lg font-bold text-slate-950">Interviewer</h2>
                    <p class="mt-2 text-slate-600 break-all">${interview.interviewer.email}</p>
                </div>
                <div>
                    <h2 class="text-lg font-bold text-slate-950">Panel</h2>
                    <p class="mt-2 text-slate-600 whitespace-pre-line">${not empty interview.panelInfo ? interview.panelInfo : 'Not added yet'}</p>
                </div>
                <div>
                    <h2 class="text-lg font-bold text-slate-950">Notes / Instructions</h2>
                    <p class="mt-2 text-slate-600 whitespace-pre-line">${not empty interview.notes ? interview.notes : 'Not added yet'}</p>
                </div>
                <c:if test="${user.role != 'JOB_SEEKER'}">
                    <div>
                        <h2 class="text-lg font-bold text-slate-950">Feedback / Result</h2>
                        <p class="mt-2 text-slate-600 whitespace-pre-line">${not empty interview.feedback ? interview.feedback : 'Not added yet'}</p>
                    </div>
                </c:if>
            </div>

            <c:if test="${user.role != 'JOB_SEEKER'}">
                <div class="border-t border-slate-200 pt-6 mt-8 space-y-5">
                    <div class="flex flex-col sm:flex-row gap-3">
                        <a href="/lankajobshub/interviews/${interview.id}/edit" class="ui-btn-primary focus-ring px-5 py-3">Reschedule / Edit</a>
                        <form action="/lankajobshub/interviews/${interview.id}/cancel" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" class="rounded-xl bg-yellow-600 px-5 py-3 font-semibold text-white hover:bg-yellow-700 focus-ring">Cancel Interview</button>
                        </form>
                        <c:if test="${interview.status == 'CANCELLED'}">
                            <form action="/lankajobshub/interviews/${interview.id}/delete" method="post" onsubmit="return confirm('Delete this cancelled interview?')">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button type="submit" class="rounded-xl bg-red-600 px-5 py-3 font-semibold text-white hover:bg-red-700 focus-ring">Delete</button>
                            </form>
                        </c:if>
                    </div>
                    <form action="/lankajobshub/interviews/${interview.id}/complete" method="post" class="space-y-3">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <label class="field-label">Evaluation / result</label>
                        <textarea name="feedback" rows="4" class="ui-input focus-ring">${interview.feedback}</textarea>
                        <button type="submit" class="rounded-xl bg-green-600 px-5 py-3 font-semibold text-white hover:bg-green-700 focus-ring">Mark Completed</button>
                    </form>
                </div>
            </c:if>
        </section>
    </main>
</body>
</html>
