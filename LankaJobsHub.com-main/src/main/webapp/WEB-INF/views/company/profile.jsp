<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${company.name} - Company Profile</title>
    <link rel="icon" type="image/svg+xml" href="/lankajobshub/static/images/favicon.svg">
    <link rel="apple-touch-icon" href="/lankajobshub/static/images/lankajobshub-logo.svg">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="/lankajobshub/static/css/lankajobshub-ui.css">
    <style>.line-clamp-2 { display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }</style>
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
                    <a href="/lankajobshub/jobs" class="hidden sm:inline-flex px-4 py-2 rounded-lg text-slate-700 hover:text-blue-600 font-medium focus-ring">Find Jobs</a>
                    <a href="/lankajobshub/dashboard" class="ui-btn-secondary focus-ring px-4 py-2">Dashboard</a>
                </div>
            </div>
        </div>
    </nav>

    <main>
        <section class="bg-gradient-to-br from-white via-blue-50 to-slate-50 border-b border-slate-200">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 lg:py-14">
                <div class="flex flex-col md:flex-row md:items-center gap-6">
                    <div class="h-20 w-20 rounded-2xl bg-blue-600 text-white flex items-center justify-center text-3xl font-bold shadow-lg shadow-blue-200">
                        <c:out value="${fn:substring(company.name,0,1)}" />
                    </div>
                    <div>
                        <p class="text-blue-700 font-semibold">Company profile</p>
                        <h1 class="mt-2 text-4xl lg:text-5xl font-bold text-slate-950">${company.name}</h1>
                        <c:if test="${not empty company.websiteUrl}">
                            <a href="${company.websiteUrl}" target="_blank" class="mt-3 inline-flex text-blue-700 font-semibold hover:text-blue-900 focus-ring rounded break-all">${company.websiteUrl}</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </section>

        <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 lg:py-14">
            <div class="grid lg:grid-cols-3 gap-6">
                <div class="lg:col-span-2 space-y-6">
                    <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                        <h2 class="text-xl font-bold text-slate-950 mb-4">Company Overview</h2>
                        <p class="text-slate-600 leading-relaxed whitespace-pre-wrap">${company.description != null ? company.description : 'No description provided.'}</p>
                    </section>

                    <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                        <h2 class="text-xl font-bold text-slate-950 mb-5">Available Vacancies</h2>
                        <c:choose>
                            <c:when test="${empty jobs}">
                                <div class="rounded-2xl border border-dashed border-slate-300 bg-slate-50 p-8 text-center">
                                    <h3 class="font-bold text-slate-950">No open positions</h3>
                                    <p class="mt-2 text-slate-500">This company has no active vacancies listed right now.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="grid gap-4">
                                    <c:forEach items="${jobs}" var="job">
                                        <article class="ui-soft-card rounded-2xl bg-white p-5">
                                            <div class="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4">
                                                <div>
                                                    <h3 class="text-lg font-bold text-slate-950">
                                                        <a href="/lankajobshub/jobs/${job.id}" class="hover:text-blue-700 focus-ring rounded">${job.title}</a>
                                                    </h3>
                                                    <p class="mt-1 text-sm text-slate-500">${job.location} • ${job.jobType.displayName}</p>
                                                    <c:if test="${not empty job.description}">
                                                        <p class="mt-3 text-slate-600 line-clamp-2">${job.description}</p>
                                                    </c:if>
                                                </div>
                                                <a href="/lankajobshub/jobs/${job.id}" class="ui-btn-secondary focus-ring px-4 py-2">View Job</a>
                                            </div>
                                        </article>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </section>
                </div>

                <aside class="ui-card rounded-3xl bg-white p-6 lg:p-8 h-fit">
                    <h2 class="text-xl font-bold text-slate-950 mb-5">Company Information</h2>
                    <dl class="space-y-4 text-sm">
                        <div><dt class="text-slate-500">Contact Person</dt><dd class="font-semibold text-slate-900">${company.contactPerson != null ? company.contactPerson : 'Not added yet'}</dd></div>
                        <div><dt class="text-slate-500">Phone</dt><dd class="font-semibold text-slate-900">${company.phoneNumber != null ? company.phoneNumber : 'Not added yet'}</dd></div>
                        <div><dt class="text-slate-500">Website</dt><dd class="font-semibold break-all"><c:choose><c:when test="${not empty company.websiteUrl}"><a href="${company.websiteUrl}" target="_blank" class="text-blue-700 hover:text-blue-900">${company.websiteUrl}</a></c:when><c:otherwise>Not added yet</c:otherwise></c:choose></dd></div>
                        <div><dt class="text-slate-500">Address</dt><dd class="font-semibold text-slate-900">${company.address != null ? company.address : 'Not added yet'}</dd></div>
                    </dl>
                </aside>
            </div>
        </section>
    </main>
</body>
</html>
