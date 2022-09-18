from flask_login import UserMixin
from exts import db

class Food_Elimination(UserMixin, db.Model):
    __tablename__ = 'Food_Elimination'
    food_id = db.Column(db.Integer, primary_key=True, nullable=False)
    food_name = db.Column(db.String(64), nullable=False)
    food_elimination_duration = db.Column(db.Integer, nullable=False)