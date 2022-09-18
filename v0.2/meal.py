from flask_login import UserMixin
from exts import db

class Meal(UserMixin, db.Model):
    __tablename__ = 'Food_Diary'
    diary_id = db.Column(db.Integer, primary_key=True, nullable=False, autoincrement=True)
    child_id = db.Column(db.Integer, nullable=False)
    diary_datetime = db.Column(db.String(128), nullable=False)
    diary_food_name = db.Column(db.String(128), nullable=False)
    diary_food_description = db.Column(db.String(128))
    diary_ezcema = db.Column(db.Integer, nullable=False)
    diary_stomach_ache = db.Column(db.Integer, nullable=False)
    diary_nausea = db.Column(db.Integer, nullable=False)
    diary_respiratory = db.Column(db.Integer, nullable=False)
    diary_headaches = db.Column(db.Integer, nullable=False)
    diary_diarrhea = db.Column(db.Integer, nullable=False)
    diary_tiredness = db.Column(db.Integer, nullable=False)
    diary_bloating = db.Column(db.Integer, nullable=False)
    diary_joint_pain = db.Column(db.Integer, nullable=False)
    diary_additional_symptoms = db.Column(db.String(64))
    diary_food_type = db.Column(db.Integer, nullable=False)

    def get_meal(self):
        return {
            'meal_id': self.diary_id, 
            'meal_datetime': int(self.diary_datetime.timestamp() * 1000), 
            'meal_food_name': self.diary_food_name, 
            'meal_food_type': self.diary_food_type
            }

    def get_meal_details(self):
        return {
            'meal_id': self.diary_id, 
            'meal_datetime': int(self.diary_datetime.timestamp() * 1000), 
            'meal_food_name': self.diary_food_name, 
            'meal_food_description': self.diary_food_description, 
            'diary_ezcema': self.diary_ezcema, 
            'stomach_ache': self.diary_stomach_ache, 
            'nausea': self.diary_nausea, 
            'respiratory': self.diary_respiratory, 
            'headaches': self.diary_headaches, 
            'diarrhea': self.diary_diarrhea, 
            'tiredness': self.diary_tiredness, 
            'bloating': self.diary_bloating, 
            'joint_pain': self.diary_joint_pain, 
            'additional_symptoms': self.diary_additional_symptoms, 
            'meal_food_type': self.diary_food_type
            }