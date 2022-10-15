from flask_login import UserMixin
from exts import db

class Child_Food_Elimination(UserMixin, db.Model):
    __tablename__ = 'Child_Food_Elimination'
    child_id = db.Column(db.Integer, nullable=False)
    food_elimination_id = db.Column(db.Integer, primary_key=True, nullable=False)
    food_elimination_name = db.Column(db.String(128), nullable=False)
    food_elimination_begin_date = db.Column(db.String(128), nullable=False)
    food_elimination_duration = db.Column(db.Integer, nullable=False)

    def is_current_ongoing(self):
        import time
        food_elimination_end_date = int((self.food_elimination_begin_date.timestamp() + 24 * 60 * 60 * self.food_elimination_duration) * 1000)
        if food_elimination_end_date - time.time() * 1000 <= 0:
            return False
        else:
            return True

    def get_diet_info(self, food_elimination_type):
        import time
        food_elimination_start_date = int(self.food_elimination_begin_date.timestamp() * 1000)
        food_elimination_end_date = int((self.food_elimination_begin_date.timestamp() + 24 * 60 * 60 * self.food_elimination_duration) * 1000)
        return {
            'food_elimination_type': food_elimination_type, 
            'food_elimination_name': self.food_elimination_name, 
            'food_elimination_start_date': food_elimination_start_date, 
            'food_elimination_end_date': food_elimination_end_date, 
            'ongoing_food_elimination': int(food_elimination_start_date < time.time() * 1000 < food_elimination_end_date)
        }

    def get_time_remaining(self):
        import time
        food_elimination_start_date = int(self.food_elimination_begin_date.timestamp() * 1000)
        food_elimination_end_date = int((self.food_elimination_begin_date.timestamp() + 24 * 60 * 60 * self.food_elimination_duration) * 1000)
        food_elimination_time_remaining = food_elimination_end_date - time.time() * 1000
        if food_elimination_time_remaining <= 0:
            food_elimination_time_remaining = 0
        return {
            'food_elimination_name': self.food_elimination_name, 
            'food_elimination_start_date': food_elimination_start_date, 
            'food_elimination_end_date': food_elimination_end_date, 
            'food_elimination_time_remaining': food_elimination_time_remaining
        }