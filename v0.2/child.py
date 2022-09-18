from flask_login import UserMixin
from exts import db

class Child(UserMixin, db.Model):
    __tablename__ = 'Child'
    user_id = db.Column(db.Integer, nullable=False)
    child_id = db.Column(db.Integer, primary_key=True, nullable=False, autoincrement=True)
    child_name = db.Column(db.String(16), nullable=False)
    child_age = db.Column(db.Integer, nullable=False)
    child_gender = db.Column(db.Integer, nullable=False)
    child_intolerance = db.Column(db.String(128), nullable=False)

    def get_child_dict(self):
        return {'child_id': self.child_id, 'child_name': self.child_name, 'child_age': self.child_age, 'child_gender': self.child_gender}