<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${job.title} - LankaJobsHub</title>
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
                    <span>
                        <span class="block text-xl font-bold text-slate-950">LankaJobsHub</span>
                        <span class="hidden sm:block text-xs text-slate-500">Recruitment Company & Job Portal</span>
                    </span>
                </a>
                <div class="hidden md:flex items-center gap-7 text-sm font-medium">
                    <a href="/lankajobshub/" class="ui-nav-link focus-ring rounded">Home</a>
                    <a href="/lankajobshub/jobs" class="text-blue-600 focus-ring rounded">Find Jobs</a>
                    <a href="/lankajobshub/companies" class="ui-nav-link focus-ring rounded">Companies</a>
                </div>
                <div class="flex items-center gap-3">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <a href="/lankajobshub/dashboard" class="hidden sm:inline-flex px-4 py-2 rounded-lg text-slate-700 hover:text-blue-600 font-medium focus-ring">Dashboard</a>
                            <a href="/lankajobshub/users/logout" class="ui-btn-secondary focus-ring px-4 py-2">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/lankajobshub/users/login" class="hidden sm:inline-flex px-4 py-2 rounded-lg text-slate-700 hover:text-blue-600 font-medium focus-ring">Login</a>
                            <a href="/lankajobshub/users/register" class="ui-btn-primary focus-ring px-5 py-2.5">Register</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>

    <main>
        <section class="bg-gradient-to-br from-white via-blue-50 to-slate-50 border-b border-slate-200">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-12">
                <nav class="flex text-sm text-slate-500 mb-8" aria-label="Breadcrumb">
                    <a href="/lankajobshub/" class="hover:text-blue-700 focus-ring rounded">Home</a>
                    <span class="mx-2">/</span>
                    <a href="/lankajobshub/jobs" class="hover:text-blue-700 focus-ring rounded">Jobs</a>
                    <span class="mx-2">/</span>
                    <span class="text-slate-700">${job.title}</span>
                </nav>

                <div class="grid lg:grid-cols-12 gap-8 items-start">
                    <div class="lg:col-span-8">
                        <p class="text-sm font-semibold text-blue-700">
                            <c:choose>
                                <c:when test="${not empty job.company}">
                                    <a href="/lankajobshub/companies/${job.company.id}" class="hover:text-blue-900 focus-ring rounded">${job.company.name}</a>
                                </c:when>
                                <c:otherwise>LankaJobsHub</c:otherwise>
                            </c:choose>
                        </p>
                        <h1 class="mt-3 text-4xl lg:text-5xl font-bold tracking-tight text-slate-950">${job.title}</h1>
                        <div class="mt-5 flex flex-wrap gap-2">
                            <span class="rounded-full bg-blue-600 px-3 py-1 text-xs font-semibold text-white">${job.jobType.displayName}</span>
                            <span class="rounded-full bg-white border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-700">${job.experienceLevel.displayName}</span>
                            <c:if test="${not empty job.industry}">
                                <span class="rounded-full bg-white border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-700">${job.industry}</span>
                            </c:if>
                            <span class="rounded-full bg-white border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-700">${job.status.displayName}</span>
                        </div>
                    </div>

                    <div class="lg:col-span-4 ui-card rounded-3xl bg-white p-6">
                        <p class="text-sm font-semibold text-slate-500">Ready to apply?</p>
                        <c:choose>
                            <c:when test="${not empty sessionScope.user && sessionScope.user.role == 'JOB_SEEKER'}">
                                <a href="/lankajobshub/applications/apply/${job.id}" class="ui-btn-primary focus-ring mt-4 w-full px-5 py-3">Apply Now</a>
                            </c:when>
                            <c:when test="${empty sessionScope.user}">
                                <a href="/lankajobshub/users/login" class="ui-btn-primary focus-ring mt-4 w-full px-5 py-3">Login to Apply</a>
                                <p class="mt-3 text-sm text-slate-500">Sign in as a job seeker to submit an application.</p>
                            </c:when>
                            <c:otherwise>
                                <div class="mt-4 rounded-2xl bg-slate-50 p-4 text-sm font-semibold text-slate-600">Application is available for job seeker accounts.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </section>

        <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 lg:py-14">
            <div class="grid lg:grid-cols-12 gap-8 items-start">
                <div class="lg:col-span-8 space-y-6">
                    <c:if test="${not empty job.company}">
                        <section class="ui-soft-card rounded-3xl bg-white p-6">
                            <h2 class="text-xl font-bold text-slate-950">Company</h2>
                            <p class="mt-3 font-semibold text-slate-900">
                                <a href="/lankajobshub/companies/${job.company.id}" class="text-blue-700 hover:text-blue-900 focus-ring rounded">${job.company.name}</a>
                            </p>
                            <c:if test="${not empty job.company.description}">
                                <p class="mt-3 text-slate-600 leading-relaxed">${job.company.description}</p>
                            </c:if>
                        </section>
                    </c:if>

                    <section class="ui-soft-card rounded-3xl bg-white p-6">
                        <h2 class="text-xl font-bold text-slate-950">Job Description</h2>
                        <p class="mt-4 text-slate-600 leading-relaxed whitespace-pre-wrap">${job.description}</p>
                    </section>

                    <section class="ui-soft-card rounded-3xl bg-white p-6">
                        <h2 class="text-xl font-bold text-slate-950">Requirements</h2>
                        <p class="mt-4 text-slate-600 leading-relaxed whitespace-pre-wrap">${job.requirements}</p>
                    </section>

                    <c:if test="${not empty job.responsibilities}">
                        <section class="ui-soft-card rounded-3xl bg-white p-6">
                            <h2 class="text-xl font-bold text-slate-950">Responsibilities</h2>
                            <p class="mt-4 text-slate-600 leading-relaxed whitespace-pre-wrap">${job.responsibilities}</p>
                        </section>
                    </c:if>
                </div>

                <aside class="lg:col-span-4 space-y-6 lg:sticky lg:top-28">
                    <section class="ui-card rounded-3xl bg-white p-6">
                        <h2 class="text-lg font-bold text-slate-950">Job Summary</h2>
                        <dl class="mt-5 space-y-4 text-sm">
                            <div class="flex justify-between gap-4 border-b border-slate-100 pb-3">
                                <dt class="text-slate-500">Location</dt>
                                <dd class="font-semibold text-slate-900 text-right">${job.location}</dd>
                            </div>
                            <div class="flex justify-between gap-4 border-b border-slate-100 pb-3">
                                <dt class="text-slate-500">Job type</dt>
                                <dd class="font-semibold text-slate-900 text-right">${job.jobType.displayName}</dd>
                            </div>
                            <div class="flex justify-between gap-4 border-b border-slate-100 pb-3">
                                <dt class="text-slate-500">Experience</dt>
                                <dd class="font-semibold text-slate-900 text-right">${job.experienceLevel.displayName}</dd>
                            </div>
                            <div class="flex justify-between gap-4 border-b border-slate-100 pb-3">
                                <dt class="text-slate-500">Salary</dt>
                                <dd class="font-semibold text-slate-900 text-right">
                                    <c:choose>
                                        <c:when test="${job.salaryMin != null || job.salaryMax != null}">
                                            ${job.salaryMin != null ? job.salaryMin : 'N/A'} - ${job.salaryMax != null ? job.salaryMax : 'N/A'}
                                        </c:when>
                                        <c:otherwise>Not specified</c:otherwise>
                                    </c:choose>
                                </dd>
                            </div>
                            <div class="flex justify-between gap-4 border-b border-slate-100 pb-3">
                                <dt class="text-slate-500">Posted</dt>
                                <dd class="font-semibold text-slate-900 text-right">${job.postedDate}</dd>
                            </div>
                            <div class="flex justify-between gap-4">
                                <dt class="text-slate-500">Deadline</dt>
                                <dd class="font-semibold text-slate-900 text-right">
                                    <c:choose>
                                        <c:when test="${job.deadline != null}">${job.deadline}</c:when>
                                        <c:otherwise>Open until filled</c:otherwise>
                                    </c:choose>
                                </dd>
                            </div>
                        </dl>
                    </section>

                    <section class="ui-soft-card rounded-3xl bg-white p-6">
                        <h2 class="text-lg font-bold text-slate-950">Posted By</h2>
                        <div class="mt-4 flex items-center gap-3">
                            <div class="flex h-12 w-12 items-center justify-center rounded-2xl bg-blue-50 text-lg font-bold text-blue-700">
                                ${fn:toUpperCase(fn:substring(job.postedBy.email,0,1))}
                            </div>
                            <div class="min-w-0">
                                <p class="font-semibold text-slate-950 break-all">${job.postedBy.email}</p>
                                <p class="text-sm text-slate-500">${job.postedBy.role.displayName}</p>
                            </div>
                        </div>
                    </section>

                    <c:if test="${not empty sessionScope.user && (sessionScope.user.id == job.postedBy.id || sessionScope.user.role == 'ADMIN')}">
                        <section class="ui-soft-card rounded-3xl bg-white p-6">
                            <h2 class="text-lg font-bold text-slate-950">Manage Job</h2>
                            <div class="mt-4 flex flex-col gap-3">
                                <a href="/lankajobshub/jobs/${job.id}/edit" class="ui-btn-secondary focus-ring px-4 py-2">Edit Job</a>
                                <form action="/lankajobshub/jobs/${job.id}/delete" method="POST" onsubmit="return confirm('Are you sure you want to delete this job?')">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button type="submit" class="w-full rounded-xl bg-red-600 px-4 py-2 font-semibold text-white hover:bg-red-700 focus-ring">Delete Job</button>
                                </form>
                            </div>
                        </section>
                    </c:if>
                </aside>
            </div>
        </section>
    </main>
</body>
</html>
