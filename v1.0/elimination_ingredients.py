class Elimination_Ingredients:
    egg = ['egg', 'mayonnaise', 'meringue', 'flavoprotein', 'hollandaise', 'custard', 'surimi', 'nougat' 'puddings', 'marshmallow', 'marzipan']
    soy = ['soy', 'peanut', 'cereal', 'sausage', 'natto', 'miso', 'tofu', 'bean curd', 'edamame', 'shoyo', 'tamari', 'vegetable gum']
    dairy = ['milk', 'cheese', 'yoghurt', 'butter', 'custard', 'ghee', 'galactose', 'cream', 'curd', 'caramel', 'lactate', 'lactulose', 'pudding', 'nougat']
    gluten = ['wheat', 'rye', 'barley', 'triticale', 'bread', 'pitta', 'bagel', 'flatbread', 'crouton', 'malt', 'yeast', 'fries', 'potato', 'soy', 'gravy', 'tortilla', 'cereal', 'granola', 'oats', 'spelt', 'noodle', 'durum', 'panko']
    fructose = ['fruit', 'apple', 'grapes', 'guava', 'mango', 'pear', 'raisin', 'kiwi', 'apricot', 'nectarine', 'papaya', 'raspberry', 'pineapple', 'artichoke', 'bean', 'broccoli', 'cabbage', 'pepper', 'leek', 'onion', 'shallots', 'shiitake', 'shitake', 'tomato', 'beetroot', 'broccoli', 'carrot', 'chickpea', 'eggplant', 'lentil', 'mung', 'sauerkraut', 'sweet potato', 'rye', 'wheat', 'peanut', 'cashew', 'almond', 'hazelnut', 'pecan', 'yoghurt', 'chutney', 'mustard', 'fructose', 'vineger', 'tiramisu', 'fruitcake', 'lasagne', 'pizza', 'pretzel', 'licorice', 'marshmallow', 'garlic', 'chilli', 'sucrose', 'molasses', 'teriyaki', 'guacamole']
    salicylate = ['apricot', 'cherry', 'cranberry', 'date', 'grapes', 'grapefruit', 'guava', 'orange', 'raspberry', 'redcurrent', 'strawberry', 'tangelo', 'apple', 'avocado', 'lychee', 'nectarine', 'passionfruit', 'peach', 'watermelon', 'fruit', 'broccoli', 'cauliflower', 'champignon', 'eggplant', 'gherkin', 'mushroom', 'olive', 'spinach', 'tomato', 'artichoke', 'corn', 'chilli', 'cucumber', 'radish', 'zucchini', 'almond', 'cheese', 'honey', 'liquorice', 'aniseed', 'cayenne', 'cinamon', 'ginger', 'coriander', 'curry', 'dill', 'miso', 'soy', 'anchovies', 'salami', 'chorizo', 'cabanna', 'cheese', 'mustard', 'oregano', 'rosemary']
    histamine = ['mushroom', 'cheese', 'tofu', 'soy', 'yeast', 'chocolate', 'smoke', 'salami', 'chorizo', 'ham', 'vinegar', 'crab', 'lobster', 'prawn', 'shellfish', 'cocoa', 'bean', 'pulse', 'papaya', 'wheat', 'kiwi', 'lemon', 'lime', 'pineapple', 'plum', 'spinach', 'sauerkraut', 'corn', 'grapefuit', 'banana', 'spelt', 'kamut', 'rye', 'oats', 'kefir', 'yoghurt', 'walnut', 'pistachio', 'almond', 'chickpea', 'aniseed', 'licorice', 'tomato', 'miso', 'pickles']
    FODMAPs = ['onion', 'apple', 'cheese', 'honey', 'milk', 'yoghurt', 'artichoke', 'bread', 'bean', 'lentils', 'chickpea', 'wheat', 'garlic', 'barley', 'lentil', 'rye', 'leek', 'pea', 'cashew', 'asparagus', 'beetroot', 'cauliflower', 'falafel', 'cabbage', 'mung', 'shallots', 'taro', 'cassava', 'celery', 'apricot', 'avocado', 'blackberry', 'boysenberry', 'boysenberries', 'cherry', 'cherries', 'dates', 'figs', 'goji', 'lychee', 'pear', 'peach', 'mango', 'persimmon', 'prunes', 'sultana', 'sausage', 'cake', 'croissant', 'muffin', 'udon', 'pasta', 'cream', 'custard', 'kefir']
    total = {'Eggs':egg, 'Soy':soy, 'Dairy':dairy, 'Gluten':gluten, 'Fructose':fructose, 'Salicylates':salicylate, 'Histamine':histamine, 'Fodmap':FODMAPs}
    
    def check_ingredients_exist(self, current_elimination_type, ingredients_list):
        for i in ingredients_list:
            for j in self.total.get(current_elimination_type):
                if i == j:
                    return 1
        return 0

    # when there is an ongoing elimination, check if current recommand list is ok, if there is elimination ingredient in a recipe, then kick it off from list. (ramain order)
    def check_meal_recipe_is_ok(self, current_elimination_type, recipe_list):
        return_recipe_list = []
        kick_out_list = []
        for i in recipe_list:
            for j in self.total.get(current_elimination_type):
                if (j in i.title) or (j in i.ingredients) or (j in i.instructions):
                    kick_out_list.append(i)
                    break
        return_recipe_set = set(recipe_list).difference(set(kick_out_list))
        for i in recipe_list:
            if i in return_recipe_set:
                return_recipe_list.append(i)
        return return_recipe_list