from flask_login import UserMixin
from exts import db

class User_Ratings(UserMixin, db.Model):
    __tablename__ = 'User_Ratings'
    user_id = db.Column(db.Integer, primary_key=True, nullable=False)
    recipe_index = db.Column(db.Integer, primary_key=True, nullable=False)
    rating = db.Column(db.Float, nullable=False)