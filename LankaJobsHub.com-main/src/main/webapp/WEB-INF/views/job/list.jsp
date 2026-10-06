<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Find Jobs - LankaJobsHub</title>
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
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 lg:py-16">
                <div class="max-w-3xl">
                    <span class="ui-badge">Public job discovery</span>
                    <h1 class="mt-5 text-4xl lg:text-5xl font-bold tracking-tight text-slate-950">Find Your Next Opportunity</h1>
                    <p class="mt-4 text-lg text-slate-600">Search active vacancies by role, location, job type, and experience level.</p>
                    <c:if test="${not empty jobs}">
                        <p class="mt-4 text-sm font-semibold text-blue-700">Showing ${jobs.size()} job${jobs.size() == 1 ? '' : 's'}</p>
                    </c:if>
                </div>

                <form action="/lankajobshub/jobs" method="GET" class="ui-card mt-8 rounded-3xl bg-white p-4 sm:p-6">
                    <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-4">
                        <div>
                            <label for="keyword" class="block text-sm font-semibold text-slate-700 mb-2">Keyword</label>
                            <input type="text" id="keyword" name="keyword" value="${keyword}" class="ui-input focus-ring"
                                   placeholder="Title, skill, company">
                        </div>
                        <div>
                            <label for="location" class="block text-sm font-semibold text-slate-700 mb-2">Location</label>
                            <input type="text" id="location" name="location" value="${location}" class="ui-input focus-ring"
                                   placeholder="City or province">
                        </div>
                        <div>
                            <label for="jobType" class="block text-sm font-semibold text-slate-700 mb-2">Job type</label>
                            <select id="jobType" name="jobType" class="ui-input focus-ring">
                                <option value="">All types</option>
                                <c:forEach items="${jobTypes}" var="type">
                                    <option value="${type}" ${selectedJobType == type ? 'selected' : ''}>${type.displayName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label for="experienceLevel" class="block text-sm font-semibold text-slate-700 mb-2">Experience</label>
                            <select id="experienceLevel" name="experienceLevel" class="ui-input focus-ring">
                                <option value="">All levels</option>
                                <c:forEach items="${experienceLevels}" var="level">
                                    <option value="${level}" ${selectedExperienceLevel == level ? 'selected' : ''}>${level.displayName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mt-5 flex flex-col sm:flex-row gap-3">
                        <button type="submit" class="ui-btn-primary focus-ring px-6 py-3">Search Jobs</button>
                        <a href="/lankajobshub/jobs" class="ui-btn-secondary focus-ring px-6 py-3">Clear Filters</a>
                    </div>
                </form>
            </div>
        </section>

        <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 lg:py-14">
            <c:choose>
                <c:when test="${empty jobs}">
                    <div class="ui-card rounded-3xl bg-white p-10 text-center">
                        <div class="mx-auto mb-5 flex h-14 w-14 items-center justify-center rounded-2xl bg-blue-50 text-2xl text-blue-600">?</div>
                        <h2 class="text-2xl font-bold text-slate-950">No jobs matched your search</h2>
                        <p class="mt-3 text-slate-500">Try a broader keyword, remove a filter, or browse all active vacancies.</p>
                        <a href="/lankajobshub/jobs" class="ui-btn-primary focus-ring mt-6 px-6 py-3">View All Jobs</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="grid gap-5">
                        <c:forEach items="${jobs}" var="job">
                            <article class="ui-soft-card rounded-3xl bg-white p-6 hover:shadow-xl transition">
                                <div class="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-5">
                                    <div class="min-w-0">
                                        <p class="text-sm font-semibold text-slate-500">
                                            <c:choose>
                                                <c:when test="${not empty job.company}">
                                                    <a href="/lankajobshub/companies/${job.company.id}" class="hover:text-blue-700 focus-ring rounded">${job.company.name}</a>
                                                </c:when>
                                                <c:otherwise>LankaJobsHub</c:otherwise>
                                            </c:choose>
                                        </p>
                                        <h2 class="mt-1 text-2xl font-bold text-slate-950">
                                            <a href="/lankajobshub/jobs/${job.id}" class="hover:text-blue-700 focus-ring rounded">${job.title}</a>
                                        </h2>
                                        <div class="mt-3 flex flex-wrap gap-2">
                                            <span class="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">${job.jobType.displayName}</span>
                                            <span class="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700">${job.experienceLevel.displayName}</span>
                                            <c:if test="${not empty job.industry}">
                                                <span class="rounded-full bg-white border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600">${job.industry}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <a href="/lankajobshub/jobs/${job.id}" class="ui-btn-primary focus-ring shrink-0 px-5 py-3">View Details</a>
                                </div>

                                <div class="mt-5 grid sm:grid-cols-2 lg:grid-cols-4 gap-3 text-sm text-slate-600">
                                    <div class="rounded-2xl bg-slate-50 p-3">
                                        <span class="block text-xs font-semibold uppercase text-slate-400">Location</span>
                                        <span class="mt-1 block font-semibold text-slate-800">${job.location}</span>
                                    </div>
                                    <div class="rounded-2xl bg-slate-50 p-3">
                                        <span class="block text-xs font-semibold uppercase text-slate-400">Salary</span>
                                        <span class="mt-1 block font-semibold text-slate-800">
                                            <c:choose>
                                                <c:when test="${job.salaryMin != null || job.salaryMax != null}">
                                                    ${job.salaryMin != null ? job.salaryMin : 'N/A'} - ${job.salaryMax != null ? job.salaryMax : 'N/A'}
                                                </c:when>
                                                <c:otherwise>Not specified</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="rounded-2xl bg-slate-50 p-3">
                                        <span class="block text-xs font-semibold uppercase text-slate-400">Posted</span>
                                        <span class="mt-1 block font-semibold text-slate-800">${job.postedDate}</span>
                                    </div>
                                    <div class="rounded-2xl bg-slate-50 p-3">
                                        <span class="block text-xs font-semibold uppercase text-slate-400">Deadline</span>
                                        <span class="mt-1 block font-semibold text-slate-800">
                                            <c:choose>
                                                <c:when test="${job.deadline != null}">${job.deadline}</c:when>
                                                <c:otherwise>Open until filled</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>

                                <c:if test="${not empty job.description}">
                                    <p class="mt-5 text-slate-600 leading-relaxed">${job.description}</p>
                                </c:if>
                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</body>
</html>
