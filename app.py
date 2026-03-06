import os
from pathlib import Path

from flask import Flask, abort, jsonify, request, send_from_directory

app = Flask(__name__)

MIRROR_DIR = Path(__file__).parent / "mirror_site"

API_HOME = {
    "college": "Jyothi Bharathi Adarsha College",
    "tagline": "Learning for life and leadership",
    "highlights": [
        "Experienced faculty across UG and PG streams",
        "Career guidance and placement support",
        "Library, lab, and co-curricular facilities",
    ],
    "stats": {
        "students": 1800,
        "faculty": 85,
        "programs": 24,
    },
}

API_NOTICES = [
    {
        "id": 1,
        "title": "UG admissions open for academic year 2026-27",
        "publishedOn": "2026-03-01",
        "category": "Admissions",
        "important": True,
    },
    {
        "id": 2,
        "title": "Internal assessment timetable released",
        "publishedOn": "2026-02-25",
        "category": "Academics",
        "important": False,
    },
    {
        "id": 3,
        "title": "Scholarship verification last date extended",
        "publishedOn": "2026-02-22",
        "category": "Scholarships",
        "important": True,
    },
]

API_EVENTS = [
    {
        "id": 101,
        "name": "Science Expo 2026",
        "date": "2026-03-20",
        "venue": "Main Auditorium",
        "description": "Student-led exhibition of research and innovation projects.",
    },
    {
        "id": 102,
        "name": "Campus Placement Drive",
        "date": "2026-03-28",
        "venue": "Seminar Hall",
        "description": "Recruitment drive with partner companies.",
    },
]

USERS = {
    "student1": {
        "password": "student123",
        "name": "Student One",
        "role": "student",
    },
    "staff1": {
        "password": "staff123",
        "name": "Staff One",
        "role": "staff",
    },
    "admin": {
        "password": "admin123",
        "name": "Administrator",
        "role": "admin",
    },
}

TOKEN_PREFIX = "jbac-token-"


def _extract_token() -> str:
    auth_header = request.headers.get("Authorization", "")
    if not auth_header.startswith("Bearer "):
        return ""
    return auth_header.split(" ", 1)[1].strip()


def _username_from_token(token: str) -> str:
    if not token.startswith(TOKEN_PREFIX):
        return ""
    return token.removeprefix(TOKEN_PREFIX)


@app.get("/api/health")
def api_health():
    return jsonify({"status": "ok"})


@app.get("/api/home")
def api_home():
    return jsonify(API_HOME)


@app.get("/api/notices")
def api_notices():
    return jsonify({"items": API_NOTICES})


@app.get("/api/events")
def api_events():
    return jsonify({"items": API_EVENTS})


@app.post("/api/contact")
def api_contact():
    payload = request.get_json(silent=True) or {}
    name = str(payload.get("name", "")).strip()
    email = str(payload.get("email", "")).strip()
    message = str(payload.get("message", "")).strip()

    if not name or not email or not message:
        return (
            jsonify(
                {
                    "error": "name, email, and message are required",
                }
            ),
            400,
        )

    return jsonify({"message": "Contact request submitted", "received": payload}), 201


@app.post("/api/login")
def api_login():
    payload = request.get_json(silent=True) or {}
    username = str(payload.get("username", "")).strip()
    password = str(payload.get("password", "")).strip()

    if not username or not password:
        return jsonify({"error": "username and password are required"}), 400

    user = USERS.get(username)
    if not user or user["password"] != password:
        return jsonify({"error": "invalid credentials"}), 401

    token = f"{TOKEN_PREFIX}{username}"
    return jsonify(
        {
            "token": token,
            "user": {
                "username": username,
                "name": user["name"],
                "role": user["role"],
            },
        }
    )


@app.get("/api/profile")
def api_profile():
    token = _extract_token()
    username = _username_from_token(token)
    user = USERS.get(username)

    if not user:
        return jsonify({"error": "unauthorized"}), 401

    return jsonify(
        {
            "username": username,
            "name": user["name"],
            "role": user["role"],
        }
    )


@app.route("/api/<path:path>")
def api_not_found(path):
    return jsonify({"error": f"Unknown API endpoint: /api/{path}"}), 404


@app.route("/")
def site_index():
    return send_from_directory(MIRROR_DIR, "index.html")


@app.route("/<path:path>")
def site_router(path):
    if path.startswith("api/"):
        abort(404)

    requested_path = MIRROR_DIR / path

    if requested_path.is_file():
        return send_from_directory(MIRROR_DIR, path)

    # SPA deep links (for example /about, /admissions) should render the same entry page.
    if not os.path.splitext(path)[1]:
        return send_from_directory(MIRROR_DIR, "index.html")

    abort(404)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
