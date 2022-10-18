import os
import time, datetime
import functools
import hashlib
import requests
import google.auth.transport.requests
import google_auth
import config
from gevent import pywsgi
from flask import Flask, render_template, request, session
from flask_login import login_user, logout_user, login_required, current_user
from sqlalchemy import extract, and_
from itsdangerous import base64_decode
from pip._vendor import cachecontrol
from google.oauth2 import id_token
from exts import mail, db, login
from meal_recipes import Meal_Recipes
from user import User
from child import Child
from meal import Meal
from food_elimination import Food_Elimination
from child_food_elimination import Child_Food_Elimination
from child_fav_meal_recipes import Child_Fav_Meal_Recipes
from mail_module import send_mail, is_valid_email
from pdf_report_gen import Writter
from elimination_ingredients import Elimination_Ingredients
from user_ratings import User_Ratings
from user_recommendations import User_Recommendations




app = Flask(__name__)
app.config.from_object(config)
mail.init_app(app)
login.init_app(app)
db.init_app(app)
# print(current_user.get_id())
# temp_user_id = 11730



# check if there is a dangerous char in string. support str and str_list
def str_security_check(input):
    dangerous = '\'\"#$!`&|\\;'
    if not input:
        return True
    if type(input) == type(''):
        to_str = input
    elif type(input) == type([]):
        to_str = ''.join(input)
    else:
        to_str = input
    for i in to_str:
        if i in dangerous:
            return False
    return True




# check if current operation's child_id is belongs to current user.
def prevent_over_permission(current_child_id_for_check):
    children = Child.query.filter_by(user_id=current_user.get_id()).all()
    if not children:
        return False
    else:
        child_id_list = []
        for i in children:
            child_id_list.append(i.child_id)
        if current_child_id_for_check not in child_id_list:
            return False
        else:
            return True




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
        username = str(json_data['username'])
        password = str(json_data['password'])
        passwordR = str(json_data['passwordR'])
        email = str(json_data['email'])
        if not str_security_check([username, email, password]):
            return {'status': 400, 'message': 'Invalid input .'}
    except Exception as ex:
        print(ex)
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
        except Exception as ex:
            print(ex)
            return {'status': 500, 'message': 'Email sending failed.'}




@app.route('/api/forget_password', methods=['POST'])
def forget_password():
    try:
        json_data = request.json
        username = str(json_data['username'])
        password = str(json_data['password'])
        passwordR = str(json_data['passwordR'])
        email = str(json_data['email'])
        if not str_security_check([username, email, password]):
            return {'status': 400, 'message': 'Invalid input .'}
    except Exception as ex:
        print(ex)
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
            modified_user = User.query.filter_by(email=email, username=username, is_for_oauth2=0)
            if modified_user:
                current_user = User.query.filter_by(email=email, username=username, is_for_oauth2=0).first()
                if current_user:
                    current_user_id = current_user.user_id
                else:
                    return {'status': 400, 'message': 'This account does not exist.'}
                modified_user.update({User.password: hashlib.sha1(password.encode('utf-8')).hexdigest(), User.verification: 0})
                db.session.commit()
                current_user = User(user_id=current_user_id, username=username, email=email)
                token = current_user.generate_active_token()
                send_mail(current_user.email, 'Account activate', 'activate', username=current_user.username, token=token)
                return {'status': 200, 'message': 'Password changed, please click activate link in your mailbox.'}
            else:
                return {'status': 400, 'message': 'This account does not exist.'}
        except Exception as ex:
            print(ex)
            return {'status': 500, 'message': 'Email sending failed.'}




@app.route('/api/activate/<token>', methods=['GET'])
def activate(token):
    if User.check_activate_token(token):
        return render_template('activate_status.html', msg='Your account is activated.')
    else:
        return render_template('activate_status.html', msg='Account activation failed.')




@app.route('/oauth2')
def oauth2():
    try:
        authorization_url, state = google_auth.flow.authorization_url()
        session['state'] = state
        return {'status': 302, 'message': 'Success.', 'authorization_url': authorization_url}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Getting authorization_url failed.'}




