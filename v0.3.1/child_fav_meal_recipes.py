from flask_login import UserMixin
from exts import db

class Child_Fav_Meal_Recipes(UserMixin, db.Model):
    __tablename__ = 'Child_Fav_Meal_Recipes'
    child_id = db.Column(db.Integer, primary_key=True, nullable=False)
    recipe_index = db.Column(db.Integer, primary_key=True, nullable=False)
