import os

DEBUG = True
TEMPLATES_AUTO_RELOAD = True

# Secret key
SECRET_KEY = os.urandom(32)

# Mysql
msg = "mysql+pymysql://root:5177FC9E-8C6B@54.88.63.24:3306/food_intol"
# msg = "mysql+pymysql://root:5177FC9E-8C6B@127.0.0.1:3306/food_intol"
SQLALCHEMY_DATABASE_URI = msg
SQLALCHEMY_TRACK_MODIFICATIONS = False

# Mail
MAIL_SERVER = "smtp.163.com"
MAIL_PORT = 465
MAIL_USERNAME = "jassh_group@163.com"
MAIL_PASSWORD = "DNKJQHEGLJKGGNHW" # Web Password: E21819EB-A9F7
MAIL_USE_SSL = True
MAIL_USE_TLS = False
MAIL_DEFAULT_SENDER = "jassh_group@163.com"
SECURITY_EMAIL_SENDER = "jassh_group@163.com"

# Google auth
GOOGLE_LOGIN_CLIENT_ID = "<your-id-ending-with>.apps.googleusercontent.com"
GOOGLE_LOGIN_CLIENT_SECRET = "<your-secret>"
OAUTH_CREDENTIALS={
        'google': {
            'id': GOOGLE_LOGIN_CLIENT_ID,
            'secret': GOOGLE_LOGIN_CLIENT_SECRET
        }
}