@app.route('/callback')
def callback():
    try:
        google_auth.flow.fetch_token(authorization_response=request.url)

        #if not session['state'] == request.args['state']:
            #return {'status': 403, 'message': 'State does not match!'}

        credentials = google_auth.flow.credentials
        request_session = requests.session()
        cached_session = cachecontrol.CacheControl(request_session)
        token_request = google.auth.transport.requests.Request(session=cached_session)

        id_info = id_token.verify_oauth2_token(
            id_token=credentials._id_token,
            request=token_request,
            audience=google_auth.GOOGLE_CLIENT_ID
        )
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Oauth failed.'}
        
    session['google_id'] = id_info.get('sub')
    session['name'] = id_info.get('name')
    session['email'] = id_info.get('email')

    user = User.query.filter_by(email=session['email']).first()
    if not user:
        # Create the user. Try and use their name returned by Google,
        user = User(username=session['name'], email=session['email'], is_for_oauth2=1)
        db.session.add(user)
        db.session.commit()
    # Log in the user, by default remembering them for their next visit
    # unless they log out.
    login_user(user, remember=True)
    return {'status': 200, 'message': 'You logged in with oauth2.'}




@app.route('/api/login', methods=['POST'])
def login():
    try:
        json_data = request.json
        email = str(json_data['email'])
        password = str(json_data['password'])
        if not str_security_check([email, password]):
            return {'status': 400, 'message': 'Invalid input.'}
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        this_user = User.query.filter_by(email=email, password=hashlib.sha1(password.encode('utf-8')).hexdigest()).first()
        if not this_user:
            return {'status': 202, 'message': 'Wrong username or password.'}
        elif this_user.verification == 0:
            return {'status': 202, 'message': 'Current user is not activated.'}
        elif this_user.is_for_oauth2 == 1:
            return {'status': 202, 'message': 'Wrong username or password.'}
        else:
            login_user(this_user, remember=True)
            return {'status': 200, 'message': 'Login successfully.', 'user_id': this_user.user_id, 'username': this_user.username}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Login faild.'}




@app.route('/api/logout')
@login_required
def logout():
   logout_user()
   return {'status': 200, 'message': 'Logout successfully.'}




@app.route('/api/remove_account', methods=['GET'])
@login_required
def remove_account():
    try:
        if User.query.filter_by(user_id=current_user.get_id()).delete():
            db.session.commit()
            return {'status': 200, 'message': 'Account canceled successful.'}
        else:
            return {'status': 202, 'message': 'This user is not exist.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Deleting account faild.'}




@app.route('/api/user_profile', methods=['GET'])
@login_required
def get_user_profile():
    try:
        user = User.query.filter_by(user_id=current_user.get_id()).first()
        children = Child.query.filter_by(user_id=current_user.get_id()).all()
        if not children:
            return {'status': 200, 'message': 'Success.', 'username':user.username , 'children_count': 0}
        else:
            children_list = []
            for i in range(0, len(children)):
                children_list.append(children[i].get_child_dict())
            return {'status': 200, 'message': 'Success.', 'username':user.username, 'children_count': len(children), 'children_list': children_list}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Getting user profile faild.'}




@app.route('/api/get_children', methods=['GET'])
@login_required
def get_children():
    try:
        children = Child.query.filter_by(user_id=current_user.get_id()).all()
        if not children:
            return {'status': 202, 'message': 'You have not added children.'}
        else:
            children_list = []
            for i in range(0, len(children)):
                children_list.append(children[i].get_child_dict())
            children_list.sort(key=lambda k: (k.get('child_id', 0)), reverse=True)
            return {'status': 200, 'message': 'Success.', 'children_count': len(children), 'children_list': children_list}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Getting child list faild.'}




@app.route('/api/add_child', methods=['POST'])
@login_required
def add_child():
    try:
        json_data = request.json
        gender = int(json_data['gender'])
        name = str(json_data['name'])
        age = int(json_data['age'])
        intolerance = json_data['intolerance']
        if not str_security_check(name):
            return {'status': 400, 'message': 'Invalid input.'}
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        child = Child(child_gender=gender, child_name=name, child_age=age, child_intolerance = intolerance, user_id=current_user.get_id())
        db.session.add(child)
        db.session.commit()
        return {'status': 200, 'message': 'Adding child successful.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Adding child faild.'}




@app.route('/api/del_child', methods=['POST'])
@login_required
def del_child():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}
    
    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        if Child.query.filter_by(child_id=child_id).delete():
            db.session.commit()
            return {'status': 200, 'message': 'Deleting child successful.'}
        else:
            return {'status': 202, 'message': 'This child_id is not exist.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Deleting child faild.'}




