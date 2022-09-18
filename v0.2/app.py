import datetime
import functools
import hashlib
import re
from child_fav_meal_recipes import Child_Fav_Meal_Recipes
import config
from flask import Flask, request
from flask_login import login_user, logout_user, login_required, current_user
from sqlalchemy import extract, and_
from exts import mail, db, login
from google_auth import OAuthSignIn
from meal_recipes import Meal_Recipes
from user import User
from child import Child
from meal import Meal
from food_elimination import Food_Elimination
from child_food_elimination import Child_Food_Elimination
from mail_module import send_mail, is_valid_email

app = Flask(__name__)
app.config.from_object(config)
mail.init_app(app)
login.init_app(app)
db.init_app(app)

def login_required(func):
    @functools.wraps(func)
    def decorated_view(*args, **kwargs):
        if not current_user.is_authenticated:
            return {'status': 401, 'message': 'You need login to access this api.'}
        else:
            pass
        return func(*args, **kwargs)
    return decorated_view

@login.user_loader
def load_user(id):
    return User.query.get(int(id))

@app.route('/')
def home():
   return {'status': 403, 'message': 'AWAY FROM ME!'}

@app.route('/api/register', methods=['POST'])
def register():
    try:
        json_data = request.json
        username = json_data['username']
        password = json_data['password']
        passwordR = json_data['passwordR']
        email = json_data['email']
    except:
        return {'status': 400, 'message': 'Invalid input.'}
        
    if len(username) < 4 or len(username) > 32:
        return {'status': 400, 'message': 'The username should be at least 4 characters (no more than 32 characters).'}
    elif len(password) < 8 or len(password) > 32:
        return {'status': 400, 'message': 'The password should be at least 8 characters (no more than 32 characters).'}
    elif passwordR != password:
        return {'status': 400, 'message': 'The passwords entered are inconsistent.'}
    elif is_valid_email(email) != True:
        return {'status': 400, 'message': 'The email address is not in correct format.'}
    else:
        try:
            user = User(username=username, email=email, password=hashlib.sha1(password.encode('utf-8')).hexdigest())
            if user.query.filter(User.username==username).count() != 0:
                return {'status': 202, 'message': 'This username is already exists.'}
            else:
                db.session.add(user)
                db.session.commit()
                token = user.generate_active_token()
                send_mail(user.email, 'Account activate', 'activate', username=user.username, token=token)
                return {'status': 200, 'message': 'Registration succeeded, please click activate link in your mailbox.'}
        except:
            return {'status': 500, 'message': 'Email sending failed.'}

@app.route('/api/activate/<token>', methods=['GET'])
def activate(token):
    if User.check_activate_token(token):
        return {'status': 200, 'message': 'Your account is activated.'}
    else:
        return {'status': 403, 'message': 'Account activation failed.'}

@app.route('/authorize/<provider>')
def oauth_authorize(provider):
    # Flask-Login function
    if not current_user.is_anonymous:
        return 'No'
    oauth = OAuthSignIn.get_provider(provider)
    return oauth.authorize()

@app.route('/callback/<provider>')
def oauth_callback(provider):
    if not current_user.is_anonymous:
        return 'anonymous'
    oauth = OAuthSignIn.get_provider(provider)
    username, email = oauth.callback()
    if email is None:
        # I need a valid email address for my user identification
        return 'Need email'
    # Look if the user already exists
    user = User.query.filter_by(email=email).first()
    if not user:
        # Create the user. Try and use their name returned by Google,
        # but if it is not set, split the email address at the @.
        nickname = username
        if nickname is None or nickname == "":
            nickname = email.split('@')[0]
        # We can do more work here to ensure a unique nickname, if you 
        # require that.
        user = User(nickname=nickname, email=email)
        db.session.add(user)
        db.session.commit()
    # Log in the user, by default remembering them for their next visit
    # unless they log out.
    login_user(user, remember=True)
    return 'Yes'

