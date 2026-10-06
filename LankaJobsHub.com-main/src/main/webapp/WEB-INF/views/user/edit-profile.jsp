<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile - LankaJobsHub</title>
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
                    <a href="/lankajobshub/users/profile" class="ui-btn-secondary focus-ring px-4 py-2">Back to Profile</a>
                </div>
            </div>
        </div>
    </nav>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 lg:py-10">
        <div class="workspace-nav mb-8">
            <a href="/lankajobshub/users/profile" class="workspace-nav-link active focus-ring">Overview/Profile</a>
            <a href="/lankajobshub/jobs" class="workspace-nav-link focus-ring">Find Jobs</a>
            <a href="/lankajobshub/applications/my-applications" class="workspace-nav-link focus-ring">My Applications</a>
            <a href="/lankajobshub/interviews" class="workspace-nav-link focus-ring">Interviews</a>
            <a href="/lankajobshub/notifications" class="workspace-nav-link focus-ring">Notifications</a>
        </div>

        <div class="mb-8">
            <p class="text-blue-700 font-semibold">Candidate workspace</p>
            <h1 class="mt-2 text-3xl lg:text-4xl font-bold text-slate-950">Update Your Profile</h1>
            <p class="mt-2 text-slate-500">Keep your contact details, career preferences, skills, and resume current.</p>
        </div>

        <c:if test="${not empty error}">
            <div class="ui-alert ui-alert-error mb-6">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="ui-alert ui-alert-success mb-6">${success}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/users/profile/edit" method="post" class="space-y-6">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                <h2 class="text-xl font-bold text-slate-950 mb-5">Personal Information</h2>
                <div class="grid md:grid-cols-2 gap-5">
                    <div>
                        <label class="field-label">Full name</label>
                        <input type="text" name="fullName" value="${user.fullName}" class="ui-input focus-ring" autocomplete="name" />
                    </div>
                    <div>
                        <label class="field-label">Phone number</label>
                        <input type="text" name="phoneNumber" value="${user.phoneNumber}" class="ui-input focus-ring" autocomplete="tel" />
                    </div>
                    <div>
                        <label class="field-label">LinkedIn URL</label>
                        <input type="text" name="linkedinUrl" value="${user.linkedinUrl}" class="ui-input focus-ring" autocomplete="url" />
                    </div>
                    <div>
                        <label class="field-label">GitHub URL</label>
                        <input type="text" name="githubUrl" value="${user.githubUrl}" class="ui-input focus-ring" autocomplete="url" />
                    </div>
                    <div class="md:col-span-2">
                        <label class="field-label">Bio</label>
                        <textarea name="bio" rows="4" class="ui-input focus-ring">${user.bio}</textarea>
                    </div>
                </div>
            </section>

            <c:if test="${user.role == 'JOB_SEEKER'}">
                <section class="ui-card rounded-3xl bg-white p-6 lg:p-8">
                    <h2 class="text-xl font-bold text-slate-950 mb-5">Career Preferences</h2>
                    <div class="grid md:grid-cols-3 gap-5">
                        <div>
                            <label class="field-label">Preferred job type</label>
                            <select name="preferredJobType" class="ui-input focus-ring">
                                <option value="">Select job type</option>
                                <c:forEach items="${jobTypes}" var="type">
                                    <option value="${type}" ${candidateProfile.preferredJobType == type ? 'selected' : ''}>${type.displayName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label class="field-label">Preferred location</label>
                            <input type="text" name="preferredLocation" value="${candidateProfile.preferredLocation}" class="ui-input focus-ring" />
                        </div>
                        <div>
                            <label class="field-label">Expected salary</label>
                            <input type="number" name="expectedSalary" value="${candidateProfile.expectedSalary}" step="0.01" min="0" data-non-negative="true" class="ui-input focus-ring" />
                        </div>
                    </div>
                </section>

                <section class="ui-card rounded-3xl bg-white p-6 lg:p-8 space-y-5">
                    <h2 class="text-xl font-bold text-slate-950">Skills, Education & Experience</h2>
                    <div>
                        <label class="field-label">Skills</label>
                        <textarea name="skills" rows="4" class="ui-input focus-ring" placeholder="Add one skill per line">${candidateProfile.skills}</textarea>
                        <p class="text-xs text-slate-500 mt-2">Add or remove skills by editing the lines.</p>
                    </div>
                    <div>
                        <label class="field-label">Education</label>
                        <textarea name="education" rows="4" class="ui-input focus-ring" placeholder="Add one education item per line">${candidateProfile.education}</textarea>
                        <p class="text-xs text-slate-500 mt-2">Add, edit, or remove education entries by editing the lines.</p>
                    </div>
                    <div>
                        <label class="field-label">Work experience</label>
                        <textarea name="workExperience" rows="4" class="ui-input focus-ring" placeholder="Add one work experience item per line">${candidateProfile.workExperience}</textarea>
                        <p class="text-xs text-slate-500 mt-2">Add, edit, or remove experience entries by editing the lines.</p>
                    </div>
                </section>
            </c:if>

            <input type="hidden" name="email" value="${user.email}" />
            <input type="hidden" name="role" value="${user.role}" />
            <input type="hidden" name="status" value="${user.status}" />

            <div class="flex flex-col sm:flex-row gap-3">
                <button type="submit" class="ui-btn-primary focus-ring px-6 py-3">Save Changes</button>
                <a href="${pageContext.request.contextPath}/users/profile" class="ui-btn-secondary focus-ring px-6 py-3">Cancel</a>
            </div>
        </form>

        <section class="grid lg:grid-cols-2 gap-6 mt-10">
            <div class="ui-soft-card rounded-3xl bg-white p-6">
                <h2 class="text-xl font-bold text-slate-950 mb-3">Profile Photo</h2>
                <c:if test="${not empty user.profilePhotoPath}">
                    <p class="text-sm text-slate-500 mb-3 break-all">Current: ${user.profilePhotoPath}</p>
                </c:if>
                <form action="${pageContext.request.contextPath}/upload/profile-photo" method="post" enctype="multipart/form-data" class="space-y-4">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type="file" name="file" accept=".jpg,.jpeg,.png,.gif" class="ui-input focus-ring" required />
                    <button type="submit" class="ui-btn-primary focus-ring px-5 py-3">Upload Photo</button>
                </form>
                <p class="text-xs text-slate-500 mt-3">Allowed: JPG, JPEG, PNG, GIF. Max 2MB.</p>
            </div>

            <div class="ui-soft-card rounded-3xl bg-white p-6">
                <h2 class="text-xl font-bold text-slate-950 mb-3">Resume/CV</h2>
                <c:if test="${not empty user.resumePath}">
                    <p class="text-sm text-slate-500 mb-3 break-all">Current: ${user.resumePath}</p>
                </c:if>
                <form action="${pageContext.request.contextPath}/upload/resume" method="post" enctype="multipart/form-data" class="space-y-4">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <input type="file" name="file" accept=".pdf,.doc,.docx" class="ui-input focus-ring" required />
                    <button type="submit" class="ui-btn-primary focus-ring px-5 py-3">Upload Resume</button>
                </form>
                <c:if test="${not empty user.resumePath}">
                    <form action="${pageContext.request.contextPath}/upload/resume/delete" method="post" class="mt-3">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="rounded-xl bg-red-600 px-5 py-3 font-semibold text-white hover:bg-red-700 focus-ring">Delete Resume</button>
                    </form>
                </c:if>
                <p class="text-xs text-slate-500 mt-3">Allowed: PDF, DOC, DOCX. Max 2MB.</p>
            </div>
        </section>

        <section class="ui-soft-card rounded-3xl bg-white p-6 mt-6">
            <h2 class="text-xl font-bold text-slate-950 mb-3">Security</h2>
            <p class="text-slate-500 mb-5">Update your account password using your current password.</p>
            <form action="${pageContext.request.contextPath}/users/profile/password" method="post" class="grid md:grid-cols-3 gap-5">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div>
                    <label class="field-label">Current password</label>
                    <input type="password" name="currentPassword" required autocomplete="current-password" class="ui-input focus-ring" />
                </div>
                <div>
                    <label class="field-label">New password</label>
                    <input type="password" name="newPassword" required minlength="6" autocomplete="new-password" class="ui-input focus-ring" />
                </div>
                <div>
                    <label class="field-label">Confirm new password</label>
                    <input type="password" name="confirmPassword" required minlength="6" autocomplete="new-password" class="ui-input focus-ring" />
                </div>
                <div class="md:col-span-3">
                    <button type="submit" class="ui-btn-primary focus-ring px-5 py-3">Update Password</button>
                </div>
            </form>
        </section>

        <c:if test="${user.role == 'JOB_SEEKER'}">
            <section class="ui-soft-card rounded-3xl bg-white p-6 mt-6">
                <h2 class="text-xl font-bold text-slate-950 mb-2">Candidate Data</h2>
                <p class="text-slate-500 mb-4">Remove only the candidate-specific profile details from your account.</p>
                <form action="${pageContext.request.contextPath}/users/profile/candidate/delete" method="post"
                      onsubmit="return confirm('Delete your candidate profile data?')">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="rounded-xl bg-red-600 px-5 py-3 font-semibold text-white hover:bg-red-700 focus-ring">Delete Candidate Data</button>
                </form>
            </section>
        </c:if>
    </main>
    <script src="/lankajobshub/static/js/validation.js"></script>
</body>
</html>
