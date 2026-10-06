<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - LankaJobsHub</title>
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
                        <a href="/lankajobshub/users/register" class="ui-btn-secondary focus-ring px-4 py-2">Register</a>
                    </div>
                </div>
            </div>
        </nav>

        <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 lg:py-20">
            <div class="grid lg:grid-cols-2 gap-10 items-center">
                <section class="hidden lg:block">
                    <span class="ui-badge">Trusted hiring platform</span>
                    <h1 class="mt-6 text-5xl font-bold tracking-tight text-slate-950 leading-tight">Welcome back to smarter recruitment.</h1>
                  
                    <div class="ui-card mt-10 rounded-3xl bg-white p-6">
                        <div class="rounded-2xl bg-[#0F2A5F] p-6 text-white">
                            <p class="text-blue-100 text-sm font-medium">LankaJobsHub workspace</p>
                            <h2 class="mt-2 text-2xl font-bold">Jobs, candidates, interviews, and updates in one place.</h2>
                        </div>
                        <div class="mt-5 grid grid-cols-3 gap-3 text-center">
                            <div class="rounded-2xl bg-blue-50 p-4">
                                <div class="text-xl font-bold text-blue-700">01</div>
                                <div class="text-xs text-slate-500">Search</div>
                            </div>
                            <div class="rounded-2xl bg-slate-50 p-4">
                                <div class="text-xl font-bold text-slate-900">02</div>
                                <div class="text-xs text-slate-500">Apply</div>
                            </div>
                            <div class="rounded-2xl bg-blue-50 p-4">
                                <div class="text-xl font-bold text-blue-700">03</div>
                                <div class="text-xs text-slate-500">Interview</div>
                            </div>
                        </div>
                    </div>
                </section>

                <section class="ui-card bg-white rounded-3xl p-6 sm:p-8 lg:p-10 max-w-xl w-full mx-auto">
                    <div>
                        <p class="text-blue-600 font-semibold">Account Login</p>
                        <h2 class="mt-2 text-3xl font-bold text-slate-950">Sign in to your account</h2>
                        <p class="mt-2 text-slate-500">Use your LankaJobsHub email and password to continue.</p>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="ui-alert ui-alert-error mt-6">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="ui-alert ui-alert-success mt-6">${success}</div>
                    </c:if>

                    <form class="mt-8 space-y-5" action="/lankajobshub/users/login" method="POST">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <c:if test="${not empty redirect}">
                            <input type="hidden" name="redirect" value="${redirect}" />
                        </c:if>

                        <div>
                            <label for="email" class="block text-sm font-semibold text-slate-700 mb-2">Email address</label>
                            <input id="email" name="email" type="email" required autocomplete="email"
                                   class="ui-input focus-ring" placeholder="you@example.com">
                        </div>

                        <div>
                            <label for="password" class="block text-sm font-semibold text-slate-700 mb-2">Password</label>
                            <div class="relative">
                                <input id="password" name="password" type="password" required autocomplete="current-password"
                                       class="ui-input focus-ring pr-24" placeholder="Enter your password">
                                <button type="button" data-password-toggle="password"
                                        class="focus-ring absolute inset-y-1 right-1 rounded-lg px-3 text-sm font-semibold text-blue-700 hover:bg-blue-50">
                                    Show
                                </button>
                            </div>
                        </div>

                        <button type="submit" class="ui-btn-primary focus-ring w-full px-5 py-3">Sign in</button>

                        <p class="text-center text-sm text-slate-600">
                            Do not have an account?
                            <a href="/lankajobshub/users/register" class="font-semibold text-blue-600 hover:text-blue-800 focus-ring rounded">Create one</a>
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
