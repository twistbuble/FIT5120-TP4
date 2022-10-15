from flask_login import UserMixin
from exts import db

class User_Recommendations(UserMixin, db.Model):
    __tablename__ = 'User_Recommendations'
    user_id = db.Column(db.Integer, primary_key=True, nullable=False)
    recommendations = db.Column(db.String, nullable=False)