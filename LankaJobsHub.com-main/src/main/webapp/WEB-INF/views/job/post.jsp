<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Post a Job - LankaJobsHub</title>
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
            <a href="/lankajobshub/jobs/my-jobs" class="workspace-nav-link focus-ring">My Vacancies</a>
            <a href="/lankajobshub/jobs/post" class="workspace-nav-link active focus-ring">Post Job</a>
            <a href="/lankajobshub/interviews" class="workspace-nav-link focus-ring">Interviews</a>
            <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
            <a href="/lankajobshub/users/profile" class="workspace-nav-link focus-ring">Profile</a>
        </div>

        <div class="mb-8">
            <p class="text-blue-700 font-semibold">Vacancy management</p>
            <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">Post a New Job</h1>
            <p class="mt-2 text-slate-500">Create a clear vacancy for candidates and recruiters to act on.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>

        <form action="/lankajobshub/jobs/post" method="post" class="space-y-6">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Basic Information</h2>
                <div class="grid md:grid-cols-2 gap-5">
                    <c:if test="${not empty companies}">
                        <div class="md:col-span-2">
                            <label for="companyId" class="field-label">Company *</label>
                            <select id="companyId" name="company.id" required class="ui-input focus-ring">
                                <option value="">Select Company</option>
                                <c:forEach items="${companies}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:if>
                    <div>
                        <label for="title" class="field-label">Job Title *</label>
                        <input type="text" id="title" name="title" required class="ui-input focus-ring">
                    </div>
                    <div>
                        <label for="location" class="field-label">Location *</label>
                        <input type="text" id="location" name="location" required class="ui-input focus-ring">
                    </div>
                    <div>
                        <label for="jobType" class="field-label">Job Type *</label>
                        <select id="jobType" name="jobType" required class="ui-input focus-ring">
                            <option value="">Select Job Type</option>
                            <c:forEach items="${jobTypes}" var="type">
                                <option value="${type}">${type.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="experienceLevel" class="field-label">Experience Level *</label>
                        <select id="experienceLevel" name="experienceLevel" required class="ui-input focus-ring">
                            <option value="">Select Experience Level</option>
                            <c:forEach items="${experienceLevels}" var="level">
                                <option value="${level}">${level.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="md:col-span-2">
                        <label for="industry" class="field-label">Industry</label>
                        <input type="text" id="industry" name="industry" class="ui-input focus-ring">
                    </div>
                </div>
            </section>

            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8 space-y-5">
                <h2 class="text-xl font-bold text-slate-950">Job Details</h2>
                <div>
                    <label for="description" class="field-label">Job Description</label>
                    <textarea id="description" name="description" rows="5" class="ui-input focus-ring" placeholder="Describe the role, team, and opportunity."></textarea>
                </div>
                <div>
                    <label for="requirements" class="field-label">Requirements *</label>
                    <textarea id="requirements" name="requirements" rows="5" required class="ui-input focus-ring" placeholder="List required skills, qualifications, and experience."></textarea>
                </div>
                <div>
                    <label for="responsibilities" class="field-label">Responsibilities</label>
                    <textarea id="responsibilities" name="responsibilities" rows="5" class="ui-input focus-ring" placeholder="List key responsibilities and duties."></textarea>
                </div>
            </section>

            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Compensation & Application</h2>
                <div class="grid md:grid-cols-3 gap-5">
                    <div>
                        <label for="salaryMin" class="field-label">Minimum Salary (LKR)</label>
                        <input type="number" id="salaryMin" name="salaryMin" step="0.01" min="0" data-non-negative="true" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label for="salaryMax" class="field-label">Maximum Salary (LKR)</label>
                        <input type="number" id="salaryMax" name="salaryMax" step="0.01" min="0" data-non-negative="true" class="ui-input focus-ring">
                    </div>
                    <div>
                        <label for="deadline" class="field-label">Application Deadline</label>
                        <input type="datetime-local" id="deadline" name="deadline" data-min-now="true" class="ui-input focus-ring">
                    </div>
                </div>
            </section>

            <div class="flex flex-col sm:flex-row justify-end gap-3">
                <a href="/lankajobshub/jobs/my-jobs" class="ui-btn-secondary focus-ring px-6 py-3">Cancel</a>
                <button type="submit" class="ui-btn-primary focus-ring px-6 py-3">Post Job</button>
            </div>
        </form>
    </main>
    <script src="/lankajobshub/static/js/validation.js"></script>
</body>
</html>