@app.route('/api/login', methods=['POST'])
def login():
    try:
        json_data = request.json
        username = json_data['username']
        password = json_data['password']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        this_user = User.query.filter_by(username=username, password=hashlib.sha1(password.encode('utf-8')).hexdigest()).first()
        if not this_user:
            return {'status': 202, 'message': 'Wrong username or password.'}
        else:
            login_user(this_user, remember=True)
            return {'status': 200, 'message': 'Login successfully.', 'user_id': this_user.user_id, 'username': this_user.username}
    except:
        return {'status': 500, 'message': 'Login faild.'}

@app.route('/api/logout')
@login_required
def logout():
   logout_user()
   return {'status': 200, 'message': 'Logout successfully.'}

@app.route('/api/user_profile', methods=['GET'])
# @login_required
def get_user_profile():
    try:
        user = User.query.filter_by(user_id=1234).first()
        children = Child.query.filter_by(user_id=1234).all()
        if not children:
            return {'status': 200, 'message': 'Success.', 'username':user.username , 'children_count': 0}
        else:
            children_list = []
            for i in range(0, len(children)):
                children_list.append(children[i].get_child_dict())
            return {'status': 200, 'message': 'Success.', 'username':user.username, 'children_count': len(children), 'children_list': children_list}
    except:
        return {'status': 500, 'message': 'Getting user profile faild.'}

@app.route('/api/get_children', methods=['GET'])
# @login_required
def get_children():
    try:
        children = Child.query.filter_by(user_id=1234).all()
        if not children:
            return {'status': 202, 'message': 'You have not added children.'}
        else:
            children_list = []
            for i in range(0, len(children)):
                children_list.append(children[i].get_child_dict())
            return {'status': 200, 'message': 'Success.', 'children_count': len(children), 'children_list': children_list}
    except:
        return {'status': 500, 'message': 'Getting child list faild.'}

@app.route('/api/add_child', methods=['POST'])
# @login_required
def add_child():
    try:
        json_data = request.json
        gender = json_data['gender']
        name = json_data['name']
        age = json_data['age']
        intolerance = json_data['intolerance']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        child = Child(child_gender=int(gender), child_name=name, child_age=int(age), child_intolerance = intolerance, user_id=1234)
        db.session.add(child)
        db.session.commit()
        return {'status': 200, 'message': 'Adding child successful.'}
    except:
        return {'status': 500, 'message': 'Adding child faild.'}

