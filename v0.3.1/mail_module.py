import os, re
from flask_mail import Message
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

def async_send_mail(app, msg, attach=None):
    with app.app_context():
        try:
            if attach == None:
                mail.send(msg)
            else:
                with app.open_resource(attach) as fp:
                    msg.attach('Food Intolerance Report.pdf', 'application/octet-stream', fp.read())
                    mail.send(msg)
                    os.remove(attach)
        except Exception as e:
            print(e)

def send_mail(to, subject, template, attach=None, **kwargs):
    try:
        msg = Message(subject, recipients=[to])
        msg.html = render_template(template + '.html', **kwargs)
        app = current_app._get_current_object()
        thread = Thread(target=async_send_mail, args=[app, msg, attach])
        thread.start()
        return thread
    except Exception as e:
        print(e)