import asyncio
import base64
import sqlite3
import time
import datetime
import types
from flask import Flask, request, jsonify
from geopy.geocoders import Nominatim

from aiohttp import ClientSession
from pyopenuv import Client
from pyopenuv.errors import OpenUvError

class db(object):
    def __init__(self, db_path):
        self.conn = sqlite3.connect(db_path)
    def __del__(self):
        self.conn.close()
    def sqlite_db_fetch(self, sql_op):
        cursor = self.conn.cursor()
        cursor.execute(sql_op)
        res = cursor.fetchall()
        cursor.close()
        self.conn.commit()
        return res
    def sqlite_db_insert(self, sql_op):
        cursor = self.conn.cursor()
        cursor.execute(sql_op)
        cursor.close()
        self.conn.commit()

class user(object):
    def __init__(self):
        self.db = db("./sun_safe_DB.db")
        self.uid = ""
        self.gender = ""
        self.race = ""
        self.skin_tone = ""
    def login(self, uid):
        self.uid = uid
        if self.db.sqlite_db_operation(""):
            return "{'login':'success'}"
        else:
            return "{'login':'fail'}"
    def logout(self, uid):
        self.uid = ""
        return "{'logout':'success'}" 

def get_current_season():
  current_month = datetime.datetime.today().month
  if current_month == 1 or current_month == 2 or current_month == 12:
    return "Summer"
  elif current_month >= 3 and current_month <=5:
    return "Autumn"
  elif current_month >= 6 and current_month <= 8:
    return "Winter"
  else:
    return "Spring"

def get_current_loc(lat, lng):
  geolocator = Nominatim(user_agent = "geoapiExercises")
  location = geolocator.reverse(lat + "," + lng)
  if location == None:
    return "unknown area"
  else:
    return location.raw['address']
  

async def get_current_uv_related_info(lat, lng, alt, info_type):
    api_token = "758a878160e1859067e33fb54d8a9797"
    async with ClientSession() as session:
        client = Client(
            api_token,
            lat,
            lng,
            altitude=alt,
            session=session,
        )

        try:
            # Get current UV info:
            if info_type == "current_uv":
                # current_uv = {'result': {'uv': 0, 'uv_time': '2022-08-04T17:34:10.835Z', 'uv_max': 11.2417, 'uv_max_time': '2022-08-05T05:18:07.597Z', 'ozone': 288, 'ozone_time': '2022-06-02T12:05:13.749Z', 'safe_exposure_time': {'st1': None, 'st2': None, 'st3': None, 'st4': None, 'st5': None, 'st6': None}, 'sun_info': {'sun_times': {'solarNoon': '2022-08-05T05:18:07.597Z', 'nadir': '2022-08-04T17:18:07.597Z', 'sunrise': '2022-08-04T22:27:27.028Z', 'sunset': '2022-08-05T12:08:48.166Z', 'sunriseEnd': '2022-08-04T22:30:10.742Z', 'sunsetStart': '2022-08-05T12:06:04.452Z', 'dawn': '2022-08-04T22:00:36.283Z', 'dusk': '2022-08-05T12:35:38.910Z', 'nauticalDawn': '2022-08-04T21:28:17.554Z', 'nauticalDusk': '2022-08-05T13:07:57.640Z', 'nightEnd': '2022-08-04T20:54:12.028Z', 'night': '2022-08-05T13:42:03.166Z', 'goldenHourEnd': '2022-08-04T23:01:58.138Z', 'goldenHour': '2022-08-05T11:34:17.055Z'}, 'sun_position': {'azimuth': -3.050740368467013, 'altitude': -0.6880141215935368}}}}
                current_uv = await client.uv_index()
                # return base64.b64encode(str(current_uv).encode("utf-8")).decode('utf-8')
                return current_uv
            # Get forecasted UV info:
            elif info_type == "forecasted_uv":
                forecasted_uv = await client.uv_forecast()
                # return base64.b64encode(str(forecasted_uv).encode("utf-8")).decode('utf-8')
                return forecasted_uv
            # Get UV protection window: 
            elif info_type == "uv_protection_window":
                uv_protection_window = await client.uv_protection_window()
                return uv_protection_window
                # return base64.b64encode(str(uv_protection_window).encode("utf-8")).decode('utf-8')
            else:
                return "{'error': 'wrong type of info request'}".encode("utf-8")
                # return base64.b64encode("{'error': 'wrong type of info request'}".encode("utf-8")).decode('utf-8')
        except OpenUvError as err:
            return f"There was an error: {err}"

app = Flask(__name__)

@app.route('/')
def hello():
    return "Hello!"

@app.route('/login')
def login():
    current_user = user()
    json_data = request.json
    uid = json_data["uid"]
    return current_user.login(uid)

@app.route('/api/get_current_uv_index_info', methods=['POST'])
def current_uv():
    json_data = request.json
    lat = json_data["lat"]
    lng = json_data["lng"]
    alt = json_data["alt"]
    res = asyncio.run(get_current_uv_related_info(lat, lng, alt, "current_uv"))
    database = db("./sun_safe_DB.db")
    database.sqlite_db_insert("insert into Log (user_id, query_type, query_data, query_time) values ('%s', '%s', '%s', '%s')" % ("1", "current_uv" ,base64.b64encode(str(res).encode("utf-8")).decode('utf-8'), time.time()))
    
    current_serson = get_current_season()
    current_loc = get_current_loc(lat, lng)
    res["current_season"] = current_serson
    res["current_loc"] = current_loc
    if current_loc == "unknown area":
      res["average_uv"] = "no record of this area."
      return str(res).encode("utf-8")
    else:
      avg_uv = database.sqlite_db_fetch("select avg_uv_index from Avg where season=\'" + current_serson + "\'" + " and region=\'" + current_loc.get('city', '') + "\'")
      if len(avg_uv) != 0:
        res["average_uv"] = avg_uv[0][0]
      else:
        res["average_uv"] = "no record of this area."
      return str(res).encode("utf-8")

@app.route('/api/get_forecasted_uv_info', methods=['POST'])
def forecasted_uv():
    json_data = request.json
    lat = json_data["lat"]
    lng = json_data["lng"] 
    alt = json_data["alt"]
    res = asyncio.run(get_current_uv_related_info(lat, lng, alt, "forecasted_uv"))
    database = db("./sun_safe_DB.db")
    database.sqlite_db_insert("insert into Log (user_id, query_type, query_data, query_time) values ('%s', '%s', '%s', '%s')" % ("1", "forecasted_uv" ,base64.b64encode(str(res).encode("utf-8")).decode('utf-8'), time.time()))
    return str(res).encode("utf-8")

@app.route('/api/get_uv_protection_window', methods=['POST'])
def uv_protection_window():
    json_data = request.json
    lat = json_data["lat"]
    lng = json_data["lng"] 
    alt = json_data["alt"]
    res = asyncio.run(get_current_uv_related_info(lat, lng, alt, "uv_protection_window"))
    database = db("./sun_safe_DB.db")
    database.sqlite_db_insert("insert into Log (user_id, query_type, query_data, query_time) values ('%s', '%s', '%s', '%s')" % ("1", "uv_protection_window" ,base64.b64encode(str(res).encode("utf-8")).decode('utf-8'), time.time()))
    return str(res).encode("utf-8")
  
if __name__ == "__main__":
    app.run(threaded=True, host="0.0.0.0", port=11730)