@app.route('/api/del_child', methods=['POST'])
# @login_required
def del_child():
    try:
        json_data = request.json
        child_id = json_data['child_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}
    
    try:
        Meal.query.filter_by(child_id=int(child_id)).delete()
        Child.query.filter_by(child_id=int(child_id)).delete()
        db.session.commit()
        return {'status': 200, 'message': 'Deleting child successful.'}
    except:
        return {'status': 500, 'message': 'Deleting child faild.'}

@app.route('/api/get_child_profile', methods=['POST'])
# @login_required
def get_child_profile():
    try:
        json_data = request.json
        child_id = json_data['child_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        child = Child.query.filter_by(child_id=int(child_id)).all()
        if not child:
            return {'status': 202, 'message': 'This child_id is not exists.'}
        else:
            child_name = child[0].child_name
            child_age = child[0].child_age
            current_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
            meal_history_info = Meal.query.filter_by(child_id=child_id).all()
            current_elimination_list = []
            ezcema_count = 0
            stomach_ache_count = 0
            nausea_count = 0
            headache_count = 0
            respiratory_problem_count = 0
            diarrhoea_count = 0
            tiredness_count = 0
            bloating_count = 0
            joint_pain_count = 0
            additional_symptoms_count = 0
            for i in range(0, len(current_elimination)):
                current_elimination_list.append(current_elimination[i].get_time_remaining())
            for i in range(0, len(meal_history_info)):
                if meal_history_info[i].diary_ezcema == 1:
                    ezcema_count += 1
                if meal_history_info[i].diary_stomach_ache == 1:
                    stomach_ache_count += 1
                if meal_history_info[i].diary_nausea == 1:
                    nausea_count += 1
                if meal_history_info[i].diary_headaches == 1:
                    headache_count += 1
                if meal_history_info[i].diary_respiratory == 1:
                    respiratory_problem_count += 1
                if meal_history_info[i].diary_diarrhea == 1:
                    diarrhoea_count += 1
                if meal_history_info[i].diary_tiredness == 1:
                    tiredness_count += 1
                if meal_history_info[i].diary_bloating == 1:
                    bloating_count += 1
                if meal_history_info[i].diary_joint_pain == 1:
                    joint_pain_count += 1
                if meal_history_info[i].diary_additional_symptoms == 1:
                    additional_symptoms_count += 1
            return {'status': 200, 'message': 'Success.', 
            'child_name': child_name, 
            'child_age': child_age, 
            'current_elimination_list': current_elimination_list, 
            'ezcema_count': ezcema_count, 
            'stomach_ache_count': stomach_ache_count, 
            'nausea_count': nausea_count, 
            'headache_count': headache_count, 
            'respiratory_problem_count': respiratory_problem_count, 
            'diarrhoea_count': diarrhoea_count, 
            'tiredness_count': tiredness_count, 
            'bloating_count': bloating_count, 
            'joint_pain_count': joint_pain_count, 
            'additional_symptoms_count': additional_symptoms_count
            }
    except:
        return {'status': 500, 'message': 'Getting child profile faild.'}

                

@app.route('/api/get_diary', methods=['POST'])
# @login_required
def get_diary():
    try:
        json_data = request.json
        child_id = json_data['child_id']
        timestamp = int(str(json_data['timestamp'])[0:10])
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        meals = Meal.query.filter(Meal.child_id==int(child_id), and_(extract('year', Meal.diary_datetime) == datetime.datetime.fromtimestamp(timestamp).year, extract('month', Meal.diary_datetime) == datetime.datetime.fromtimestamp(timestamp).month, extract('day', Meal.diary_datetime) == datetime.datetime.fromtimestamp(timestamp).day)).all()
        if not meals:
            return {'status': 202, 'message': 'Empty diary of this date.'}
        else:
            diary_list = []
            for i in range(0, len(meals)):
                diary_list.append(meals[i].get_meal_details())
            return {'status': 200, 'message': 'Success.', 'meal_list': diary_list}
    except:
        return {'status': 500, 'message': 'Getting diary faild.'}

@app.route('/api/add_meal_to_diary', methods=['POST'])
# @login_required
def add_meal_to_diary():
    try:
        json_data = request.json
        child_id = json_data['child_id']
        food_type = json_data['food_type']
        food_name = json_data['food_name']
        food_description = json_data['food_description']
        ezcema = json_data['ezcema']
        stomach_ache = json_data['stomach_ache']
        respiratory = json_data['respiratory']
        headaches = json_data['headaches']
        diarrhea = json_data['diarrhea']
        tiredness = json_data['tiredness']
        bloating = json_data['bloating']
        joint_pain = json_data['joint_pain']
        if json_data['additional_symptoms'] == 0:
            additional_symptoms = ''
        else:
            additional_symptoms = json_data['additional_symptoms']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        meal = Meal(child_id=child_id, diary_food_name=food_name, diary_food_description=food_description, diary_ezcema=ezcema, diary_stomach_ache=stomach_ache, diary_respiratory=respiratory, diary_headaches=headaches, diary_diarrhea=diarrhea, diary_tiredness=tiredness, diary_bloating=bloating, diary_joint_pain=joint_pain, diary_additional_symptoms=additional_symptoms, diary_food_type=food_type)
        db.session.add(meal)
        db.session.commit()
        return {'status': 200, 'message': 'Adding meal to diary successful.'}
    except:
        return {'status': 500, 'message': 'Adding meal to diary faild.'}

@app.route('/api/view_meal_details', methods=['POST'])
# @login_required
def view_meal_details():
    try:
        json_data = request.json
        id = json_data['meal_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        meal = Meal.query.filter_by(diary_id=int(id)).all()
        if not meal:
            return {'status': 202, 'message': 'This meal id is not exists.'}
        else:
            return {'status': 200, 'message': 'Success.', 'meal_details': meal[0].get_meal_details()}
    except:
        return {'status': 500, 'message': 'Query meal details faild.'}

@app.route('/api/add_food_elimination', methods=['POST'])
# @login_required
def add_food_elimination():
    try:
        json_data = request.json
        child_id = json_data['child_id']
        food_intolerance_name = json_data['food_intolerance_name']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        existing_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        current_ongoing = False
        for i in range(0, len(existing_food_elimination)):
            if existing_food_elimination[i].is_current_ongoing():
                current_ongoing = True
                break
        if not current_ongoing:
            food_elimination = Food_Elimination.query.filter_by(food_name=food_intolerance_name).all()
            if not food_elimination:
                return {'status': 202, 'message': 'No data for this food.'}
            else:
                child_food_elimination = Child_Food_Elimination(child_id=child_id, food_elimination_name=food_elimination[0].food_name, food_elimination_duration=food_elimination[0].food_elimination_duration)
                db.session.add(child_food_elimination)
                db.session.commit()
                return {'status': 200, 'message': 'Adding food elimination successful.'}
        else:
            return {'status': 202, 'message': 'There is an elimination ongoing.'}
    except:
        return {'status': 500, 'message': 'Adding food elimination faild.'}

@app.route('/api/load_food_elimination_diets', methods=['POST'])
# @login_required
def load_food_elimination_diets():
    try:
        json_data = request.json
        child_id = json_data['child_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        if not child_food_elimination:
            return {'status': 202, 'message': 'Empty record of current child.'}
        else:
            child_food_elimination_list = []
            for i in range(0, len(child_food_elimination)):
                child_food_elimination_list.append(child_food_elimination[i].get_diet_info())
            return {'status': 200, 'message': 'Success.', 'child_food_elimination_list': child_food_elimination_list}
    except:
        return {'status': 500, 'message': 'Loading food elimination diets faild.'}

@app.route('/api/add_favourite_recipe', methods=['POST'])
# @login_required
def add_favourite_recipe():
    try:
        json_data = request.json
        child_id = json_data['child_id']
        recipe_id = json_data['recipe_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not Meal_Recipes.query.filter_by(recipe_index=recipe_id).all():
            return {'status': 202, 'message': 'No recipe for this recipe_id.'}
        if Child_Fav_Meal_Recipes.query.filter_by(recipe_index=recipe_id).all():
            return {'status': 202, 'message': 'This recipe is already your favourite.'}
        child_meal_recipes = Child_Fav_Meal_Recipes(child_id=child_id, recipe_index=recipe_id)
        db.session.add(child_meal_recipes)
        db.session.commit()
        return {'status': 200, 'message': 'Adding favourite recipe successful.'}
    except:
        return {'status': 500, 'message': 'Adding favourite recipe faild.'}

@app.route('/api/add_recommend_recipe', methods=['POST'])
# @login_required
def add_recommend_recipe():
    try:
        json_data = request.json
        recipe_id = json_data['recipe_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        recipe = Meal_Recipes.query.filter_by(recipe_index=recipe_id).first()
        recipe.counter += 1
        db.session.commit()
        return {'status': 200, 'message': 'Recommending recipe successful.'}
    except:
        return {'status': 500, 'message': 'Recommending recipe faild.'}

@app.route('/api/load_recipe_details', methods=['POST'])
# @login_required
def load_recipe_details():
    try:
        json_data = request.json
        child_id = json_data['child_id']
        recipe_id = json_data['recipe_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        recipe = Meal_Recipes.query.filter_by(recipe_index=recipe_id).all()
        if not recipe:
            return {'status': 202, 'message': 'No recipe for this recipe_id.'}
        favourite_this = 0
        fav = Child_Fav_Meal_Recipes.query.filter_by(recipe_index=recipe_id, child_id=child_id).all()
        if fav:
            favourite_this = 1
        return {'status': 200, 'message': 'Success.', 'favourite': favourite_this, 'recipe_details': recipe[0].get_recipe_detail()}
    except:
        return {'status': 500, 'message': 'Loading recipe details faild.'}
    
@app.route('/api/meal_recipes', methods=['POST'])
# @login_required
def meal_recipes():
    try:
        json_data = request.json
        child_id = json_data['child_id']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        fav = Child_Fav_Meal_Recipes.query.filter_by(child_id=child_id).all()
        fav_recipe_ids = []
        for i in range(0, len(fav)):
            fav_recipe_ids.append(fav[i].recipe_index)

        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        current_elimination = ''
        if child_food_elimination:
            for i in range(0, len(child_food_elimination)):
                if child_food_elimination[i].is_current_ongoing():
                    current_elimination = child_food_elimination[i].food_elimination_name
                    break
    
        recommend_recipes = Meal_Recipes.query.filter(Meal_Recipes.counter!=0).all()
        recommend_recipe_list = []
        for i in range(0, len(recommend_recipes)):
            if current_elimination == '' or (current_elimination not in recommend_recipes[i].title and current_elimination not in recommend_recipes[i].ingredients and current_elimination not in recommend_recipes[i].instructions):
                recommend_recipe_list.append(recommend_recipes[i].get_recipe(fav_recipe_ids))
        recommend_recipe_list.sort(key=lambda k: (k.get('recommend_counter', 0)), reverse=True)

        other_recipes = Meal_Recipes.query.filter(Meal_Recipes.recipe_index<20).all()
        other_recipe_list = []
        for i in range(0, len(other_recipes)):
            if current_elimination == '' or (current_elimination not in other_recipes[i].title and current_elimination not in other_recipes[i].ingredients and current_elimination not in other_recipes[i].instructions):
                other_recipe_list.append(other_recipes[i].get_recipe(fav_recipe_ids))
        other_recipe_list.sort(key=lambda k: (k.get('recommend_counter', 0)), reverse=True)
        other_recipe_list.sort(key=lambda k: (k.get('favourite', 0)), reverse=True)
        return {'status': 200, 'message': 'Success.', 'ongoing_food_elimination': current_elimination, 'recommend_recipes': recommend_recipe_list, 'other_recipes': other_recipe_list}
    except:
        return {'status': 500, 'message': 'Loading meal recipes faild.'}

'''
@app.route('/profile')
@login_required
def profile():
   return render_template('profile.html')

@app.route('/login', methods=['POST', 'GET'])
def html_login():
  if request.method == 'POST':
      user = User.query.filter_by(email=request.form["email"], password=hashlib.sha1(request.form["password"].encode('utf-8')).hexdigest()).first()
      if user is not None:
        login_user(user, remember=True)
        flash('Welcome!', 'success')
        return redirect(url_for('profile'))
      else:
        flash('Sorry, your username or password is incorrect.', 'danger')
        return redirect(url_for('login'))
  else:
      return render_template('login.html')

@app.route('/signup', methods=['POST', 'GET'])
def signup():
    error = None
    form = NewUser()
    if request.method == 'POST':        
        new_userx = User(email=form.email.data)
        new_userx.set_password(form.password.data)
        db.session.add(new_userx)
        db.session.commit()
        flash ("Thank you for signing up", "success")
        login_user(new_userx)
        return redirect(url_for('profile'))
    return render_template('signup.html')   

@app.route('/profile/posts')
def posts():
    posts = Posts.query.filter_by(author_id=current_user.id).all()
    return render_template('posts.html', posts = posts)

@app.route('/profile/posts/create', methods=['POST', 'GET'])
def createpost():
    error = None
    new_article = NewPost()
    if request.method == 'POST':        
        new_post = Posts(title=new_article.title.data, 
                         body=new_article.body.data) 
                         
        current_user.posts.append(new_post)
        db.session.add(new_post)
        db.session.commit()
        flash ("You have created a new post", 'success')
        return redirect(url_for('posts'))
    return render_template('newpost.html', form=new_article)
'''



if __name__ == '__main__':
    app.run(debug=True, ssl_context=('./cert/server.crt', './cert/server.key'), host="0.0.0.0", port=443)