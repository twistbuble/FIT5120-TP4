import datetime
import functools
import hashlib
import config
from flask import Flask, request
from flask_login import login_user, logout_user, login_required, current_user
from sqlalchemy import extract, and_
from exts import mail, db, login
from user import User
from child import Child
from meal import Meal
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

@app.route('/activate/<token>', methods=['GET'])
def activate(token):
    if User.check_activate_token(token):
        return {'status': 200, 'message': 'Your account is activated.'}
    else:
        return {'status': 403, 'message': 'Account activation failed.'}

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
        Child.query.filter_by(child_id=int(child_id)).delete()
        db.session.commit()
        return {'status': 200, 'message': 'Deleting child successful.'}
    except:
        return {'status': 500, 'message': 'Deleting child faild.'}

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
        food_name = json_data['food_name']
        food_description = json_data['food_description']
        mood = json_data['mood']
        energy = json_data['energy']
        stomach_ache = json_data['stomach_ache']
    except:
        return {'status': 400, 'message': 'Invalid input.'}

    try:
        meal = Meal(child_id=child_id, diary_food_name=food_name, diary_food_description=food_description, diary_mood=int(mood), diary_energy=int(energy), stomach_ache=stomach_ache)
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