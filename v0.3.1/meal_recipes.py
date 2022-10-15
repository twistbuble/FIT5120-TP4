from flask_login import UserMixin
from exts import db

class Meal_Recipes(UserMixin, db.Model):
    __tablename__ = 'Meal_Recipes'
    recipe_index = db.Column(db.Integer, primary_key=True, nullable=False)
    title = db.Column(db.String(64), nullable=False)
    ingredients = db.Column(db.String(1024), nullable=False)
    instructions = db.Column(db.String(1024), nullable=False)
    count = db.Column(db.Integer, nullable=False)
    ratings = db.Column(db.Float, nullable=False)
    image_name = db.Column(db.String(64), nullable=False)
    is_recommended = 0

    def get_recipe(self, fav_recipe_id_list):
        return {
            'favourite': int(self.recipe_index in fav_recipe_id_list), 
            'recommended': self.is_recommended, 
            'recipe_id': self.recipe_index, 
            'recipe_title': self.title, 
            'ratings_count': self.count, 
            'ratings': self.ratings, 
            'recipe_image_link': 'https://54.88.63.24/' + self.image_name.strip('\r') + '.jpg'
        }

    def get_recipe_detail(self):
        return {
            'recipe_id': self.recipe_index, 
            'recipe_title': self.title, 
            'ratings_count': self.count, 
            'ratings': self.ratings, 
            'ingredients': self.ingredients, 
            'cooking_instructions': self.instructions,
            'recipe_image_link': 'https://54.88.63.24/' + self.image_name.strip('\r') + '.jpg'
        }
