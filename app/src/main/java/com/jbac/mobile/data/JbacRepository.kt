package com.jbac.mobile.data

import com.jbac.mobile.model.Event
import com.jbac.mobile.model.HomeResponse
import com.jbac.mobile.model.HomeStats
import com.jbac.mobile.model.Notice
import kotlinx.coroutines.delay

class JbacRepository {
    suspend fun getHome(): HomeResponse {
        delay(300)
        return HomeResponse(
            title = "JBAC Mobile Portal",
            tagline = "Student-centric academic and campus updates in one place",
            highlights = listOf(
                "Admissions, notices, and events",
                "Works fully offline",
                "Fast search and filtering"
            ),
            stats = HomeStats(students = 1840, faculty = 126, programs = 22),
        )
    }

    suspend fun getNotices(): List<Notice> {
        delay(250)
        return listOf(
            Notice(1, "Admissions Open for 2026", "2026-03-01", "Admission", true),
            Notice(2, "Internal Exam Timetable Published", "2026-03-03", "Exams", true),
            Notice(3, "Library Extended Timings", "2026-03-04", "Campus", false),
            Notice(4, "NSS Volunteer Registration", "2026-03-05", "Activities", false),
            Notice(5, "Scholarship Renewal Window", "2026-03-06", "Finance", true),
        )
    }

    suspend fun getEvents(): List<Event> {
        delay(250)
        return listOf(
            Event(
                id = 1,
                name = "Innovation Expo",
                date = "2026-03-18",
                venue = "Main Auditorium",
                description = "Student projects, startup demos, and jury evaluation.",
            ),
            Event(
                id = 2,
                name = "Career Guidance Summit",
                date = "2026-03-24",
                venue = "Seminar Hall A",
                description = "Industry mentors share internship and placement pathways.",
            ),
            Event(
                id = 3,
                name = "Inter-Department Sports Meet",
                date = "2026-04-02",
                venue = "College Grounds",
                description = "Track events, team sports, and final award ceremony.",
            ),
        )
    }

    suspend fun submitContact(name: String, email: String, message: String) {
        delay(350)
        require(name.isNotBlank()) { "Name is required" }
        require(email.contains("@") && email.contains(".")) { "Enter a valid email" }
        require(message.length >= 10) { "Message should be at least 10 characters" }
    }
}
