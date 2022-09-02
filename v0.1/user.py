from flask import current_app
from flask_login import UserMixin
from authlib.jose import jwt
from exts import db

class User(UserMixin, db.Model):
    __tablename__ = 'User'
    user_id = db.Column(db.Integer, primary_key=True, nullable=False, autoincrement=True)
    username = db.Column(db.String(64), nullable=False)
    email = db.Column(db.String(64), nullable=False)
    password = db.Column(db.String(64), nullable=False)
    verification = db.Column(db.Boolean, default=0, nullable=False)

    def generate_active_token(self):
        header = {'alg': 'HS256'}
        key = current_app._get_current_object().config['SECRET_KEY']
        data = {'id': self.user_id}
        return jwt.encode(header=header, payload=data, key=key)

    # To adapt minins.py return text_type(self.id)
    @property
    def id(self):
        return self.user_id

    @staticmethod
    def check_activate_token(token):
        key = current_app._get_current_object().config['SECRET_KEY']
        try:
            data = jwt.decode(token, key)
        except:
            return False
        u = User.query.get(data['id'])
        if not u:
            return False
        if not u.verification:
            u.verification = 1
            db.session.add(u)
            db.session.commit()
            return True