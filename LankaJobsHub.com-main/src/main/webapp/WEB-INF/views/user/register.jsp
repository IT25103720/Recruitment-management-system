<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - LankaJobsHub</title>
    <link rel="icon" type="image/svg+xml" href="/lankajobshub/static/images/favicon.svg">
    <link rel="apple-touch-icon" href="/lankajobshub/static/images/lankajobshub-logo.svg">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="/lankajobshub/static/css/lankajobshub-ui.css">
</head>
<body>
    <div class="min-h-screen bg-gradient-to-br from-white via-blue-50 to-slate-50">
        <nav class="bg-white/95 backdrop-blur border-b border-slate-200">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex items-center justify-between h-20">
                    <a href="/lankajobshub/" class="flex items-center gap-3 focus-ring rounded-lg">
                        <img src="/lankajobshub/static/images/lankajobshub-logo.svg" alt="LankaJobsHub logo" class="h-11 w-11 rounded-xl shadow-lg shadow-blue-200">
                        <span>
                            <span class="block text-xl font-bold text-slate-950">LankaJobsHub</span>
                            <span class="hidden sm:block text-xs text-slate-500">Recruitment Company & Job Portal</span>
                        </span>
                    </a>
                    <div class="flex items-center gap-4 text-sm font-medium">
                        <a href="/lankajobshub/jobs" class="hidden sm:inline ui-nav-link focus-ring rounded">Find Jobs</a>
                        <a href="/lankajobshub/users/login" class="ui-btn-secondary focus-ring px-4 py-2">Login</a>
                    </div>
                </div>
            </div>
        </nav>

        <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 lg:py-20">
            <div class="grid lg:grid-cols-12 gap-10 items-start">
                <section class="lg:col-span-5">
                    <span class="ui-badge">Candidate access</span>
                    <h1 class="mt-6 text-4xl lg:text-5xl font-bold tracking-tight text-slate-950 leading-tight">Create your job seeker account.</h1>
                    
                    <div class="mt-8 space-y-4">
                        <div class="ui-soft-card rounded-2xl bg-white p-5">
                            <h2 class="font-semibold text-slate-950">Profile-first applications</h2>
                            <p class="mt-1 text-sm text-slate-500">Keep your skills, education, experience, and CV ready for employers.</p>
                        </div>
                        <div class="ui-soft-card rounded-2xl bg-white p-5">
                            <h2 class="font-semibold text-slate-950">Application visibility</h2>
                            <p class="mt-1 text-sm text-slate-500">Track applications, interviews, and notifications after you sign in.</p>
                        </div>
                    </div>
                </section>

                <section class="lg:col-span-7 ui-card bg-white rounded-3xl p-6 sm:p-8 lg:p-10">
                    <div>
                        <p class="text-blue-600 font-semibold">Registration</p>
                        <h2 class="mt-2 text-3xl font-bold text-slate-950">Join LankaJobsHub</h2>
                        <p class="mt-2 text-slate-500">Public registration is currently available for job seekers.</p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="ui-alert ui-alert-error mt-6">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="ui-alert ui-alert-success mt-6">${success}</div>
                    </c:if>

                    <form class="mt-8 space-y-6" action="/lankajobshub/users/register" method="POST">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                        <div class="grid sm:grid-cols-2 gap-5">
                            <div class="sm:col-span-2">
                                <label for="email" class="block text-sm font-semibold text-slate-700 mb-2">Email address</label>
                                <input id="email" name="email" type="email" required autocomplete="email"
                                       class="ui-input focus-ring" placeholder="you@example.com" value="${user.email}">
                            </div>

                            <div>
                                <label for="password" class="block text-sm font-semibold text-slate-700 mb-2">Password</label>
                                <div class="relative">
                                    <input id="password" name="password" type="password" required autocomplete="new-password"
                                           class="ui-input focus-ring pr-24" placeholder="Create password">
                                    <button type="button" data-password-toggle="password"
                                            class="focus-ring absolute inset-y-1 right-1 rounded-lg px-3 text-sm font-semibold text-blue-700 hover:bg-blue-50">
                                        Show
                                    </button>
                                </div>
                            </div>

                            <div>
                                <label for="confirmPassword" class="block text-sm font-semibold text-slate-700 mb-2">Confirm password</label>
                                <div class="relative">
                                    <input id="confirmPassword" name="confirmPassword" type="password" required autocomplete="new-password"
                                           class="ui-input focus-ring pr-24" placeholder="Repeat password">
                                    <button type="button" data-password-toggle="confirmPassword"
                                            class="focus-ring absolute inset-y-1 right-1 rounded-lg px-3 text-sm font-semibold text-blue-700 hover:bg-blue-50">
                                        Show
                                    </button>
                                </div>
                            </div>

                            <div class="sm:col-span-2">
                                <label for="role" class="block text-sm font-semibold text-slate-700 mb-2">Account type</label>
                                <select id="role" name="role" required class="ui-input focus-ring">
                                    <option value="">Select your role</option>
                                    <c:forEach items="${roles}" var="role">
                                        <option value="${role}" ${user.role == role ? 'selected' : ''}>
                                            ${role.displayName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <button type="submit" class="ui-btn-primary focus-ring w-full px-5 py-3">Create account</button>

                        <p class="text-center text-sm text-slate-600">
                            Already have an account?
                            <a href="/lankajobshub/users/login" class="font-semibold text-blue-600 hover:text-blue-800 focus-ring rounded">Sign in</a>
                        </p>
                    </form>
                </section>
            </div>
        </main>
    </div>

    <script>
        document.querySelectorAll('[data-password-toggle]').forEach(function (button) {
            button.addEventListener('click', function () {
                var input = document.getElementById(button.getAttribute('data-password-toggle'));
                var isHidden = input.type === 'password';
                input.type = isHidden ? 'text' : 'password';
                button.textContent = isHidden ? 'Hide' : 'Show';
            });
        });
    </script>
</body>
</html>
