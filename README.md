# jbac

A Python/Flask web application that replicates the **Jyothi Bharathi Adarsha College (jbac.in)** website.

## Pages

| Route | Description |
|-------|-------------|
| `/` | Home – hero, stats, news ticker, notices & events |
| `/about` | About – history, vision/mission, achievements, management |
| `/courses` | Courses – UG, PG and certificate programmes |
| `/admissions` | Admissions – eligibility, documents, fees, key dates |
| `/faculty` | Faculty – department-wise staff directory |
| `/events` | Events – upcoming & past events |
| `/notices` | Notices & Announcements |
| `/gallery` | Photo gallery with category filter |
| `/contact` | Contact form and college address |
| `/login` | Student / Staff login |
| `/dashboard` | Authenticated user dashboard |

## Quick Start

```bash
# 1. Create & activate a virtual environment (optional but recommended)
python -m venv .venv
source .venv/bin/activate   # Windows: .venv\Scripts\activate

# 2. Install dependencies
pip install -r requirements.txt

# 3. Run the development server
python app.py
```

Open http://127.0.0.1:5000 in your browser.

### Demo credentials

| Role    | Username  | Password    |
|---------|-----------|-------------|
| Student | student1  | student123  |
| Staff   | staff1    | staff123    |
| Admin   | admin     | admin123    |

## Configuration

| Environment variable | Default | Purpose |
|---------------------|---------|---------|
| `SECRET_KEY` | `jbac-secret-key-change-in-production` | Flask session secret |
| `DATABASE_URL` | `sqlite:///jbac.db` | SQLAlchemy DB URI |
| `FLASK_DEBUG` | `0` | Set to `1` to enable debug mode |

## Tech Stack

- **Flask** – web framework
- **Flask-SQLAlchemy** – ORM / SQLite database
- **Werkzeug** – password hashing
- **Bootstrap 5** – UI (bundled locally, no CDN required)
- **Bootstrap Icons** – icon font (bundled locally)
