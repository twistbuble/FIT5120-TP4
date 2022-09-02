from flask_login import UserMixin
from exts import db

class Meal(UserMixin, db.Model):
    __tablename__ = 'Food_Diary'
    diary_id = db.Column(db.Integer, primary_key=True, nullable=False, autoincrement=True)
    child_id = db.Column(db.Integer, nullable=False)
    diary_datetime = db.Column(db.String(128), nullable=False)
    diary_food_name = db.Column(db.String(128), nullable=False)
    diary_food_description = db.Column(db.String(128), nullable=False)
    diary_mood = db.Column(db.Integer, nullable=False)
    diary_energy = db.Column(db.Integer, nullable=False)
    diary_report = db.Column(db.String(128), nullable=True)
    stomach_ache = db.Column(db.Integer, nullable=False)

    def get_meal(self):
        return {'meal_id': self.diary_id, 'meal_datetime': int(self.diary_datetime.timestamp() * 1000), 'meal_food_name': self.diary_food_name}

    def get_meal_details(self):
        return {'meal_id': self.diary_id, 'meal_datetime': int(self.diary_datetime.timestamp() * 1000), 'meal_food_name': self.diary_food_name, 'meal_food_description': self.diary_food_description, 'meal_mood': self.diary_mood, 'meal_energy':self.diary_energy, 'stomach_ache': self.stomach_ache}