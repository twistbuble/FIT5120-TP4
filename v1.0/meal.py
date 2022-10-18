import base64
from flask_login import UserMixin
from numpy import empty
from exts import db

class Meal(UserMixin, db.Model):
    __tablename__ = 'Food_Diary'
    diary_id = db.Column(db.Integer, primary_key=True, nullable=False, autoincrement=True)
    child_id = db.Column(db.Integer, nullable=False)
    diary_datetime = db.Column(db.String(128), nullable=False)
    diary_food_name = db.Column(db.String(128), nullable=False)
    diary_food_description = db.Column(db.String(128))
    diary_eczema = db.Column(db.Integer, nullable=False)
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
    diary_pic = db.Column(db.BLOB)

    def get_meal(self):
        return {
            'meal_id': self.diary_id, 
            'meal_datetime': int(self.diary_datetime.timestamp() * 1000), 
            'meal_food_name': self.diary_food_name, 
            'meal_food_type': self.diary_food_type
            }

    def get_meal_details(self):
        if self.diary_pic != None:
            image = str(base64.b64encode(self.diary_pic), encoding='utf-8')
        else:
            image = ''
        if self.diary_additional_symptoms == None:
            self.diary_additional_symptoms = ''
        if self.diary_food_description == None:
            self.diary_food_description = ''
        return {
            'meal_id': self.diary_id, 
            'meal_datetime': int(self.diary_datetime.timestamp() * 1000), 
            'meal_food_name': self.diary_food_name, 
            'meal_food_description': self.diary_food_description, 
            'eczema': self.diary_eczema, 
            'stomach_ache': self.diary_stomach_ache, 
            'nausea': self.diary_nausea, 
            'respiratory': self.diary_respiratory, 
            'headaches': self.diary_headaches, 
            'diarrhea': self.diary_diarrhea, 
            'tiredness': self.diary_tiredness, 
            'bloating': self.diary_bloating, 
            'joint_pain': self.diary_joint_pain, 
            'additional_symptoms': self.diary_additional_symptoms, 
            'meal_food_type': self.diary_food_type,
            'image': image
            }

    def get_meal_type(self):
        if self.diary_food_type == 0:
            return 'breakfast'
        elif self.diary_food_type == 1:
            return 'lunch'
        elif self.diary_food_type == 2:
            return 'dinner'
        elif self.diary_food_type == 3:
            return 'snack'
        else:
            return 'unknown'

    def get_symptoms(self):
        symp_list = []
        if self.diary_eczema == 1:
            symp_list.append('eczema')
        if self.diary_stomach_ache == 1:
            symp_list.append('stomach ache')
        if self.diary_nausea == 1:
            symp_list.append('nausea')
        if self.diary_respiratory == 1:
            symp_list.append('respiratory')
        if self.diary_headaches == 1:
            symp_list.append('headaches')
        if self.diary_diarrhea == 1:
            symp_list.append('diarrhea')
        if self.diary_tiredness == 1:
            symp_list.append('tiredness')
        if self.diary_bloating == 1:
            symp_list.append('bloating')
        if self.diary_joint_pain == 1:
            symp_list.append('joint pain')
        if len(symp_list) == 0:
            str = None
        else:
            str = ', '.join(symp_list)
        return str

    def get_meal_pic(self, filepath):
        if self.diary_pic == None:
            return './templates/empty.png'
        pic_file = open(filepath, 'ab')
        pic_file.write(self.diary_pic)
        pic_file.close()
        return filepath