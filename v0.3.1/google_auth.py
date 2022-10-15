import os, pathlib
from google_auth_oauthlib.flow import Flow

GOOGLE_CLIENT_ID = "717305002273-vn1lim3ldcdilldb8vl5i8urd2rlsqse.apps.googleusercontent.com"
client_secrets_file = os.path.join(pathlib.Path(__file__).parent, "client_secret.json")

flow = Flow.from_client_secrets_file(
    client_secrets_file=client_secrets_file,
    scopes=["https://www.googleapis.com/auth/userinfo.profile", "https://www.googleapis.com/auth/userinfo.email", "openid"],
    redirect_uri="https://api.jassh.ga/callback"
)