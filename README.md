# jbac

Flask app that serves a mirrored static frontend from `mirror_site/`.

The backend is intentionally thin: it serves files from disk and supports SPA-style deep links by returning `mirror_site/index.html` for extensionless paths (for example `/about` or `/admissions`).

This repository now also includes:

- a JSON API surface under `/api/*`
- a Kotlin Android client in `android-app/` that consumes those APIs

## Current App Behavior

- `GET /` serves `mirror_site/index.html`
- `GET /<path>` serves a matching file from `mirror_site/` when it exists
- `GET /<path>` falls back to `mirror_site/index.html` when `<path>` has no file extension (SPA deep link)
- Missing file paths with extensions return `404`

## API Endpoints

Base URL (local): `http://127.0.0.1:5000`

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/api/health` | Health check |
| `GET` | `/api/home` | Home content and stats |
| `GET` | `/api/notices` | Notices list |
| `GET` | `/api/events` | Events list |
| `POST` | `/api/contact` | Submit contact form |
| `POST` | `/api/login` | Login and return bearer token |
| `GET` | `/api/profile` | Profile for authenticated user |

Example contact payload:

```json
{
	"name": "Ravi Kumar",
	"email": "ravi@example.com",
	"message": "I need admission details for B.Com"
}
```

Example login payload:

```json
{
	"username": "student1",
	"password": "student123"
}
```

### Demo API credentials

| Role | Username | Password |
|---|---|---|
| Student | `student1` | `student123` |
| Staff | `staff1` | `staff123` |
| Admin | `admin` | `admin123` |

## Project Structure

```text
.
|-- app.py
|-- android-app/
|-- Procfile
|-- requirements.txt
|-- mirror_site/
|   |-- index.html
|   |-- assets/
|   `-- ...
|-- static/
|-- templates/
`-- README.md
```

## Local Development

```bash
# 1. Create and activate a virtual environment (recommended)
python -m venv .venv
source .venv/bin/activate   # Windows: .venv\Scripts\activate

# 2. Install dependencies
pip install -r requirements.txt

# 3. Run the app
python app.py
```

App URL: `http://127.0.0.1:5000`

## Production Run

`Procfile` uses:

```bash
web: gunicorn app:app
```

Equivalent manual command:

```bash
gunicorn app:app
```

## Android App (Kotlin)

Android client source lives in `android-app/`.

### Features included

- Retrofit integration for `/api/home`, `/api/notices`, `/api/events`
- Login flow using `/api/login` and bearer-token requests
- Protected profile fetch via `/api/profile`
- Repository + ViewModel pattern
- Jetpack Compose UI for Login, Home, and Contact screens
- Navigation flow with logout support

### Open and run

1. Open `android-app/` in Android Studio.
2. Let Gradle sync complete.
3. Start the Flask backend (`python app.py`) on your machine.
4. Run the Android app on emulator/device.

At launch, sign in using one of the demo credentials above.

Default API base URL in Android is:

`http://10.0.2.2:5000/`

This works for Android Emulator when backend runs on host. If you use a physical device, update `BASE_URL` in `android-app/app/build.gradle.kts` to your machine's LAN IP.

## Notes

- The active runtime entrypoint is `app.py`.
- Static mirror content is served from `mirror_site/`.
- `templates/` and `static/` folders may exist for legacy or future server-rendered pages, but are not used by the current router.
