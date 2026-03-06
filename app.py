import os
from datetime import datetime, timezone
from functools import wraps

from flask import Flask, flash, redirect, render_template, request, session, url_for
from flask_sqlalchemy import SQLAlchemy
from werkzeug.security import check_password_hash, generate_password_hash

app = Flask(__name__)
app.config["SECRET_KEY"] = os.environ.get("SECRET_KEY", "jbac-secret-key-change-in-production")
app.config["SQLALCHEMY_DATABASE_URI"] = os.environ.get("DATABASE_URL", "sqlite:///jbac.db")
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

db = SQLAlchemy(app)

# ---------------------------------------------------------------------------
# Models
# ---------------------------------------------------------------------------

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    password_hash = db.Column(db.String(200), nullable=False)
    role = db.Column(db.String(20), nullable=False, default="student")  # student / staff / admin
    full_name = db.Column(db.String(120), nullable=False, default="")
    email = db.Column(db.String(120), unique=True, nullable=False)
    created_at = db.Column(db.DateTime, nullable=False, default=lambda: datetime.now(timezone.utc))

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)


class Notice(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(200), nullable=False)
    content = db.Column(db.Text, nullable=False)
    category = db.Column(db.String(50), nullable=False, default="General")
    date = db.Column(db.DateTime, nullable=False, default=lambda: datetime.now(timezone.utc))
    is_important = db.Column(db.Boolean, nullable=False, default=False)


class Event(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(200), nullable=False)
    description = db.Column(db.Text, nullable=False)
    event_date = db.Column(db.DateTime, nullable=False)
    location = db.Column(db.String(200), nullable=False, default="College Campus")
    image_url = db.Column(db.String(300), nullable=True)


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def login_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        if "user_id" not in session:
            flash("Please log in to access this page.", "warning")
            return redirect(url_for("login", next=request.url))
        return f(*args, **kwargs)
    return decorated


def get_current_user():
    if "user_id" in session:
        return db.session.get(User, session["user_id"])
    return None


# ---------------------------------------------------------------------------
# Seed data
# ---------------------------------------------------------------------------

def seed_data():
    if User.query.count() == 0:
        admin = User(username="admin", role="admin", full_name="Administrator", email="admin@jbac.in")
        admin.set_password("admin123")
        student = User(username="student1", role="student", full_name="Arjun Sharma", email="arjun@jbac.in")
        student.set_password("student123")
        staff = User(username="staff1", role="staff", full_name="Dr. Priya Patel", email="priya@jbac.in")
        staff.set_password("staff123")
        db.session.add_all([admin, student, staff])

    if Notice.query.count() == 0:
        notices = [
            Notice(
                title="Admission Open 2025-26",
                content="Applications are invited for all undergraduate and postgraduate programmes for the academic year 2025-26. Last date to apply: 30th June 2025.",
                category="Admissions",
                is_important=True,
            ),
            Notice(
                title="Annual Sports Meet – Registration",
                content="Students wishing to participate in the Annual Sports Meet should register with their respective Physical Education teacher before 15th March 2025.",
                category="Sports",
            ),
            Notice(
                title="Internal Assessment Schedule",
                content="The internal assessment examinations for all departments will be held from 10th March to 20th March 2025. Time-tables are available on the notice board.",
                category="Examinations",
                is_important=True,
            ),
            Notice(
                title="National Scholarship Portal – Last Date Extended",
                content="The last date for submitting applications on the National Scholarship Portal has been extended to 31st March 2025.",
                category="Scholarships",
            ),
            Notice(
                title="Library Holiday Notice",
                content="The college library will remain closed on 12th March 2025 on account of Holi. It will resume normal timings from 13th March 2025.",
                category="General",
            ),
        ]
        db.session.add_all(notices)

    if Event.query.count() == 0:
        events = [
            Event(
                title="Annual College Day Celebrations",
                description="Join us for the grand Annual College Day Celebrations with cultural performances, prize distributions, and a guest lecture by a distinguished alumnus.",
                event_date=datetime(2025, 3, 28),
                location="College Auditorium",
            ),
            Event(
                title="Science Exhibition 2025",
                description="Students from all departments will showcase their innovative projects and research work at the Science Exhibition.",
                event_date=datetime(2025, 4, 5),
                location="Main Hall",
            ),
            Event(
                title="Career Guidance Seminar",
                description="A career guidance seminar will be conducted for final-year students by experts from various industries.",
                event_date=datetime(2025, 3, 18),
                location="Seminar Hall",
            ),
            Event(
                title="Intercollegiate Cultural Fest – JBAC Utsav",
                description="JBAC Utsav is our flagship intercollegiate cultural festival featuring competitions in music, dance, drama, and fine arts.",
                event_date=datetime(2025, 4, 20),
                location="Open Air Theatre",
            ),
        ]
        db.session.add_all(events)

    db.session.commit()


