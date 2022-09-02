import re
from flask_mail import Message, Mail
from flask import render_template, current_app
from threading import Thread
from exts import mail

def is_valid_email(email):
    ex_email = re.compile(r'^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$')
    result = ex_email.match(email)
    if result:
        return True
    else:
        return False

def async_send_mail(app, msg):
    with app.app_context():
       mail.send(msg)

def send_mail(to, subject, template, **kwargs):
    try:
        msg = Message(subject, recipients=[to])
        msg.html = render_template(template + '.html', **kwargs)
        app = current_app._get_current_object()
        thread = Thread(target=async_send_mail, args=[app, msg])
        thread.start()
        return thread
    except Exception as e:
        print(e)