@app.route('/api/get_child_profile', methods=['POST'])
@login_required
def get_child_profile():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        child = Child.query.filter_by(child_id=int(child_id)).all()
        if not child:
            return {'status': 202, 'message': 'This child_id is not exists.'}
        else:
            child_name = child[0].child_name
            child_age = child[0].child_age
            current_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
            meal_history_info = Meal.query.filter_by(child_id=child_id).all()
            current_elimination_list = []
            current_ongoing = False
            current_elimination_type = -1
            eczema_count = 0
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
                if current_elimination[i].is_current_ongoing(): # get rid off the nodes that is not ongoing.
                    current_ongoing = True
                    current_elimination_list.append(current_elimination[i].get_time_remaining())
            if current_ongoing and current_elimination_list: # get "id" from Food_Elimination table.
                current_elimination_type = Food_Elimination.query.filter_by(food_name=current_elimination_list[0].get('food_elimination_name')).first().food_id
            for i in range(0, len(meal_history_info)):
                if meal_history_info[i].diary_eczema == 1:
                    eczema_count += 1
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
            # reshape the response dict.
            return_dict = {'status': 200, 'message': 'Success.', 
            'child_name': child_name, 
            'child_age': child_age, 
            'current_elimination_type': current_elimination_type, 
            'eczema_count': eczema_count, 
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
            for i in range(0, len(current_elimination_list)):
                return_dict.update(current_elimination_list[i])
            return return_dict
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Getting child profile faild.'}




@app.route('/api/get_diary', methods=['POST'])
@login_required
def get_diary():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        timestamp = int(str(json_data['timestamp'])[0:10])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        meals = Meal.query.filter(Meal.child_id==child_id, and_(extract('year', Meal.diary_datetime) == datetime.datetime.fromtimestamp(timestamp).year, extract('month', Meal.diary_datetime) == datetime.datetime.fromtimestamp(timestamp).month, extract('day', Meal.diary_datetime) == datetime.datetime.fromtimestamp(timestamp).day)).all()
        if not meals:
            return {'status': 202, 'message': 'Empty diary of this date.'}
        else:
            diary_list = []
            for i in range(0, len(meals)):
                diary_list.append(meals[i].get_meal_details())
            return {'status': 200, 'message': 'Success.', 'meal_list': diary_list}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Getting diary faild.'}




@app.route('/api/add_meal_to_diary', methods=['POST'])
@login_required
def add_meal_to_diary():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        diary_datetime = int(json_data['datetime'])
        food_type = int(json_data['food_type'])
        food_name = str(json_data['food_name'])
        food_description = str(json_data['food_description'])
        eczema = int(json_data['eczema'])
        stomach_ache = int(json_data['stomach_ache'])
        nausea = int(json_data['nausea'])
        respiratory = int(json_data['respiratory'])
        headaches = int(json_data['headaches'])
        diarrhea = int(json_data['diarrhea'])
        tiredness = int(json_data['tiredness'])
        bloating = int(json_data['bloating'])
        joint_pain = json_data['joint_pain']
        if str(json_data['additional_symptoms']) == '0':
            additional_symptoms = ''
        else:
            additional_symptoms = str(json_data['additional_symptoms'])
        
        if not str_security_check([food_name, food_description, additional_symptoms]):
            return {'status': 400, 'message': 'Invalid input.'}

        if str(json_data['image']) == '0':
            image = None
        else:
            image = base64_decode(json_data['image'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        meal = Meal(child_id=child_id, diary_datetime=datetime.datetime.fromtimestamp(diary_datetime/1000), diary_food_name=food_name, diary_food_description=food_description, diary_eczema=eczema, diary_stomach_ache=stomach_ache, diary_nausea=nausea, diary_respiratory=respiratory, diary_headaches=headaches, diary_diarrhea=diarrhea, diary_tiredness=tiredness, diary_bloating=bloating, diary_joint_pain=joint_pain, diary_additional_symptoms=additional_symptoms, diary_food_type=food_type, diary_pic=image)
        db.session.add(meal)
        db.session.commit()
        return {'status': 200, 'message': 'Adding meal to diary successful.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Adding meal to diary faild.'}




@app.route('/api/view_meal_details', methods=['POST'])
@login_required
def view_meal_details():
    try:
        json_data = request.json
        id = int(json_data['meal_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        meal = Meal.query.filter_by(diary_id=int(id)).all()
        if not meal:
            return {'status': 202, 'message': 'This meal id is not exists.'}   
        else:
            if not prevent_over_permission(meal[0].child_id):
                return {'status': 400, 'message': 'This meal can not be accessed from current user.'}
            return {'status': 200, 'message': 'Success.', 'meal_details': meal[0].get_meal_details()}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Query meal details faild.'}




@app.route('/api/add_food_elimination', methods=['POST'])
@login_required
def add_food_elimination():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        food_intolerance_id = int(json_data['food_intolerance_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        existing_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        current_ongoing = False
        for i in range(0, len(existing_food_elimination)):
            if existing_food_elimination[i].is_current_ongoing():
                current_ongoing = True
                break
        if not current_ongoing:
            food_elimination = Food_Elimination.query.filter_by(food_id=food_intolerance_id).all()
            if not food_elimination:
                return {'status': 202, 'message': 'No data for this food.'}
            else:
                child_food_elimination = Child_Food_Elimination(child_id=child_id, food_elimination_name=food_elimination[0].food_name, food_elimination_duration=food_elimination[0].food_elimination_duration)
                db.session.add(child_food_elimination)
                db.session.commit()
                return {'status': 200, 'message': 'Adding food elimination successful.'}
        else:
            return {'status': 202, 'message': 'There is an elimination ongoing.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Adding food elimination faild.'}




@app.route('/api/load_food_elimination_diets', methods=['POST'])
@login_required
def load_food_elimination_diets():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        if not child_food_elimination:
            return {'status': 202, 'message': 'Empty record of current child.', 'child_food_elimination_list': []}
        else:
            child_food_elimination_list = []
            for i in range(0, len(child_food_elimination)):
                food_elimination_type = Food_Elimination.query.filter_by(food_name=child_food_elimination[i].food_elimination_name).first().food_id # get "id" from Food_Elimination table.
                child_food_elimination_list.append(child_food_elimination[i].get_diet_info(food_elimination_type))
            return {'status': 200, 'message': 'Success.', 'child_food_elimination_list': child_food_elimination_list}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Loading food elimination diets faild.'}




@app.route('/api/add_favourite_recipe', methods=['POST'])
@login_required
def add_favourite_recipe():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        recipe_id = int(json_data['recipe_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        recipe = Meal_Recipes.query.filter_by(recipe_index=recipe_id).first()
        if recipe:
            if not Child_Fav_Meal_Recipes.query.filter_by(child_id=child_id, recipe_index=recipe_id).all():
                this_fav = Child_Fav_Meal_Recipes(child_id=child_id, recipe_index=recipe_id)
                db.session.add(this_fav)
                db.session.commit()
                return {'status': 200, 'message': 'Adding favourite recipe successful.'}
            else:
                return {'status': 202, 'message': 'This recipe is already your favourite.'}
        else:
            return {'status': 202, 'message': 'No recipe for this recipe_id.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Adding favourite recipe faild.'}




@app.route('/api/del_favourite_recipe', methods=['POST'])
@login_required
def del_favourite_recipe():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        recipe_id = int(json_data['recipe_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        if Child_Fav_Meal_Recipes.query.filter_by(child_id=child_id, recipe_index=recipe_id).delete():
            db.session.commit()
            return {'status': 200, 'message': 'Deleting favourite recipe successful.'}
        else:
            return {'status': 202, 'message': 'This recipe is not your favourite.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Deleting favourite recipe faild.'}




'''
@app.route('/api/add_recommend_recipe', methods=['POST'])
@login_required
def add_recommend_recipe():
    try:
        json_data = request.json
        recipe_id = json_data['recipe_id']
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        recipe = Meal_Recipes.query.filter_by(recipe_index=recipe_id).first()
        recipe.counter += 1
        db.session.commit()
        return {'status': 200, 'message': 'Recommending recipe successful.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Recommending recipe faild.'}
'''




@app.route('/api/rate_recipe', methods=['POST'])
@login_required
def rate_recipe():
    try:
        json_data = request.json
        recipe_id = int(json_data['recipe_id'])
        ratings = float(json_data['ratings'])
        if ratings > 5 or ratings < 0:
            return {'status': 400, 'message': 'Ratings must between 0 and 5.'}
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        recipe = Meal_Recipes.query.filter_by(recipe_index=recipe_id).first()
        if recipe:
            if User_Ratings.query.filter_by(user_id=current_user.get_id(), recipe_index=recipe_id).update({User_Ratings.rating: ratings}) == 0:
                this_rating = User_Ratings(user_id=current_user.get_id(), recipe_index=recipe_id, rating=ratings)
                db.session.add(this_rating)
                db.session.commit()
            else:
                db.session.commit()
            if recipe.count == 0:
                recipe.ratings += ratings
                recipe.count += 1
                db.session.commit()
            else:
                recipe.ratings = (recipe.ratings * recipe.count + ratings) / (recipe.count + 1)
                recipe.count += 1
                db.session.commit()
        else:
            return {'status': 400, 'message': 'Invalid recipe_id.'}
        return {'status': 200, 'message': 'Rating recipe successful.'}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Rating recipe faild.'}




@app.route('/api/load_recipe_details', methods=['POST'])
@login_required
def load_recipe_details():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        recipe_id = int(json_data['recipe_id'])
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        recipe = Meal_Recipes.query.filter_by(recipe_index=recipe_id).all()
        if not recipe:
            return {'status': 202, 'message': 'No recipe for this recipe_id.'}
        favourite_this = 0
        fav = Child_Fav_Meal_Recipes.query.filter_by(recipe_index=recipe_id, child_id=child_id).all()
        if fav:
            favourite_this = 1
        return {'status': 200, 'message': 'Success.', 'favourite': favourite_this, 'recipe_details': recipe[0].get_recipe_detail()}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Loading recipe details faild.'}




@app.route('/api/meal_recipes', methods=['POST'])
@login_required
def meal_recipes():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        page_number = int(json_data['page_number'])
        if page_number <= 0:
            return {'status': 400, 'message': 'Invalid page number.'}
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        items_per_page = 25
        # get child fav recipes to a list
        fav = Child_Fav_Meal_Recipes.query.filter_by(child_id=child_id).all()
        fav_recipe_ids = []
        for i in fav:
            fav_recipe_ids.append(i.recipe_index)

        # check if there is an ongoing elimination, if yes, store current elimination temporarily.
        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        current_elimination = ''
        current_elimination_id = -1 # type (if no ongoing elimination, remain -1.)
        if child_food_elimination:
            for i in child_food_elimination:
                if i.is_current_ongoing():
                    current_elimination = i.food_elimination_name
                    break
        if current_elimination:
            current_elimination_id = Food_Elimination.query.filter_by(food_name=current_elimination).first().food_id
    
        # get recommend_recipes and merge a new list with fav recipes. (this list is filled with recipe ids)
        recommend_recipes = User_Recommendations.query.filter_by(user_id=current_user.get_id()).first()
        if recommend_recipes:
            recommend_recipe_id_list = recommend_recipes.recommendations.replace(' ', '').split(',')
            recommend_recipe_id_list = list(map(int, recommend_recipe_id_list))
            merged_recipe_ids_list = list(dict.fromkeys(fav_recipe_ids + recommend_recipe_id_list))
            merged_recipe_ids_list = list(map(int, merged_recipe_ids_list))
        else:
            recommend_recipe_id_list = []
            merged_recipe_ids_list = list(dict.fromkeys(fav_recipe_ids + recommend_recipe_id_list))
            merged_recipe_ids_list = list(map(int, merged_recipe_ids_list))

        # make a merged_recipes_list from merged_recipe_ids_list and then kick out the intol items. (if there is an ongoing elimination)
        merged_recipes_list = []
        for i in merged_recipe_ids_list:
            merged_recipes_list.append(Meal_Recipes.query.filter_by(recipe_index=i).first())
        if current_elimination:
            ingredients_checker = Elimination_Ingredients()
            merged_recipes_list = ingredients_checker.check_meal_recipe_is_ok(current_elimination, merged_recipes_list)

        other_recipe_start_page_num = int(len(merged_recipes_list) / items_per_page) + 1
        other_recipe_start_index_num = len(merged_recipes_list) % items_per_page
        temp_recipe_list = []
        out_list = []
        if page_number < other_recipe_start_page_num:
            for i in range(items_per_page*(page_number-1), items_per_page*page_number-1):
                if merged_recipes_list[i].recipe_index in recommend_recipe_id_list:
                    merged_recipes_list[i].is_recommended = 1
                temp_recipe_list.append(merged_recipes_list[i])
            for i in temp_recipe_list:
                out_list.append(i.get_recipe(fav_recipe_ids))
            return {'status': 200, 'message': 'Success.', 'page_number': page_number, 'ongoing_food_intolerance': current_elimination, 'ongoing_food_intolerance_type': current_elimination_id, 'recipes': out_list}
        elif page_number == other_recipe_start_page_num:
            for i in range(len(merged_recipes_list)-other_recipe_start_index_num, len(merged_recipes_list)):
                if merged_recipes_list[i].recipe_index in recommend_recipe_id_list:
                    merged_recipes_list[i].is_recommended = 1
                temp_recipe_list.append(merged_recipes_list[i])
            for i in range(0, items_per_page-other_recipe_start_index_num):
                other_recipes = Meal_Recipes.query.filter_by(recipe_index=i).all()
                if other_recipes[0].recipe_index in merged_recipe_ids_list:
                    pass
                else:
                    temp_recipe_list += other_recipes
            for i in temp_recipe_list:
                out_list.append(i.get_recipe(fav_recipe_ids))
            return {'status': 200, 'message': 'Success.', 'page_number': page_number, 'ongoing_food_intolerance': current_elimination, 'ongoing_food_intolerance_type': current_elimination_id, 'recipes': out_list}            
        else:
            for i in range(items_per_page*(page_number-other_recipe_start_page_num-1)+(items_per_page-other_recipe_start_index_num), items_per_page*(page_number-other_recipe_start_page_num)+(items_per_page-other_recipe_start_index_num)):
                other_recipes = Meal_Recipes.query.filter_by(recipe_index=i).all()
                if other_recipes[0].recipe_index in merged_recipe_ids_list:
                    pass
                else:
                    temp_recipe_list += other_recipes
            for i in temp_recipe_list:
                out_list.append(i.get_recipe(fav_recipe_ids))
            return {'status': 200, 'message': 'Success.', 'page_number': page_number, 'ongoing_food_intolerance': current_elimination, 'ongoing_food_intolerance_type': current_elimination_id, 'recipes': out_list}
        '''
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
                if other_recipes[i].counter > 0:
                    pass
                else:
                    other_recipe_list.append(other_recipes[i].get_recipe(fav_recipe_ids))
        other_recipe_list.sort(key=lambda k: (k.get('favourite', 0)), reverse=True)
        return {'status': 200, 'message': 'Success.', 'ongoing_food_elimination_type': current_elimination_id, 'recommend_recipes': recommend_recipe_list, 'other_recipes': other_recipe_list}
        '''
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Loading meal recipes faild.'}




@app.route('/api/search_meal_recipe', methods=['POST'])
@login_required
def search_meal_recipe():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        keyword = str(json_data['keyword'])
        page_number = int(json_data['page_number'])
        if page_number <= 0:
            return {'status': 400, 'message': 'Invalid page number.'}
        if not str_security_check(keyword):
            return {'status': 400, 'message': 'Invalid input.'}
        keyword_list = keyword.split(',')
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        items_per_page = 25
        # get child fav recipes to a list
        fav = Child_Fav_Meal_Recipes.query.filter_by(child_id=child_id).all()
        fav_recipe_ids = []
        for i in fav:
            fav_recipe_ids.append(i.recipe_index)

        # check if there is an ongoing elimination, if yes, store current elimination temporarily.
        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        current_elimination = ''
        current_elimination_id = -1 # type (if no ongoing elimination, remain -1.)
        if child_food_elimination:
            for i in child_food_elimination:
                if i.is_current_ongoing():
                    current_elimination = i.food_elimination_name
                    break
        if current_elimination:
            current_elimination_id = Food_Elimination.query.filter_by(food_name=current_elimination).first().food_id
        
        # get recommend_recipes and merge a new list with fav recipes. (this list is filled with recipe ids)
        recommend_recipes = User_Recommendations.query.filter_by(user_id=current_user.get_id()).first()
        if recommend_recipes:
            recommend_recipe_id_list = recommend_recipes.recommendations.replace(' ', '').split(',')
            recommend_recipe_id_list = list(map(int, recommend_recipe_id_list))
            merged_recipe_ids_list = list(dict.fromkeys(fav_recipe_ids + recommend_recipe_id_list))
            merged_recipe_ids_list = list(map(int, merged_recipe_ids_list))
        else:
            recommend_recipe_id_list = []
            merged_recipe_ids_list = list(dict.fromkeys(fav_recipe_ids + recommend_recipe_id_list))
            merged_recipe_ids_list = list(map(int, merged_recipe_ids_list))

        matched_recipe_list = []
        if current_elimination:
            ingredients_checker = Elimination_Ingredients()
        if len(keyword_list) == 1 and keyword_list[0] == '':
            # make a merged_recipes_list from merged_recipe_ids_list and then kick out the intol items. (if there is an ongoing elimination)
            merged_recipes_list = []
            for i in merged_recipe_ids_list:
                merged_recipes_list.append(Meal_Recipes.query.filter_by(recipe_index=i).first())
            if current_elimination:
                merged_recipes_list = ingredients_checker.check_meal_recipe_is_ok(current_elimination, merged_recipes_list)

            other_recipe_start_page_num = int(len(merged_recipes_list) / items_per_page) + 1
            other_recipe_start_index_num = len(merged_recipes_list) % items_per_page
            if page_number < other_recipe_start_page_num:
                for i in range(items_per_page*(page_number-1), items_per_page*page_number-1):
                    if merged_recipes_list[i].recipe_index in recommend_recipe_id_list:
                        merged_recipes_list[i].is_recommended = 1
                    matched_recipe_list.append(merged_recipes_list[i])
            elif page_number == other_recipe_start_page_num:
                for i in range(len(merged_recipes_list)-other_recipe_start_index_num, len(merged_recipes_list)):
                    if merged_recipes_list[i].recipe_index in recommend_recipe_id_list:
                        merged_recipes_list[i].is_recommended = 1
                    matched_recipe_list.append(merged_recipes_list[i])
                for i in range(0, items_per_page-other_recipe_start_index_num):
                    other_recipes = Meal_Recipes.query.filter_by(recipe_index=i).all()
                    if other_recipes[0].recipe_index in merged_recipe_ids_list:
                        pass
                    else:
                        matched_recipe_list += other_recipes
            else:
                for i in range(items_per_page*(page_number-other_recipe_start_page_num-1)+(items_per_page-other_recipe_start_index_num), items_per_page*(page_number-other_recipe_start_page_num)+(items_per_page-other_recipe_start_index_num)):
                    other_recipes = Meal_Recipes.query.filter_by(recipe_index=i).all()
                    if other_recipes[0].recipe_index in merged_recipe_ids_list:
                        pass
                    else:
                        matched_recipe_list += other_recipes
            # make a merged_recipes_list from merged_recipe_ids_list and then kick out the intol items. (if there is an ongoing elimination)
            '''
            merged_recipes_list = []
            for i in merged_recipe_ids_list:
                merged_recipes_list.append(Meal_Recipes.query.filter_by(recipe_index=i).first())
            for i in merged_recipes_list:
                if i.recipe_index in recommend_recipe_id_list:
                    i.is_recommended = 1
                matched_recipe_list.append(i)
            other_recipes = list(set(Meal_Recipes.query.filter_by().all()))
            for i in other_recipes:
                if i.recipe_index in merged_recipe_ids_list:
                    pass
                else:
                    matched_recipe_list.append(i)
            if current_elimination:
                ingredients_checker = Elimination_Ingredients()
                matched_recipe_list = ingredients_checker.check_meal_recipe_is_ok(current_elimination, matched_recipe_list)
            '''
            # matched_recipe_list = Meal_Recipes.query.filter_by().all()
            # matched_recipe_list = ingredients_checker.check_meal_recipe_is_ok(current_elimination, matched_recipe_list)
            # matched_recipe_list = list(set(matched_recipe_list))
        else:
            for i in keyword_list:
                matched_recipe_title_list = Meal_Recipes.query.filter(Meal_Recipes.title.like('%'+i+'%')).all()
                matched_recipe_ingredients_list = Meal_Recipes.query.filter(Meal_Recipes.ingredients.like('%'+i+'%')).all()
                matched_recipe_instructions_list = Meal_Recipes.query.filter(Meal_Recipes.instructions.like('%'+i+'%')).all()
                if matched_recipe_title_list or matched_recipe_ingredients_list or matched_recipe_instructions_list:
                    matched_recipe_list += (list(set(matched_recipe_title_list)|set(matched_recipe_ingredients_list)|set(matched_recipe_instructions_list)))
                    if current_elimination:
                        matched_recipe_list = ingredients_checker.check_meal_recipe_is_ok(current_elimination, matched_recipe_list)
        out_list = []
        for i in matched_recipe_list:
            out_list.append(i.get_recipe(fav_recipe_ids))
        return {'status': 200, 'message': 'Success.', 'page_number': page_number, 'ongoing_food_intolerance': current_elimination, 'ongoing_food_intolerance_type': current_elimination_id, 'recipes_count': len(out_list), 'recipes': out_list}
        

    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Searching meal recipes faild.'}



@app.route('/api/get_pdf_report', methods=['POST'])
@login_required
def get_pdf_report():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        email = str(json_data['email'])
        if is_valid_email(email) != True:
            return {'status': 400, 'message': 'The email address is not in correct format.'}
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        timestamp_str = str(int(datetime.datetime.now().timestamp()*1000000))
        w = Writter("./pdf_tmp_files/", timestamp_str+'.pdf', "./templates/cover_pic.png")

        child = Child.query.filter_by(child_id=int(child_id)).all()
        if child:
            w.name = child[0].child_name
            w.age = str(child[0].child_age)
            if child[0].child_gender == 0:
                w.gender = 'Male'
            elif child[0].child_gender == 1:
                w.gender = 'Female'
            else:
                w.gender = 'Other'
            w.write_info('info')
        else:
            return {'status': 202, 'message': 'This child_id is not exists.'}

        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        if not child_food_elimination:
            return {'status': 202, 'message': 'Empty record of current child.'}
        else:
            child_food_elimination_list = []
            for i in range(0, len(child_food_elimination)):
                food_elimination_type = Food_Elimination.query.filter_by(food_name=child_food_elimination[i].food_elimination_name).first().food_id # get "id" from Food_Elimination table.
                child_food_elimination_list.append(child_food_elimination[i].get_diet_info(food_elimination_type))

        if len(child_food_elimination_list) != 0:
            for i in child_food_elimination_list:
                if i.get('ongoing_food_elimination') == 1:
                    w.food = i.get('food_elimination_name')
                    w.date = str(time.strftime("%Y/%m/%d", time.localtime(i.get('food_elimination_start_date')/1000))) + ' -- ' + str(time.strftime("%Y/%m/%d", time.localtime(i.get('food_elimination_end_date')/1000)))
                    w.state = str(round((i.get('food_elimination_end_date')/1000-time.time())/86400, 2)) + ' days left.'
        else:
            w.food = 'No food elimination.'
            w.date = 'No food elimination.'
            w.state = 'No food elimination.'
        w.write_info("elimination")
        
        diaries = Meal.query.filter_by(child_id=child_id).all()
        if len(diaries) != 0:
            for i in range(0, len(diaries)):
                w.diary = str(i)
                w.time = str(diaries[i].diary_datetime).split(' ')[0]
                w.type = str(diaries[i].get_meal_type())
                w.title = str(diaries[i].diary_food_name)
                w.desp = str(diaries[i].diary_food_description)
                w.image = diaries[i].get_meal_pic('./pdf_tmp_files/' + timestamp_str + str(diaries[i].diary_id) + '.png')
                w.sysm = str(diaries[i].get_symptoms())
                w.addition = str(diaries[i].diary_additional_symptoms)
                w.write_info('meal_diary')
                if diaries[i].diary_pic != None:
                    os.remove('./pdf_tmp_files/' + timestamp_str + str(diaries[i].diary_id) + '.png')
            w.save_pdf()
            send_mail(email, 'Food intol report', 'report', './pdf_tmp_files/'+timestamp_str+'.pdf')
        else:
            return {'status': 202, 'message': 'No food diary for current child.'}
            '''
            w.write_info('info')
            w.diary = 'No meal diary record.'
            w.time = time.strftime("%d/%m/%Y", time.localtime(time.time()))
            w.type = 'No meal diary record.'
            w.title = 'No meal diary record.'
            w.desp = 'No meal diary record.'
            w.image = './templates/empty.png'
            w.sysm = 'No meal diary record.'
            w.addition = 'No meal diary record.'
            w.write_info('meal_diary')
            w.save_pdf()
            send_mail(email, 'Food intol report', 'report', './pdf_tmp_files/'+timestamp_str+'.pdf')
            '''
        return {'status': 200, 'message': 'Success.'}
        
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Loading food elimination diets faild.'}




@app.route('/api/scanner', methods=['POST'])
@login_required
def scanner():
    try:
        json_data = request.json
        child_id = int(json_data['child_id'])
        ingredients = list(json_data['ingredients'])
        if not str_security_check(ingredients):
            return {'status': 400, 'message': 'Invalid input.'}
    except Exception as ex:
        print(ex)
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        if not prevent_over_permission(child_id):
            return {'status': 400, 'message': 'This child_id is not for current user.'}
        child_food_elimination = Child_Food_Elimination.query.filter_by(child_id=child_id).all()
        if not child_food_elimination:
            return {'status': 202, 'message': 'Empty record of current child.', 'output': 0}
        else:
            child_food_elimination_list = []
            for i in range(0, len(child_food_elimination)):
                food_elimination_type = Food_Elimination.query.filter_by(food_name=child_food_elimination[i].food_elimination_name).first().food_id # get "id" from Food_Elimination table.
                child_food_elimination_list.append(child_food_elimination[i].get_diet_info(food_elimination_type))

        if len(child_food_elimination_list) != 0:
            elimination_ingredients = Elimination_Ingredients()
            ongoing = 0
            exists = 0
            for i in child_food_elimination_list:
                if i.get('ongoing_food_elimination') == 1:
                    ongoing = 1
                    exists = elimination_ingredients.check_ingredients_exist(i.get('food_elimination_name'), ingredients)
            if ongoing == 0:
                return {'status': 200, 'message': 'No ongoing elimination for current child_id.', 'output': 0}
            else:
                return {'status': 200, 'message': 'Success.', 'output': exists}
        else:
            return {'status': 200, 'message': 'No ongoing elimination for current child_id.', 'output': 0}
    except Exception as ex:
        print(ex)
        return {'status': 500, 'message': 'Get scanning result failed.'}




if __name__ == '__main__':
    print('Serving on https://0.0.0.0:443')
    server = pywsgi.WSGIServer(('0.0.0.0', 443), app, keyfile='./cert/server.key', certfile='./cert/server.crt', log=None)
    server.serve_forever()
    # app.run(debug=False, ssl_context=('./cert/server.crt', './cert/server.key'), host="0.0.0.0", port=443)
    # app.run(debug=True, ssl_context=('./cert/server.crt', './cert/server.key'), host="0.0.0.0", port=443)