# ---------------------------------------------------------------------------
# Context processors
# ---------------------------------------------------------------------------

@app.context_processor
def inject_globals():
    return {"current_user": get_current_user(), "now": datetime.now()}


# ---------------------------------------------------------------------------
# Routes – Public pages
# ---------------------------------------------------------------------------

@app.route("/")
def index():
    notices = Notice.query.order_by(Notice.date.desc()).limit(5).all()
    events = Event.query.order_by(Event.event_date).filter(Event.event_date >= datetime.now()).limit(4).all()
    return render_template("index.html", notices=notices, events=events)


@app.route("/about")
def about():
    return render_template("about.html")


@app.route("/courses")
def courses():
    return render_template("courses.html")


@app.route("/admissions")
def admissions():
    return render_template("admissions.html")


@app.route("/faculty")
def faculty():
    return render_template("faculty.html")


@app.route("/events")
def events():
    all_events = Event.query.order_by(Event.event_date.desc()).all()
    return render_template("events.html", events=all_events)


@app.route("/notices")
def notices():
    all_notices = Notice.query.order_by(Notice.date.desc()).all()
    return render_template("notices.html", notices=all_notices)


@app.route("/gallery")
def gallery():
    return render_template("gallery.html")


@app.route("/contact")
def contact():
    return render_template("contact.html")


# ---------------------------------------------------------------------------
# Routes – Authentication
# ---------------------------------------------------------------------------

@app.route("/login", methods=["GET", "POST"])
def login():
    if "user_id" in session:
        return redirect(url_for("dashboard"))

    error = None
    if request.method == "POST":
        username = request.form.get("username", "").strip()
        password = request.form.get("password", "").strip()
        user = User.query.filter_by(username=username).first()
        if user and user.check_password(password):
            session["user_id"] = user.id
            session["role"] = user.role
            flash(f"Welcome back, {user.full_name}!", "success")
            next_url = request.form.get("next") or url_for("dashboard")
            return redirect(next_url)
        error = "Invalid username or password. Please try again."

    return render_template("login.html", error=error)


@app.route("/logout")
def logout():
    session.clear()
    flash("You have been logged out.", "info")
    return redirect(url_for("index"))


# ---------------------------------------------------------------------------
# Routes – Authenticated pages
# ---------------------------------------------------------------------------

@app.route("/dashboard")
@login_required
def dashboard():
    user = get_current_user()
    notices = Notice.query.order_by(Notice.date.desc()).limit(5).all()
    events = Event.query.order_by(Event.event_date).filter(Event.event_date >= datetime.now()).limit(3).all()
    return render_template("dashboard.html", user=user, notices=notices, events=events)


# ---------------------------------------------------------------------------
# App initialisation
# ---------------------------------------------------------------------------

with app.app_context():
    db.create_all()
    seed_data()

if __name__ == "__main__":
    debug = os.environ.get("FLASK_DEBUG", "0") == "1"
    app.run(debug=debug)
