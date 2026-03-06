# jbac

Flask app that serves a mirrored static frontend from `mirror_site/`.

The backend is intentionally thin: it serves files from disk and supports SPA-style deep links by returning `mirror_site/index.html` for extensionless paths (for example `/about` or `/admissions`).

## Current App Behavior

- `GET /` serves `mirror_site/index.html`
- `GET /<path>` serves a matching file from `mirror_site/` when it exists
- `GET /<path>` falls back to `mirror_site/index.html` when `<path>` has no file extension (SPA deep link)
- Missing file paths with extensions return `404`

## Project Structure

```text
.
|-- app.py
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

## Notes

- The active runtime entrypoint is `app.py`.
- Static mirror content is served from `mirror_site/`.
- `templates/` and `static/` folders may exist for legacy or future server-rendered pages, but are not used by the current router.
