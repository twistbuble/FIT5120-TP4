from flask_login import UserMixin
from exts import db

class Meal_Recipes(UserMixin, db.Model):
    __tablename__ = 'Meal_Recipes'
    recipe_index = db.Column(db.Integer, primary_key=True, nullable=False)
    title = db.Column(db.String(64))
    ingredients = db.Column(db.String(1024))
    instructions = db.Column(db.String(1024))
    counter = db.Column(db.Integer)
    image_name = db.Column(db.String(64))

    def get_recipe(self, fav_recipe_id_list):
        return {
            'favourite': int(self.recipe_index in fav_recipe_id_list), 
            'recipe_id': self.recipe_index, 
            'recipe_title': self.title, 
            'recommend_counter': self.counter, 
            'image_url': 'http://jassh.ga/' + self.image_name.strip('\r') + '.jpg'
        }

    def get_recipe_detail(self):
        return {
            'recipe_id': self.recipe_index, 
            'recipe_title': self.title, 
            'recommend_counter': self.counter, 
            'ingredients': self.ingredients, 
            'cooking_instructions': self.instructions,
            'image_url': 'http://jassh.ga/' + self.image_name.strip('\r') + '.jpg'
        }
