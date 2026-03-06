import os
from pathlib import Path

from flask import Flask, abort, send_from_directory

app = Flask(__name__)

MIRROR_DIR = Path(__file__).parent / "mirror_site"


@app.route("/")
def site_index():
    return send_from_directory(MIRROR_DIR, "index.html")


@app.route("/<path:path>")
def site_router(path):
    requested_path = MIRROR_DIR / path

    if requested_path.is_file():
        return send_from_directory(MIRROR_DIR, path)

    # SPA deep links (for example /about, /admissions) should render the same entry page.
    if not os.path.splitext(path)[1]:
        return send_from_directory(MIRROR_DIR, "index.html")

    abort(404)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
