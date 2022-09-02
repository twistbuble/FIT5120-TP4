from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import extract, and_
from flask_mail import Mail
from flask_wtf.csrf import CSRFProtect
from flask_httpauth import HTTPBasicAuth
from flask_login import LoginManager

db = SQLAlchemy()
mail = Mail()
csrf = CSRFProtect()
auth = HTTPBasicAuth()
login = LoginManager()