import random
import string
from datetime import datetime, timezone
from urllib.parse import urlparse

from flask import Flask, abort, redirect, render_template, request, url_for
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///links.db"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

db = SQLAlchemy(app)

SHORT_CODE_LENGTH = 6
MAX_URL_LENGTH = 2048
MAX_GENERATE_ATTEMPTS = 10
ALLOWED_SCHEMES = {"http", "https"}


class Link(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    original_url = db.Column(db.String(MAX_URL_LENGTH), nullable=False)
    short_code = db.Column(db.String(20), unique=True, nullable=False)
    created_at = db.Column(db.DateTime, nullable=False, default=lambda: datetime.now(timezone.utc))
    clicks = db.Column(db.Integer, nullable=False, default=0)


def generate_short_code(length=SHORT_CODE_LENGTH):
    characters = string.ascii_letters + string.digits
    for _ in range(MAX_GENERATE_ATTEMPTS):
        code = "".join(random.choices(characters, k=length))
        if not Link.query.filter_by(short_code=code).first():
            return code
    raise RuntimeError("Could not generate a unique short code. Try again later.")


def is_valid_url(url):
    try:
        parsed = urlparse(url)
        return parsed.scheme in ALLOWED_SCHEMES and bool(parsed.netloc)
    except Exception:
        return False


@app.route("/l", methods=["GET", "POST"])
def links():
    error = None
    success = None

    if request.method == "POST":
        original_url = request.form.get("url", "").strip()
        custom_code = request.form.get("custom_code", "").strip()

        if not original_url:
            error = "Please enter a URL."
        elif not is_valid_url(original_url):
            error = "URL must start with http:// or https://"
        elif len(original_url) > MAX_URL_LENGTH:
            error = f"URL must be {MAX_URL_LENGTH} characters or fewer."
        elif custom_code and not custom_code.isalnum():
            error = "Custom code must contain only letters and numbers."
        elif custom_code and Link.query.filter_by(short_code=custom_code).first():
            error = "That custom code is already taken."
        else:
            short_code = custom_code if custom_code else generate_short_code()
            link = Link(original_url=original_url, short_code=short_code)
            db.session.add(link)
            db.session.commit()
            short_url = request.host_url.rstrip("/") + "/" + short_code
            success = short_url

    all_links = Link.query.order_by(Link.created_at.desc()).all()
    return render_template("index.html", links=all_links, error=error, success=success)


@app.route("/<short_code>")
def redirect_link(short_code):
    link = Link.query.filter_by(short_code=short_code).first()
    if link is None:
        abort(404)
    if not is_valid_url(link.original_url):
        abort(400)
    link.clicks += 1
    db.session.commit()
    return redirect(link.original_url)


@app.route("/")
def index():
    return redirect(url_for("links"))


with app.app_context():
    db.create_all()

if __name__ == "__main__":
    app.run(debug=True)
